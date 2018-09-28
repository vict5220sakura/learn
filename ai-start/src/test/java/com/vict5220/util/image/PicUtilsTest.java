/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  上午11:01:19
 * @version   V 1.0
 */
package com.vict5220.util.image;

import org.junit.Test;

import com.vict5220.util.file.FileUtil;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 上午11:01:19
 * @version  V 1.0
 */
public class PicUtilsTest {
	
	@Test
	public void compressPicForScale(){
		byte[] bytesByFile = FileUtil.getBytesByFile("D://wechat.png");
		byte[] compressPicForScale = PicUtils.compressPicForScale(bytesByFile, 1L, "1");
		FileUtil.getFileByBytes(compressPicForScale, "D://", "123.jpg");
	}
}
