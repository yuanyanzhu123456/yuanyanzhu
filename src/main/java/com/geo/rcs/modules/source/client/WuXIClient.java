package com.geo.rcs.modules.source.client;

import com.alibaba.druid.util.StringUtils;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.source.dao.WuXiMapper;
import com.squareup.okhttp.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Created by 曾志 on 2019/1/17.
 */
@Service
public class WuXIClient {
    @Value("${dataSourceServer.wuXiDate.url}")
    private String url;
    @Value("${dataSourceServer.wuXiDate.port}")
    private Integer port;
    @Value("${dataSourceServer.wuXiDate.xml76}")
    private String xml76;
    @Value("${dataSourceServer.wuXiDate.xml77}")
    private String xml77;
    @Value("${dataSourceServer.wuXiDate.xml79}")
    private String xml79;
    @Autowired
    private WuXiMapper wuXiMapper;

    public String getWuXiDate(String inner, String parameters) {
//        System.out.println("gjXml:"+xml76);
        Map<String, Object> map = JSONUtil.jsonToMap(parameters);
        Document reqDocument = DocumentHelper.createDocument();
        reqDocument.setXMLEncoding("GBK");
        Element root = reqDocument.addElement("hosts");
        //==========
        //添加请求报文头
        if (inner.equals("CIIS76")||inner.equals("CIIS77")||inner.equals("CIIS79")){
            Element ictrl = root.addElement("ictrl");
            ictrl.addElement("other_tell").setText("");
            ictrl.addElement("snd_tx_log_no").setText("123");
            ictrl.addElement("snd_node_id").setText("OLS");
            ictrl.addElement("snd_branch_id").setText("019101");
            ictrl.addElement("auth_pwd").setText("");
            ictrl.addElement("auth_tell").setText("");
            ictrl.addElement("sys_biz_code").setText("CIIS76");
            ictrl.addElement("sys_trx_id").setText("CIIS76");
            ictrl.addElement("snd_tx_time").setText("112118");
            ictrl.addElement("snd_tx_date").setText("20181218");
            ictrl.addElement("trx_tell").setText("M0032");
            ictrl.addElement("sys_chl_code").setText("405");
            ictrl.addElement("drivtp").setText("");
            ictrl.addElement("back_node").setText("");
            ictrl.addElement("drivno").setText("");
            ictrl.addElement("passwd").setText("");
            //==========
        }else {
            //todo
        }

        Element req = root.addElement("req");
        if ("CIIS76".equals(inner)) {
            for (String key : map.keySet()) {
                System.out.println("Key = " + key);
                req.addElement(key).setText(map.get(key).toString());
            }
            req.addElement("USER_CODE").setText("00001");
            req.addElement("QUERY_ORG").setText("00000");

        }else if("CIIS77".equals(inner)){
            String  spcode="";
            //添加身份证 姓名
            try {
                Element name = req.addElement("NAME");
                name.setText(map.get("NAME").toString());
                Element id_num = req.addElement("ID_NUM");
                id_num.setText(map.get("ID_NUM").toString());
                req.addElement("USER_CODE").setText("00001");
                req.addElement("QUERY_ORG").setText("00000");
                //通过Socket客户端获取返回xml
                try {
                    String resultXml = socketXml("CIIS76", url, port, root.asXML());
                    //解析返回xml中住房公积金账号字段给CIIS77和CIIS79作为参数
                    Document doc = null;
                    doc = DocumentHelper.parseText(resultXml);
                    Element roots =doc.getRootElement();
                    Element head = roots.element("resp");
                    spcode=head.element("GRZHXXLIST").element("detail").element("SPCODE").getTextTrim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                req.remove(name);
                req.remove(id_num);
                for (String key : map.keySet()) {
                    if(!key.equals("NAME")&&!key.equals("ID_NUM")){
                        System.out.println("Key = " + key);
                        req.addElement(key).setText(map.get(key).toString());
                    }

                }
                req.addElement("TYPE").setText("1");
            } catch (Exception e) {
                e.printStackTrace();
                spcode="123";
            }
//            req.addElement("USER_CODE").setText("00001");
//            req.addElement("QUERY_ORG").setText("00000");
            req.addElement("SPCODE").setText(spcode);

        }else if("CIIS79".equals(inner)){
            String spcode="";

            try {
                Element name = req.addElement("NAME");
                name.setText(map.get("NAME").toString());
                Element id_num = req.addElement("ID_NUM");
                id_num.setText(map.get("ID_NUM").toString());
                req.addElement("USER_CODE").setText("00001");
                req.addElement("QUERY_ORG").setText("00000");
                //通过Socket客户端获取返回xml
                try {
                    String resultXml= socketXml("CIIS76",url, port, root.asXML());
                    //解析返回xml中住房公积金账号字段给CIIS77和CIIS79作为参数
                    Document doc = null;
                    doc = DocumentHelper.parseText(resultXml);
                    Element roots =doc.getRootElement();
                    Element head = roots.element("resp");
                    spcode=head.element("GRZHXXLIST").element("detail").element("SNCODE").getTextTrim();
                } catch (Exception e) {
                    e.printStackTrace();
                    spcode="123";
                }
                req.remove(name);
                req.remove(id_num);
                for (String key : map.keySet()) {
                    if(!key.equals("NAME")&&!key.equals("ID_NUM")){
                        System.out.println("Key = " + key);
                        req.addElement(key).setText(map.get(key).toString());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            req.addElement("USER_CODE").setText("00001");
//            req.addElement("QUERY_ORG").setText("00000");
            req.addElement("SNCODE").setText(spcode);
        } else {
            for (String key : map.keySet()) {
                System.out.println("Key = " + key);
                req.addElement(key).setText(map.get(key).toString());
            }
        }

        //获取配置信息,请求无锡银行接口
        Map<String, Object> mapXml = new HashMap<>();
        mapXml.put("param", req.asXML());
        String xmlString = null;
        System.out.println("请求参数:" + root.asXML());
        if ("CIIS01".equals(inner)) {
            //如果是个人征信接口,取数据库中数据,否则调用无锡银行接口

            //从数据库中查询字段结果,模拟返回

            //根据idNumber查询到report_sn  TBL_PER_RESULT_INFO.report_sn  '报告编号'   再用report_sn关联其他表,在其他表中字段名为report_id
            String reportId=wuXiMapper.getReportNo(map.get("ID_NUM").toString());
            //他行个人消费贷款余额大于20万校验----> TBL_PER_LOAN_INFO表  cue '信贷交易信息'字段  包含 个人消费贷款   && balance '本金余额' >20万
            List<Double> balanceList=wuXiMapper.getBalance(reportId);
            String tbl_per_loan_info_balance=getDoubleSum(balanceList);
            //个人征信贷款及贷记卡当前逾期情况校验 -----> tbl_per_loan_info 表     curroverdueamount 当前逾期金额 字段值>0  tbl_per_loan_card_info表  curroverdueamount 当前逾期金额  字段值>0
            String tbl_per_loan_info_curroverdueamount=getIntegerSum(wuXiMapper.getLoanInfoCurroverdueamount(reportId));
            String tbl_per_loan_card_info_curroverdueamount=getIntegerSum(wuXiMapper.getLoanCardInfoCurroverdueamount(reportId));
            //最近12个月内贷款或信用卡存在累计2次以上逾期校验 ------> 最近12个月内贷款字段 tbl_per_loan_info 表12个月内有记录  latest24state  24月还款状态  字段(如果不为null再判断最后12个字符里有几个数字就是有几次逾期) / 信用卡累计逾期次数字段 tbl_per_loan_card_info表  last_24state  24月还款状态  字段(如果不为null再判断最后12个字符里有几个数字就是有几次逾期) ;
//            Map<String,String> oAndE=wuXiMapper.getOpendateAndEnddate(appNo);
//            String tbl_per_loan_info_opendate= null==oAndE?"":oAndE.get("o");
//            String tbl_per_loan_info_enddate=null==oAndE?"":oAndE.get("e");
//            String tbl_per_loan_card_info_curroverduecyc=wuXiMapper.getLoanCardInfoCurroverduecyc(appNo);
            List<String> tbl_per_loan_info_latest24stateList=wuXiMapper.getLoanInfoLatest24state(reportId);
            List<String> tbl_per_loan_card_info_last_24stateList=wuXiMapper.getLoanCardInfoLast24state(reportId);
            List<Integer> loanInfo24=new ArrayList<>();
            List<Integer> loanCardInfo24=new ArrayList<>();
            for (String tbl_per_loan_info_latest24state:tbl_per_loan_info_latest24stateList){
                if (null!=tbl_per_loan_info_latest24state&&tbl_per_loan_info_latest24state.length()>11){
                    tbl_per_loan_info_latest24state=tbl_per_loan_info_latest24state.substring(tbl_per_loan_info_latest24state.length()-12);
                }
                Integer loanOverdueNumber12state=0;
                if (!StringUtils.isEmpty(tbl_per_loan_info_latest24state)){
                    for(int i=0 ; i<tbl_per_loan_info_latest24state.length() ; i++){
                        if (Character.isDigit(tbl_per_loan_info_latest24state.charAt(i))){
                            loanOverdueNumber12state++;
                        }
                    }
                }
                loanInfo24.add(loanOverdueNumber12state);
            }
            for (String tbl_per_loan_card_info_last_24state:tbl_per_loan_card_info_last_24stateList){
                if(null!=tbl_per_loan_card_info_last_24state&&tbl_per_loan_card_info_last_24state.length()>11){
                    tbl_per_loan_card_info_last_24state=tbl_per_loan_card_info_last_24state.substring(tbl_per_loan_card_info_last_24state.length()-12);
                }
                Integer cardOverdueNumber12state=0;

                if (!StringUtils.isEmpty(tbl_per_loan_card_info_last_24state)){
                    for(int i=0 ; i<tbl_per_loan_card_info_last_24state.length() ; i++){
                        if (Character.isDigit(tbl_per_loan_card_info_last_24state.charAt(i))){
                            cardOverdueNumber12state++;
                        }
                    }
                }
                loanCardInfo24.add(cardOverdueNumber12state);
            }
            //征信显示未销户贷记卡信息汇总中“近6个月平均使用额度”占“授信总额”70%（含）以上  ------------->近6个月平均使用额度   TBL_PER_INFO_SUMARRY 表  undestyryloancard_used_avg_6 '最近6个月平均使用额度'  授信总额   TBL_PER_INFO_SUMARRY  undestyryloancard_credit_limit  授信总额
            String tbl_per_info_sumarry_undestyryloancard_used_avg_6=wuXiMapper.getUndestyryloancardUsedAvg6(reportId);
            String tbl_per_info_sumarry_undestyrysloancard_creditlimit=wuXiMapper.getUndestyrysloancardCreditlimit(reportId);
            Double avg6=null==tbl_per_info_sumarry_undestyryloancard_used_avg_6?0:Double.valueOf(tbl_per_info_sumarry_undestyryloancard_used_avg_6);
            Double avg6ToLimitrate= Double.valueOf(0);
            if (null!=tbl_per_info_sumarry_undestyrysloancard_creditlimit){
                avg6ToLimitrate=avg6/Double.valueOf(tbl_per_info_sumarry_undestyrysloancard_creditlimit);
            }
            //个人消费贷款、其他贷款、信用免担保个人经营性贷款授信额度60万以上------------>TBL_PER_LOAN_INFO表  cue '信贷交易信息'字段  包含 个人消费贷款、其他贷款、信用免担保个人经营性贷款,和account'总额'>60万
            String tbl_per_loan_info_account=getDoubleSum(wuXiMapper.getAccount(reportId));
            //学历 ----------> TBL_PER_PERSIONAL_INFO edu_level字段
            String tbl_per_persional_info_edu_level=wuXiMapper.getEduLevel(reportId);
            //信用卡账户数量------------------------->信用卡账户数量    TBL_PER_INFO_SUMARRY  loan_card_count '贷记卡账户数';
            String tbl_per_info_sumarry_loan_card_count=wuXiMapper.getLoanCardCount(reportId);

            //拼接返回XML格式
            Document respDocument= DocumentHelper.createDocument();
            Element rootReturn = respDocument.addElement( "hosts" );
            Element resp = rootReturn.addElement("resp");
            resp.addElement( "tbl_per_loan_info_balance" ).setText(null==tbl_per_loan_info_balance?"":tbl_per_loan_info_balance);
            resp.addElement( "tbl_per_loan_info_curroverdueamount" ).setText(null==tbl_per_loan_info_curroverdueamount?"":tbl_per_loan_info_curroverdueamount);
            resp.addElement( "tbl_per_loan_card_info_curroverdueamount" ).setText(null==tbl_per_loan_card_info_curroverdueamount?"":tbl_per_loan_card_info_curroverdueamount);
            resp.addElement( "loanOverdueNumber12state" ).setText(getIntegerMax(loanInfo24));
            resp.addElement( "cardOverdueNumber12state" ).setText(getIntegerMax(loanCardInfo24));
            resp.addElement( "tbl_per_info_sumarry_loan_card_count" ).setText(null==tbl_per_info_sumarry_loan_card_count?"":tbl_per_info_sumarry_loan_card_count);
            resp.addElement( "avg6ToLimitrate" ).setText(0==avg6ToLimitrate?"":String.format("%.2f", avg6ToLimitrate));
            resp.addElement( "tbl_per_loan_info_account" ).setText(null==tbl_per_loan_info_account?"":tbl_per_loan_info_account);
            resp.addElement( "tbl_per_persional_info_edu_level" ).setText(null==tbl_per_persional_info_edu_level?"":tbl_per_persional_info_edu_level);
            xmlString=rootReturn.asXML().replaceAll("\"","");
            xmlString="<?xml version=\"1.0\" encoding=\"GBK\"?>"+xmlString;
            System.out.println("征信查询数据库结果:"+xmlString);
            //测试表:
        }
        //todo 注释掉
//        else {
//
//            //todo 此处socket请求
////            try {
////                xmlString=  socketXml(url,port,reqDocument.asXML());
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
//            //模拟假数据
//            StringBuffer sb = new StringBuffer();
//            sb.append("<?xml version='1.0' encoding='UTF-8'?>").append("<hosts><ictrl><GJJYJE>20181228</GJJYJE><DWJCBL_12>1|1|1|3</DWJCBL_12><GJJYJE>8000</GJJYJE></ictrl></hosts>");
//            xmlString=sb.toString();
//            System.out.println("socket返回结果:"+xmlString.toString());
//
//        }
        //todo 注释掉

        try {
            if (null==xmlString){
                //qq
                xmlString = socketXml(inner,url,port,root.asXML());
            }
        } catch (IOException e) {
            System.out.println("--------------post请求xml出现异常-------------------");
            e.printStackTrace();
        }
//        if (!StringUtils.isEmpty(xmlString)){
//            xmlString=xmlString.substring(xmlString.indexOf("<?xml"),xmlString.length());
//        }
        return xmlString;
    }

    private static FormEncodingBuilder makeBuilderFromMap(final Map<String, Object> map) {
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        for (final Map.Entry<String, Object> entrySet : map.entrySet()) {
            formBody.add(entrySet.getKey(), entrySet.getValue().toString());
        }
        return formBody;
    }

    public static String postXmlParams(String url, String xml) throws IOException {
        String responseBody = "";
        //视情况,修改请求方式
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        response = okHttpClient.newCall(request).execute();
        int status = response.code();
        if (response.isSuccessful()) {
            return response.body().string();
        }
        if (response != null) {
            response.body().close();
        }
        return responseBody;
    }

    public String socketXml(String inter, String url, Integer port, String xml) throws IOException {
        System.out.println("进入socketXml方法");
        String xmlpre = "00000690" + inter + "                             Q01OLS     721                  ";

      if (inter.equals("CIIS76")){
          System.out.println("报文头:"+xml76);

          if (xml76 == null || xml76 == "") {
              xml = xmlpre + xml;
          } else {
              xml = xml76.split("aaa")[1] + xml;
          }
      }else if (inter.equals("CIIS77")){
          System.out.println("报文头:"+xml77);
        xml=  xml.replace("<sys_biz_code>CIIS76</sys_biz_code>","<sys_biz_code>CIIS77</sys_biz_code>");
        xml=xml.replace("<sys_trx_id>CIIS76</sys_trx_id>","<sys_trx_id>CIIS77</sys_trx_id>");
          if (xml77 == null || xml77 == "") {
              xml = xmlpre + xml;
          } else {
              xml = xml77.split("aaa")[1] + xml;
          }
      }else if (inter.equals("CIIS79")){
          System.out.println("报文头:"+xml79);
         xml= xml.replace("<sys_biz_code>CIIS76</sys_biz_code>","<sys_biz_code>CIIS79</sys_biz_code>");
        xml=  xml.replace("<sys_trx_id>CIIS76</sys_trx_id>","<sys_trx_id>CIIS79</sys_trx_id>");
          if (xml79 == null || xml79 == "") {
              xml = xmlpre + xml;
          } else {
              xml = xml76.split("aaa")[1] + xml;
          }
      }
        StringBuilder sb =  new StringBuilder();
        String xmlString="";
        try {
            xml = xml.replaceAll("\n", "");
            System.out.println("client方法:");
            System.out.println("xml:"+xml);
            System.out.println("请求url: " + url);
            System.out.println("请求端口: " + port);
            System.out.println("请求接口: " + inter);
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
                sb.append(new String(bytes, 0, len, "GBK"));
            }
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("soucket返回值:" + sb.toString());
             xmlString = sb.toString();
            if (!StringUtils.isEmpty(xmlString)){
                xmlString=xmlString.substring(xmlString.indexOf("<?xml"),xmlString.length());
            }
        } catch (IOException e) {
            System.out.println("socket接口:"+inter+"失败!");
            e.printStackTrace();
        }
        return xmlString;
    }

    //求和
    private String getDoubleSum(List<Double> doubleList){
        Double sum= Double.valueOf(0);
        if (doubleList.size()!=0){
            for (Double d:doubleList){
                d=null==d?0:d;
                sum=sum+d;
            }
        }else {
            return "";
        }
        return sum.toString();
    }

    private String getIntegerSum(List<Integer> integerList){
        Integer sum=0;
        if (integerList.size()!=0){
            for (Integer i:integerList){
                i=null==i?0:i;
                sum=sum+i;
            }
        }else {
            return "";
        }
        return  sum.toString();
    }

    //取最大值
    private String getIntegerMax(List<Integer> integerList){
        if (integerList.size()!=0){
            Integer max= Collections.max(integerList);
            return max.toString();
        }else {
            return "";
        }
    }

}
