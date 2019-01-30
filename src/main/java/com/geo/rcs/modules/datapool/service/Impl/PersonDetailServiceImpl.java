package com.geo.rcs.modules.datapool.service.Impl;

import com.geo.rcs.modules.datapool.dao.PersonDetailMapper;
import com.geo.rcs.modules.datapool.entity.PersonDetail;
import com.geo.rcs.modules.datapool.service.PersonDetailService;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class PersonDetailServiceImpl implements PersonDetailService {

    @Autowired
    private PersonDetailMapper personDetailMapper;

    @Override
    public void addByRidDimension(String rid, String dimension, Map<String, Object> parameters) {
        String partTableNameId = rid.substring(1,2);
        String partTableName = getPartTableName(partTableNameId);

        // init parameters
        String now = DatetimeFormattor.nowDateTime();
        int queryTimes = 1;
        int updateTimes = 1;
        int queryStatus = 1;
        int updateStatus = 1;
        int maxInterval = 1;
        int minInterval = 1;

        parameters.put("partTableName", partTableName);
        parameters.put("createTime", now);
        parameters.put("updateTime", now);
        parameters.put("rid", rid);
        parameters.put("dimension", dimension);
        parameters.put("queryTimes", String.valueOf(queryTimes));
        parameters.put("queryStatus", String.valueOf(queryStatus));
        parameters.put("updateTimes", String.valueOf(updateTimes));
        parameters.put("updateStatus", String.valueOf(updateStatus));
        parameters.put("maxInterval", String.valueOf(maxInterval));
        parameters.put("minInterval", String.valueOf(minInterval));

        if(isExists(partTableName)){
            personDetailMapper.addByRidDimension(parameters);
        }else{
            personDetailMapper.createPartTable(partTableName);
            personDetailMapper.addByRidDimension(parameters);
        }
    }

    @Override
    public PersonDetail findByRidDimension(String rid, String dimension) {
        String partTableNameId = rid.substring(1,2);
        String partTableName = getPartTableName(partTableNameId);
        if(isExists(partTableName)) {
            return personDetailMapper.findByRidDimension(partTableName, rid, dimension);
        }else{
            return null;
        }
    }

    @Override
    public void deleteByRidDimension(String rid, String dimension) {
        //todo: delete person datail
    }


    @Override
    public void updateById(Map<String, String> updateParameters) {

        Long id = Long.valueOf(updateParameters.get("id"));
        String rid = updateParameters.get("rid");
        String partTableNameId = rid.substring(1,2);
        String partTableName = getPartTableName(partTableNameId);

        updateParameters.put("partTableName", partTableName);

        personDetailMapper.updateById(updateParameters);

    }

    /**
     * Dropped Method
     *
    @Override
    public void updateByRidDimension(String rid, String dimension, Map <String, String> parameters) {
        todo: update person datail
    }
    */

    @Override
    public Boolean isExists(String partTableName) {
        partTableName = getPartTableName(partTableName);
        String res = personDetailMapper.isExists(partTableName);
        if(res != null && res.equals(partTableName)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void createPartTable(String partTableName) {
        partTableName = getPartTableName(partTableName);
        personDetailMapper.createPartTable(partTableName);
    }

    /**
     * Generate part—table Name
     * @param partableTableName
     * @return
     */
    private String getPartTableName(String partableTableName){
        partableTableName = "datapool_detail_"+ partableTableName.replace("datapool_detail_", "");
        partableTableName = partableTableName.replace("_part", "") + "_part";
        return  partableTableName;
    }
}
