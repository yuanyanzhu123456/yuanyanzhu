package com.geo.rcs.modules.source.client;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * DES加密介绍 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现 。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public class DES extends ByteHexStr {
	public DES() {
	}

	// 测试
	public static void main(String args[]) {
		// 待加密内容
		String str = "测试内容";
		// 密码，长度要是8的倍数
		String password = "qazwsxed";

		String result = DES.encrypt(str, password);
		System.out.println("加密后：" + result);

		// 直接将如上内容解密
		try {
			String decryResult = DES.decrypt(result, password);
			System.out.println("解密后：" + decryResult);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	/**
	 * 加密
	 * @param datasource
	 * @param password
	 * @return
	 */
    public static String encrypt(String datasource, String password){
    	byte[] result = null;
		try {
			result = encrypt(datasource.getBytes("UTF-8"), password);
		} catch (UnsupportedEncodingException e) {
			System.out.println("content:"+datasource+",password:"+password);
			e.printStackTrace();
		}
		String rs = parseByte2HexStr(result) ;
		System.out.println("encrypt;content:"+datasource+";password:"+password+";rs:"+rs);
		return rs ;
    }
	/**
	 * 加密
	 * 
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes("UTF-8"));
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 解密
	 * @param src
	 * @param password
	 * @return
	 */
	public static String decrypt(String src, String password) {
		byte[] decryResult = null;
		try {
			decryResult = decrypt(parseHexStr2Byte(src), password);
			String rs = new String(decryResult,"UTF-8") ;
			System.out.println("decrypt;content:"+src+";password:"+password+";rs:"+rs);
			return rs ;
		} catch (InvalidKeyException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (InvalidKeySpecException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (NoSuchPaddingException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (BadPaddingException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
            System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		} catch (RuntimeException e) {
			System.out.println("content:"+src+",password:"+password);
            e.printStackTrace();
		}
		return null ;
	}
	/**
	 * 解密
	 * 
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = null;
		try {
			desKey = new DESKeySpec(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}
}
