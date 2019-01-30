package com.geo.rcs.modules.emp.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.MD5Util;
import com.geo.rcs.modules.emp.dao.StaffMapper;
import com.geo.rcs.modules.emp.entity.Staff;
import com.geo.rcs.modules.emp.service.StaffService;
import com.geo.rcs.modules.sys.dao.SysUserRoleMapper;
import com.geo.rcs.modules.sys.service.SysUserRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月26日 下午4:47
 */
@Service
public class StaffServiceImpl implements StaffService{

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    /**
     * 查询员工列表（支持多条件模糊，分页）
     * @param staff
     * @return
     */
    @Override
    public List<Staff> getStaffList(Staff staff) {
        return staffMapper.getStaffList(staff);
    }

    @Override
    public Page<Staff> findByPage(Staff staff) {
        PageHelper.startPage(staff.getPageNo(), staff.getPageSize());
        return staffMapper.findByPage(staff);
    }

    /**
     * 添加员工
     * @param staff
     * @throws ServiceException
     */
    @Override
    @Transactional
    public void save(Staff staff){

        //转换提日期输出格式
        staff.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        staff.setSalt(salt);
        staff.setPassword(MD5Util.encode(staff.getPassword()));
        staff.setContact(staff.getCompany());
        staffMapper.insertSelective(staff);
        Staff staff1 = staffMapper.queryByName(staff.getName());
        //保存用户与角色关系
        sysUserRoleService.updateOrSave(staff1.getId(), staff.getRoleId());

        //清空缓存用户数据
        //String key = RedisKeys.USER.getUserKey(user.getCompanyId(), user.getDeptId());
        //redisUtils.deletes(key);


    }

    /**
     * 判断员工是否存在
     * @param username
     * @return
     * @throws ServiceException
     */
    @Override
    public Staff usernameUnique(String username) {
        if (BlankUtil.isBlank(username))
            return null;
        ;
        return staffMapper.queryByUserName(username);
    }



    /**
     * 修改员工信息以及权限
     * @param staff
     * @throws ServiceException
     */
    @Override
    public void updateStaff(Staff staff) throws ServiceException {
        if (staff == null)
            return;
        if (BlankUtil.isNotBlank(staff.getPassword()))
            staff.setPassword(MD5Util.encode(staff.getPassword()));
        Staff staff1 = getStaffById(staff.getId());
        if (staff1 == null || staff1.getId() == null)
            return;
        //转换提日期输出格式
        staff.setCreateTime(new Date());
        try {
            staffMapper.updateByPrimaryKeySelective(staff);
            //保存用户与角色关系
            Map<String, Object> map = new HashMap<>();
            map.put("staffId", staff.getId());
            map.put("roleId", staff.getRoleId());
            if(staff.getId() != null && staff.getRoleId()!= null)
            sysUserRoleMapper.updateStaffRole(map);
        } catch (Exception e) {
            //logger.error("更新用户失败！", e);
            throw new ServiceException();
        }
    }

    /**
     * 查询员工信息（回显）
     * @param id
     * @return
     */
    @Override
    public Staff getStaffById(Long id) {
        return staffMapper.selectByPrimaryKey(id);
    }

    @Override
    public Staff queryByName(String name) {
        return staffMapper.queryByName(name);
    }
}
