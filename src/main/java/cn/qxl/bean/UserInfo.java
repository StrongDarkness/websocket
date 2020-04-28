package cn.qxl.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
     /** 用户基本信息表*/
    private Long id;

     /** 用户ID*/
    private String userId;

     /** 用户姓名*/
    private String userName;

     /** 用户密码*/
    private String password;

     /** 支付密码*/
    private String payPassword;

     /** 用户昵称*/
    private String nickName;

     /** 用户头像*/
    private String headImg;

     /** 用户手机*/
    private String phone;

     /** 用户手机唯一标识*/
    private String phoneId;

     /** 身份证号*/
    private String idCardNo;

     /** 实名状态： 0、未实名; 1、已实名;*/
    private String realNameState;

     /** 用户拥有的角色，关联角色表ID,可多个，格式如：1==2==3*/
    private String roles;

     /** 创建人*/
    private String createUser;

     /** 更新人*/
    private String updateUser;

     /** 创建时间*/
     @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

     /** 更新时间*/
     @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

     /** 0:逻辑数据删除 1:逻辑数据存在*/
    private String isInUse;
}