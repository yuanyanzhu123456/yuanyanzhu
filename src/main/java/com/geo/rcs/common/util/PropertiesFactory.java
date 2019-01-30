package com.geo.rcs.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件属性工厂
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/11/24 10:18
 */
public class PropertiesFactory {
    private static Logger logger = LoggerFactory.getLogger(PropertiesFactory.class);

    private static PropertiesBean propertiesBean = null;

    public static PropertiesBean getPropertiesBean() {
        if (propertiesBean == null) {
            propertiesBean = SpringContextUtils.getBean(PropertiesBean.class);
        }
        if (propertiesBean == null){
            logger.error("get PropertiesFactory propertiesBean fails");
        }
        return propertiesBean;
    }

}
