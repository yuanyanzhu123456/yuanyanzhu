package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component(value = "sysUserMapper")
public interface SysUserMapper extends BaseMapper{
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser queryUserByUserName(String username);

    int updateLoginInfo(Map<String, Object> map);

    /**
     * 获取所有数据
     * @return
     */
    List<SysUser> findAll(SysUser sysUser);

    /**
     * 分页查询数据
     * @return
     */
    Page<SysUser> findByPage(SysUser sysUser);

    /**
     * 后台查询客户下的用户列表
     * @param sysUser
     * @return
     */
    Page<SysUser> findEmployerListByPage(SysUser sysUser);

    List<String> queryAllPerms(long userId);

    //过期后可用权限查询
    List<String> queryAllPermsOutOfDate(long userId);

    SysUser selectAllPermission(Long userId);

    List<Long> queryAllMenuId(Long userId);


    List<SysRole> getRoleList(long id);


    Map<Object,Object> queryUserInfoById(Long userId);

    int updatePassword(Map<String, Object> map);

    int disableAccount(Long userId, Integer status);

    SysUser queryByName(String name);

    Page<SysUser> findCustomerByPage(SysUser sysUser);

    Page<SysUser> findCustomerByPageNew(SysUser sysUser);

    int updateCustomerByPrimaryKey(SysUser newUser);

    SysUser selectUniqueCode(Long uniqueCode);
    List<Map<String, Object>>  getCustomerStatistic();

    /**
     * wuzuqi
     * @return
     */
    List<SysUser> employeeCount(Integer num);

    List<SysUser> newEmployeeCount(Integer num);

    List<Map<String,Object>> comEmpRank();

    List<Map<String,Object>> comEmpSum();

    Map<Object,Object> queryUserInfoByIdNew(Long userId);

    Long queryByUserNameForId(String username);

    Long getUniqueCodeById(Long id);

    Map<Object,Object> getUserAllInfo(Long customerId);

    List<SysMenu> queryPermissionOutOfDate(@Param(value = "id") Long id, @Param(value = "roleId") Long roleId);
}