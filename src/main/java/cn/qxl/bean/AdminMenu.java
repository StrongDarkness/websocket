package cn.qxl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMenu {
     /** 管理员菜单关联表*/
    private Long id;

     /** 管理员角色id*/
    private Long adminRoleId;

     /** 管理员角色名*/
    private String adminRoleName;

     /** 菜单id用，分割 */
    private String menuId;

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