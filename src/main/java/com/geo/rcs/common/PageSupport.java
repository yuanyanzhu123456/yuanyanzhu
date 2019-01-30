package com.geo.rcs.common;

import com.github.pagehelper.Page;

/**
 * @author guoyujie
 * @date 2017-12-15
 * @version 1.0
 */
public class PageSupport {

	private static final ThreadLocal<Page> PAGE_LOCAL = new ThreadLocal<Page>();
	private static final ThreadLocal<Boolean> IS_PAGE_LOCAL = new ThreadLocal<Boolean>();

	/**
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param pageNum
	 * @param pageSize
	 */
	public static void setPagePara(int pageNum, int pageSize) {
		page();
		PAGE_LOCAL.set(new Page(pageNum, pageSize));
	}

	/**
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param page
	 */
	public static void setPage(Page page) {
		if (page == null)
			return;
		PAGE_LOCAL.set(page);
	}

	/**
	 * @author guoyujie
	 * @date 2017-12-15
	 * @return
	 */
	public static Page getPage() {
		Page page = PAGE_LOCAL.get();
		if (page == null)
			return null;
		PAGE_LOCAL.remove();
		return page;
	}

	/**
	 * 设置不分页
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 */
	public static void notPage() {
		IS_PAGE_LOCAL.set(false);
	}

	/**
	 * 默认分页，设置分页
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 */
	public static void page() {
		IS_PAGE_LOCAL.set(true);
	}

	/**
	 * 判断是否分页
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @return
	 */
	public static boolean isPage() {
		return IS_PAGE_LOCAL.get();
	}
}
