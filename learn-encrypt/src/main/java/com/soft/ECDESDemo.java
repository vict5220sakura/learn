/*
*Created on 2015年12月13日
*Copyright 2015 Yong Cai Limited crop. All Rights Reserved
*
*/
 
package com.soft;
 
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
 
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
 
public class ECDESDemo {
	private static String src = "ecdsa security";
	public static void main(String[] args) throws Exception {
		sign();
	}
	
	public static void init() throws Exception{
		//1.初始化密钥 
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
		keyPairGenerator.initialize(256);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();
		ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate();
		System.out.println("公钥16进制 : " + HexBin.encode(ecPublicKey.getEncoded()));
		System.out.println("私钥16进制 : " + HexBin.encode(ecPrivateKey.getEncoded()));
	}
	
	public static void sign() throws Exception{
		//2.执行签名
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(HexBin.decode("72fb9545ca6da763b3f577e0a605f868325c1dfd01942f4e3550c79d5def85a5"));
		
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance("SHA160withECDSA");
		signature.initSign(privateKey);
		signature.update(HexBin.decode("5803802590f0bfa1fe56a18bc95c3c56fa296bb6"));
		byte[] res = signature.sign();
		System.out.println("签名："+HexBin.encode(res));
	}
	
	public static void jdkECDSA(){
		try {
			//1.初始化密钥 
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
			keyPairGenerator.initialize(256);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();
			ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate();
			
			
			//2.执行签名
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
			
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Signature signature = Signature.getInstance("SHA1withECDSA");
			signature.initSign(privateKey);
			signature.update(src.getBytes());
			byte[] res = signature.sign();
			System.out.println("签名："+HexBin.encode(res));
			
			//3.验证签名
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
			keyFactory = KeyFactory.getInstance("EC");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			signature = Signature.getInstance("SHA1withECDSA");
			signature.initVerify(publicKey);
			signature.update(src.getBytes());
			boolean bool = signature.verify(res);
			System.out.println("验证："+bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}
