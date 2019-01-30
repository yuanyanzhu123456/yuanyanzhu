package com.geo.rcs.modules.emp.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.emp.entity.Staff;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月26日 下午4:47
 */
public interface StaffService {
    List<Staff> getStaffList(Staff staff);

    Page<Staff> findByPage(Staff staff) throws ServiceException;

    void save(Staff staff) throws ServiceException;

    Staff usernameUnique(String name) throws ServiceException;

    void updateStaff(Staff staff) throws ServiceException;

    Staff getStaffById(Long id);

    Staff queryByName(String name);
}
