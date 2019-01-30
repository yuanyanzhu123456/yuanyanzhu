package com.geo.rcs.modules.admin.menumanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.admin.menumanagement.entity.Menu;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.github.pagehelper.Page;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月7日 上午10:14:41
 */
@Mapper
@Component(value = "menuMapper")
public interface MenuMapper {
	int deleteByPrimaryKey(Long id);

	int insertSelective(Menu record);

	int updateByPrimaryKeySelective(Menu record);

	List<Menu> findByPage(Menu menu);

}
