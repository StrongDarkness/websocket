package cn.qxl.mapper;

import cn.qxl.bean.AdminUser;

import java.util.List;

public interface AdminUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    List<AdminUser> getAdminUser(AdminUser au);

    AdminUser getAdminUserByUserName(String username);
}