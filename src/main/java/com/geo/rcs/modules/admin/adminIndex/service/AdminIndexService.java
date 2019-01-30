package com.geo.rcs.modules.admin.adminIndex.service;

import java.util.List;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 18:56 2018/8/27
 */
public interface AdminIndexService {
    Map<String,Map<String,Object>> getDateOvew();
    Map<String,Object> dataOverviewTotal();
    List<Map<String,Object>> detailStatistics(Map<String, String> parmMap);

}
