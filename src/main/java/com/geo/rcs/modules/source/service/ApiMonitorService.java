package com.geo.rcs.modules.source.service;

import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 12:12 2018/7/18
 */
public interface ApiMonitorService {
     List<String> getRulesSourceData(List<String> interList, String parameters, Long userId, Integer type) throws Exception;
}
