package com.geo.rcs.modules.event.customerEvent.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.event.customerEvent.entity.CustomerEventEntry;
import com.geo.rcs.modules.sys.entity.SysUser;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年4月11日 下午7:16:15 
 */
@Mapper
@Component(value = "CustomerEventEntryMapper")
public interface CustomerEventEntryMapper {
    

    List<CustomerEventEntry> findByPage(Map<String, Object> map);

	List<SysUser> getCustomerType();

	List<CustomerEventEntry> findByPageday(Map<String, Object> map);

	List<CustomerEventEntry> findByPageweek(Map<String, Object> map);
}
