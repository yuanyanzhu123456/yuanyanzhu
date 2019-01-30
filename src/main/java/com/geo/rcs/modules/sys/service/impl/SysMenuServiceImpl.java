package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.modules.sys.dao.SysMenuMapper;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.service.SysMenuService;
import com.geo.rcs.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/10/19 09:45
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {
	@Autowired
	private SysMenuMapper sysMenuMapper;
	@Autowired
	private SysUserService sysUserService;
	
	@Override
	public List<SysMenu> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenu> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		List<SysMenu> userMenuList = new ArrayList<>();
		for(SysMenu menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenu> queryListParentId(Long parentId) {
		return sysMenuMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenu> queryNotButtonList() {
		return sysMenuMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenu> getUserMenuList(Long userId,String userType) {
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}
	
	@Override
	public SysMenu queryObject(Long menuId) {
		return sysMenuMapper.queryObject(menuId);
	}

	@Override
	public List<SysMenu> queryList(Map<String, Object> map) {
		return sysMenuMapper.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysMenuMapper.queryTotal(map);
	}

	@Override
	public void save(SysMenu menu) {
		sysMenuMapper.save(menu);
	}

	@Override
	public void update(SysMenu menu) {
		sysMenuMapper.update(menu);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] menuIds) {
		sysMenuMapper.deleteBatch(menuIds);
	}
	
	@Override
	public List<SysMenu> queryUserList(Long userId) {
		return sysMenuMapper.queryUserList(userId);
	}

	@Override
	public Integer selectRulesNumByUserId(Long uniqueCode) {
		return sysMenuMapper.selectRulesNumByUserId(uniqueCode);
	}

	@Override
	public Integer selectApprovalNumByUserId(Long uniqueCode) {
		return sysMenuMapper.selectApprovalNumByUserId(uniqueCode);
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenu> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenu> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList){
		List<SysMenu> subMenuList = new ArrayList<SysMenu>();
		for(SysMenu entity : menuList){
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){//目录
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		return subMenuList;
	}
}
