package com.geo.rcs.modules.event.customerEvent.service;

import java.util.List;
import java.util.Map;

import com.geo.rcs.modules.event.customerEvent.entity.CustomerEventEntry;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.PageInfo;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年4月11日 下午6:59:42 
 */
public interface CustomerEventService {
	   

    /**
     * 分页查询
     * @return
     */
    PageInfo<CustomerEventEntry> findByPage(Map<String, Object> map, int flag);

    /**
     * 获取客户下拉菜单
     * @return
     */
	List<SysUser> getCustomerType();

	/**
	 * 获取明细
	 * @param map
	 * @return
	 */

	CustomerEventEntry getTotal(Map<String, Object> map);
}

