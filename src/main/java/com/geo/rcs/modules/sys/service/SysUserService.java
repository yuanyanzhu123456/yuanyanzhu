package com.geo.rcs.modules.sys.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.sys.entity.Customer;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
public interface SysUserService {


	/**
	 * 获取所有数据
	 * @return
	 */
	List<SysUser> getUserList(SysUser sysUser) throws ServiceException;

	/**
	 * 分页查询数据
	 * @return
	 */
	Page<SysUser> findByPage(SysUser sysUser) throws ServiceException;


	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	/*List<String> queryAllPerms(Long userId);
	
	*//**
	 * 查询用户的所有菜单ID
	 *//*
	List<Long> queryAllMenuId(Long userId);*/

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUser queryByUserName(String username);

	/**
	 * 根据用户名，查询系统用户Id
	 */
	Long queryByUserNameForId(String username);
	
	/**
	 * 根据用户ID，查询用户
	 * @param userId
	 * @return
	 *//*
	SysUser queryObject(Long userId);
	
	*//**
	 * 查询用户列表
	 *//*
	List<SysUser> queryList(Map<String, Object> map);
	
	*//**
	 * 查询总数
	 *//*
	int queryTotal(Map<String, Object> map);
	
	*//**
	 * 保存用户
	 */
	void save(SysUser user) throws ServiceException;
	
	/**
	 * 修改用户
	 *//*
	void update(SysUser user);
	
	*//**
	 * 删除用户
	 *//*
	void deleteBatch(Long[] userIds);
	
	*//**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	int updatePassword(Long userId, String password, String newPassword);

	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param userId		用户ID
	 * @param loginIp		用户登录IP
	 * @param loginDate		用户登录时间
	 * @return
	 */
	int updateLoginInfo(Long userId, String loginIp, Date loginDate);

    List<Long> queryAllMenuId(Long userId);


    List<SysRole> getRoleList(long id);

    SysUser usernameUnique(String name) throws ServiceException ;



	/**
	 * 根据companyId和deptId获取符合条件的所有用户
	 * @param companyId
	 * @param deptId
	 * @return
	 *//*
	List<Map> queryList(Long companyId, Long deptId);

	*//**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	Map<Object,Object> queryUserInfoById(Long userId);

	/**
	 * 禁用/启用账户
	 * @param userId
	 * @param status
	 * @return
	 */
	int disableAccount(Long userId, Integer status);

    void updateUser(SysUser sysUser) throws ServiceException;

    SysUser selectByPrimaryKey(Long userId);

	Page<SysUser> findCustomerByPage(SysUser sysUser) throws ServiceException;

	//add version
	Page<SysUser> findCustomerByPageNew(SysUser sysUser) throws ServiceException;

	void saveCustomer(SysUser user);

    void updateUserNoCu(SysUser sysUser) throws ServiceException;

	SysUser selectUniqueCode(Long uniqueCode);

	void updateUserById(SysUser sysUser) throws ServiceException;

	void updateCustomer(Customer customer) throws ServiceException;


	/**
	 * 客户员工总数
	 * @return
	 */
	Map<String,Object> comEmpCount();

	/**
	 * 获取用户信息以及版本信息
	 */
	Map<Object,Object> queryUserInfoByIdNew(Long userId);

	/**
	 * 获取用户所有信息
	 * @param customerId
	 * @return
	 */
	Map<Object,Object> getUserAllInfo(Long customerId);
}
