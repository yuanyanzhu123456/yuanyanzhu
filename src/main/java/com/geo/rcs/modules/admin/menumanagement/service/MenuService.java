package com.geo.rcs.modules.admin.menumanagement.service;

import java.util.List;
import java.util.Map;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.admin.menumanagement.entity.Menu;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.github.pagehelper.Page;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月6日 上午10:14:41
 */
public interface MenuService {

	PageInfo<Menu> findByPage(Menu field, int many) throws ServiceException;

	void save(Menu Menu) throws ServiceException;

	void delete(Long id) throws ServiceException;

	void updateMenu(Menu menu) throws ServiceException;

}
