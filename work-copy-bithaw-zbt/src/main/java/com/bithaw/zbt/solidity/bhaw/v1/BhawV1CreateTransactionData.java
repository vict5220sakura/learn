/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  上午9:58:05
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

/**
 * @Description 合约交易方法,构建交易data
 * @author   WangWei
 * @date     2018年9月11日 上午9:58:05
 * @version  V 1.0
 */
@Component
public class BhawV1CreateTransactionData {
	
	/**
	 * @author WangWei
	 * @Description 创建移交管理员权限data数据
	 * @method transferOwnership
	 * @param toAddress
	 * @return Optional<String> data数据
	 * @date 2018年9月11日 下午2:28:05
	 */
	public Optional<String> transferOwnership(String toAddress){
		
		Function function = new Function(
				"transferOwnership"
				,Arrays.asList( new Address(toAddress) )
				,Arrays.asList()
				);
		
		String data = FunctionEncoder.encode(function);
		
		return Optional.ofNullable(data);
	}
	
	/**
	 * @author WangWei
	 * @Description 创建转账数据
	 * @method transfer
	 * @param toAddress 转账地址
	 * @param value 金额(b)
	 * @return Optional<String> data数据
	 * @date 2018年9月11日 下午2:45:38
	 */
	public Optional<String> transfer(String toAddress,BigInteger value){
		
		Function function = new Function(
				"transfer"
				,Arrays.asList( new Address(toAddress),new Uint256(value) )
				,Arrays.asList( new TypeReference<Bool>() {} )
				);
		
		String data = FunctionEncoder.encode(function);
		
		return Optional.ofNullable(data);
	}

	/**
	 * @author WangWei
	 * @Description 账户的冻结与解冻状态设置
	 * @method freezeAccount
	 * @param address
	 * @param flag true:冻结;解冻
	 * @return Optional<String>
	 * @date 2018年9月11日 下午3:05:47
	 */
	public Optional<String> freezeAccount(String address,boolean flag){
		Function function = new Function(
				"freezeAccount"
				,Arrays.asList( new Address(address),new Bool(flag) )
				,Arrays.asList(  )
				);
		
		String data = FunctionEncoder.encode(function);
		
		return Optional.ofNullable(data);
	}

	/**
	 * @author WangWei
	 * @Description 从制定账户所拥有的提币额度向任意账户转账,前提是发送交易者在from账户上存在一定的代币
	 * @method transferFrom
	 * @param fromAddress
	 * @param toAddress
	 * @param value
	 * @return Optional<String>
	 * @date 2018年9月11日 下午2:50:59
	 */
	public Optional<String> transferFrom(String fromAddress,String toAddress,BigInteger value){
		Function function = new Function(
				"transferFrom"
				,Arrays.asList( new Address(fromAddress),new Address(toAddress),new Uint256(value) )
				,Arrays.asList( new TypeReference<Bool>() {} )
				);
		
		String data = FunctionEncoder.encode(function);
		
		return Optional.ofNullable(data);
	}
	
	/**
	 * @author WangWei
	 * @Description 设置approve参数,指定目标地址可以从我的账户中操作的金额数目
	 * @method approve
	 * @param spenderAddress
	 * @param value
	 * @return Optional<String>
	 * @date 2018年9月11日 下午3:00:38
	 */
	public Optional<String> approve(String spenderAddress,BigInteger value){
		Function function = new Function(
				"approve"
				,Arrays.asList( new Address(spenderAddress),new Uint256(value) )
				,Arrays.asList( new TypeReference<Bool>() {} )
				);
		
		String data = FunctionEncoder.encode(function);
		
		return Optional.ofNullable(data);
	}
	
	/**
	 * @author WangWei
	 * @Description 销毁代币,只有合约管理员才能调用
	 * @method burn
	 * @param value
	 * @return Optional<String>
	 * @date 2018年9月11日 下午3:03:50
	 */
	public Optional<String> burn(BigInteger value){
		Function function = new Function(
				"burn"
				,Arrays.asList( new Uint256(value) )
				,Arrays.asList( new TypeReference<Bool>() {} )
				);
		
		String data = FunctionEncoder.encode(function);
		
		return Optional.ofNullable(data);
	}
	
}
