package com.geo.rcs.modules.api.modules.user.dao;

import com.geo.rcs.modules.api.modules.user.entity.ApiUserToken;
import com.geo.rcs.modules.sys.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Api用户Token
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/28 15:51
 */
@Mapper
@Component(value = "apiUserTokenMapper")
public interface ApiUserTokenMapper extends BaseMapper<ApiUserToken> {

    ApiUserToken queryByUserId(Long userId);

    ApiUserToken queryByToken(String token);
	
}
