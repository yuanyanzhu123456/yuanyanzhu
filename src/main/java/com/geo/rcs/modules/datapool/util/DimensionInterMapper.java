package com.geo.rcs.modules.datapool.util;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;

import java.util.*;

public enum DimensionInterMapper {

    /**
     * 多平台状态维度
     */
    MULTIPLATE("MULTIPLATE", Arrays.asList("T40301")),

    NETSTATUS("NETSTATUS", Arrays.asList("A4"));


    /**
     * 构造器
     */
    private String name;
    private List<String> interList;

    DimensionInterMapper(String name, List<String> interList) {
        this.name = name;
        this.interList = interList;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public List<String> getInterList() {
        return interList;
    }


    /**
     * 根据维度获取接口列表
     * @param dimension
     * @return
     */
    public static List<String> getInterListByDimension(String dimension){
        if(validDimension(dimension)){
            return DimensionInterMapper.valueOf(dimension.toUpperCase()).getInterList();
        }else{
            throw new RcsException(StatusCode.DATA_POOL_DIMENSION_ERROR);
        }
    }

    /**
     * 根据维度获取单接口
     * @param dimension
     * @return
     */
    public static String getInterByDimension(String dimension){
        if(validDimension(dimension)){
            return DimensionInterMapper.valueOf(dimension.toUpperCase()).getInterList().get(0);
        }else{
            throw new RcsException(StatusCode.DATA_POOL_DIMENSION_ERROR);
        }
    }

    /**
     * 验证维度合法性
     * @param dimension
     * @return
     */
    public static Boolean validDimension(String dimension){

        List<String> dimensions = new ArrayList<>();
        for (Enum e: DimensionInterMapper.values()) {
            dimensions.add(e.toString());
        }
        dimension = dimension.toUpperCase();
        return dimensions.contains(dimension);

    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(validDimension("dsaf"));
        System.out.println(validDimension("MultiPlate"));

        System.out.println(getInterByDimension("MultiPlate".toUpperCase()));

        // 维度错误
        System.out.println(getInterByDimension("dasf".toUpperCase()));

    }

}
