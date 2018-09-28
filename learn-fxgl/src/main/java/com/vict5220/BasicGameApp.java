/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  下午3:20:35
 * @version   V 1.0
 */
package com.vict5220;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.settings.GameSettings;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 下午3:20:35
 * @version  V 1.0
 */
public class BasicGameApp extends GameApplication{

	/** 
	 * <p>Title: initSettings</p>
	 * <p>Description: </p>
	 * @param arg0
	 * @see com.almasb.fxgl.app.GameApplication#initSettings(com.almasb.fxgl.settings.GameSettings)  
	 */
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
