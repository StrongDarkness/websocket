package cn.qxl.service.Impl;


import cn.qxl.bean.AdminMenu;
import cn.qxl.bean.AdminUser;
import cn.qxl.bean.SysPermission;
import cn.qxl.service.AdminUserService;
import cn.qxl.service.Base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiu on 2019/1/18.
 */
@Service
public class AdminUserServiceImpl extends BaseService implements AdminUserService {

    @Override
    public AdminUser getAdminUserByUserName(String username) {
        return adminUserMapper.getAdminUserByUserName(username);
    }

    @Override
    public List<AdminUser> getAdminUser(AdminUser au) {
        return adminUserMapper.getAdminUser(au);
    }

    @Override
    public AdminMenu getAdminMenuById(Long id) {
        return adminMenuMapper.getAdminMenuByRoleId(id);
    }

    @Override
    public SysPermission getSysPermissionById(Long id) {
        return sysPermissionMapper.selectByPrimaryKey(id);
    }
}
