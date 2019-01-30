package com.geo.rcs.modules.batchtest.uploadfile.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;
import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;
import com.github.pagehelper.Page;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月18日 上午10:25:00
 */
@Mapper
@Component(value = "messageMapper")
public interface MessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageItem record);

    int insertList(List<MessageItem> list);

    MessageItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageItem record);

    int updateByPrimaryKey(MessageItem record);

    List<MessageItem> getMessageItemList(MessageItem staff);

    Page<MessageItem> findByPage(MessageItem staff);

    MessageItem queryByName(String name);

    MessageItem queryByUserName(String username);


	ArrayList<MessageItem> selectByCondition(Long id);

	void updateMessageByBatch(BatchTask batchTask);



}