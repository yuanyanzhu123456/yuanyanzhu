package com.geo.rcs.modules.api.modules.eventin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.okhttp.okHttpClient;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.modules.api.annotation.AuthIgnore;
import com.geo.rcs.modules.api.annotation.LoginUser;
import com.geo.rcs.modules.api.modules.user.entity.ApiUserToken;
import com.geo.rcs.modules.api.modules.user.service.ApiUserTokenService;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.engine.service.EngineService;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.source.service.SourceService;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.oauth2.OAuth2Filter;
import com.geo.rcs.modules.sys.service.SysUserService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.modules.eventin.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 上午11:59
 */


@RestController
@RequestMapping("/api/test")
public class ApiTestController extends BaseController {

    /**
     * 数据平台地址
     */
    @Value("${dataSourceServer.rcsDataSource.url}")
    private String dataSourceServerUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final String getFieldRes() {
        return dataSourceServerUrl + "/dataSource/getFieldRes";
    }

    @SysLog("压测进件T1")
    @PostMapping("/t1")
    public Geo eventEntryTest1(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }

        return Geo.ok();

    }


    @SysLog("压测进件T2")
    @PostMapping("/t2")
    public Geo eventEntryTest2(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }

        return Geo.ok();

    }

    @AuthIgnore
    @SysLog("压测进件T2")
    @PostMapping("/t2x2")
    public Geo eventEntryTest2x2(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }

        return Geo.ok().put("data", "{\"msg\":\"成功\",\"code\":2000,\"data\":{\"score\":150,\"thresholdMin\":100,\"matchType\":1,\"ruleList\":[{\"conditionRelationShip\":\"375\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10006\",\"fieldName\":\"IdPhoneNameValidate\",\"fieldType\":\"string\",\"id\":10537,\"operator\":\"!=\",\"parameter\":\"0\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"三维验证一致\"}],\"fieldRelationShip\":\"10537\",\"id\":375,\"name\":\"手机三要素验证不一致\",\"result\":false}],\"id\":10375,\"level\":3,\"name\":\"手机三要素验证不一致\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"377\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10539,\"operator\":\"==\",\"parameter\":\"03\",\"result\":false,\"value\":\"3\",\"valueDesc\":\"(24,+)\"},{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10541,\"operator\":\"==\",\"parameter\":\"04\",\"result\":false,\"value\":\"3\",\"valueDesc\":\"(24,+)\"}],\"fieldRelationShip\":\"10539&&10541\",\"id\":377,\"name\":\"手机号在网时长小于6个月\",\"result\":false}],\"id\":10377,\"level\":3,\"name\":\"手机在网时长小于6个月\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"379\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10543,\"operator\":\"==\",\"parameter\":\"3\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10545,\"operator\":\"==\",\"parameter\":\"2\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10547,\"operator\":\"==\",\"parameter\":\"4\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10543||10545||10547\",\"id\":379,\"name\":\"手机号状态为不在网、不可用、销号\",\"result\":false}],\"id\":10379,\"level\":3,\"name\":\"手机状态为不在网、不可用、销号\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"381\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10549,\"operator\":\"==\",\"parameter\":\"1\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10549\",\"id\":381,\"name\":\"手机号状态为停机\",\"result\":false}],\"id\":10381,\"level\":2,\"name\":\"手机号状态为停机\",\"result\":false,\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"383\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10013\",\"fieldName\":\"consumerGradePlus\",\"fieldType\":\"string\",\"id\":10551,\"operator\":\"==\",\"parameter\":\"010\",\"result\":false,\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10551\",\"id\":383,\"name\":\"手机号码消费档次月均为0元\",\"result\":false}],\"id\":10383,\"level\":3,\"name\":\"手机号码消费档次月均为0元\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"385\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10454\",\"fieldName\":\"ownPhoneNumber\",\"fieldType\":\"int\",\"id\":10553,\"operator\":\">\",\"parameter\":\"1\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"不大于2个\"}],\"fieldRelationShip\":\"10553\",\"id\":385,\"name\":\"手机号自然人接入号码个数大于2\",\"result\":false}],\"id\":10385,\"level\":2,\"name\":\"手机号自然人接入号码个数\",\"result\":false,\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"387\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10555,\"operator\":\">\",\"parameter\":\"0\",\"result\":true,\"value\":\"1\",\"valueDesc\":\"(0,3]\"},{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10557,\"operator\":\"<=\",\"parameter\":\"3\",\"result\":true,\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10555&&10557\",\"id\":387,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\",\"result\":true}],\"id\":10387,\"level\":2,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\",\"result\":true,\"score\":50,\"threshold\":50},{\"conditionRelationShip\":\"389\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10559,\"operator\":\">\",\"parameter\":\"3\",\"result\":false,\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10559\",\"id\":389,\"name\":\"手机号近3个月停机次数大于3次\",\"result\":false}],\"id\":10389,\"level\":3,\"name\":\"手机号近3个月停机次数大于3次\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"391\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10016\",\"fieldName\":\"residenceValidate\",\"fieldType\":\"string\",\"id\":10561,\"operator\":\"!=\",\"parameter\":\"1\",\"result\":true,\"value\":\"05\",\"valueDesc\":\"不在同一城市\"}],\"fieldRelationShip\":\"10561\",\"id\":391,\"name\":\"家庭地址比对不一致\",\"result\":true}],\"id\":10391,\"level\":2,\"name\":\"家庭地址比对不一致\",\"result\":true,\"score\":50,\"threshold\":50},{\"conditionRelationShip\":\"393\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10015\",\"fieldName\":\"workPlaceValidate\",\"fieldType\":\"string\",\"id\":10563,\"operator\":\"!=\",\"parameter\":\"1\",\"result\":true,\"value\":\"\",\"valueDesc\":\"查无此记录\"}],\"fieldRelationShip\":\"10563\",\"id\":393,\"name\":\"单位地址比对不一致\",\"result\":true}],\"id\":10393,\"level\":2,\"name\":\"单位地址比对不一致\",\"result\":true,\"score\":50,\"threshold\":50},{\"conditionRelationShip\":\"395\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10455\",\"fieldName\":\"phoneDepartStatus\",\"fieldType\":\"int\",\"id\":10565,\"operator\":\"==\",\"parameter\":\"1\",\"result\":false,\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10565\",\"id\":395,\"name\":\"手机号为出境状态\",\"result\":false}],\"id\":10395,\"level\":2,\"name\":\"手机号为出境状态\",\"result\":false,\"score\":0,\"threshold\":50}],\"name\":\"手机行为及运营商数据查验\",\"id\":10081,\"sourceData\":{\"residenceValidate\":{\"field\":\"residenceValidate\",\"valueDesc\":\"不在同一城市\",\"inter\":\"B19\",\"value\":\"05\"},\"outOfServiceTimes\":{\"field\":\"outOfServiceTimes\",\"valueDesc\":\"(0,3]\",\"inter\":\"B13\",\"value\":\"1\"},\"province\":{\"field\":\"province\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"山东\"},\"ownPhoneNumber\":{\"field\":\"ownPhoneNumber\",\"valueDesc\":\"不大于2个\",\"inter\":\"A5\",\"value\":\"0\"},\"city\":{\"field\":\"city\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"枣庄\"},\"isp\":{\"field\":\"isp\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"电信\"},\"onlineStatus\":{\"field\":\"onlineStatus\",\"valueDesc\":\"正常在用\",\"inter\":\"A4\",\"value\":\"0\"},\"onlineTime\":{\"field\":\"onlineTime\",\"valueDesc\":\"(24,+)\",\"inter\":\"A3\",\"value\":\"3\"},\"phoneDepartStatus\":{\"field\":\"phoneDepartStatus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"C2\",\"value\":\"\"},\"IdPhoneNameValidate\":{\"field\":\"IdPhoneNameValidate\",\"valueDesc\":\"三维验证一致\",\"inter\":\"B7\",\"value\":\"0\"},\"consumerGradePlus\":{\"field\":\"consumerGradePlus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"D3\",\"value\":\"\"},\"workPlaceValidate\":{\"field\":\"workPlaceValidate\",\"valueDesc\":\"查无此记录\",\"inter\":\"B18\",\"value\":\"\"}},\"thresholdMax\":150,\"parameters\":{\"cid\":\"13306328903\",\"idNumber\":\"370404196212262212\",\"liveLongitude\":\"北京市\",\"realName\":\"赵玉柏\",\"workLongitude\":\"北京市\"},\"status\":2}}");

    }

    @SysLog("压测进件T2")
    @PostMapping("/t2x")
    public Geo eventEntryTest2x(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }

        return Geo.ok().put("data", "{\"msg\":\"成功\",\"code\":2000,\"data\":{\"score\":150,\"thresholdMin\":100,\"matchType\":1,\"ruleList\":[{\"conditionRelationShip\":\"375\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10006\",\"fieldName\":\"IdPhoneNameValidate\",\"fieldType\":\"string\",\"id\":10537,\"operator\":\"!=\",\"parameter\":\"0\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"三维验证一致\"}],\"fieldRelationShip\":\"10537\",\"id\":375,\"name\":\"手机三要素验证不一致\",\"result\":false}],\"id\":10375,\"level\":3,\"name\":\"手机三要素验证不一致\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"377\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10539,\"operator\":\"==\",\"parameter\":\"03\",\"result\":false,\"value\":\"3\",\"valueDesc\":\"(24,+)\"},{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10541,\"operator\":\"==\",\"parameter\":\"04\",\"result\":false,\"value\":\"3\",\"valueDesc\":\"(24,+)\"}],\"fieldRelationShip\":\"10539&&10541\",\"id\":377,\"name\":\"手机号在网时长小于6个月\",\"result\":false}],\"id\":10377,\"level\":3,\"name\":\"手机在网时长小于6个月\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"379\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10543,\"operator\":\"==\",\"parameter\":\"3\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10545,\"operator\":\"==\",\"parameter\":\"2\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10547,\"operator\":\"==\",\"parameter\":\"4\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10543||10545||10547\",\"id\":379,\"name\":\"手机号状态为不在网、不可用、销号\",\"result\":false}],\"id\":10379,\"level\":3,\"name\":\"手机状态为不在网、不可用、销号\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"381\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10549,\"operator\":\"==\",\"parameter\":\"1\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10549\",\"id\":381,\"name\":\"手机号状态为停机\",\"result\":false}],\"id\":10381,\"level\":2,\"name\":\"手机号状态为停机\",\"result\":false,\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"383\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10013\",\"fieldName\":\"consumerGradePlus\",\"fieldType\":\"string\",\"id\":10551,\"operator\":\"==\",\"parameter\":\"010\",\"result\":false,\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10551\",\"id\":383,\"name\":\"手机号码消费档次月均为0元\",\"result\":false}],\"id\":10383,\"level\":3,\"name\":\"手机号码消费档次月均为0元\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"385\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10454\",\"fieldName\":\"ownPhoneNumber\",\"fieldType\":\"int\",\"id\":10553,\"operator\":\">\",\"parameter\":\"1\",\"result\":false,\"value\":\"0\",\"valueDesc\":\"不大于2个\"}],\"fieldRelationShip\":\"10553\",\"id\":385,\"name\":\"手机号自然人接入号码个数大于2\",\"result\":false}],\"id\":10385,\"level\":2,\"name\":\"手机号自然人接入号码个数\",\"result\":false,\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"387\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10555,\"operator\":\">\",\"parameter\":\"0\",\"result\":true,\"value\":\"1\",\"valueDesc\":\"(0,3]\"},{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10557,\"operator\":\"<=\",\"parameter\":\"3\",\"result\":true,\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10555&&10557\",\"id\":387,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\",\"result\":true}],\"id\":10387,\"level\":2,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\",\"result\":true,\"score\":50,\"threshold\":50},{\"conditionRelationShip\":\"389\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10559,\"operator\":\">\",\"parameter\":\"3\",\"result\":false,\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10559\",\"id\":389,\"name\":\"手机号近3个月停机次数大于3次\",\"result\":false}],\"id\":10389,\"level\":3,\"name\":\"手机号近3个月停机次数大于3次\",\"result\":false,\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"391\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10016\",\"fieldName\":\"residenceValidate\",\"fieldType\":\"string\",\"id\":10561,\"operator\":\"!=\",\"parameter\":\"1\",\"result\":true,\"value\":\"05\",\"valueDesc\":\"不在同一城市\"}],\"fieldRelationShip\":\"10561\",\"id\":391,\"name\":\"家庭地址比对不一致\",\"result\":true}],\"id\":10391,\"level\":2,\"name\":\"家庭地址比对不一致\",\"result\":true,\"score\":50,\"threshold\":50},{\"conditionRelationShip\":\"393\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10015\",\"fieldName\":\"workPlaceValidate\",\"fieldType\":\"string\",\"id\":10563,\"operator\":\"!=\",\"parameter\":\"1\",\"result\":true,\"value\":\"\",\"valueDesc\":\"查无此记录\"}],\"fieldRelationShip\":\"10563\",\"id\":393,\"name\":\"单位地址比对不一致\",\"result\":true}],\"id\":10393,\"level\":2,\"name\":\"单位地址比对不一致\",\"result\":true,\"score\":50,\"threshold\":50},{\"conditionRelationShip\":\"395\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10455\",\"fieldName\":\"phoneDepartStatus\",\"fieldType\":\"int\",\"id\":10565,\"operator\":\"==\",\"parameter\":\"1\",\"result\":false,\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10565\",\"id\":395,\"name\":\"手机号为出境状态\",\"result\":false}],\"id\":10395,\"level\":2,\"name\":\"手机号为出境状态\",\"result\":false,\"score\":0,\"threshold\":50}],\"name\":\"手机行为及运营商数据查验\",\"id\":10081,\"sourceData\":{\"residenceValidate\":{\"field\":\"residenceValidate\",\"valueDesc\":\"不在同一城市\",\"inter\":\"B19\",\"value\":\"05\"},\"outOfServiceTimes\":{\"field\":\"outOfServiceTimes\",\"valueDesc\":\"(0,3]\",\"inter\":\"B13\",\"value\":\"1\"},\"province\":{\"field\":\"province\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"山东\"},\"ownPhoneNumber\":{\"field\":\"ownPhoneNumber\",\"valueDesc\":\"不大于2个\",\"inter\":\"A5\",\"value\":\"0\"},\"city\":{\"field\":\"city\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"枣庄\"},\"isp\":{\"field\":\"isp\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"电信\"},\"onlineStatus\":{\"field\":\"onlineStatus\",\"valueDesc\":\"正常在用\",\"inter\":\"A4\",\"value\":\"0\"},\"onlineTime\":{\"field\":\"onlineTime\",\"valueDesc\":\"(24,+)\",\"inter\":\"A3\",\"value\":\"3\"},\"phoneDepartStatus\":{\"field\":\"phoneDepartStatus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"C2\",\"value\":\"\"},\"IdPhoneNameValidate\":{\"field\":\"IdPhoneNameValidate\",\"valueDesc\":\"三维验证一致\",\"inter\":\"B7\",\"value\":\"0\"},\"consumerGradePlus\":{\"field\":\"consumerGradePlus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"D3\",\"value\":\"\"},\"workPlaceValidate\":{\"field\":\"workPlaceValidate\",\"valueDesc\":\"查无此记录\",\"inter\":\"B18\",\"value\":\"\"}},\"thresholdMax\":150,\"parameters\":{\"cid\":\"13306328903\",\"idNumber\":\"370404196212262212\",\"liveLongitude\":\"北京市\",\"realName\":\"赵玉柏\",\"workLongitude\":\"北京市\"},\"status\":2}}");

    }

    @SysLog("压测进件T2")
    @PostMapping("/t2ok")
    public Geo eventEntryTest2ok(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            Map<String, Object> headersForm = new HashMap<String, Object>();
            Map<String, Object> postForm = new HashMap<String, Object>();
            postForm.put("rulesConfig", "");
            postForm.put("userId", 1);

            headersForm.put("Content-Type", "application/json");

            if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
                throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
            }
            //getFieldRes()
            String resp = new okHttpClient().postByJSON("http://10.111.32.175:8020/api/test/t2x2",
                    JSONObject.toJSONString(postForm),
                    JSONObject.toJSONString(headersForm));

            return Geo.ok().put("data", resp);

        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }


    }

    @SysLog("压测进件T5")
    @PostMapping("/t5")
    public Geo eventEntryTest5(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }

        return Geo.ok();

    }


    @SysLog("压测进件T10")
    @PostMapping("/t10")
    public Geo eventEntryTest10(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) {

        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), "接口出现问题");
        }

        return Geo.ok();

    }

    @SysLog("压测进件T10")
    @PostMapping("/testF")
    public Geo eventEntryTestFinally(@RequestBody Map<String, Object> map, @LoginUser SysUser user, HttpServletRequest httpRequest) throws Exception {
        //从请求头或参数中获取token
        String requestToken = new OAuth2Filter().getRequestToken(httpRequest);
        if (requestToken == null) {
            return Geo.error(StatusCode.ERROR.getCode(), "登录凭证为空");
        }
        try {
            return Geo.ok();

        } catch (Exception e) {
            return Geo.error();

        } finally {
            return Geo.error("FINALLY");
        }
    }

}
