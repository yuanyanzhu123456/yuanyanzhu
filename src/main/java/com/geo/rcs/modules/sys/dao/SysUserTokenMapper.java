package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysUserToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统用户Token
 *
 * @author guoyujie
 * @email guoyujie@gmail.com
 * @date 2017-12-22
 */
@Mapper
@Component(value = "sysUserTokenMapper")
public interface SysUserTokenMapper extends BaseMapper<SysUserToken> {

    SysUserToken queryByUserId(Long userId);

    SysUserToken queryByToken(String token);

    void deleteTokenById(Long userId);

    List<SysUserToken> queryTokenList(long userId);
}
