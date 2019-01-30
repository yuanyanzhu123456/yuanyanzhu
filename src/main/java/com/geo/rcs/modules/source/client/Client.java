package com.geo.rcs.modules.source.client;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;

import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 数据请求client
 * wushujia
 */
public class Client {
	private String server = "http://127.0.0.1:8180" ; // http://yz.geotmt.com、https://yz.geotmt.com
	private int encrypted = 1 ; // 是否加密传输
	private String encryptionType = "AES" ; // AES(秘钥长度不固定)、DES(秘钥长度8)、DESede(秘钥长度24)
	private String encryptionKey = "123456789" ; // 加密类型和加密秘钥向GEO索取(如果是获取数据的时候传的是RSA那么这里自己定义即可)
	
	private String username = "test12" ; // 账户向GEO申请开通
	private String password = "test12" ;
	private String uno = "200206" ;
	private String etype = "" ;
	private int dsign = 0 ; // 是否进行数字签名 1是0否
	public String getEtype() {
		return etype;
	}

	public void setEtype(String etype) {
		this.etype = etype;
	}

	public int getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(int encrypted) {
		this.encrypted = encrypted;
	}

	public String getServer() {
		return server;
	}

	public String getEncryptionType() {
		return encryptionType;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUno() {
		return uno;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUno(String uno) {
		this.uno = uno;
	}
	
	public int getDsign() {
		return dsign;
	}

	public void setDsign(int dsign) {
		this.dsign = dsign;
	}

	private static final int httpConnectTimeout = 10000 ;
	private static final int httpReadTimeout = 10000 ;
	private final Map<String,String> tokenIdMap = Token.getInstance().getTokenIdMap() ;
	private final Map<String,Long> getTokenTimeMap = Token.getInstance().getGetTokenTimeMap() ;
	private final Map<String,String> digitalSignatureKeyMap = Token.getInstance().getDigitalSignatureKeyMap() ;
	
	private static final long tokenCycle = 86400000l ;
	private static final long tokenCyc = 35000l ;  // 避免高并发时刚好token过期造成同时多个线程一起申请新token，如果在此时间内有过更新那么直接返回内存里面的token
	public static void main(String[] args) throws Exception{
		Client client = new Client();
		String path = client.server+"/civp/getview/api/u/queryUnify" ;
		Map<String,String> params = new HashMap<String,String>();
		params.put("innerIfType", "B8,A2") ;
		params.put("cid", "17702166519") ;
		params.put("idNumber", "460006198912180030") ;
		params.put("realName", "张三") ;
		params.put("authCode", "11111111111111111111111111111111") ;
		String data = client.getData(path,params,"civp") ;
		System.out.println(data);
	}
	public String getData(String path,Map<String,String> params,String sys) throws Exception{
		return getData(path,params,sys,httpConnectTimeout,httpReadTimeout) ;
	}
	/**
	 * 
	 * @param params
	 * @param sys
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> encryptAndDigitalSignature(Map<String,String> params,String sys) throws Exception{
		Map<String,String> headers = new HashMap<String,String>() ;
		TreeMap<String,String> paraMap = new TreeMap<String,String>() ;
		String tokenId = getToken(true) ;
		StringBuilder sb = new StringBuilder() ;
		if (params != null) {
			Set<String> set = params.keySet() ;
			for (String k : set) {
				String value = params.get(k) ;
				if(dsign==1){
					paraMap.put(k, value) ;
				}
				if(encrypted == 1){
					// value = Secret.encrypt(encryptionType,value, encryptionKey)  ; // 加密
					value = encrypt(value, sys) ; // 加密
				}
				sb.append(k).append("=").append(value).append("&") ;
			}
		}
		if(dsign==1){
			paraMap.put("tokenId", tokenId) ;
			String digitalSignature = DigitalSignature.clientDigitalSignature(paraMap, null, "", digitalSignatureKeyMap.get(username)) ;
			headers.put("digitalSignature", digitalSignature) ;
		}
		String newParams = sb.toString()+"tokenId="+tokenId ;
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("para", newParams) ;
		result.put("headers", headers) ;
		return result ;
	}
	/**
	 * 该方法可以通过用户名密码直接调用数据接口，且token过期会自动重新获取
	 * @param path
	 * @param params
	 * @param sys 
	 * @param httpConnectTimeout 毫秒
	 * @param httpReadTimeout 毫秒
	 * @return
	 * @throws Exception 
	 */
	public String getData(String path,Map<String,String> params,String sys,int httpConnectTimeout,int httpReadTimeout) throws Exception{
		Map<String,String> headers = new HashMap<String,String>() ;
		TreeMap<String,String> paraMap = new TreeMap<String,String>() ;
		String tokenId = getToken(true) ;  // 等计费功能上线后第三个参数才能填RSA不然都只能是空
		StringBuilder sb = new StringBuilder() ;
		if (params != null) {
			Set<String> set = params.keySet() ;
			for (String k : set) {
				String value = params.get(k) ;
				if(dsign==1){
					paraMap.put(k, value) ;
				}
				if(encrypted == 1){
					// value = Secret.encrypt(encryptionType,value, encryptionKey)  ; // 加密
					value = encrypt(value, sys) ; // 加密
				}
				sb.append(k).append("=").append(value).append("&") ;
			}
		}
		if(dsign==1){
			paraMap.put("tokenId", tokenId) ;
			String digitalSignature = DigitalSignature.clientDigitalSignature(paraMap, null, "", digitalSignatureKeyMap.get(username)) ;
			headers.put("digitalSignature", digitalSignature) ;
		}
		String newParams = sb.toString()+"&tokenId="+tokenId ;
		String data = getDataByTokenId(path,newParams,headers,sys,httpConnectTimeout,httpReadTimeout) ;
		if(data!=null&&!"".equals(data)){
			Map<String,Object> map = JSON.parseObject(data, Map.class) ;
			String code = map.get("code")+"" ;
			if("-100".equals(code)||"-200".equals(code)||"-300".equals(code)){
				System.out.println("tokenId无效重新获取tokenId");
				tokenId = getToken(false) ;  // 等计费功能上线后第三个参数才能填RSA不然都只能是空
				if(dsign==1){
					paraMap.put("tokenId", tokenId) ;
					String digitalSignature = DigitalSignature.clientDigitalSignature(paraMap, null, "", digitalSignatureKeyMap.get(username)) ;
					headers.put("digitalSignature", digitalSignature) ;
				}
				newParams = sb.toString()+"&tokenId="+tokenId ;
				data = getDataByTokenId(path,newParams,headers,sys,httpConnectTimeout,httpReadTimeout) ;
			}
		}
		return data ;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param etype
	 * @param first
	 * @return
	 * @throws Exception 
	 */
	public String getToken(boolean first) throws Exception{
		if(first){
			if(getNewToken()){
				// 走网络获取token需要同步
				synchronized (username.intern()) {
					return getToken() ; 
				}
			}else{
				return tokenIdMap.get(username) ;
			}
		}else{
			synchronized (username.intern()) {
				Long tokenTime = getTokenTimeMap.get(username) ;
				long getTokenTime = tokenTime==null ? 0l : tokenTime;
				if(System.currentTimeMillis()-getTokenTime>=tokenCyc){
					// 避免高并发
					tokenIdMap.put(username, "") ; // 使token失效
				}
				String tokenId = getToken() ;
				return tokenId ;
			}
		}
	}
    /**
     * 获取数据访问权限,该接口24小时请求一次即可！
     * @param username
     * @param password
     * @param etype 加密类型和秘钥用什么方式进行加密
     * @return token
     * @throws Exception 
     */
	public String getToken() throws Exception{
		if(getNewToken()){
			String path = server+"/civp/getview/api/o/login" ;
			// 加密用户名密码
			String eUsername = username ;  
			String ePassword = password ;
			String eDsign = dsign+"" ;
			if(encrypted == 1){
				/*
				eUsername = Secret.encrypt(encryptionType,username, encryptionKey) ;  
				ePassword = Secret.encrypt(encryptionType,password, encryptionKey) ;
				eDsign = Secret.encrypt(encryptionType,dsign+"", encryptionKey) ;
				*/
				eUsername = encrypt(username) ;
				ePassword = encrypt(password) ;
				eDsign = encrypt(dsign+"") ;
			}
			String params = "" ;
			if("RSA".equalsIgnoreCase(etype)){
				KeyPair keyPair = RSAUtils.getKeyPair();
				String paraEncryptionType = RSAUtils.encrypt(keyPair.getPublic(), encryptionType) ;
				String paraEncryptionKey = RSAUtils.encrypt(keyPair.getPublic(), encryptionKey) ;
				params = "username="+eUsername+"&password="+ePassword+"&etype="+etype+"&encryptionType="+paraEncryptionType+"&encryptionKey="+paraEncryptionKey+"&encrypted="+encrypted+"&dsign="+eDsign ;
			}else{
				params = "username="+eUsername+"&password="+ePassword+"&uno="+uno+"&encrypted="+encrypted+"&dsign="+eDsign ;
			}
			String reqencoding = "UTF-8" ;
			String respencoding = "UTF-8" ;
			String requestMethod = "POST" ;
			Map<String,String> headerMap = null ;
			String rs=null;
			try {
				rs = HttpClient.getRs(path, params, reqencoding, respencoding, requestMethod, httpConnectTimeout, httpReadTimeout, headerMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (e instanceof UnknownHostException) {
					throw new RcsException(StatusCode.GEO_UNKNOWNHOST_ERROR.getMessage(),e);
				}else {
					throw e;
				}
			}
			if(rs!=null&&!"".equals(rs)){
				// 解密返回值
				if(rs.startsWith("{")){
					System.out.println("未加密:"+rs);
				}else{
					// rs = Secret.decrypt(encryptionType,rs, encryptionKey) ;
					rs = decrypt(rs) ;
					System.out.println("解密:"+rs);
				}
				Map<String,Object> map = JSON.parseObject(rs, Map.class) ;
				if("200".equals(map.get("code")+"")){
					long getTokenTime = System.currentTimeMillis() ;
					String tokenId = map.get("tokenId")+"" ;
					tokenIdMap.put(username, tokenId) ;
					getTokenTimeMap.put(username, getTokenTime) ;
					Map data = (Map)map.get("data") ;
					if(data!=null){
						String digitalSignatureKey = (String)data.get("digitalSignatureKey") ;
						if(digitalSignatureKey!=null){
							digitalSignatureKeyMap.put(username, digitalSignatureKey) ;
						}
					}
					return tokenId ;
				}else{
					System.out.println("登录失败!code="+map.get("code"));
					return "" ;
				}
			}else{
				return "" ;
			}
		}else{
			return tokenIdMap.get(username) ;
		}
	}
	/**
	 * 获取数据
	 * @param path
	 * @param params
	 * @param headers
	 * @param sys
	 * @param httpConnectTimeout
	 * @param httpReadTimeout
	 * @return
	 * @throws Exception 
	 */
	public String getDataByTokenId(String path,String params,Map<String,String> headers,String sys,int httpConnectTimeout,int httpReadTimeout) throws Exception{
		String reqencoding = "UTF-8" ;
		String respencoding = "UTF-8" ;
		String requestMethod = "POST" ;
		
		String rs=null;
		try {
			rs = HttpClient.getRs(path, params, reqencoding, respencoding, requestMethod, httpConnectTimeout, httpReadTimeout, headers);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RcsException(StatusCode.GEO_UNKNOWNHOST_ERROR.getMessage(),e);
		}
		if(rs!=null&&!"".equals(rs)){
			// 解密返回值
			if(rs.startsWith("{")){
				System.out.println("未加密:"+rs);
			}else{
				// rs = Secret.decrypt(encryptionType,rs, encryptionKey) ;
				rs = decrypt(rs, sys) ;
				System.out.println("解密:"+rs);
			}
			Map<String,Object> map = JSON.parseObject(rs, Map.class) ;
			/*
			if(!"200".equals(map.get("code")+"")){
				System.out.println("数据请求失败!code="+map.get("code"));
			}
			*/
		}
		return rs ;
	}
	/**
	 * 登出接口
	 * @return
	 * @throws Exception 
	 */
	public String loginOut(String sys) throws Exception{
		String tokenId = tokenIdMap.get(username) ;
		if(tokenId!=null&&!"".equals(tokenId)){
			String digitalSignatureKey = digitalSignatureKeyMap.get(username);
			Map<String,String> headers = new HashMap<String,String>() ;
			if(digitalSignatureKey!=null&&!"".equals(digitalSignatureKey)){
				TreeMap<String,String> paraMap = new TreeMap<String,String>() ;
				paraMap.put("tokenId", tokenId) ;
				String digitalSignature = DigitalSignature.clientDigitalSignature(paraMap, null, "", digitalSignatureKeyMap.get(username)) ;
				headers.put("digitalSignature", digitalSignature) ;
			}
			return loginOut(tokenId,headers,sys) ;
		}
		System.out.println("用户未登录");
		return "" ;
	}
	/**
	 * 登出(该token权限将被回收),该接口一般不需要调用
	 * @param tokenId
	 * @return
	 * @throws Exception 
	 */
	public String loginOut(String tokenId,Map<String,String> headerMap,String sys) throws Exception{
		String path = server+"/civp/getview/api/u/logout" ;
		String params = "tokenId="+tokenId ;
		String reqencoding = "UTF-8" ;
		String respencoding = "UTF-8" ;
		String requestMethod = "POST" ;
		String rs = HttpClient.getRs(path, params, reqencoding, respencoding, requestMethod, httpConnectTimeout, httpReadTimeout, headerMap) ;
		if(rs!=null&&!"".equals(rs)){
			// 解密返回值
			if(rs.startsWith("{")){
				System.out.println("未加密:"+rs);
			}else{
				// rs = Secret.decrypt(encryptionType,rs, encryptionKey) ;
				rs = decrypt(rs, sys) ;
				System.out.println("解密:"+rs);
			}
			Map<String,Object> map = JSON.parseObject(rs, Map.class) ;
			if("200".equals(map.get("code")+"")){
				System.out.println("退出成功");
				tokenIdMap.remove(username) ;
				getTokenTimeMap.remove(username) ;
				digitalSignatureKeyMap.remove(username) ;
			}else{
				System.out.println("退出失败");
			}
		}
		return rs ;
	}
	/**
	 * 加密,登陆接口
	 * @param text
	 * @return
	 */
	private String encrypt(String text){
		return encrypt(text,"civp") ;
	}
	/**
	 * 加密
	 * @param sys
	 * @return
	 */
	private String encrypt(String text,String sys){
		text = Secret.encrypt(encryptionType,text, encryptionKey) ;
		return text ;
	}
	/**
	 * 解密,登陆接口
	 * @param rs
	 * @return
	 * @throws Exception 
	 */
	private String decrypt(String rs) throws Exception{
		return decrypt(rs,"civp") ;
	}
	/**
	 * 解密
	 * @param rs
	 * @param sys
	 * @return
	 * @throws Exception 
	 */
	private String decrypt(String rs,String sys) throws Exception{
		rs = Secret.decrypt(encryptionType,rs, encryptionKey) ;
		return rs ;
	}
	
	/**
	 * 判定token是否还有效
	 * @return
	 */
	public boolean getNewToken(){
		Long tokenTime = getTokenTimeMap.get(username) ;
		long getTokenTime = tokenTime==null ? 0l : tokenTime;
		return tokenIdMap.get(username)==null||"".equals(tokenIdMap.get(username))||System.currentTimeMillis()-getTokenTime>=tokenCycle ;
	}

	public String rpad(String str, int strLength, char chr) {
		int strLen = str.length();
		if (strLen < strLength) {
			StringBuffer sb = new StringBuffer(str);
			while (strLen < strLength) {
				sb.append(chr); // 右补0
				strLen = sb.length();
			}
			str = sb.toString();
		}else{
			str = str.substring(0, strLength) ;
		}
		return str;
	}

	public String rpad(String str, int strLength) {
		return rpad(str, strLength, '0');
	}
}
