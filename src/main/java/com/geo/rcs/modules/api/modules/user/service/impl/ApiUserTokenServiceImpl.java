package com.geo.rcs.modules.api.modules.user.service.impl;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.api.modules.user.dao.ApiUserTokenMapper;
import com.geo.rcs.modules.api.modules.user.entity.ApiUserToken;
import com.geo.rcs.modules.api.modules.user.service.ApiUserTokenService;
import com.geo.rcs.modules.sys.oauth2.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Api用户Token
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/28 15:54
 */
@Service("apiUserTokenService")
public class ApiUserTokenServiceImpl implements ApiUserTokenService {
    @Autowired
    private ApiUserTokenMapper apiUserTokenMapper;
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

	@Override
	public ApiUserToken queryByUserId(Long userId) {
		return apiUserTokenMapper.queryByUserId(userId);
	}

    @Override
    public ApiUserToken queryByToken(String token) {
        return apiUserTokenMapper.queryByToken(token);
    }

    @Override
    public void save(ApiUserToken token) {
        apiUserTokenMapper.save(token);
    }

    @Override
    public void update(ApiUserToken token) {
        apiUserTokenMapper.update(token);
    }

    @Override
    public Geo createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        ApiUserToken tokenEntity = queryByUserId(userId);
        if (tokenEntity == null) {
            tokenEntity = new ApiUserToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            save(tokenEntity);
        } else if (tokenEntity.getExpireTime().getTime() > now.getTime()) {
            token = tokenEntity.getToken();
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            update(tokenEntity);
        }

        Geo geo = Geo.ok().put("token", token).put("expire", EXPIRE);

        return geo;
    }

    @Override
    public Geo createTokenNew(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        ApiUserToken tokenEntity = new ApiUserToken();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        tokenEntity.setUpdateTime(now);
        tokenEntity.setExpireTime(expireTime);

        //保存token
        save(tokenEntity);

        Geo geo = Geo.ok().put("token", token).put("expire", EXPIRE);

        return geo;
    }
}
