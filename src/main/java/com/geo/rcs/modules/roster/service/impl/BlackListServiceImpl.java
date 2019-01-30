package com.geo.rcs.modules.roster.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.excel.ReadExcel;
import com.geo.rcs.modules.roster.dao.BlackListMapper;
import com.geo.rcs.modules.roster.entity.BlackList;
import com.geo.rcs.modules.roster.service.BlackListService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.roster.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email jinlin@geotmt.com
 * @Creation Date : 2017年12月28日 上午11:38
 */
@Service
public class BlackListServiceImpl implements BlackListService {

    @Autowired
    private BlackListMapper blackListMapper;

    /**
     * 查看黑名单列表（模糊，分页）
     * @param blackList
     * @return
     * @throws ServiceException
     */
    @Override
    public Page<BlackList> findByPage(BlackList blackList) {
        PageHelper.startPage(blackList.getPageNo(),blackList.getPageSize());
        return blackListMapper.findByPage(blackList);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        blackListMapper.deleteBatch(ids);
    }

    @Override
    public List<Map<String,Object>>  findAll() {
        return blackListMapper.findAll();
    }

    @Override
    public String readExcelFile(MultipartFile file) {
        String result ="";
        //创建处理EXCEL的类
        ReadExcel readExcel=new ReadExcel();
        //解析excel，获取上传的事件单
        /*//List<BlackList> blackLists = readExcel.getExcelInfo(file);
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
        //和你具体业务有关,这里不做具体的示范
        if(blackLists != null && !blackLists.isEmpty()){
            result = "上传成功";
        }else{
            result = "上传失败";
        }*/
        return result;
    }
    /**
     * 添加员工
     * @param staff
     * @throws ServiceException
     */
   /* @Override
    @Transactional
    public void save(AbTest staff){
        staff.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        staff.setSalt(salt);
        //user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        staff.setPassword(MD5Util.encode(staff.getPassword()));
        staffMapper.insert(staff);
        AbTest newStaff= staffMapper.queryByName(staff.getName());
        //保存用户与角色关系
        sysUserRoleService.updateOrSave(newStaff.getId(), staff.getRoleId(),staff.getuId());

        //清空缓存用户数据
        //String key = RedisKeys.USER.getUserKey(user.getCompanyId(), user.getDeptId());
        //redisUtils.deletes(key);


    }

    *//**
     * 判断员工是否存在
     * @param name
     * @return
     * @throws ServiceException
     *//*
    @Override
    public boolean usernameUnique(String name) throws ServiceException {
        if (BlankUtil.isBlank(name))
            return false;
        Staff staff = queryByName(name);
        return staff == null ? true : false;
    }*/
}
