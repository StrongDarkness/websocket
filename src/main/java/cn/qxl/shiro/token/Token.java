package cn.qxl.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用于App登录的token
 * Created by qiu on 2018/10/29.
 */
public class Token implements AuthenticationToken {
    private String type = "jwtToken";//token类型
    private String token;

    public Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getType() {
        return type;
    }
}
