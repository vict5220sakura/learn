/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月26日  下午3:41:45
 * @version   V 1.0
 */
package com.soft.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.soft.util.WebCrawlerRobot;
import com.soft.util.image.GetImage;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月26日 下午3:41:45
 * @version  V 1.0
 */
@Slf4j
@Order(value = 9)
@Component
public class Start implements ApplicationRunner{

	@Autowired
	private GetImage getImage;
	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("爬虫开始");
		
		//WebCrawlerRobot.crawler("https://blog.csdn.net/forever_wind/article/details/37506389", "D://test.txt");
		
		String html = getImage.getHtml("http://dafeiyu.cn/");
		List<String> imageUrl = getImage.getImageUrl(html);
		List<String> imageSrc = getImage.getImageSrc(imageUrl);
		getImage.Download(imageSrc, "D://webCrawler\\image");
		
		log.info("爬虫结束");
	}
}
