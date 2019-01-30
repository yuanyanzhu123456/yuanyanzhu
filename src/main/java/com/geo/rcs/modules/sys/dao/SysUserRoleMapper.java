package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户与角色对应关系
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
@Mapper
@Component(value = "sysUserRoleMapper")
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);

	@Override
	void save(Map<String, Object> map);

	void add(Map<String, Object> map);

	void deleteStaff(Long staffId);

	void updateStaffRole(Map<String, Object> map);
}
