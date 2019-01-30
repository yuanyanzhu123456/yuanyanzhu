package com.geo.rcs.modules.event.customerEvent.entity;

import java.io.Serializable;
import java.util.List;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年4月11日 下午7:08:05 
 */
public class CustomerEventEntry implements Serializable {
	private String date;
	private Integer pass;
	private Integer mannual;
	private Integer notpass;
	private String passpercent;
	private String mannualpercent;
	private String notpasspercent;
	private String name;
	private List<CustomerEventEntry> eventStatEntryList;
	
	public String getName() {
		return name;
	}
	public List<CustomerEventEntry> getEventStatEntryList() {
		return eventStatEntryList;
	}
	public void setEventStatEntryList(List<CustomerEventEntry> eventStatEntryList) {
		this.eventStatEntryList = eventStatEntryList;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasspercent() {
		return passpercent;
	}
	public void setPasspercent(String passpercent) {
		this.passpercent = passpercent;
	}
	public String getMannualpercent() {
		return mannualpercent;
	}
	public void setMannualpercent(String mannualpercent) {
		this.mannualpercent = mannualpercent;
	}
	public String getNotpasspercent() {
		return notpasspercent;
	}
	public void setNotpasspercent(String notpasspercent) {
		this.notpasspercent = notpasspercent;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getPass() {
		return pass;
	}
	public void setPass(Integer pass) {
		this.pass = pass;
	}
	public Integer getMannual() {
		return mannual;
	}
	public void setMannual(Integer mannual) {
		this.mannual = mannual;
	}
	public Integer getNotpass() {
		return notpass;
	}
	public void setNotpass(Integer notpass) {
		this.notpass = notpass;
	}
	
}
