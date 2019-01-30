package com.geo.rcs.modules.admin.customerstatistics.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.admin.customerstatistics.dao.CustomerStatisticsMapper;
import com.geo.rcs.modules.admin.customerstatistics.service.CustomerStatisticsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月2日 下午4:32:53 
 */
@Service
public class CustomerStatisticsServiceImpl implements CustomerStatisticsService {
	 @Autowired
	 CustomerStatisticsMapper customerMapper;
	@Override
	public Page<EventEntry> findAllByPage(EventEntry entry) {
		// TODO Auto-generated method stub
		PageHelper.startPage(entry.getPageNo(), entry.getPageSize());
		Page<EventEntry> findAllByPage = customerMapper.findAllByPage(entry);
		return findAllByPage;
	}

}
