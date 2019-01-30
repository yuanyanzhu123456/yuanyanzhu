package com.geo.rcs.modules.kafka;

import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.modules.engine.entity.User;
import com.geo.rcs.modules.event.entity.EventEntry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 11:52 2018/11/9
 */
@Component
public class ObjToStrLine {
    /**
     * 获取属性名数组
     */
    public  String getObjectLogRecord(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        String log = "";
        String field = "";
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
            //判断不需要记录日志的字段
            if ( "pageNo".equalsIgnoreCase(fieldNames[i])||"pageSize".equalsIgnoreCase(fields[i].getName())){
                continue;
            }
            if (o instanceof  EventEntry &&( "startTime".equalsIgnoreCase(fieldNames[i])||"endTime".equalsIgnoreCase(fieldNames[i]))){
                continue;
            }
            Object fieldValueByName = getFieldValueByName(fieldNames[i], o);
            if (fieldValueByName == null) {
                field = "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Integer) {
                field = (Integer) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Long) {
                field = (Long) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof String) {
                field = (String) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Date) {
                field = (DateUtils.format((Date) fieldValueByName, "yyyy-MM-dd hh:mm:ss"));
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Byte) {
                field = (Byte) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Short) {
                field = (Short) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Float) {
                field = (Float) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            } else if (fieldValueByName instanceof Double) {
                field = (Double) fieldValueByName + "";
                log += field + HadoopConsts.FIELD_SPLITE;
            }
        }
        return log;
    }

    /* 根据属性名获取属性值
    * */
    public  Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {

            return null;
        }
    }

    public  static  void main(String[] args) {
        ObjToStrLine obj = new ObjToStrLine();
        EventEntry eventEntry = new EventEntry();
        eventEntry.setAcctNo("1235");
        eventEntry.setId(1235L);
        eventEntry.setCreateTime(new Date());
        eventEntry.setElseTime(213);
        String objectLogRecord = obj.getObjectLogRecord(eventEntry);
        User user = new User();
        user.setKp(1565);
        user.setMoney(565);
        user.setTotals(45);
        String objectLogRecord2 = obj.getObjectLogRecord(user);
        System.out.println(objectLogRecord);
        System.out.println(objectLogRecord2);
    }
}
