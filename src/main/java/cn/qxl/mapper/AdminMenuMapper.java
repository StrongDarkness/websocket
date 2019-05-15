package cn.qxl.mapper;


import cn.qxl.bean.AdminMenu;

public interface AdminMenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminMenu record);

    int insertSelective(AdminMenu record);

    AdminMenu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminMenu record);

    int updateByPrimaryKey(AdminMenu record);

    AdminMenu getAdminMenuByRoleId(long id);
}