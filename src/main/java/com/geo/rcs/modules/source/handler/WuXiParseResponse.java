package com.geo.rcs.modules.source.handler;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.source.client.WuXIClient;
import com.geo.rcs.modules.source.dao.WuXiMapper;
import com.geo.rcs.modules.source.entity.WuXiWhiteList;
import com.squareup.okhttp.*;
import org.apache.commons.collections.map.HashedMap;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by geo on 2019/1/17.
 */
@Service
public class WuXiParseResponse {
    // 初始化常量
    private static final String CODE = "code";
    private static final String SUCCSEE_CODE = "200";
    private static final String DATA = "data";
    private static final String DESC = "desc";
    private static final String INTER = "inter";
    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private static final String VALUEDESC = "valueDesc";
    //优质名称单位关键字
    private static final String SNNAME="铁路,盐,燃气,烟,电信,村民委员会,学院,幼儿园,卫生物服中心,电台,广电,液化气,中学,机场,电力,银行,医院,电视台,学校，" +
            "公路,地铁,国联,证券,小学,出版社，居民委员会";
    //优质单位所属行业
    private static final List<String> DWSSHYLIST= Arrays.asList("0-其他","D-电力热力、燃气及水生产和供应业","F-批发和零售行业","G-交通运输、仓库和邮政业","J-金融业","N-水利、环境和公共设施管理业","O-居民服务、修理和其他行业","P-教育","Q-卫生和社会工作","R-文化、体育和娱乐业","S-公众管理、社会保障和社会组织");
    //优质单位类别
    private static final List<String> DWLBLIST= Arrays.asList("02-国有企业","05-城镇私营企业及其他城镇企业","03-城镇集团企业","08-社会团体","04-外商投资企业","06-事业单位","01-国家机关","09-其他");

    @Autowired
    private WuXIClient wuXIClient;
    @Autowired
    private WuXiMapper wuXiMapper;

    public Map<String,Map> parseData(List<String> listData,String  parameters) {
        Map<String,Object> params = JSONUtil.jsonToMap(parameters);
        Map<String,Object> paramsUpdate = new HashedMap();
        String xml="";
        Map<String,Map> inMap=new HashMap<>();
        for (String in:listData){
            xml= wuXIClient.getWuXiDate(in,parameters.toUpperCase());
            inMap.putAll(xmlParseMap(in,xml));
        }
        return inMap;
    }

    public Map<String,Map> parseDataWhiteList(List<String> listData,String  parameters){
        Map<String,Map> returnMap=new HashMap<>();
        Map<String,Object> map = JSONUtil.jsonToMap(parameters);
        String code="";
        for (String in:listData){
            List<WuXiWhiteList> wuXiWhiteLists=wuXiMapper.getWuXiWhiteLists(map.get("name").toString(),map.get("id_num").toString());
            code=wuXiWhiteLists.size()>0?"0":"1";
            Map<String, String> _interData = new HashMap<>();
            _interData.put(INTER,in);
            _interData.put(FIELD,"wuXiWhiteCode");
            _interData.put(VALUE, code);
            _interData.put(VALUEDESC, "");
            returnMap.put("wuXiWhiteCode", _interData);
        }
        return  returnMap;
    }

    /**
     *
     * @param xmlData xml字符串
     * @return
     */
    public Map<String,Map> xmlParseMap(String in,String xmlData){
        System.out.println("in:"+in);
        System.out.println("xmlData:"+xmlData);
        if (xmlData.startsWith("000")){
            String s = xmlData.split("<octrl>")[1];
            xmlData="<hosts><octrl>"+s;
        }
        System.out.println("转换后:"+xmlData);
        Document doc = null;
        int i=0;
        //  Map<String,String>  xmlDataMap= (Map) JSONObject.parse(xmlData);
        Map<String,String> map=new LinkedHashMap<>() ;
        Map<String,Map> result = new HashedMap();
        try {
            // String str1=xmlDataMap.get("data");
            try {
                doc = DocumentHelper.parseText(xmlData);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element root = doc.getRootElement(); // 获取根节点
            //公积金连续12月纳存
            if("CIIS77".equals(in)){
                List<Element> childElements = root.elements();
                for (Element child : childElements) {
                    List<Element> elementList = child.elements();
                    for (Element ele : elementList) {
                        System.out.println(ele.getName());
                        Iterator iterator1 = ele.elementIterator();
                        while(iterator1.hasNext()){
                            Element element = (Element)iterator1.next();
                            if("detail".equals(element.getName() )){
                                Iterator iterator = element.elementIterator();
                                while(iterator.hasNext()){
                                    Element next = (Element) iterator.next();
                                    if("TYPE".equals(next.getName())){
                                        if("1".equals(next.getText())){
                                            i++;
                                        }else{
                                            Map<String, String> _interData = new HashMap<>();
                                            _interData.put(INTER,in);
                                            _interData.put(FIELD,"TYPE");
                                            _interData.put(VALUE, "0");
                                            _interData.put(VALUEDESC, "");
                                            result.put("TYPE", _interData);
                                            return  result;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
                if(i>=12){
                    Map<String, String> _interData = new HashMap<>();
                    _interData.put(INTER,in);
                    _interData.put(FIELD,"TYPE");
                    _interData.put(VALUE, "2");
                    _interData.put(VALUEDESC, "");
                    result.put("TYPE", _interData);
                }else if(i<6){
                    Map<String, String> _interData = new HashMap<>();
                    _interData.put(INTER,in);
                    _interData.put(FIELD,"TYPE");
                    _interData.put(VALUE, "0");
                    _interData.put(VALUEDESC, "");
                    result.put("TYPE", _interData);
                } else{
                    Map<String, String> _interData = new HashMap<>();
                    _interData.put(INTER,in);
                    _interData.put(FIELD,"TYPE");
                    _interData.put(VALUE, "1");
                    _interData.put(VALUEDESC, "");
                    result.put("TYPE", _interData);
                }
                //优质行业资质筛选
            }else if("CIIS79".equals(in)){
                List<Element> childElements = root.elements();
                for (Element child : childElements) {
                    List<Element> elementList = child.elements();
                    for (Element ele : elementList) {
                        System.out.println(ele.getName());
                        Iterator iterator = ele.elementIterator();
                        while (iterator.hasNext()) {
                            Element next = (Element) iterator.next();
                            System.out.println(next.getName());
                            Iterator iterator1 = next.elementIterator();
                            while (iterator1.hasNext()) {
                                Element next1 = (Element) iterator1.next();
                                System.out.println(next1.getName());
                                //优质行业名称关键字解析
                                if ("SNNAME".equals(next1.getName())) {
                                    //优质行业名称关键字
                                    if (SNNAME.indexOf(next1.getText()) >= 0) {
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER, in);
                                        _interData.put(FIELD, next1.getName());
                                        _interData.put(VALUE, "1");
                                        _interData.put(VALUEDESC, "");
                                        result.put(next1.getName(), _interData);
                                    }
                                    //非优质行业名称关键字
                                    else {
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER, in);
                                        _interData.put(FIELD, next1.getName());
                                        _interData.put(VALUE, "0");
                                        _interData.put(VALUEDESC, "");
                                        result.put(next1.getName(), _interData);
                                    }
                                }
                                //优质单位所属行业解析
                                else if ("DWSSHY".equals(next1.getName())) {
                                    //优质单位所属行业
                                    if (DWSSHYLIST.contains(next1.getText())) {
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER, in);
                                        _interData.put(FIELD, next1.getName());
                                        _interData.put(VALUE, "1");
                                        _interData.put(VALUEDESC, "");
                                        result.put(next1.getName(), _interData);
                                    }
                                    //非优质单位所属行业
                                    else {
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER, in);
                                        _interData.put(FIELD, next1.getName());
                                        _interData.put(VALUE, "0");
                                        _interData.put(VALUEDESC, "");
                                        result.put(next1.getName(), _interData);
                                    }
                                }
                                //优质单位解析
                                else if ("DWLB".equals(next1.getName())) {
                                    //优质单位
                                    if (DWLBLIST.contains(ele.getText())) {
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER, in);
                                        _interData.put(FIELD, next1.getName());
                                        _interData.put(VALUE, "1");
                                        _interData.put(VALUEDESC, "");
                                        result.put(next1.getName(), _interData);
                                    }
                                    //非优质单位
                                    else {
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER, in);
                                        _interData.put(FIELD, next1.getName());
                                        _interData.put(VALUE, "0");
                                        _interData.put(VALUEDESC, "");
                                        result.put(next1.getName(), _interData);
                                    }
                                }
                            }
                        }
                    }
                }
            }else if("CIIS76".equals(in)){

                List<Element> childElements = root.elements();
                for (Element child : childElements) {
                    List<Element> elementList = child.elements();
                    for (Element ele : elementList) {
                        System.out.println(ele.getName());
                            Iterator iterator = ele.elementIterator();
                            while(iterator.hasNext()){
                                Element next = (Element) iterator.next();
                                System.out.println(next.getName());
                                Iterator iterator1 = next.elementIterator();
                                while(iterator1.hasNext()){
                                    Element next1 = (Element) iterator1.next();
                                    System.out.println(next1.getName());
                                    if("GJJYJE".equals(next1.getName())){
                                        Map<String, String> _interData = new HashMap<>();
                                        _interData.put(INTER,in);
                                        _interData.put(FIELD,next1.getName());
                                        _interData.put(VALUE, next1.getText());
                                        _interData.put(VALUEDESC, "");
                                        result.put( next1.getName(), _interData);
                                        return  result;
                                    }
                                }

                            }
                        }
                    }
                }
            else{
                Iterator elements=root.elementIterator();
                while (elements.hasNext()){
                    Element child= (Element) elements.next();
                    Iterator elementIterator = child.elementIterator();
                    while(elementIterator.hasNext()){
                        Element next = (Element)elementIterator.next();
                        Map<String, String> _interData = new HashMap<>();
                        _interData.put(INTER,in);
                        _interData.put(FIELD,next.getName());
                        _interData.put(VALUE, next.getText());
                        _interData.put(VALUEDESC, "");
                        result.put(next.getName(), _interData);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private   FormEncodingBuilder makeBuilderFromMap(final Map<String, Object> map) {
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        for (final Map.Entry<String, Object> entrySet : map.entrySet()) {
            formBody.add(entrySet.getKey(), entrySet.getValue().toString());
        }
        return formBody;
    }

}
