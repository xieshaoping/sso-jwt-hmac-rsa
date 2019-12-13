package com.hellokoding.sso.auth;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.io.IOException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/**
 * @author XieShaoping
 */
public class JwtUtil {

    private static final String REDIS_SET_ACTIVE_SUBJECTS = "active-subjects";

    private static String caUrl = "C:\\Users\\Administrator\\Desktop\\key.txt";

    public static String generateToken(String privateKeyStr, String subject) throws Exception {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //过期时间,设置3天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 3);
        Map map = new HashMap();
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        final int keySize = 2048;
        keyPairGenerator.initialize(keySize);
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        //下面就是在为payload添加各种标准声明和私有声明
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setSubject(subject)        //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setExpiration(calendar.getTime()) //设置过期时间
                .setHeaderParams(map)
                .setId("jwt-id")
                .signWith(SignatureAlgorithm.RS256, privateKey);//设置签名使用的签名算法和签名使用的秘钥,指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了
        String token = builder.compact();
        RedisUtil.INSTANCE.sadd(REDIS_SET_ACTIVE_SUBJECTS, subject);
        return token;
    }

    /**
     * @author XieShaoping
     * @description 根据生成的私钥字符串，封装成PrivateKey对象
     * @date 2019/12/12
     * @param privateKeyBase64 需要被转化成base64的字符串
     */
    private static PrivateKey getPrivateKey(String privateKeyBase64) {
        //去除头部尾部注释
        String privKeyPEM = privateKeyBase64
                .replaceAll("-*BEGIN*KEY-*", "")
                .replaceAll("-*END*KEY-*", "");
        byte[] encoded = Base64.decodeBase64(privKeyPEM);
        try {
            DerInputStream derReader = new DerInputStream(encoded);
            DerValue[] seq = derReader.getSequence(0);
            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not read private key");
            }
            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger primeP = seq[4].getBigInteger();
            BigInteger primeQ = seq[5].getBigInteger();
            BigInteger expP = seq[6].getBigInteger();
            BigInteger expQ = seq[7].getBigInteger();
            BigInteger crtCoeff = seq[8].getBigInteger();
            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp,
                    primeP, primeQ, expP, expQ, crtCoeff);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(keySpec);
        } catch (IOException | GeneralSecurityException | java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

