package com.geo.rcs.common.util.pdf;


import com.geo.rcs.modules.engine.entity.Field;
import com.geo.rcs.modules.engine.entity.Rule;
import com.geo.rcs.modules.event.vo.EventReport;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;
import java.util.List;


/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/12/17  17:20
 **/
public class RulesPdfResult {

    /**图片资源路径*/
    private static final String HEAD_IMAGE = "static/pdf/image/head_image.png";
    private static final String TAIL_IMAGE = "static/pdf/image/tail_image.png";
    private static final String RULE_REPORT_IMAGE = "static/pdf/image/rule_report.png";
    private static final String LEFT_LEAF_IMAGE = "static/pdf/image/left_leaf.png";
    private static final String RIGHT_LEAF_IMAGE = "static/pdf/image/right_leaf.png";
    private static final String ICON_IMAGE = "static/pdf/image/icon_image.png";


    /**字体路径*/
    private static final String FONT_RESOURCE = "static/pdf/font/msyh.ttf";

    /**字体大小*/
    private static final int[] FONT_SIZE = {8, 9, 12, 14, 16, 18,20,22};
    private static BaseFont fontChinese = null;

    /** 单元格固定高度 */
    private static final Float FIXED_HEIGHT = 24.5f;

    private static final Float FIXED_HEIGHT_2 = 45f;

    /**默认表名上下间距*/
    private static final Integer TABLE_TITLE_SPACE = 30;

    /**表列比例*/
    private static final int[]
            WIDTH_1 = {7,11,7,13, 11,20, 11, 20},
            WIDTH_2= {17,23,20,20,20},
            WIDTH_3= {10,20,14,14,14,14,14},
            WIDTH_4 = {20,20,20,20,20},
            WIDTH_5={25,25,25,25};

    public static final Logger log = LoggerFactory.getLogger(RulesPdfResult.class);


    /**
     * 创建在线决策结果pdf模板
     *
     * @param  report
     * @throws DocumentException
     * @throws IOException
     */
    public static ByteArrayOutputStream createPdf(EventReport report)
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
        Font font4 = setFontSize(FONT_SIZE[1], Font.NORMAL, BaseColor.RED);      //红色
        Font font5 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(75,101,223));//深蓝色
        Font font6 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(54,209,181));//浅绿色
        Font font7 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(54,209,181));//浅绿色
        Font font8 = setFontSize(FONT_SIZE[1], Font.NORMAL,new BaseColor(255,163,158));//浅红色
        Font[] fonts = {font0, font1, font2, font3, font4,font5,font6,font7,font8};


        //创建一个document对象
        document = new Document(new Rectangle(PageSize.A4), 0, 0, 0, 0);
        document.setPageCount(1);
        //为document创建一个监听，并把pdf流写入到ByteArrayOutputStream流中
        PdfWriter write = PdfWriter.getInstance(document, ba);

        try {
            //打开文档
            document.open();
            document.addTitle("规则报告");

            //首部图示
            createPdfAbsoluteHead(document,fonts,"个人基本信息");

            //添加空行
            Paragraph blankLine2 = new Paragraph(50, " ", font1);
            document.add(blankLine2);

             //个人信息表
            createTableForPersonInfo(document,4,fonts,report);

            //模型结果
            if (report.getEngineRules().getMatchType() == 2){
                //模型结果
                createTableForModel(document,5,fonts,report);
            }else{
                //识别结果表
                createTableForDiscernResult(document,8,fonts,report);

                //规则集信息
                createTableForRulesInfo(document, 5, fonts,report);
            }


            //命中规则
            createTableForHitRule(document,7,fonts,report);

            //规则字段
            createTableForRuleFields(document,5,fonts,report);

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
        Image eventReport = PdfUtil.byte2Image(PdfUtil.file2Bytes(RULE_REPORT_IMAGE));
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

    private static void createTableForPersonInfo(Document document,int numColumns,Font[] fonts,EventReport report) throws DocumentException, IOException {

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_5);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);

        Map<String,String> param = report.getParamInfo();
        int i = 1;
        for (Map.Entry<String,String> entry : param.entrySet()){
            if (i % 2 != 0){
                table.addCell(createCell(entry.getKey(),fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ,new PdfPCellEvent[]{cellBorderLeft}));
                table.addCell(createCell(entry.getValue(),fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT ));
            }else{
                table.addCell(createCell(entry.getKey(),fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
                table.addCell(createCell(entry.getValue(),fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
            }

            i++;
        }

        if (i % 2 == 0){
            table.addCell(decorateEmptyCell(new PdfPCellEvent[]{}));
            table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderRight}));
        }

        if (param.size() == 0){
            table.addCell(createCell("暂无",fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell("",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
            table.addCell(createCell("",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
            table.addCell(createCell("",fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));
        }


        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }

    private static void createTableForDiscernResult(Document document,int numColumns,Font[] fonts,EventReport report) throws DocumentException, IOException {

        //data
        String resultRisk = "详见规则";
        String resultScore = String.valueOf(report.getEngineRules().getScore());
        String resultStatus = String.valueOf(report.getEngineRules().getStatus());
        if("1".equals(resultStatus)){
            resultStatus = "通过";
        }else if("2".equals(resultStatus)){
            resultStatus = "人工审核";
        }else if("3".equals(resultStatus)){
            resultStatus = "拒绝";
        }else if("4".equals(resultStatus)){
            resultStatus = "失效";
        }else{
             resultStatus = "失败";
        }

        //添加表名：识别结果
        drawTableTitile(document, "识别结果", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_1);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);

        //1 row
        table.addCell(decorateEmptyCell(new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell("命中风险",fonts[1],Element.ALIGN_LEFT,FIXED_HEIGHT ));
        table.addCell(decorateEmptyCell(new PdfPCellEvent[]{}));
        table.addCell(createCell(resultRisk,fonts[2],Element.ALIGN_LEFT,FIXED_HEIGHT));
        table.addCell(createCell("识别分数",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell(resultScore,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT));
        table.addCell(createCell("结果建议",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell(resultStatus,fonts[2],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));


        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }


    private static void createTableForRulesInfo(Document document, int numColumns, Font[] fonts, EventReport report) throws DocumentException, IOException {

        //data
        String ruleSetMatchType = report.getEngineRules().getMatchType()==0 ? "权重匹配":"最坏匹配";

        //添加表名：规则集信息
        drawTableTitile(document, "规则集信息", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_2);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);

        //1 row
        table.addCell(createCell("规则集ID",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell("名称",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("匹配模式",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("最小阈值",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("最大阈值",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight} ));


        if (report.getEngineRules().getId() == null){
            table.addCell(createCell("-",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell("-",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
            table.addCell(createCell("-",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
            table.addCell(createCell("-",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
            table.addCell(createCell("-",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight} ));
        }else {
            //2row
            table.addCell(createCell(String.valueOf(report.getEngineRules().getId()), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT, new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell(report.getEngineRules().getName(), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
            table.addCell(createCell(ruleSetMatchType, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
            table.addCell(createCell(String.valueOf(report.getEngineRules().getThresholdMin()), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
            table.addCell(createCell(String.valueOf(report.getEngineRules().getThresholdMax()), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT, new PdfPCellEvent[]{cellBorderRight}));
        }



        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }

    private static void createTableForModel(Document document, int numColumns, Font[] fonts, EventReport report) throws DocumentException, IOException {

        //Data
        String resultStatus = String.valueOf(report.getEngineRules().getStatus());
        if("1".equals(resultStatus)){
            resultStatus = "通过";
        }else if("2".equals(resultStatus)){
            resultStatus = "人工审核";
        }else if("3".equals(resultStatus)){
            resultStatus = "拒绝";
        }else {
            resultStatus = "失效";
        }

        List<Field> fieldList = report.getFieldList();
        String modelScore = "无";
        modelScore = fieldList.get(0).getValue();

        //添加表名：规则集信息
        drawTableTitile(document, "模型信息", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_2);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);

        //1 row
        table.addCell(createCell("模型ID",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell("模型名称",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("匹配模式",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("模型分数",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("模型结果",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight} ));

        //2row
        table.addCell(createCell(String.valueOf(report.getEngineRules().getId()), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell(report.getEngineRules().getName(), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
        table.addCell(createCell("衍生模型", fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
        table.addCell(createCell(modelScore, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT));
        table.addCell(createCell(resultStatus, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight}));


        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }

    private static void createTableForHitRule(Document document,int numColumns,Font[] fonts,EventReport report) throws DocumentException, IOException {

        //data
        java.util.List<Rule> ruleList = report.getEngineRules().getRuleList();

        //添加表名：命中规则
        drawTableTitile(document, "命中规则", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_3);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);

        //1 row
        table.addCell(createCell("规则ID",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell("规则名称",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("规则阈值",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("规则风险",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("命中结果",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("命中得分",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT ));
        table.addCell(createCell("命中风险",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT,new PdfPCellEvent[]{cellBorderRight} ));

        if (ruleList == null || ruleList.isEmpty()) {
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2,new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
            table.addCell(createCell(null, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2,new PdfPCellEvent[]{cellBorderRight}));
        }else {
            String ruleId = "";
            String ruleName = "";
            String ruleThreshold = "";
            String ruleLevel = "";
            String conditionRelationShip = "";
            String ruleScore = "";
            String ruleArrow = "";
            String ruleArrowLevel = "";
            Font font = null;

            List<Rule> listScoreUp = new ArrayList<>();
            List<Rule> listScoreDown = new ArrayList<>();
           for (Rule rule : ruleList){
               if (rule.getScore() > 0){
                   listScoreUp.add(rule);
               }else{
                   listScoreDown.add(rule);
               }
           }

            //对listScoreUp进行排序。根据level排序
            Collections.sort(listScoreUp, new Comparator<Rule>() {
                @Override
                public int compare(Rule o1, Rule o2) {
                    Integer level1 = o1.getLevel();
                    Integer level2 = o2.getLevel();
                    return level2.compareTo(level1);
                }
            });

            ruleList.clear();
            ruleList.addAll(listScoreUp);
            ruleList.addAll(listScoreDown);
            for (Rule rule : ruleList) {

                if (rule.getLevel() == 1) {
                    ruleLevel = "无风险";
                } else if (rule.getLevel() == 2) {
                    ruleLevel = "低风险";
                } else if (rule.getLevel() == 3) {
                    ruleLevel = "高风险";
                } else {
                    ruleLevel = "未设定";
                }

                ruleId = String.valueOf(rule.getId());
                ruleName = String.valueOf(rule.getName());
                ruleThreshold = String.valueOf(rule.getThreshold());
                conditionRelationShip = rule.getConditionRelationShip();
                ruleScore = String.valueOf(rule.getScore());
                if (rule.getScore() > 0) {
                    ruleArrow = "命中";
                    ruleArrowLevel = ruleLevel;
                    if (ruleArrowLevel.equalsIgnoreCase("高风险")){
                        font = fonts[4];
                    }else if (ruleArrowLevel.equalsIgnoreCase("低风险")){
                        font = fonts[8];
                    }else{
                        font = fonts[2];
                    }
                } else {
                    /*if ((report.getEngineRules().getStatus() == 2 || report.getEngineRules().getStatus() == 4)
                            && report.getEngineRules().getScore() == 0) {
                        ruleArrow = "需要人工识别";
                        ruleArrowLevel = "需要人工识别";
                    } else {
                        ruleArrow = "未命中";
                        ruleArrowLevel = "无风险";
                    }*/

                    ruleArrow = "未命中";
                    ruleArrowLevel = "-";
                    font = fonts[2];
                }

                table.addCell(createCell(ruleId, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2, new PdfPCellEvent[]{cellBorderLeft}));
                table.addCell(createCell(ruleName, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                table.addCell(createCell(ruleThreshold, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                table.addCell(createCell(ruleLevel, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                table.addCell(createCell(ruleArrow, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                table.addCell(createCell(ruleScore, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                table.addCell(createCell(ruleArrowLevel, font, Element.ALIGN_CENTER, FIXED_HEIGHT_2, new PdfPCellEvent[]{cellBorderRight}));

            }
        }

        decorateTableBorderBottom(table,numColumns);
        document.add(table);
    }

    private static void createTableForRuleFields(Document document,int numColumns,Font[] fonts,EventReport report) throws DocumentException, IOException {

        //data
        List<Map<String, Object>> productFieldList = report.getProductFieldList();

        //添加表名：规则字段
        drawTableTitile(document, "规则字段", fonts[1], TABLE_TITLE_SPACE,-70,10);

        PdfPTable table = new PdfPTable(numColumns);
        table.setWidths(WIDTH_4);

        CustomCellBorderLeft cellBorderLeft = new CustomCellBorderLeft();
        CustomCellBorderRight cellBorderRight = new CustomCellBorderRight();

        decorateTableBorderTop(table,numColumns);
        table.addCell(createCell("接口名称",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2,new PdfPCellEvent[]{cellBorderLeft}));
        table.addCell(createCell("输出字段",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2 ));
        table.addCell(createCell("字段说明",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2 ));
        table.addCell(createCell("字段结果",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2 ));
        table.addCell(createCell("结果描述",fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2,new PdfPCellEvent[]{cellBorderRight} ));

        if (productFieldList == null || productFieldList.isEmpty()) {
            table.addCell(createCell(null,fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2,new PdfPCellEvent[]{cellBorderLeft}));
            table.addCell(createCell(null,fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2 ));
            table.addCell(createCell(null,fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2 ));
            table.addCell(createCell(null,fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2 ));
            table.addCell(createCell(null,fonts[1],Element.ALIGN_CENTER,FIXED_HEIGHT_2,new PdfPCellEvent[]{cellBorderRight} ));
        }else{
            if (report.getEngineRules().getMatchType() == 2){
                for (Field field : report.getFieldList()){
                    table.addCell(createCell(report.getFieldDesc().get("innerName"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2, new PdfPCellEvent[]{cellBorderLeft}));
                    table.addCell(createCell(field.getFieldName(), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                    table.addCell(createCell(report.getFieldDesc().get("fieldDesc"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                    table.addCell(createCell(field.getValue(), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                    String desc = field.getValueDesc().equals("") ? "暂无" : field.getValueDesc();
                    table.addCell(createCell(desc, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2, new PdfPCellEvent[]{cellBorderRight}));
                }
            }else {
                for (Map<String, Object> productField : productFieldList) {
                    String interfaceName = productField.get("interfaceName") == null ? "暂无" : (String) productField.get("interfaceName");
                    table.addCell(createCell(interfaceName, fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2, new PdfPCellEvent[]{cellBorderLeft}));
                    table.addCell(createCell(productField.get("fieldName"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                    table.addCell(createCell(productField.get("fieldNameDesc"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                    table.addCell(createCell(productField.get("verificationResult"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2));
                    table.addCell(createCell(productField.get("verificationResultDesc"), fonts[2], Element.ALIGN_CENTER, FIXED_HEIGHT_2, new PdfPCellEvent[]{cellBorderRight}));
                }
            }
        }


        decorateTableBorderBottom(table,numColumns);
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
    private static PdfPCell createCell(Object text,Font font,Integer align,Integer colspan,float fixed,PdfPCellEvent[] events){
        PdfPCell cell =  createCell(text, font, colspan, align, null,
                BaseColor.WHITE,Rectangle.NO_BORDER, fixed);
        for (int i = 0; i < events.length;i++){
            cell.setCellEvent(events[i]);
        }
        return cell;
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
