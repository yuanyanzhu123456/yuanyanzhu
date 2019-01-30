package com.geo.rcs.modules.source.service.impl;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.source.handler.ValidateHandler;
import com.geo.rcs.modules.source.service.ApiMonitorService;
import com.geo.rcs.modules.source.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 12:14 2018/7/18
 */
@Service("apiMonitorService")
public class ApiMonitorServiceImpl implements ApiMonitorService {
    @Autowired
    InterfaceService interfaceService;
    private Map<String,List<String>> systemValidInterMap = new HashMap<String,List<String>>();
    @Override
    public List<String>  getRulesSourceData(List<String> interList, String parameters,Long userId, Integer type) throws Exception {
        ArrayList<String> interResultList = new ArrayList<>();

        String interData = null;
        String rulesConfig = null;
        List<String> tongDunInerList = new ArrayList<>();
        for (String inter : interList) {
            String interName = ValidateHandler.interNameValidate(inter);
            //校验接口合法性
            String dataSourceType = ValidateHandler.getDataSourceType(new SourceServiceImpl().getInterMap(),interName);
            System.out.println(interName+"==请求类型:"+dataSourceType);
            if (ValidateHandler.TONG_DUN.equals(dataSourceType)) {
                tongDunInerList.add(interName);
                System.out.println("放入同盾接口list"+tongDunInerList);
                continue;
            } else if (ValidateHandler.GEO.equals(dataSourceType)) {
                System.out.println("interName:" + interName+"请求geo");
                if (type==1){
                    interData = interfaceService.getInterfaceDataMap(interName, parameters, userId, type);
                    interResultList.add(interData);
                }
            } else {
                System.out.println("接口参数错误：不识别的接口：" + interName + "  不在合法参数内：" + ValidateHandler.getValidInterSet());
                throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());
            }

        }
        // 获取同盾数据源接口数据
        if (tongDunInerList.size() > 0) {
            // 一次性请求同盾数据源
            interData = interfaceService
                    .getInterfaceDataMapTongDun(tongDunInerList, parameters,type);
            interResultList.add(interData);
        }

        System.out.println("[RCS-INFO]:数据源调用外部返回结果：");

		/* 解析接口数据 */
//        rulesConfig= JSON.toJSONString(interResultList);
//		System.out.println("[RCS-INFO]:数据源引擎返回结果：");
//        System.out.println(rulesConfig);
        System.out.println(interResultList);
//        ArrayList arrayList = JSON.parseObject(rulesConfig, ArrayList.class);
        return interResultList;
    }
}
