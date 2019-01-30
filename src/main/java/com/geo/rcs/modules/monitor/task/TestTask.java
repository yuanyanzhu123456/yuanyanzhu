package com.geo.rcs.modules.monitor.task;

import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 *
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Component("testTask")
@EnableScheduling
public class TestTask implements Job {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SysUserService sysUserService;
	
	public void test(String params){
		logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
		System.out.printf("我是带参数的test方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SysUser user = sysUserService.selectByPrimaryKey(1L);
		System.out.println(ToStringBuilder.reflectionToString(user));
		
	}
	
	
	public void test2() throws Exception {
		System.out.printf("我是不带参数的test2方法，正在被执行");
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.printf("每10秒执行1次，正在被执行");
	}

	public static void main(String[] args) {
		// 字符串转数组 java.lang.String
		String str = "[0,1,2,3,4,5]";
		String[] arr = str.replace("[","").replace("]","").split(","); // 用,分割
		System.out.println(Arrays.toString(arr)); // [0, 1, 2, 3, 4, 5]
	}
}
