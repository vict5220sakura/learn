package com.bithaw.btc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bithaw.btc.config.FeignCommonConfiguration;

@FeignClient(value = "trade-service",configuration=FeignCommonConfiguration.class)
public interface SysCoinfigClient {
	/**
	 * @author WangWei
	 * @Description 获取配置文件参数,为空则不存在
	 * @method getSysConfigValue
	 * @param configKey
	 * @return 
	 * @return String
	 * @date 2018年8月28日 上午10:44:58
	 */
	@PostMapping(value = "/sysConfig/getValue")
	public String getSysConfigValue(@RequestParam("configKey")String configKey);
	
	/**
	 * @author WangWei
	 * @Description 设置配置文件sysconfigvalue
	 * @method setSysConfigValue
	 * @param configKey
	 * @param configValue
	 * @return 
	 * @return String "true","fasle"
	 * @date 2018年8月28日 上午10:45:13
	 */
	@PostMapping(value = "/sysConfig/setValue")
	public String setSysConfigValue(//
			@RequestParam("configKey")String configKey//
			,@RequestParam("configValue")String configValue);
}
