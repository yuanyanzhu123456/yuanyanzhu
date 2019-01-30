package com.geo.rcs.common;


/**
 * @author guoyujie
 * @date 2017-12-15
 */
public enum StatusCode {

	/**
	 * Token Validate
	 */
	TOKEN_INVALID(4010, "用户登录超时，请重新登录"),

	/**
	 * Session Validate
	 */
	EXPIRED(4010, "用户登录超时，请重新登录"),
	DENIED(4030, "权限不足"),
	DATA_SOURCE_DENIED(4031, "数据源未配置或配置错误，请配置数据源账户信息"),

	/**
	 * Parameters Validate
	 */
	PARAMS_ERROR(4001, "参数错误"),
	TYPE_ERROR(4002, "类型错误"),

	/**
	 * geo数据源异常
	 */
	GEO_ENCRYPT_ERROR(6001,"加密失败, 集奥验真账户配置有误"),
	GEO_DECRYPT_ERROR(6002,"解密失败, 集奥验真账户配置有误"),
	GEO_UNKNOWNHOST_ERROR(6003,"访问失败, 集奥验真账户域名配置有误或连接超时"),

	/**
	 * Net Validate
	 */
	NET_ERROR(4002, "数据源网络异常"),
	RES_ERROR(4003, "数据源接口返回异常"),
	FIELD_ERROR(4004, "字段数据异常"),


	/**
	 * RuleEngine Validate
	 */

	RULE_ERROR(4023,"规则运行异常"),
	RULE_INIT_ERROR(4024,"规则初始化失败，条件或字段配置错误"),
	RULE_BUILD_ERROR(4025,"规则初始化失败，规则存在语法错误"),
	RULE_EXCUTE_ERROR(4026,"规则未正常执行"),
	RULE_OPERATE_ERROR(4027,"规则初始化失败，规则，条件或字段运算关系错误"),
	RULE_NOTEXIST_ERROR(4028,"规则初始化失败，规则集、规则、条件或字段缺失"),
	RULE_GENERATE_ERROR(4029,"规则文件生成错误"),
	RULE_APPR_ERROR(4030, "规则集配置未完成，规则、条件或字段缺失"),
	RULE_CON_ERROR(4031, "规则集条件关联关系或字段关联关系缺失"),


	/**
	 * DataPool Usage
	 */

	DATA_POOL_UNION_ERROR(4100, "数据池存储错误，数据出现重复"),
	DATA_POOL_DIMENSION_ERROR(4101, "数据池存储错误，维度参数错误"),
	DATA_POOL_STORAGE_ERROR(4102, "数据池存储错误"),
	DATA_POOL_GROUP_DATA_ERROR(4103, "数据池聚合数据错误"),

	/**
	 * APIEventError
	 */
	API_PARSE_SOURCE_DATA_ERROR(4032,"规则字段返回数据存在错误，解析异常"),

	/**
	 * Server Exception/Error
	 */
	SERVER_ERROR(5000, "服务器开小差去了，请重试或联系管理员"),
	DATASOURCE_ERROR(5000, "数据源服务异常，请检查该服务是否正常"),
	DATASOURCE_CONN_ERROR(5001, "数据源服务网络异常，请检查该服务是否正常"),
	DATASOURCE_CONN_TIMEOUT(5002, "数据源服务繁忙，请稍后重试"),
	DATASOURCE_RESP_ERROR(5003, "数据源服务返回数据异常，请重试或联系管理员"),
	DATASERVER_CONFIG_ERROR(5004, "数据源接口配置出错了，请重试或联系管理员"),


	/**
	 * Server Exception/Error
	 */
	DATA_SOURCE_SERVER_ERROR(5001, "数据源服务异常，请重试或联系管理员"),

	DATASOURCE_CONN_ERROR_TONGDUN(5002,"同盾数据源连接异常，请检查该服务是否正常"),

	DATA_SOURCE_OUTER_ERROR(5003, "上游数据接口请求异常"),

	/**
	 * Rabbitmq Exception/Error
	 */
	RABBIT_MQ_FULL(5672, "队列已满，请稍后重试或联系管理员"),

	RABBIT_MQ_PUSH_FAIL(5673, "推送失败，请稍后重试或联系管理员"),

	/**
	 * Failed Info
	 */
	ERROR(4000, "失败"),

	/**
	 * Success Info
	 */
	SUCCESS(2000, "成功"),

	/**
	 * RulesQuery Validate
	 */

	INTER_NOTFOUND_ERROR(4042,"接口已下线"),
	RULES_NOTFOUND_ERROR(4041,"暂无规则集信息"),

	REDIS_RUN_ERROR(6370,"redis连接异常"),

	FILE_UPLOAD_ERRO(8000,"文件上传失败"),
	FAIL_PARSE_ERRO(4201,"解析出错"),
	/**
	 * worker系列异常
	 *
	 */
	WORKER_TASKROLE_ERROR(7000,"worker创建失败,任务类型非法,请查看application.yml配置和启动参数是否匹配!"),
	WORKER_QUEUE_ILLEGAL_ERROR(7001,"worker创建失败,监听队列非法,请查看application.yml配置和启动参数是否匹配!"),
	WORKER_HEART_BEAT(7002,"worker定时发送心跳异常!"),
	WORKER_LISTEN_QUEUE(7003,"worker监听队列异常!"),
	WORK_EXECUTE_TASK_ERROR(7004,"worker执行任务异常!重新放入队列!失败次数:"),
	WORK_EXECUTE_TASK_ERROR_3(7005,"worker执行任务异常3次,记录消息不再消费,message:"),
	WORK_ERROR(7006,"worker异常!"),
	WORK_NAME_ERROR(7007,"worker创建失败,worker名称不能为空"),
	WORK_QUEUE_NULL_ERROR(7007,"worker创建失败,监听队列不能为空"),
	WORK_QUEUE_TYPE_ERROR(7008,"worker创建失败,只能监听一种队列类型"),
	WORK_MQ_CLOSE(7009,"worker创建失败,rabbitmq开关没打开!"),
	WORK_REDIS_CLOSE(8001,"worker创建失败,redis开关没打开!"),

	KAFKA_ERROR(9000,"推送kafka异常!");


	private int code;
	private String message;

	StatusCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


}