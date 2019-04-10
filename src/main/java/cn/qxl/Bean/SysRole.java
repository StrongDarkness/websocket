package cn.qxl.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {
     /** 系统角色表*/
    private Integer roleId;

     /** 角色名*/
    private String roleName;

     /** 角色描述*/
    private String roleDesc;

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

     /** 权限id用，分割*/
    private String permissionId;
}