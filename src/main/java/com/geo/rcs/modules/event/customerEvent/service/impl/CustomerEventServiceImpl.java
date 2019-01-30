package com.geo.rcs.modules.event.customerEvent.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geo.rcs.common.redis.RedisUtils;
import com.geo.rcs.modules.event.customerEvent.dao.CustomerEventEntryMapper;
import com.geo.rcs.modules.event.customerEvent.entity.CustomerEventEntry;
import com.geo.rcs.modules.event.customerEvent.service.CustomerEventService;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年4月11日 下午7:03:52 
 */
@Service
public class CustomerEventServiceImpl implements CustomerEventService {
    @Autowired
    private CustomerEventEntryMapper customerEventEntryMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<SysUser> getCustomerType() {
        return customerEventEntryMapper.getCustomerType();
    }
    @Override
    public PageInfo<CustomerEventEntry> findByPage(Map<String, Object> map,int flag) {
    
    	int circle=Integer.valueOf((String) map.get("circle"));
    	PageInfo<CustomerEventEntry> pageInfo=null;
    	if (flag==1) {
    		PageHelper.startPage((int)map.get("pageNo"), (int)map.get("pageSize"));
    	}
    	if (circle==0) {
	        List<CustomerEventEntry> eventEntries = customerEventEntryMapper.findByPage(map);
	         pageInfo = new PageInfo<>(eventEntries);
    	}else if (circle==1) {
    		 List<CustomerEventEntry> eventEntries = customerEventEntryMapper.findByPageday(map);
	         pageInfo = new PageInfo<>(eventEntries);
		}else if (circle==2) {
			List<CustomerEventEntry> eventEntries = customerEventEntryMapper.findByPageweek(map);
	         pageInfo = new PageInfo<>(eventEntries);
		}
    	
        return pageInfo;
    }

 
  

    /**
     * 拼装数据
     * @param eventStatEntryList
     * @return
     */
    private CustomerEventEntry assembleData(List<CustomerEventEntry> customereventEntryList) {
    	CustomerEventEntry eventStatEntry = new CustomerEventEntry();
        int passEventCount = 0, refuseEventCount = 0, manualReviewCount = 0, invalidEventCount = 0, eventTotal = 0;

        if(customereventEntryList != null || !customereventEntryList.isEmpty()){
            for (CustomerEventEntry entry : customereventEntryList) {
                passEventCount += entry.getPass();
                refuseEventCount += entry.getNotpass();
                manualReviewCount += entry.getMannual();
            }
        }
        eventTotal=passEventCount+refuseEventCount+manualReviewCount;
        eventStatEntry.setPass(passEventCount);
        eventStatEntry.setNotpass(refuseEventCount);
        eventStatEntry.setMannual(manualReviewCount);
        
        
        eventStatEntry.setPasspercent(getPercentStr(passEventCount, eventTotal));
        eventStatEntry.setMannualpercent(getPercentStr(manualReviewCount, eventTotal));
        eventStatEntry.setNotpasspercent(getPercentStr(refuseEventCount, eventTotal));
        eventStatEntry.setDate("总计");
        eventStatEntry.setName(String.valueOf(customereventEntryList.size()));
        eventStatEntry.setEventStatEntryList(customereventEntryList);

        return eventStatEntry;
    }

    public String getPercentStr(int x,int total){
    	 BigDecimal b = new BigDecimal((double)x/total);
		 int f1 = (int) (b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
		 String percentStr = String.valueOf(f1)+"%";
		return percentStr;
    }

	@Override
	public CustomerEventEntry getTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		int circle=Integer.valueOf((String) map.get("circle"));
		CustomerEventEntry customerEventEntry=null;
    	if (circle==0) {
	        List<CustomerEventEntry> eventEntries = customerEventEntryMapper.findByPage(map);
	        customerEventEntry = assembleData(eventEntries);
    	}else if (circle==1) {
    		 List<CustomerEventEntry> eventEntries = customerEventEntryMapper.findByPageday(map);
    		 customerEventEntry =assembleData(eventEntries);
		}else if (circle==2) {
			List<CustomerEventEntry> eventEntries = customerEventEntryMapper.findByPageweek(map);
			customerEventEntry = assembleData(eventEntries);
		}
    	
		return customerEventEntry;
	}
}

