package com.geo.rcs.modules.batchtest.uploadfile.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.modules.batchtest.ExcelImportUtils;
import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;
import com.geo.rcs.modules.batchtest.uploadfile.service.UploadExcelService;


/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月18日 上午10:25:00
 */
@RestController
@RequestMapping("/uploadExcel")
public class UploadExcelController extends BaseController {// 导入

	@Autowired
	private UploadExcelService uploadExcelService;

	@PostMapping(value = "batchImport")
	public Geo batchImportUserKnowledge(@RequestParam(value = "filename") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response, BatchTask batchTask) throws IOException {
		ValidatorUtils.validateEntity(batchTask, AddGroup.class);
		// 判断文件是否为空
		if (file == null) {
			return Geo.error("文件不能为空！");
		}

		// 获取文件名
		String fileName = file.getOriginalFilename();

		// 验证文件名是否合格
		if (!ExcelImportUtils.validateExcel(fileName)) {
			return Geo.error("文件必须是excel格式或者csv格式！");
		}

		// 进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
		long size = file.getSize();
		if (StringUtils.isEmpty(fileName) || size == 0) {
			return Geo.error("文件不能为空！");
		}
		Long uniqueCode = getGeoUser().getUniqueCode();
		batchTask.setUniqueCode(uniqueCode);
		// 批量导入
		String message = uploadExcelService.batchImport(fileName, file,batchTask);
		if (message.contains("导入失败")) {

			return Geo.error(message);
		}
		return Geo.ok(message);
	}
}
