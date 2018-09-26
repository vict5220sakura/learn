package com.bithaw.zbt.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bithaw.zbt.entity.EthTradeNonce;

/**
 * @Description EthTradeNonce Jpa Dao
 * @author WangWei
 * @date 2018年8月24日 上午11:19:03
 * @version V 1.0
 */
public interface EthTradeNonceMapper extends JpaRepository<EthTradeNonce, Integer> {
	/**
	 * @author WangWei
	 * @Description 根据orderNo查找实体类
	 * @method findByorderNo
	 * @param @param orderNo
	 * @param @return 
	 * @return EthTradeNonce
	 * @date 2018年8月24日 上午11:19:33
	 */
	EthTradeNonce findByorderNo(String orderNo); 
	
	/**
	 * @author WangWei
	 * @Description 根据FromAddress查询对应的交易集合
	 * @method findAllByFromAddress
	 * @param @param fromAddress
	 * @param @return 
	 * @return List<EthTradeNonce>
	 * @date 2018年8月24日 上午11:19:59
	 */
	List<EthTradeNonce> findAllByFromAddress(String fromAddress);

	/**
	 * @author WangWei
	 * @Description 查找一个FromAddress的nonce数组
	 * @method findNonceByFromAddress
	 * @param @param fromAddress
	 * @param @return 
	 * @return int[]
	 * @date 2018年8月24日 上午11:20:23
	 */
	@Query(value = "select e.nonce from eth_trade_nonce e where e.from_address = ?1 and !ISNULL(e.nonce)", nativeQuery = true)
	List<Integer> findNonceByFromAddress(String fromAddress);

	/**
	 * @author WangWei
	 * @Description 根据state状态查询实体数组
	 * @method findAllByState
	 * @param @param state
	 * @param @return 
	 * @return List<EthTradeNonce>
	 * @date 2018年8月24日 上午11:20:48
	 */
	List<EthTradeNonce> findAllByState(int state);

	/**
	 * @author WangWei
	 * @Description 查询没有txhash的交易集合
	 * @method findAllTxhashNull
	 * @param @return 
	 * @return List<EthTradeNonce>
	 * @date 2018年8月24日 上午11:21:09
	 */
	@Query(value = "select * from eth_trade_nonce eth where eth.txhash IS NULL or eth.txhash = \"\"", nativeQuery = true)
	List<EthTradeNonce> findAllTxhashNull();
	
	/**
	 * @author WangWei
	 * @Description 找到所有没有签名的交易数据
	 * @method findAllRawTranssactionNull
	 * @return 
	 * @return List<EthTradeNonce>
	 * @date 2018年8月27日 下午3:25:41
	 */
	@Query(value = "select * from eth_trade_nonce eth where eth.raw_transaction IS NULL or eth.raw_transaction = \"\"", nativeQuery = true)
	List<EthTradeNonce> findAllRawTranssactionNull();
	
	/**
	 * @author WangWei
	 * @Description 查询state = 0的fromAddress列表,去除重复
	 * @method findFromAddresses
	 * @return List<String>
	 * @date 2018年9月10日 上午11:17:08
	 */
	@Query(value = "select DISTINCT eth.from_address from eth_trade_nonce eth where eth.state = 0", nativeQuery = true)
	List<String> findFromAddressesList();
	
	/**
	 * @author WangWei
	 * @Description 根据fromaddress 查询 state = 3 的交易,createtime排序
	 * @method findAllNonceNull
	 * @param fromAddress
	 * @return List<EthTradeNonce>
	 * @date 2018年9月10日 下午1:49:51
	 */
	@Query(value = "select * from eth_trade_nonce eth where eth.from_address = ?1 and state = 0 order by eth.create_time asc", nativeQuery = true)
	List<EthTradeNonce> findAllNonceNull(String fromAddress);
}