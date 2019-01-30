package com.geo.rcs.modules.decision.util;

import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.pdf.PdfUtil;
import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.decision.entity.EngineDecisionLog;
import com.geo.rcs.modules.decision.entity.ExcuteFlowForApi;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/10/31  16:51
 **/
public class DecisionPdfResult {


    /**图片资源路径*/
    private static final String HEAD_IMAGE = "static/pdf/image/head_image.png";
    private static final String TAIL_IMAGE = "static/pdf/image/tail_image.png";
    private static final String EVENT_REPORT_IMAGE = "static/pdf/image/event_report.png";
    private static final String LEFT_LEAF_IMAGE = "static/pdf/image/left_leaf.png";
    private static final String RIGHT_LEAF_IMAGE = "static/pdf/image/right_leaf.png";
    private static final String ICON_IMAGE = "static/pdf/image/icon_image.png";
    private static final String ERROR_IMAGE="static/pdf/image/error_image.png";
    private static final String RIGHT_IMAGE="static/pdf/image/right_image.png";
    private static final String REVIEW_IMAGE="static/pdf/image/review_image.png";
    private static final String CIRCLE_IMAGE="static/pdf/image/circle_image.png";
    private static final String RESULT_IMAGE="static/pdf/result/image/result.png";

    /**字体路径*/
    private static final String FONT_RESOURCE = "static/pdf/font/msyh.ttf";

    /**字体大小*/
    private static final int[] FONT_SIZE = {8, 9, 12, 14, 16, 18,20,22};
    private static BaseFont fontChinese = null;

    /** 单元格固定高度 */
    private static final Float FIXED_HEIGHT = 24.5f;

    /**默认表名上下间距*/
    private static final Integer TABLE_TITLE_SPACE = 30;

    /**表列比例*/
    private static final int[]
            WIDTH_1 = {8,12, 10,25, 5, 10, 5, 25},
            WIDTH_2= {25, 20, 30, 25},
            WIDTH_3= {25,30,20,25};

    public static final Logger log = LoggerFactory.getLogger(DecisionPdfResult.class);

    /**
     * 创建在线决策结果pdf模板
     *
     * @param  map
     * @throws DocumentException
     * @throws IOException
     */
    public static ByteArrayOutputStream createAbTestPdf(Map<String,Object> map)
            throws DocumentException, IOException {

        //pdf数据
        AbScheduleTask data = (AbScheduleTask)map.get("data");
        List<ExcuteFlowForApi> decisions = (List<ExcuteFlowForApi>)map.get("decisions");
        Map<Long,String> rulesNamesMap = ( Map<Long,String>)map.get("rulesNamesMap");
        Map<Long,Integer> rulesMatchTypeMap = ( Map<Long,Integer>)map.get("rulesMatchTypeMap");
        Integer decisionId = data.getGoalId();
        String decisionName = (String)map.get("decisionName");
        Integer hitNumber = (Integer)map.get("count");
        Map<String,Object> info = new HashedMap();
        Map<String,Object> statusDataMap = new HashedMap();
        info.put("realName",data.getName());
        info.put("idNumber",data.getIdNumber());
        info.put("cid",data.getCid());
        statusDataMap.put("hitNumber",hitNumber);
        statusDataMap.put("status",data.getExecuteStatus());
        //规则集list
        List<Map<String,Object>> rulesList = new ArrayList<>();
        //模型list
        List<Map<String,Object>> modelList = new ArrayList<>();
        for (int i = 0; i < decisions.size();i++){
            Long rulesId = decisions.get(i).getRulesId();
            String rulesName = rulesNamesMap.get(rulesId);
            Integer score = decisions.get(i).getScore();
            Integer status = decisions.get(i).getStatus();
            if (rulesMatchTypeMap.get(rulesId) == 2){
                Map<String,Object> model = new HashedMap();
                model.put("rulesId",rulesId);
                model.put("rulesName",rulesName);
                model.put("score",score);
                model.put("status",status);
                modelList.add(model);
            }else{
                Map<String,Object> rules = new HashedMap();
                rules.put("rulesId",rulesId);
                rules.put("rulesName",rulesName);
                rules.put("score",score);
                rules.put("status",status);
                rulesList.add(rules);
            }
        }


        //基本设置
        Document document = null;
        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        //设置字体
        if (isExistsFontResource()){
            log.info("加载字体成功");
        }else{
            log.info("加载字体失败，默认使用宋体");
        }
        Font font0 = new Font();
        Font font1 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.BLACK);
        Font font2 = setFontSize(FONT_SIZE[1], Font.NORMAL, new BaseColor(103, 107, 113));  //浅灰色
        Font font3 = setFontSize(FONT_SIZE[7], Font.BOLD, BaseColor.WHITE);
        Font font4 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.RED);
        Font font5 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(75,101,223));//深蓝色
        Font font6 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(54,209,181));//浅绿色
        Font[] fonts = {font0, font1, font2, font3, font4,font5,font6};


        //创建一个document对象
        document = new Document(new Rectangle(PageSize.A4), 0, 0, 0, 0);
        document.setPageCount(1);
        //为document创建一个监听，并把pdf流写入到ByteArrayOutputStream流中
        PdfWriter write = PdfWriter.getInstance(document, ba);
        try {
            //打开文档
            document.open();
            document.addTitle("事件报告");

            //首部图示
            createPdfAbsoluteHead(document,fonts,"个人信息");

            //添加空行
            Paragraph blankLine2 = new Paragraph(50, " ", font1);
            document.add(blankLine2);

            //个人信息表
            createTableForPersonInfo(document,info,fonts,statusDataMap);

            //规则命中结果表
            String[] tableHeads1 = {"规则ID", "规则名称", "命中分数", "规则结果"};
            createTableForRulesResult(document, tableHeads1, 4, fonts,rulesList);

            if (!modelList.isEmpty()) {
                //模型结果表
                String[] tableHeads2 = {"规则ID", "模型名称", "模型评分", "模型结果"};
                createTableForModel(document, tableHeads2, 4, fonts, modelList);
            }

            //决策集信息表
            createTableForDecisionInfo(document, fonts,decisionId,decisionName);

            //决策图示
            showDecisionImage(document,fonts);

            //footer底部图示
            showFooter(document);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文档
            if (document != null) {
                if (document.isOpen()) {
                    document.close();
                }
            }
            return ba;
        }
    }

    /**
     * 创建在线决策结果pdf模板
     *
     * @param  map
     * @throws DocumentException
     * @throws IOException
     */
    public static ByteArrayOutputStream createAbTestPdfResult(Map<String,Object> map)
            throws DocumentException, IOException {

        //基本设置
        Document document = null;
        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        //设置字体
        if (isExistsFontResource()){
            log.info("加载字体成功");
        }else{
            log.info("加载字体失败，默认使用宋体");
        }
        Font font0 = new Font();
        Font font1 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.BLACK);
        Font font2 = setFontSize(FONT_SIZE[1], Font.NORMAL, new BaseColor(103, 107, 113));  //浅灰色
        Font font3 = setFontSize(FONT_SIZE[7], Font.BOLD, BaseColor.WHITE);
        Font font4 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.RED);
        Font font5 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(75,101,223));//深蓝色
        Font font6 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(54,209,181));//浅绿色
        Font[] fonts = {font0, font1, font2, font3, font4,font5,font6};


        //创建一个document对象
        document = new Document(new Rectangle(PageSize.A4), 0, 0, 0, 0);
        document.setPageCount(1);
        //为document创建一个监听，并把pdf流写入到ByteArrayOutputStream流中
        PdfWriter write = PdfWriter.getInstance(document, ba);
        try {
            //打开文档
            document.open();

            //首部图示
            createPdfAbsoluteHead(document,fonts,"决策图示");

            //添加空行
            Paragraph blankLine2 = new Paragraph(50, " ", font1);
            document.add(blankLine2);

            Image image = Image.getInstance(RESULT_IMAGE);
            image.scaleAbsolute(image.getWidth()*(float)0.5,image.getHeight()*(float)0.5);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

            //footer底部图示
            showFooter(document);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文档
            if (document != null) {
                if (document.isOpen()) {
                    document.close();
                }
            }
            return ba;
        }
    }

    /**
     * 创建在线决策结果pdf模板
     *
     * @param  map
     * @throws DocumentException
     * @throws IOException
     */
    public static ByteArrayOutputStream createPdf(Map<String,Object> map)
            throws DocumentException, IOException {

        //pdf数据
        EngineDecisionLog data = (EngineDecisionLog)map.get("data");
        String parameters = data.getParameters();
        Map<String,Object> info = JSONUtil.jsonToMap(parameters);
        List<ExcuteFlowForApi> decisions = (List<ExcuteFlowForApi>)map.get("decisions");
        /**映射：rulesId——rulesName*/
        Map<Long,String> rulesNamesMap = ( Map<Long,String>)map.get("rulesNamesMap");
        /**映射：rulesId——rulesMatchType*/
        Map<Long,Integer> rulesMatchTypeMap = (Map<Long,Integer>)map.get("rulesMatchTypeMap");
        Integer decisonId = data.getDecisionId();
        Map<String,Object> statusDataMap = new HashMap<>();
        String decisionName = (String)map.get("decisionName");
        Integer hitNumber = (Integer)map.get("count");
        statusDataMap.put("hitNumber",hitNumber);
        statusDataMap.put("status",data.getSysStatus());
        //规则集list
        List<Map<String,Object>> rulesList = new ArrayList<>();
        //模型list
        List<Map<String,Object>> modelList = new ArrayList<>();
        for (int i = 0; i < decisions.size();i++){
            Long rulesId = decisions.get(i).getRulesId();
            String rulesName = rulesNamesMap.get(rulesId);
            Integer score = decisions.get(i).getScore();
            Integer status = decisions.get(i).getStatus();
            if (rulesMatchTypeMap.get(rulesId) == 2){
                Map<String,Object> model = new HashedMap();
                model.put("rulesId",rulesId);
                model.put("rulesName",rulesName);
                model.put("score",score);
                model.put("status",status);
                modelList.add(model);
            }else{
                Map<String,Object> rules = new HashedMap();
                rules.put("rulesId",rulesId);
                rules.put("rulesName",rulesName);
                rules.put("score",score);
                rules.put("status",status);
                rulesList.add(rules);
                }
            }

        //基本设置
        Document document = null;
        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        //设置字体
        if (isExistsFontResource()){
            log.info("加载字体成功");
        }else{
            log.info("加载字体失败，默认使用宋体");
        }
        Font font0 = new Font();
        Font font1 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.BLACK);
        Font font2 = setFontSize(FONT_SIZE[1], Font.NORMAL, new BaseColor(103, 107, 113));  //浅灰色
        Font font3 = setFontSize(FONT_SIZE[7], Font.BOLD, BaseColor.WHITE);
        Font font4 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.RED);
        Font font5 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(75,101,223));//深蓝色
        Font font6 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(54,209,181));//浅绿色
        Font[] fonts = {font0, font1, font2, font3, font4,font5,font6};


        //创建一个document对象
        document = new Document(new Rectangle(PageSize.A4), 0, 0, 0, 0);
        document.setPageCount(1);
        //为document创建一个监听，并把pdf流写入到ByteArrayOutputStream流中
        PdfWriter write = PdfWriter.getInstance(document, ba);
        try {
            //打开文档
            document.open();
            document.addTitle("事件报告");

            //首部图示
            createPdfAbsoluteHead(document,fonts,"个人信息");

            //添加空行
            Paragraph blankLine2 = new Paragraph(50, " ", font1);
            document.add(blankLine2);

            //个人信息表
            createTableForPersonInfo(document,info,fonts,statusDataMap);

            //规则命中结果表
            String[] tableHeads1 = {"规则集ID", "规则集名称", "命中分数", "规则集结果"};
            createTableForRulesResult(document, tableHeads1, 4, fonts,rulesList);

            if (!modelList.isEmpty()) {
                //模型结果表
                String[] tableHeads2 = {"规则ID", "模型名称", "模型评分", "模型结果"};
                createTableForModel(document, tableHeads2, 4, fonts, modelList);
            }

            //决策集信息表
            createTableForDecisionInfo(document, fonts,decisonId,decisionName);

            //决策图示
            showDecisionImage(document,fonts);

            //footer底部图示
            showFooter(document);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文档
            if (document != null) {
                if (document.isOpen()) {
                    document.close();
                }
            }
            return ba;
        }
    }

    /**
     * 创建pdf绝对定位首部图示（固定位置展示）
     * @param document
     * @param fonts
     * @throws DocumentException
     */
    private static void createPdfAbsoluteHead(Document document,Font[] fonts,String tableName) throws DocumentException {
        //加头部背景图片
        Image headImage = PdfUtil.byte2Image(PdfUtil.file2Bytes(HEAD_IMAGE));
        headImage.scaleAbsolute(595, 47);
        headImage.setAlignment(Image.UNDERLYING);
        headImage.setAbsolutePosition(0f, 842 - 47);
        document.add(headImage);

        //事件报告四个字
        Image eventReport = PdfUtil.byte2Image(PdfUtil.file2Bytes(EVENT_REPORT_IMAGE));
        eventReport.scaleAbsolute(94, 22);
        eventReport.setAlignment(Image.UNDERLYING);
        eventReport.setAbsolutePosition(251, 842 - 53 - 22);
        document.add(eventReport);

        //icon
        Image leftLeaf = PdfUtil.byte2Image(PdfUtil.file2Bytes(LEFT_LEAF_IMAGE));
        leftLeaf.scaleAbsolute(15.4f, 10.3f);
        leftLeaf.setAbsolutePosition(220.2f, (float) (842 - 59.2 - 10.3));
        document.add(leftLeaf);

        Image rightLeaf = PdfUtil.byte2Image(PdfUtil.file2Bytes(RIGHT_LEAF_IMAGE));
        rightLeaf.scaleAbsolute(15.4f, 10.3f);
        rightLeaf.setAbsolutePosition(356.4f, (float) (842 - 59.2 - 10.3));
        document.add(rightLeaf);

        Image icon1 = PdfUtil.byte2Image(PdfUtil.file2Bytes(ICON_IMAGE));
        icon1.scaleAbsolute(8, 8);
        icon1.setAbsolutePosition(247, 842 - 112 - 8);
        document.add(icon1);

        Image icon2 = PdfUtil.byte2Image(PdfUtil.file2Bytes(ICON_IMAGE));
        icon2.scaleAbsolute(8, 8);
        icon2.setAbsolutePosition(335, 842 - 112 - 8);
        document.add(icon2);

        //添加空行
        Paragraph blankLine1 = new Paragraph(105, " ", fonts[1]);
        document.add(blankLine1);

        //添加个人信息四个字
        Paragraph p1 = new Paragraph(tableName, fonts[1]);
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);
    }

    /**
     * 创建个人信息表
     * @param document
     *              Document对象
     * @param info
     *              个人信息集合
     * @param fonts
     *              字体集合
     * @throws DocumentException
     * @throws IOException
     */
    private static void createTableForPersonInfo(Document document,Map<String,Object> info,Font[] fonts,Map<String,Object> statusDataMap) throws DocumentException, IOException {
        //个人信息表
        PdfPTable table1 = new PdfPTable(8);
        table1.setWidths(WIDTH_1);
        decorateTableBorderTop(table1,8);
        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();
        List<String> keyList = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();

        int tag = 0;
        for (Map.Entry<String,Object> entry : info.entrySet()){
            keyList.add(entry.getKey());
            valueList.add(entry.getValue());
            tag++;
            if (tag == 3){
                break;
            }
        }
        for (int i = 0; i < keyList.size(); i++){
            info.remove(keyList.get(i));
        }
        //1row
        table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft}));
        table1.addCell(createCell(keyList.size() > 0 ? keyList.get(0) : null, fonts[1], Element.ALIGN_LEFT, FIXED_HEIGHT));
        table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{}));
        table1.addCell(createCell(valueList.size() > 0 ? valueList.get(0) : null, fonts[2], Element.ALIGN_LEFT, FIXED_HEIGHT));
        //table1.addCell(createCell("命中风险数量", fonts[1], Element.ALIGN_CENTER, 3, FIXED_HEIGHT));
        PdfPCell emptyCell_tmp = new PdfPCell(new Phrase(""));
        emptyCell_tmp.setColspan(3);
        emptyCell_tmp.setRowspan(3);
        emptyCell_tmp.setBorder(Rectangle.NO_BORDER);
        table1.addCell(emptyCell_tmp);
        table1.addCell(createCell("决策建议", fonts[1], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
        //2row
        table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft}));
        table1.addCell(createCell(keyList.size() > 1 ? keyList.get(1) : null, fonts[1], Element.ALIGN_LEFT, FIXED_HEIGHT));
        table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{}));
        table1.addCell(createCell(valueList.size() > 1 ? valueList.get(1) : null, fonts[2], Element.ALIGN_LEFT, FIXED_HEIGHT));



       /* //待改进：pp1是强行挤出一段空间来保证中间单元格是正方形（数字背景是圆形）
        PdfPCell pp1 = new PdfPCell();
        pp1.setRowspan(2);
        pp1.setBorder(Rectangle.NO_BORDER);
        table1.addCell(pp1);
        // Paragraph num = new Paragraph(hitNumber.toString(), font3);
        PdfPCell numCell = new PdfPCell();
        numCell.setRowspan(2);
        ImageEvent imageEvent = new ImageEvent(PdfUtil.byte2Image(PdfUtil.file2Bytes(CIRCLE_IMAGE)));
        numCell.setCellEvent(imageEvent);
        numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        numCell.setBorder(Rectangle.NO_BORDER);
        table1.addCell(numCell);
        table1.addCell(pp1);
*/
        //结果
        PdfPCell resultCell = resultCell((Integer) statusDataMap.get("status"),fonts,2);
        resultCell.setCellEvent(cellBorderRight);
        table1.addCell(resultCell);

        //3 row
        table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft}));
        table1.addCell(createCell(keyList.size() > 2 ? keyList.get(2) : null, fonts[1], Element.ALIGN_LEFT, FIXED_HEIGHT));
        table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{}));
        table1.addCell(createCell(valueList.size() > 2 ? valueList.get(2) : null, fonts[2], Element.ALIGN_LEFT, FIXED_HEIGHT));

        for (String key : info.keySet()){
           /* String title = "";
            String content = "";
            if (key.equalsIgnoreCase("workLongitude")){
                title = "工作地";
                content = (String)info.get(key);
            }else if (key.equalsIgnoreCase("liveLongitude")){
                title = "现居地";
                content = (String)info.get(key);
            }else if (key.equalsIgnoreCase("cycle")){
                title = "周期";
                content = (String)info.get(key);
            }else{
                continue;
            }*/
            //n row
            table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft}));
            table1.addCell(createCell(key, fonts[1], Element.ALIGN_LEFT, FIXED_HEIGHT));
            table1.addCell(decorateEmptyCell(new PdfPCellEvent[]{}));
            table1.addCell(createCell(info.get(key), fonts[2], Element.ALIGN_LEFT, FIXED_HEIGHT));
            //空单元格填充
            PdfPCell emptyCell= new PdfPCell(new Phrase(""));
            emptyCell.setColspan(3);
            emptyCell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(emptyCell);
            table1.addCell(createCell("", fonts[1], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));

        }


        decorateTableBorderBottom(table1,8);
        document.add(table1);
    }

    /**
     * 构建规则命中结果表
     * @param document
     * @param tableHeads
     * @param numColumns
     * @param fonts
     * @param rulesList
     * @throws DocumentException
     */
    private static void createTableForRulesResult(Document document, String[] tableHeads, int numColumns, Font[] fonts, List<Map<String,Object>> rulesList) throws DocumentException, IOException {
        //添加表名：规则命中结果
        drawTableTitile(document, "    "+"规则集命中结果", fonts[1], TABLE_TITLE_SPACE,-85,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_3);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);

        //设置表头
        for (int i = 0; i < tableHeads.length;i++){
            if (i==0){
                table.addCell(createCell(tableHeads[i],fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
            }else if (i == tableHeads.length-1){
                table.addCell(createCell(tableHeads[i],fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
            }else{
                table.addCell(createCell(tableHeads[i],fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT));
            }
        }
        if(rulesList.isEmpty()){
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT));
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT));
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
        } else {
            for (int i = 0; i < rulesList.size(); i++) {

                Map<String,Object> rules = rulesList.get(i);
                table.addCell(createCell(rules.get("rulesId"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
                table.addCell(createCell(rules.get("rulesName"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
                table.addCell(createCell(rules.get("score"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
                PdfPCell resultCell = resultCell((Integer)rules.get("status"),fonts,1);
                resultCell.setFixedHeight(FIXED_HEIGHT);
                resultCell.setCellEvent(cellBorderRight);
                table.addCell(resultCell);
            }
        }
        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }

    /**
     * 模型结果表(待添加)
     * @param document               pdf文档
     * @param tableHeads            表头名称
     * @param numColumns            指定列数
     * @param fonts                 字体集合
     * @throws DocumentException
     */
    private static void createTableForModel(Document document,String[] tableHeads,int numColumns,Font[] fonts,List<Map<String,Object>> modelList) throws DocumentException, IOException {

        //添加表名：模型结果
        drawTableTitile(document, "模型结果", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_3);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);
        //设置表头
        for (int i = 0; i < tableHeads.length;i++){
            if (i==0){
                table.addCell(createCell(tableHeads[i],fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
            }else if (i == tableHeads.length-1){
                table.addCell(createCell(tableHeads[i],fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
            }else{
                table.addCell(createCell(tableHeads[i],fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT));
            }
        }
        //设置数据
        if(modelList.isEmpty()){
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT));
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT));
            table.addCell(createCell(null,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
        } else {
            for (int i = 0; i < modelList.size(); i++) {

                Map<String,Object> models = modelList.get(i);
                table.addCell(createCell(models.get("rulesId"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
                table.addCell(createCell(models.get("rulesName"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
                table.addCell(createCell(models.get("score"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
                PdfPCell resultCell = resultCell((Integer)models.get("status"),fonts,1);
                resultCell.setFixedHeight(FIXED_HEIGHT);
                resultCell.setCellEvent(cellBorderRight);
                table.addCell(resultCell);
            }
        }
        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }

    /**
     * 决策集信息表
     * @param document 文档
     * @param fonts    字体集
     * @throws DocumentException
     */
    public static void createTableForDecisionInfo(Document document,Font[] fonts,Integer id,String name) throws DocumentException, IOException {
        //添加表名：决策集信息
        drawTableTitile(document, "决策集信息", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(4);
        table.setWidths(WIDTH_2);
        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();
        decorateTableBorderTop(table,4);

        table.addCell(createCell("决策集ID", fonts[1], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell(id, fonts[2], Element.ALIGN_LEFT, FIXED_HEIGHT));
        table.addCell(createCell("名称", fonts[1], Element.ALIGN_CENTER, FIXED_HEIGHT));
        table.addCell(createCell(name, fonts[2], Element.ALIGN_LEFT, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));

        decorateTableBorderBottom(table,4);

        document.add(table);

    }

    /**
     * 加载决策图示
     * @param document
     * @param fonts
     * @throws IOException
     * @throws DocumentException
     */
    private static void showDecisionImage(Document document,Font[] fonts) throws IOException, DocumentException {
         //决策图示标题
        drawTableTitile(document, "决策图示", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        Image image = Image.getInstance(RESULT_IMAGE);
        ImageEvent event = new ImageEvent(image);
        cell.setCellEvent(event);
        cell.setCellEvent(new CustomCellBorderLeft());
        cell.setCellEvent(new CustomCellBorderRight());
        cell.setCellEvent(new CustomCellBorderTop());
        cell.setCellEvent(new CustomCellBorderBottom());
        cell.setFixedHeight(380);
        table.addCell(cell);
        document.add(table);
    }

    /**
     * 展示页脚
     * @param document
     * @throws DocumentException
     */
    private static void showFooter(Document document) throws DocumentException {
        Image tailImage = PdfUtil.byte2Image(PdfUtil.file2Bytes(TAIL_IMAGE));
        tailImage.scaleAbsolute(595, 47);
        tailImage.setAbsolutePosition(0,0);
        document.add(tailImage);
    }

    /**
     * 将结果修饰成建议（添加logo）
     * @param status
     * @param fonts
     * @param rowNum
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    public static PdfPCell resultCell(Integer status,Font[] fonts,Integer rowNum) throws IOException, BadElementException {
        Paragraph suggestMessage = new Paragraph();
        Image suggestIcon = null;
        Chunk resultChunk = null;
        if (status == 1) {
            resultChunk = new Chunk("  "+"通过",fonts[6]);
            suggestIcon = PdfUtil.byte2Image(PdfUtil.file2Bytes(RIGHT_IMAGE));
        } else if (status == 2){
            resultChunk = new Chunk("  "+"人工审核",fonts[5]);
            suggestIcon = PdfUtil.byte2Image(PdfUtil.file2Bytes(REVIEW_IMAGE));
        }else if (status == 3){
            resultChunk = new Chunk("  "+"拒绝",fonts[4]);
            suggestIcon = PdfUtil.byte2Image(PdfUtil.file2Bytes(ERROR_IMAGE));
        }else if (status == 4){
            resultChunk = new Chunk("  "+"失效",fonts[4]);
            suggestIcon = PdfUtil.byte2Image(PdfUtil.file2Bytes(ERROR_IMAGE));
        }
        else if (status == 5){
            resultChunk = new Chunk("  "+"失败",fonts[4]);
            suggestIcon = PdfUtil.byte2Image(PdfUtil.file2Bytes(ERROR_IMAGE));
        }
        suggestIcon.scaleAbsolute(8, 8);
        suggestMessage.add(new Chunk(suggestIcon, 0, 0, true));
        suggestMessage.add(resultChunk);
        PdfPCell resultCell = new PdfPCell(suggestMessage);
        resultCell.setRowspan(rowNum);
        resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        resultCell.setBorder(Rectangle.NO_BORDER);
        return resultCell;
    }


    /**
     * 单独设置一行单元格（最顶端）来描绘表格外边框
     * @param table
     * @param numColumns
     */
    private static void decorateTableBorderTop(PdfPTable table,int numColumns){
        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();
        CustomCellBorderTop cellBorderTop = new CustomCellBorderTop();
        for (int i = 0; i < numColumns;i++){
            if (i == 0){
                table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft,cellBorderTop}));
            }else if (i == numColumns-1){
                table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderRight,cellBorderTop}));
            }else{
                table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderTop}));
            }
        }
    }

    /**
     * 单独设置一行单元格（最底端）来描绘表格外边框
     * @param table
     * @param numColumns
     */
    private static void decorateTableBorderBottom(PdfPTable table,int numColumns){
        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();
        CustomCellBorderBottom cellBorderBottom = new CustomCellBorderBottom();
        for (int i = 0; i < numColumns;i++){
            if (i == 0){
                table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft,cellBorderBottom}));
            }else if (i == numColumns-1){
                table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderRight,cellBorderBottom}));
            }else{
                table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderBottom}));
            }
        }
    }

    /**
     * 修饰空单元格：描绘表格虚线
     * @param events
     * @return
     */
    private static PdfPCell decorateEmptyCell(PdfPCellEvent[] events){
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setFixedHeight(FIXED_HEIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        for (int i = 0; i < events.length; i++){
            cell.setCellEvent(events[i]);
        }
        return cell;
    }


    /**
     * 用于生成cell，并且设置指定虚线边框
     * @param text
     * @param font
     * @param align
     * @param fixed
     * @param events   设置虚线边框事件
     * @return
     */
    private static PdfPCell createCell(Object text,Font font,Integer align,float fixed,PdfPCellEvent[] events){
        PdfPCell cell = createCell(text, font, null, align, null,BaseColor.WHITE,Rectangle.NO_BORDER, fixed);
        for (int i = 0; i < events.length;i++){
            cell.setCellEvent(events[i]);
        }
        return cell;
    }

    /**
     * 用于生成cell
     * @param text
     * @param font
     * @param fixed
     * @return
     */
    private static PdfPCell createCell(Object text,Font font,Integer align,float fixed){
        return createCell(text, font, null, align, null,
                BaseColor.WHITE,Rectangle.NO_BORDER, fixed);
    }

    /**
     * 用于生成cell
     * @param text
     * @param font
     * @param align
     * @param colspan
     * @param fixed
     * @return
     */
    private static PdfPCell createCell(Object text,Font font,Integer align,Integer colspan,float fixed){
        return createCell(text, font, colspan, align, null,
                BaseColor.WHITE,Rectangle.NO_BORDER, fixed);
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
     * 设置表头名字
     * @param document  pdf文档
     * @param name      表名
     * @param font      字体
     * @param space     表名上下距离
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    public static void drawTableTitile(Document document,String name,Font font,Integer space,Integer leftOffsetX,Integer rightOffsetX) throws IOException, DocumentException {
        //上间距
        Paragraph blankLineUp = new Paragraph(space," ",font);
        document.add(blankLineUp);

        PdfPTable titleTable = new PdfPTable(1);
        PdfPCell titleCell = new PdfPCell();
        Paragraph pTitle = new Paragraph(name,font);
        pTitle.setAlignment(Element.ALIGN_CENTER);
        Image icon3 = PdfUtil.byte2Image(PdfUtil.file2Bytes(ICON_IMAGE));
        icon3.scaleAbsolute(8,8);
        pTitle.add(new Chunk(icon3,leftOffsetX,0,true));
        pTitle.add(new Chunk(icon3,rightOffsetX,0,true));
        titleCell.addElement(pTitle);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleTable.addCell(titleCell);

        document.add(titleTable);

        //下间距
        Paragraph blankLineDown = new Paragraph(space," ",font);
        document.add(blankLineDown);
    }

    /**
     * 判断字体资源是否存在
     *
     * @return
     */
    public static boolean isExistsFontResource() {
        boolean flag = true;
        Resource resource = new ClassPathResource(FONT_RESOURCE);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            if (br == null) {
                flag = false;
            }
        } catch (IOException e) {
            //log.info("字体资源不存在");
            flag = false;
        }
        return flag;
    }

    /**
     * 设置中文字体
     *
     * @return
     */
    private static BaseFont setChineseFont() {
        if (fontChinese == null) {
            try {
                if (isExistsFontResource()) {
                    fontChinese = BaseFont.createFont(FONT_RESOURCE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    // log.info("加载字体资源成功");
                } else {
                    fontChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    //log.info("加载字体资源失败，默认使用宋体");
                }
            } catch (Exception e) {
                //log.info("加载字体资源失败，默认使用宋体");
                try {
                    fontChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        return fontChinese;
    }

    private static Font setFontSize(float size,int style,BaseColor color){
        Font f = new Font(setChineseFont(),size,style,color);
        return f;
    }

    /**
     * 图片填充单元格event class
     */
    static class ImageEvent implements PdfPCellEvent {
        protected Image img;
        public ImageEvent(Image img) {
            this.img = img;
        }
        @Override
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            img.scaleAbsolute(position.getWidth(), position.getHeight());
            img.setAbsolutePosition(position.getLeft(), position.getBottom());
            PdfContentByte canvas = canvases[PdfPTable.BACKGROUNDCANVAS];
            try {
                canvas.addImage(img);
            } catch (DocumentException ex) {
                // do nothing
            }
        }
    }


    /**
     * class CustomCellBorderTop、CustomCellBorderBottom、CustomCellBorderLeft、CustomCellBorderRight
     * 自定义设置单元格Cell的上、下、左、右边框为蓝色虚线
     */
    static class CustomCellBorderTop implements PdfPCellEvent{

        @Override
        public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentBytes) {
            PdfContentByte cb = pdfContentBytes[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.8f);  //设置虚线大小
            cb.setLineDash(new float[]{1.0f,3.0f},0);   //第一个参数设置单个虚线的长度,第二个参数设置虚线之间的距离
            cb.moveTo(rectangle.getLeft(),rectangle.getTop());  //虚线起点：矩形左上角
            cb.lineTo(rectangle.getRight(),rectangle.getTop()); //虚线终点：矩形右上角
            cb.setColorStroke(new BaseColor(80,138,247));   //虚线颜色
            cb.stroke();
            cb.restoreState();
        }
    }

    static class CustomCellBorderBottom implements PdfPCellEvent{

        @Override
        public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentBytes) {
            PdfContentByte cb = pdfContentBytes[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.8f);
            cb.setLineDash(new float[]{1.0f,3.0f},0);
            cb.moveTo(rectangle.getLeft(),rectangle.getBottom());
            cb.lineTo(rectangle.getRight(),rectangle.getBottom());
            cb.setColorStroke(new BaseColor(80,138,247));
            cb.stroke();
            cb.restoreState();
        }
    }

    static class CustomCellBorderLeft implements PdfPCellEvent{

        @Override
        public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentBytes) {
            PdfContentByte cb = pdfContentBytes[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.8f);
            cb.setLineDash(new float[]{1.0f,3.0f},0);
            cb.moveTo(rectangle.getLeft(),rectangle.getTop());
            cb.lineTo(rectangle.getLeft(),rectangle.getBottom());
            cb.setColorStroke(new BaseColor(80,138,247));
            cb.stroke();
            cb.restoreState();
        }
    }

    static class CustomCellBorderRight implements PdfPCellEvent{

        @Override
        public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentBytes) {
            PdfContentByte cb = pdfContentBytes[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.8f);
            cb.setLineDash(new float[]{1.0f,3.0f},0);
            cb.moveTo(rectangle.getRight(),rectangle.getTop());
            cb.lineTo(rectangle.getRight(),rectangle.getBottom());
            cb.setColorStroke(new BaseColor(80,138,247));
            cb.stroke();
            cb.restoreState();
        }
    }


}

