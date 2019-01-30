package com.geo.rcs.common.util.pdf;

import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.modules.decision.entity.EngineDecisionLog;
import com.geo.rcs.modules.decision.util.Base64Utils;
import com.geo.rcs.modules.decision.util.DecisionPdfResult;
import com.geo.rcs.modules.engine.entity.Rule;
import com.geo.rcs.modules.event.vo.EventReport;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * pdf工具
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/1/26 15:53
 */
public class PdfUtil {
	/** pdf模板路径 */
	private static final String EVENT_PDF_TEMPLATE = "static/pdf/template/event_manage_template.pdf";
	/** pdf批量下载临时Zip路径 */
	private static final String TEMP_BATCH_EVENT_PDF_ZIP = "static/pdf/tempzip/";
	/** 字体路径 */
	private static final String FONT_SOURCEPATH = "static/pdf/font/msyh.ttf";
	/** 图片路径 */
	private static final String IMAGE1_SOURCE_PATH = "static/pdf/image/event_header.png";
	private static final String IMAGE2_SOURCE_PATH = "static/pdf/image/event_body.png";

	/** 字体大小 */
	private static Integer[] FONT_SIZE = { 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40 };
	private static BaseFont fontChinese = null;

	/** 单元格固定高度 */
	private static final Float FIXED_HEIGHT = 25f;

	/** 默认边框颜色 */
	private static final BaseColor BORDER_COLOR = new BaseColor(204, 204, 204);

	private static final Logger log = LoggerFactory.getLogger(PdfUtil.class);

	/**
	 * 将静态资源转换成字节流
	 *
	 * @return
	 */
	public static byte[] file2Bytes(String path) {
		if (path == null || path == "") {
			return null;
		}

		Resource resource = new ClassPathResource(path);
		InputStream is = null;
		ByteArrayOutputStream bt = null;

		try {
			is = resource.getInputStream();
			bt = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				bt.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bt != null) {
				try {
					bt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bt.toByteArray();
	}

	/**
	 * 将字节流转换成图片
	 *
	 * @return
	 */
	public static Image byte2Image(byte[] bytes) {
		Image image = null;
		try {
			image = Image.getInstance(bytes);
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 判断字体资源文件是否存在
	 *
	 * @return
	 */
	public static boolean isExistsFontSource() {
		boolean flag = true;
		Resource resource = new ClassPathResource(FONT_SOURCEPATH);

		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			if (br == null) {
				flag = false;
			}
		} catch (IOException e) {
			log.info("字体资源文件不存在");
			flag = false;
		}

		return flag;
	}

	/**
	 * 设置中文
	 *
	 * @return
	 */
	public static BaseFont getChinese() {
		if (fontChinese == null) {
			try {
				if (isExistsFontSource()) {
					log.info("字体文件加载成功");
					fontChinese = BaseFont.createFont(FONT_SOURCEPATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				} else {
					log.info("字体文件加载失败");
					fontChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				}
			} catch (Exception e) {
				log.info("字体文件加载失败");
				try {
					fontChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return fontChinese;
	}

	/**
	 * 设置字体大小、样式
	 *
	 * @param size
	 * @param style
	 * @return
	 */
	public static Font setFontSize(float size, int style, BaseColor color) {
		Font font = new Font(getChinese(), size, style, color);
		return font;
	}

	/**
	 * 批量生成并合并输出pdf
	 *
	 * @param reportList
	 * @param response
	 * @return
	 */
	public static boolean pdfWriter(List<EventReport> reportList, HttpServletResponse response) {
		boolean flag = true;
		if (reportList == null || reportList.isEmpty()) {
			return false;
		}

		List<byte[]> bytes = new ArrayList<>();
		try {
			for (EventReport report : reportList) {
				bytes.add(RulesPdfResult.createPdf(report).toByteArray());
			}
		}catch (DocumentException e) {
			e.printStackTrace();
			flag = false;
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		}

		try {
			flag = concatPDFs(bytes, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		}

		return flag;
	}

	/**
	 * 生成并向浏览器输出zip
	 *
	 * @return
	 */
	public static boolean zipWriter(List<EventReport> reportList, HttpServletRequest request, HttpServletResponse response) {
		// 压缩
		boolean fileToZip = false;
		try {
			boolean flag = true;
			ByteArrayOutputStream ba = null;
			String dir = TEMP_BATCH_EVENT_PDF_ZIP + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
			String fileDir=dir+"/eventPdfFiles";
			File file = new File(fileDir);
			if (!file.exists()) {
				boolean mkdirs = file.mkdirs();
			}
			for (int i = 0; i < reportList.size(); i++) {
				EventReport report = reportList.get(i);
				String reportName = report.getName() == null ? "" : report.getName();
				String fileName = fileDir + "/" + reportName + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS") + ".pdf";
				ba = RulesPdfResult.createPdf(report);
				if (ba != null) {
					pdfWriterToLocal(ba, fileName);
				}
			}

			// 待生成的zip包名
			String zipFileName = "进件报告" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
			String zipName = zipFileName;
			// 待生成的zip保存路径
			String zipFilePath = dir+"/zip";
			File zipDir = new File(zipFilePath);
			if (!zipDir.exists()) {
				zipDir.mkdirs();
			}
			fileToZip = writeZipToBrower(fileDir, zipFilePath, zipName,request, response,dir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("批量下载事件报告失败!"+e.getMessage());
			e.printStackTrace();
		}
		return fileToZip;
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 *
	 * @param sourceFilePath
	 *            :待压缩的文件路径
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @param response
	 * @param dir
	 * @return
	 */
	public static boolean writeZipToBrower(String sourceFilePath, String zipFilePath, String fileName,HttpServletRequest request,
										   HttpServletResponse response, String dir) throws UnsupportedEncodingException {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
			sourceFile.mkdir(); // 新建目录
		}
		File zipFile=null;
		try {
			zipFile = new File(zipFilePath + "/" + fileName + ".zip");
			if (zipFile.exists()) {
				System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
			} else {
				File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
				} else {
					// 从response对象中得到输出流,准备下载
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// 创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
					flag = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				if (null != zos)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}


		if (flag) {
			filnameEncode(fileName, request, response);
				try {
				FileInputStream fileInStreamm = new FileInputStream(zipFile);
				BufferedInputStream buff = new BufferedInputStream(fileInStreamm);
				byte[] b = new byte[1024];// 相当于我们的缓存
				long k = 0;// 该值用于计算当前实际下载了多少字节
				OutputStream myout = response.getOutputStream();// 从response对象中得到输出流,准备下载
				// 开始循环下载
				while (k < zipFile.length()) {
					int j = buff.read(b, 0, 1024);
					k += j;
					myout.write(b, 0, j);
				}
				myout.flush();
				buff.close();

			} catch (IOException e) {
				flag=false;
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 从response对象中得到输出流,准备下载

		}else {
			log.error("批量下载事件报告失败!");
		}
		boolean delAllFile = delFolder(dir);
		if (!delAllFile) {
			log.error("删除临时zip文件夹失败!");
			System.out.println("删除临时zip文件夹失败!");
		}
		return flag;
	}

	private static void filnameEncode(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		fileName+=".zip";
		String userAgent = request.getHeader("User-Agent");
		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
		response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName));
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/zip");
	}

	public static boolean delFile(String path){
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()){
			return flag;
		}
		flag = file.delete();
		return  flag;
	}

	/**
	 *
	 * @param path 删除的文件夹目录
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	//删除文件夹
	public static boolean delFolder(String folderPath) {
		boolean flag=false;
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
			flag=true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return flag;

	}
	/**
	 * 生成本地pdf文件
	 *
	 * @return
	 */
	public static boolean pdfWriterToLocal(ByteArrayOutputStream ba, String fileName) {
		boolean flag = true;
		FileOutputStream out =null;

		try {
			File filePdf = new File(fileName);
			if (!filePdf.exists()) {
				filePdf.createNewFile();
			}
			out = new FileOutputStream(filePdf);

			try(OutputStream outputStream = out) {
				ba.writeTo(outputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {

			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return flag;
	}

	/**
	 * 生成并向浏览器输出pdf
	 *
	 * @return
	 */
	public static boolean pdfWriter(EventReport report, HttpServletResponse response) {
		boolean flag = true;
		PdfReader reader = null;					//读取pdf
		try {
			byte[] bytes = RulesPdfResult.createPdf(report).toByteArray();	  //将pdf报告转为数组字节
			ByteArrayOutputStream ba = RulesPdfResult.createPdf(report);	  // 字节数组输出流

			if (bytes != null) {
				reader = new PdfReader(bytes);
			}

			try(OutputStream outputStream = response.getOutputStream()) {
				ba.writeTo(outputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return flag;
	}
	/**
	 * 生成并输出PDF字节流
	 */
	public static ByteArrayOutputStream createReport(EventReport report) {
		Document document = null;
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		try {

			/**
			 * PDF 基本设置
			 */
			// 设置字体
			Font font1 = setFontSize(FONT_SIZE[6], Font.NORMAL, BaseColor.BLACK);
			Font font2 = setFontSize(FONT_SIZE[3], Font.NORMAL, BaseColor.WHITE);
			Font font3 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.BLACK);

			// 背景颜色
			BaseColor bgColor = new BaseColor(200, 222, 251);
			BaseColor bgColor1 = new BaseColor(247, 252, 255);
			BaseColor RedBgColor = new BaseColor(247, 252, 255);


			// 设置表格列
			int[]
					width0 = { 25, 75 },
					width1 = { 15, 15, 15, 20, 15, 20 },
					width2 = { 15, 85 },
					width3 = { 14, 26, 12, 12, 12, 12, 12 },
					width4 = { 14, 42, 16, 14, 14 },
					width5 = { 15, 15, 20, 25, 25 };


			// 创建一个document对象
			document = new Document(new Rectangle(PageSize.A4), 0, 0, 10, 0);
			document.setPageCount(1);

			// 我们为document创建一个监听，并把PDF流写到文件中
			PdfWriter writer = PdfWriter.getInstance(document, ba);
			// 打开文档
			document.open();
			document.addTitle("事件报告");

			// 加背景图片
			Image image1 = byte2Image(file2Bytes(IMAGE1_SOURCE_PATH));
			image1.scaleAbsolute(600, 50);
			image1.setAlignment(Image.UNDERLYING);
			document.add(image1);

			// 加背景图片
			Image image2 = byte2Image(file2Bytes(IMAGE2_SOURCE_PATH));
			image2.scaleAbsolute(400, 900);
			image2.setAlignment(Image.UNDERLYING);
			document.add(image2);

			/**
			 * 模块：标题
			 * */

			// 添加标题
			Paragraph ph1 = new Paragraph("事件报告", font1);
			ph1.setAlignment(Element.ALIGN_CENTER);
			document.add(ph1);

			// 添加空行
			Paragraph blankRow1 = new Paragraph(25f, " ", font1);
			document.add(blankRow1);

			/**
			 * 模块：基本信息
			 */
			// 添加表头
			PdfPTable table0 = new PdfPTable(2);
			table0.setWidths(width0);
			table0.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table0.addCell(createCell("个人基本信息", font2, bgColor, Rectangle.NO_BORDER));
			table0.addCell(createCell("", font2, 5, Rectangle.NO_BORDER));
			document.add(table0);

			// 添加空行
			Paragraph blankRow2 = new Paragraph(6f, " ", font3);
			document.add(blankRow2);

			// 创建一个6列的表(内容)
			PdfPTable table1 = new PdfPTable(6);
			table1.setWidths(width1);
			table1.addCell(createCell("姓名", font3, bgColor));
			table1.addCell(createCell(report.getName(), font3, bgColor1));
			table1.addCell(createCell("性别", font3, bgColor));
			table1.addCell(createCell(report.getGender(), font3, bgColor1));
			table1.addCell(createCell("年龄", font3, bgColor));
			table1.addCell(createCell(report.getAge(), font3, bgColor1));
			document.add(table1);

			// 创建一个6列的表(内容)
			PdfPTable table2 = new PdfPTable(6);
			table2.setWidths(width1);
			table2.addCell(createCell("职业", font3, bgColor));
			table2.addCell(createCell(report.getOccupation(), font3, bgColor1));
			table2.addCell(createCell("手机号", font3, bgColor));
			table2.addCell(createCell(report.getMobile(), font3, bgColor1));
			table2.addCell(createCell("常联系号码", font3, bgColor));
			table2.addCell(createCell(report.getTelephone(), font3, bgColor1));
			document.add(table2);

			// 创建一个2列的表(内容)
			PdfPTable table3 = new PdfPTable(2);
			table3.setWidths(width2);

			table3.addCell(createCell("身份证号", font3, bgColor));
			table3.addCell(createCell(report.getIdCard(), font3, 5, bgColor1));

			table3.addCell(createCell("工作单位", font3, bgColor));
			table3.addCell(createCell(report.getWorkUnit(), font3, 5, bgColor1));

			table3.addCell(createCell("家庭住址", font3, bgColor));
			table3.addCell(createCell(report.getHomeAddress(), font3, 5, bgColor1));

			table3.addCell(createCell("单位地址", font3, bgColor));
			table3.addCell(createCell(report.getCompanyAddress(), font3, 5, bgColor1));

			document.add(table3);

			// 添加空行
			Paragraph blankRow3 = new Paragraph(25f, " ", font1);
			document.add(blankRow3);


			/**
			 * 模块：决策结果
			 */

			// 添加表头
			PdfPTable resuleTableHeader = new PdfPTable(2);
			resuleTableHeader.setWidths(width0);
			resuleTableHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			resuleTableHeader.addCell(createCell("识别结果", font2, bgColor, Rectangle.NO_BORDER));
			resuleTableHeader.addCell(createCell("", font2, 5, Rectangle.NO_BORDER));
			document.add(resuleTableHeader);

			// 添加空行
			Paragraph blankRow22 = new Paragraph(6f, " ", font3);
			document.add(blankRow22);

			String resultRisk = "-";
			String resultScore = String.valueOf(report.getEngineRules().getScore());
			String resultStatus = String.valueOf(report.getEngineRules().getStatus());
			if("1".equals(resultStatus)){
				resultStatus = "通过";
			}else if("2".equals(resultStatus)){
				resultStatus = "人工审核";
			}else if("3".equals(resultStatus)){
				resultStatus = "拒绝";
			}else {
				resultStatus = "识别异常";
			}
			// 创建一个6列的表(内容)
			PdfPTable resultTable = new PdfPTable(6);
			resultTable.setWidths(width1);
			resultTable.addCell(createCell("命中风险", font3, bgColor));
			resultTable.addCell(createCell(resultRisk, font3, bgColor1));
			resultTable.addCell(createCell("识别分数", font3, bgColor));
			resultTable.addCell(createCell(resultScore, font3, bgColor1));
			resultTable.addCell(createCell("建议决策", font3, bgColor));
			resultTable.addCell(createCell(resultStatus, font3, bgColor1));
			document.add(resultTable);


			// 添加空行
			Paragraph blankRow33 = new Paragraph(25f, " ", font1);
			document.add(blankRow33);


			/**
			 * 模块：规则集信息
			 */

			// 添加表头
			PdfPTable table8 = new PdfPTable(2);
			table8.setWidths(width0);
			table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table8.addCell(createCell("规则集信息", font2, bgColor, Rectangle.NO_BORDER));
			table8.addCell(createCell("", font2, 6, Rectangle.NO_BORDER));
			document.add(table8);

			// 添加空行
			Paragraph blankRow4 = new Paragraph(6f, " ", font3);
			document.add(blankRow4);

			// 创建一个5列的表(表头)
			PdfPTable table9 = new PdfPTable(5);
			table9.setWidths(width4);
			table9.addCell(createCell("ID", font3, bgColor));
			table9.addCell(createCell("名称", font3, bgColor));
			table9.addCell(createCell("匹配模式", font3, bgColor));
			table9.addCell(createCell("最小阈值", font3, bgColor));
			table9.addCell(createCell("最大阈值", font3, bgColor));
//            table9.addCell(createCell("命中风险", font3, bgColor1));
//            table9.addCell(createCell("识别得分", font3, bgColor1));
//            table9.addCell(createCell("识别决策", font3, bgColor1));
			document.add(table9);

			// 创建一个5列的表(内容)
			PdfPTable table10 = new PdfPTable(5);
			table10.setWidths(width4);

			String ruleSetMatchType = report.getEngineRules().getMatchType()==0 ? "均值匹配":"风险匹配";

			table10.addCell(createCell(String.valueOf(report.getEngineRules().getId()), font3, bgColor1));
			table10.addCell(createCell(report.getEngineRules().getName(), font3, bgColor1));
			table10.addCell(createCell(ruleSetMatchType, font3, bgColor1));
			table10.addCell(createCell(String.valueOf(report.getEngineRules().getThresholdMin()), font3, bgColor1));
			table10.addCell(createCell(String.valueOf(report.getEngineRules().getThresholdMax()), font3, bgColor1));
//			table10.addCell(createCell(report.getAddTime(), font3, bgColor1));
//			table10.addCell(createCell(report.getAddTime(), font3, bgColor1));
//			table10.addCell(createCell(report.getAddTime(), font3, bgColor1));

			document.add(table10);

			// 添加空行
			Paragraph blankRow5 = new Paragraph(25f, " ", font1);
			document.add(blankRow5);

			/**
			 * 模块：命中规则
			 */

			// 添加表头
			PdfPTable table11 = new PdfPTable(2);
			table11.setWidths(width0);
			table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table11.addCell(createCell("命中规则", font2, bgColor, Rectangle.NO_BORDER));
			table11.addCell(createCell("", font2, 6, Rectangle.NO_BORDER));
			document.add(table11);

			// 添加空行
			Paragraph blankRow42 = new Paragraph(6f, " ", font3);
			document.add(blankRow42);

			// 创建一个7列的表(表头)
			PdfPTable table12 = new PdfPTable(7);
			table12.setWidths(width3);
			table12.addCell(createCell("规则ID", font3, bgColor));
			table12.addCell(createCell("规则名称", font3, bgColor));
			table12.addCell(createCell("规则阈值", font3, bgColor));
			table12.addCell(createCell("规则风险", font3, bgColor));
			table12.addCell(createCell("命中结果", font3, bgColor));
			table12.addCell(createCell("命中得分", font3, bgColor));
			table12.addCell(createCell("命中风险", font3, bgColor));

			document.add(table12);

			// 创建一个7列的表(内容)
			PdfPTable table13 = new PdfPTable(7);
			table13.setWidths(width3);
			List<Rule> ruleList = report.getEngineRules().getRuleList();
			if (ruleList == null || ruleList.isEmpty()) {
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table13.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
			} else {
				String ruleId = "";
				String ruleName =  "";
				String ruleThreshold =  "";
				String ruleLevel =  "";
				String conditionRelationShip = "";
				String ruleScore = "";
				String ruleArrow = "";
				String ruleArrowLevel = "";

				for (Rule rule : ruleList) {
					ruleId = String.valueOf(rule.getId());
					ruleName = String.valueOf(rule.getName());
					ruleThreshold = String.valueOf(rule.getThreshold());
					conditionRelationShip = rule.getConditionRelationShip();
					ruleScore = String.valueOf(rule.getScore());
					if(rule.getScore()>0){
						ruleArrow="命中";
						ruleArrowLevel=ruleLevel;
					} else{
						if((report.getEngineRules().getStatus()==2 || report.getEngineRules().getStatus()==4)
								&& report.getEngineRules().getScore()==0 ){
							ruleArrow="需要人工识别";
							ruleArrowLevel="需要人工识别";
						}
						else{
							ruleArrow="未命中";
							ruleArrowLevel="无风险";
						}
					}

					if(rule.getLevel()==1){ ruleLevel = "无风险"; } else if(rule.getLevel()==2){ ruleLevel = "低风险"; }
					else if(rule.getLevel()==3){ ruleLevel = "高风险"; } else{ ruleLevel = "未设定";
					}

					table13.addCell(createCell(ruleId, font3, bgColor1, 2 * FIXED_HEIGHT));
					table13.addCell(createCell(ruleName, font3, bgColor1, 2 * FIXED_HEIGHT));
					table13.addCell(createCell(ruleThreshold, font3, bgColor1, 2 * FIXED_HEIGHT));
					table13.addCell(createCell(ruleLevel, font3, bgColor1, 2 * FIXED_HEIGHT));
					table13.addCell(createCell(ruleArrow, font3, bgColor1, 2 * FIXED_HEIGHT));
					table13.addCell(createCell(ruleScore, font3, bgColor1, 2 * FIXED_HEIGHT));
					table13.addCell(createCell(ruleArrowLevel, font3, bgColor1, 2 * FIXED_HEIGHT));

				}
			}
			document.add(table13);

			// 添加空行
			Paragraph blankRow52 = new Paragraph(25f, " ", font1);
			document.add(blankRow52);

			/**
			 * 模块：字段及接口数据
			 */

			// 添加表头
			PdfPTable table14 = new PdfPTable(2);
			table14.setWidths(width0);
			table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table14.addCell(createCell("规则字段", font2, bgColor, Rectangle.NO_BORDER));
			table14.addCell(createCell("", font2, 6, Rectangle.NO_BORDER));
			document.add(table14);

			// 添加空行
			Paragraph blankRow6 = new Paragraph(6f, " ", font3);
			document.add(blankRow6);

			// 创建一个2列的表(表头)
			PdfPTable table15 = new PdfPTable(5);
			table15.setWidths(width5);
			table15.addCell(createCell("接口名称", font3, bgColor));
			table15.addCell(createCell("输出字段", font3, bgColor));
			table15.addCell(createCell("字段说明 ", font3, bgColor));
			table15.addCell(createCell("字段结果 ", font3, bgColor));
			table15.addCell(createCell("结果描述 ", font3, bgColor));
			document.add(table15);

			// 创建一个2列的表(内容)
			PdfPTable table16 = new PdfPTable(5);
			table16.setWidths(width5);
			List<Map<String, Object>> productFieldList = report.getProductFieldList();
			if (productFieldList == null || productFieldList.isEmpty()) {
				table16.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table16.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table16.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table16.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
				table16.addCell(createCell(null, font3, bgColor1, 2 * FIXED_HEIGHT));
			} else {
				for (Map<String, Object> productField : productFieldList) {
					table16.addCell(
							createCell(productField.get("interfaceName"), font3, bgColor1, 2 * FIXED_HEIGHT));
					table16.addCell(
							createCell(productField.get("fieldName"), font3, bgColor1, 2 * FIXED_HEIGHT));
					table16.addCell(
							createCell(productField.get("fieldNameDesc"), font3, bgColor1, 2 * FIXED_HEIGHT));
					table16.addCell(
							createCell(productField.get("verificationResult"), font3, bgColor1, 2 * FIXED_HEIGHT));
					table16.addCell(
							createCell(productField.get("verificationResultDesc"), font3, bgColor1, 2 * FIXED_HEIGHT));
				}
			}
			document.add(table16);
			document.close();
			writer.close();

			// try(OutputStream outputStream = new FileOutputStream("/tmp/ba2pdf.pdf")) {
			//   ba.writeTo(outputStream);
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭文档
			if (document != null) {
				if (document.isOpen()) {
					document.close();
				}
			}
		}

		return ba;
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font) {
		return createCell(text, font, null, null, BORDER_COLOR, null, null, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Float fixedHeight) {
		return createCell(text, font, null, null, BORDER_COLOR, null, null, fixedHeight);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, BaseColor bgColor, Float fixedHeight) {
		return createCell(text, font, null, null, BORDER_COLOR, bgColor, null, fixedHeight);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param bgColor
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, BaseColor bgColor) {
		return createCell(text, font, null, null, BORDER_COLOR, bgColor, null, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param colspan
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer colspan) {
		return createCell(text, font, colspan, null, BORDER_COLOR, null, null, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param colspan
	 * @param border
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer colspan, Integer border) {
		return createCell(text, font, colspan, null, BORDER_COLOR, null, border, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param colspan
	 * @param fixedHeight
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer colspan, Float fixedHeight) {
		return createCell(text, font, colspan, null, BORDER_COLOR, null, null, fixedHeight);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param colspan
	 * @param bgColor
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer colspan, BaseColor bgColor) {
		return createCell(text, font, colspan, null, BORDER_COLOR, bgColor, null, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param bgColor
	 * @param border
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, BaseColor bgColor, Integer border) {
		return createCell(text, font, null, null, BORDER_COLOR, bgColor, border, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param align
	 * @param bgColor
	 * @param border
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer align, BaseColor bgColor, Integer border) {
		return createCell(text, font, null, align, BORDER_COLOR, bgColor, border, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param borderColor
	 * @param bgColor
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, BaseColor borderColor, BaseColor bgColor) {
		return createCell(text, font, null, null, borderColor, bgColor, null, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 * @param font
	 * @param colspan
	 * @param align
	 * @param borderColor
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer colspan, Integer align, BaseColor borderColor) {
		return createCell(text, font, colspan, align, borderColor, null, null, FIXED_HEIGHT);
	}

	/**
	 * 用於生成cell
	 *
	 * @param text
	 *            Cell文字内容
	 * @param font
	 *            字体
	 * @param colspan
	 *            合并列数量
	 * @param align
	 *            显示位置(左中右，Paragraph对象)
	 * @param borderColor
	 *            Cell边框颜色
	 * @param bgColor
	 *            Cell背景色
	 * @return
	 */
	private static PdfPCell createCell(Object text, Font font, Integer colspan, Integer align, BaseColor borderColor,
									   BaseColor bgColor, Integer border, Float fixedHeight) {
		if (text == null) {
			text = "-";
		}
		Paragraph pagragraph = new Paragraph( text.toString(), font);
		PdfPCell cell = new PdfPCell(pagragraph);
		// 上中下，Element对象
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// 左中右，Element对象
		if (align != null) {
			cell.setHorizontalAlignment(align);
		} else {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		}
		// 合并单元格
		if (colspan != null && colspan > 1) {
			cell.setColspan(colspan);
		}
		// 边框颜色
		if (borderColor != null) {
			cell.setBorderColor(borderColor);
		}
		// 背景颜色
		if (bgColor != null) {
			cell.setBackgroundColor(bgColor);
		}
		// 设置有无边框
		if (border != null) {
			cell.setBorder(border);
		}
		// 设置固定高度
		if (fixedHeight != null) {
			cell.setFixedHeight(fixedHeight);
		}
		return cell;
	}

	/**
	 * 合并PDF
	 *
	 * @param bytes
	 * @param outputStream
	 * @return
	 */
	private static boolean concatPDFs(List<byte[]> bytes, OutputStream outputStream) {
		boolean flag = true;
		List<PdfReader> readers = new ArrayList<>();
		try {
			if (bytes == null || bytes.isEmpty()) {
				return false;
			}

			for (byte[] bytes1 : bytes) {
				readers.add(new PdfReader(bytes1));
			}

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					// 创建新的一页
					document.newPage();
					pageOfCurrentReaderPDF++;
					PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
				}
				pageOfCurrentReaderPDF = 0;
			}

			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		return flag;
	}

				/*************************************在线决策pdfUtil********************************************/


	public static Map<String,String> zipWriterForDecision(List<Map<String,Object> > dataList,HttpServletRequest request,HttpServletResponse response,String resultImgPath){
		boolean flag = true;
		Map<String,String> map = new HashMap<>();
		ByteArrayOutputStream ba = null;
			String dir = TEMP_BATCH_EVENT_PDF_ZIP + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
			String fileDir = dir + "/decisionPdfFilies";
			File file = new File(fileDir);
			if (!file.exists()) {
				boolean mkdirsFlag = file.mkdirs();
			}
		try {
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> data = dataList.get(i);

				//存放result_img文件
				String imgStr = (String)data.get("imgBase");
				String s = "data:image/png;base64,";
				String imgBase = imgStr.replace(s,"");
				Base64Utils.Base64ToImage(imgBase,resultImgPath);
				ba = DecisionPdfResult.createPdf(data);
				EngineDecisionLog e = (EngineDecisionLog) data.get("data");
				String fileName = fileDir + "/" + e.getRealName() + DateUtils.format(new Date(),"yyyyMMddHHmmssSSS") + ".pdf";
				if (ba != null){
					pdfWriterToLocal(ba,fileName);
				}
			}
		} catch (DocumentException e) {
			log.error("创建pdf文件失败");
			flag = false;
			e.printStackTrace();
		} catch (IOException e) {
			log.error("创建pdf文件失败");
			flag = false;
			e.printStackTrace();
		}
		try {
			String zipFileName = "事件报告" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
			String zipName = zipFileName;
			// 待生成的zip保存路径
			String zipFilePath = dir + "/zip";
			File zipDir = new File(zipFilePath);
			if (!zipDir.exists()) {
				zipDir.mkdirs();
			}
			map = writeZipToLocal(fileDir, zipFilePath, zipName, dir);
		} catch (UnsupportedEncodingException e) {
			log.error("批量下载事件报告失败");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 在线决策结果pdf
	 * 生成并向浏览器输出pdf
	 * @param map		pdf data
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static boolean pdfWriterForDecision(Map<String,Object> map, HttpServletResponse response) throws IOException, DocumentException {
		boolean flag = true;
		byte[] bytes = DecisionPdfResult.createPdf(map).toByteArray();	  //将pdf报告转为数组字节
		ByteArrayOutputStream ba = 	DecisionPdfResult.createPdf(map);  // 字节数组输出流
		PdfReader reader = null;					//读取pdf
		try {
			if (bytes != null) {
				reader = new PdfReader(bytes);
			}

			try(OutputStream outputStream = response.getOutputStream()) {
				ba.writeTo(outputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return flag;
	}

	/**
	 * ab测试结果pdf
	 * 生成并向浏览器输出pdf
	 * @param map		pdf data
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static boolean pdfWriterForAbTest(Map<String,Object> map, HttpServletResponse response) throws IOException, DocumentException {
		boolean flag = true;
		byte[] bytes = DecisionPdfResult.createAbTestPdf(map).toByteArray();	  //将pdf报告转为数组字节
		ByteArrayOutputStream ba = 	DecisionPdfResult.createAbTestPdf(map);  // 字节数组输出流
		PdfReader reader = null;					//读取pdf
		try {
			if (bytes != null) {
				reader = new PdfReader(bytes);
			}

			try(OutputStream outputStream = response.getOutputStream()) {
				ba.writeTo(outputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return flag;
	}
	/**
	 * ab测试结果pdf(仅图片)
	 * 生成并向浏览器输出pdf
	 * @param map		pdf data
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static boolean pdfWriterForAbTestResult(Map<String,Object> map, HttpServletResponse response) throws IOException, DocumentException {
		boolean flag = true;
		byte[] bytes = DecisionPdfResult.createAbTestPdfResult(map).toByteArray();	  //将pdf报告转为数组字节
		ByteArrayOutputStream ba = 	DecisionPdfResult.createAbTestPdfResult(map);  // 字节数组输出流
		PdfReader reader = null;					//读取pdf
		try {
			if (bytes != null) {
				reader = new PdfReader(bytes);
			}

			try(OutputStream outputStream = response.getOutputStream()) {
				ba.writeTo(outputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return flag;
	}

	public static Map<String,String> writeZipToLocal(String sourceFilePath, String zipFilePath, String fileName, String dir) throws UnsupportedEncodingException {
		boolean flag = false;
		Map<String,String> map = new HashMap<>();
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		String zipPath = null;

		if (sourceFile.exists() == false) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
			sourceFile.mkdir(); // 新建目录
		}
		File zipFile=null;
		try {
			zipFile = new File(zipFilePath + "/" + fileName + ".zip");
			zipPath = zipFilePath + "/" + fileName + ".zip";
			if (zipFile.exists()) {
				System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
			} else {
				File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
				} else {
					// 从response对象中得到输出流,准备下载
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// 创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
					flag = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				if (null != zos)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		map.put("dir",dir);
		map.put("zipPath",zipPath);
		map.put("fileName",fileName);
		return map;
	}

	public static boolean downloadZip(HttpServletResponse response,HttpServletRequest request,String zipPath,String fileName,String dir) throws UnsupportedEncodingException {
		boolean flag = true;
		filnameEncode(fileName, request, response);
		File zipFile = new File(zipPath);
		try {
			FileInputStream fileInStreamm = new FileInputStream(zipFile);
			BufferedInputStream buff = new BufferedInputStream(fileInStreamm);
			byte[] b = new byte[1024];// 相当于我们的缓存
			long k = 0;// 该值用于计算当前实际下载了多少字节
			OutputStream myout = response.getOutputStream();// 从response对象中得到输出流,准备下载
			// 开始循环下载
			while (k < zipFile.length()) {
				int j = buff.read(b, 0, 1024);
				k += j;
				myout.write(b, 0, j);
			}
			myout.flush();
			buff.close();

		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		} // 从response对象中得到输出流,准备下载

		boolean delAllFile = delFolder(dir);
		if (!delAllFile) {
			log.error("删除临时zip文件夹失败!");
			System.out.println("删除临时zip文件夹失败!");
		}
		return flag;
	}

}