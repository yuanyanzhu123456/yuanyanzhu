package com.geo.rcs.modules.datapool.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.datapool.entity.Person;
import com.geo.rcs.modules.datapool.entity.PersonDetail;
import com.geo.rcs.modules.datapool.service.DataPoolService;
import com.geo.rcs.modules.datapool.service.PersonDetailService;
import com.geo.rcs.modules.datapool.service.PersonService;
import com.geo.rcs.modules.datapool.util.DimensionInterMapper;
import com.geo.rcs.modules.datapool.util.PersonParser;
import com.geo.rcs.modules.datapool.util.RidGenerator;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.source.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class DataPoolServiceImpl implements DataPoolService {

    // 定义常量
    private static final String REALNAME = "realName";
    private static final String CID = "cid";
    private static final String IDNUMBER = "idNumber";
//    private static final String IDNUMBER = "idNumber";
//    private static final String IDNUMBER = "idNumber";
//    private static final String IDNUMBER = "idNumber";

    @Autowired
    public PersonService personService;

    @Autowired
    public PersonDetailService personDetailService;


    @Autowired
    public SourceService sourceService;

    /**
     * get data by factors:  cid, name, idNumber
     * 1: 检查参数：维度，用户id，参数集合（身份证，手机号，姓名，期数，间隔，单位，开始时间，或上一次时间）
     * 2. 检查是否需要请求API
     * 3. 是-直接请求api更新缓存数据，计算符合间隔的数据：如第二期的放款次数，注册次数
     * 4. 否-直接请求缓存数据，计算符合间隔的数据：如第二期的放款次数，注册次数
     * @return
     */
    @Override
    public String getPersonByThreeFactor(String dimension, Long userId, Map<String, Object> parameters) {

        try{
            String inter = "";
            //todo: make dimension to inter dao.
            if(DimensionInterMapper.validDimension(dimension)){
               inter = DimensionInterMapper.getInterByDimension(dimension);
            }else{
                return JSONObject.toJSONString(Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage()));
            }
            // 检查参数
            if(validateParameters(parameters)){
                if(checkCacheStatus(dimension, userId, parameters)){
                    String resp = getPersonByCache(dimension, userId, parameters);
                    resp = PersonParser.analysis(dimension, resp, parameters);
                    return resp;
                }else{
                    String resp = getPersonByApi(dimension, inter, userId, parameters);
                    resp = PersonParser.analysis(dimension, resp, parameters);
                    return resp;
                }
            }else{
                // 参数检查异常，模拟返回结果
                return JSONObject.toJSONString(Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage()));
            }
        }catch (DuplicateKeyException e) {
            e.printStackTrace();
            return JSONObject.toJSONString(Geo.error(StatusCode.DATA_POOL_UNION_ERROR.getCode(), StatusCode.DATA_POOL_UNION_ERROR.getMessage()));
        } catch (RcsException e2){
            e2.printStackTrace();
            return JSONObject.toJSONString(Geo.error(e2.getCode(),e2.getMessage()));
        } catch (Exception e3){
            e3.printStackTrace();
            return JSONObject.toJSONString(Geo.error(StatusCode.ERROR.getCode(),StatusCode.ERROR.getMessage()));
        }

    }

    /**
     * 检查数据池缓存有效性
     * @param dimension
     * @param userId
     * @param parameters
     * @return
     */
    // @Transactional
    public boolean checkCacheStatus(String dimension, Long userId, Map<String, Object> parameters){

        LogUtil.info("数据池服务", "检查缓存状态:" + parameters, userId.toString(),"开始");

        Boolean cacheStatus;
        PersonDetail personDetail;
        Person person =  personService.findByRid(getRidByParameters(parameters));

        // todo: 并发会出现同一数据冲突
        if(person==null){
            personService.addByRid(getRidByParameters(parameters),parameters);
            personDetail = personDetailService.findByRidDimension(getRidByParameters(parameters), dimension);
        }else{
            personDetail = personDetailService.findByRidDimension(getRidByParameters(parameters), dimension);
        }

        if(personDetail!=null){
            Date updateTime = personDetail.getUpdateTime();
            Date validDate = DatetimeFormattor.getRecentDateime(-1);
            cacheStatus = updateTime.compareTo(validDate)>=0;
        }else{
            cacheStatus = false;
        }

        LogUtil.info("数据池服务", "检查缓存状态:" + parameters, userId.toString(),cacheStatus?"有效":"失效");
        return cacheStatus;
    }

    /**
     * 从数据池获取最近信息
     * @param dimension
     * @param userId
     * @param parameters
     * @return
     */
    public String getPersonByCache(String dimension, Long userId, Map<String, Object> parameters){
        //todo: get Person by datapool

        LogUtil.info("数据池服务", "从缓存获取数据:" + parameters, userId.toString(),"开始");

        Person person =  personService.findByRid(getRidByParameters(parameters));

        if(person!=null){
            PersonDetail personDetail = personDetailService.findByRidDimension(getRidByParameters(parameters), dimension);
            if(personDetail!=null){

                // todo: 高并发加锁：使用显式锁或使用乐观锁
                Map<String, String> updateParameters =  new HashMap<>();
                updateParameters.put("queryTimes",  String.valueOf(personDetail.getQueryTimes()+1));
                updateParameters.put("rid",  personDetail.getRid());
                updateParameters.put("id",  String.valueOf(personDetail.getId()));
                updateParameters.put("dimension",  personDetail.getDimension());

                personDetailService.updateById(updateParameters);

                LogUtil.info("数据池服务", "从缓存获取数据:" + parameters, userId.toString(),"完成");

                return personDetail.getRecentData();
            }
        }
        LogUtil.info("数据池服务", "从缓存获取数据:" + parameters, userId.toString(),"缓存数据为空null");

        return null;

    }

    /**
     * 从数据源服务获取最新数据
     * @param inter
     * @param userId
     * @param parameters
     * @return
     */
    public String getPersonByApi(String dimension, String inter, Long userId, Map<String, Object> parameters){

        LogUtil.info("数据池服务", "从第三方数据源获取数据:" + parameters, userId.toString(),"开始");

        String resp = sourceService.getInterResByThird(userId, inter, parameters);

        if(resp!=null&&resp.length()>0){
            // 解析数据
            resp = PersonParser.parse(dimension, resp);

            try{
                if(resp!=null&& resp.length()>0){
                    resp = updatePersonFromApi(dimension, userId, parameters, resp);
                    LogUtil.info("数据池服务", "从第三方数据源获取数据:" + parameters, userId.toString(),"完成");
                    return resp;
                }else{
                    // 如果数据源返回为空，直接报异常
                    throw new RcsException(StatusCode.DATASOURCE_RESP_ERROR);
                    // 使用Json数据返回异常信息：dropped
                    // return JSONObject.toJSONString(Geo.error(StatusCode.DATASOURCE_RESP_ERROR.getCode(), StatusCode.DATASOURCE_RESP_ERROR.getMessage()));
                }
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.error("数据池服务", "更新缓存为API最新数据:" + parameters, userId.toString(),"更新缓存失败",e);
                throw new RcsException(StatusCode.DATA_POOL_STORAGE_ERROR);
            }
        }else{
            return JSONObject.toJSONString(Geo.error(StatusCode.DATASOURCE_CONN_ERROR.getCode(), StatusCode.DATASOURCE_CONN_ERROR.getMessage()));
        }
    }

    /**
     * 从数据源服务获取最新数据
     * @param dimension
     * @param userId
     * @param parameters
     * @return
     */
    public String updatePersonFromApi(String dimension, Long userId, Map<String, Object> parameters, String resp){
        //todo: get Person by sourceService

        LogUtil.info("数据池服务", "更新缓存为API最新数据:" + parameters, userId.toString(),"开始");

        String groupData = resp;
        parameters.put("recentData", groupData);

        Person person = personService.findByRid(getRidByParameters(parameters));

        if(person==null){
            personService.addByRid(getRidByParameters(parameters),parameters);
        }

        PersonDetail personDetail = personDetailService.findByRidDimension(getRidByParameters(parameters), dimension);

        if(personDetail!=null){
            // todo: 高并发加锁：使用显式锁或使用乐观锁
            Map<String, String> updateParameters =  new HashMap<>();
            String now = DatetimeFormattor.nowDateTime();
            groupData = PersonParser.group(personDetail.getDimension(), personDetail.getRecentData(), resp, parameters);

            updateParameters.put("updateTimes", String.valueOf(personDetail.getUpdateTimes()+1));
            updateParameters.put("queryTimes",  String.valueOf(personDetail.getQueryTimes()+1));
            updateParameters.put("rid",  personDetail.getRid());
            updateParameters.put("id",  String.valueOf(personDetail.getId()));
            updateParameters.put("dimension",  personDetail.getDimension());
            updateParameters.put("updateTime", now);
            updateParameters.put("recentData", groupData);

            personDetailService.updateById(updateParameters);

            LogUtil.info("数据池服务", "更新缓存为API最新数据:" + parameters, userId.toString(),"完成");

            return groupData;
        }else{
            //todo：优化-后台异步任务
            personDetailService.addByRidDimension(getRidByParameters(parameters), dimension, parameters);

            LogUtil.info("数据池服务", "缓存为空，更新为API最新数据:" + parameters, userId.toString(),"完成");

            return resp;
        }
    }

    /**
     * 参数检查
     * @param parameters
     * @return
     */
    public Boolean validateParameters(Map<String, Object> parameters){
        //todo: validate parameters validity

        String realName = String.valueOf(parameters.get(REALNAME));
        String cid = String.valueOf(parameters.get(CID));
        String idNumber = String.valueOf(parameters.get(IDNUMBER));

        return !(realName == null || realName.length()<2 || cid == null ||
               cid.length()<11 || idNumber == null || idNumber.length() < 18);
    }

    /**
     * 获取RID
     * @param parameters
     * @return
     */
    public String getRidByParameters(Map<String, Object> parameters){

        String realName = String.valueOf(parameters.get(REALNAME));
        String cid = String.valueOf(parameters.get(CID));
        String idNumber = String.valueOf(parameters.get(IDNUMBER));

        if(realName == null || realName.length()<2 || cid == null ||
                cid.length()<11 || idNumber == null || idNumber.length() < 18){
            throw  new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());
        }

        return RidGenerator.generate(realName, idNumber, cid);
    }

}
