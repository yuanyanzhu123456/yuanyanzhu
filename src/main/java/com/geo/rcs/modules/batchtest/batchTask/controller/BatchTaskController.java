package com.geo.rcs.modules.batchtest.batchTask.controller;

import java.util.Date;

import org.apache.xmlbeans.impl.validator.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.pdf.PdfUtil;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;
import com.geo.rcs.modules.batchtest.batchTask.service.BatchTaskService;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月23日 下午3:46:50 
 */


@RestController
@RequestMapping("/batchTask")
public class BatchTaskController extends BaseController{

	@Autowired
	private BatchTaskService batchTaskService;
	private static final Logger log = LoggerFactory.getLogger(PdfUtil.class);

	@PostMapping(value = "/start")
	public Geo start(BatchTask batchTask) throws Exception {
		
		try {
			if (batchTask.getId()==null||batchTask.getId()==0) {
				return Geo.error(StatusCode.PARAMS_ERROR.getCode(),"请先选择批次!");
			}
			BatchTask batchTaskRecord=batchTaskService.selectByPrimaryKey((long)batchTask.getId());
			if (batchTaskRecord!=null) {
				Integer status = batchTaskRecord.getStatus();
				if (status!=0) {
					return Geo.error("该批次已测试过");
				}
			}
			batchTask.setSubmitUser(getGeoUser().getName());
			batchTask.setSubmitTimeTamp(System.currentTimeMillis());
			batchTask.setStatus(1);
			batchTaskService.start(batchTask);
		} catch (Exception e) {
			
			log.error("事件:某公司:某用户:开启批次测试失败!"+e.getMessage());;
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
		}
		return Geo.ok("开启成功");
	}
}
