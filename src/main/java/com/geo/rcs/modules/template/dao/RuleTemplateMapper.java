package com.geo.rcs.modules.template.dao;

import com.geo.rcs.modules.template.entity.RuleTemplate;
import com.geo.rcs.modules.template.entity.TemplateLog;
import com.geo.rcs.modules.template.entity.TemplateType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.template.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月20日 下午3:02
 */
@Mapper
@Component(value = "ruleTemplateMapper")
public interface RuleTemplateMapper {

    void save(RuleTemplate rulesTemplate);

    RuleTemplate selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(RuleTemplate ruleTemplate);

    Page<RuleTemplate> findByPage(RuleTemplate ruleTemplate);

    void deleteByPrimaryKey(Integer id);

    void saveLog(TemplateLog templateLog);

    List<TemplateType> getTemplateType();
}
