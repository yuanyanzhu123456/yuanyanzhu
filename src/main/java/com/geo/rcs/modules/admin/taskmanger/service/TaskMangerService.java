package com.geo.rcs.modules.admin.taskmanger.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.admin.taskmanger.entity.QueueDetail;
import com.geo.rcs.modules.admin.taskmanger.entity.TaskManger;
import com.geo.rcs.modules.jobs.entity.JobRegister;
import com.github.pagehelper.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月6日 上午10:14:41
 */
public interface TaskMangerService {


	TaskManger overView(String role) throws ServiceException, ParseException;
	QueueDetail quueDetail(String role) throws Exception;
	List<JobRegister> getRegistrars(String role)throws Exception;
	TaskManger delData() throws ServiceException, ParseException;

	List<Map<Object,Object>> taskDetail(String unit, int interval, int size,String role) throws ServiceException;


}
