/*
package com.geotmt.common.excel;

import com.geo.springbootmybatis.model.User;
import com.geo.springbootmybatis.util.BlankUtil;
import com.geo.springbootmybatis.util.DateUtil;
import com.geo.springbootmybatis.util.ExcelUitl;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;


*/
/**
 * @author guoyujie
 * @date 2016年11月25日
 * @version 2.0
 *//*

public class ExportExcel{

	*/
/**
	 * 财务应收导出，及样式
	 * @author guoyujie
	 * @dataTime 2016年11月25日下午5:06:36
	 * @param dataList
	 * @param fileName
	 * @param response
	 * @param request
	 *//*


	private final static String PATTERN = "yyyyMMddHHmmss";


	public  static void ReceivableExportExcel(Collection<User> dataList, HttpServletResponse response, HttpServletRequest request) {

		if(BlankUtil.isBlank(dataList)){
			return;
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.RED.index);
		XSSFRow row = sheet.createRow(0);
		XSSFRow lastRow = sheet.createRow(dataList.size() + 1);
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFCellStyle lastStyle = workbook.createCellStyle();
		lastStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		lastStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		lastStyle.setFont(font);
		for(int i = 0; i < 15; i++){
			XSSFCell Total = lastRow.createCell(i);
			sheet.setColumnWidth(i, 4000);
			if(i == 7)
				Total.setCellValue("总计");
			Total.setCellStyle(lastStyle);
		}
		XSSFCell associatedLineNumberSum = lastRow.createCell(8);
		associatedLineNumberSum.setCellFormula("SUM(I2:I"+(dataList.size()+1)+")");
		associatedLineNumberSum.setCellStyle(lastStyle);
		XSSFCell taxPayableSum = lastRow.createCell(9);
		taxPayableSum.setCellFormula("SUM(J2:J"+(dataList.size()+1)+")");
		taxPayableSum.setCellStyle(lastStyle);
		XSSFCell incrementTaxRateSum = lastRow.createCell(11);
		incrementTaxRateSum.setCellFormula("SUM(M2:M"+(dataList.size()+1)+")");
		incrementTaxRateSum.setCellStyle(lastStyle);
		XSSFCell actualPayableSum = lastRow.createCell(13);
		actualPayableSum.setCellFormula("SUM(N2:N"+(dataList.size()+1)+")");
		actualPayableSum.setCellStyle(lastStyle);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		ExcelUitl.exportExcels(dataList, response, request, DateUtil.dateToString(new Date(), PATTERN), row, sheet, style, workbook);
	}

	*/
/**
	 * 财务应付导出，及样式
	 * @author jianghu
	 * @dataTime 2016年12月9日上午10:45:12
	 * @param dataList
	 * @param response
	 * @param request
	 *//*

	public  static void PayableExportExcel(Collection<?> dataList, HttpServletResponse response, HttpServletRequest request) {

		if(BlankUtil.isBlank(dataList)){
			return;
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.RED.index);
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow lastRow = sheet.createRow(dataList.size()+1);
		XSSFCellStyle lastStyle = workbook.createCellStyle();
		lastStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		lastStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		lastStyle.setFont(font);
		for(int i = 0; i < 13; i++){
			XSSFCell Total = lastRow.createCell(i);
			sheet.setColumnWidth(i, 4000);
			if(i == 7)
				Total.setCellValue("总计");
			Total.setCellStyle(lastStyle);
		}
		XSSFCell taxPayableSum = lastRow.createCell(8);
		taxPayableSum.setCellFormula("SUM(I2:I"+(dataList.size()+1)+")");
		taxPayableSum.setCellStyle(lastStyle);
		XSSFCell taxSum = lastRow.createCell(10);
		taxSum.setCellFormula("SUM(K2:K"+(dataList.size()+1)+")");
		taxSum.setCellStyle(lastStyle);
		XSSFCell actualPayble = lastRow.createCell(11);
		actualPayble.setCellFormula("SUM(L2:L"+(dataList.size()+1)+")");
		actualPayble.setCellStyle(lastStyle);
		XSSFRow row = sheet.createRow(0);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		ExcelUitl.exportExcels(dataList, response, request, DateUtil.dateToString(new Date(), PATTERN), row, sheet,
				style, workbook);
	}
}
*/
