package com.geo.rcs.modules.decision.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.decision.entity.DecisionHistoryLog;
import com.geo.rcs.modules.decision.service.DecisionHistoryService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月03日 上午11:48
 */
@RequestMapping("/decision/history")
@RestController
public class DecisionHistoryController extends BaseController {

    @Autowired
    private DecisionHistoryService decisionHistoryService;

    private static Logger logger = LoggerFactory.getLogger(DecisionHistoryController.class);


    /**
     * 查询决策历史列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param decisionHistoryLog
     */
    @RequestMapping("/list")
    @RequiresPermissions("engine:decision:history")
    public void getDecisionHistoryList(HttpServletRequest request, HttpServletResponse response, DecisionHistoryLog decisionHistoryLog) {
        try {
            //添加unique_code （客户唯一标识）
            decisionHistoryLog.setUniqueCode(getUser().getUniqueCode());
            PageInfo<DecisionHistoryLog> pageInfo = new PageInfo<>(decisionHistoryService.findByPage(decisionHistoryLog));
            this.sendData(request, response, pageInfo);

            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取决策历史列表",decisionHistoryLog.toString(),getUser().getName(),e);
        }
    }

    /**
     * 历史记录详情
     *
     * @param id
     * @return
     */
    @PostMapping("/getHistoryDetail")
    @RequiresPermissions("decision:history:detail")
    public Geo getEntryDetail(Long id) {
        try {
            if (id != null) {
                DecisionHistoryLog decisionHistoryLog = decisionHistoryService.selectByPrimaryKey(id);
                return Geo.ok().put("decisionHistoryLog", decisionHistoryLog);
            }
            return Geo.error("编号不能为空，获取数据失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LogUtil.error("历史详情",id.toString(), getUser().getName(), e);
            e.printStackTrace();
            return  Geo.error();
        }

    }


}
