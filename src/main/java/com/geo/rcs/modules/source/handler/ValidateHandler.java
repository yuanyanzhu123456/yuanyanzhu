package com.geo.rcs.modules.source.handler;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;

import java.util.*;

public class ValidateHandler {


    public static final Map<String,String> INTER2FIELD;
    //数据源类型
    public static final String GEO="geo";
    public static final String TONG_DUN="tongDun";
    public static final String PARAMETER_CALC = "parameterCalc";
    public static final String DATA_PLATFORM="dataPlatform";
    public static final String WU="wuxi";
    public static final String WU_XI_WHITE_LIST="wuXiWhiteList";

    //同盾所有字段总接口
    public static final String TONG_DUN_ALLINTER_FIELDS="all_inter_fields";
    /**
     * 系统合法参数集合,包含geo和第三方数据源的接口集合
     *
     */
    private static final Map<String,List<String>> SystemValidInterMap = new HashMap<String,List<String>>();


    /**
     * geo合法接口集合，依赖于接口请求及解析硬编码方法
     * @ENGINE_RAW_INTER： 数据库表中添加的接口是validInterSet的子集
     * @Add:  新添加接口流程
     * 1. 注册接口名==>validInterSet
     * 2. 注册接口解析分类
     * 3. 注册合法字段
     */
    private static final List<String> validInterSet = Arrays.asList("A3", "A4", "B7", "B13",
            "A2", "C6", "C8", "C9", "D1", "D3", "Y1", "Y4", "T20103","T10204",
            "B11", "B18", "B19", "C1", "C7", "Z4", "Z5", "Z7", "Y3", "T20107", "T40303", "T40301", "T40302",
            "A5", "C2", "B21", "B1", "T20105", "T10103","TONG_DUN_ANTIFRAUD");

    /**
     * geo合法接口集合, 对内部接口字段进行分类
     */
    public static final String SINGLEFIELD_1 = "singleField_1";
    public static final String SINGLEFIELD_2 = "singleField_2";
    public static final String MULTIFIELD_1 = "multiField_1";
    public static final String MULTIFIELD_2 = "multiField_2";
    public static final String MULTIFIELD_3 = "multiField_3";
    public static final String MULTIFIELD_4 = "multiField_4";
    public static final String MULTIFIELD_5 = "multiField_5";
    //同盾接口解析方式1
    public static final String MULTILFIELD1_RESULT_TONGDUN = "multilfield1_result_tongdun";
    private static final Map<String,List<String>> interfaceDic;
    private static final Map<String,List<String>> interfaceDicTongDun;
    //同盾风险字段中文映射自定义字段
    private static final Map<String,String> TONG_DUN_FIELD_MAP;

    public static Map<String, String> getTongDunFieldMap() {
        return TONG_DUN_FIELD_MAP;
    }
    static{
        SystemValidInterMap.put(GEO,Arrays.asList("A3", "A4", "B7", "B13",
                "A2", "C6", "C8", "C9", "D1", "D3", "Y1", "Y4", "T20103","T10204",
                "B11", "B18", "B19", "C1", "C7", "Z4", "Z5", "Z7", "Y3", "T20107", "T40303", "T40301", "T40302",
                "A5", "C2", "B21", "B1", "T20105","T10103",
                "T10106"));
        SystemValidInterMap.put(TONG_DUN,Arrays.asList("TONG_DUN_ANTIFRAUD"));
    }
    static {
        interfaceDic = new HashMap<>();
        interfaceDicTongDun=new HashMap<>();
        /**
         * geo单字段基本类型
         * 特殊： "RSL":[{"RS":{"code":"0","desc":"在网"},"IFT":"C6"}]
         */

        interfaceDic.put("singleField_1", Arrays.asList("A3", "A4", "B7", "B13", "A2", "C8", "C9", "D1", "D3",
                "B11", "B18", "B19", "C1", "C7", "Z4", "Z5",
                "A5", "C2", "B21", "B1"
        ));
        /**
         * geo单字段特殊类型
         * 特殊： "RSL":[{"RS":{"code":"-9999","desc":" ZTE-NX511J"},"IFT":"C6"}]
         */
        interfaceDic.put("singleField_2", Arrays.asList("C6"));

        /**
         * geo多维字段数据类型一，例如T20103:
         *  RSL":[{"RS":{"code":"-9999","desc":"{\"error_code\":0,
         *              \"data\":{\"account_no\":\"6228483738174751273\",
         *              \"CSSP001\":\"3\",\"score\":null}}"},"IFT":"Z7"}]
         */
        interfaceDic.put("multiField_1", Arrays.asList("Y1", "Y4", "T20103","T10204", "Y3", "T20107","T10103"));

        /**
         * geo多维字段数据类型二，例Z7:
         *  RSL":[{"RS":{"code":"-9999","desc":"{\"error_code\":0,
         *              \"data\":[{\"account_no\":\"6228483738174751273\",
         *              \"CSSP001\":\"3\",\"score\":null}]}"},"IFT":"Z7"}]
         */
        interfaceDic.put("multiField_2", Arrays.asList("Z7"));


        /**
         * geo多维字段数据类型三
         * T40302: {"code":"200","data":{"ISPNUM":{"province":"山东","city":"枣庄","isp":"电信"},"RSL":[{"RS":{"code":"-9998","desc":"{\"telloc\":\"山东枣庄电信\",\"itag_ids\":[],\"status\":\"0\",\"telnum\":\"13306328903\"}"},"IFT":"T40302"}],"ECL":[]},"msg":"成功"}
         * T40303: {"code":"200","data":{"ISPNUM":{"province":"山东","city":"枣庄","isp":"电信"},"RSL":[{"RS":{"code":"-9998","desc":"{\"realName\":\"赵玉柏\",\"callertag\":{\"tagType\":\"\",\"updateTime\":\"\"},\"black\":{\"updateTime\":\"\"},\"alt\":{\"updateTime\":\"\"},\"idNumber\":\"370404196212262212\",\"phone1\":\"13306328903\"}"},"IFT":"T40303"}],"ECL":[]},"msg":"成功"}
         */
        interfaceDic.put("multiField_3", Arrays.asList("T40303", "T40301", "T40302", "T10106"));

        /**
         * geo多维字段数据类型四: desc为[{},{}] 类型
         * T20105: {"code":"200",
         *          "data":{
         *              "ISPNUM":{"province":"河北","city":"石家庄","isp":"联通"},"RSL":[{"RS":{"code":"-9999","desc":"[{\"yiju\":\"，本院不予支持。依照《中华人民共和国民事诉讼法》第一百七十条第一款第一项、第一百七十一条\",\"pname\":\"赵玉柏\",\"sortTime\":1508083200000,\"dataType\":\"cpws\",\"judgeResult\":\"驳回上诉，维持原裁定。本裁定为终审裁定。\",\"matchRatio\":0.8,\"body\":\"被上诉人（原审原告）:赵玉柏;...\",\"court\":\"山东省枣庄市中级人民法院\",\"title\":\"赵玉祥、赵玉柏赠与合同纠纷二审民事裁定书\",\"caseNo\":\"（2017）鲁04民辖终115号\",\"caseType\":\"民事裁定书\",\"entryId\":\"c20173704minxiazhong115_t20171016\",\"courtRank\":\"3\",\"caseCause\":\"赠与合同纠纷\",\"sortTimeString\":\"2017年10月16日\",\"judge\":\"审判长:廖建新,审判员:张洪光,审判员:杜兆锋,书记员:李　晶\",\"partyId\":\"c20173704minxiazhong115_t20171016_pzhaoyubo_rt_111\",\"trialProcedure\":\"二审\"}]"},"IFT":"T20105"}],"ECL":[]},"msg":"成功"}

         */
        interfaceDic.put("multiField_4", Arrays.asList("T20105"));

        interfaceDic.put("multiField_5",Arrays.asList("INDX103000","INDX104000","INDX204000","INDX303001","INDX304000","INDX403000","INDX509000","INDX103070"));
        /**
         * 同盾字段解析方式,同盾数据源返回策略
         */
        interfaceDicTongDun.put(MULTILFIELD1_RESULT_TONGDUN,Arrays.asList("TONG_DUN_ANTIFRAUD"));

    }

    /**
     * geo合法字段名，接口字段映射关系
     */
    static{
        /* 单字段接口，定义字段名 */
        Hashtable<String,String> inter2Field = new Hashtable<String,String>();
        inter2Field.put("A3", "onlineTime");
        inter2Field.put("A4","onlineStatus");
        inter2Field.put("B7","IdPhoneNameValidate");
        inter2Field.put("B13","outOfServiceTimes");
        inter2Field.put("A2","phoneNameValidate");
        inter2Field.put("C6","phoneModel");
        inter2Field.put("C8","IdPhoneValidate");
        inter2Field.put("C9","phoneGroupValidate");
        inter2Field.put("D1","onlineTimePlus");
        inter2Field.put("D3","consumerGradePlus");
        inter2Field.put("B11","frequentContactsValidate");
        inter2Field.put("B18","workPlaceValidate");
        inter2Field.put("B19","residenceValidate");
        inter2Field.put("C1","currentCityValidate");
        inter2Field.put("C7","CallTimes");
        inter2Field.put("Z4","IdPhoneNameCardValidate");
        inter2Field.put("Z5","IdNameCardValidate");

        /* part3 */
        inter2Field.put("A5","ownPhoneNumber");
        inter2Field.put("C2","phoneDepartStatus");
        inter2Field.put("B21","phoneDataLevel");
        inter2Field.put("B1","consumerGrade");

        INTER2FIELD = Collections.unmodifiableMap(inter2Field);
    }
    //同盾字段中文映射方式解析
    static{
        TONG_DUN_FIELD_MAP=new HashMap<>();

        TONG_DUN_FIELD_MAP.put("身份证归属地位于高风险较为集中地区","risk_areas");
        TONG_DUN_FIELD_MAP.put("身份证命中法院失信名单","id_discredit");
        TONG_DUN_FIELD_MAP.put("身份证命中法院执行名单","id_court_execution");
        TONG_DUN_FIELD_MAP.put("身份证命中信贷逾期名单","id_overdue");
        TONG_DUN_FIELD_MAP.put("身份证命中高风险关注名单","id_risk_attention");
        TONG_DUN_FIELD_MAP.put("身份证命中车辆租赁违约名单","id_vehicle_lease");
        TONG_DUN_FIELD_MAP.put("身份证命中法院结案名单","id_court_cases");
        TONG_DUN_FIELD_MAP.put("身份证_姓名命中信贷逾期模糊名单","id_name_overdue");
        TONG_DUN_FIELD_MAP.put("3个月内身份证关联多个申请信息","id_3m_frequency");
        TONG_DUN_FIELD_MAP.put("3个月内申请信息关联多个身份证","id_3m_frequencycards");
        TONG_DUN_FIELD_MAP.put("3个月内申请人手机号作为联系人手机号出现的次数大于等于2","id_phone_contact3");
        TONG_DUN_FIELD_MAP.put("7天内设备或身份证或手机号申请次数过多","id_phone_many");
        TONG_DUN_FIELD_MAP.put("6个月内申请人在多个平台申请借款","multiple_6m");
        TONG_DUN_FIELD_MAP.put("身份证命中风险群体规则","id_risk_group");
        TONG_DUN_FIELD_MAP.put("信贷名单","credit_list");
        TONG_DUN_FIELD_MAP.put("设备状态异常","equipment_state_anomaly");

    }


    public static final Map<String, List<String>> MULTIINTER2FIELD;


    static{
        /*geo 多字段接口， 定义合法字段名 */
        Hashtable<String, List<String>> multiInter2Field2 = new Hashtable<>();

        multiInter2Field2.put("T20105", Arrays.asList(

                "cpws_sortTime",
                "cpws_body",
                "cpws_caseType",
                "cpws_sortTimeString",
                "cpws_trialProcedure",
                "cpws_court",
                "cpws_entryId",
                "cpws_dataType",
                "cpws_caseCause",
                "cpws_title",
                "cpws_partyId",
                "cpws_judge",
                "cpws_matchRatio",
                "cpws_pname",
                "cpws_caseNo",
                "cpws_judgeResult",
                "cpws_yiju",
                "cpws_courtRank",

                "zxgg_sortTime",
                "zxgg_body",
                "zxgg_sortTimeString",
                "zxgg_court",
                "zxgg_idcardNo",
                "zxgg_entryId",
                "zxgg_title",
                "zxgg_dataType",
                "zxgg_matchRatio",
                "zxgg_pname",
                "zxgg_caseNo",
                "zxgg_caseState",
                "zxgg_execMoney",

                "shixin_sortTime",
                "shixin_body",
                "shixin_sortTimeString",
                "shixin_sex",
                "shixin_lxqk",
                "shixin_yjCode",
                "shixin_court",
                "shixin_idcardNo",
                "shixin_entryId",
                "shixin_yjdw",
                "shixin_dataType",
                "shixin_jtqx",
                "shixin_yiwu",
                "shixin_age",
                "shixin_matchRatio",
                "shixin_province",
                "shixin_pname",
                "shixin_caseNo",
                "shixin_postTime",

                "bgt_sortTime",
                "bgt_body",
                "bgt_sortTimeString",
                "bgt_bgDate",
                "bgt_partyType",
                "bgt_court",
                "bgt_proposer",
                "bgt_idcardNo",
                "bgt_entryId",
                "bgt_dataType",
                "bgt_caseCause",
                "bgt_unnexeMoney",
                "bgt_address",
                "bgt_matchRatio",
                "bgt_pname",
                "bgt_caseNo",
                "bgt_yiju",
                "bgt_execMoney"

        ));

        multiInter2Field2.put("T20103", Arrays.asList(
                "result_YQ_ZZSJ",
                "result_YQ_ZDSC",
                "result_SX_LJCS",
                "result_QZ_LJCS",
                "result_YQ_LJCS",
                "result_SX_ZZSJ",
                "result_QZ_ZZSJ",
                "result_YQ_ZJSJ",
                "result_SX_ZJSJ",
                "result_YQ_DQSC",
                "result_YQ_ZDJE",
                "result_YQ_DQJE" ));
        multiInter2Field2.put("T10204", Arrays.asList(
                "result_YQ_ZZSJ",
                "result_YQ_ZJSJ",
                "result_YQ_LJCS",
                "result_YQ_DQJE",
                "result_YQ_DQSC",
                "result_YQ_ZDJE",
                "result_YQ_ZDSC"
        ));
        multiInter2Field2.put("Y1", Arrays.asList(
                "birthday",
                "gender",
                "originalAdress",   //源数据字段错误
                "identityResult",
                "age"
        ));
        multiInter2Field2.put("T10103", Arrays.asList(
                "illegal_level",
                "illegal_count",
                "illegal_time",
                "illegal_source",
                "illegal_type",
                "illegal_caseType"
        ));

        multiInter2Field2.put("Y4", Arrays.asList(
                "lasttime",
                "identify_result"
        ));

        multiInter2Field2.put("Z7", Arrays.asList(
                "account_no",
                "score",
                "CSSP001"
        ));

        multiInter2Field2.put("Y3", Arrays.asList(
                "firstTime",
                "lastTime",
                "score",
                "identify_result"
        ));

        multiInter2Field2.put("T20107", Arrays.asList(
                "isMachdSEO",
                "isMachdProxy",
                "rskScore",
                "isMachdDNS",
                "iUpdateDate",
                "isMachdMailServ",
                "isMachdForce",
                "isMachdBlacklist",
                "isMachdWebServ",
                "isMachdOrg",
                "isMachdCrawler",
                "isMachdVPN"
        ));

        multiInter2Field2.put("T40301", Arrays.asList(
                "TJXX_30d_regtimes",
                "TJXX_30d_regplatforms",
                "TJXX_30d_reglasttime",
                "TJXX_30d_regfirsttime",
                "TJXX_30d_apptimes",
                "TJXX_30d_appplatforms",
                "TJXX_30d_appmoney",
                "TJXX_30d_applasttime",
                "TJXX_30d_appfirsttime",
                "TJXX_30d_loantimes",
                "TJXX_30d_loanplatforms",
                "TJXX_30d_loanmoney",
                "TJXX_30d_loanfirsttime",
                "TJXX_30d_loanlasttime",
                "TJXX_30d_rejtimes",
                "TJXX_30d_rejplatforms",
                "TJXX_30d_rejfirsttime",
                "TJXX_30d_rejlasttime",
                "TJXX_30d_regtimes_bank",
                "TJXX_30d_regplatforms_bank",
                "TJXX_30d_reglasttime_bank",
                "TJXX_30d_regfirsttime_bank",
                "TJXX_30d_apptimes_bank",
                "TJXX_30d_appplatforms_bank",
                "TJXX_30d_appmoney_bank",
                "TJXX_30d_applasttime_bank",
                "TJXX_30d_appfirsttime_bank",
                "TJXX_30d_loantimes_bank",
                "TJXX_30d_loanplatforms_bank",
                "TJXX_30d_loanmoney_bank",
                "TJXX_30d_loanfirsttime_bank",
                "TJXX_30d_loanlasttime_bank",
                "TJXX_30d_rejtimes_bank",
                "TJXX_30d_rejplatforms_bank",
                "TJXX_30d_rejfirsttime_bank",
                "TJXX_30d_rejlasttime_bank",
                "TJXX_30d_regtimes_nonbank",
                "TJXX_30d_regplatforms_nonbank",
                "TJXX_30d_reglasttime_nonbank",
                "TJXX_30d_regfirsttime_nonbank",
                "TJXX_30d_apptimes_nonbank",
                "TJXX_30d_appplatforms_nonbank",
                "TJXX_30d_appmoney_nonbank",
                "TJXX_30d_applasttime_nonbank",
                "TJXX_30d_appfirsttime_nonbank",
                "TJXX_30d_loantimes_nonbank",
                "TJXX_30d_loanplatforms_nonbank",
                "TJXX_30d_loanmoney_nonbank",
                "TJXX_30d_loanfirsttime_nonbank",
                "TJXX_30d_loanlasttime_nonbank",
                "TJXX_30d_rejtimes_nonbank",
                "TJXX_30d_rejplatforms_nonbank",
                "TJXX_30d_rejfirsttime_nonbank",
                "TJXX_30d_rejlasttime_nonbank",
                "TJXX_7d_regtimes",
                "TJXX_7d_regplatforms",
                "TJXX_7d_reglasttime",
                "TJXX_7d_regfirsttime",
                "TJXX_7d_apptimes",
                "TJXX_7d_appplatforms",
                "TJXX_7d_appmoney",
                "TJXX_7d_applasttime",
                "TJXX_7d_appfirsttime",
                "TJXX_7d_loantimes",
                "TJXX_7d_loanplatforms",
                "TJXX_7d_loanmoney",
                "TJXX_7d_loanfirsttime",
                "TJXX_7d_loanlasttime",
                "TJXX_7d_rejtimes",
                "TJXX_7d_rejplatforms",
                "TJXX_7d_rejfirsttime",
                "TJXX_7d_rejlasttime",
                "TJXX_7d_regtimes_bank",
                "TJXX_7d_regplatforms_bank",
                "TJXX_7d_reglasttime_bank",
                "TJXX_7d_regfirsttime_bank",
                "TJXX_7d_apptimes_bank",
                "TJXX_7d_appplatforms_bank",
                "TJXX_7d_appmoney_bank",
                "TJXX_7d_applasttime_bank",
                "TJXX_7d_appfirsttime_bank",
                "TJXX_7d_loantimes_bank",
                "TJXX_7d_loanplatforms_bank",
                "TJXX_7d_loanmoney_bank",
                "TJXX_7d_loanfirsttime_bank",
                "TJXX_7d_loanlasttime_bank",
                "TJXX_7d_rejtimes_bank",
                "TJXX_7d_rejplatforms_bank",
                "TJXX_7d_rejfirsttime_bank",
                "TJXX_7d_rejlasttime_bank",
                "TJXX_7d_regtimes_nonbank",
                "TJXX_7d_regplatforms_nonbank",
                "TJXX_7d_reglasttime_nonbank",
                "TJXX_7d_regfirsttime_nonbank",
                "TJXX_7d_apptimes_nonbank",
                "TJXX_7d_appplatforms_nonbank",
                "TJXX_7d_appmoney_nonbank",
                "TJXX_7d_applasttime_nonbank",
                "TJXX_7d_appfirsttime_nonbank",
                "TJXX_7d_loantimes_nonbank",
                "TJXX_7d_loanplatforms_nonbank",
                "TJXX_7d_loanmoney_nonbank",
                "TJXX_7d_loanfirsttime_nonbank",
                "TJXX_7d_loanlasttime_nonbank",
                "TJXX_7d_rejtimes_nonbank",
                "TJXX_7d_rejplatforms_nonbank",
                "TJXX_7d_rejfirsttime_nonbank",
                "TJXX_7d_rejlasttime_nonbank",
                "TJXX_180d_regtimes",
                "TJXX_180d_regplatforms",
                "TJXX_180d_reglasttime",
                "TJXX_180d_regfirsttime",
                "TJXX_180d_apptimes",
                "TJXX_180d_appplatforms",
                "TJXX_180d_appmoney",
                "TJXX_180d_applasttime",
                "TJXX_180d_appfirsttime",
                "TJXX_180d_loantimes",
                "TJXX_180d_loanplatforms",
                "TJXX_180d_loanmoney",
                "TJXX_180d_loanfirsttime",
                "TJXX_180d_loanlasttime",
                "TJXX_180d_rejtimes",
                "TJXX_180d_rejplatforms",
                "TJXX_180d_rejfirsttime",
                "TJXX_180d_rejlasttime",
                "TJXX_180d_regtimes_bank",
                "TJXX_180d_regplatforms_bank",
                "TJXX_180d_reglasttime_bank",
                "TJXX_180d_regfirsttime_bank",
                "TJXX_180d_apptimes_bank",
                "TJXX_180d_appplatforms_bank",
                "TJXX_180d_appmoney_bank",
                "TJXX_180d_applasttime_bank",
                "TJXX_180d_appfirsttime_bank",
                "TJXX_180d_loantimes_bank",
                "TJXX_180d_loanplatforms_bank",
                "TJXX_180d_loanmoney_bank",
                "TJXX_180d_loanfirsttime_bank",
                "TJXX_180d_loanlasttime_bank",
                "TJXX_180d_rejtimes_bank",
                "TJXX_180d_rejplatforms_bank",
                "TJXX_180d_rejfirsttime_bank",
                "TJXX_180d_rejlasttime_bank",
                "TJXX_180d_regtimes_nonbank",
                "TJXX_180d_regplatforms_nonbank",
                "TJXX_180d_reglasttime_nonbank",
                "TJXX_180d_regfirsttime_nonbank",
                "TJXX_180d_apptimes_nonbank",
                "TJXX_180d_appplatforms_nonbank",
                "TJXX_180d_appmoney_nonbank",
                "TJXX_180d_applasttime_nonbank",
                "TJXX_180d_appfirsttime_nonbank",
                "TJXX_180d_loantimes_nonbank",
                "TJXX_180d_loanplatforms_nonbank",
                "TJXX_180d_loanmoney_nonbank",
                "TJXX_180d_loanfirsttime_nonbank",
                "TJXX_180d_loanlasttime_nonbank",
                "TJXX_180d_rejtimes_nonbank",
                "TJXX_180d_rejplatforms_nonbank",
                "TJXX_180d_rejfirsttime_nonbank",
                "TJXX_180d_rejlasttime_nonbank",
                "TJXX_3d_regtimes",
                "TJXX_3d_regplatforms",
                "TJXX_3d_reglasttime",
                "TJXX_3d_regfirsttime",
                "TJXX_3d_apptimes",
                "TJXX_3d_appplatforms",
                "TJXX_3d_appmoney",
                "TJXX_3d_applasttime",
                "TJXX_3d_appfirsttime",
                "TJXX_3d_loantimes",
                "TJXX_3d_loanplatforms",
                "TJXX_3d_loanmoney",
                "TJXX_3d_loanfirsttime",
                "TJXX_3d_loanlasttime",
                "TJXX_3d_rejtimes",
                "TJXX_3d_rejplatforms",
                "TJXX_3d_rejfirsttime",
                "TJXX_3d_rejlasttime",
                "TJXX_3d_regtimes_bank",
                "TJXX_3d_regplatforms_bank",
                "TJXX_3d_reglasttime_bank",
                "TJXX_3d_regfirsttime_bank",
                "TJXX_3d_apptimes_bank",
                "TJXX_3d_appplatforms_bank",
                "TJXX_3d_appmoney_bank",
                "TJXX_3d_applasttime_bank",
                "TJXX_3d_appfirsttime_bank",
                "TJXX_3d_loantimes_bank",
                "TJXX_3d_loanplatforms_bank",
                "TJXX_3d_loanmoney_bank",
                "TJXX_3d_loanfirsttime_bank",
                "TJXX_3d_loanlasttime_bank",
                "TJXX_3d_rejtimes_bank",
                "TJXX_3d_rejplatforms_bank",
                "TJXX_3d_rejfirsttime_bank",
                "TJXX_3d_rejlasttime_bank",
                "TJXX_3d_regtimes_nonbank",
                "TJXX_3d_regplatforms_nonbank",
                "TJXX_3d_reglasttime_nonbank",
                "TJXX_3d_regfirsttime_nonbank",
                "TJXX_3d_apptimes_nonbank",
                "TJXX_3d_appplatforms_nonbank",
                "TJXX_3d_appmoney_nonbank",
                "TJXX_3d_applasttime_nonbank",
                "TJXX_3d_appfirsttime_nonbank",
                "TJXX_3d_loantimes_nonbank",
                "TJXX_3d_loanplatforms_nonbank",
                "TJXX_3d_loanmoney_nonbank",
                "TJXX_3d_loanfirsttime_nonbank",
                "TJXX_3d_loanlasttime_nonbank",
                "TJXX_3d_rejtimes_nonbank",
                "TJXX_3d_rejplatforms_nonbank",
                "TJXX_3d_rejfirsttime_nonbank",
                "TJXX_3d_rejlasttime_nonbank",
                "TJXX_regtimes",
                "TJXX_regplatforms",
                "TJXX_reglasttime",
                "TJXX_regfirsttime",
                "TJXX_apptimes",
                "TJXX_appplatforms",
                "TJXX_appmoney",
                "TJXX_applasttime",
                "TJXX_appfirsttime",
                "TJXX_loantimes",
                "TJXX_loanplatforms",
                "TJXX_loanmoney",
                "TJXX_loanfirsttime",
                "TJXX_loanlasttime",
                "TJXX_rejtimes",
                "TJXX_rejplatforms",
                "TJXX_rejfirsttime",
                "TJXX_rejlasttime",
                "TJXX_regtimes_bank",
                "TJXX_regplatforms_bank",
                "TJXX_reglasttime_bank",
                "TJXX_regfirsttime_bank",
                "TJXX_apptimes_bank",
                "TJXX_appplatforms_bank",
                "TJXX_appmoney_bank",
                "TJXX_applasttime_bank",
                "TJXX_appfirsttime_bank",
                "TJXX_loantimes_bank",
                "TJXX_loanplatforms_bank",
                "TJXX_loanmoney_bank",
                "TJXX_loanfirsttime_bank",
                "TJXX_loanlasttime_bank",
                "TJXX_rejtimes_bank",
                "TJXX_rejplatforms_bank",
                "TJXX_rejfirsttime_bank",
                "TJXX_rejlasttime_bank",
                "TJXX_regtimes_nonbank",
                "TJXX_regplatforms_nonbank",
                "TJXX_reglasttime_nonbank",
                "TJXX_regfirsttime_nonbank",
                "TJXX_apptimes_nonbank",
                "TJXX_appplatforms_nonbank",
                "TJXX_appmoney_nonbank",
                "TJXX_applasttime_nonbank",
                "TJXX_appfirsttime_nonbank",
                "TJXX_loantimes_nonbank",
                "TJXX_loanplatforms_nonbank",
                "TJXX_loanmoney_nonbank",
                "TJXX_loanfirsttime_nonbank",
                "TJXX_loanlasttime_nonbank",
                "TJXX_rejtimes_nonbank",
                "TJXX_rejplatforms_nonbank",
                "TJXX_rejfirsttime_nonbank",
                "TJXX_rejlasttime_nonbank",
                "TJXX_90d_regtimes",
                "TJXX_90d_regplatforms",
                "TJXX_90d_reglasttime",
                "TJXX_90d_regfirsttime",
                "TJXX_90d_apptimes",
                "TJXX_90d_appplatforms",
                "TJXX_90d_appmoney",
                "TJXX_90d_applasttime",
                "TJXX_90d_appfirsttime",
                "TJXX_90d_loantimes",
                "TJXX_90d_loanplatforms",
                "TJXX_90d_loanmoney",
                "TJXX_90d_loanfirsttime",
                "TJXX_90d_loanlasttime",
                "TJXX_90d_rejtimes",
                "TJXX_90d_rejplatforms",
                "TJXX_90d_rejfirsttime",
                "TJXX_90d_rejlasttime",
                "TJXX_90d_regtimes_bank",
                "TJXX_90d_regplatforms_bank",
                "TJXX_90d_reglasttime_bank",
                "TJXX_90d_regfirsttime_bank",
                "TJXX_90d_apptimes_bank",
                "TJXX_90d_appplatforms_bank",
                "TJXX_90d_appmoney_bank",
                "TJXX_90d_applasttime_bank",
                "TJXX_90d_appfirsttime_bank",
                "TJXX_90d_loantimes_bank",
                "TJXX_90d_loanplatforms_bank",
                "TJXX_90d_loanmoney_bank",
                "TJXX_90d_loanfirsttime_bank",
                "TJXX_90d_loanlasttime_bank",
                "TJXX_90d_rejtimes_bank",
                "TJXX_90d_rejplatforms_bank",
                "TJXX_90d_rejfirsttime_bank",
                "TJXX_90d_rejlasttime_bank",
                "TJXX_90d_regtimes_nonbank",
                "TJXX_90d_regplatforms_nonbank",
                "TJXX_90d_reglasttime_nonbank",
                "TJXX_90d_regfirsttime_nonbank",
                "TJXX_90d_apptimes_nonbank",
                "TJXX_90d_appplatforms_nonbank",
                "TJXX_90d_appmoney_nonbank",
                "TJXX_90d_applasttime_nonbank",
                "TJXX_90d_appfirsttime_nonbank",
                "TJXX_90d_loantimes_nonbank",
                "TJXX_90d_loanplatforms_nonbank",
                "TJXX_90d_loanmoney_nonbank",
                "TJXX_90d_loanfirsttime_nonbank",
                "TJXX_90d_loanlasttime_nonbank",
                "TJXX_90d_rejtimes_nonbank",
                "TJXX_90d_rejplatforms_nonbank",
                "TJXX_90d_rejfirsttime_nonbank",
                "TJXX_90d_rejlasttime_nonbank",
                "TJXX_60d_regtimes",
                "TJXX_60d_regplatforms",
                "TJXX_60d_reglasttime",
                "TJXX_60d_regfirsttime",
                "TJXX_60d_apptimes",
                "TJXX_60d_appplatforms",
                "TJXX_60d_appmoney",
                "TJXX_60d_applasttime",
                "TJXX_60d_appfirsttime",
                "TJXX_60d_loantimes",
                "TJXX_60d_loanplatforms",
                "TJXX_60d_loanmoney",
                "TJXX_60d_loanfirsttime",
                "TJXX_60d_loanlasttime",
                "TJXX_60d_rejtimes",
                "TJXX_60d_rejplatforms",
                "TJXX_60d_rejfirsttime",
                "TJXX_60d_rejlasttime",
                "TJXX_60d_regtimes_bank",
                "TJXX_60d_regplatforms_bank",
                "TJXX_60d_reglasttime_bank",
                "TJXX_60d_regfirsttime_bank",
                "TJXX_60d_apptimes_bank",
                "TJXX_60d_appplatforms_bank",
                "TJXX_60d_appmoney_bank",
                "TJXX_60d_applasttime_bank",
                "TJXX_60d_appfirsttime_bank",
                "TJXX_60d_loantimes_bank",
                "TJXX_60d_loanplatforms_bank",
                "TJXX_60d_loanmoney_bank",
                "TJXX_60d_loanfirsttime_bank",
                "TJXX_60d_loanlasttime_bank",
                "TJXX_60d_rejtimes_bank",
                "TJXX_60d_rejplatforms_bank",
                "TJXX_60d_rejfirsttime_bank",
                "TJXX_60d_rejlasttime_bank",
                "TJXX_60d_regtimes_nonbank",
                "TJXX_60d_regplatforms_nonbank",
                "TJXX_60d_reglasttime_nonbank",
                "TJXX_60d_regfirsttime_nonbank",
                "TJXX_60d_apptimes_nonbank",
                "TJXX_60d_appplatforms_nonbank",
                "TJXX_60d_appmoney_nonbank",
                "TJXX_60d_applasttime_nonbank",
                "TJXX_60d_appfirsttime_nonbank",
                "TJXX_60d_loantimes_nonbank",
                "TJXX_60d_loanplatforms_nonbank",
                "TJXX_60d_loanmoney_nonbank",
                "TJXX_60d_loanfirsttime_nonbank",
                "TJXX_60d_loanlasttime_nonbank",
                "TJXX_60d_rejtimes_nonbank",
                "TJXX_60d_rejplatforms_nonbank",
                "TJXX_60d_rejfirsttime_nonbank",
                "TJXX_60d_rejlasttime_nonbank"
        ));

        multiInter2Field2.put("T10106", Arrays.asList(

                "TJXX_overdueplatform",
                "TJXX_overduetimes",
                "TJXX_overduemax"
        ));

        multiInter2Field2.put("T40302", Arrays.asList(
                "telloc",
                "teldesc",
                "itag_ids",
                "name",
                "catnames",
                "status",
                "telnum",
                "flag_num",
                "flag_fid",
                "flag_type",
                "catnames"

        ));

        multiInter2Field2.put("T40303", Arrays.asList(
                "realName",
                "callertag_tagType",
                "callertag_updateTime",
                "black_updateTime",
                "alt_updateTime",
                "idNumber",
                "phone1"
        ));
        multiInter2Field2.put("T40303", Arrays.asList(
                "realName",
                "callertag_tagType",
                "callertag_updateTime",
                "black_updateTime",
                "alt_updateTime",
                "idNumber",
                "phone1"
        ));
        multiInter2Field2.put("INDX103000",Arrays.asList("INDX103000_GEO_SCORE","INDX103000_CDXX001","INDX103000_CDDT000","INDX103000_CDDT001","INDX103000_CDDT002","INDX103000_CDDT003","INDX103000_CDDT004","INDX103000_CDDT005","INDX103000_CDDT006","INDX103000_CDDT007","INDX103000_CDDT008","INDX103000_CDDT009","INDX103000_CDDT010","INDX103000_CDDT011","INDX103000_CDDT012","INDX103000_CDDT013","INDX103000_CDDT014","INDX103000_CDDT015","INDX103000_CDDT016","INDX103000_CDDT017","INDX103000_CDDT018","INDX103000_CDDT019","INDX103000_CDDT020","INDX103000_CDDT021","INDX103000_CDDT022","INDX103000_CDDT023","INDX103000_CDDT024","INDX103000_CDDT025","INDX103000_CDDT026","INDX103000_CDDT027","INDX103000_CDDT028","INDX103000_CDDT029","INDX103000_CDDT030","INDX103000_CDDT031","INDX103000_CDDT032","INDX103000_CDDT033","INDX103000_CDDT034","INDX103000_CDDT035","INDX103000_CDDT036","INDX103000_CDDT037","INDX103000_CDDT038","INDX103000_CDDT039","INDX103000_CDDT040","INDX103000_CDDT041","INDX103000_CDDT042","INDX103000_CDDT043","INDX103000_CDDT044","INDX103000_CDDT045","INDX103000_CDDT046","INDX103000_CDDT047","INDX103000_CDDT048","INDX103000_CDDT049","INDX103000_CDDT050","INDX103000_CDDT051","INDX103000_CDDT052","INDX103000_CDDT053","INDX103000_CDDT054","INDX103000_CDDT055","INDX103000_CDDT056","INDX103000_CDDT057","INDX103000_CDDT058","INDX103000_CDDT059","INDX103000_CDDT060","INDX103000_CDDT061","INDX103000_CDDT062","INDX103000_CDDT063","INDX103000_CDDT064","INDX103000_CDDT065","INDX103000_CDDT066","INDX103000_CDDT067","INDX103000_CDDT068","INDX103000_CDDT069","INDX103000_CDDT070","INDX103000_CDZC017","INDX103000_CDZC002","INDX103000_CDZC003","INDX103000_CDZC004","INDX103000_CDZC005","INDX103000_CDZC006","INDX103000_CDZC007","INDX103000_CDZC008","INDX103000_CDZC009","INDX103000_CDZC010"));
        multiInter2Field2.put("INDX104000",Arrays.asList("INDX104000_GEO_SCORE","INDX104000_CDZC017","INDX104000_CDZC002","INDX104000_CDZC003","INDX104000_CDZC004","INDX104000_CDZC005","INDX104000_CDZC006","INDX104000_CDZC007","INDX104000_CDZC008","INDX104000_CDZC009","INDX104000_CDZC010"));
        multiInter2Field2.put("INDX204000",Arrays.asList("INDX204000_GEO_SCORE","INDX204000_CDZC013","INDX204000_CDZC003","INDX204000_CDZC018","INDX204000_CDZC004","INDX204000_CDZC017","INDX204000_CDZC019","INDX204000_CDZC020","INDX204000_CDZC021","INDX204000_CDZC022","INDX204000_CDZC023"));
        multiInter2Field2.put("INDX303001",Arrays.asList("INDX303001_GEO_SCORE","INDX303001_CDZC013","INDX303001_CDZC003","INDX303001_CDZC018","INDX303001_CDZC004","INDX303001_CDZC017","INDX303001_CDZC019","INDX303001_CDZC020","INDX303001_CDZC021","INDX303001_CDZC022","INDX303001_CDZC023"));
        multiInter2Field2.put("INDX304000",Arrays.asList("INDX304000_GEO_SCORE","INDX304000_CDZC017","INDX304000_CDZC011","INDX304000_CDZC003","INDX304000_CDZC007","INDX304000_CDZC012","INDX304000_CDZC013","INDX304000_CDZC014","INDX304000_CDZC015","INDX304000_CDZC016","INDX304000_CDZC005"));
        multiInter2Field2.put("INDX403000",Arrays.asList("INDX403000_GEO_SCORE","INDX403000_CDDT001","INDX403000_CDDT002","INDX403000_CDDT003","INDX403000_CDDT004","INDX403000_CDDT005","INDX403000_CDDT006","INDX403000_CDDT007","INDX403000_CDDT008","INDX403000_CDDT009","INDX403000_CDDT010","INDX403000_CDDT011","INDX403000_CDDT012","INDX403000_CDDT013","INDX403000_CDDT014","INDX403000_CDDT015","INDX403000_CDDT016","INDX403000_CDDT017","INDX403000_CDDT018","INDX403000_CDDT019","INDX403000_CDDT020","INDX403000_CDDT021","INDX403000_CDDT022","INDX403000_CDDT023","INDX403000_CDDT024","INDX403000_CDDT025","INDX403000_CDDT026","INDX403000_CDDT027","INDX403000_CDDT028","INDX403000_CDDT029","INDX403000_CDDT030","INDX403000_CDDT031","INDX403000_CDDT032","INDX403000_CDDT033","INDX403000_CDDT034","INDX403000_CDDT035","INDX403000_CDDT036","INDX403000_CDDT037","INDX403000_CDDT038","INDX403000_CDDT039","INDX403000_CDDT040","INDX403000_CDDT041","INDX403000_CDDT042","INDX403000_CDDT043","INDX403000_CDDT044","INDX403000_CDDT045","INDX403000_CDDT046","INDX403000_CDDT047","INDX403000_CDDT048","INDX403000_CDDT049","INDX403000_CDDT050","INDX403000_CDDT051","INDX403000_CDDT052","INDX403000_CDDT053","INDX403000_CDDT054","INDX403000_CDDT055","INDX403000_CDDT056","INDX403000_CDDT057","INDX403000_CDDT058","INDX403000_CDDT059","INDX403000_CDDT060","INDX403000_CDDT061","INDX403000_CDDT062","INDX403000_CDDT063","INDX403000_CDDT064","INDX403000_CDDT065","INDX403000_CDDT066","INDX403000_CDDT067","INDX403000_CDDT068","INDX403000_CDDT069","INDX403000_CDDT070","INDX403000_CDZC029","INDX403000_CDZC024","INDX403000_CDZC023","INDX403000_CDZC022","INDX403000_CDZC010","INDX403000_CDZC017","INDX403000_CDZC004","INDX403000_CDZC009","INDX403000_CDZC003","INDX403000_CDZC025"));
        multiInter2Field2.put("INDX509000",Arrays.asList("INDX509000_GEO_SCORE","INDX509000_CDDT001","INDX509000_CDDT002","INDX509000_CDDT003","INDX509000_CDDT004","INDX509000_CDDT005","INDX509000_CDDT006","INDX509000_CDDT007","INDX509000_CDDT008","INDX509000_CDDT009","INDX509000_CDDT010","INDX509000_CDDT011","INDX509000_CDDT012","INDX509000_CDDT013","INDX509000_CDDT014","INDX509000_CDDT015","INDX509000_CDDT016","INDX509000_CDDT017","INDX509000_CDDT018","INDX509000_CDDT019","INDX509000_CDDT020","INDX509000_CDDT021","INDX509000_CDDT022","INDX509000_CDDT023","INDX509000_CDDT024","INDX509000_CDDT025","INDX509000_CDDT026","INDX509000_CDDT027","INDX509000_CDDT028","INDX509000_CDDT029","INDX509000_CDDT030","INDX509000_CDDT031","INDX509000_CDDT032","INDX509000_CDDT033","INDX509000_CDDT034","INDX509000_CDDT035","INDX509000_CDDT036","INDX509000_CDDT037","INDX509000_CDDT038","INDX509000_CDDT039","INDX509000_CDDT040","INDX509000_CDDT041","INDX509000_CDDT042","INDX509000_CDDT043","INDX509000_CDDT044","INDX509000_CDDT045","INDX509000_CDDT046","INDX509000_CDDT047","INDX509000_CDDT048","INDX509000_CDDT049","INDX509000_CDDT050","INDX509000_CDDT051","INDX509000_CDDT052","INDX509000_CDDT053","INDX509000_CDDT054","INDX509000_CDDT055","INDX509000_CDDT056","INDX509000_CDDT057","INDX509000_CDDT058","INDX509000_CDDT059","INDX509000_CDDT060","INDX509000_CDDT061","INDX509000_CDDT062","INDX509000_CDDT063","INDX509000_CDDT064","INDX509000_CDDT065","INDX509000_CDDT066","INDX509000_CDDT067","INDX509000_CDDT068","INDX509000_CDDT069","INDX509000_CDDT070","INDX509000_CDZC023","INDX509000_CDZC021","INDX509000_CDZC020","INDX509000_CDZC011","INDX509000_CDZC017","INDX509000_CDZC004","INDX509000_CDZC003","INDX509000_CDZC007","INDX509000_CDZC013","INDX509000_CDZC027"));
        multiInter2Field2.put("INDX103070",Arrays.asList("INDX103070_GEO_SCORE","INDX103070_CDDT001","INDX103070_CDDT002","INDX103070_CDDT003","INDX103070_CDDT004","INDX103070_CDDT005","INDX103070_CDDT006","INDX103070_CDDT007","INDX103070_CDDT008","INDX103070_CDDT009","INDX103070_CDDT010","INDX103070_CDDT011","INDX103070_CDDT012","INDX103070_CDDT013","INDX103070_CDDT014","INDX103070_CDDT015","INDX103070_CDDT016","INDX103070_CDDT017","INDX103070_CDDT018","INDX103070_CDDT019","INDX103070_CDDT020","INDX103070_CDDT021","INDX103070_CDDT022","INDX103070_CDDT023","INDX103070_CDDT024","INDX103070_CDDT025","INDX103070_CDDT026","INDX103070_CDDT027","INDX103070_CDDT028","INDX103070_CDDT029","INDX103070_CDDT030","INDX103070_CDDT031","INDX103070_CDDT032","INDX103070_CDDT033","INDX103070_CDDT034","INDX103070_CDDT035","INDX103070_CDDT036","INDX103070_CDDT037","INDX103070_CDDT038","INDX103070_CDDT039","INDX103070_CDDT040","INDX103070_CDDT041","INDX103070_CDDT042","INDX103070_CDDT043","INDX103070_CDDT044","INDX103070_CDDT045","INDX103070_CDDT046","INDX103070_CDDT047","INDX103070_CDDT048","INDX103070_CDDT049","INDX103070_CDDT050","INDX103070_CDDT051","INDX103070_CDDT052","INDX103070_CDDT053","INDX103070_CDDT054","INDX103070_CDDT055","INDX103070_CDDT056","INDX103070_CDDT057","INDX103070_CDDT058","INDX103070_CDDT059","INDX103070_CDDT060","INDX103070_CDDT061","INDX103070_CDDT062","INDX103070_CDDT063","INDX103070_CDDT064","INDX103070_CDDT065","INDX103070_CDDT066","INDX103070_CDDT067","INDX103070_CDDT068","INDX103070_CDDT069","INDX103070_CDDT070","INDX103070_CDXX001","INDX103070_CDDT000","INDX103070_CDZC024","INDX103070_CDZC023","INDX103070_CDZC011","INDX103070_CDZC017","INDX103070_CDZC007","INDX103070_CDZC012","INDX103070_CDZC013","INDX103070_CDZC025","INDX103070_CDZC026","INDX103070_CDZC027"));

        //同盾接口合法字段
        multiInter2Field2.put("TONG_DUN_ANTIFRAUD", Arrays.asList(


                "antifraud_equipment_state_anomaly_score",
                "antifraud_risk_areas_score",
                "antifraud_id_vehicle_lease_score",
                "antifraud_final_score",
                "antifraud_output_fields_antifraud_model_score",
                "antifraud_output_fields_antifraud_decision",
                "antifraud_id_discredit_score",
                "antifraud_credit_list_score",
                "antifraud_id_3m_frequencycards_score",
                "antifraud_id_3m_frequencycards_decision",
                "antifraud_id_overdue_decision",
                "antifraud_risk_areas_decision",
                "antifraud_id_name_overdue_score",
                "antifraud_final_decision",
                "antifraud_equipment_state_anomaly_decision",
                "antifraud_id_3m_frequency_score",
                "antifraud_id_phone_many_score",
                "antifraud_id_phone_many_decision",
                "antifraud_id_risk_attention_decision",
                "antifraud_id_phone_contact3_decision",
                "antifraud_id_risk_group_score",
                "antifraud_multiple_6m_decision",
                "antifraud_id_discredit_decision",
                "antifraud_id_vehicle_lease_decision",
                "antifraud_credit_list_decision",
                "antifraud_id_court_cases_decision",
                "antifraud_id_court_execution_decision",
                "antifraud_id_court_cases_score",
                "antifraud_id_risk_attention_score",
                "antifraud_id_court_execution_score",
                "antifraud_id_overdue_score",
                "antifraud_id_name_overdue_decision",
                "antifraud_multiple_6m_score",
                "antifraud_id_phone_contact3_score",
                "antifraud_id_3m_frequency_decision",
                "antifraud_id_risk_group_decision",
                "antifraud_multiple_6m_byphone",
                "antifraud_multiple_6m_bycard"

        ));

        multiInter2Field2.put(TONG_DUN_ALLINTER_FIELDS,multiInter2Field2.get("TONG_DUN_ANTIFRAUD"));
        MULTIINTER2FIELD = Collections.unmodifiableMap(multiInter2Field2);
    }
    /**
     * 根据接口名获取在哪个数据源,如果返回null说明是非法接口
     * @param interName
     * @return
     */
    public static String getDataSourceType(Map<String,List<String>> SystemValidInterMap,String interName){
        for(String key:SystemValidInterMap.keySet()){
            List<String> interList = SystemValidInterMap.get(key);
            for (String inter:interList) {
                if (interName.equals(inter)){
                    return key;
                }
            }
        }
        return null;
    }

    public static Map<String,List<String>> getInterfaceDic() {



        return interfaceDic;
    }
    public static Map<String,List<String>> getInterfaceDicTongDun() {
        return interfaceDicTongDun;
    }
    public static List<String> getValidInterSet() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(validInterSet);

        for (String key:SystemValidInterMap.keySet()) {
            list.addAll(SystemValidInterMap.get(key));
        }
        return list;
    }

    /**
     * geo接口返回结果校验
     */
    public static String responseValidate(String responseData) {

        if (responseData != null && responseData.length() != 0) {
            return responseData;
        } else {
            System.out.println("数据数据源访问错误："+ StatusCode.RES_ERROR.getCode()+ StatusCode.RES_ERROR.getMessage());
            throw new RcsException( StatusCode.RES_ERROR.getMessage(), StatusCode.RES_ERROR.getCode());
        }
    }

    /**
     * geo字符串返回结果校验：接口名等
     */
    public static String interNameValidate(String interName) {

        if (interName != null && interName.length() != 0) {
            return interName.toUpperCase().replaceAll("\\r\\n|\\r|\\n", "");
        } else {
            System.out.println("接口名称错误："+ StatusCode.TYPE_ERROR.getCode()+ StatusCode.TYPE_ERROR.getMessage());
            throw new RcsException( StatusCode.TYPE_ERROR.getMessage(), StatusCode.TYPE_ERROR.getCode());
        }
    }




}
