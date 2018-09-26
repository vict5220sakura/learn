package com.soft.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java实现爬虫
 */
public class WebCrawlerRobot {
	public static void crawler(String urlStr, String file) {
		URL url = null;
		URLConnection urlconn = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		// String regex = "http://[\\w+\\.?/?]+\\.[A-Za-z]+";
		String regex = "https://[\\w+\\.?/?]+\\.[A-Za-z]+";// url匹配规则
		Pattern p = Pattern.compile(regex);
		try {
			url = new URL(urlStr);// 爬取的网址、这里爬取的是一个生物网站
			urlconn = url.openConnection();
			pw = new PrintWriter(new FileWriter(file), true);// 将爬取到的链接放到D盘的SiteURL文件中
			br = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
			String buf = null;
			while ((buf = br.readLine()) != null) {
				Matcher buf_m = p.matcher(buf);
				while (buf_m.find()) {
					pw.println(buf_m.group());
				}
			}
			System.out.println("爬取成功^_^");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pw.close();
		}
	}
}