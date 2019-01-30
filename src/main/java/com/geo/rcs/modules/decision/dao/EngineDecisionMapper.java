package com.geo.rcs.modules.decision.dao;

import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月03日 上午11:25
 */
@Mapper
@Component("engineDecisionMapper")
public interface EngineDecisionMapper {
    /**
     * 根据用户编号获取决策集
     * @param userId
     * @return
     */
    EngineDecision getEngineDecisionByUserId(Long userId);
    /**
     * 根据编号获取决策集
     * @param id
     * @return
     */
    EngineDecision selectByPrimaryKey(Integer id);
    /**
     * 保存决策集
     * @param engineDecision
     * @return
     */
    int save(EngineDecision engineDecision);
    /**
     * 修改决策集
     * @param engineDecision
     * @return
     */
    void update(EngineDecision engineDecision);
    /**
     * 获取决策集列表（分页）
     * @param map
     * @return
     */
    Page<EngineDecision> findByPage(Map<String, Object> map);
    /**
     * 根据编号删除决策集
     * @param id
     * @return
     */
    void deleteByPrimaryKey(Integer id);
    /**
     * 根据用户编号获取决策集
     * @param uniqueCode
     * @return
     */
    List<EngineDecision> getDecisionByUserId(Long uniqueCode);
    /**
     * 获取决策集总数
     * @param map
     * @return
     */
    int getDecisionTotal(Map<String, Object> map);
    /**
     * 根据名称获取决策集
     * @param name,businessId,unique_code
     * @return
     */
    List<EngineDecision> verifyName(@Param("name") String name, @Param("uniqueCode") Long unique_code,@Param("businessId") Integer businessId);
    /**
     * 根据编号获取决策集名称
     * @param id
     * @return
     */
    String selectNameById(Integer id);
    /**
     * 根据业务类型获取决策集
     * @param businessId
     * @return
     */
    List<Integer> selectBusinessIdById(Integer businessId);
    /**
     * 根据名称获取决策集
     * @param name
     * @return
     */
    List<EngineDecision> getDecisionByName(String name);

    /**
     * 获取所有决策集中使用的规则集编号
     * @param uniqueCode
     * @return
     */
    List<String> getUsageRuleSet(Long uniqueCode);
}
