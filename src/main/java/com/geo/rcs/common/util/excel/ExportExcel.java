package com.geo.rcs.common.util.excel;

import com.geo.rcs.common.util.Encodes;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/11/15 20:23
 */
public class ExportExcel {

	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);

	private static final String TOAL = "总计";
	private static final String SUBTOAL = "合计";

	/**
	 * 工作薄对象
	 */
	private SXSSFWorkbook wb;

	/**
	 * 工作表对象
	 */
	private Sheet sheet;

	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;

	/**
	 * 当前行号
	 */
	private int rownum ;

	private int rownum1 = 20;
	private int fisrRowNUm = 0;

	/**
	 * 行下标记号
	 */
	private int rowsIdx = 6;

	public int getRowsIdx() {
		return this.rowsIdx;
	}

	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headers 表头数组
	 */
	public ExportExcel(String title, String[] headers) {
		initialize(title, Lists.newArrayList(headers));
	}

	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headers 表头数组
	 */
	public ExportExcel(String title, String[] headersParmsEn,String[] headersParmsCh) {
		initialize(title, Lists.newArrayList(headersParmsEn), Lists.newArrayList(headersParmsCh));
	}

	private  void initialize(String title, ArrayList<String> headerList, ArrayList<String> headerList2) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("sheet");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
		}
		setHeaders(headerList,true);
		setHeaders(headerList2,false);

	}

	private void setHeaders(ArrayList<String> headerList,boolean flag) {
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		if (flag){
		headerRow.setHeightInPoints(0);
		}else {
		headerRow.setHeightInPoints(16);
		}
		CellStyle textStyle = getColumTestStyle();

		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
			sheet.setDefaultColumnStyle(i, textStyle);
		}
		for (int i = 0, length = headerList.size(); i < length; i++) {
			int colWidth = sheet.getColumnWidth(i)*2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}
		if (flag){
		sheet.createFreezePane(0,2,0,2);
		}else {
		sheet.createFreezePane(0,3,0,3);
		}
		log.debug("Initialize success.");
	}

	public ExportExcel(String title, String[] headers, boolean flag) {
		initialize1(title, Lists.newArrayList(headers));
	}
	public ExportExcel(String title, String[] headers, int starRow) {
		initialize1(title, Lists.newArrayList(headers),starRow);
	}

	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	public ExportExcel(String title, List<String> headerList) {
		initialize(title, headerList);
	}

	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 *  @param  head01：表头第一行列名1
	 *  @param  head02: 表头第一列名2
	 *  @param head1：表头第二行列名
	 */
	public ExportExcel(String title, String[] head01, String[] head02, String[] head1, int num) {
		initialize(title,head01,head02,head1,num);
	}

	public ExportExcel(String title, String[] head01, String[] head02, String[] head1, int num, boolean flag) {
		initialize1(title,head01,head02,head1,num,flag);
	}

	static String[] concat(String[] a, String[] b) {
		String[] c= new String[a.length+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	private void initialize(String title, List<String> headerList) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("sheet");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
		}
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);

		CellStyle textStyle = getColumTestStyle();

		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
			sheet.setDefaultColumnStyle(i, textStyle);
		}
		for (int i = 0, length = headerList.size(); i < length; i++) {
			int colWidth = sheet.getColumnWidth(i)*2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}
		sheet.createFreezePane(0,2,0,2);
		log.debug("Initialize success.");
	}

	private void initialize1(String title, List<String> headerList) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("sheet");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum1++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), 0, headerList.size()-1));
		}
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum1++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}
		for (int i = 0, length = headerList.size(); i < length; i++) {
			int colWidth = sheet.getColumnWidth(i)*2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}
		log.debug("Initialize success.");
	}
	private void initialize1(String title, List<String> headerList,int startRow) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("sheet");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(fisrRowNUm++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), 0, headerList.size()-1));
		}
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(fisrRowNUm++);
		headerRow.setHeightInPoints(16);

		CellStyle textStyle = getColumTestStyle();
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
			sheet.setDefaultColumnStyle(i, textStyle);
		}
		for (int i = 0, length = headerList.size(); i < length; i++) {
			int colWidth = sheet.getColumnWidth(i)*2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}
		log.debug("Initialize success.");
	}

	private CellStyle getColumTestStyle() {
		CellStyle textStyle = wb.createCellStyle();
		DataFormat format = wb.createDataFormat();
		textStyle.setDataFormat(format.getFormat("@"));
		return textStyle;
	}


	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param  head01：表头第一行列名1
	 * @param  head02: 表头第一列名2
	 *  @param head1：表头第二行列名
	 *   @param num:表头中需要合并的行数
	 */
	private void initialize(String title,String[] head01,String[] head02,
							String[] head1,int num) {
		String[] head0 = concat(head01,head02);

		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("sheet");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), head0.length-1));
		}
		// Create header
		if (head0 == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);

		for (int i = 0; i < head0.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(head0[i]);
			cell.setCellStyle(styles.get("header"));
		}

		//动态合并单元格
		for (int i = 0; i < head01.length; i++) {
			sheet.addMergedRegion(new CellRangeAddress(1,2,i,i));
		}

		for (int i = 0; i < (head02.length/num); i++) {
			sheet.addMergedRegion(new CellRangeAddress(1,1,(head01.length+num*i),(head01.length+num-1+num*i)));
		}

		//设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		headerRow = sheet.createRow(rownum++);//因为下标从0开始，所以这里表示的是excel中的第3行
		for (int i = 0; i < head0.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			for (int j = 0; j < head1.length; j++) {
				cell = headerRow.createCell(j+head01.length);
				cell.setCellValue(head1[j]);
				cell.setCellStyle(styles.get("header"));
			}
		}
		sheet.createFreezePane(head01.length,3,head01.length,3);
	}

	private void initialize1(String title,String[] head01,String[] head02,
							 String[] head1,int num,boolean flag) {
		String[] head0 = concat(head01,head02);

		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("sheet");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum1++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			System.out.println(titleRow.getRowNum());
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), 0, head0.length-1));
		}
		// Create header
		if (head0 == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum1++);
		headerRow.setHeightInPoints(16);

		for (int i = 0; i < head0.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(head0[i]);
			cell.setCellStyle(styles.get("header"));
		}

		//动态合并单元格
		for (int i = 0; i < head01.length; i++) {
			sheet.addMergedRegion(new CellRangeAddress(21,22,i,i));
		}

		for (int i = 0; i < (head02.length/num); i++) {
			sheet.addMergedRegion(new CellRangeAddress(21,21,(head01.length+num*i),(head01.length+num-1+num*i)));
		}

		//设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
		headerRow = sheet.createRow(rownum1++);//因为下标从0开始，所以这里表示的是excel中的第3行
		for (int i = 0; i < head0.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			for (int j = 0; j < head1.length; j++) {
				cell = headerRow.createCell(j+head01.length);
				cell.setCellValue(head1[j]);
				cell.setCellStyle(styles.get("header"));
			}
		}
	}

	/**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);

		return styles;
	}

	/**
	 * 添加一行
	 * @return 行对象
	 */
	public Row addRow(){
		return sheet.createRow(rownum++);
	}

	public Row addRow1(){
		return sheet.createRow(rownum1++);
	}
	public Row addRow2(){
		return sheet.createRow(fisrRowNUm++);
	}


	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val){
		return this.addCell(row, column, val, 2, Class.class);
	}

	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
		Cell cell = row.createCell(column);
		String cellFormatString = "@";
		try {
			if(val == null){
				cell.setCellValue("");
			}else if(fieldType != Class.class){
				cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
			}else{
				if(val instanceof String) {
					cell.setCellValue((String) val);
				}else if(val instanceof Integer) {
					cell.setCellValue((Integer) val);
					cellFormatString = "0";
				}else if(val instanceof Long) {
					cell.setCellValue((Long) val);
					cellFormatString = "0";
				}else if(val instanceof Double) {
					cell.setCellValue((Double) val);
					cellFormatString = "0.00";
				}else if(val instanceof Float) {
					cell.setCellValue((Float) val);
					cellFormatString = "0.00";
				}else if(val instanceof Date) {
					cell.setCellValue((Date) val);
					cellFormatString = "yyyy-MM-dd";
				}else {
					cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
							"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
				}
			}
			if (val != null){
				CellStyle style = styles.get("data_column_"+column);
				if (style == null){
					style = wb.createCellStyle();
					style.cloneStyleFrom(styles.get("data"+(align>=1&&align<=3?align:"")));
					style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
					styles.put("data_column_" + column, style);
				}
				cell.setCellStyle(style);
			}
		} catch (Exception ex) {
			log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
			cell.setCellValue(val.toString());
			if (val != null){
				CellStyle style = styles.get("data_column_"+column);
				if (style == null){
					style = wb.createCellStyle();
					style.cloneStyleFrom(styles.get("data"+(align>=1&&align<=3?align:"")));
					style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
					styles.put("data_column_" + column, style);
				}
				cell.setCellStyle(style);
			}
		}
		return cell;
	}

	/**
	 * 插入图片（默认插入png格式图片）
	 * @param bt 图片流数据
	 * @param rowsIdx 图片开始行
	 */
	public void setPictuce( byte[] bt,int rowsIdx){

		ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
		anchor.setCol1(0); //图片开始列
		anchor.setRow1(0); //图片开始行
		anchor.setCol2(10); //图片结束列
		anchor.setRow2(20); //图片结束行

		Drawing patriarch = sheet.createDrawingPatriarch();

		patriarch.createPicture(anchor, wb.addPicture(bt,SXSSFWorkbook.PICTURE_TYPE_PNG));

		sheet.addMergedRegion(new CellRangeAddress(0,19,0,10));
	}

	/**
	 * 添加数据
	 * @return
	 */
	public ExportExcel setDataList(List<Map<String, Object>> listMap, String[] props) {
		List<List<Object>> lists = Lists.newArrayList();
		int index = 1;
		List<Object> listT = Lists.newArrayList();
		for (Map map : listMap) {
			if(!TOAL.equals(map.get("total"))){
				map.put("index", index);
				List<Object> list = Lists.newArrayList();
				for(int i=0; i<props.length; i++) {
					list.add(map.get(props[i]));
				}
				lists.add(list);
				index++;
			}else {
				for(int i=0; i<props.length; i++) {
					listT.add(map.get(props[i]));
				}
				listT.set(0, "总计");
				listT.set(1, "总计");
			}
		}
		lists.add(listT);
		for (List<Object> list : lists) {
			Row row = this.addRow1();
			for(int i = 0, length = list.size(); i < length; i++) {
				this.addCell(row, i, list.get(i));
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(rownum1-1,rownum1-1,0,1));
		return this;
	}
	/**
	 * 添加数据
	 *
	 * @return
	 */
	public ExportExcel setDataList2(List<Map<String, Object>> listMap, String[] props) {
		List<List<Object>> lists = Lists.newArrayList();
		int index = 1;
		List<Object> listT = Lists.newArrayList();
		for (Map map : listMap) {
			if (!TOAL.equals(map.get("total"))) {
				map.put("index", index);
				List<Object> list = Lists.newArrayList();
				for (int i = 0; i < props.length; i++) {
					list.add(map.get(props[i])==null?"":map.get(props[i]));
				}
				lists.add(list);
				index++;
			} else {
				for (int i = 0; i < props.length; i++) {
					listT.add(map.get(props[i]));
				}
				listT.set(0, "总计");
				listT.set(1, "总计");
			}
		}
		lists.add(listT);
		for (List<Object> list : lists) {
			Row row = this.addRow2();
			for (int i = 0, length = list.size(); i < length; i++) {
				this.addCell(row, i, list.get(i));
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(fisrRowNUm - 1, fisrRowNUm - 1, 0, 1));
		return this;
	}

	/**
	 * 添加数据
	 * @return
	 */
	public ExportExcel setDataList3(List<Map<String, Object>> listMap, String[] props,String[] columns,String[] headers) {
		List<List<Object>> lists = Lists.newArrayList();
		Double[] doubles = new Double[headers.length];

		for (int i = 0; i < columns.length; i++) {
			List<Object> list = Lists.newArrayList();
			list.add(columns[i]);

			HashMap<Integer, Object> indexMap = new HashMap<>();
			Object[] strings = new Object[headers.length];

			if(!SUBTOAL.equals(columns[i])) {
				Integer count1 = 0;
				Double count2 = 0d;

				for (Map<String, Object> map : listMap) {
					if(map.containsValue(columns[i]) && map.containsKey(props[0])){
						for (int j = 0; j < headers.length; j += 2) {
							if(map.get(props[0]).equals(headers[j])) {
								indexMap.put(j, map.get(props[1]) + "");
								indexMap.put(j + 1, map.get(props[2]) + "");

								count1 += NumberUtils.toInt(map.get(props[1])+"");
								count2 += NumberUtils.toDouble(map.get(props[2])+"");
							}
							if(SUBTOAL.equals(headers[j])){
								indexMap.put(j, count1);
								indexMap.put(j + 1, count2);
							}
						}
					}
				}
				for (Map.Entry<Integer, Object> entry : indexMap.entrySet()) {
					strings[entry.getKey()] = entry.getValue() + "";
				}
				List<Object> integers = Arrays.asList(strings);
				for (int k=0; k<integers.size(); k++) {
					if(k%2==0 && integers.get(k)!=null) {
						list.add(NumberUtils.toInt(integers.get(k)+""));
					}else if(k%2!=0 && integers.get(k)!=null){
						list.add(NumberUtils.toDouble(integers.get(k)+""));
					}else {
						list.add(integers.get(k));
					}
					doubles[k] = doubles[k]==null?0:doubles[k];
					doubles[k] += NumberUtils.toDouble(integers.get(k)+"");
				}
			}else {
				for (int m=0; m<doubles.length; m++) {
					list.add(doubles[m]);
				}
			}
			lists.add(list);
		}
		for (List<Object> list : lists) {
			Row row = this.addRow1();
			for(int ii = 0, length = list.size(); ii < length; ii++) {
				this.addCell(row, ii, list.get(ii));
			}
		}
		rowsIdx = lists.size();
		return this;
	}

	/**
	 * 添加数据
	 * @return
	 */
	public ExportExcel setDataList4(List<Map<String, Object>> mapList, String[] headers,String[][] columns,String[] props) {
		int statusCount = columns[1].length;
		List<List<Object>> lists = Lists.newArrayList();
		for (int i = 0; i < columns[0].length; i++) {

			for (int j = 0; j < columns[1].length; j++) {
				List<Object> list = Lists.newArrayList();
				list.add(columns[0][i]);
				list.add(columns[1][j]);
				list.add("");
				HashMap<Integer, Object> indexMap = new HashMap<>();
				Object[] strings = new Object[headers.length-3];
				for (Map<String,Object> map : mapList) {
					if(map.containsValue(columns[0][i]) && map.containsValue(columns[1][j])){
						for (int k = 3; k < headers.length; k++) {
							if (map.containsValue(headers[k])){
								indexMap.put(k-3,map.get(props[0]));
							}
						}
					}
				}

				for (Map.Entry<Integer,Object> entry: indexMap.entrySet()) {
					strings[entry.getKey()] = entry.getValue()+"";
				}
				List<Object> integers = Arrays.asList(strings);
				for (Object integer:integers) {
					if(integer != null) {
						list.add(NumberUtils.toInt(integer+""));
					}else {
						list.add(integer);
					}
				}
				lists.add(list);
			}
			sheet.addMergedRegion(new CellRangeAddress(2+statusCount*i,statusCount*(i+1)+1,0,0));
		}
		for (List<Object> list : lists) {
			Row row = this.addRow();
			for(int ii = 0, length = list.size(); ii < length; ii++) {
				this.addCell(row, ii, list.get(ii));
			}
		}
		rowsIdx = lists.size();
		return this;
	}

	/**
	 * 添加数据
	 * @return
	 */
	public ExportExcel setDataList(List<Map<String, Object>> listMap, String[] props,String[] columns,String[] headers) {
		List<List<Object>> lists = Lists.newArrayList();
		Double[] doubles = new Double[headers.length-1];
		for (int i = 0; i < columns.length; i++) {
			HashMap<Integer, Object> indexMap = new HashMap<>();
			List<Object> list = Lists.newArrayList();
			list.add(columns[i]);
			Object[] strings = new Object[headers.length-1];
			if(!SUBTOAL.equals(columns[i])){
				for (Map<String,Object> map : listMap) {
					if(map.values().contains(columns[i]) && map.keySet().contains(props[1])) {
						for (int j = 1; j < headers.length; j++) {
							if (map.get(props[1]).equals(headers[j])){
								indexMap.put(j-1,map.get(props[2]));
							}
						}
					}
				}
				for (Map.Entry<Integer,Object> entry: indexMap.entrySet()) {
					strings[entry.getKey()] = entry.getValue()+"";
				}
				List<Object> integers = Arrays.asList(strings);
				for (int k=0; k<integers.size(); k++) {
					if(integers.get(k) != null) {
						list.add(NumberUtils.toDouble(integers.get(k)+""));
					}else {
						list.add(integers.get(k));
					}
					doubles[k] = doubles[k]==null?0:doubles[k];
					doubles[k] += NumberUtils.toDouble(integers.get(k)+"");
				}
			}else {
				for (int m=0; m<doubles.length; m++) {
					list.add(doubles[m]);
				}
			}
			lists.add(list);
		}
		for (List<Object> list : lists) {
			Row row = this.addRow1();
			for(int ii = 0, length = list.size(); ii < length; ii++) {
				this.addCell(row, ii, list.get(ii));
			}
		}
		rowsIdx = lists.size();
		return this;
	}

	/**
	 * 添加数据
	 * @return
	 */
	public ExportExcel setDataList(List<Map<String, Object>> listMap, String[] props1, String[] props2, List<Integer> intList) {
		int index1 = 1, x = 2;
		for (int ii = 0; ii < intList.size(); ii++) {
			Integer size = intList.get(ii);
			List<List<Object>> lists = Lists.newArrayList();
			int index = 1;

			for (int k = 0; k < size; k++) {
				Map<String, Object> map = listMap.get(ii);
				map.put("index", index1);

				List<Object> list = Lists.newArrayList();
				for (int i = 0; i < props1.length; i++) {
					if (map.get(props1[i]) instanceof List) {
						ArrayList<HashMap> proList = (ArrayList<HashMap>) map.get(props1[i]);

						for (int l=0;l<size;l++) {
							HashMap map1 = proList.get(l);
							for (int j = 0; j < props2.length; j++) {
								if (index == l+1){
									list.add(map1.get(props2[j]));
								}else {
									break;
								}
							}
						}
					}else{
						list.add(map.get(props1[i]));
					}
				}
				lists.add(list);
				index++;
			}
			for (List<Object> list : lists) {
				Row row = this.addRow();
				for(int j = 0, length = list.size(); j < length; j++) {
					this.addCell(row, j, list.get(j));
				}
			}
			index1++;
			for (int i = 0; i < 5; i++) {
				sheet.addMergedRegion(new CellRangeAddress(x,x+size-1,i,i));
			}
			x += size;
			rowsIdx += lists.size();
		}
		return this;
	}
	/**
	 * 添加数据
	 * @param listMap
	 * @param props
	 * @param head02
	 * @param lowprops
	 * @param num 和产品表无关字段，但需要使用复杂表头的字段个数（以实际输入的表头个数为准）
	 * @param num2 表头第二行字段占每列的个数
	 * @return
	 */
	public ExportExcel setDataList(List<Map<String, Object>> listMap, String[] props,String[] head02,String[] lowprops,int num,int num2) {
		List<List<Object>> lists = Lists.newArrayList();
		int index = 1;
		for (Map map : listMap) {
			map.put("index", index);
			List<Object> list = Lists.newArrayList();
			String[] str = new String[head02.length-num];

			for(int i=0; i<props.length; i++) {

				if (map.get(props[i]) instanceof List){
					HashMap<Integer, String> indexMap = new HashMap<>();
					for (Map map1:(ArrayList<HashMap>)map.get(props[i])) {
						for (int j = 0; j < (head02.length/num2)-num/num2; j++) {
							if (map1.get(lowprops[0]) == map.get(lowprops[1]) && map1.get(lowprops[2]).equals(head02[num2*j])){
								for (int k = 0; k < num2; k++) {
									indexMap.put(num2*j+k-num,(map1.get(lowprops[3+k])+""));
								}
							}
						}
					}
					for (Map.Entry<Integer, String> entry : indexMap.entrySet()) {
						str[entry.getKey()] = entry.getValue();
					}
					List<String> strings = Arrays.asList(str);
					for (String s:strings) {
						list.add(s);
					}
				}else{
					list.add(map.get(props[i]));
				}
			}

			lists.add(list);
			index++;
		}
		for (List<Object> list : lists) {
			Row row = this.addRow();
			for(int ii = 0, length = list.size(); ii < length; ii++) {
				this.addCell(row, ii, list.get(ii));
			}
		}
		rowsIdx = lists.size();
		return this;
	}

	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}

	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletResponse response, String fileName) throws IOException{
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="+ Encodes.urlEncode(fileName));
		write(response.getOutputStream());
		return this;
	}
	/**
	 * 输出到客户端兼容多版本浏览器
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletRequest request,HttpServletResponse response, String fileName) throws IOException{
		String userAgent = request.getHeader("User-Agent");
		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		} else {
			// 非IE浏览器的处理：
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.reset();
		response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName));
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("UTF-8");
		write(response.getOutputStream());
		return this;
	}
	/**
	 * 输出到文件
	 * @param name 输出文件名
	 */
	public ExportExcel writeFile(String name) throws IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}

	/**
	 * 清理临时文件
	 */
	public ExportExcel dispose(){
		wb.dispose();
		return this;
	}

	/**
	 * 导出测试
	 */
	public static void main(String[] args) throws Throwable {
		//表头
		List<String> headerList = Lists.newArrayList();
		for (int i = 1; i <= 10; i++) {
			headerList.add("表头"+i);
		}
		//行数据
		List<String> dataRowList = Lists.newArrayList();
		for (int i = 1; i <= headerList.size(); i++) {
			dataRowList.add("数据"+i);
		}
		//行数据的封装
		List<List<String>> dataList = Lists.newArrayList();
		for (int i = 1; i <=1000000; i++) {
			dataList.add(dataRowList);
		}
		String[] head01 = new String[]{"序号","片区","销售","客户名称","客户等级","产品"};
		String[] head02 = new String[]{"验真老三样","验真老三样","验真老三样","验真新接口","验真新接口","验真新接口"};
		String[] head1 = new String[]{"是否有需求","推进优先级","竞对情况","是否有需求","推进优先级","竞对情况"};

		ExportExcel ee = new ExportExcel("表格标题",  head01, head02, head1,3);

		for (int i = 0; i < dataList.size(); i++) {
			Row row = ee.addRow();
			for (int j = 0; j < dataList.get(i).size(); j++) {
				ee.addCell(row, j, dataList.get(i).get(j));
			}
		}
		//BufferedImage bufferImg = null;
		FileOutputStream os = null;
		//ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet();
		try {
			//bufferImg = ImageIO.read();
			//ImageIO.write(bufferImg,"png",byteArrayOut);

			//byte[] bt = FileUtils.readFileToByteArray(new File("C:\\Users\\geo\\Pictures\\Camera Roll\\bird.png"));

			ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
			anchor.setCol1(0);
			anchor.setRow1(0);


			Drawing patriarch = sheet.createDrawingPatriarch();
			//XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) 1, 1, (short) 5, 8);

			//插入图片
			//patriarch.createPicture(anchor, wb.addPicture(bt,SXSSFWorkbook.PICTURE_TYPE_PNG)).resize();

			os = new FileOutputStream("/Users/geo/Desktop/exportttt.xlsx");
			wb.write(os);
			log.debug("Export success.");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (os != null){
				os.close();
			}
		}

	}

}
