package com.geo.rcs.modules.batchtest.uploadfile.service;

import org.springframework.web.multipart.MultipartFile;

import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;



/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月18日 上午10:25:00
 */
public interface UploadExcelService {
	public String batchImport(String fileName,MultipartFile mfile,BatchTask batchTask);
}
