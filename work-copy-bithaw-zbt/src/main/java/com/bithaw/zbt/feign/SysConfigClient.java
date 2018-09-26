package com.bithaw.zbt.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bithaw.common.annotation.RpcAuthRequire;
import com.bithaw.zbt.config.FeignCommonConfiguration;

/**
 * @Description 配置参数feign客户端
 * @author   WangWei
 * @date     2018年8月24日 上午11:06:20
 * @version  V 1.0
 */
@FeignClient(value = "trade-service",configuration=FeignCommonConfiguration.class)
public interface SysConfigClient {
	/**
	 * @author WangWei
	 * @Description 获取配置文件已扫描高度
	 * @method getScanBlockHeight
	 * @param @return 
	 * @return String
	 * @date 2018年8月24日 上午11:07:19
	 * @deprecated 不推荐使用,推荐改为getSysConfigValue方法
	 */
	@PostMapping(value = "/ethBlock/getScanBlockHeight")
	@Deprecated
	public String getScanBlockHeight();
	
	/**
	 * @author WangWei
	 * @Description 设置配置文件已扫描高度
	 * @method setScanBlockHeight
	 * @param @param scanBlockHeight
	 * @param @return 
	 * @return String
	 * @date 2018年8月24日 上午11:07:27
	 * @deprecated 不推荐使用,推荐改为setSysConfigValue方法
	 */
	@PostMapping(value = "/ethBlock/setScanBlockHeight")
	@Deprecated
	public String setScanBlockHeight(@RequestParam("scanBlockHeight")String scanBlockHeight);
	
	/**
	 * @author WangWei
	 * @Description 获取配置文件sysconfigvalue
	 * @method getSysConfigValue
	 * @param @param configKey
	 * @param @return 
	 * @return String
	 * @date 2018年8月24日 上午11:09:03
	 */
	@PostMapping(value = "/sysConfig/getValue")
	public String getSysConfigValue(@RequestParam("configKey")String configKey);
	
	/**
	 * @author WangWei
	 * @Description 设置配置文件接口
	 * @method setSysConfigValue
	 * @param @param configKey
	 * @param @param configValue
	 * @param @return 
	 * @return String
	 * @date 2018年8月24日 上午11:09:19
	 */
	@PostMapping(value = "/sysConfig/setValue")
    public String setSysConfigValue(@RequestParam("configKey")String configKey//
    		,@RequestParam("configValue")String configValue);
	
    /**
     * @author WangWei
     * @Description 查看配置是否存在
     * @method isKeyExist
     * @param @param configKey
     * @param @return 
     * @return boolean
     * @date 2018年8月24日 上午11:09:34
     */
    @PostMapping(value = "/sysConfig/isKeyExist")
    @RpcAuthRequire
    public String isKeyExist(@RequestParam("configKey")String configKey);
}
