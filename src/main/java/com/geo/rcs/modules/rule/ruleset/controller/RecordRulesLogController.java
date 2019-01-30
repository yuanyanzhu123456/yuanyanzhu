package com.geo.rcs.modules.rule.ruleset.controller;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.rule.ruleset.service.RecordRulesLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月12日 下午4:20
 */
@RestController
@RequestMapping("ruleSet/record")
public class RecordRulesLogController {

    @Autowired
    private RecordRulesLogService recordRulesLogService;

    /**
     * 历史记录恢复
     * @param logId
     * @return
     */
    @RequestMapping("/revert")
    public Geo recordRevert(Long logId){

        return Geo.ok();
    }
}
