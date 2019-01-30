package com.geo.rcs.common.util;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.excel.ExcelColumn;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


/**
 * @author jianghu
 * @date 2016年11月25日
 * @version 2.0
 */
public class ExcelUitl {

	/**
	 * @throws InstantiationException
	 */
	private ExcelUitl() throws InstantiationException{

		throw new InstantiationException("Cannot be called by reflection");
	}

	/**
	 * @author jianghu
	 * @dataTime 2016年11月25日下午5:07:55
	 * @param dataList
	 * @param response
	 * @param request
	 * @param fileName
	 * @param row
	 * @param sheet
	 * @param style
	 * @param workbook
	 */
	public static void exportExcels(Collection<?> dataList, HttpServletResponse response, HttpServletRequest request, 
			String fileName, XSSFRow row, XSSFSheet sheet, XSSFCellStyle style, XSSFWorkbook workbook){

		if(BlankUtil.isBlank(dataList))
			return ;

		ServletOutputStream outputStream = null;
		try {
			BaseController.sendExcel(response, request, fileName);
			outputStream = response.getOutputStream();
			columnData(dataList, row, sheet, style);
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @author jianghu
	 * @dataTime 2016年11月25日下午5:07:52
	 * @param dataList
	 * @param row
	 * @param sheet
	 * @param style
	 */
	private static void columnData(Collection<?> dataList, XSSFRow row, XSSFSheet sheet, XSSFCellStyle style){

		StringBuilder headersBuilder = new StringBuilder();
		Iterator<?> iterator = dataList.iterator();
		Field[] fields = iterator.next().getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(ExcelColumn.class)){
				ExcelColumn columnName = field.getAnnotation(ExcelColumn.class);
				headersBuilder.append(columnName.value()+",");
			}
		}
		String[] headers = headersBuilder.toString().split(",");
		for (int i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(new XSSFRichTextString(headers[i]));
			cell.setCellStyle(style);
		}
		dataList(dataList, row, sheet, style);
	}

	/**
	 * @author jianghu
	 * @dataTime 2016年11月25日下午5:07:48
	 * @param dataList
	 * @param row
	 * @param sheet
	 * @param style
	 */
	private  static void dataList(Collection<?> dataList, XSSFRow row, XSSFSheet sheet, XSSFCellStyle style){

		Iterator<?> it = dataList.iterator();
		String textValue = null;
		int index = 0;
		int j = -1;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);   
			Object obj = it.next();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(!field.isAnnotationPresent(ExcelColumn.class))
					continue;
				j++;
				XSSFCell cell = row.createCell(j);
				try {
					Object value = getMethod(obj.getClass(), getGetMethod(field.getName())).invoke(obj);
					if(value instanceof Date)
						textValue = new SimpleDateFormat("yyyy-MM-dd").format((Date) value);
					else if(value instanceof BigDecimal){
						cell.setCellValue(((BigDecimal) value).doubleValue());
						cell.setCellStyle(style);
						continue;
					}else {
						if(value == null)
							continue;
						else
							textValue = value.toString();
					}
					cell.setCellValue(textValue);
					cell.setCellStyle(style);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			j = -1;
		}
	}

	/**
	 * @author jianghu
	 * @date 2016年12月23日下午1:39:43
	 * @param field
	 * @return
	 */
	private static String getGetMethod(String field) {

		return "get"+ field.substring(0, 1).toUpperCase()+ field.substring(1);
	}

	/**
	 * @author jianghu
	 * @date 2016年12月23日下午1:52:41
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	private static Method getMethod(Class<?> clazz, String methodName) {

		try {
			return clazz.getMethod(methodName);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
