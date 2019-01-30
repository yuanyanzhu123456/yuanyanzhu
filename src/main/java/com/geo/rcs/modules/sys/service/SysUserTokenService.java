package com.geo.rcs.modules.sys.service;


import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.sys.entity.SysUserToken;

/**
 * 用户Token
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
public interface SysUserTokenService {

	SysUserToken queryByUserId(Long userId);

	SysUserToken queryByToken(String token);
	
	void save(SysUserToken token);
	
	void update(SysUserToken token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	Geo createToken(long userId);

    void deleteTokenById(Long userId);
}
