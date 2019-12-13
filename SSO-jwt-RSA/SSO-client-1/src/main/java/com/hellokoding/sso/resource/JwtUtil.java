package com.hellokoding.sso.resource;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author XieShaoping
 */
public class JwtUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String REDIS_SET_ACTIVE_SUBJECTS = "active-subjects";


    static String parseToken(HttpServletRequest httpServletRequest,
                             String jwtTokenCookieName,
                             String publicKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String token = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
        if (token == null) {
            return null;
        }
        String subject = null;
        PublicKey publicKey = getPublicKey(publicKeyStr);
        try {
            subject = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody() //获取Claims
                    .getSubject();
        } catch (ExpiredJwtException e) {

            logger.error("过期异常");
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的Jwt异常");
        } catch (MalformedJwtException e) {
            logger.error("格式错误的Jwt异常");
        } catch (SignatureException e) {
            logger.error("签名异常");
        } catch (IllegalArgumentException e) {
            logger.error("非法参数异常");
        }
        //验证判断标准
        if (subject != null && !RedisUtil.INSTANCE.sismember(REDIS_SET_ACTIVE_SUBJECTS, subject)) {
            return null;
        }
        return subject;
    }

    static void invalidateRelatedTokens(HttpServletRequest httpServletRequest) {
        RedisUtil.INSTANCE.srem(REDIS_SET_ACTIVE_SUBJECTS, (String) httpServletRequest.getAttribute("username"));
    }

    /**
     * @author XieShaoping
     * @description
     * @date 2019/12/12
     * @param publicKeyBase64 需要转化为base64的公钥字符串
     */
    private static PublicKey getPublicKey(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pem = publicKeyBase64
                .replaceAll("-*BEGIN.*KEY-*", "")
                .replaceAll("-*END.*KEY-*", "");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pem));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
        logger.info(publicKey.toString());
        return publicKey;
    }

}

