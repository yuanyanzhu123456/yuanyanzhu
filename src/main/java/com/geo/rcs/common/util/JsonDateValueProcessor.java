package com.geo.rcs.common.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月09日 下午4:02
 */
public class JsonDateValueProcessor implements JsonValueProcessor{
    private  String datePattern = "yyyy-MM-dd HH:mm:ss";//默认将Date转成的我们需要的样式

    public JsonDateValueProcessor() {
        super();
    }

    public JsonDateValueProcessor(String datePattern) {//这个构造方法的作用就是我们在创建对象还可以改变默认样式
        super();
        this.datePattern = datePattern;
    }


    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {//这里强调下 这里的value其实是数组里单个value值
        try {                              //所以这个方法的实现和下面那个一毛一样 你可以合并
            if(value instanceof Date){          //我之所以没合并是为了做实验 验证强调的点
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                Date date = (Date)value;
                return sdf.format(date);
            }
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }
//      return null;
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        try {
            if(value instanceof Date){
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                Date date = (Date)value;
                return sdf.format(date);
            }
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }
//      return null;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
}
