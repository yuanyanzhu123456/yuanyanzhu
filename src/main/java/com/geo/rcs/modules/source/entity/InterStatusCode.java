package com.geo.rcs.modules.source.entity;

import java.util.*;

public class InterStatusCode {

    public static final Map<String,String> BASICDICT;

    static{
        /* 系统返回状态码 */
        Hashtable<String,String> basic = new Hashtable<String,String>();

        basic.put("200","成功");
        basic.put("-888","登录失败,用户不存在");
        basic.put("-999","用户名不能为空");
        basic.put("-747","该用户已经删除");
        basic.put("-700","登陆出现异常");
        basic.put("-666","登陆失败,记录登陆信息失败");
        basic.put("-555","登录失败,用户名密码错误");
        basic.put("-556","图形验证码输入错误");
        basic.put("-444","登录失败,账户被锁定");
        // 说明: 拓展返回内容说明,原token为空拓展解释为登录失败,请检查数据源账户配置
        // 原: basic.put("-100","token为空（需要重新获tokenID）");
        basic.put("-100","登录失败,请检查数据源账户配置");
        basic.put("-200","用户被新登录挤掉（需要重新获tokenID）");
        basic.put("-300","用户未登录（需要重新获tokenID）");
        basic.put("-400","用户未具备访问权限");
        basic.put("500","服务器错误");
        basic.put("-41054","注册失败");
        basic.put("50001","剩余使用量不足");
        basic.put("50002","接口查询接口类型参数配置错误");
        basic.put("50003","文件拷贝错误");
        basic.put("50004","业务类型为空");
        basic.put("1100000","授权码格式错误");
        basic.put("1000000","暂不支持此运营商");
        basic.put("1000005","type为空或type格式错误(正确示例：A1,A5,B1)");
        basic.put("1000006","手机号码错误(没有匹配的运营商)");
        basic.put("1000020","手机号不能为空");
        basic.put("1000009","手机号格式错误");
        basic.put("1000021","身份证号不能为空");
        basic.put("1000007","身份证号格式错误");
        basic.put("1000008","姓名不能为空");
        basic.put("49999","余额不足");
        basic.put("100000801","姓名不能为乱码");
        basic.put("100000802","姓名格式错误");
        basic.put("501","参数错误");
        basic.put("10009","系统异常");
        basic.put("10010","对象异常");
        basic.put("10011","其他异常");
        // 说明: 拓展返回内容说明,原token为空拓展解释为登录失败,请检查数据源账户配置
        // 原: basic.put("10012","签名验证失败");
        basic.put("10012","签名验证失败,请检查数据源账户签名配置项");
        basic.put("10013","数据查询超时");
        basic.put("100000001","不支持虚拟运营商");
        basic.put("49998","T、Y、Z、E系列接口不能和其他接口同时调用");
        basic.put("50020","回调接口和实时接口不能同时调用");

        BASICDICT = Collections.unmodifiableMap(basic);
    }

    public static final Map<String,String> ECLDICT;
    static{
        Hashtable<String,String> tmp = new Hashtable<String,String>();
        tmp.put("10000002","暂不支持此运营商");
        tmp.put("10000003","数据源异常");
        tmp.put("10000004","查无此记录");
        tmp.put("10000005","Type为空或type格式错误");
        tmp.put("10000006","手机号格式错误或手机号不能为空");
        tmp.put("10000007","身份证号格式错误或身份证号不能为空");
        tmp.put("10000008","姓名不能为空");
        tmp.put("10000010","月份格式错误或月份不能为空");
        tmp.put("10000011","接口返回错误或不支持此接口");
        tmp.put("10000012","参数错误");
        tmp.put("10000013","不支持此接口");
        tmp.put("10000014","休息地址不能为空");
        tmp.put("10000015","工作地址不能为空");
        tmp.put("10000018","经度不能为空");
        tmp.put("10000019","经度格式错误;经度范围[-180,180]");
        tmp.put("10000020","纬度不能为空");
        tmp.put("10000021","纬度格式错误;纬度范围[-90,90]\n");
        tmp.put("10000022","未命中黑名单");
        tmp.put("10000023","常驻地址不能为空");
        tmp.put("10000131","银行卡号格式错误");
        tmp.put("10000132","银行卡号不能为空");

        /* 系统返回状态码 */

        ECLDICT = Collections.unmodifiableMap(tmp);
    }


    public static final List<String> ECLKEYS;

    static{
        /* 系统返回状态码列表 */
        ECLKEYS = new ArrayList<>(ECLDICT.keySet());
    }

}
