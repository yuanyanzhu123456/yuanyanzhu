package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.monitor.entity.Dimension;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12-22
 */
@Mapper
@Component(value = "baseMapper")
public interface BaseMapper<T> {
	
	void save(T t);
	
	void save(Map<String, Object> map);
	
	void saveBatch(List<T> list);

	void saveBatch(Map<String, Object> map);
	
	int update(T t);
	
	int update(Map<String, Object> map);

	int updateBatch(Map<String, Object> map);
	
	int delete(Object id);
	
	int delete(Map<String, Object> map);
	
	int deleteBatch(Object[] id);

	T queryObject(Object id);
	
	List<T> queryList(Map<String, Object> map);
	
	List<T> queryList(Object id);
	
	int queryTotal(Map<String, Object> map);

	int queryTotal();

	String queryTableIFExists(@Param("tableName")String tableName);

    void createTaskLogTable(@Param("tableName")String logTableName);

    void createTaskTable(@Param("tableName")String taskTableName);

    List<Dimension> getDimension(Long unicode);
}
