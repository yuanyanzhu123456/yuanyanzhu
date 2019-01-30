package com.geo.rcs.modules.sys.controller;

import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.ShiroService;
import com.geo.rcs.modules.sys.service.SysMenuService;
import com.geo.rcs.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/1/16 14:42
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;
	@Autowired
	private SysUserService sysUserService;

	/**
	 * 导航菜单
	 */
	@RequestMapping("/nav")
	public Geo nav(){
		List<SysMenu> menuList = sysMenuService.getUserMenuList(getUserId(),getUserType());
		Set<String> permissions = shiroService.getUserPermissions(getUserId(), getUserType());
		return Geo.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 所有菜单列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenu> list(){
		List<SysMenu> menuList = sysMenuService.queryList(new HashMap<String, Object>());
		return menuList;
	}
	
	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public Geo select(){
		//查询列表数据
		List<SysMenu> menuList = sysMenuService.queryNotButtonList();
		
		//添加顶级菜单
		SysMenu root = new SysMenu();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return Geo.ok().put("menuList", menuList);
	}
	
	/**
	 * 菜单信息
	 */
	@RequestMapping("/info/{menuId}")

	public Geo info(@PathVariable("menuId") Long menuId){
		SysMenu menu = sysMenuService.queryObject(menuId);
		return Geo.ok().put("menu", menu);
	}

	/**
	 * 主页信息
	 */
	@RequestMapping("/msg")
	@RequiresPermissions("index:list")
	public Geo msg(){
		Integer rulesNum = sysMenuService.selectRulesNumByUserId(getUser().getUniqueCode());
		Integer approvalNum = sysMenuService.selectApprovalNumByUserId(getUser().getUniqueCode());
		SysUser sysUser = sysUserService.selectUniqueCode(getUser().getUniqueCode());
		return Geo.ok().put("rulesNum", rulesNum).put("approvalNum",approvalNum).put("sysUser",sysUser);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public Geo save(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);
		
		sysMenuService.save(menu);
		
		return Geo.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public Geo update(@RequestBody SysMenu menu){
		//数据校验
		verifyForm(menu);
				
		sysMenuService.update(menu);
		
		return Geo.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public Geo delete(long menuId){
		if(menuId <= 31){
			return Geo.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<SysMenu> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return Geo.error("请先删除子菜单或按钮");
		}

		sysMenuService.deleteBatch(new Long[]{menuId});
		
		return Geo.ok();
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenu menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RcsException("菜单名称不能为空");
		}
		
		if(menu.getParentId() == null){
			throw new RcsException("上级菜单不能为空");
		}
		
		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new RcsException("菜单URL不能为空");
			}
		}
		
		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenu parentMenu = sysMenuService.queryObject(menu.getParentId());
			parentType = parentMenu.getType();
		}
		
		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RcsException("上级菜单只能为目录类型");
			}
			return ;
		}
		
		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RcsException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
}
