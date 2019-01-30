package com.geo.rcs.modules.geo.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.MD5Util;
import com.geo.rcs.modules.geo.dao.GeoUserMapper;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.entity.GeoUserToken;
import com.geo.rcs.modules.geo.service.GeoUserService;
import com.geo.rcs.modules.sys.dao.SysUserRoleMapper;
import com.geo.rcs.modules.sys.oauth2.TokenGenerator;
import com.geo.rcs.modules.sys.service.SysUserRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年02月27日 上午10:25
 */
@Service
public class GeoUserServiceImpl implements GeoUserService{
    //24小时后过期
    private final static int EXPIRE = 3600 * 24;
    @Autowired
    private GeoUserMapper geoUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    /**
     * 查询员工列表（支持多条件模糊，分页）
     * @param GeoUser
     * @return
     */
    @Override
    public List<GeoUser> getGeoUserList(GeoUser GeoUser) {
        return geoUserMapper.getGeoUserList(GeoUser);
    }

    @Override
    public Page<GeoUser> findByPage(GeoUser GeoUser) {
        PageHelper.startPage(GeoUser.getPageNo(), GeoUser.getPageSize());
        return geoUserMapper.findByPage(GeoUser);
    }

    /**
     * 添加员工
     * @param GeoUser
     * @throws ServiceException
     */
    @Override
    @Transactional
    public void save(GeoUser GeoUser){

        //转换提日期输出格式
        GeoUser.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        GeoUser.setSalt(salt);
        GeoUser.setPassword(MD5Util.encode(GeoUser.getPassword()));
        GeoUser.setContact(GeoUser.getCompany());
        geoUserMapper.insertSelective(GeoUser);
        GeoUser geoUser = geoUserMapper.queryByName(GeoUser.getName());
        //保存用户与角色关系
        sysUserRoleService.updateOrSave(geoUser.getId(), geoUser.getRoleId());

        //清空缓存用户数据
        //String key = RedisKeys.USER.getUserKey(user.getCompanyId(), user.getDeptId());
        //redisUtils.deletes(key);


    }

    /**
     * 判断员工是否存在
     * @param name
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean usernameUnique(String name) {
        if (BlankUtil.isBlank(name))
            return false;
        GeoUser geoUser = queryByName(name);
        return geoUser == null;
    }

    /**
     * 修改员工信息以及权限
     * @param GeoUser
     * @throws ServiceException
     */
    @Override
    public void updateGeoUser(GeoUser GeoUser) throws ServiceException {
        if (GeoUser == null)
            return;
        if (BlankUtil.isNotBlank(GeoUser.getPassword()))
            GeoUser.setPassword(MD5Util.encode(GeoUser.getPassword()));
        GeoUser geoUser = getGeoUserById(GeoUser.getId());
        if (geoUser == null || geoUser.getId() == null)
            return;
        //转换提日期输出格式
        GeoUser.setCreateTime(new Date());
        try {
            geoUserMapper.updateByPrimaryKeySelective(GeoUser);
            //保存用户与角色关系
            Map<String, Object> map = new HashMap<>();
            map.put("GeoUserId", GeoUser.getId());
            map.put("roleId", GeoUser.getRoleId());
            if(GeoUser.getId() != null && GeoUser.getRoleId()!= null)
                sysUserRoleMapper.updateStaffRole(map);
        } catch (Exception e) {
            //logger.error("更新用户失败！", e);
            throw new ServiceException();
        }
    }

    /**
     * 查询员工信息（回显）
     * @param id
     * @return
     */
    @Override
    public GeoUser getGeoUserById(Long id) {
        return geoUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public GeoUser queryByName(String name) {
        return geoUserMapper.queryByName(name);
    }

    @Override
    public int updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("loginIp", loginIp);
        map.put("loginDate", loginDate);
        return geoUserMapper.updateLoginInfo(map);
    }

    @Override
    public Geo createToken(Long geoId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        GeoUserToken tokenEntity = queryByGeoId(geoId);
        if(tokenEntity == null){
            tokenEntity = new GeoUserToken();
            tokenEntity.setGeoId(geoId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            saveToken(tokenEntity);
        }else if(tokenEntity.getExpireTime().getTime() > now.getTime()) {
            token = tokenEntity.getToken();
        }else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            updateToken(tokenEntity);
        }

        Geo geo = Geo.ok().put("token", token).put("expire", EXPIRE).put("geoId",geoId);

        return geo;
    }

    private GeoUserToken queryByGeoId(Long geoId) {
        return geoUserMapper.queryByGeoId(geoId);
    }
    @Override
    public void saveToken(GeoUserToken token){
        geoUserMapper.saveToken(token);
    }
    @Override
    public void updateToken(GeoUserToken token){
        geoUserMapper.updateToken(token);
    }

    @Override
    public GeoUserToken queryGeoUserByToken(String accessToken) {
        return geoUserMapper.queryGeoUserByToken(accessToken);
    }

    @Override
    public GeoUser queryGeoUserById(Long geoId) {
        return geoUserMapper.selectByPrimaryKey(geoId);
    }

    @Override
    public Set<String> getUserPermissions(Long geoId) {
        List<String> permsList;
        permsList = geoUserMapper.queryAllPerms(geoId);
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

	@Override
	public void deleteTokenById(Long id) {
		// TODO Auto-generated method stub
		geoUserMapper.deleteTokenByPrimaryKey(id);
	}
}
