package com.aiwue.utils;

import org.apache.http.util.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.util.TextUtils.isBlank;


/**
 * 
 * @description 主要包含常用的一些参数的有效性验证
 * @ClassName: RegExpValidator
 * @version V1.0
 * @author 赵以宝
 * @Date 2015年12月7日 上午11:06:40
 * Copyright(c) 2015 www.ehean.com  All rights reserved
 *
 */
public class RegExpValidator {

	/**
	 * 
	 * @description 验证输入字符串是否是一个有效的邮件地址
	 * @param @param str
	 * @param @return   
	 * @return boolean    返回类型 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:05:12
	 */
	public static boolean isValidEmail(String str) {
		String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return match(regex, str);
	}

	/**
	 * 
	 * @description 验证输入字符串是否是一个有效的中国手机号
	 * @param @param str 待验证的字符串
	 * @return boolean    如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b> 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:26:28
	 */
	public static boolean IsValidMobileNum(String str) {
		return (IsValidChinaMobileNum(str) || IsValidChinaTelecomMobileNum(str)|| IsValidChinaUnicomMobileNum(str));
	}

	/**
	 * 
	 * @description 验证输入字符串是否是一个有效的中国移动手机号
	 * </br/>中国移动号码格式验证手机段：134,135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188,1705 
	 * @param @param str 待验证的字符串
	 * @return boolean    如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b> 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:36:41
	 */
	public static boolean IsValidChinaMobileNum(String str) {
		
		String regex = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
		return match(regex, str);
	}

	/**
	 * 
	 * @description 验证输入字符串是否是一个有效的中国联通手机号
	 * <br/>中国联通号码格式验证 手机段：130,131,132,145,155,156,176,185,186,1709 
	 * @param @param str 待验证的字符串
	 * @return boolean    如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b> 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:36:41
	 */
	public static boolean IsValidChinaUnicomMobileNum(String str) {
		
		String regex = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
		return match(regex, str);
	}

	/**
	 * 
	 * @description 验证输入字符串是否是一个有效的中国电信手机号
	 * <br/>中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700 
	 * @param @param str 待验证的字符串
	 * @return boolean    如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b> 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:36:41
	 */
	public static boolean IsValidChinaTelecomMobileNum(String str) {
		
		String regex = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";
		return match(regex, str);
	}

	
	/**
	 * 
	 * @description 验证输入字符串是否是一个有效的IP地址
	 * @param @param str
	 * @param @return   
	 * @return boolean    返回类型 
	 * @throws 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:07:37
	 */
	public static boolean isIP(String str) {
		String num = "(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
		return match(regex, str);
	}
	
	/**
	 * 验证数字输入
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDigital(String str) {
		if (TextUtils.isBlank(str)) return false;
		String regex = "^[0-9]*$";
		return match(regex, str);
	}
	
	
	/**
	 * 
	 * @description 基本正则表达式匹配函数
	 * @param @param regex 正则表达式字符串
	 * @param @param str 要匹配的字符串
	 * @return boolean    如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 * @throws 
	 * @author 赵以宝
	 * @date 2015年12月7日 上午11:09:16
	 */
	private static boolean match(String regex, String str) {
		boolean flag = false;
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		 }catch(Exception e){
		 }
		return flag;
	}

	/**
	 * 验证网址Url
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUrl(String str) {
		if (str == null)
			return false;
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?#%&=]*)?";
		return match(regex, str);
	}

	
	public static void main(String[] args) {
 
		System.out.println("hehel");
		boolean res = IsUrl("http://mp.weixin.qq.com/s?__biz=MjM5NzAyNDk4NA==&mid=2649888699&idx=1&sn=b48df4464cc1052fe50e688535ed3a1a&scene=0#wechat_redirect");
		System.out.println("hehel"+res);
			
	}

	
	//=========================================================================================================================	
	//==================================================以下为未重构的代码======================================================
	//=========================================================================================================================



	

	/**
	 * 验证输入密码条件(字符与数据同时出现)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPassword(String str) {
		String regex = "[A-Za-z]+[0-9]";
		return match(regex, str);
	}

	/**
	 * 验证输入密码长度 (6-18位)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPasswLength(String str) {
		String regex = "^\\d{6,18}$";
		return match(regex, str);
	}

	/**
	 * 验证输入邮政编号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPostalcode(String str) {
		String regex = "^\\d{6}$";
		return match(regex, str);
	}




	/**
	 * 验证输入两位小数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDecimal(String str) {
		String regex = "^[0-9]+(.[0-9]{2})?$";
		return match(regex, str);
	}

	/**
	 * 验证输入一年的12个月
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsMonth(String str) {
		String regex = "^(0?[[1-9]|1[0-2])$";
		return match(regex, str);
	}

	/**
	 * 验证输入一个月的31天
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDay(String str) {
		String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
		return match(regex, str);
	}

	/**
	 * 验证日期时间
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合网址格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isDate(String str) {
		String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
		return match(regex, str);
	}

	/**
	 * 验证数字输入
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsNumber(String str) {
		String regex = "^[0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证非零的正整数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIntNumber(String str) {
		String regex = "^\\+?[1-9][0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证大写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUpChar(String str) {
		String regex = "^[A-Z]+$";
		return match(regex, str);
	}

	/**
	 * 验证小写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLowChar(String str) {
		String regex = "^[a-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLetter(String str) {
		String regex = "^[A-Za-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入汉字
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsChinese(String str) {
		String regex = "^[\u4e00-\u9fa5],{0,}$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字符串
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLength(String str) {
		String regex = "^.{8,}$";
		return match(regex, str);
	}

	

	/**
	 * 检查字符串是否含有HTML标签  
	 * @param value
	 * @return
	 */
	public static boolean checkHtmlTag(String str){   
		String regex = "^[a-zA-Z0-9],{0,}$";
		return match(regex, str);
	}   
	
	/**
	 * 检查输入的数据中是否有特殊字符
	 * 
	 * @param qString
	 *            要检查的数据
	 * @param regx
	 *            特殊字符正则表达式
	 * @return boolean 如果包含正则表达式 <code> regx </code> 中定义的特殊字符，返回true； 否则返回false
	 */
	public static boolean hasCrossScriptRisk(String qString) {
		String regx = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§ ";
		if (qString != null) {
			qString = qString.trim();
			Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(qString);
			return m.find();
		}
		return false;
	}
}
