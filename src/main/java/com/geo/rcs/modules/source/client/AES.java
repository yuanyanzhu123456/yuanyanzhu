package com.geo.rcs.modules.source.client;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES 算法 对称加密，密码学中的高级加密标准 2005年成为有效标准
 * 
 */
public class AES extends ByteHexStr {
	public static void main(String[] args) {
		String content = "jshagdauiwqhi测试";
		String password = "123456sd78";
		String a = encrypt(content, password);
		System.out.println(a);
		String b = decrypt(a, password);
		System.out.println(b);
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static String encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");  // http://blog.csdn.net/jeamking/article/details/8904080   win 下正常 linux下加密结果随机
			random.setSeed(password.getBytes("UTF-8"));
			//kgen.init(128, new SecureRandom(password.getBytes("UTF-8")));
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			String rs = parseByte2HexStr(result); // 加密
			System.out.println("encrypt;content:"+content+";password:"+password+";rs:"+rs);
			return rs ;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("content:"+content+",password:"+password);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (InvalidKeyException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (BadPaddingException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static String decrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");  // http://blog.csdn.net/jeamking/article/details/8904080   win 下正常 linux下加密结果随机
			random.setSeed(password.getBytes("UTF-8"));
			//kgen.init(128, new SecureRandom(password.getBytes("UTF-8")));
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(content));
			String rs = new String(result,"UTF-8"); // 解密
			System.out.println("decrypt;content:"+content+";password:"+password+";rs:"+rs);
			return rs ;
		} catch (NoSuchAlgorithmException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (NoSuchPaddingException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (InvalidKeyException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (BadPaddingException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
            System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		} catch (RuntimeException e) {
			System.out.println("content:"+content+",password:"+password);
            e.printStackTrace();
		}
		return null;
	}
}
