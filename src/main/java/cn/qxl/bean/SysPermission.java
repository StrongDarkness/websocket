package cn.qxl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {
     /** 用户权限表，用于区分管理员权限，类似（客服，财务，管理员）*/
    private Long id;

     /** 权限id*/
    private Integer permissionId;

     /** 权限名*/
    private String permissionName;

     /** 权限描述*/
    private String permissionInfo;

     /** 版本*/
    private Integer ver;

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