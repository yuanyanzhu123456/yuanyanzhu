package com.geo.rcs.modules.api.modules.decision;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.api.annotation.LoginUser;
import com.geo.rcs.modules.decision.engine.DecisionEngine;
import com.geo.rcs.modules.decision.entity.DecisionForApi;
import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.decision.service.EngineDecisionService;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.sys.controller.SysLoginController;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月03日 上午11:48
 */
@RequestMapping("/api/decision")
@RestController
public class DecisionEngineApi extends BaseController {

    @Autowired
    private DecisionEngine decisionEngine;
    @Autowired
    private EngineDecisionService engineDecisionService;

    private static Logger logger = LoggerFactory.getLogger(DecisionEngineApi.class);

    @RequestMapping("/eventIn")
    public Geo eventIn(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response, @LoginUser SysUser user) {
        try {
            EngineDecision engineDecision = engineDecisionService.selectByPrimaryKey(Integer.valueOf(map.get("decisionId").toString()));
            if (ValidateNull.isNull(engineDecision)) {
                return Geo.error("决策为空");
            } else {
                DecisionForApi recursive = decisionEngine.recursive(user, request, engineDecision, map);
                return Geo.ok().put("data", recursive);
            }
        }catch (RcsException e) {
            logger.error("执行决策失败", map.toString(), user.getName(), e);
            return Geo.error(e.getCode(),e.getMsg());
        }catch (Exception e){
            logger.error("执行决策失败", map.toString(), user.getName(), e);
            return Geo.error("执行决策失败,请稍后重试");
        }
    }

}
