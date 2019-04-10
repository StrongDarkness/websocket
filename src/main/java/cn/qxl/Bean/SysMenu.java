package cn.qxl.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu {
     /** 后台栏目表,总后台和代理后台通用*/
    private Integer id;

     /** 上级id*/
    private Integer parnetId;

     /** 栏目名称*/
    private String menuName;

     /** 小图标*/
    private String menuIcon;

     /** 跳转页面*/
    private String menuLink;

     /** 排序*/
    private Integer menuSort;

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