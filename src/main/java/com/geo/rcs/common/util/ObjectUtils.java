package com.geo.rcs.common.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象处理工具
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/11/17 14:47
 */
public class ObjectUtils {

    /**
     * 将request请求转为Map
     * @return
     */
    public static Map<String,Object> requestToMap(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Enumeration<String> params = request.getParameterNames();
        Map<String, Object> resultMap = new HashMap();
        if (params == null) {
            return null;
        }

        String key;
        for (Enumeration e = params; e.hasMoreElements(); ) {
            key = (String) e.nextElement();
            if(key.endsWith("List")) {
                resultMap.put(key, Arrays.asList(request.getParameterValues(key)));
            }else {
                resultMap.put(key, StringUtils.trimToNull(request.getParameter(key)));
            }
        }

        return resultMap;
    }

    /**
     * 将request请求转为Bean
     *
     * @param request
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T requestToBean(HttpServletRequest request, Class<T> beanClass) {
        if (request == null) {
            return null;
        }
        Enumeration<String> params = request.getParameterNames();
        Map<String, Object> resultMap = new HashMap();
        if (params == null) {
            return null;
        }

        String key;
        String value;
        for (Enumeration e = params; e.hasMoreElements(); ) {
            key = (String) e.nextElement();
            if(!isCheckBeanExitsPropertyName(beanClass,key)){
                continue;
            }
            value = StringUtils.trimToNull(request.getParameter(key));
            resultMap.put(key, value);
        }

        return JSONUtil.mapToBean(resultMap, beanClass);
    }

    /**
     * 判断某个属性是否是类的属性
     * @param clazz
     * @param propertyName
     * @return
     */
    private  static boolean isCheckBeanExitsPropertyName(Class<?> clazz,String propertyName){
        boolean retValue = false;
        try {
            Field field =  clazz.getDeclaredField(propertyName);
            if(null != field){
                retValue = true;
            }
        } catch (NoSuchFieldException e) {
        }
        return retValue;
    }
}
