package com.bithaw.btc.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;

public class Rsa2Sign {

	
    public static String sign(String content,String privateKey) {
    	try {
    		PrivateKey privateKey_ = getPrivateKey(privateKey);
			//转成byte数组，按实际情况使用字符集，此处使用utf-8
			byte[] reqBs = content.getBytes("UTF-8");
			Signature signature = Signature.getInstance("SHA256WithRSA");
			signature.initSign(privateKey_);
			signature.update(reqBs);
			byte[] signedBs = signature.sign();
			//对签名数据进行base64编码,并对一些特殊字符进行置换
			String signedStr = Base64.encodeBase64String(signedBs);
			//System.out.println(signedStr);
			//将签名信息加入原始请求报文map
			return signedStr;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
     * 把私钥转换为PrivateKey对象
     * @param key
     * @return
     * @throws Exception
     */
      private static PrivateKey getPrivateKey(String key) throws Exception {  
          byte[] keyBytes;  
          keyBytes = Base64.decodeBase64(key);  
          PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
          PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
          return privateKey;  
    }  
    
    public static String signForMap(Map<String, String> reqMap,String privateKey){
    	
 		//将reqMap排序
 		SortedMap<String, String> sm = new TreeMap<String, String>(reqMap);
 		//按序拼接
 		StringBuilder sb = new StringBuilder();
 		for(Entry<String, String> sme : sm.entrySet()){
 			String v = sme.getValue();
 			//空字段不参加签名
 			if(null == v || v.length()==0)
 				continue;
 			sb.append("&").append(sme.getKey()).append("=").append(v);
 		}
 		//System.out.println(sb.substring(1));
 		//使用私钥签名
 		try {
 			PrivateKey privateKey_ = getPrivateKey(privateKey);
 			//转成byte数组，按实际情况使用字符集，此处使用utf-8
 			byte[] reqBs = sb.substring(1).getBytes("UTF-8");
 			Signature signature = Signature.getInstance("SHA256WithRSA");
 			signature.initSign(privateKey_);
 			signature.update(reqBs);
 			byte[] signedBs = signature.sign();
 			//对签名数据进行base64编码,并对一些特殊字符进行置换
 			String signedStr = Base64.encodeBase64String(signedBs);
 			//System.out.println(signedStr);
 			//将签名信息加入原始请求报文map
 			return signedStr;
 		} catch (Exception e) {
 			throw new RuntimeException(e);
 		}
 	}
    
    public static boolean verify(String data, String sign, String publicKeyString) throws Exception {  
        KeyFactory keyf = KeyFactory.getInstance("RSA");  
        PublicKey publicKey = keyf.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString)));  
        java.security.Signature signet = java.security.Signature.getInstance("SHA256WithRSA");  
        signet.initVerify(publicKey);  
        signet.update(data.getBytes("utf-8"));  
        return signet.verify(Base64.decodeBase64(sign));  
    }  
    
}
