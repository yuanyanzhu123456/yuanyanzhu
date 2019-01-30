package com.geo.rcs.modules.source.handler;


import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.source.service.impl.InterfaceServiceImpl;

import java.util.HashMap;
import java.util.Map;


public class InterfaceHandler {

    public static void main(String[]  args){

        String innerIfType =  "A3" ;
        String cid = "13306328903" ;
        String idNumber = "370404196212262212" ;
        String realName = "赵玉柏" ;

        String parameters = "" +
                "{\n" +
                "  \"cid\": \"17692171798\",     \n" +
//                "  \"cid\": \"13306328903\",     \n" +
                "  \"idNumber\": \"370404196212262212\",  \n" +
                "  \"cardNo\": \"6228480402564890018\",  \n" +
                "  \"realName\": \"赵玉柏\",      \n" +
                "  \"company\": \"北京集奥聚合科技有限公司\", \n" +
                "  \"ip\": \"10.122.144.1\" \n" +
                "}";

        Map result = new HashMap<>();
        try{
            InterfaceService interfaceService = new InterfaceServiceImpl();
            result = interfaceService.getDataMap(innerIfType, parameters, 1L);
            System.out.println("数据返回：" + result);
        }catch (RcsException e){
            System.out.println("RCS错误：" + e.getCode() + e.getMessage());
        }catch (Exception e){
            System.out.println("错误：" + e);

        }
    }

}
