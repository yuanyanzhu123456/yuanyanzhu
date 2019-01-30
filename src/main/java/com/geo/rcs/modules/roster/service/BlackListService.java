package com.geo.rcs.modules.roster.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.roster.entity.BlackList;
import com.github.pagehelper.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.roster.service
 * @Description : TODD
 * @Author guoyujie
 * @email jinlin@geotmt.com
 * @Creation Date : 2017年12月28日 上午11:37
 */
public interface BlackListService {

    Page<BlackList> findByPage(BlackList blackList) throws ServiceException;

    void deleteBatch(Long[] ids) throws ServiceException;

    List<Map<String,Object>>  findAll();

    String readExcelFile(MultipartFile file);
}
