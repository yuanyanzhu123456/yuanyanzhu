package com.geo.rcs.modules.source.controller;

import com.alibaba.druid.util.StringUtils;
import com.geo.rcs.common.util.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by 曾志 on 2019/1/18.
 */
@RestController
@RequestMapping("/WXSource")
public class WuXiZhenXingController {

    @Value("${dataSourceServer.wuXiDateZx.url}")
    private  String url;
    @Value("${dataSourceServer.wuXiDateZx.port}")
    private  Integer port;
    @Value("${dataSourceServer.wuXiDateZx.xml1}")
    private String xml1;
    @Value("${dataSourceServer.wuXiDateZx.xml3}")
    private String xml3;
    @RequestMapping("/startTest")
    public void startTest(HttpServletRequest request, HttpServletResponse response){
        String BRANCH_ID=request.getParameter("BRANCH_ID");
        String USER_ID=request.getParameter("USER_ID");
        String GLOBAL_TYPE=request.getParameter("GLOBAL_TYPE");
        String GLOBAL_ID=request.getParameter("GLOBAL_ID");
        String CUSTOMER_NAME=request.getParameter("CUSTOMER_NAME");
        String QUERY_REASON=request.getParameter("QUERY_REASON");
        String VER_TYPE=request.getParameter("VER_TYPE");
        String IDAUTH_FLAG=request.getParameter("IDAUTH_FLAG");
        String IMAGE_URL=request.getParameter("IMAGE_URL");
        String NOTE=request.getParameter("NOTE");

        String TRAN_DATE= DateUtils.format(new Date(),"yyyyMMddHHmmssSSS");
        String TRAN_TYPE=request.getParameter("TRAN_TYPE");
        String QUERY_TIME_BEGEIN=request.getParameter("QUERY_TIME_BEGEIN");
        String QUERY_TIME_END=request.getParameter("QUERY_TIME_END");
        String REMARK=request.getParameter("REMARK");

        String xmlCIIS03="";
        try {
            System.out.println("----------------------------------开始请求CIIS03----------------------------------------");
            xmlCIIS03= this.getCIIS03(BRANCH_ID,USER_ID,GLOBAL_TYPE,GLOBAL_ID,CUSTOMER_NAME,QUERY_REASON,VER_TYPE,IDAUTH_FLAG,IMAGE_URL,NOTE);
            System.out.println("CIIS03返回结果:"+xmlCIIS03);
        } catch (Exception e) {
            System.out.println("----------------------------------获取CIIS03出现异常------------------------------------"+e.getMessage());
        }
        System.out.println("-------------------------------------返回结果:"+xmlCIIS03+"-----------------------------------");
        System.out.println("睡眠60秒调CIIS01");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String xmlCIIS01="";

        try {
            xmlCIIS01=this.getCIIS01(true,"-1",TRAN_DATE,BRANCH_ID, USER_ID, TRAN_TYPE, GLOBAL_TYPE,
                    GLOBAL_ID,CUSTOMER_NAME,  QUERY_TIME_BEGEIN,QUERY_TIME_END,
                    QUERY_REASON, REMARK);
        } catch (IOException e) {
            System.out.println("----------------------------------获取CIIS01出现异常------------------------------------"+e.getMessage());
        }
        System.out.println("---------------------------------------返回CIIS01数据:"+xmlCIIS01+"----------------------------");

    }

    public String getCIIS03(String BRANCH_ID,String USER_ID,String GLOBAL_TYPE,String GLOBAL_ID,String CUSTOMER_NAME,
                            String QUERY_REASON,String VER_TYPE,String IDAUTH_FLAG,String IMAGE_URL,String NOTE) throws IOException {
        Document reqDocument= DocumentHelper.createDocument();


        Element root = reqDocument.addElement( "hosts" );
        //======
        //添加请求报文头
        Element ictrl = root.addElement("ictrl");
        ictrl.addElement("other_tell").setText("");
        ictrl.addElement("snd_tx_log_no").setText("123");
        ictrl.addElement("snd_node_id").setText("OLS");
        ictrl.addElement("snd_branch_id").setText("019101");
        ictrl.addElement("auth_pwd").setText("");
        ictrl.addElement("auth_tell").setText("");
        ictrl.addElement("sys_biz_code").setText("CIIS03");
        ictrl.addElement("sys_trx_id").setText("CIIS03");
        ictrl.addElement("snd_tx_time").setText("154000");
        ictrl.addElement("snd_tx_date").setText("20190124");
        ictrl.addElement("trx_tell").setText("M0032");
        ictrl.addElement("sys_chl_code").setText("736");
        ictrl.addElement("drivtp").setText("");
        ictrl.addElement("back_node").setText("");
        ictrl.addElement("drivno").setText("");
        ictrl.addElement("passwd").setText("");

        //======


        Element req = root.addElement("req");
        req.addElement("NOTE").setText("");
        req.addElement("VER_TYPE").setText(VER_TYPE);
        req.addElement("GLOBAL_TYPE").setText(GLOBAL_TYPE);
        req.addElement("GLOBAL_ID").setText(GLOBAL_ID);
        req.addElement("IDAUTH_FLAG").setText(IDAUTH_FLAG);
        req.addElement( "BRANCH_ID" ).setText(BRANCH_ID);
        req.addElement("IMAGE_URL").setText(IMAGE_URL);
        req.addElement("QUERY_REASON").setText(QUERY_REASON);
        req.addElement("CUSTOMER_NAME").setText(CUSTOMER_NAME);
        req.addElement("USER_ID").setText(USER_ID);
        System.out.println("CIIS03传入报文:"+root.asXML());
//        获取配置信息,请求无锡银行接口
        String xml= socketXml("CIIS03",url,port,root.asXML());
        if (!StringUtils.isEmpty(xml)){
            xml=xml.substring(xml.indexOf("<?xml"),xml.length());
        }

//        String xml="";
        if ("".equals(xml)) {
//            Element  = reqDocument.addElement( "hosts" );
            Element resp = root.addElement("resp");
            resp.addElement( "respCode" ).setText("0000");//返回码  0000-处理成功      E001-处理失败   E002-请求报文格式有误   E003-不支持的交易类型    E012-交易码不存在
            resp.addElement( "respDesc" ).setText("返回描述");
            resp.addElement( "localReport" ).setText("0");//是否存在本地报告 0：不存在  1：存在
            resp.addElement("UNDESTYRYSLOANCARD_CREDITLIMIT").setText("0");
            resp.addElement("UNDESTYRYLOANCARD_USED_AVG_6").setText("0");
            resp.addElement("LASTMONTHS").setText("0");
            resp.addElement("TblPbocOverDueRecord").setText("0");
            resp.addElement("GUAR_BALANCE").setText("0");
            resp.addElement("EDU_LEVEL").setText("0");
            xml=reqDocument.asXML();
        }
        return xml;
    }

    public String getCIIS01(boolean flag,String msgCode,String TRAN_DATE, String BRANCH_ID, String USER_ID, String TRAN_TYPE, String GLOBAL_TYPE,
                            String GLOBAL_ID, String CUSTOMER_NAME, String QUERY_TIME_BEGEIN, String QUERY_TIME_END,
                            String QUERY_REASON, String REMARK) throws IOException {
        System.out.println("----------------------------------开始请求CIIS01----------------------------------------");


        String xml="";
        Document reqDocument = DocumentHelper.createDocument();
        Element root = reqDocument.addElement("hosts");
        //======
        //添加请求报文头
        Element ictrl = root.addElement("ictrl");
        ictrl.addElement("other_tell").setText("");
        ictrl.addElement("snd_tx_log_no").setText("123");
        ictrl.addElement("snd_node_id").setText("OLS");
        ictrl.addElement("snd_branch_id").setText("019101");
        ictrl.addElement("auth_pwd").setText("");
        ictrl.addElement("auth_tell").setText("");
        ictrl.addElement("sys_biz_code").setText("CIIS01");
        ictrl.addElement("sys_trx_id").setText("CIIS01");
        ictrl.addElement("snd_tx_time").setText("154000");
        ictrl.addElement("snd_tx_date").setText("20190124");
        ictrl.addElement("trx_tell").setText("M0032");
        ictrl.addElement("sys_chl_code").setText("736");
        ictrl.addElement("drivtp").setText("");
        ictrl.addElement("back_node").setText("");
        ictrl.addElement("drivno").setText("");
        ictrl.addElement("passwd").setText("");



        //======
        Element req = root.addElement("req");
        req.addElement("TRAN_DATE").setText(TRAN_DATE);
        req.addElement("BRANCH_ID").setText(BRANCH_ID);
        req.addElement("USER_ID").setText(USER_ID);
        req.addElement("TRAN_TYPE").setText(TRAN_TYPE);
        req.addElement("GLOBAL_TYPE").setText(GLOBAL_TYPE);
        req.addElement("GLOBAL_ID").setText(GLOBAL_ID);
        req.addElement("CUSTOMER_NAME").setText(CUSTOMER_NAME);
        req.addElement("QUERY_TIME_BEGEIN").setText(QUERY_TIME_BEGEIN);
        req.addElement("QUERY_TIME_END").setText(QUERY_TIME_END);
        req.addElement("QUERY_REASON").setText(QUERY_REASON);
        req.addElement("REMARK").setText(REMARK);

//        Element octrl=root.addElement("octrl");
//        octrl.addElement("msg_code").setText("0000");
        System.out.println("请求参数:"+root.asXML());
        //获取配置信息,请求无锡银行接口
        xml = socketXml("CIIS01",url,port,root.asXML());
        System.out.println("CIIS01返回结果:"+xml);
        if (!StringUtils.isEmpty(xml)){
            xml=xml.substring(xml.indexOf("<?xml"),xml.length());
        }
        Document documentCIIS01 = null;
        try {
            if (!StringUtils.isEmpty(xml)){
                documentCIIS01=DocumentHelper.parseText(xml);
            }
        } catch (DocumentException e) {
            System.out.println("解析CIIS01,xml出现异常");
        }
        msgCode=null==documentCIIS01?"-1":documentCIIS01.getRootElement().element("octrl").element("msg_code").getText();
        System.out.println("-------------------------返回msg_code:"+msgCode+"-----------------------");
        if (!"0000".equals(msgCode)&&flag){
            try {
                System.out.println("------------------------msg_code不为0000,休眠30秒再请求CIIS01-----------------");
                TimeUnit.SECONDS.sleep(30);//秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.getCIIS01(false,msgCode, TRAN_DATE, BRANCH_ID, USER_ID, TRAN_TYPE, GLOBAL_TYPE,
                    GLOBAL_ID, CUSTOMER_NAME, QUERY_TIME_BEGEIN, QUERY_TIME_END,
                    QUERY_REASON, REMARK);
        }
        return xml;
    }

    public  String socketXml(String inter,String url,Integer port,String xml) throws IOException {

        String xmlpre = "00000690" + inter + "                             Q01OLS     721                  ";
        if (inter.equals("CIIS01")){
            if (xml1 == null || xml1 == "") {
                xml = xmlpre + xml;
            } else {
                xml = xml1.split("aaa")[1] + xml;
            }
        }else if (inter.equals("CIIS03")){
            if (xml3 == null || xml3 == "") {
                xml = xmlpre + xml;
            } else {
                xml = xml3.split("aaa")[1] + xml;
            }
        }

        StringBuilder sb =  new StringBuilder();;
        try {
            xml = xml.replaceAll("\n", "");
            System.out.println("zxXml:"+xml1);
            System.out.println("xml:"+xml);
            System.out.println("ip:"+url);
            System.out.println("端口:"+port);
            System.out.println("接口:"+inter);
            System.out.println("参数:"+xml);
//            计算报文长度
            String substring = xml.substring(8, xml.length());
            int size = substring.getBytes("GBK").length;
            if (size<10){
                xml="0000000"+size+substring;
            }else if (size<100){
                xml="000000"+size+substring;
            }else if (size<1000){
                xml="00000"+size+substring;
            }else if (size<10000){
                xml="0000"+size+substring;
            }else if (size<100000){
                xml="000"+size+substring;
            }else if (size<1000000){
                xml="00"+size+substring;
            }
            System.out.println("请求参数xml: " + xml.toString());

//
            Socket socket = new Socket(url, port);
            // 建立连接后获得输出流
            OutputStream outputStream = socket.getOutputStream();
            socket.getOutputStream().write(xml.getBytes("GBK"));
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            socket.shutdownOutput();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"GBK"));
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("socket接口:"+inter+"失败!");
           e.printStackTrace();
        }
        return sb.toString();
    }
}
