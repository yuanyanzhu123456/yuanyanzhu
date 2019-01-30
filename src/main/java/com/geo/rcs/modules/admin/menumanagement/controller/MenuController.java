package com.geo.rcs.modules.admin.menumanagement.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.admin.menumanagement.entity.Menu;
import com.geo.rcs.modules.admin.menumanagement.service.MenuService;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月5日 上午10:14:41 
 */
@RestController
@RequestMapping("/operate/menu")
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;

	/**
	 * 查询全部菜单类型
	 *
	 * @param request
	 * @param response
	 * @param
	 */
	@RequestMapping("/selectList")
	@RequiresPermissions("operate:menu:list") // 权限管理
	public void list(HttpServletRequest request, HttpServletResponse response, Menu menu) {
		try {

			this.sendData(request, response, menuService.findByPage(menu, 1));
			this.sendOK(request, response);
		} catch (Exception e) {
			LogUtil.error("查询全部菜单类型", menu.toString(),getGeoUser().getName(), e);

			this.sendError(request, response,StatusCode.SERVER_ERROR.getMessage());
		}
	}

	/**
	 * 查询菜单列表,树形菜单
	 *
	 * @param request
	 * @param response
	 * @param 
	 */
	@RequestMapping("/list")
	@RequiresPermissions("operate:menu:list") // 权限管理
	public void listSelect(HttpServletRequest request, HttpServletResponse response, Menu menu) {
		try {
			if(menu.getPageSize()==null){
				this.sendData(request, response, menuService.findByPage(menu, 1));

			}else {
				this.sendData(request, response, menuService.findByPage(menu, 0));

			}
			this.sendOK(request, response);
		} catch (Exception e) {
			LogUtil.error("查询菜单列表,树形菜单", menu.toString(),getGeoUser().getName(), e);
			this.sendError(request, response,StatusCode.SERVER_ERROR.getMessage());
		}
	}

	@SysLog("添加菜单")
	@RequestMapping("/save")
	@RequiresPermissions("operate:menu:save")
	public void save(Menu menu, HttpServletRequest request, HttpServletResponse response) {
		ResultType resultType = ValidateNull.check(menu, NotNull.RequestType.NEW);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
		}
		try {
			menuService.save(menu);
			this.sendOK(request, response);// code:200表示成功
		} catch (Exception e) {
			LogUtil.error("添加菜单", menu.toString(),getGeoUser().getName(), e);
			this.sendError(request, response,StatusCode.SERVER_ERROR.getMessage());
		}
	}

	/**
	 * 修改菜单配置
	 */
	@SysLog("修改菜单")
	@RequestMapping("/update")
	@RequiresPermissions("operate:menu:update")
	public void updateScene(@Validated Menu menu, HttpServletRequest request, HttpServletResponse response,
			Approval approval) {


		try {
			

			menuService.updateMenu(menu);
			this.sendOK(request, response);
		} catch (Exception e) {
			LogUtil.error("修改菜单配置", menu.toString(),getGeoUser().getName(), e);

			this.sendError(request, response,StatusCode.SERVER_ERROR.getMessage());
		}
	}

	/**
	 * 删除菜单
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("operate:menu:delete")
	public void deleteRoster(Long id, HttpServletRequest request, HttpServletResponse response) {
		try {
			menuService.delete(id);
			this.sendOK(request, response);

		} catch (Exception e) {
			LogUtil.error("删除菜单", id.toString(),getGeoUser().getName(), e);

			this.sendError(request, response, "删除失败！");
		}

	}

}
