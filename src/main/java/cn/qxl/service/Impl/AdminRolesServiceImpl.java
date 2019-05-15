package cn.qxl.service.Impl;

import cn.qxl.bean.SysRole;
import cn.qxl.service.AdminRolesService;
import cn.qxl.service.Base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created by qiu on 2019/2/18.
 */
@Service
public class AdminRolesServiceImpl extends BaseService implements AdminRolesService {
    @Override
    public SysRole getSysRoleById(Long id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }
}
