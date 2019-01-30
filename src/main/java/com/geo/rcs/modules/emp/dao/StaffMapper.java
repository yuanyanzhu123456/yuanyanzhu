package com.geo.rcs.modules.emp.dao;

import com.geo.rcs.modules.emp.entity.Staff;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "staffMapper")
public interface StaffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Staff record);

    int insertSelective(Staff record);

    Staff selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);

    List<Staff> getStaffList(Staff staff);

    Page<Staff> findByPage(Staff staff);

    Staff queryByName(String name);

    Staff queryByUserName(String username);
}