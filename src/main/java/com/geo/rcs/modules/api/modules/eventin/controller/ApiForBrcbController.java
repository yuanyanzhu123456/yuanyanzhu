package com.geo.rcs.modules.api.modules.eventin.controller;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.api.annotation.LoginUser;
import com.geo.rcs.modules.kafka.HadoopConsts;
import com.geo.rcs.modules.kafka.KafkaProducer;
import com.geo.rcs.modules.kafka.ObjToStrLine;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.modules.eventin.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2019年01月12日 15:14
 */
@RestController
@RequestMapping("/api/event")
@SuppressWarnings("all")
public class ApiForBrcbController {

    @Value("${spring.kafka.flinkStreamOpen}")
    private boolean open;

    /**
     * 创建一个静态钥匙-值是任意的
     */
    static Object ThreadKey = "ApiForBrcb";

    @Autowired
    private ObjToStrLine objToStrLine;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ApiEventInController apiEventInController;

    @SysLog("进件接口调用（数据平台接口）")
    @PostMapping("/entryBrcb")
    public Geo entryBrcb(@RequestBody String parameterJson,
                          @LoginUser SysUser user) {

        try {
            Map<String, Object> stringObjectMap = JSONUtil.jsonToMap(parameterJson);

            Map<String,Object> parameterMap = new ConcurrentHashMap<>();

            try{
                synchronized (ThreadKey){
                    if (open) {
                        //推送到kafka
                        if(parameterJson.startsWith("{\"Login")){
                            kafkaProducer.sendMessage(HadoopConsts.TOPIC_BRCB_LOGIN, parameterJson);
                            parameterMap = (Map<String,Object>)stringObjectMap.get("Login");
                        }
                        else if(parameterJson.startsWith("{\"TxInfo")){
                            kafkaProducer.sendMessage(HadoopConsts.TOPIC_BRCB_TXINFO, parameterJson);
                            parameterMap = (Map<String,Object>)stringObjectMap.get("TxInfo");
                        }

                    }
                }
            }catch (Exception e){
                return Geo.error(StatusCode.KAFKA_ERROR.getCode(),StatusCode.KAFKA_ERROR.getMessage());
            }

            parameterMap.put("type",0);
            Geo geo = apiEventInController.eventEntry(parameterMap, user);
            return geo;
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof RcsException){
                return Geo.error(((RcsException) e).getCode(),((RcsException) e).getMsg());
            }
            return Geo.error();
        }

    }
}
