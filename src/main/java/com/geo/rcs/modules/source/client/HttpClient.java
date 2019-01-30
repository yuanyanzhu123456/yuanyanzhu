package com.geo.rcs.modules.source.client;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * http 或 https 请求工具类
 * wushujia
 */
public class HttpClient {
	private static ThreadLocal<Integer> httpState = new ThreadLocal<Integer>();  // http 本次请求返回的状态码
	private static ThreadLocal<String> httpCookieString = new ThreadLocal<String>();  // http 本次请求返回的cookie   getRs 用这个方法时有效
	
	/**
	 * 最好能在过滤器里面调用下该方法，可以减少内存消耗
	 */
	public static void removeThreadLocal(){
		httpState.remove();
		httpCookieString.remove();
	}
	/**
	 * @return
	 */
	public static Integer getHttpState(){
		Integer state = httpState.get();
		// httpState.remove();
		return state ;
	}

	public static String getHttpCookieString(){
		String cookies = httpCookieString.get() ;
		// httpCookieString.remove();
		return cookies ;
	}
	public static void main(String[] args){
		try {
			System.out.println(getRs("http://127.0.0.1:8080/taskmeta/getview/web/o/login","username=test1&password=test1","UTF-8","UTF-8","POST",10000,10000,null));
			String cookies = httpCookieString.get() ;
			System.out.println(cookies);
			System.out.println(getRs("http://127.0.0.1:8080/taskmeta/getview/web/u/getuseraction","username=test1&password=test1&uno=200002&dsign=1","UTF-8","UTF-8","POST",10000,10000,null,cookies,null,"",""));
			cookies = httpCookieString.get() ;
			System.out.println(cookies);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String readCookies(URLConnection url_con){
		StringBuilder cookieSb = new StringBuilder();
		List<String> cookieList = url_con.getHeaderFields().get("Set-Cookie"); // 响应cookie头
		if(cookieList != null){
			for(String cookie : cookieList){
				//cookieSb.append(cookie).append(";") ;
				
				if(cookie!=null&&!"".equals(cookie)){
					int p = cookie.indexOf("=");
					if(p>0){
						String key = cookie.substring(0, p);
						int vp = cookie.indexOf(";") ;
						if(vp<=0){
							vp = cookie.length() ;
						}
						String value = cookie.substring(p+1, vp) ;
						cookieSb.append(key).append("=").append(value).append(";") ;
					}
				}
				
			}
		}
		return cookieSb.toString() ;
	}
	public static String getRs(String path, String params, String reqencoding,
			String respencoding, String requestMethod,Integer httpConnectTimeout,Integer httpReadTimeout,Map<String, String> headerMap) throws Exception {
		return getRs(path, params, reqencoding, respencoding, requestMethod,httpConnectTimeout,httpReadTimeout,headerMap,null,null,"","") ;
	}
	/**
	 * 从url获取结果
	 * // 初始化proxy对象
	   Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
	   
	   // 创建连接
	   URL url = new URL(url);
	   URLConnection connection = url.openConnection(proxy);
	 * @param path URL 支持http和https
	 * @param params parname1=parvalue1&parname2=parvalue2
	 * @param reqencoding UTF-8 GBK
	 * @param respencoding UTF-8 GBK
	 * @param requestMethod POST|GET
	 * @param httpConnectTimeout 连接超时时间，单位毫秒
	 * @param httpReadTimeout 读取数据超时时间，单位毫秒
	 * @param headerMap http请求头部信息 如果params参数想通过流传输那么需要在header里面添加httpStreamStr
	 * @param cookies a=1;b=2;
	 * @return
	 * @throws Exception 
	 */
	public static String getRs(String path, String params, String reqencoding,
			String respencoding, String requestMethod,Integer httpConnectTimeout,Integer httpReadTimeout,Map<String, String> headerMap,String cookies,Proxy proxy,String proxyUser,String proxyPwd) throws Exception {
		long a = System.currentTimeMillis();
		httpState.set(null); // 初始化
		httpCookieString.set(null); // 初始化
		
		URL url = null;
		URLConnection connection = null;
		try {
			// 如有中文一定要加上，在接收方用相应字符转码即可 ,如果这里直接转换那么=和&也会一起转换
			if(headerMap==null||!headerMap.containsKey("httpStreamStr")){
				params = trsPara(params, reqencoding);
			}
			url = new URL(path);
			// 设置代理
			if(proxy!=null){
				connection = url.openConnection(proxy);
				if(!"".equals(proxyUser)&&proxyUser!=null){
					//以下三行是在需要验证时，输入帐号密码信息 "Proxy-Authorization"="Basic Base64.encode(user:password)"
	                String headerkey = "Proxy-Authorization";  
	                String headerValue = "Basic "+(new String(Base64.getEncoder().encode((proxyUser+":"+proxyPwd).getBytes("UTF-8")),"UTF-8")); //帐号密码用:隔开，base64加密方式
	                connection.setRequestProperty(headerkey, headerValue);
				}
			}else{
				connection = url.openConnection(); // 新建连接实例
			}
			
			if (connection instanceof HttpsURLConnection){
				((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
					@Override
				    public boolean verify(String hostname, SSLSession session) {
				      return true;  // 不验证证书
				    }
				  });
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());

				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				((HttpsURLConnection) connection).setSSLSocketFactory(ssf);
				((HttpsURLConnection) connection).setRequestMethod(requestMethod);// 提交方法POST|GET
			}else{
				((HttpURLConnection)connection).setRequestMethod(requestMethod);// 提交方法POST|GET
			}
						
			connection.setRequestProperty("Connection", "close"); //不进行持久化连接
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
			if(cookies!=null&&!"".equals(cookies)&&!"null".equalsIgnoreCase(cookies)){
				connection.setRequestProperty("Cookie", cookies);
			}
			if (headerMap != null) {
				Set<String> set = headerMap.keySet() ;
				if(set != null){
					for (String k : set) {
						connection.setRequestProperty(k, headerMap.get(k));
					}
				}
			}
			
			if(httpConnectTimeout == null){
				httpConnectTimeout = 10000 ;
			}
			connection.setConnectTimeout(httpConnectTimeout);// 设置连接超时时间，单位毫秒
			if(httpReadTimeout == null){
				httpReadTimeout = 10000 ;
			}
			connection.setReadTimeout(httpReadTimeout);// 设置读取数据超时时间，单位毫秒
			connection.setDoInput(true);// 是否打开输出流 true|false
			connection.setDoOutput(true);// 是否打开输入流 true|false
			
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection
					.getOutputStream());
			
			if(headerMap!=null&&headerMap.containsKey("httpStreamStr")){
				params = params + "";
				out.write(params.getBytes(reqencoding), 0, params.length());
			}else{
				out.writeBytes(params);
			}
			
			out.flush();
			out.close();
			BufferedReader reader = null ;
			String encode = "" ;
			try{
				
				InputStream in = null;
				if (connection.getContentEncoding()!=null&&!"".equals(connection.getContentEncoding())&&!"null".equals(connection.getContentEncoding())) {
	                encode = connection.getContentEncoding().toLowerCase();
	                if (encode!=null && encode.indexOf("gzip") >= 0) {
	                    in = new GZIPInputStream(connection.getInputStream()) ;
	                }
	            }
				if (null == in) {
	                in = connection.getInputStream();
	            }
				
				reader = new BufferedReader(new InputStreamReader(in, respencoding));
			}catch(Exception e){
				e.printStackTrace();
			}
			StringBuilder buffer = new StringBuilder();
			String line = "";
			String rs = "" ;
			if(reader!=null){
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append("\n");
				}
				reader.close();
				// 去掉最后一个换行符
				if(buffer.length()>1){
					rs = buffer.substring(0,buffer.length()-1) ;
				}
			}
			int state = 0 ;
			if (connection instanceof HttpsURLConnection){
				state = ((HttpsURLConnection) connection).getResponseCode();
			}else{
				state = ((HttpURLConnection) connection).getResponseCode();
			}
			httpState.set(state);
			String ck = readCookies(connection);
			httpCookieString.set(ck);
			// rs = rs.replace("&nbsp;", " ");
			System.out.println(path + "?" + params + "@" + headerMap + "@" + cookies + ";状态码:" + state + ";gzip:"+encode+";返回结果:" + rs + ";cookies:" + ck + "##耗时:"+(System.currentTimeMillis()-a));
			return rs;
		} catch (Exception e) {
			System.out.println(path + "?" + params + "@" + headerMap + "@" + cookies);
			e.printStackTrace();
			throw e;
//			return "" ;
		} finally {
			if (connection instanceof HttpsURLConnection){
				((HttpsURLConnection)connection).disconnect(); // 关闭连接
			}else{
				((HttpURLConnection)connection).disconnect(); // 关闭连接
			}
		}
	}
	
	/**
	 * 处理参数
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String trsPara(String params, String reqencoding)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		if (params == null || "".equals(params)) {
			return "";
		} else {
			String[] ps = params.split("&");
			for (int i = 0, l = ps.length; i < l; i++) {
				if (ps[i] == null) {
					continue;
				} else {
					String[] ps1 = ps[i].split("=");
					if (ps1.length != 2) {
						continue;
					} else {
						sb.append("&").append(ps1[0]).append("=").append(
								URLEncoder.encode(ps1[1], reqencoding));
					}
				}
			}
			return sb.length() <= 1 ? "" : sb.toString().substring(1);
		}
	}

	
	private static class MyX509TrustManager implements X509TrustManager {

		/*
		 * The default X509TrustManager returned by SunX509. We’ll delegate
		 * decisions to it, and fall back to the logic in this class if the default
		 * X509TrustManager doesn’t trust it.
		 */
		X509TrustManager sunJSSEX509TrustManager;

		MyX509TrustManager() throws Exception {
			// create a “default” JSSE X509TrustManager.

			KeyStore ks = KeyStore.getInstance("JKS");
			//ks.load(new FileInputStream("trustedCerts"), "passphrase".toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509",
					"SunJSSE");

			tmf.init(ks);

			TrustManager tms[] = tmf.getTrustManagers();

			/*
			 * Iterate over the returned trustmanagers, look for an instance of
			 * X509TrustManager. If found, use that as our “default” trust manager.
			 */
			for (int i = 0; i < tms.length; i++) {
				if (tms[i] instanceof X509TrustManager) {
					sunJSSEX509TrustManager = (X509TrustManager) tms[i];
					return;
				}
			}

			/*
			 * Find some other way to initialize, or else we have to fail the
			 * constructor.
			 */
			throw new Exception("Couldn’t initialize");
		}

		/*
		 * Delegate to the default trust manager.
		 */
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
			try {
				sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
			} catch (CertificateException excep) {
				// do any special handling here, or rethrow exception.
				excep.printStackTrace();
			}
		}

		/*
		 * Delegate to the default trust manager.
		 */
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
			try {
				sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
			} catch (CertificateException excep) {
				/*
				 * Possibly pop up a dialog box asking whether to trust the cert
				 * chain.
				 */
				// excep.printStackTrace();
			}
		}

		/*
		 * Merely pass this through.
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return sunJSSEX509TrustManager.getAcceptedIssuers();
		}
	}
}
