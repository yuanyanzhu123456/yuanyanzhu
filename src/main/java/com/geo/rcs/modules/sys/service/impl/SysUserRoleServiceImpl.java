package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.modules.sys.dao.SysUserRoleMapper;
import com.geo.rcs.modules.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户与角色的对应关系
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	//用户角色添加
	@Override
	public void saveOrUpdate(Long userId, Long roleId) {
		if(roleId == null){
			return ;
		}
		
		//先删除用户与角色关系
		sysUserRoleMapper.delete(userId);
		
		//保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("roleId", roleId);
		sysUserRoleMapper.save(map);
	}
	//员工角色添加
	@Override
	public void updateOrSave(Long staffId, Long roleId) {
		if(roleId == null){
			return ;
		}

		//先删除员工与角色关系
		sysUserRoleMapper.deleteStaff(staffId);

		//保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("staffId", staffId);
		map.put("roleId", roleId);
		sysUserRoleMapper.add(map);
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return sysUserRoleMapper.queryRoleIdList(userId);
	}

	@Override
	public void delete(Long userId) {
		sysUserRoleMapper.delete(userId);
	}
}
