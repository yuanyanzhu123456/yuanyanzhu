package com.geo.rcs.modules.source.client;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * 原理：XOR异或运算
 * @author wushujia
 *
 */
public class XOREncrypt {
	private static final int RADIX = 16;
    /**
     * 加密
     * @param content
     * @param seed 只能是数字
     * @return
     */
	public static final String encrypt(String content,String seed) {
		if (content == null){
			return "";
		}
		if (content.length() == 0){
			return "";
		}
		BigInteger bi_content = null;
		try {
			bi_content = new BigInteger(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		BigInteger bi_r0 = new BigInteger(seed);
		BigInteger bi_r1 = bi_r0.xor(bi_content);
		String rs = bi_r1.toString(RADIX);
		System.out.println("encrypt;content:"+content+";password:"+seed+";rs:"+rs);
		return rs ;
	}
    /**
     * 解密
     * @param encrypted
     * @param seed 只能是数字
     * @return
     */
	public static final String decrypt(String encrypted,String seed) {
		if (encrypted == null){
			return "";
		}
		if (encrypted.length() == 0){
			return "";
		}
		BigInteger bi_confuse = new BigInteger(seed);
		try {
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);
			String rs = new String(bi_r0.toByteArray(),"UTF-8");
			System.out.println("decrypt;content:"+encrypted+";password:"+seed+";rs:"+rs);
			return rs ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String args[]){
		String seed = "5623154561113129614987132134943111" ;
		System.out.println(encrypt("sddsds",seed));
		System.out.println(decrypt(encrypt("sddsds",seed),seed));
		System.out.println(encrypt("你好",seed));
		System.out.println(decrypt(encrypt("你好",seed),seed));
	}
}
