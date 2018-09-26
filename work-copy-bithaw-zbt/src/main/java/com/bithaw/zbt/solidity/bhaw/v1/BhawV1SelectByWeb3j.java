/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  上午9:55:41
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description web3j本地节点调用查看合约状态
 * @author   WangWei
 * @date     2018年9月11日 上午9:55:41
 * @version  V 1.0
 */
@Slf4j
@Component
public class BhawV1SelectByWeb3j {
	
	/**
	 * web3j : 本地节点web3j方式查看
	 */
	@Autowired
	private Web3j web3j;
	
	/**
	 * @author WangWei
	 * @Description 查看账户余额
	 * @method balanceOf
	 * @param fromAddress 调用地址
	 * @param address 查询地址
	 * @return BigInteger (b)
	 * @date 2018年9月11日 上午10:02:28
	 */
	public BigInteger balanceOf(String fromAddress,String address){
		
		Function function = new Function(
				"balanceOf"
				, Arrays.asList( new Address(address) )
				, Arrays.asList( new TypeReference<Uint256>() {} )
		);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
		);
		
		BigInteger balance = BigInteger.ZERO;
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			balance = (BigInteger) results.get(0).getValue();
		} catch (IOException e) {
			log.error("智能合约BHAW,balanceOf查询方法失败");
		}
		return balance;
	}
	
	
	/**
	 * @author WangWei
	 * @Description 查看总裁地址
	 * @method balanceOf
	 * @param fromAddress
	 * @return String address
	 * @date 2018年9月11日 上午10:02:28
	 */
	public String owner(String fromAddress){
		
		Function function = new Function(
				"owner"
				, Arrays.asList(  )
				, Arrays.asList( new TypeReference<Address>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		String owner = null;
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			owner = (String) results.get(0).getValue();
		} catch (IOException e) {
			log.error("智能合约BHAW,owner查询方法失败");
		}
		return owner;
	}
	
	/**
	 * @author WangWei
	 * @Description 查看总发行量
	 * @method totalSupply
	 * @param fromAddress
	 * @return BigInteger (b)
	 * @date 2018年9月11日 上午11:02:37
	 */
	public BigInteger totalSupply(String fromAddress){
		
		Function function = new Function(
				"totalSupply"
				, Arrays.asList(  )
				, Arrays.asList( new TypeReference<Uint256>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		BigInteger totalSupply = null;
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			totalSupply = (BigInteger) results.get(0).getValue();
		} catch (IOException e) {
			log.error("智能合约BHAW,totalSupply查询方法失败");
		}
		return totalSupply;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取允许spender还能从owner中提取token的额度 
	 * @method allowance
	 * @param fromAddress
	 * @param ownerAddress
	 * @param spenderAddress
	 * @return BigInteger
	 * @date 2018年9月11日 上午11:14:15
	 */
	public BigInteger allowance(String fromAddress,String ownerAddress,String spenderAddress){
		Function function = new Function(
				"allowance"
				, Arrays.asList( new Address(ownerAddress) , new Address(spenderAddress) )
				, Arrays.asList( new TypeReference<Uint256>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		BigInteger allowance = null;
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			allowance = (BigInteger) results.get(0).getValue();
		} catch (IOException e) {
			log.error("智能合约BHAW,allowance查询方法失败");
		}
		return allowance;
	}
	
	/**
	 * @author WangWei
	 * @Description 查询冻结状态
	 * @method freezeFlag
	 * @param fromAddress 发送地址
	 * @param address 查询地址
	 * @return Optional<Boolean> false:未冻结;true:冻结
	 * @date 2018年9月11日 上午11:25:59
	 */
	public Optional<Boolean> freezeFlag(String fromAddress,String address){
		Function function = new Function(
				"frozenAccount"
				, Arrays.asList( new Address(address)  )
				, Arrays.asList( new TypeReference<Bool>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		Optional<Boolean> freezeFlag = Optional.empty();
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			freezeFlag = Optional.ofNullable(results)
				.map(p -> p.get(0))
				.map(p -> p.getValue())
				.map(p -> (Boolean)p);
		} catch (IOException e) {
			log.error("智能合约BHAW,freezeFlag查询方法失败");
		}
		return freezeFlag;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取合约名字
	 * @method name
	 * @param fromAddress
	 * @return Optional<String>
	 * @date 2018年9月11日 上午11:31:42
	 */
	public Optional<String> name(String fromAddress){
		Function function = new Function(
				"name"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Utf8String>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		Optional<String> name = Optional.empty();
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			name = Optional.ofNullable(results)
					.map(p -> p.get(0))
					.map(p -> p.getValue())
					.map(p -> (String)p);
		} catch (IOException e) {
			log.error("智能合约BHAW,name查询方法失败");
		}
		return name;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取合约icon
	 * @method symbol
	 * @param fromAddress
	 * @return Optional<String> icon
	 * @date 2018年9月11日 上午11:31:42
	 */
	public Optional<String> symbol(String fromAddress){
		Function function = new Function(
				"symbol"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Utf8String>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		Optional<String> symbol = Optional.empty();
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			symbol = Optional.ofNullable(results)
					.map(p -> p.get(0))
					.map(p -> p.getValue())
					.map(p -> (String)p);
		} catch (IOException e) {
			log.error("智能合约BHAW,symbol查询方法失败");
		}
		return symbol;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取合约代币精度
	 * @method decimals
	 * @param fromAddress
	 * @return Optional<Integer> 代币精度
	 * @date 2018年9月11日 上午11:31:42
	 */
	public Optional<BigInteger> decimals(String fromAddress){
		Function function = new Function(
				"decimals"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Uint8>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		Optional<BigInteger> decimals = Optional.empty();
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			decimals = Optional.ofNullable(results)
					.map(p -> p.get(0))
					.map(p -> p.getValue())
					.map(p -> (BigInteger)p);
		} catch (IOException e) {
			log.error("智能合约BHAW,decimals查询方法失败");
		}
		return decimals;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取卖价
	 * @method sellPrice
	 * @param fromAddress
	 * @return Optional<BigInteger>
	 * @date 2018年9月11日 下午3:14:55
	 */
	public Optional<BigInteger> sellPrice(String fromAddress){
		Function function = new Function(
				"sellPrice"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Uint256>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		Optional<BigInteger> sellPrice = Optional.empty();
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			sellPrice = Optional.ofNullable(results)
					.map(p -> p.get(0))
					.map(p -> p.getValue())
					.map(p -> (BigInteger)p);
		} catch (IOException e) {
			log.error("智能合约BHAW,sellPrice查询方法失败");
		}
		return sellPrice;
	}
	
	
	/**
	 * @author WangWei
	 * @Description 获取买价
	 * @method buyPrice
	 * @param fromAddress
	 * @return Optional<BigInteger>
	 * @date 2018年9月11日 下午3:15:07
	 */
	public Optional<BigInteger> buyPrice(String fromAddress){
		Function function = new Function(
				"buyPrice"
				, Arrays.asList(   )
				, Arrays.asList( new TypeReference<Uint256>() {} )
				);
		
		Transaction transaction = Transaction.createEthCallTransaction(
				fromAddress
				, BhawV1.solidityAddress
				, FunctionEncoder.encode(function)
				);
		
		Optional<BigInteger> buyPrice = Optional.empty();
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			buyPrice = Optional.ofNullable(results)
					.map(p -> p.get(0))
					.map(p -> p.getValue())
					.map(p -> (BigInteger)p);
		} catch (IOException e) {
			log.error("智能合约BHAW,sellPrice查询方法失败");
		}
		return buyPrice;
	}
	
	
}
