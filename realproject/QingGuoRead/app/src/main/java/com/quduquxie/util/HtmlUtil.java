package com.quduquxie.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class HtmlUtil {

	/**
	 * HTML工具类函数：读取URL中的HTML文本
	 * 
	 * @param url
	 *            原始html的访问路径
	 */
	public static String readURL(String url) {
		/* StringBuffer的缓冲区大小 */
		int TRANSFER_SIZE = 4096;

		/* 当前平台的行分隔符 */
		String lineSep = System.getProperty("line.separator");

		String content = "";
		URL source = null;
		HttpURLConnection urlCon = null;
		try {
			source = new URL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (source == null) {
			return content;
		}
		// 自动探测页面的编码
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());

		Charset charset = null;
		try {
			charset = detector.detectCodepage(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (charset == null) {
			charset = Charset.defaultCharset();
		}

		InputStream in = null;
		try {
			urlCon = (HttpURLConnection) source.openConnection();
			urlCon.setConnectTimeout(30000);
			urlCon.setReadTimeout(30000);
			if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
				in = urlCon.getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader reader = null;
		if (in != null) {
			try {

				reader = new BufferedReader(new InputStreamReader(in, charset));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String line = new String();
			StringBuffer temp = new StringBuffer(TRANSFER_SIZE);
			if (reader != null) {
				try {
					while ((line = reader.readLine()) != null) {
						temp.append(line);
						temp.append(lineSep);
					}
					in.close();
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			content = temp.toString();
		}

		return content;
	}

	/**
	 * 获取原始正文，将正文中的特殊字符替换掉
	 * 
	 * @param content
	 *            原始html页面内容
	 * @param regexp
	 *            模板规则
	 * @return 匹配成功，则返回处理后的小说正文； 匹配不成功，返回null
	 */
	public static String replace(String content, String regexp) {
		content = content.replaceAll("\\n|\\r", "");
		Pattern pattern = Pattern.compile(regexp, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(content);
		// System.out.println(matcher.find());
		while (matcher.find()) {
			content = matcher.group(1);
			content = content.trim().replaceAll("&nbsp;", " ");
			content = content.replaceAll("<br />|<br>|<br/>|<p>|<P>|</p>|<li>|<Li>|<LI>|<div>", "\r\n");
			content = content.replaceAll("<[^>]*>", "");
			return content;
		}
		return null;
	}

	/**
	 * 获取小说正文
	 * 
	 * @param url
	 *            原始html的访问路径
	 * @param regexp
	 *            模板规则
	 * @return 匹配成功，则返回处理后的小说正文； 匹配不成功，返回null
	 */
	public static String getContent(String url, String regexp) {
		return replace(readURL(url), regexp);
	}

	/**
	 * 获取小说正文,带<br />
	 * 
	 * @param url
	 *            原始html的访问路径
	 * @param regexp
	 *            模板规则
	 * @return 匹配成功，则返回处理后的小说正文； 匹配不成功，返回null
	 */
	public static String getContentWithBR(String url, String regexp) {
		String content = getContent(url, regexp);
		return content != null ? content.replaceAll("\r\n", "<br />") : content;
	}

	/**
	 * args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://www.airead.com/files/article/html/0/7/71633.html";
		String regexp = "<div id=\"adb\">(.*?)<br class=\"clearfloat\" />";
		System.out.println(regexp);
		String str = getContent(url, regexp);
		System.out.println(str);
	}
}
