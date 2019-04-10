package cn.qxl.Service.Base;

import cn.qxl.Mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qiu on 2019/4/10.
 */
@Slf4j
@Service
public class BaseService {
    /**
     * 管理员
     */
    @Autowired
    protected AdminUserMapper adminUserMapper;
    /**
     * 管理员菜单
     */
    @Autowired
    protected AdminMenuMapper adminMenuMapper;
    /**
     * 系统权限
     */
    @Autowired
    protected SysPermissionMapper sysPermissionMapper;
    /**
     * 系统菜单列表
     */
    @Autowired
    protected SysMenuMapper sysMenuMapper;
    /**
     * 系统角色
     */
    @Autowired
    protected SysRoleMapper sysRoleMapper;
}
