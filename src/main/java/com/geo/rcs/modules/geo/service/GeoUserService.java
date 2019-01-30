package com.geo.rcs.modules.geo.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.entity.GeoUserToken;
import com.github.pagehelper.Page;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年02月27日 上午10:23
 */

public interface GeoUserService {
    List<GeoUser> getGeoUserList(GeoUser geoUser);

    Page<GeoUser> findByPage(GeoUser geoUser) throws ServiceException;

    void save(GeoUser geoUser) throws ServiceException;

    boolean usernameUnique(String name) throws ServiceException;

    void updateGeoUser(GeoUser geoUser) throws ServiceException;

    GeoUser getGeoUserById(Long id);

    GeoUser queryByName(String name);

    int updateLoginInfo(Long userId, String loginIp, Date loginDate);

    Geo createToken(Long geoId);

    void saveToken(GeoUserToken token);

    void updateToken(GeoUserToken token);

    GeoUserToken queryGeoUserByToken(String accessToken);

    GeoUser queryGeoUserById(Long geoId);

    Set<String> getUserPermissions(Long geoId);

	void deleteTokenById(Long id);
}
