package com.quduquxie.util;

import android.content.pm.PackageInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public static String encodeMD5String(String str) {
		if (str == null || "".equals(str))
			return "";
		return encode(str, "MD5");
	}

	private static String encode(String str, String method) {
		MessageDigest md = null;
		String dstr = null;
		try {
			md = MessageDigest.getInstance(method);
			md.update(str.getBytes());
			dstr = new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dstr;
	}

	/**
	 * baidu 根据packageinfo 计算签名的MD5值;
	 * 
	 * packageInfo
	 */
	public static String getMd5(PackageInfo packageInfo) {
		if (packageInfo == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(packageInfo.signatures[0].toCharsString().getBytes());
			byte[] b = md.digest();
			char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			StringBuffer sb = new StringBuffer(b.length * 2);
			for (int i = 0; i < b.length; i++) {
				sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
				sb.append(HEXCHAR[b[i] & 0x0f]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * baidu 根据MD5值计算signmd5值;
	 * 
	 * md5
	 */
	public static String creatSignInt(String md5) {
		if (md5 == null || md5.length() < 32)
			return "-1";
		String sign = md5.substring(8, 8 + 16);
		long id1 = 0;
		long id2 = 0;
		String s = "";
		for (int i = 0; i < 8; i++) {
			id2 *= 16;
			s = sign.substring(i, i + 1);
			id2 += Integer.parseInt(s, 16);
		}
		for (int i = 8; i < sign.length(); i++) {
			id1 *= 16;
			s = sign.substring(i, i + 1);
			id1 += Integer.parseInt(s, 16);
		}
		long id = (id1 + id2) & 0xFFFFFFFFL;
		return String.valueOf(id);
	}

	/*
	 * md5
	 */
	public static String getApkMD5(File file) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			InputStream fis;
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				digest.update(buffer, 0, numRead);
			}
			fis.close();
			return bufferToHex(digest.digest());
		} catch (Exception e) {
			return null;
		}
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = HEX_DIGITS[(bt & 0xf0) >> 4];
		char c1 = HEX_DIGITS[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static String getFileMD5String(File file) throws IOException, NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
//		Log.e("lq", "file length="+file.length());
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messageDigest.update(byteBuffer);
		return byteArrayToHex(messageDigest.digest());
	}
	
	/*
	 *MD5文件
	 */
	public static String getFileMD5String2(File file) throws IOException, NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		FileInputStream in;
		in = new FileInputStream(file);
		byte[] bytes = new byte[4 * 1024];  
	    int byteCount;  
	    while ((byteCount = in.read(bytes)) > 0) {  
	    	messageDigest.update(bytes, 0, byteCount);  
	    }
	    in.close();
		return byteArrayToHex(messageDigest.digest());
	}	
	
	public static String byteArrayToHex(byte[] byteArray) {

		// 首先初始化一个字符数组，用来存放每个16进制字符

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

		char[] resultCharArray = new char[byteArray.length * 2];

		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

		int index = 0;

		for (byte b : byteArray) {

			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];

			resultCharArray[index++] = hexDigits[b & 0xf];

		}

		// 字符数组组合成字符串返回

		return new String(resultCharArray);

	}
}
