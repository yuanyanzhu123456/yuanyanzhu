package com.geo.rcs.modules.source.client;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件信息
 * @author 吴淑佳
 *
 */
public class ReadConf {
	private static Map<String,Properties> properties = new HashMap<String,Properties>() ;
	private static String baseDir = "" + File.separator ;
	
	public static String getProperty(String key){
		return getProperty("geoclient.properties",key) ;
	}
	public static String getProperty(String propertiesFile,String key){
		initProperties(propertiesFile, "UTF-8") ;
		return properties.get(baseDir+propertiesFile).getProperty(key) ;
	}
	public static void initProperties(String propertiesFile, String charset){
		Properties prop = properties.get(baseDir+propertiesFile) ;
		if(prop == null){
			prop = getProperties(propertiesFile, charset) ;
			properties.put(baseDir+propertiesFile, prop) ;
		}
	}
	
	public static Properties getPropertiesInCache(String propertiesFile, String charset){
		initProperties(propertiesFile, charset) ;
		return properties.get(baseDir+propertiesFile) ;
	}
	
	/**
	 * 
	 * @param propertiesFile 路径可以是绝对路径，也可以是相对路径（相对于项目工程为根目录） ,绝对路径要以 / 开头
	 * @param charset
	 * @return
	 */
	public static Properties getProperties(String propertiesFile, String charset) {
		InputStream is = null;
		Properties p = null;
		InputStreamReader insReader = null;
		try {
			if(!propertiesFile.startsWith(File.separator)){
				String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() ;
				propertiesFile = classPath + baseDir + propertiesFile ;
			}
			is = new FileInputStream(propertiesFile);  // 创建字节输入流，路径可以是绝对路径，也可以是相对路径（相对于项目工程为根目录） 
			// BufferedReader bf = new BufferedReader(new
			// InputStreamReader(is));
			insReader = new InputStreamReader(is, charset);
			p = new Properties();
			p.load(insReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (insReader != null) {
				try {
					insReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return p;
	}
	
	public static String getBaseDir() {
		return baseDir;
	}
	public static void setBaseDir(String baseDir) {
		ReadConf.baseDir = baseDir;
	}
	public static void main(String[] args){
		System.out.println(getProperty("ClientID"));
	}
}
