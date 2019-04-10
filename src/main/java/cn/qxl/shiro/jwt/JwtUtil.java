package cn.qxl.shiro.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by qiu on 2018/10/29.
 */
public class JwtUtil {
    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token
     * @param username
     * @param password
     * @return
     */
    public static boolean verify(String token, String username, String password, String userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);//密码加密
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username)
                    .withClaim("userId", userId).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

    }

    /**
     * 获取解密后的用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取解密后的用户id
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param username
     * @param password
     * @return
     */
    public static String sign(String username, String password, String userId) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);//密码加密
            return JWT.create().withClaim("username", username)
                    .withClaim("userId", userId)
                    .withExpiresAt(date).sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
