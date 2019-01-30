package com.geo.rcs.common.util.excel;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.regex.RegexUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.geo.rcs.common.util.WDWUtil.isExcel2007;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util.excel
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 上午9:54
 */
public class ReadExcel {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    public ReadExcel() {
    }

    //获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param mFile
     * @return
     */
    public List<ScheduleTask> getExcelInfo(MultipartFile mFile, Integer type) {
        String fileName = mFile.getOriginalFilename();//获取文件名
        List<ScheduleTask> taskList = null;
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            taskList = createExcel(mFile.getInputStream(), isExcel2003, type);
        } catch (Exception e) {
            throw new RcsException(e.getMessage());
        }
        return taskList;
    }

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param mFile
     * @return
     */
    public List<AbScheduleTask> getExcelInfoDynamic(MultipartFile mFile, Integer type) {
        String fileName = mFile.getOriginalFilename();//获取文件名
        List<AbScheduleTask> taskList = null;
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            taskList = createExcelDynamic(mFile.getInputStream(), isExcel2003, type);
        } catch (RcsException e) {
            throw new RcsException(e.getMsg(),e.getCode());
        }catch (Exception e){
            throw new RcsException("解析异常，请检查文件格式或是否加密");
        }
        return taskList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<ScheduleTask> createExcel(InputStream is, boolean isExcel2003, Integer type) {
        List<ScheduleTask> taskList = null;
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            taskList = readExcelValue(wb, type);
        } catch (IOException e) {
            throw new RcsException(e.getMessage());
        }
        return taskList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<AbScheduleTask> createExcelDynamic(InputStream is, boolean isExcel2003, Integer type) {
        List<AbScheduleTask> taskList = null;
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            taskList = readExcelValueDynamic(wb, type);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RcsException(e.getMessage());
        }
        return taskList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<ScheduleTask> readExcelValue(Workbook wb, Integer type) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        if (type == 1) {
            if (sheet.getPhysicalNumberOfRows() >= 100) {
                this.totalRows = 100;
            } else {
                this.totalRows = sheet.getPhysicalNumberOfRows();
            }
        } else {
            this.totalRows = sheet.getPhysicalNumberOfRows();
        }
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<ScheduleTask> taskList = new ArrayList<ScheduleTask>();
        // 循环Excel行数

        Row enParmName = sheet.getRow(1);
        String[] parmNames = new String[totalCells];
        for (int i = 0; i < totalCells; i++) {
            Cell cell = enParmName.getCell(i);
            String parmName = cell.getStringCellValue();
            if (StringUtils.isBlank(parmName)) {
                throw new RcsException("解析异常,文件已被破坏!");
            } else {
                parmNames[i] = parmName;
            }
        }
        for (int r = 3; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            ScheduleTask task = new ScheduleTask();
            String cellValue="";
            HashMap<String, String> parmsMap = new HashMap<>();
            // 循环Excel的列
            boolean cellIsNull = false;
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                cellValue = getCellValue(r, cellValue, c, cell).replace(" ","");

                if (cell != null && !(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)) {
                    if ("realName".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkChinese(cellValue.trim())) {
                            throw new RcsException("解析异常,请上传正确格式姓名", StatusCode.FAIL_PARSE_ERRO.getCode());
                        } else {
                            task.setName(cellValue);
                        }
                    }
                    if ("idNumber".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkIdCard(cellValue)) {
                            throw new RcsException("解析异常,请上传正确格式身份证号", StatusCode.FAIL_PARSE_ERRO.getCode());
                        } else {
                            task.setIdNumber(cellValue);
                        }
                    }
                    if ("cid".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkMobile(cellValue)) {
                            throw new RcsException("解析异常,请上传正确格式手机号", StatusCode.FAIL_PARSE_ERRO.getCode());
                        } else {
                            task.setCid(cellValue);
                        }
                    }
                    if ("startTime".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkBirthday(cell.getStringCellValue())) {
                            throw new RcsException("解析异常,请上传正确格式日期,如2016-06-06", StatusCode.FAIL_PARSE_ERRO.getCode());
                        }
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            Date date = DatetimeFormattor.parseDate(cell.getStringCellValue());
                            if (date == null) {
                                throw new RcsException("解析异常,请上传正确格式日期,如2016-06-06", StatusCode.FAIL_PARSE_ERRO.getCode());
                            }
                            task.setStartTime(date);//开始时间
                        } else {
                            task.setStartTime(cell.getDateCellValue());//开始时间
                        }
                    }
                    parmsMap.put(parmNames[c], cellValue);
                } else {
                    cellIsNull = true;
                }
            }
            if(cellIsNull){
                continue;
            }
            task.setParmsJson(JSON.toJSONString(parmsMap));
            // 添加到list
            taskList.add(task);
        }
        return taskList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<AbScheduleTask> readExcelValueDynamic(Workbook wb, Integer type) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        if (type == 1) {
            if (sheet.getPhysicalNumberOfRows() >= 100) {
                this.totalRows = 100;
            } else {
                this.totalRows = sheet.getPhysicalNumberOfRows();
            }
        } else {
            this.totalRows = sheet.getPhysicalNumberOfRows();
        }
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<AbScheduleTask> taskList = new ArrayList<AbScheduleTask>();
        // 循环Excel行数

        Row enParmName = sheet.getRow(1);
        String[] parmNames = new String[totalCells];
        for (int i = 0; i < totalCells; i++) {
            Cell cell = enParmName.getCell(i);
            String parmName = cell.getStringCellValue();
            if (StringUtils.isBlank(parmName)) {
                throw new RcsException("解析异常,文件已被破坏!");
            } else {
                parmNames[i] = parmName;
            }
        }
        for (int r = 3; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            AbScheduleTask task = new AbScheduleTask();
            String cellValue="";
            HashMap<String, String> parmsMap = new HashMap<>();
            // 循环Excel的列
            boolean cellIsNull = false;
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                cellValue = getCellValue(r, cellValue, c, cell).replace(" ","");

                if (cell != null && !(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)) {
                    if ("realName".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkChinese(cellValue.trim())) {
                            throw new RcsException("解析异常,请上传正确格式姓名", StatusCode.FAIL_PARSE_ERRO.getCode());
                        } else {
                            task.setName(cellValue);
                        }
                    }
                    if ("idNumber".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkIdCard(cellValue)) {
                            throw new RcsException("解析异常,请上传正确格式身份证号", StatusCode.FAIL_PARSE_ERRO.getCode());
                        } else {
                            task.setIdNumber(cellValue);
                        }
                    }
                    if ("cid".equalsIgnoreCase(parmNames[c])) {
                        if (!RegexUtil.checkMobile(cellValue)) {
                            throw new RcsException("解析异常,请上传正确格式手机号", StatusCode.FAIL_PARSE_ERRO.getCode());
                        } else {
                            task.setCid(cellValue);
                        }
                    }
                    parmsMap.put(parmNames[c], cellValue);
                } else {
                    cellIsNull = true;
                }
            }
            if(cellIsNull){
                continue;
            }
            task.setParmsJson(JSON.toJSONString(parmsMap));
            // 添加到list
            taskList.add(task);
        }
        return taskList;
    }

    private String getCellValue(int r, String cellValue, int c, Cell cell) {
        if(ValidateNull.isNull(cell)){
            throw new RcsException("第"+(r+1)+"行"+"第"+(c+1)+"列数据格式错误，请检查后重试！", StatusCode.FAIL_PARSE_ERRO.getCode());
        }
        try {
            int cellType = cell.getCellType();

            //数值型
            if (cellType==0){
                DecimalFormat df=new DecimalFormat("0");
                cellValue=df.format(cell.getNumericCellValue());
                //cellValue=cell.getNumericCellValue()+"";
            }else  if (cellType==2){
                // 公式型
                cellValue=cell.getCellFormula();
            }else  if (cellType==4){
                //布尔型
                int i = cell.getBooleanCellValue() == true ? 1 : 0;
                cellValue= i+"";
            }else  if (cellType==5){
                //错误
                throw  new Exception();
            }else{
                //字符串型
                cellValue=cell.getStringCellValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RcsException("第"+(r+1)+"行"+"第"+(c+1)+"列数据格式错误，请检查后重试!", StatusCode.FAIL_PARSE_ERRO.getCode());

        }
        return cellValue;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }
}