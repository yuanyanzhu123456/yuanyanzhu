package com.geo.rcs.modules.geo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年10月19日 下午2:12
 */
@RequestMapping("/geo/stat")
@RestController
public class GeoStatisticsController {


    /**
     * 客户总量，登录次数，模型数量，进件数量统计
     * @param map
     * @param request
     * @param response
     */
    @RequestMapping("/statistics")
    public void countStatistics(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response){

    }
}
