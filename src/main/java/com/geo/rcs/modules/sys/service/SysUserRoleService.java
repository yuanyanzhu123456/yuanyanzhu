package com.geo.rcs.modules.sys.service;

import java.util.List;

/**
 * 用户与角色的对应关系
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
public interface SysUserRoleService {
	
	void saveOrUpdate(Long userId, Long roleId);

	void updateOrSave(Long staffId, Long roleId);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);
	
	void delete(Long userId);
}
