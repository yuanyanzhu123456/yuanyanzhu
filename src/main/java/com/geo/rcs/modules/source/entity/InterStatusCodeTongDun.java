package com.geo.rcs.modules.source.entity;

import java.util.*;

public class InterStatusCodeTongDun {

    public static final Map<String,String> BASICDICT_TONGDUN;

    static{
        /* 系统返回状态码 */
        Hashtable<String,String> basic = new Hashtable<String,String>();
        String td="同盾数据源";
        basic.put("000",td+"认证失败");
        basic.put("001",td+"用户认证失败");
        basic.put("100",td+"参数有误");
        basic.put("101",td+"参数不能为空");
        basic.put("415",td+"不支持的媒体类型");
        basic.put("500",td+"内部错误");
        basic.put("600",td+"合作方限流");
        basic.put("610",td+"合作方IP白名单");
        basic.put("6001",td+"流程执行异常终止");
        basic.put("9000",td+"应用未配置行业或未勾选模块");
        basic.put("9001",td+"未找到相应工作流配置");
        basic.put("9002",td+"如贷前:流量开关未开启,认证:流量开关未开启");
        basic.put("9003",td+"自定义表单校验异常,如事件类型贷前下没有自定义表单配置");
        basic.put("9004",td+"不处于数据补充状态");
        basic.put("9005",td+"不处于决策复议状态");
        basic.put("9006",td+"不处于异常不久状态");
        basic.put("9007",td+"不处于暂停状态");
        basic.put("9008",td+"根据合作方,应用,ID找不到可执行的工作流");
        basic.put("9009",td+"调用异常请联系同盾售后");
        basic.put("9010",td+"数据补充模块获取不到数据");
        basic.put("9011",td+"对不起,重试次数已达到上限或时限已过");
        basic.put("9012",td+"流程执行异常");
        basic.put("9013",td+"贷中查询输入身份证号有误");
        basic.put("9017",td+"不处于决策补充状态");
        
        BASICDICT_TONGDUN = Collections.unmodifiableMap(basic);
    }

    public static final Map<String,String> ECLDICT;
    static{
        Hashtable<String,String> tmp = new Hashtable<String,String>();
        tmp.put("10000002","暂不支持此运营商");
       

        /* 系统返回状态码 */

        ECLDICT = Collections.unmodifiableMap(tmp);
    }


    public static final List<String> ECLKEYS;

    static{
        /* 系统返回状态码列表 */
        ECLKEYS = new ArrayList<>(ECLDICT.keySet());
    }

}
