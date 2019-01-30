package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 菜单管理
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/18 15:58
 */
@Mapper
@Component(value = "sysMenuMapper")
public interface SysMenuMapper extends BaseMapper<SysMenu> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenu> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenu> queryNotButtonList();
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenu> queryUserList(Long userId);

    Integer selectApprovalNumByUserId(Long uniqueCode);

	Integer selectRulesNumByUserId(Long uniqueCode);
}
