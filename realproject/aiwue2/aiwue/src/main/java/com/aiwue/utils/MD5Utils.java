package com.aiwue.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

/**
 * 加密类
 *
 */
public class MD5Utils{

	// 加的盐

	public static String EncoderByMd5(String buf) {
		try {
			MessageDigest digist = MessageDigest.getInstance("MD5");
			byte[] rs = digist.digest(buf.getBytes());
			StringBuffer digestHexStr = new StringBuffer();
			for (int i = 0; i < 16; i++) {
				digestHexStr.append(byteHEX(rs[i]));
			}
			return digestHexStr.toString();
		} catch (NoSuchAlgorithmException e) {
			Timber.e(e.getMessage(), e);
		}
		return null;

	}

	
	public static String byteHEX(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}
}
