package com.soft;
import java.math.BigInteger;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
public class Base58Test {
	public static void main(String[] args) {
		byte[] decode = HexBin.decode("72fb9545ca6da763b3f577e0a605f868325c1dfd01942f4e3550c79d5def85a5");
		BigInteger bigInteger = new BigInteger(decode);
		System.out.println(bigInteger.toString());
	}
}
