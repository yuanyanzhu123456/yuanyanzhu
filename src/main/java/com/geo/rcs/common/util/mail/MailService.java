package com.geo.rcs.common.util.mail;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:01 2018/7/23
 */
public interface MailService {

    public static final String SENDER_NAME = "集智雷达系统";
    public static final String TEXT_MAIL = "text";
    public static final String HTML_MAIL = "html";
    public static final String ATTACHMENT_MAIL = "attachment";
    public static final String STATIC_MAIL = "static";
    public static final String TEMPLATE_MAIL = "template";
    public static final String ALARM_MAIL = "alarmmail";

    /**
     * 统一方法
     *
     * @param type    邮件类型
     * @param parmMap bcc:密送人[],cc:抄送人[],可为空,其他不可为空
     *                type=text 文本邮件         parmMap  固定key对应的参数: to:收件人[]:, bcc:密送人[],cc:抄送人[], bccsubject:主题:,content:邮件内容
     *                type=html html邮件         parmMap  固定key对应的参数: to:收件人[],bcc:密送人[],cc:抄送人[],subject:主题,content:邮件内容
     *                type=attachment 附件邮件   parmMap  固定key对应的参数: to:收件人[],bcc:密送人[],cc:抄送人[],subject:主题,content:邮件内容,filePath:附件路径
     *                type=static 静态资源邮件   parmMap  固定key对应的参数: to:收件人[],bcc:密送人[],cc:抄送人[],subject:主题,content:邮件内容,imgPath:邮件内容里静态资源路径
     *                type=template  模板邮件    parmMap  固定key对应的参数: to:收件人[],bcc:密送人[],cc:抄送人[],subject:主题,fileName:邮件模板文件名,parmName:模板文件参数名,value:模板文件参数值
     * @throws Exception
     */
    void sendMail(String type, Map<String, Object> parmMap) throws Exception;


    /**
     * 发送文本文件
     *
     * @param to
     * @param bcc
     * @param cc
     * @param subject
     * @param content
     * @throws Exception
     */
    public void sendSimpleMail(String[] to, String[] bcc, String[] cc, String subject, String content) throws Exception;

    /**
     * 发送报警邮件
     *
     * @param to
     * @param bcc
     * @param cc
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendAlarmMail(String[] to, String[] bcc, String[] cc, String subject, String[] content,String filePath,String fileName) throws MessagingException, UnsupportedEncodingException;

    /**
     * 发送html邮件
     *
     * @param to
     * @param bcc
     * @param cc
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String[] to, String[] bcc, String[] cc, String subject, String content) throws MessagingException;


    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param bcc
     * @param cc
     * @param subject
     * @param content
     * @param filePath
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String[] to, String[] bcc, String[] cc, String subject, String content, String filePath) throws MessagingException;

    /**
     * 发送正文中有静态资源（图片）的邮件
     *
     * @param to
     * @param bcc
     * @param cc
     * @param subject
     * @param content
     * @param rscPath
     * @throws MessagingException
     */
    public void sendInlineResourceMail(String[] to, String[] bcc, String[] cc, String subject, String content, String rscPath) throws MessagingException;

    /**
     * 模板邮件  <a href="#" th:href="@{ http://localhost/neo/{id}(id=${id}) }">激活账号</a>
     *
     * @param to
     * @param bcc
     * @param cc
     * @param subject
     * @param templateFileName
     * @param name
     * @param value
     * @throws MessagingException
     */
    public void sendTemplateMail(String[] to, String[] bcc, String[] cc, String subject, String templateFileName, String name, String value) throws MessagingException;


    /**
     * 校验给定参数非空
     *
     * @param args
     * @return
     */
    public Boolean validateBlank(String... args);

    /**
     * 校验给定参数非空
     */
    public Boolean validateBlankForArray(String[]... args);



}
