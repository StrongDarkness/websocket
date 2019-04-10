package cn.qxl.Service.Impl;

import cn.qxl.Bean.SysRole;
import cn.qxl.Service.AdminRolesService;
import cn.qxl.Service.Base.BaseService;
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
