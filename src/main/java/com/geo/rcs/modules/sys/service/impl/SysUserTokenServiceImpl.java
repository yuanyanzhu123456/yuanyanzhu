package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.sys.dao.SysUserTokenMapper;
import com.geo.rcs.modules.sys.entity.SysUserToken;
import com.geo.rcs.modules.sys.oauth2.TokenGenerator;
import com.geo.rcs.modules.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户Token
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
	@Autowired
	private SysUserTokenMapper sysUserTokenMapper;
	//24小时后过期
	private final static int EXPIRE = 3600 * 24;

	@Override
	public SysUserToken queryByUserId(Long userId) {
		return sysUserTokenMapper.queryByUserId(userId);
	}

	@Override
	public SysUserToken queryByToken(String token) {
		return sysUserTokenMapper.queryByToken(token);
	}

	@Override
	public void save(SysUserToken token){
		sysUserTokenMapper.save(token);
	}
	
	@Override
	public void update(SysUserToken token){
		sysUserTokenMapper.update(token);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Geo createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		List<SysUserToken> tokenList = sysUserTokenMapper.queryTokenList(userId);
		SysUserToken tokenEntity = null;
		if(tokenList.size() > 1 || tokenList.size() == 0){
			deleteTokenById(userId);
			tokenEntity = new SysUserToken();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		}else if(tokenList.size() == 1) {
			tokenEntity = tokenList.get(0);
			if(tokenEntity.getExpireTime().getTime() > now.getTime()){
				token = tokenEntity.getToken();
			}
			else {
				tokenEntity.setToken(token);
				tokenEntity.setUpdateTime(now);
				tokenEntity.setExpireTime(expireTime);

				//更新token
				update(tokenEntity);
			}
		}

		Geo geo = Geo.ok().put("token", token).put("expire", EXPIRE).put("userId",tokenEntity.getUserId());

		return geo;
	}

	@Override
	public void deleteTokenById(Long userId) {
		sysUserTokenMapper.deleteTokenById(userId);
	}
}
