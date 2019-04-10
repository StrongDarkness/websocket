package cn.qxl.Bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by qiu on 2018/12/27.
 */
public class chatBean {
    private String username;
    private String toUser;
    private String msg;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "chatBean{" +
                "username='" + username + '\'' +
                ", toUser='" + toUser + '\'' +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
