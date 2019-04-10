package cn.qxl.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户form账密登录的token
 * Created by qiu on 2019/1/14.
 */
public class PasswordToken extends UsernamePasswordToken {

    private String type = "formToken";//定义token类型

    public PasswordToken(String username, String password) {
        super(username, password);
    }

    public PasswordToken(String username, char[] password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public String getType() {
        return type;
    }
}
