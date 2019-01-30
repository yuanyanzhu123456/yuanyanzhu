package com.geo.rcs.common.schedule;

import com.geo.rcs.common.exception.GeoException;
import com.geo.rcs.common.util.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 执行定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public class ScheduleRunnable implements Runnable {

	private Object  target;

	private Method  method;

	private Long jobId;

	private Long    userId;

	public ScheduleRunnable(String beanName, String methodName,Long jobId,Long userId) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtils.getBean(beanName);
		this.jobId = jobId;
		this.userId = userId;

		if(jobId != null){
			this.method = target.getClass().getDeclaredMethod(methodName,Long.class,Long.class);
		}else{
			throw new GeoException("执行定时任务失败,任务编号为空", 1);
		}
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if(jobId != null && userId != null){
				method.invoke(target,jobId,userId);
			}
			else{
				throw new GeoException("执行定时任务失败,任务编号为空", 1);
			}
		}catch (Exception e) {
			throw new GeoException("执行定时任务失败", e);
		}
	}

}
