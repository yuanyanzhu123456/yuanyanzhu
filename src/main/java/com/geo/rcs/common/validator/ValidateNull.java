package com.geo.rcs.common.validator;

import com.geo.rcs.common.validator.NotNull.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author guoyujie
 * @date 2017-12-22
 * @version 1.0
 */
public class ValidateNull {

	private final static Logger logger = LoggerFactory.getLogger(ValidateNull.class);

	/**
	 * 私有且禁止通过反射实例化
	 */
	private ValidateNull() {
		throw new RuntimeException();
	}

	/**
	 * 注解处理
	 *
	 * @author guoyujie
	 * @dateTime 2016-7-25 下午12:04:23
	 * @param obj
	 * @param type
	 * @return
	 */
	public static ResultType check(Object obj, NotNull.RequestType type) {

		if (obj == null || type == null)
			return ResultType.FAILD.setMsg("对象为空！");

		Field[] fields = obj.getClass().getDeclaredFields();
		boolean flag = false;
		StringBuilder sb = new StringBuilder();

		for (Field field : fields) {
			if (!field.isAnnotationPresent(NotNull.class))
				continue;
			NotNull annotation = field.getAnnotation(NotNull.class);
			field.setAccessible(true);
			try {
				Object o = field.get(obj);
				if (equ(annotation.type(), type) && isNull(o)) {
					sb.append(field.getName() + annotation.value()).append(";");
					flag = true;
				}
			} catch (Exception e) {
				flag = true;
				logger.error("参数为空判断异常", e);
			}
		}

		if (flag)
			return ResultType.FAILD.setMsg(sb.toString());
		return ResultType.SUCCESS;
	}

	/**
	 * 请求参数为空判断
	 *
	 * @author guoyujie
	 * @dateTime 2016-7-25 下午1:21:38
	 * @param
	 * @return
	 */
	public static boolean isNull(Object para) {

		return (para == null || "".equals(para) || "()".equals(para) || "null".equals(para) || "(null)".equals(para)
				|| "undefined".equals(para))|| ((para instanceof String) && ((String) para).trim().length() == 0);
	}

	/**
	 * 请求类型与请求处理逻辑实现
	 *
	 * @author liuzeke
	 * @dateTime 2016-7-25 下午1:23:32
	 * @param t1
	 * @param t2
	 * @return
	 */
	private static boolean equ(RequestType t1, RequestType t2) {

		if (t1 == null || t2 == null)
			return false;
		if (t1 == t2)
			return true;
		else if (t1 == RequestType.NEW_UPDATE && t2 == RequestType.NEW)
			return true;
		else if (t1 == RequestType.NEW_UPDATE && t2 == RequestType.UPDATE)
			return true;
		else if (t1 == RequestType.UPDATE_DELETE && t2 == RequestType.UPDATE)
			return true;
		else return t1 == RequestType.UPDATE_DELETE && t2 == RequestType.DELETE;
	}
}
