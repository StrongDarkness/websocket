package cn.qxl.service;


import cn.qxl.bean.AdminMenu;
import cn.qxl.bean.AdminUser;
import cn.qxl.bean.SysPermission;

import java.util.List;

/**
 * 管理员用户管理
 * Created by qiu on 2019/1/18.
 */
public interface AdminUserService {

    AdminUser getAdminUserByUserName(String username);

    List<AdminUser> getAdminUser(AdminUser au);

    AdminMenu getAdminMenuById(Long id);

    SysPermission getSysPermissionById(Long id);
}
