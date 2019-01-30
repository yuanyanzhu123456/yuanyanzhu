package com.geo.rcs.modules.api.modules.user.service;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.api.modules.user.entity.ApiUserToken;

/**
 * Api用户Token
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/28 15:52
 */
public interface ApiUserTokenService {

	ApiUserToken queryByUserId(Long userId);

	ApiUserToken queryByToken(String token);
	
	void save(ApiUserToken token);
	
	void update(ApiUserToken token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	Geo createToken(long userId);
	Geo createTokenNew(long userId);

}
