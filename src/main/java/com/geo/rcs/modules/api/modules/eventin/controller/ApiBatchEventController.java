package com.geo.rcs.modules.api.modules.eventin.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.api.annotation.LoginUser;
import com.geo.rcs.modules.engine.service.EngineService;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.source.service.SourceService;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.modules.eventin.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 上午11:59
 */
@RestController
@RequestMapping("/api/batch")
public class ApiBatchEventController extends BaseController {
    @Autowired
    private EventService eventService;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private EngineService engineService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private EngineInterService engineInterService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static List<String[]> writeContent = new ArrayList<String[]>();

    @SysLog("进件接口调用（规则校验）")
    @PostMapping("/entry")
    public Geo eventEntry(@RequestBody Map<String, Object> map, @LoginUser SysUser user, @RequestParam("fileName") MultipartFile file) {


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Geo.ok();
    }

}