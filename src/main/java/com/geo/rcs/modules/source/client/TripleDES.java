package com.geo.rcs.modules.source.client;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class TripleDES extends ByteHexStr {

    public static final String Key = "123456781234567811122235" ; // 24位
    private static final String Algorithm = "DESede";  //定义 加密算法,可用 DES,DESede,Blowfish
    /**
     * 加密
     * @param src
     * @param keybyte
     * @return
     */
    public static String encrypt(String src,String keybyte){
    	try {
			String rs = parseByte2HexStr(encrypt(src.getBytes("UTF-8"),keybyte.getBytes("UTF-8"))) ;
			System.out.println("encrypt;content:"+src+";password:"+keybyte+";rs:"+rs);
			return rs ;
		} catch (UnsupportedEncodingException e) {
			System.out.println("src:"+src+";keybyte:"+keybyte);
			e.printStackTrace();
		}
		return null ;
    }
    // 加密字符串
    public static byte[] encrypt(byte[] src,byte[] keybyte) {
        try { // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
        	e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
        	e2.printStackTrace();
        } catch (Exception e3) {
        	e3.printStackTrace();
        }
        return null;
    }
    /**
     * 解密
     * @param src
     * @param keybyte
     * @return
     */
    public static String decrypt(String src,String keybyte){
    	try {
			String rs = new String(decrypt(parseHexStr2Byte(src),keybyte.getBytes("UTF-8")),"UTF-8");
			System.out.println("decrypt;content:"+src+";password:"+keybyte+";rs:"+rs);
			return rs ;
		} catch (UnsupportedEncodingException e) {
			System.out.println("src:"+src+";keybyte:"+keybyte);
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.out.println("src:"+src+";keybyte:"+keybyte);
			e.printStackTrace();
		}
		return null ;
    }
    // 解密字符串
    public static byte[] decrypt(byte[] src,byte[] keybyte) {
        try { // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
        	e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
        	e2.printStackTrace();
        } catch (Exception e3) {
        	e3.printStackTrace();
        }
        return null;
    }
 
    public static void main(String[] args) { 
    	// 添加新安全算法,如果用JCE就要把它添加进去
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
        final String k = Key;    //8字节的密钥
        String szSrc = "测试内容";
        System.out.println("加密前的字符串:" + szSrc);
        String encoded = encrypt(szSrc,k);
        System.out.println("加密后的字符串:" + encoded);
        String srcBytes = decrypt(encoded,k);
        System.out.println("解密后的字符串:" + srcBytes);
    }

}
