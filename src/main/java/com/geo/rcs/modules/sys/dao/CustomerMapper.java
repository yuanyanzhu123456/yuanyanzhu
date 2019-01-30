package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.Customer;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component(value = "customerMapper")
public interface CustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    Customer findByUserId(Long userId);

    Page<SysUser> findCustomerByPage(SysUser sysUser);

    Customer selectByUserId(Long userId);
}