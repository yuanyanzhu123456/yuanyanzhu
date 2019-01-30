package com.geo.rcs.modules.source.client;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 
 * @author wushujia
 *
 */
public class ParseFile {
	public static void main(String[] args) {
		// 测试从Base64编码转换为图片文件
		String strImg = "";
		generateFile(strImg, "D:\\jiangliping.jpg");

		// 测试从图片文件转换为Base64编码
		System.out.println(getFileStr("d:\\jiangliping.jpg"));
	}
    /**
     * 将文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param filePath 文件全路径
     * @return 文件base64编码字符串
     */
	public static String getFileStr(String filePath) {
		byte[] data = null;
		// 读取文件字节数组
		try {
			InputStream in = new FileInputStream(filePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "" ;
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
    /**
     * 对字节数组字符串进行Base64解码并生成文件
     * @param fileStr 文件base64编码字符串
     * @param filePath 生成的文件全路径
     * @return
     */
	public static boolean generateFile(String fileStr, String filePath) {
		if (fileStr == null||"".equals(fileStr)) {
			return false;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(fileStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成文件
			OutputStream out = new FileOutputStream(filePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
