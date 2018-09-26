/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  上午10:23:33
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.common.Common;
import com.bithaw.zbt.feign.SysConfigClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description etherscan查询数据
 * @author   WangWei
 * @date     2018年9月12日 上午10:23:33
 * @version  V 1.0
 */
@Slf4j
@Component
public class BhawV1SelectByEtherscan {
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SysConfigClient sysConfigClient;
	
	/**
	 * etherscanUrl : etherscan 接口地址
	 */
	private String etherscanUrl;
	private String etherscanApikey;
	
	public void init(){
		System.out.println("初始化");
		etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		System.out.println("初始化结束");
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method commonFunction
	 * @param toAddress 目标地址(合约地址)
	 * @param callData 访问数据
	 * @return json字符串
	 * @date 2018年9月12日 上午10:45:01
	 */
	private Optional<String> commonFunction(String toAddress,String callData){
		log.info("Etherscan通用eth_call接口调用;toAddress {} ;callData {}",toAddress,callData);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_call");
		map.add("apikey", etherscanApikey);
		map.add("to", toAddress);
		map.add("data", callData);
		map.add("tag", "latest");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(etherscanUrl, request, String.class);
		return Optional.ofNullable(postForEntity)
			.map(p -> p.getBody());
	}
	
	/**
	 * @author WangWei
	 * @Description 查询代币余额
	 * @method balanceOf 
	 * @param address 查询地址
	 * @return BigDecimal (B)
	 * @date 2018年9月12日 上午11:14:21
	 */
	public BigDecimal balanceOf(String address){
		Function function = new Function(
				"balanceOf"
				, Arrays.asList( new Address(address) )
				, Arrays.asList( new TypeReference<Uint256>() {} )
		);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
			.map(o -> o.getString("result"))
			.map(o -> o.substring(2))
			.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		BigInteger bigInteger = new BigInteger(result,16);
		return new BigDecimal(bigInteger.toString()).divide(Common.B1x10_6, 6, BigDecimal.ROUND_HALF_UP);
	}
	

	/**
	 * @author WangWei
	 * @Description 查看总裁地址
	 * @method owner
	 * @param fromAddress
	 * @return String
	 * @date 2018年9月12日 上午11:26:16
	 */
	public String owner(String fromAddress){
		Function function = new Function(
				"owner"
				, Arrays.asList(  )
				, Arrays.asList( new TypeReference<Address>() {} )
				);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
			.map(o -> o.getString("result"))
			.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		return new Address(result).toString();
	}
	
	/**
	 * @author WangWei
	 * @Description 查看总发行量
	 * @method totalSupply
	 * @return BigInteger (B)
	 * @date 2018年9月12日 上午11:33:13
	 */
	public BigDecimal totalSupply(){
		Function function = new Function(
				"totalSupply"
				, Arrays.asList(  )
				, Arrays.asList( new TypeReference<Uint256>() {} )
		);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
			.map(o -> o.getString("result"))
			.map(o -> o.substring(2))
			.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		BigInteger bigInteger = new BigInteger(result,16);
		return new BigDecimal(bigInteger.toString()).divide(Common.B1x10_6, 6, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取允许spender还能从owner中动用的token的额度 
	 * @method allowance
	 * @param ownerAddress 
	 * @param spenderAddress 
	 * @return BigDecimal (B)
	 * @date 2018年9月12日 上午11:35:48
	 */
	public BigDecimal allowance(String ownerAddress,String spenderAddress){
		Function function = new Function(
				"allowance"
				, Arrays.asList( new Address(ownerAddress) , new Address(spenderAddress) )
				, Arrays.asList( new TypeReference<Uint256>() {} )
				);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
				.map(o -> o.getString("result"))
				.map(o -> o.substring(2))
				.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		BigInteger bigInteger = new BigInteger(result,16);
		return new BigDecimal(bigInteger.toString()).divide(Common.B1x10_6, 6, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * @author WangWei
	 * @Description 查询冻结状态
	 * @method freezeFlag
	 * @param address
	 * @return Optional<Boolean> true:冻结;false:未冻结
	 * @date 2018年9月12日 上午11:44:25
	 */
	public Optional<Boolean> freezeFlag(String address){
		Function function = new Function(
				"frozenAccount"
				, Arrays.asList( new Address(address)  )
				, Arrays.asList( new TypeReference<Bool>() {} )
				);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
				.map(o -> o.getString("result"))
				.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		List<Type> results = FunctionReturnDecoder.decode(result, function.getOutputParameters());
		return Optional.ofNullable(results)
			.map(p -> p.get(0))
			.map(p -> p.getValue())
			.map(p -> (Boolean)p);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取合约名字
	 * @method name
	 * @return Optional<String>
	 * @date 2018年9月12日 上午11:52:14
	 */
	public Optional<String> name(){
		Function function = new Function(
				"name"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Utf8String>() {} )
				);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
				.map(o -> o.getString("result"))
				.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		List<Type> results = FunctionReturnDecoder.decode(result, function.getOutputParameters());
		return Optional.ofNullable(results)
			.map(p -> p.get(0))
			.map(p -> p.getValue())
			.map(p -> (String)p);
	}
	
	/**
	 * @author WangWei
	 * @Description 查询合约icon
	 * @method symbol
	 * @return Optional<String>
	 * @date 2018年9月12日 上午11:55:47
	 */
	public Optional<String> symbol(){
		Function function = new Function(
				"symbol"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Utf8String>() {} )
				);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
				.map(o -> o.getString("result"))
				.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		List<Type> results = FunctionReturnDecoder.decode(result, function.getOutputParameters());
		return Optional.ofNullable(results)
			.map(p -> p.get(0))
			.map(p -> p.getValue())
			.map(p -> (String)p);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取代币合约精度
	 * @method decimals
	 * @return Optional<BigInteger>
	 * @date 2018年9月12日 上午11:58:17
	 */
	public Optional<BigInteger> decimals(){
		Function function = new Function(
				"decimals"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Uint8>() {} )
				);
		String callData = FunctionEncoder.encode(function);
		Optional<String> commonFunction = commonFunction(BhawV1.solidityAddress, callData);
		JSONObject json = JSONObject.parseObject(commonFunction.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值")));
		String result = Optional.ofNullable(json)
				.map(o -> o.getString("result"))
				.orElseThrow(() -> new RuntimeException("Etherscan接口调用返回空值"));
		List<Type> results = FunctionReturnDecoder.decode(result, function.getOutputParameters());
		return Optional.ofNullable(results)
			.map(p -> p.get(0))
			.map(p -> p.getValue())
			.map(p -> (BigInteger)p);
	}
	
}
