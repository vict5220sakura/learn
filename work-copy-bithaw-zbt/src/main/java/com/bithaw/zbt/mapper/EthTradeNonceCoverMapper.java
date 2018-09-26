/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午6:58:48
 * @version   V 1.0
 */
package com.bithaw.zbt.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bithaw.zbt.entity.EthTradeNonceCover;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 下午6:58:48
 * @version  V 1.0
 */
public interface EthTradeNonceCoverMapper extends JpaRepository<EthTradeNonceCover,BigInteger>{
	
	/**
	 * @author WangWei
	 * @Description 根据state状态查询实体数组
	 * @method findAllByState
	 * @param @param state
	 * @param @return 
	 * @return List<EthTradeNonce>
	 * @date 2018年8月24日 上午11:20:48
	 */
	List<EthTradeNonceCover> findAllByState(int state);
	
	EthTradeNonceCover findByCoverNo(String coverNo);

	/**
	 * @author WangWei
	 * @Description 
	 * @method findAllTxhashNull
	 * @return List<EthTradeNonceCover>
	 * @date 2018年9月13日 下午3:10:05
	 */
	@Query(value = "select * from eth_trade_nonce_cover eth where eth.txhash IS NULL or eth.txhash = \"\"", nativeQuery = true)
	List<EthTradeNonceCover> findAllTxhashNull();
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method findAllFinalStateNull
	 * @return List<EthTradeNonceCover>
	 * @date 2018年9月13日 下午3:59:54
	 */
	@Query(value = "select * from eth_trade_nonce_cover eth where eth.final_state IS NULL or eth.final_state = 0", nativeQuery = true)
	List<EthTradeNonceCover> findAllFinalStateNull(); 
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method findAllByOrderNo
	 * @param orderNo
	 * @return List<EthTradeNonceCover>
	 * @date 2018年9月13日 下午5:53:04
	 */
	List<EthTradeNonceCover> findAllByOrderNo(String orderNo);
	
	@Query(value = ""
			+ "select * from eth_trade_nonce_cover eth "
			+ "where "
			+ "eth.order_no = ?1 "
			+ "AND eth.from_address = ?2 "
			+ "AND eth.to_address = ?3 "
			+ "AND eth.value = ?4 "
			+ "AND eth.gas_price = ?5 "
			+ "AND eth.data = ?6",nativeQuery = true)
	EthTradeNonceCover findSameParam(
			String orderNo
			,String fromAddress
			,String toAddress
			,BigDecimal value
			,BigDecimal gasPrice
			,String data);
}
