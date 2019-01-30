package com.geo.rcs.common.util.mail.impl;

import com.alibaba.fastjson.JSONArray;
import com.geo.rcs.common.util.mail.MailService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:10 2018/7/23
 */
@Service
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;
    private Context context = new Context();

    @Value("${spring.mail.fromMail.addr}")
    private String from;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendMail(String type, Map<String, Object> parmMap) throws Exception {


        /**
         * 获取最基本的参数 收件人,主题
         */

        String[] to = {};
        String[] bcc = {};
        String[] cc = {};
        if (parmMap.get("to") != null && parmMap.get("to") instanceof JSONArray) {
            to = ((JSONArray) parmMap.get("to")) == null ? null : ((JSONArray) parmMap.get("to")).toArray(to);
            bcc = ((JSONArray) parmMap.get("bcc")) == null ? null : ((JSONArray) parmMap.get("bcc")).toArray(bcc);
            cc = ((JSONArray) parmMap.get("cc")) == null ? null : ((JSONArray) parmMap.get("cc")).toArray(cc);
        }else if (parmMap.get("to") != null && parmMap.get("to") instanceof String[]){
            to=((String[]) parmMap.get("to")) == null ? null : (String[]) parmMap.get("to");
            bcc=((String[]) parmMap.get("bcc")) == null ? null : (String[]) parmMap.get("bcc");
            cc=((String[]) parmMap.get("cc")) == null ? null : (String[]) parmMap.get("cc");
        }
        String subject = (String) parmMap.get("subject");
        Boolean validateBlank = validateBlank("to", subject);
        if (validateBlank) {
            String content = (String) parmMap.get("content");
            logger.error("邮件收件人/主题都不能为空");
            throw new Exception("邮件收件人/主题都不能为空");
        }
        if (MailService.TEXT_MAIL.equalsIgnoreCase(type)) {
            String content = (String) parmMap.get("content");
            if (validateBlank(content)) {
                logger.error("邮件内容不能为空");
                throw new Exception("邮件内容不能为空");
            }
            sendSimpleMail(to, bcc, cc, subject, content);
        } else if (MailService.HTML_MAIL.equalsIgnoreCase(type)) {
            String content = (String) parmMap.get("content");
            if (validateBlank(content)) {
                logger.error("邮件内容不能为空");
                throw new Exception("邮件内容不能为空");
            }
            sendHtmlMail(to, bcc, cc, subject, content);

        } else if (MailService.ATTACHMENT_MAIL.equalsIgnoreCase(type)) {
            String content = (String) parmMap.get("content");
            String filePath = (String) parmMap.get("filePath");
            if (validateBlank(content, filePath)) {
                logger.error("邮件内容,附件路径都不能为空");
                throw new Exception("邮件内容,附件路径都不能为空");
            }
            sendAttachmentsMail(to, bcc, cc, subject, content, filePath);
        } else if (MailService.STATIC_MAIL.equalsIgnoreCase(type)) {
            String content = (String) parmMap.get("content");
            String imgPath = (String) parmMap.get("imgPath");
            if (validateBlank(content, imgPath)) {
                logger.error("邮件内容,静态资源路径都不能为空");
                throw new Exception("邮件内容,静态资源路径都不能为空");
            }
            sendInlineResourceMail(to, bcc, cc, subject, content, imgPath);
        } else if (MailService.TEMPLATE_MAIL.equalsIgnoreCase(type)) {
            String fileName = (String) parmMap.get("fileName");
            String parmName = (String) parmMap.get("parmName");
            String value = (String) parmMap.get("value");
            if (validateBlank(fileName, parmName, value)) {
                logger.error("邮件模板文件名,参数名,参数值都不能为空");
                throw new Exception("邮件模板文件名,参数名,参数值都不能为空");
            }
            sendTemplateMail(to, bcc, cc, subject, fileName, parmName, value);
        }else if (MailService.ALARM_MAIL.equalsIgnoreCase(type)) {
            String[] content = (String[]) parmMap.get("content");
            String filePath = (String) parmMap.get("filePath");
            if (validateBlankForArray(content) && validateBlank(filePath)) {
                logger.error("邮件内容,附件路径都不能为空");
                throw new Exception("邮件内容,附件路径都不能为空");
            }
            String fileName = filePath.substring(filePath.lastIndexOf("【"));
            sendAlarmMail(to, bcc, cc, subject, content, filePath, fileName);
        }else {
            logger.error("暂不支持该邮件类型:" + type);
            throw new Exception("暂不支持该邮件类型:" + type);
        }
    }

    /**
     * 发送文本邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendSimpleMail(String[] to, String[] bcc, String[] cc, String subject, String content) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        sendByBccOrCC(bcc, cc, message);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
            throw e;
        }

    }

    private void sendByBccOrCC(String[] bcc, String[] cc, SimpleMailMessage message) {
        if (bcc != null && bcc.length > 0) {
            message.setBcc(bcc);
        }
        if (cc != null && cc.length > 0) {
            message.setCc(cc);
        }
    }

    /**
     * 发送报警邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendAlarmMail(String[] to, String[] bcc, String[] cc, String subject, String[] content,String filePath,String fileName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            System.setProperty("mail.mime.splitlongparameters", "false");
            String alarmContent ="<!DOCTYPE html>\n"+
                    "<html xmlns='http://www.w3.org/1999/xhtml'>\n"+
                    "<head><meta http-equiv='X-UA-Compatible' content='ie=8'>\n"+
                    "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>\n"+
                    "<meta http-equiv='cache-control' content='no-cache'>\n"+
                    "<meta http-equiv='expires' content='mon, 20 jul 2001 01:02:01 GMT'>\n"+
                    "<meta http-equiv='pragma' content='no-cache'>\n"+
                    "<meta name='renderer' content='webkit'>\n"+
                    "<title>"+subject+"</title>\n"+
                    "<style>.setAddrInUe {font-style: normal}.setAddrInUe-done{color: #539bde;}.setAddrInUe em {font-style: normal;}</style>\n"+
                    "<style>#bodyDef {  margin: 0 !important;  padding: 0 !important; background-image: none !important; background-color: transparent !important;  font-family: verdana, Tahoma, Arial, '宋体', sans-serif;}\n"+
                    "#bodyDef #mailCont { width: 100% !important; font-size: 14px; word-wrap: break-word !important; table-layout: fixed !important; _overflow-x: hidden;  color: #333;  margin: 5px 0 0 0;  padding: 0; }\n"+
                    ".view {  padding: 0; word-wrap: break-word; cursor: text;  height: 100%;} body {  margin: 8px;} table.customTableClassName {margin-bottom: 10px;border-collapse: collapse; display: table;}\n"+
                    ".customTableClassName td, .customTableClassName th {  border: 1px solid #DDD;  padding: 5px 10px;}.customTableClassName td p {margin: 0; padding: 0;} ol, ul {margin: 0;  pading: 0;width: 95% }\n"+
                    "li {clear: both;}table.noBorder td, table.noBorder th {border: none 0 !important;padding: 0;}\n"+
                    "#bodyDef .backIpt1 * {font-family: verdana, Tahoma, Arial, '宋体', sans-serif !important;font-size: 12px !important;font-weight: normal !important; text-decoration: none !important;  line-height: 21px !important;}\n"+
                    "#bodyDef .backIpt1 {width: 800px !important;height: 50px !important;border: 1px solid #999 !important;margin: 10px 0 !important;}#bodyDef .backIpt1:hover { border: 1px solid #4D90FE !important;}\n"+
                    "#bodyDef .backIpt2 {padding-top: 10px !important;}#bodyDef .backIpt2 li {list-style-type: none !important;}\n"+
                    "#bodyDef .backIpt2 textarea.replayText {width: 98%;height: 30px;border: 1px solid #d3d3d3 !important;color: #ccc !important;font-size: 12px !important;font-family: 'Microsoft Yahei', Arial;border-radius: 3px 3px 3px 3px;\n"+
                    "box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1) inset;  margin: 12px 0;  padding: 6px;  line-height: 19px;  font-size: 12px;  resize: none;  outline: none; }\n"+
                    "#bodyDef .backIpt2 textarea.replayText:focus {color: #444 !important;}#bodyDef .backIpt2 textarea.replayText.current {  height: 60px;  border: 1px solid #999 !important;  color: #444 !important;  }\n"+
                    "#bodyDef .backIpt2 .aBtn {border: none;height: 30px;*line-height: 28px;padding: 0 25px;*padding: 0 15px;_padding: 0 10px;border: 1px solid #999;color: #fff;font-size:14px;text-align:center;margin-right: 6px;vertical-align: top;border-radius: 3px 3px 3px 3px;cursor: pointer;background: #89c7a0;border-color: #79b991;float: left;}\n"+
                    "#bodyDef .backIpt2 .aBtn:hover {background: #96d0ac;border-color: #79b991;}#bodyDef .backIpt2 .aBtn:active {background: #67aa80;border-color: #79b991;}\n"+
                    "#bodyDef .backIpt2 .aBtn.cancel {height: 30px;line-height: 30px;padding: 0 25px;*line-height: 27px;*padding: 0 15px;border: 1px solid #999;color: #6a6a6a;font-size: 14px;text-align: center;vertical-align: top;border-radius: 3px 3px 3px 3px;cursor: pointer;background: #f4f4f4;border-color: #ddd;}\n"+
                    "#bodyDef .backIpt2 .aBtn.cancel:hover {background: #f9f9f9;border-color: #d4d4d4;}h1,h2,h3,h4,h5,h6 {font-size: 16px;font-weight: bold;margin: 0;}\n"+
                    "@-moz-document url-prefix() {#bodyDef .backIpt2 .aBtn {padding-bottom: 4px !important;}</style></head>\n"+
                    "<body id='bodyDef'><div id='mailCont'><div id='mailContDiv'><div id='mailText' class='mailTextDivMG' style='font-size:14px'>\n"+
                    "<div style='font:14px/1.5 'Lucida Grande', '微软雅黑';color:#333;'>\n"+
                    "<div class='mail_quote_CFEF9AE2204044B297BCDE09B675A4C9' style='font: 14px/1.5 'Lucida Grande';color:#333;'>\n"+
                    "<table border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse;' width='650'><tbody><tr><td><div style='margin: 0; padding: 0; width: 650px'>\n"+
                    "<img src=\'http://static.xinrenxinshi.com/admin/edm-border.jpg\' width=\'650px\' height=\'4px\' style=\'margin: 0; padding: 0\'>\n"+
                    "<div style='font-size: 16px; font-weight: bold; text-align: center; color: #555'>"+subject+"</div>\n"+
                    "</strong>邮件正文内容如下</div><div style=\'font-weight: bold; color: #555\'>报告汇总信息</div><div style=\'margin: 0 0 20px 20px;\'>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>报告编号：</span><span style=\'color: #555; word-break: break-all\'>"+content[0]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>监控任务名：</span><span style=\'color: #555; word-break: break-all\'>"+content[1]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>监控策略名：</span><span style=\'color: #555; word-break: break-all\'>"+content[2]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>监控时间：</span><span style=\'color: #555; word-break: break-all\'>"+content[3]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>处理时长：</span><span style=\'color: #555; word-break: break-all\'>"+content[4]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>监控总数：</span><span style=\'color: #555; word-break: break-all\'>"+content[5]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>有效监控数：</span><span style=\'color: #555; word-break: break-all\'>"+content[6]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; color: red; margin-right: 5px\'>报警条数：</span><span style=\'color: red; word-break: break-all\'>"+content[7]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>正常条数：</span><span style=\'color: #555; word-break: break-all\'>"+content[8]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; color: red; margin-right: 5px\'>错误条数：</span><span style=\'color: red; word-break: break-all\'>"+content[9]+"</span></div>\n"+
                    "<div><span style=\'display: inline-block; margin-right: 5px\'>停止条数：</span><span style=\'color: #555; word-break: break-all\'>"+content[10]+"</span></div></div>\n"+
                    "<div style=\'border-bottom: 2px solid #d5d5d5\'></div>\n"+
                    "<div class=\'tips\' style=\'width: 580px; font-size: 12px; color: #999999; margin: auto; -ms-word-wrap: break-word; word-wrap: break-word; text-align: left;\'>\n"+
                    "<p>温馨提示：本邮件为系统自动发送，请勿回复。</p > </div>\n"+
                    "</div><img src=\'http://static.xinrenxinshi.com/admin/edm-border.jpg\' width=\'650px\' height=\'4px\' style=\'margin: 0; padding: 0\'></div>\n"+
                    "</td></tr></tbody></table></div></div></div></div><div class=\"clear\"></div></div></body></html>";
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            sendByBccOrCc(bcc, cc, helper);
            helper.setSubject(subject);
            helper.setText(alarmContent, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            logger.info("转码前-报警邮件附件名称：" + fileName);
            fileName = MimeUtility.encodeText(fileName,"utf-8",null);
            logger.info("转码后-报警邮件附件名称：" + fileName);
            //调用多次即添加多个附件
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            logger.info("报警邮件发送成功");
        } catch (MessagingException e) {
            logger.error("报警邮件时发生异常！", e);
            throw e;

        } catch (UnsupportedEncodingException e) {
            logger.error("报警邮件 文件名转码异常！", e);
            throw e;
        }
    }



    /**
     * 发送html邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendHtmlMail(String[] to, String[] bcc, String[] cc, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        try {

            content = "<html>\n" +
                    "<body>\n" +
                    "    <h3>" + content + "</h3>\n" +
                    "</body>\n" +
                    "</html>";
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            sendByBccOrCc(bcc, cc, helper);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            logger.info("html邮件发送成功");
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
            throw e;

        }
    }

    private void sendByBccOrCc(String[] bcc, String[] cc, MimeMessageHelper helper) throws MessagingException {
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
    }


    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    @Override
    public void sendAttachmentsMail(String[] to, String[] bcc, String[] cc, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            sendByBccOrCc(bcc, cc, helper);
            helper.setSubject(subject);
            helper.setText(content);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            //调用多次即添加多个附件
            helper.addAttachment(fileName, file);
            mailSender.send(message);

            logger.info("带附件的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送带附件的邮件时发生异常！", e);
            throw e;
        }
    }


    /**
     * 发送正文中有静态资源（图片）的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     */
    @Override
    public void sendInlineResourceMail(String[] to, String[] bcc, String[] cc, String subject, String content, String rscPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            String rscId = "neo006";
            content = "<html><body>" + content + "<img src=\'cid:" + rscId + "\' ></body></html>";
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            sendByBccOrCc(bcc, cc, helper);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
            logger.info("嵌入静态资源的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送嵌入静态资源的邮件时发生异常！", e);
            throw e;
        }
    }


    @Override
    public void sendTemplateMail(String[] to, String[] bcc, String[] cc, String subject, String templateFileName, String name, String value) throws MessagingException {


        //创建邮件正文
        context.setVariable(name, value);
        String emailContent = templateEngine.process(templateFileName, context);
        sendHtmlMail(to, bcc, cc, subject, emailContent);
    }

    @Override
    public Boolean validateBlank(String... args) {

        for (String arg : args) {
            if (StringUtils.isBlank(arg)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean validateBlankForArray(String[]... args) {
        for (String[] arg : args){
            if (args == null && args.length == 0){
                return true;
            }
        }
        return false;
    }


}
