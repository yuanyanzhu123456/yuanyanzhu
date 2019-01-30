package com.geo.rcs.modules.template.service.impl;

import com.geo.rcs.modules.template.dao.RuleTemplateMapper;
import com.geo.rcs.modules.template.entity.RuleTemplate;
import com.geo.rcs.modules.template.entity.TemplateLog;
import com.geo.rcs.modules.template.entity.TemplateType;
import com.geo.rcs.modules.template.service.RuleTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.template.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月20日 下午2:58
 */
@Service
public class RuleTemplateServiceImpl implements RuleTemplateService{

    @Autowired
    private RuleTemplateMapper ruleTemplateMapper;

    @Override
    public void save(RuleTemplate ruleTemplate){
        ruleTemplate.setAddTime(new Date());
        ruleTemplateMapper.save(ruleTemplate);

    }

    @Override
    public RuleTemplate selectById(Integer id) {
        return ruleTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(RuleTemplate ruleTemplate) {
        ruleTemplateMapper.updateByPrimaryKeySelective(ruleTemplate);
    }

    @Override
    public void delete(Integer id){
        ruleTemplateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void saveLog(TemplateLog templateLog) {
        ruleTemplateMapper.saveLog(templateLog);
    }

    @Override
    public List<TemplateType> getTemplateType() {
        return ruleTemplateMapper.getTemplateType();
    }

    @Override
    public Page<RuleTemplate> findByPage(RuleTemplate ruleTemplate) {
        PageHelper.startPage(ruleTemplate.getPageNo(), ruleTemplate.getPageSize());
        return ruleTemplateMapper.findByPage(ruleTemplate);
    }
}
