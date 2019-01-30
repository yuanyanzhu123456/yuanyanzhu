package com.geo.rcs.modules.sys.controller;


import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.log.LogFactory;
import com.geo.rcs.common.util.DateOperateUtil;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.sys.entity.*;
import com.geo.rcs.modules.sys.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    public FieldService fieldService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CusVersionService cusVersionService;

    @Autowired
    private SysVersionService sysVersionService;

    @Autowired
    private SysVersionLogService sysVersionLogService;
    /**
     * 所有用户列表
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")//权限管理
    public void itemsPage(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, new PageInfo<>(sysUserService.findCustomerByPage(sysUser)));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取用户列表", sysUser.toString(), getUser().getName(), e);
        }
    }

    /**
     * 所有用户列表（增加版本信息）
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/listNew")
    @RequiresPermissions("sys:user:list")//权限管理
    public void itemsPageNew(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        try {
            Page<SysUser> result = sysUserService.findCustomerByPageNew(sysUser);
            this.sendDataWhereDateToString(request, response, new PageInfo<>(result));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取用户列表", sysUser.toString(), getUser().getName(), e);
        }
    }


    /**
     * 获取当前用户信息
     *
     */
    @RequestMapping("/userInfo")
    @RequiresPermissions("index:list")
    public Geo userInfo() {
        return Geo.ok().put("info", getUser());
    }

    /**
     * 前台获取登录的个人账户信息，所属公司主账户信息，关联验真账号信息
     */

    @RequestMapping("/info")
    public Geo info() {
        SysUser sysUser = new SysUser();
        Map<Object, Object> userInfoMap = new HashMap<>();
        sysUser.setUniqueCode(getUser().getUniqueCode());
        sysUser.setId(getUserId());
        SysUser activeUser = sysUserService.selectByPrimaryKey(sysUser.getId());
        SysUser masterUser = sysUserService.selectUniqueCode(sysUser.getUniqueCode());
        Customer customer = customerService.selectByUserId(sysUser.getUniqueCode());
        userInfoMap.put("activeUser", activeUser);
        userInfoMap.put("masterUser", masterUser);
        userInfoMap.put("customer", customer);
        return Geo.ok().put("userInfoMap", userInfoMap);
    }

    /**
     * 前台修改个人详情页
     */
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public void updateInfo(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (sysUser == null) {
                this.sendError(request, response, "参数为空");
            } else {
                sysUser.setId(getUserId());
                sysUserService.updateUserById(sysUser);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "修改信息失败！");
            LogUtil.error("修改个人详情", sysUser.toString(), getUser().getName(), e);
        }
    }

    /**
     * 前台修改验真账号
     */
    @RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
    public void updateCustomer(@RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (customer == null) {
                this.sendError(request, response, "参数为空");
            } else if (getUser().getRoleId() != 2) {
                this.sendError(request, response, StatusCode.DENIED.getMessage());
            } else {
                customer.setUserId(getUserId());
                sysUserService.updateCustomer(customer);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "修改信息失败！");
            LogUtil.error("修改验真账号", customer.toString(), getUser().getName(), e);
        }
    }

    /**
     * 后台添加客户
     */
    @SysLog("保存用户")
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions("sys:user:save")
    public void save(@RequestBody SysUser user, HttpServletRequest request, HttpServletResponse response) {

        ResultType resultType = ValidateNull.check(user, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            ValidatorUtils.validateEntity(user, AddGroup.class);
            ValidatorUtils.validateEntity(user.getCustomer(), AddGroup.class);
            if (sysUserService.usernameUnique(user.getName()) != null) {
                this.sendError(request, response, "客户名称已存在！");
            } else if (sysUserService.queryByUserName(user.getUsername()) != null) {
                this.sendError(request, response, "用户名不可重复！");
            } else {
                user.setCreater(getGeoUser().getName());
                sysUserService.saveCustomer(user);
                //code:200表示成功
                this.sendOK(request, response);
                //增加版本日志
                SysVersionLog sysVersionLog =new SysVersionLog();
                SysVersion sysVersion = sysVersionService.getVerInfoById(user.getVersionId());
                //记录日志
                sysVersionLog.setName(Constant.LogType.BUSSINESS.getMessage());
                sysVersionLog.setCreater(getGeoUser().getName());
                sysVersionLog.setCreateTime(new Date());
                sysVersionLog.setNewVersionId(user.getVersionId());
                sysVersionLog.setCustomerId(user.getId());
                sysVersionLog.setStatus(1);
                sysVersionLog.setType(1);
                sysVersionLog.setTimes(1);
                sysVersionLog.setPrice(1*sysVersion.getPrice());
                sysVersionLogService.createVersionLog(sysVersionLog);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "新增用户失败！");
            LogUtil.error("添加客户", user.toString(), getGeoUser().getName(), e);
        }
    }


    /**
     * 后台添加客户(增加版本信息)
     */
    @SysLog("保存用户")
    @RequestMapping(value = "/saveNew", method = RequestMethod.POST)
    @RequiresPermissions("sys:user:save")
    public void saveNew(@RequestBody SysUser user, HttpServletRequest request, HttpServletResponse response) {

        ResultType resultType = ValidateNull.check(user, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            ValidatorUtils.validateEntity(user, AddGroup.class);
            ValidatorUtils.validateEntity(user.getCustomer(), AddGroup.class);
            if (sysUserService.usernameUnique(user.getName()) != null) {
                this.sendError(request, response, "客户名称已存在！");
            } else if (sysUserService.queryByUserName(user.getUsername()) != null) {
                this.sendError(request, response, "用户名不可重复！");
            } else {

                user.setCreater(getGeoUser().getName());
                sysUserService.saveCustomer(user);

                CusVersion cusVersion = new CusVersion();
                cusVersion.setCreater(getGeoUser().getName());
                cusVersion.setCustomerId(sysUserService.queryByUserNameForId(user.getUsername()));
                cusVersion.setVersionId(user.getVersionId());
                cusVersionService.save(cusVersion);
                this.sendOK(request, response);//code:200表示成功

                //增加版本日志
                SysVersionLog sysVersionLog =new SysVersionLog();
                SysVersion sysVersion = sysVersionService.getVerInfoById(user.getVersionId());
                //记录日志
                sysVersionLog.setName(Constant.LogType.BUSSINESS.getMessage());
                sysVersionLog.setCreater(getGeoUser().getName());
                sysVersionLog.setCreateTime(new Date());
                sysVersionLog.setNewVersionId(user.getVersionId());
                sysVersionLog.setCustomerId(user.getId());
                sysVersionLog.setStatus(1);
                sysVersionLog.setType(1);
                sysVersionLog.setTimes(1);
                sysVersionLog.setPrice(1*sysVersion.getPrice());
                sysVersionLogService.createVersionLog(sysVersionLog);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "新增用户失败！");
            LogUtil.error("添加客户", user.toString(), getGeoUser().getName(), e);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendError(request, response, "新增用户失败！");
        }
    }


    /**
     * 修改前台客户 启用／禁用
     */
    @SysLog("修改客户")
    @RequestMapping("/updateStatus")
    public void updateStatus(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            sysUserService.updateUserNoCu(sysUser);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "修改客户失败！");
            LogUtil.error("修改客户", sysUser.toString(), getUser().getName(), e);
        }
    }

    /**
     * 获取员工信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        Map<Object, Object> map = sysUserService.queryUserInfoById(id);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject.toString();
    }

    /**
     * 获取员工信息，用于回显数据(增加版本信息)
     */
    @RequestMapping("/toUpdateNew")
    public String toUpdateNew(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        Map<Object, Object> map = sysUserService.queryUserInfoByIdNew(id);
        Long customerId = id;
        Long versionId = cusVersionService.queryVersionId(customerId);
        map.put("versionId", versionId);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject.toString();
    }


    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @RequiresPermissions("sys:user:update")
    public void updateUser(@RequestBody SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        ValidatorUtils.validateEntity(sysUser, UpdateGroup.class);
        ValidatorUtils.validateEntity(sysUser.getCustomer(), UpdateGroup.class);
        ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            SysUser sysUserUnique = sysUserService.usernameUnique(sysUser.getName());
            SysUser queryByUserName = sysUserService.queryByUserName(sysUser.getUsername());
            if (sysUserUnique != null && !sysUserUnique.getId().equals(sysUser.getId())) {
                this.sendError(request, response, "客户名称已存在！");
                return;
            } else if (queryByUserName != null && !queryByUserName.getId().equals(sysUser.getId())) {
                this.sendError(request, response, "用户名不可重复！");
                return;
            }
            sysUserService.updateUser(sysUser);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新用户失败！");
            LogUtil.error("修改用户", sysUser.toString(), getUser().getName(), e);
        }
    }

    /**
     * 修改用户（增加版本信息）
     */
    @SysLog("修改用户")
    @RequestMapping(value = "/updateNew", method = RequestMethod.POST)
    @RequiresPermissions("sys:user:update")
    public void updateUserNew(@RequestBody SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        ValidatorUtils.validateEntity(sysUser, UpdateGroup.class);
        ValidatorUtils.validateEntity(sysUser.getCustomer(), UpdateGroup.class);
        ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            SysUser sysUserUnique = sysUserService.usernameUnique(sysUser.getName());
            SysUser queryByUserName = sysUserService.queryByUserName(sysUser.getUsername());
            if (sysUserUnique != null && !sysUserUnique.getId().equals(sysUser.getId())) {
                this.sendError(request, response, "客户名称已存在！");
                return;
            } else if (queryByUserName != null && !queryByUserName.getId().equals(sysUser.getId())) {
                this.sendError(request, response, "用户名不可重复！");
                return;
            }
            sysUserService.updateUser(sysUser);

            CusVersion cusVersion = new CusVersion();
            cusVersion.setCustomerId(sysUser.getId());
            cusVersion.setVersionId(sysUser.getVersionId());

            CusVersion data = cusVersionService.selectByCustomId(sysUser.getId());
            if (data == null) {
                cusVersion.setCreater(getGeoUser().getName());
                cusVersionService.save(cusVersion);
            } else {
                cusVersionService.updateCusVersion(cusVersion);
            }
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新用户失败！");
            LogUtil.error("修改用户", sysUser.toString(), getUser().getName(), e);
        } catch (Exception e) {
            this.sendError(request, response, "更新用户失败！");
            LogUtil.error("修改用户", sysUser.toString(), getUser().getName(), e);

        }
    }


    /**
     * 前台修改用户密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @RequiresPermissions("sys:user:updatePassword")
    public void updatePassword(@Validated SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {

        ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            if (getUser().getRoleId() > sysUser.getRoleId()) {
                this.sendError(request, response, "权限不足！");
            } else if (sysUser.getPassword().equals(sysUser.getPasswordTwo()) == false) {
                this.sendError(request, response, "两次密码不一致！");
            } else {
                sysUserService.updateUserNoCu(sysUser);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            //logger.error("更新用户失败！", e);
            this.sendError(request, response, "修改密码失败！");
            LogUtil.error("修改用户密码", sysUser.toString(), getUser().getName(), e);
        }
    }

    /**
     * 运营端修改客户密码
     */
    @RequestMapping(value = "/updateCuPassword", method = RequestMethod.POST)
    @RequiresPermissions("emp:update")
    public void updateCuPassword(@Validated SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {

        ResultType resultType = ValidateNull.check(sysUser, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            if (sysUser.getPassword().equals(sysUser.getPasswordTwo()) == false) {
                this.sendError(request, response, "两次密码不一致！");
            } else {
                sysUserService.updateUserNoCu(sysUser);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            //logger.error("更新用户失败！", e);
            this.sendError(request, response, "更新用户失败！");
            LogUtil.error("修改客户密码", sysUser.toString(), getGeoUser().getName(), e);
        }
    }

    /**
     * 前台登陆的版本信息
     */
    @PostMapping("/version")
    @RequiresPermissions("index:list")
    public void sysVersion(HttpServletRequest request,HttpServletResponse response){
        try {

            if (getUserId() == null) {
                this.sendError(request, response, "用户登陆失效，请重新登录");
            }
            SysUser sysUser = getUser();
            Long id = sysUser.getUniqueCode();

            Map<String,Object> result = cusVersionService.getCusVersionInfo(id);
            if (result == null){
                this.sendError(request,response,"用户尚未绑定版本信息"+StatusCode.ERROR.getMessage());
            }else{
                this.sendData(request,response,result);
            }
        }catch (Exception e){

            e.printStackTrace();
            LogUtil.error("到期提醒", "", getUser().getName(), e);
            this.sendError(request, response, "版本到期提醒" + StatusCode.ERROR.getMessage());
        }
    }

    /**
     * 版本续期
     */
    @PostMapping("/renewal")
    @RequiresPermissions("sys:version:update")
    public void renewal(Long customerId,Long versionId,Integer num, HttpServletRequest request, HttpServletResponse response) {
        SysVersion sysVersion = sysVersionService.getVerInfoById(versionId);
        CusVersion cusVersion = cusVersionService.getCusInfoById(customerId);
        if (sysVersion.getActive() == 0) {
            this.sendError(request, response, "该版本没启用！");
            return;
        }
        //该用户无版本信息
        if (cusVersion == null) {
            try {
                CusVersion info = new CusVersion();
                info.setCreater(getGeoUser().getName());
                info.setUpdater(getGeoUser().getName());
                info.setCustomerId(customerId);
                info.setVersionId(versionId);
                cusVersionService.save(info);
                this.sendDataWhereDateToString(request, response, sysUserService.getUserAllInfo(info.getCustomerId()));
                //记录日志
                SysVersionLog s = new SysVersionLog();
                s.setName(Constant.LogType.BUSSINESS.getMessage());
                s.setCreater(getGeoUser().getName());
                s.setCreateTime(new Date());
                s.setNewVersionId(versionId);
                s.setCustomerId(customerId);
                s.setStatus(1);
                s.setType(1);
                s.setTimes(1);
                s.setPrice(1 * sysVersion.getPrice());
                sysVersionLogService.createVersionLog(s);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.error("添加版本", "", getUser().getName(), e);
                this.sendError(request, response, "添加版本失败" + StatusCode.ERROR.getMessage());
            }
        }
        //更改版本或续期
        try {
            SysVersionLog sysVersionLog = LogFactory.createVersionLog(Constant.LogType.BUSSINESS, cusVersion);
            Double oldVersionPrice = sysVersionService.getPriceById(cusVersion.getVersionId());
            Double newVersionPrice = sysVersion.getPrice();
            if (oldVersionPrice < newVersionPrice) {
                //升级
                sysVersionLog.setType(2);
            } else if (oldVersionPrice > newVersionPrice || oldVersionPrice.equals(newVersionPrice)) {
                //降级/续期
                sysVersionLog.setType(1);
            }

            sysVersionLog.setOldVersionId(cusVersion.getVersionId());
            if (num == null) {
                //如果该用户已经是该版本就报版本更改重复
                if(cusVersion.getVersionId().equals(versionId)){
                    this.sendError(request,response,"请勿更改重复版本！");
                    return;
                }
                sysVersionLog.setTimes(1);
                sysVersionLog.setPrice(1 * sysVersion.getPrice());
                Date newExpireTime = DateOperateUtil.addDate(new Date(),  (1 * sysVersion.getDay()), '+');
                cusVersion.setUpdater(getGeoUser().getName());
                cusVersion.setUpdateTime(new Date());
                cusVersion.setExpireTime(newExpireTime);
                cusVersion.setVersionId(versionId);
                cusVersionService.renewal(cusVersion);
                this.sendDataWhereDateToString(request, response, sysUserService.getUserAllInfo(customerId));
                //记录日志
                //判断价格高低决定是升级  降级/续期
                sysVersionLog.setTimes(1);
                sysVersionLog.setPrice(1*sysVersion.getPrice());
                sysVersionLog.setNewVersionId(versionId);
                sysVersionLogService.createVersionLog(sysVersionLog);
            } else {
                //续期
                if (num<=0 || num>10){
                    this.sendError(request,response,"参数输入错误！");
                    return;
                }
                Date newExpireTime = DateOperateUtil.addDate(cusVersion.getExpireTime(), (num * sysVersion.getDay()), '+');
                cusVersion.setUpdater(getGeoUser().getName());
                cusVersion.setUpdateTime(new Date());
                cusVersion.setExpireTime(newExpireTime);
                cusVersion.setVersionId(versionId);
                cusVersionService.renewal(cusVersion);
                this.sendDataWhereDateToString(request, response, sysUserService.getUserAllInfo(customerId));
                //记录日志
                sysVersionLog.setTimes(num);
                sysVersionLog.setPrice(num*sysVersion.getPrice());
                sysVersionLogService.createVersionLog(sysVersionLog);
            }
        }catch(Exception e){
            e.printStackTrace();
            LogUtil.error("版本续期", "", getUser().getName(), e);
            this.sendError(request, response, "版本续期失败" + StatusCode.ERROR.getMessage());
        }
    }




    }










