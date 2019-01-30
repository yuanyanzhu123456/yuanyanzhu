package com.geo.rcs.modules.batchtest.uploadfile.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.geo.rcs.modules.batchtest.ExcelImportUtils;
import com.geo.rcs.modules.batchtest.batchTask.dao.BatchTaskMapper;
import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;
import com.geo.rcs.modules.batchtest.uploadfile.dao.MessageMapper;
import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;
import com.geo.rcs.modules.batchtest.uploadfile.service.UploadExcelService;
import com.github.pagehelper.util.StringUtil;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月26日 下午4:47
 */
@Service
public class UploadExcelServiceImpl implements UploadExcelService {
	/**
	 * 上传excel文件到临时目录后并开始解析
	 * 
	 * @param fileName
	 * @param file
	 * @param userName
	 * @return
	 */
	public String uploadFileDir = "E:\\fileupload\\batchTask\\";
	@Autowired
	private BatchTaskMapper batchMapper;
	@Autowired
	private MessageMapper messageMapper;

	@Override
	public String batchImport(String fileName, MultipartFile mfile, BatchTask batchTask) {

		File uploadDir = new File(uploadFileDir);
		// 创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		// 新建一个文件
		String filename = mfile.getOriginalFilename();

		File tempFile = new File(uploadFileDir + mfile.getOriginalFilename());
		// 初始化输入流
		InputStream is = null;
		try {
			// 将上传的文件写入新建的文件中
			mfile.transferTo(tempFile);

			// 根据新建的文件实例化输入流
			is = new FileInputStream(tempFile);

			// 根据版本选择创建Workbook的方式
			Workbook wb = null;
			// 根据文件名判断文件是2003版本还是2007版本
			if (ExcelImportUtils.isExcel2007(fileName)) {
				wb = new XSSFWorkbook(is);
				return readExcelValue(wb, tempFile);
			} else if (ExcelImportUtils.isExcel2003(fileName)) {
				wb = new HSSFWorkbook(is);
				return readExcelValue(wb, tempFile);
			} else {
				// wb = new XSSFWorkbook(is);
				// return readExcelValue(wb, userName, tempFile);

				return readCsvValue(tempFile, batchTask);

			}
			// 根据excel里面的内容读取知识库信息
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		return "导入出错！请检查数据格式！";
	}
	@Transactional
	private String readCsvValue(File tempFile, BatchTask batchTask) {
		BufferedReader br = null;
		// 错误信息接收器
		String errorMsg = "导入失败,";
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(tempFile));
			br = new BufferedReader(new InputStreamReader(in, "utf-8"));// 这里如果csv文件编码格式是utf-8,改成utf-8即可
			String firstLine = br.readLine();
			ArrayList<String> parmNamelist = new ArrayList<String>();
			String[] split = firstLine.split(",");
			for (int i = 0; i < split.length; i++) {
				if (StringUtil.isEmpty(split[i])) {
					return errorMsg += "下载的模板不匹配,请联系管理员";
				}
				parmNamelist.add(split[i]);
			}
			String line = null;
			br.readLine();// 中文标题不读取
			int y = 2;// 行
			String brs = "<br/>";
			ArrayList<MessageItem> messageLIst = new ArrayList<>();
			while ((line = br.readLine()) != null) // 读取到的内容给line变量
			{
				MessageItem messageItem = new MessageItem();

				String rowMessage = "";
				split = line.split(",");
				HashMap<String, Object> parmMap = new HashMap<>();
				for (int i = 0; i < split.length; i++) {
					if (StringUtil.isEmpty(split[i])) {
						rowMessage += "第" + (i + 1) + "列数据不能为空";
						continue;
					}
					parmMap.put(parmNamelist.get(i), split[i]);
				}
				// =====最后一列为空,说明后面少参数
				if (split.length < parmNamelist.size()) {
					for (int i = split.length; i < parmNamelist.size(); i++) {
						rowMessage += "第" + (i + 1) + "列数据不能为空";
					}
				}
				// =====
				if (!"".equals(rowMessage)) {
					errorMsg += brs + "第" + (y - 1) + "行" + rowMessage;
				}
				y++;
				messageItem.setStatus(0);
				messageItem.setApiParmMap(parmMap.toString());
				messageLIst.add(messageItem);
			}
			if (errorMsg.length() == 5) {
				// 校验成功,先把批次任务存到数据库然后保存批次明细
				batchTask.setTestCount(messageLIst.size());
				batchTask.setComplete(0);
				batchTask.setStatus(0);
				batchTask.setPriorityLevel(0);
				batchMapper.insertBatch(batchTask);
				Integer batchId = batchTask.getId();
				String rulesId = batchTask.getRulesId();
				Integer level = batchTask.getPriorityLevel();
				for (int i = 0; i < messageLIst.size(); i++) {
					messageLIst.get(i).setBatchId(batchId);
					messageLIst.get(i).setRulesId(rulesId);
					messageLIst.get(i).setPriorityLevel(level);
					
				}
				messageMapper.insertList(messageLIst);
				return "导入成功";
			} else {
				return errorMsg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					e.printStackTrace();
				}
			}
		}
		return errorMsg;
		// TODO Auto-generated method stub
	}

	/**
	 * 解析Excel里面的数据
	 * 
	 * @param wb
	 * @return
	 */
	private String readExcelValue(Workbook wb, File tempFile) {

		// 错误信息接收器
		String errorMsg = "导入失败,";
		// 得到第一个shell
		Sheet sheet = wb.getSheetAt(0);
		// 得到Excel的行数
		int totalRows = sheet.getPhysicalNumberOfRows();
		// 总列数
		int totalCells = 0;
		// =========
		if (totalRows == 2 || totalRows == 1) {
			errorMsg += "内容不能为空!";
		}
		Row rowFirst = sheet.getRow(0);
		totalCells = rowFirst.getPhysicalNumberOfCells();
		// name,cid,phone
		ArrayList<String> listParmName = new ArrayList<String>();
		String parmName = null;
		for (int i = 0; i < totalCells; i++) {
			Cell cell = rowFirst.getCell(i);
			parmName = cell.getStringCellValue();
			if (StringUtils.isEmpty(parmName)) {
				errorMsg += "标题不能为空!";
			}
			listParmName.add(parmName);
		}
		// =========
		// // 得到Excel的列数(前提是有行数)，从第二行算起
		// if (totalRows >= 2 && sheet.getRow(1) != null) {
		// totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
		// }
		List<MessageItem> messageList = new ArrayList<MessageItem>();
		MessageItem message;

		String br = "<br/>";

		// 循环Excel行数,从第二行开始。标题不入库
		for (int r = 2; r < totalRows; r++) {
			String rowMessage = "";
			Row row = sheet.getRow(r);
			if (row == null) {
				errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
				continue;
			}
			message = new MessageItem();

			String parmValue = "";
			String answer = "";

			HashMap<String, Object> parmMap = new HashMap<>();
			// 循环Excel的列
			for (int c = 0; c < totalCells; c++) {
				Cell cell = row.getCell(c);
				if (null != cell) {
					// ==========
					cell.setCellType(Cell.CELL_TYPE_STRING);
					parmValue = cell.getStringCellValue();
					if (StringUtils.isEmpty(parmValue)) {
						rowMessage += "第" + (c + 1) + "列不能为空!";
					}
					parmMap.put(listParmName.get(c), parmValue);
				} else {
					rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
				}
				// ==========

			}
			message.setApiParmMap(parmMap.toString());
			// 拼接每行的错误提示
			if (!StringUtils.isEmpty(rowMessage)) {
				errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
			} else {
				messageList.add(message);
			}
		}

		// 删除上传的临时文件
		if (tempFile.exists()) {
			tempFile.delete();
		}

		return errorMsg;
	}

	private void saveUserKnowledge(MessageItem userKnowledgeBase, String userName) {
		// TODO Auto-generated method stub
		System.out.println();
	}
}
