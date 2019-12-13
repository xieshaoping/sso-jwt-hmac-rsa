package com.hellokoding.sso.resource;

import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author XieShaoping
 */
public class JwtUtil {
    private static final String REDIS_SET_ACTIVE_SUBJECTS = "active-subjects";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static String generateToken(String signingKey, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, signingKey);

        String token = builder.compact();

        RedisUtil.INSTANCE.sadd(REDIS_SET_ACTIVE_SUBJECTS, subject);

        return token;
    }

    static String parseToken(HttpServletRequest httpServletRequest, String jwtTokenCookieName, String signingKey){
        String token = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
        if(token == null) {
            return null;
        }
        String subject = null;
        try {
            subject = Jwts.parser()
                    .setSigningKey(signingKey)
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
        if (subject!=null&&!RedisUtil.INSTANCE.sismember(REDIS_SET_ACTIVE_SUBJECTS, subject)) {
            return null;
        }

        return subject;
    }

    static void invalidateRelatedTokens(HttpServletRequest httpServletRequest) {
        RedisUtil.INSTANCE.srem(REDIS_SET_ACTIVE_SUBJECTS, (String) httpServletRequest.getAttribute("username"));
    }
}

