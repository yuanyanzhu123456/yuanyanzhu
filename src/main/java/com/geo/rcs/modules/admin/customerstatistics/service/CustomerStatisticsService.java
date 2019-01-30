package com.geo.rcs.modules.admin.customerstatistics.service;


import com.geo.rcs.modules.event.entity.EventEntry;
import com.github.pagehelper.Page;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月2日 下午4:23:02 
 */
public interface CustomerStatisticsService {

	Page<EventEntry>  findAllByPage(EventEntry entry);

}
