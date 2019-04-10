package cn.qxl.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {
     /** 系统管理员用户表*/
    private Long id;

     /** 管理员用户名*/
    private String adminName;

     /** 管理员密码*/
    private String adminPassword;

     /** 角色id*/
    private String roleId;

     /** 邮箱地址*/
    private String email;

     /** 电话号码*/
    private String phoneNum;

     /** 是否允许登录 0-允许 1-不允许*/
    private Integer loginFlag;

     /** 创建人*/
    private String createUser;

     /** 更新人*/
    private String updateUser;

     /** 创建时间*/
    private Date createTime;

     /** 更新时间*/
    private Date updateTime;

     /** 0:逻辑数据删除 1:逻辑数据存在*/
    private String isInUse;
}