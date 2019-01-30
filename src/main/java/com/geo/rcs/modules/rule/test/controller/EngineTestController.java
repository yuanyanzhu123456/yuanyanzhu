package com.geo.rcs.modules.rule.test.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.rule.test.entity.EngineTest;
import com.geo.rcs.modules.rule.test.service.EngineTestService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.test.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月04日 上午10:59
 */
@RestController
@RequestMapping("/rule/test")
public class EngineTestController extends BaseController{

    @Autowired
    private EngineTestService engineTestService;
    /**
     * 查询规则测试列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param engineTest
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:test:list")//权限管理
    public void getRuleSetList(HttpServletRequest request, HttpServletResponse response, EngineTest engineTest) {
        try {
                engineTest.setUniqueCode(getUserId());

                this.sendData(request, response, new PageInfo<>(engineTestService.findByPage(engineTest)));

                this.sendOK(request, response);
        } catch (ServiceException e) {
            //logger.error("获取列表失败！", e);
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 删除规则测试
     */
    @RequestMapping("/delete")
    public void deleteRuleTest(@RequestBody Long id, HttpServletRequest request, HttpServletResponse response){
        try {

            engineTestService.delete(id);

            this.sendOK(request, response);

        } catch (ServiceException e) {
            //logger.error("获取列表失败！", e);
            this.sendError(request, response, "删除失败！");
        }

    }
}
