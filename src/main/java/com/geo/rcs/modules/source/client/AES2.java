package com.geo.rcs.modules.source.client;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
 
// import org.apache.commons.codec.binary.Base64;

/**
 * 适用于PHP对接时的AES加解密
 * @author wushujia
 *
 */
public class AES2 extends ByteHexStr {
    private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
    /**
     * 加密
     * @param plainText
     * @param keyStr 16个字符
     * @return
     */
    public static String encrypt(String plainText,String keyStr) { 
        byte[] encrypt = null; 
        try{
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            encrypt = cipher.doFinal(plainText.getBytes("UTF-8"));     
        }catch(Exception e){
        	System.out.println("content:"+plainText+",password:"+keyStr);
            e.printStackTrace(); 
        }
        // return new String(Base64.encodeBase64(encrypt)); 
        String rs = parseByte2HexStr(encrypt);
        System.out.println("encrypt;content:"+plainText+";password:"+keyStr+";rs:"+rs);
        return rs ;
    } 
    /**
     * 解密
     * @param encryptData
     * @param keyStr 16个字符
     * @return
     */
    public static String decrypt(String encryptData,String keyStr) {
        byte[] decrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.DECRYPT_MODE, key); 
            // decrypt = cipher.doFinal(Base64.decodeBase64(encryptData)); 
            decrypt = cipher.doFinal(parseHexStr2Byte(encryptData));
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
        try {
			String rs = new String(decrypt,"UTF-8") ;
			System.out.println("decrypt;content:"+encryptData+";password:"+keyStr+";rs:"+rs);
			return rs ;
		} catch (UnsupportedEncodingException e) {
			System.out.println("content:"+encryptData+",password:"+keyStr);
			e.printStackTrace();
			return "" ;
		} catch (RuntimeException e) {
			System.out.println("content:"+encryptData+",password:"+keyStr);
			e.printStackTrace();
			return "" ;
		}
    } 
 
    private static Key generateKey(String key)throws Exception{ 
        try{
        	SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES"); 
            return keySpec; 
        }catch(Exception e){
        	System.out.println("key:"+key);
        	e.printStackTrace();
            throw e; 
        } 
    } 
 
    public static void main(String[] args) {
         
        String keyStr = "UITN25LMUQC436IM";
 
        String plainText = "this is a string will be AES_Encrypt";
         
        String encText = encrypt(plainText ,keyStr);
        System.out.println(encText); 
        String decString = decrypt(encText ,keyStr); 
         
        
        System.out.println(decString); 
 
    } 
}