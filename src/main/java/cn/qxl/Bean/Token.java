package cn.qxl.Bean;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by qiu on 2018/10/29.
 */
public class Token implements AuthenticationToken {
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
}
