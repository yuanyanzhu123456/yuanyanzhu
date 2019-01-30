package com.geo.rcs.modules.batchtest.batchTask.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月23日 下午12:30:02 
 */
@Mapper
@Component(value = "batchMapper")
public interface BatchTaskMapper {

	void insertBatch(BatchTask batchTask);


	void updateBatchByPrimaryKey(BatchTask batchTask);


	BatchTask selectByPrimaryKey(long id);
	

}
