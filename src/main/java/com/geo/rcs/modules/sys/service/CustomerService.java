package com.geo.rcs.modules.sys.service;

import com.geo.rcs.modules.sys.entity.Customer;

/**
 * 客户验真平台接口认证信息
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/3/5 15:48
 */
public interface CustomerService {

    /**
     * 根据用户id获取客户验真平台接口认证信息
     * @param userId
     * @return
     */
    Customer findByUserId(Long userId);

    Customer selectByUserId(Long id);
}
