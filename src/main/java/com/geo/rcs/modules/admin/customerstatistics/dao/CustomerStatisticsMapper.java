package com.geo.rcs.modules.admin.customerstatistics.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月2日 下午4:24:05
 */
@Mapper
@Component(value = "customerStatisticsMapper")
public interface CustomerStatisticsMapper {
	Page<EventEntry> findAllByPage(EventEntry entry);
}
