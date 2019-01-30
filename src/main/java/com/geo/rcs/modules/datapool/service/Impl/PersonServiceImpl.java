package com.geo.rcs.modules.datapool.service.Impl;

import com.geo.rcs.modules.datapool.dao.PersonMapper;
import com.geo.rcs.modules.datapool.entity.Person;
import com.geo.rcs.modules.datapool.service.PersonService;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    public void addByRid(String rid, Map<String, Object> parameters) {

        String partTableNameId = rid.substring(1,2);
        String partTableName = getPartTableName(partTableNameId);

        String now = DatetimeFormattor.nowDateTime();

        parameters.put("partTableName", partTableName);
        parameters.put("createTime", now);
        parameters.put("updateTime", now);
        parameters.put("rid", rid);
        parameters.put("remark", "");

        if(isExists(partTableName)){
            personMapper.addByRid(parameters);
        }else{
            personMapper.createPartTable(partTableName);
            personMapper.addByRid(parameters);
        }
    }

//    @Override
//    public Person updateByRid(String rid) {
//        String partTableNameId = rid.substring(1,2);
//        String partTableName = getPartTableName(partTableNameId);
//        if(isExists(partTableName)) {
//            return personMapper.updateByRid(partTableName, rid);
//        }else{
//            return null;
//        }
//    }

    @Override
    public Person findByRid(String rid) {
        String partTableNameId = rid.substring(1,2);
        String partTableName = getPartTableName(partTableNameId);
        if(isExists(partTableName)) {
            return personMapper.findByRid(partTableName, rid);
        }else{
            return null;
        }
    }

    @Override
    public void deleteByRid(String rid) {
        String partTableNameId = rid.substring(1,2);
        String partTableName = getPartTableName(partTableNameId);
        if(isExists(partTableName)) {
            personMapper.deleteByRid(partTableName, rid);
        }
    }

    @Override
    public Boolean isExists(@Param(value = "partTableName") String partTableName){
        partTableName = getPartTableName(partTableName);
        String res = personMapper.isExists(partTableName);
        if(res != null && res.equals(partTableName)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void createPartTable(String partTableName){
        partTableName = getPartTableName(partTableName);
        personMapper.createPartTable(partTableName);
    }

    /**
     * Generate part—table Name
     * @param partableTableName
     * @return
     */
    private String getPartTableName(String partableTableName){
        partableTableName =  "datapool_"+ partableTableName.replace("datapool_", "");
        partableTableName =  partableTableName.replace("_part", "") + "_part";
        return  partableTableName;
    }
}
