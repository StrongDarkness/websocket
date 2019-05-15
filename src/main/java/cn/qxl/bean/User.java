package cn.qxl.bean;

import org.springframework.stereotype.Repository;

/**
 * Created by qiu on 2018/10/29.
 */
@Repository
public class User {
    private String username;
    private String password;
    private int status;
    private String phone;
    //private List<UserRole> roleList;
    private String role;
    private String permission;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    //    public List<UserRole> getRoleList() {
//        return roleList;
//    }
//
//    public void setRoleList(List<UserRole> roleList) {
//        this.roleList = roleList;
//    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", phone='" + phone + '\'' +
//                ", roleList=" + roleList +
                '}';
    }
}
