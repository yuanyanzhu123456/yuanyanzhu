package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.MD5Util;
import com.geo.rcs.modules.sys.dao.CustomerMapper;
import com.geo.rcs.modules.sys.dao.SysUserMapper;
import com.geo.rcs.modules.sys.entity.Customer;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.SysUserRoleService;
import com.geo.rcs.modules.sys.service.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 系统用户
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private CustomerMapper customerMapper;
	@Override
	@Transactional
	public void updateUser(SysUser user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(MD5Util.encode(user.getPassword()));
		}
		sysUserMapper.updateByPrimaryKeySelective(user);
		if(user.getCustomer().getId() != null){
			user.getCustomer().setDsign(1);
			customerMapper.updateByPrimaryKeySelective(user.getCustomer());
		}
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleId());
	}

	@Override
	public SysUser selectByPrimaryKey(Long userId) {
		return sysUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public Page<SysUser> findCustomerByPage(SysUser sysUser) {
		PageHelper.startPage(sysUser.getPageNo(), sysUser.getPageSize());
		return sysUserMapper.findCustomerByPage(sysUser);
	}


	@Override
	//add version
	public Page<SysUser> findCustomerByPageNew(SysUser sysUser) throws ServiceException{
		PageHelper.startPage(sysUser.getPageNo(), sysUser.getPageSize());
		return sysUserMapper.findCustomerByPageNew(sysUser);
	}
	@Override
	@Transactional
	public void saveCustomer(SysUser user) {
		user.setCreateTime(new Date());
		user.setPassword(MD5Util.encode(user.getPassword()));
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setCompany(user.getName());
		user.setRoleId(2L);
		sysUserMapper.insertSelective(user);
		SysUser newUser = sysUserMapper.queryByName(user.getName());
		sysUserMapper.updateCustomerByPrimaryKey(newUser);
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(newUser.getId(), user.getRoleId());
		//保存验真客户信息
		user.getCustomer().setUserId(newUser.getId());
		user.getCustomer().setDsign(1);
		customerMapper.insertSelective(user.getCustomer());
	}

	@Override
	public void updateUserNoCu(SysUser sysUser) throws ServiceException {
		if(StringUtils.isBlank(sysUser.getPassword())){
			sysUser.setPassword(null);
		}else{
			sysUser.setPassword(MD5Util.encode(sysUser.getPassword()));
		}
		sysUserMapper.updateByPrimaryKeySelective(sysUser);
		/*if(sysUser.getCustomer() != null){
			customerMapper.updateByPrimaryKeySelective(sysUser.getCustomer());
		}*/
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(sysUser.getId(), sysUser.getRoleId());
	}

	@Override
	public SysUser selectUniqueCode(Long uniqueCode) {
		return sysUserMapper.selectUniqueCode(uniqueCode);
	}

	@Override
	public void updateUserById(SysUser sysUser){
		if(StringUtils.isBlank(sysUser.getPassword())){
			sysUser.setPassword(null);
		}else{
			sysUser.setPassword(MD5Util.encode(sysUser.getPassword()));
		}
		sysUserMapper.updateByPrimaryKeySelective(sysUser);
	}

	@Override
	public void updateCustomer(Customer customer) throws ServiceException {
		customerMapper.updateByPrimaryKeySelective(customer);
	}


	@Override
	public int updateLoginInfo(Long userId, String loginIp, Date loginDate) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", userId);
		map.put("loginIp", loginIp);
		map.put("loginDate", loginDate);
		return sysUserMapper.updateLoginInfo(map);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserMapper.queryAllMenuId(userId);
	}


	@Override
	public List<SysRole> getRoleList(long id) {
		return sysUserMapper.getRoleList(id);
	}

	@Override
	public SysUser usernameUnique(String name) {
		SysUser user = sysUserMapper.queryByName(name);
		return user;
	}

	private SysUser queryByName(String name) {
		return sysUserMapper.queryByName(name);
	}


	@Override
	public List<SysUser> getUserList(SysUser sysUser) {
		return sysUserMapper.findAll(sysUser);
	}

	@Override
	public Page<SysUser> findByPage(SysUser sysUser) {
		PageHelper.startPage(sysUser.getPageNo(), sysUser.getPageSize());
		return sysUserMapper.findByPage(sysUser);
	}

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	@Override
	public SysUser queryByUserName(String username) {
		return sysUserMapper.queryUserByUserName(username);
	}

	/**
	 * 根据用户名查询用户id
	 * @param username
	 * @return
	 */
	@Override
	public Long queryByUserNameForId(String username){return sysUserMapper.queryByUserNameForId(username);}

	@Override
	public void save(SysUser user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(MD5Util.encode(user.getPassword()));
		if(user.getUniqueCode() != null ){
			SysUser sysUser = sysUserMapper.selectUniqueCode(user.getUniqueCode());
			user.setCompany(sysUser.getName());
		}
		else{
			user.setCompany(user.getName());
		}
		user.setRoleId(user.getRoleId());
		sysUserMapper.insertSelective(user);
		SysUser newUser = sysUserMapper.selectByPrimaryKey(user.getId());
		sysUserMapper.updateByPrimaryKeySelective(newUser);
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(newUser.getId(), user.getRoleId());
		//保存验真客户信息
		if(user.getCustomer() != null){
			user.getCustomer().setUserId(newUser.getId());
			user.getCustomer().setDsign(1);
			customerMapper.insertSelective(user.getCustomer());
		}
		//清空缓存用户数据
		//String key = RedisKeys.USER.getUserKey(user.getCompanyId(), user.getDeptId());
		//redisUtils.deletes(key);
	}
	@Override
	public Map<Object,Object> queryUserInfoById(Long userId) {
		return sysUserMapper.queryUserInfoById(userId);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserMapper.updatePassword(map);
	}

	@Override
	public int disableAccount(Long userId, Integer status) {
		return sysUserMapper.disableAccount(userId, status);
	}

	/**
	 * 客户员工总数
	 * @return
	 */
	@Override
	public Map<String,Object> comEmpCount() {

		List<Map<String,Object>> comEmpRank;
		comEmpRank = sysUserMapper.comEmpRank();

		List<Map<String,Object>> comEmpSum;
		comEmpSum = sysUserMapper.comEmpSum();

		Map<String,Object> result = new HashMap<>();
		result.put("comEmpRank",comEmpRank);
		result.put("comEmpSum",comEmpSum);

		return result;

	}

	@Override
	public Map<Object, Object> queryUserInfoByIdNew(Long userId) {
		return sysUserMapper.queryUserInfoByIdNew(userId);
	}

	@Override
	public Map<Object, Object> getUserAllInfo(Long customerId) {
		Map<Object,Object> map = sysUserMapper.getUserAllInfo(customerId);
		Date old_create_time = (Date)map.get("create_time");
		Date old_versionCreateTime = (Date)map.get("versionCreateTime");
		Date old_versionExpireTime = (Date)map.get("versionExpireTime");
		Date old_versionUpdateTime = (Date)map.get("versionUpdateTime");

		String formate = "yyyy-MM-dd HH:mm:dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);
		String createTime = simpleDateFormat.format(old_create_time);
		String versionCreateTime = simpleDateFormat.format(old_versionCreateTime);
		String versionExpireTime = simpleDateFormat.format(old_versionExpireTime);
		String versionUpdateTime = simpleDateFormat.format(old_versionUpdateTime);

		map.remove("create_time");
		map.remove("versionCreateTime");
		map.remove("versionExpireTime");
		map.remove("versionUpdateTime");

		map.put("createTime",createTime);
		map.put("versionCreateTime",versionCreateTime);
		map.put("versionExpireTime",versionExpireTime);
		map.put("versionUpdateTime",versionUpdateTime);

		return map;
	}

}
