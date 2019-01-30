package com.geo.rcs.modules.geo.dao;

import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.entity.GeoUserToken;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年02月27日 上午10:21
 */
@Mapper
@Component(value = "geoUserMapper")
public interface GeoUserMapper {
    int deleteTokenByPrimaryKey(Long id);

    int insert(GeoUser record);

    int insertSelective(GeoUser record);

    GeoUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GeoUser record);

    int updateByPrimaryKey(GeoUser record);

    List<GeoUser> getGeoUserList(GeoUser geoUser);

    Page<GeoUser> findByPage(GeoUser geoUser);

    GeoUser queryByName(String name);

    int updateLoginInfo(Map<String, Object> map);

    GeoUserToken queryByGeoId(Long geoId);

    void saveToken(GeoUserToken token);

    void updateToken(GeoUserToken token);

    GeoUserToken queryGeoUserByToken(String accessToken);

    Set<String> getUserPermissions(Long geoId);

    List<String> queryAllPerms(Long geoId);
    List<Map<String, Object>>  getUserStatistics();
    List<Map<String, Object>>  getUserLoginStatistics();
    List<Map<String, Object>>  getActiveUsersDay(Map<String, String> parmMap);
    List<Map<String, Object>>  getActiveUsersMonth(Map<String, String> parmMap);

}
