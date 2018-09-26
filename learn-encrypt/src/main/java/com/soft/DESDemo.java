package com.soft;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
 
import sun.security.provider.DSAPrivateKey;
import sun.security.provider.DSAPublicKey;

public class DESDemo {
	private static String src = "你好";
	//SHA1withDES  //302C02140FDCD4F2BBFE79A1F773F1D81A05C8DFC541F54302146E7805492C17AF83C01657504CB508946D473D73
	//SHA224withDES//302C02146FA5519458C3CF26EB46502FEEEABB40E0087DC902140698A455120CF15C3062659658010DFB0A9CE53C
	public static void main(String[] args) throws Exception {
		sign();
	}

//公钥	3081F03081A806072A8648CE38040130819C024100FCA682CE8E12CABA26EFCCF7110E526DB078B05EDECBCD1EB4A208F3AE1617AE01F35B91A47E6DF63413C5E12ED0899BCD132ACD50D99151BDC43EE737592E17021500962EDDCC369CBA8EBB260EE6B6A126D9346E38C50240678471B27A9CF44EE91A49C5147DB1A9AAF244F05A434D6486931D2D14271B9E35030B71FD73DA179069B32E2935630E1C2062354D0DA20A6C416E50BE794CA40343000240440B57E1918E615F1AE2C2840D164735A9A9947003A19AFB578B7B08526C7F6C0BE270B8AFE4F01B240A0F54D47366D0CC3DD0F05EBB491FB61238D14ECD4DC5
//私钥	3081C60201003081A806072A8648CE38040130819C024100FCA682CE8E12CABA26EFCCF7110E526DB078B05EDECBCD1EB4A208F3AE1617AE01F35B91A47E6DF63413C5E12ED0899BCD132ACD50D99151BDC43EE737592E17021500962EDDCC369CBA8EBB260EE6B6A126D9346E38C50240678471B27A9CF44EE91A49C5147DB1A9AAF244F05A434D6486931D2D14271B9E35030B71FD73DA179069B32E2935630E1C2062354D0DA20A6C416E50BE794CA4041602140333FBCE8932BC4A8777496545D382B11C70C284

	public static void initKey() throws InvalidKeyException{
		//1.初始化密钥 
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			keyPairGenerator.initialize(512);
			keyPairGenerator.initialize(512, SecureRandom.getInstance(""));
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			DSAPublicKey dsaPublicKey = (DSAPublicKey)keyPair.getPublic();
			DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)keyPair.getPrivate();
			System.out.println(HexBin.encode(dsaPublicKey.getEncoded()));
			System.out.println(HexBin.encode(dsaPrivateKey.getEncoded()));
			System.out.println(dsaPublicKey.getAlgorithm());
			System.out.println(dsaPublicKey.getFormat());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static void sign() throws Exception{
		//2.执行签名
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(HexBin.decode("3081C60201003081A806072A8648CE38040130819C024100FCA682CE8E12CABA26EFCCF7110E526DB078B05EDECBCD1EB4A208F3AE1617AE01F35B91A47E6DF63413C5E12ED0899BCD132ACD50D99151BDC43EE737592E17021500962EDDCC369CBA8EBB260EE6B6A126D9346E38C50240678471B27A9CF44EE91A49C5147DB1A9AAF244F05A434D6486931D2D14271B9E35030B71FD73DA179069B32E2935630E1C2062354D0DA20A6C416E50BE794CA4041602140333FBCE8932BC4A8777496545D382B11C70C284"));
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance("SHA384withDSA");
		signature.initSign(privateKey);
		signature.update(src.getBytes("UTF-8"));
		byte[] res = signature.sign();
		System.out.println("签名："+HexBin.encode(res));
	}
	
	public static void check() throws Exception{
		//3.验证签名
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(HexBin.decode("3081F03081A806072A8648CE38040130819C024100FCA682CE8E12CABA26EFCCF7110E526DB078B05EDECBCD1EB4A208F3AE1617AE01F35B91A47E6DF63413C5E12ED0899BCD132ACD50D99151BDC43EE737592E17021500962EDDCC369CBA8EBB260EE6B6A126D9346E38C50240678471B27A9CF44EE91A49C5147DB1A9AAF244F05A434D6486931D2D14271B9E35030B71FD73DA179069B32E2935630E1C2062354D0DA20A6C416E50BE794CA40343000240440B57E1918E615F1AE2C2840D164735A9A9947003A19AFB578B7B08526C7F6C0BE270B8AFE4F01B240A0F54D47366D0CC3DD0F05EBB491FB61238D14ECD4DC5"));
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance("SHA512withDSA");
		signature.initVerify(publicKey);
		signature.update(src.getBytes("UTF-8"));
		boolean bool = signature.verify(HexBin.decode("302C021418D981CE86E02B55B90F571CBD2F4EB1B98509B50214124CEC7C39EF0D6041F8EF6663F6DCF9CF4D1438"));
		System.out.println("验证："+bool);
	}
	
	public static void jdkDSA(){
		try {
			//1.初始化密钥 
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			DSAPublicKey dsaPublicKey = (DSAPublicKey)keyPair.getPublic();
			DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)keyPair.getPrivate();
			//2.执行签名
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(dsaPrivateKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Signature signature = Signature.getInstance("SHA1withDSA");
			signature.initSign(privateKey);
			signature.update(src.getBytes());
			byte[] res = signature.sign();
			System.out.println("签名："+HexBin.encode(res));
			
			//3.验证签名
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(dsaPublicKey.getEncoded());
			keyFactory = KeyFactory.getInstance("DSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			signature = Signature.getInstance("SHA1withDSA");
			signature.initVerify(publicKey);
			signature.update(src.getBytes());
			boolean bool = signature.verify(res);
			System.out.println("验证："+bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
