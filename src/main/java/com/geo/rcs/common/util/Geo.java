package com.geo.rcs.common.util;

import com.geo.rcs.common.StatusCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回数据
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/10/18 10:49
 */
public class Geo extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Geo() {
		put("code", StatusCode.SUCCESS.getCode());
	}

	public static Geo error() {
		return error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
	}
	public static Geo dataError() {
		return error(StatusCode.DATA_SOURCE_SERVER_ERROR.getCode(),StatusCode.DATA_SOURCE_SERVER_ERROR.getMessage());
	}

	public static Geo error(String msg) {
		return error(StatusCode.SERVER_ERROR.getCode(), msg);
	}

	public static Geo error(int code, String msg) {
		Geo geo = new Geo();
		geo.put("code", code);
		geo.put("msg", msg);
		return geo;
	}

	public static Geo ok(String msg) {
		Geo geo = new Geo();
		geo.put("msg", msg);
		return geo;
	}

	public static Geo ok(String msg, String data) {
		Geo geo = new Geo();
		geo.put("msg", msg);
		geo.put("data", data);
		return geo;
	}

	public static Geo ok(String msg, List<String> data) {
		Geo geo = new Geo();
		geo.put("msg", msg);
		geo.put("data", data);
		return geo;
	}

	public static Geo ok(Map<String, Object> map) {
		Geo geo = new Geo();
		geo.putAll(map);
		return geo;
	}

	public static Geo ok() {
		return new Geo();
	}

	@Override
	public Geo put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
