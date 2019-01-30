package com.geo.rcs.modules.admin.adminIndex.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.admin.adminIndex.service.StatisticInfoService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.LoginLogService;
import com.github.pagehelper.Page;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/10/25  15:01
 **/
@RestController
@RequestMapping("/crm/user")
public class AdminStatisticUserInfoController extends BaseController {
    @Autowired
    private StatisticInfoService statisticInfoService;
    @Autowired
    private LoginLogService loginLogService;



    /**
     * 统计客户的决策集数、规则集数、用户数等
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping("/info")
    public String countSingleCustomInfo(HttpServletRequest request, HttpServletResponse response, Long id){
        if (id == null){
            this.sendError(request,response,"id 不能为空");
            return null;
        }
        try{
            Map<Object,Object> result = statisticInfoService.getSingleCustomInfo(id);
            JSONObject jsonObject = JSONObject.fromObject(result);
            return jsonObject.toString();
        }catch(Exception e){
            e.printStackTrace();
            LogUtil.error("统计客户具体信息失败","",getGeoUser().getName(),e);
            return StatusCode.ERROR.getMessage();
        }
    }

    /**
     * 获取指定客户下的用户列表
     * @param request
     * @param response
     * @param sysUser
     */
    @RequestMapping("/list")
    public void getEmployerList(HttpServletRequest request,HttpServletResponse response,SysUser sysUser){
        if (sysUser.getId() == null){
            this.sendError(request,response,"id不能为空");
            return;
        }else{
            sysUser.setUniqueCode(sysUser.getId());
        }
        try{
            Page<SysUser> result = statisticInfoService.findEmployerListByPage(sysUser);
            this.sendDataWhereDateToString(request,response,new PageInfo<>(result));
        }catch(Exception e){
            e.printStackTrace();
            if (e instanceof ServiceException){
                LogUtil.error("获取客户下用户列表失败","",getGeoUser().getName(),e);
                this.sendError(request,response,StatusCode.SERVER_ERROR.getMessage());
            }else{
                LogUtil.error("获取客户下用户列表失败","",getGeoUser().getName(),e);
                this.sendError(request,response,"获取客户下用户列表"+StatusCode.ERROR.getMessage());
            }
        }
    }

    /**
     * 员工登陆次数统计排行
     * @param num
     * @param id
     * @return
     */
    @PostMapping("/logEmp")
    public Geo logCountByEmp(Integer num,Long id){
        try {
            //参数传入
            if (id != null && num != null) {
                //设置查询参数
                Map<String,Object> map = new HashedMap();
                map.put("id",id);
                map.put("num",num);

                return Geo.ok("员工登录次数排行和总数查询").put("data",loginLogService.logCountByEmp(map));
            }
            return Geo.error("传入参数不能为空，获取数据失败");
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.error("top10员工登录次数",id.toString(),getGeoUser().getName(),e);
            return Geo.error(StatusCode.ERROR.getMessage());
        }

    }


}
