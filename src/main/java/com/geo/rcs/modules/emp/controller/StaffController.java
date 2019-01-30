package com.geo.rcs.modules.emp.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import com.geo.rcs.modules.emp.entity.Staff;
import com.geo.rcs.modules.emp.service.StaffService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.SysUserService;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月26日 下午4:47
 */
@RestController
@RequestMapping("/emp")
public class StaffController extends BaseController {

	@Autowired
	private StaffService staffService;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 前台查询员工列表（模糊，分页）
	 *
	 * @param request
	 * @param response
	 * @param sysUser
	 */
	@RequestMapping("/list")
	@RequiresPermissions("emp:list")
	public void getStaffList(HttpServletRequest request, HttpServletResponse response, SysUser sysUser) {
		try {
			// 添加unique_code （客户唯一标识）
			sysUser.setUniqueCode(getUser().getUniqueCode());
			this.sendData(request, response, new PageInfo<>(sysUserService.findByPage(sysUser)));
			this.sendOK(request, response);
		} catch (Exception e) {
			this.sendError(request, response, "获取列表失败！");
			LogUtil.error("前台查询员工列表（模糊，分页）", sysUser.toString(),getUser().getName(), e);
		}
	}

	/**
	 * 后台查询员工列表（模糊，分页）
	 *
	 * @param request
	 * @param response
	 * @param staff
	 */
	@RequestMapping("/geoList")
	@RequiresPermissions("emp:list")
	public void getGeoStaffList(HttpServletRequest request, HttpServletResponse response, Staff staff) {
		try {
			// 添加unique_code （客户唯一标识）
			staff.setUniqueCode(getGeoUser().getUniqueCode());
			this.sendData(request, response, new PageInfo<>(staffService.findByPage(staff)));
			this.sendOK(request, response);
		} catch (Exception e) {
			this.sendError(request, response, "获取列表失败！");
			LogUtil.error("后台查询员工列表（模糊，分页）", staff.toString(),getGeoUser().getName(), e);

		}
	}

	/**
	 * 前台添加员工
	 */
	@SysLog("添加员工")
	@RequestMapping("/save")
	@RequiresPermissions("emp:save")
	public void save(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
		ValidatorUtils.validateEntity(sysUser, AddGroup.class);
		ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.NEW);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
		}
		try {
			if (sysUserService.queryByUserName(sysUser.getUsername()) != null) {
				this.sendError(request, response, "用户名已存在！");
				return;
			} else if (getUser().getRoleId() == null) {
				this.sendError(request, response, "角色不能为空！");
			} else if (getUser().getRoleId() > sysUser.getRoleId()) {
				this.sendErrNp(request, response, "权限不足！");
			} else {
				// 添加unique_code （客户唯一标识）
				sysUser.setUniqueCode(getUser().getUniqueCode());
				sysUser.setCreater(getUser().getName());
				sysUser.setCompany(getUser().getName());
				sysUserService.save(sysUser);
				this.sendOK(request, response);
			}
		} catch (Exception e) {
			this.sendError(request, response, "添加员工失败！");
			LogUtil.error("前台添加员工", sysUser.toString(),getUser().getName(), e);

		}
	}

	/**
	 * 后台添加员工
	 */
	@SysLog("添加员工")
	@RequestMapping("/geoSave")
	@RequiresPermissions("emp:save")
	public void geoSave(Staff staff, HttpServletRequest request, HttpServletResponse response) {
		ResultType resultType = ValidateNull.check(staff, NotNull.RequestType.NEW);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
			return;
		}
		try {
			if (staffService.usernameUnique(staff.getUsername())!=null || staffService.queryByName(staff.getName()) != null) {
				this.sendError(request, response, "用户名或姓名已存在！");
				return;
			}
			// 添加unique_code （客户唯一标识）
			staff.setUniqueCode(getGeoUser().getUniqueCode());
			staff.setCreater(getGeoUser().getName());
			staff.setCompany(getGeoUser().getName());
			staffService.save(staff);
			this.sendOK(request, response);
		} catch (Exception e) {
			this.sendError(request, response, "添加员工失败！");
			LogUtil.error("后台添加员工", staff.toString(),getGeoUser().getName(), e);

		}
	}

	/**
	 * 前台修改员工
	 */
	@SysLog("修改员工")
	@RequestMapping("/update")
	@RequiresPermissions("emp:update")
	public void update(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
		ValidatorUtils.validateEntity(sysUser, UpdateGroup.class);
		ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
		}
		try {
			if (getUser().getRoleId() > sysUser.getRoleId()) {
				this.sendError(request, response, "权限不足！");
			} else if (sysUser.getId().equals(getUserId())) {
				this.sendError(request, response, "本人不能进行此操作！");
			} else {
				SysUser sysUserUnique = sysUserService.queryByUserName(sysUser.getUsername());
				if (sysUserUnique != null && !sysUserUnique.getId().equals(sysUser.getId())) {
					this.sendError(request, response, "用户名已存在！");
					return;
				}

				sysUserService.updateUserNoCu(sysUser);
				this.sendOK(request, response);
			}
		} catch (Exception e) {
			this.sendError(request, response, "更新员工失败！");
			LogUtil.error("前台修改员工", sysUser.toString(),getUser().getName(), e);

		}
	}

	/**
	 * 前台修改员工启用／禁用
	 */
	@SysLog("修改员工")
	@RequestMapping("/updateStatus")
	@RequiresPermissions("sys:user:updateStatus")
	public void updateStatus(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
		ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
		}
		try {
			if (getUser().getRoleId() > sysUser.getRoleId()) {
				this.sendError(request, response, "权限不足！");
			} else if (sysUser.getId().equals(getUserId())) {
				this.sendError(request, response, "本人不能进行此操作！");
			} else {
				sysUserService.updateUserNoCu(sysUser);
				this.sendOK(request, response);
			}
		} catch (Exception e) {
			this.sendError(request, response, "更新员工失败！");
			LogUtil.error("前台修改员工启用／禁用", sysUser.toString(),getUser().getName(), e);

		}
	}

	/**
	 * 后台修改密码
	 */
	@RequestMapping("/geoUpdatePassword")
	@RequiresPermissions("emp:update")
	public void geoUpdatePassword(@Validated Staff staff, HttpServletRequest request, HttpServletResponse response) {

		ResultType resultType = ValidateNull.check(staff, NotNull.RequestType.UPDATE);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
		}
		try {
			if (getGeoUser().getRoleId() > staff.getRoleId()) {
				this.sendError(request, response, "权限不足！");
			} else if (staff.getPassword().equals(staff.getPasswordTwo()) == false) {
				this.sendError(request, response, "两次密码不一致！");
			} else {
				staffService.updateStaff(staff);
				this.sendOK(request, response);
			}
		} catch (Exception e) {
			this.sendError(request, response, "更新员工失败！");
			LogUtil.error("后台修改密码", staff.toString(),getGeoUser().getName(), e);

		}
	}

	/**
	 * 后台修改员工
	 */
	@SysLog("修改员工")
	@RequestMapping("/geoUpdate")
	@RequiresPermissions("emp:update")
	public void geoUpdate(@Validated Staff staff, HttpServletRequest request, HttpServletResponse response) {

		ResultType resultType = ValidateNull.check(getGeoUser(), NotNull.RequestType.UPDATE);
		if (ResultType.FAILD.equals(resultType)) {
			this.sendError(request, response, resultType.getMsg());
			return;
		} else if (staff.getRoleId() == null || staff.getId() == null) {
			this.sendError(request, response, "参数不齐全");
			return;
		}
		try {
			if (staff.getId().equals(getGeoId()) || staff.getId() == 1) {
				this.sendError(request, response, "本人不能进行此操作！");
			} else if (staff.getRoleId() < getGeoUser().getRoleId()) {
				this.sendError(request, response, "权限不足！");
			} else {
				Staff usernameUnique = staffService.usernameUnique(staff.getUsername());
				if (usernameUnique != null && !usernameUnique.getId().equals(staff.getId())) {
					this.sendError(request, response, "用户名已存在！");
					return;
				}
				staffService.updateStaff(staff);
				this.sendOK(request, response);
			}
		} catch (Exception e) {
			this.sendError(request, response, "更新员工失败！");
			LogUtil.error("后台修改员工", staff.toString(),getGeoUser().getName(), e);

		}
	}

	/**
	 * 获取客户端员工信息，用于回显数据
	 */
	@RequestMapping("/getClientInfo")
	public String getClientInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (id == null) {
				this.sendError(request, response, "id不能为空！");
			}
			Map<Object, Object> map = sysUserService.queryUserInfoById(id);
			JSONObject jsonObject = JSONObject.fromObject(map);
			return jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("获取客户端员工信息，用于回显数据", id.toString(),getUser().getName(), e);
			return StatusCode.ERROR.getMessage();
		}
		
	}

	/**
	 * 获取运营端员工信息，用于回显数据
	 */
	@RequestMapping("/getGeoInfo")
	public String getGeoInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (id == null) {
				this.sendError(request, response, "id不能为空！");
			}
			Staff staff = staffService.getStaffById(id);
			Map hashMap = new HashMap();
			hashMap.put("message", staff);
			JSONObject jsonObject = JSONObject.fromObject(hashMap);
			return jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("获取运营端员工信息，用于回显数据", id.toString(),getGeoUser().getName(), e);
			return StatusCode.ERROR.getMessage();
		}
	}

}
