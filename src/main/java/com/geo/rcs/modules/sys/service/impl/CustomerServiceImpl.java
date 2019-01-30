package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.redis.RedisKeys;
import com.geo.rcs.common.redis.RedisUtils;
import com.geo.rcs.modules.sys.dao.CustomerMapper;
import com.geo.rcs.modules.sys.entity.Customer;
import com.geo.rcs.modules.sys.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验真平台接口认证信息
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/3/5 15:52
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Customer findByUserId(Long userId) {
        if(userId == null){
            return null;
        }

        /** 获取缓存key */
        String key = RedisKeys.YANZHEN.getOauthInfoKey(userId);

        /**
         * 根据key从缓存中获取信息，若获取不到重新缓存
         */
//        Customer customer = JSONUtil.jsonToBean(redisUtils.get(key), Customer.class);
        Customer customer =null;
        if(customer == null){
            customer = customerMapper.findByUserId(userId);
//            if(customer != null){
//                redisUtils.set(key, customer);
//            }
        }

        return customer;
    }

    @Override
    public Customer selectByUserId(Long id) {
        return customerMapper.selectByUserId(id);
    }
}
