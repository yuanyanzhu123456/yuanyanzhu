package com.geo.rcs.modules.source.client;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
//import org.apache.commons.codec.binary.Base64;

/**
 * 1024位key的最多只能加密117位数据   计算方式为1024/8-11=117
 * 对于超长数据的加密可以使用对称加密DES、3DES、AES等
 * 公钥加密私钥解密
 * 私钥加密公钥解密
 * @author 吴淑佳
 *
 */
public final class RSAUtils {
	private static final Provider PROVIDER = new BouncyCastleProvider();
   
	private static final int KEY_SIZE = 2048 ;
	private static KeyPair staticKeyPair ;
	public static int getKeySize() {
		return KEY_SIZE;
	}
	private RSAUtils() {
	}
	/**
     * 随机生成公钥私钥
     * @return
	 * @throws NoSuchAlgorithmException 
     */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		long st = System.currentTimeMillis() ;
		KeyPair keyPair = null ;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
		System.out.printf("生成随机公私钥耗时:"+(System.currentTimeMillis()-st));
		return keyPair ;
	}
	/**
	 * 生成固定公钥私钥
	 * @return
	 * @throws Exception 
	 */
	public static KeyPair getKeyPair(String RSAmodulus,String RSApublicExponent,String RSAprivateExponent) throws Exception {
		long st = System.currentTimeMillis() ;
		RSAPublicKey publicKey = null ;
		if(RSApublicExponent!=null&&!"".equals(RSApublicExponent)){
			publicKey = getPublicKey(RSAmodulus,RSApublicExponent) ;
		}
		RSAPrivateKey privateKey = null ;
        if(RSAprivateExponent!=null&&!"".equals(RSAprivateExponent)){
        	privateKey = getPrivateKey(RSAmodulus,RSAprivateExponent) ;
        }
        KeyPair keyPair = new KeyPair(publicKey,privateKey);
        System.out.printf("生成固定公私钥耗时:"+(System.currentTimeMillis()-st));
        return keyPair ;
	}
	public static KeyPair getKeyPair() throws Exception {
		if(staticKeyPair!=null){
			return staticKeyPair ;
		}else{
			//模  
	        String RSAmodulus = "17590361768550206211304296020909443672497991496756109773322252259013465286845579413752496184027248059820980704679469737118666075144537820150739324451689607094439709077353019826775495958346915123823636420712765173032060048441941353150612472037329023471233201526271263321748209433499510445056447198407665583194128929575382802662715348893612504772549730627496131956857340581560167388183512266198528481499308939895214866963512452810966483935049796734695702115469855329855747438056741212320013011404385827781345796864627964307106735940995903236433032449374592345961492652542537539286775674624957345808007153510920388584373" ;
	    	//公钥指数  
	        String RSApublicExponent = "65537" ;
	        //私钥指数
	        String RSAprivateExponent = "" ;
	        staticKeyPair = getKeyPair(RSAmodulus, RSApublicExponent, RSAprivateExponent) ;
	        return staticKeyPair ;
		}
	}
	/**
	 * 加密
	 * @param publicKey
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public static byte[] encrypt(Key publicKey, byte[] data) throws Exception {
		try {
			//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
     * 加密
     * @param publicKey
     * @param text
     * @return
	 * @throws Exception 
     */
	public static String encrypt(Key publicKey, String text) throws Exception {
		long st = System.currentTimeMillis() ;
		byte[] data = null;
		try {
			data = encrypt(publicKey, text.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
		// return data != null ? Base64.encodeBase64String(data) : null;
		String encryptValue = data != null ? parseByteToHexStr(data) : null;
		System.out.printf("加密:"+text+";耗时:"+(System.currentTimeMillis()-st));
		return encryptValue ;
	}
    /**
     * 
     * @param privateKey
     * @param data
     * @return
     * @throws Exception 
     */
	public static byte[] decrypt(Key privateKey, byte[] data) throws Exception {
		/*
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			//Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		*/
		
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;
			while (data.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(data, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	/**
     * 解密
     * @param privateKey
     * @param text
     * @return
	 * @throws Exception 
     */
	public static String decrypt(Key privateKey, String text) throws Exception {
		// byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		long st = System.currentTimeMillis() ;
		String decryptValue = null ;
		byte[] data = decrypt(privateKey, hexStringToBytes(text));
		try {
			decryptValue = data != null ? new String(data,"UTF-8") : null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		System.out.printf("解密:"+text+";耗时:"+(System.currentTimeMillis()-st));
		return decryptValue ;
	}
	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByteToHexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	/** * 16进制 To byte[] * @param hexString * @return byte[] 
	 * 
	 * js前台使用公钥加密，后台java解密
            问题是：
     String result = request.getParameter("encryedPwd"); byte[] en_result = new BigInteger(result, 16).toByteArray(); 
         这里:byte[] en_result = new BigInteger(result, 16).toByteArray();  
         有的时候你会发现数组长度129，第一个元素为0，这肯定是不正确的！
         解决办法自己写一个hex to byte[]的方法，，就不出现上面的问题了。
	 * 
	 * */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/** * Convert char to byte * @param c char * @return byte */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
    /** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     * @throws Exception 
     */  
    public static RSAPublicKey getPublicKey(String modulus, String exponent) throws Exception {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
        	e.printStackTrace();
			throw e;
        }  
    }  
  
    /** 
     * 使用模和指数生成RSA私钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     * @throws Exception 
     */  
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) throws Exception {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
        	e.printStackTrace();
			throw e;
        }  
    }
//    /**
//     * 随机生成公钥私钥
//     * @return
//     */
//    public static KeyPair generateKeyPair() {
//    	long st = System.currentTimeMillis() ;
//    	KeyPair keyPair = null ;
//    	try {
//    		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
//    		keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
//    		keyPair = keyPairGenerator.generateKeyPair();
//    	} catch (NoSuchAlgorithmException e) {
//    		e.printStackTrace();
//    	}
//    	System.out.printf("生成随机公私钥耗时:"+(System.currentTimeMillis()-st));
//    	return keyPair ;
//    }
//    /**
//     * 生成固定公钥私钥
//     * @return
//     */
//    public static KeyPair getKeyPair(String RSAmodulus,String RSApublicExponent,String RSAprivateExponent) {
//    	long st = System.currentTimeMillis() ;
//    	RSAPublicKey publicKey = null ;
//    	if(RSApublicExponent!=null&&!"".equals(RSApublicExponent)){
//    		publicKey = getPublicKey(RSAmodulus,RSApublicExponent) ;
//    	}
//    	RSAPrivateKey privateKey = null ;
//    	if(RSAprivateExponent!=null&&!"".equals(RSAprivateExponent)){
//    		privateKey = getPrivateKey(RSAmodulus,RSAprivateExponent) ;
//    	}
//    	KeyPair keyPair = new KeyPair(publicKey,privateKey);
//    	System.out.printf("生成固定公私钥耗时:"+(System.currentTimeMillis()-st));
//    	return keyPair ;
//    }
//    public static KeyPair getKeyPair() {
//    	if(staticKeyPair!=null){
//    		return staticKeyPair ;
//    	}else{
//    		//模  
//    		String RSAmodulus = "17590361768550206211304296020909443672497991496756109773322252259013465286845579413752496184027248059820980704679469737118666075144537820150739324451689607094439709077353019826775495958346915123823636420712765173032060048441941353150612472037329023471233201526271263321748209433499510445056447198407665583194128929575382802662715348893612504772549730627496131956857340581560167388183512266198528481499308939895214866963512452810966483935049796734695702115469855329855747438056741212320013011404385827781345796864627964307106735940995903236433032449374592345961492652542537539286775674624957345808007153510920388584373" ;
//    		//公钥指数  
//    		String RSApublicExponent = "65537" ;
//    		//私钥指数
//    		String RSAprivateExponent = "" ;
//    		staticKeyPair = getKeyPair(RSAmodulus, RSApublicExponent, RSAprivateExponent) ;
//    		return staticKeyPair ;
//    	}
//    }
//    /**
//     * 加密
//     * @param publicKey
//     * @param data
//     * @return
//     */
//    public static byte[] encrypt(Key publicKey, byte[] data) {
//    	try {
//    		//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
//    		Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
//    		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//    		return cipher.doFinal(data);
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//    
//    /**
//     * 加密
//     * @param publicKey
//     * @param text
//     * @return
//     */
//    public static String encrypt(Key publicKey, String text) {
//    	long st = System.currentTimeMillis() ;
//    	byte[] data = null;
//    	try {
//    		data = encrypt(publicKey, text.getBytes("UTF-8"));
//    	} catch (UnsupportedEncodingException e) {
//    		e.printStackTrace();
//    	}
//    	// return data != null ? Base64.encodeBase64String(data) : null;
//    	String encryptValue = data != null ? parseByteToHexStr(data) : null;
//    	System.out.printf("加密:"+text+";耗时:"+(System.currentTimeMillis()-st));
//    	return encryptValue ;
//    }
//    /**
//     * 
//     * @param privateKey
//     * @param data
//     * @return
//     */
//    public static byte[] decrypt(Key privateKey, byte[] data) {
//    	/*
//		try {
//			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
//			//Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			return cipher.doFinal(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//    	 */
//    	
//    	try {
//    		Cipher cipher = Cipher.getInstance("RSA",
//    				new org.bouncycastle.jce.provider.BouncyCastleProvider());
//    		cipher.init(Cipher.DECRYPT_MODE, privateKey);
//    		int blockSize = cipher.getBlockSize();
//    		ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
//    		int j = 0;
//    		while (data.length - j * blockSize > 0) {
//    			bout.write(cipher.doFinal(data, j * blockSize, blockSize));
//    			j++;
//    		}
//    		return bout.toByteArray();
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    		return null ;
//    	}
//    	
//    }
//    /**
//     * 解密
//     * @param privateKey
//     * @param text
//     * @return
//     */
//    public static String decrypt(Key privateKey, String text) {
//    	// byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
//    	long st = System.currentTimeMillis() ;
//    	String decryptValue = null ;
//    	byte[] data = decrypt(privateKey, hexStringToBytes(text));
//    	try {
//    		decryptValue = data != null ? new String(data,"UTF-8") : null;
//    	} catch (UnsupportedEncodingException e) {
//    		e.printStackTrace();
//    	}
//    	System.out.printf("解密:"+text+";耗时:"+(System.currentTimeMillis()-st));
//    	return decryptValue ;
//    }
//    /**
//     * 将二进制转换成16进制
//     * 
//     * @param buf
//     * @return
//     */
//    public static String parseByteToHexStr(byte buf[]) {
//    	StringBuffer sb = new StringBuffer();
//    	for (int i = 0; i < buf.length; i++) {
//    		String hex = Integer.toHexString(buf[i] & 0xFF);
//    		if (hex.length() == 1) {
//    			hex = '0' + hex;
//    		}
//    		sb.append(hex.toUpperCase());
//    	}
//    	return sb.toString();
//    }
//    /** * 16进制 To byte[] * @param hexString * @return byte[] 
//     * 
//     * js前台使用公钥加密，后台java解密
//            问题是：
//     String result = request.getParameter("encryedPwd"); byte[] en_result = new BigInteger(result, 16).toByteArray(); 
//         这里:byte[] en_result = new BigInteger(result, 16).toByteArray();  
//         有的时候你会发现数组长度129，第一个元素为0，这肯定是不正确的！
//         解决办法自己写一个hex to byte[]的方法，，就不出现上面的问题了。
//     * 
//     * */
//    public static byte[] hexStringToBytes(String hexString) {
//    	if (hexString == null || hexString.equals("")) {
//    		return null;
//    	}
//    	hexString = hexString.toUpperCase();
//    	int length = hexString.length() / 2;
//    	char[] hexChars = hexString.toCharArray();
//    	byte[] d = new byte[length];
//    	for (int i = 0; i < length; i++) {
//    		int pos = i * 2;
//    		d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//    	}
//    	return d;
//    }
//    
//    /** * Convert char to byte * @param c char * @return byte */
//    private static byte charToByte(char c) {
//    	return (byte) "0123456789ABCDEF".indexOf(c);
//    }
//    /** 
//     * 使用模和指数生成RSA公钥 
//     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
//     * /None/NoPadding】 
//     *  
//     * @param modulus 
//     *            模 
//     * @param exponent 
//     *            指数 
//     * @return 
//     */  
//    public static RSAPublicKey getPublicKey(String modulus, String exponent) {  
//    	try {  
//    		BigInteger b1 = new BigInteger(modulus);  
//    		BigInteger b2 = new BigInteger(exponent);  
//    		KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
////            KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");  
//    		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);  
//    		return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
//    	} catch (Exception e) {  
//    		e.printStackTrace();
//    		return null;  
//    	}  
//    }  
//    
//    /** 
//     * 使用模和指数生成RSA私钥 
//     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
//     * /None/NoPadding】 
//     *  
//     * @param modulus 
//     *            模 
//     * @param exponent 
//     *            指数 
//     * @return 
//     */  
//    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {  
//    	try {  
//    		BigInteger b1 = new BigInteger(modulus);  
//    		BigInteger b2 = new BigInteger(exponent);  
//    		KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
////            KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");  
//    		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
//    		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
//    	} catch (Exception e) {  
//    		e.printStackTrace();
//    		return null;  
//    	}  
//    }
	
	public static void main(String[] args) {
		try {
			KeyPair keyPair = generateKeyPair();
			//KeyPair keyPair = getKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			System.err.println("publicKey.getModulus:"+publicKey.getModulus());
			//System.err.println(publicKey.getModulus().toString(16));
			System.err.println("publicKey.getPublicExponent:"+publicKey.getPublicExponent());
			//System.err.println(publicKey.getPublicExponent().toString(16));
			//System.out.println(privateKey.getModulus());
			System.err.println("privateKey.getPrivateExponent:"+privateKey.getPrivateExponent());
			String miwen = encrypt(publicKey,"你好大使馆哈师大说句话 hgfghf655766") ;
			System.out.println(miwen);
			String jiemi = decrypt(privateKey,miwen) ;
			System.out.println(jiemi);
			
			System.out.println(publicKey.getEncoded().length+"ddddddddddddddddddddd");
			System.out.println(publicKey);
			miwen = encrypt(privateKey,"{\"code\":\"200\",\"data\":{\"tokenId\":\"3b5335396092dbf2da0012808a47d0df7\",\"digitalSignatureKey\":\"0f3hcwpqlqkmnw1\"},\"msg\":\"成功\",\"tokenId\":\"3b5335396092dbf2da0012808a47d0df7\"}") ;
			System.out.println(miwen);
			jiemi = decrypt(publicKey,miwen) ;
			System.out.println(jiemi);
			
			
			String publicKeygetModulus = "146092808730320144735729361737259771395235784649397849347545828067983277664773638462655578460422052152852205026953787284472096786817652688013074920919852351549938104513323543701126867458745130312737974044654218992287837756506147223536240986269365483829624306509395462000876837761910186773929621438806709092917";
			String publicKeygetPublicExponent = "65537" ;
			String privateKeygetPrivateExponent = "42797745316590879331999756107140246746374289782623393039405715139191768739582054180760859527620168443514495389054820362155420391688849260069320008647637896635466193738861220089120152377950332958757102733242003174861734744105050029562777024551670221668038025921938206071392308124916602082924998644417338384865" ;

			KeyPair keyPair1 = getKeyPair(publicKeygetModulus,publicKeygetPublicExponent,privateKeygetPrivateExponent);
			publicKey = (RSAPublicKey) keyPair1.getPublic();
			
			privateKey = (RSAPrivateKey) keyPair1.getPrivate();
			String miwen1 = encrypt(publicKey,"adminahgsdvghasgdhagsdhjgasyudgasudbjkasbdhjasdbjhasbdhasjdvasfdasgvgdhv") ;
			System.out.println("11111111:"+miwen1);
			String jiemi1 = decrypt(privateKey,miwen1) ;
			System.out.println(jiemi1);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
