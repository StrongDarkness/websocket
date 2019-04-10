package cn.qxl.Service.Impl;


import cn.qxl.Bean.AdminMenu;
import cn.qxl.Bean.AdminUser;
import cn.qxl.Bean.SysPermission;
import cn.qxl.Service.AdminUserService;
import cn.qxl.Service.Base.BaseService;
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
