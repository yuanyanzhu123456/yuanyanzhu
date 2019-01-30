package com.geo.rcs.modules.admin.adminIndex.service.impl;

import com.geo.rcs.modules.admin.adminIndex.dao.AdminViewMapper;
import com.geo.rcs.modules.admin.adminIndex.service.AdminViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by geo on 2018/11/12.
 */
@Service
public class AdminViewServiceImpl implements AdminViewService{

    @Autowired
    AdminViewMapper adminViewMapper;

    @Override
    public Map<String, Object> statistic(Long id) {

        Map<String,Object> result  = new HashMap<>();
        //查unique_code
        Long uniqueCode = adminViewMapper.queryUniqueCode(id);

        //查公司员工数
        Long userNum = adminViewMapper.getUserNum(uniqueCode);


        //查客户概览信息
        List<Map<Object,Object>> getUserInfo;
        getUserInfo = adminViewMapper.getUserInfo(id);

        //查询活跃度
        Long  num = adminViewMapper.getActivity(id);

        //1--2 low 3--4middle 5--7hign
        if(num >= 0 && num <= 2){
            result.put("level","low");
        }else if(num > 2 && num <= 4){
            result.put("level","middle");
        }else if(num > 4 && num <= 8){
            result.put("level","high");
        }

        //全部审批记录统计
        Long allCount = adminViewMapper.getAllCount(uniqueCode);


        //我发出审批记录统计
        String submitter = adminViewMapper.getSubmitter(id);
        Long myCount = adminViewMapper.getMyCount(submitter);

        //管理员或超级管理员才能查看待处理审批
        Long roleId = adminViewMapper.getRoleId(id);

        if (2 == roleId || 1 == roleId || 3 == roleId || 5 == roleId || 6==roleId){
            //待我处理审批记录统计
            Long prepareCount = adminViewMapper.getPrepareCount(uniqueCode);
            result.put("prepareCount",prepareCount);
        }else {
            result.put("prepareCount",0);
        }

        //Long prepareCount = adminViewMapper.getPrepareCount(uniqueCode);
        result.put("userNum",userNum);
        result.put("allCount",allCount);
        result.put("myCount",myCount);
        //result.put("prepareCount",prepareCount);
        result.put("getUserInfo",getUserInfo);
        return result;
    }
}
