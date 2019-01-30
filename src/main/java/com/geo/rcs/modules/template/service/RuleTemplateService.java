package com.geo.rcs.modules.template.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.template.entity.RuleTemplate;
import com.geo.rcs.modules.template.entity.TemplateLog;
import com.geo.rcs.modules.template.entity.TemplateType;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.template.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月20日 下午2:58
 */
public interface RuleTemplateService {

    Page<RuleTemplate> findByPage(RuleTemplate engineRule) throws ServiceException;

    void save(RuleTemplate rulesTemplate) throws ServiceException;

    RuleTemplate selectById(Integer id) throws ServiceException;

    void updateByPrimaryKeySelective(RuleTemplate ruleTemplate) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    void saveLog(TemplateLog templateLog);

    List<TemplateType> getTemplateType();
}
