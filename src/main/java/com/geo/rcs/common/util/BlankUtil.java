package com.geo.rcs.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author guoyujie
 * @since 1.0
 */
public final class BlankUtil {

	/**
	 * private constructor and cannot be called by reflection
	 * 
	 * @throws InstantiationException
	 */
	private BlankUtil() throws InstantiationException {
		throw new InstantiationException("Cannot be called by reflection");
	}

	/**
	 * @author guoyujie
	 * @param o
	 * @return
	 */
	public static boolean isBlank(Object o) {

		if (o == null)
			return true;

		if (o instanceof String)
			return ((String) o).trim().length() == 0;

		if (o.getClass().isArray())
			return Array.getLength(o) == 0;

		if (o instanceof Collection)
			return ((Collection<?>) o).size() == 0;

		if (o instanceof Map)
			return ((Map<?, ?>) o).isEmpty();

		return false;
	}

	/**
	 * @author guoyujie
	 * @param o
	 * @return
	 */
	public static boolean isNotBlank(Object o) {
		return !isBlank(o);
	}

	/**
	 * @author guoyujie
	 * @param o
	 * @return
	 */
	public static boolean isBlankAll(Object... o) {

		if (o == null)
			return true;

		for (Object obj : o)
			if (!isBlank(obj))
				return false;

		return true;
	}

	/**
	 * @author guoyujie
	 * @param o
	 * @return
	 */
	public static boolean isNotBlankAll(Object... o) {

		if (o == null)
			return false;

		for (Object obj : o)
			if (isBlank(obj))
				return false;

		return true;
	}
}
