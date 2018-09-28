/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午4:08:31
 * @version   V 1.0
 */
package com.bithaw.btc.entity;

import lombok.ToString;

/**
 * @Description 未花费实体,通用实体
 * @author   WangWei
 * @date     2018年8月31日 下午4:08:31
 * @version  V 1.0
 */
@ToString
public class UTXO {
	
	/**
	 * address : 所属地址
	 */
	private String address;
	/**
	 * txid : 零钱hash
	 */
	private String txid;
	/**
	 * index : 未花费序号
	 */
	private String index;
	/**
	 * confirmations : 确认数(整数)
	 */
	private String confirmations;
	/**
	 * value : 金额(单位BTC)
	 */
	private String value;
	/**
	 * scriptPubKey : 锁定脚本
	 */
	private String scriptPubKey;
	
	public UTXO(String address, String txid, String index, String confirmations, String value, String scriptPubKey) {
		super();
		this.address = address;
		this.txid = txid;
		this.index = index;
		this.confirmations = confirmations;
		this.value = value;
		this.scriptPubKey = scriptPubKey;
	}
	
	public static class Builder{
		/**
		 * address : 所属地址
		 */
		private String address;
		/**
		 * txid : 零钱hash
		 */
		private String txid;
		/**
		 * index : 未花费序号
		 */
		private String index;
		/**
		 * confirmations : 确认数(整数)
		 */
		private String confirmations;
		/**
		 * value : 金额(单位BTC)
		 */
		private String value;
		/**
		 * scriptPubKey : 脚本公钥
		 */
		private String scriptPubKey;
		
		public UTXO build(){
			return new UTXO(address, txid, index, confirmations, value, scriptPubKey);
		}
		
		public Builder setTxid(String txid) {
			this.txid = txid;
			return this;
		}
		public Builder setIndex(String index) {
			this.index = index;
			return this;
		}
		public Builder setConfirmations(String confirmations) {
			this.confirmations = confirmations;
			return this;
		}
		/**
		 * @author WangWei
		 * @Description (单位BTC)
		 */
		public Builder setValue(String value) {
			this.value = value;
			return this;
		}
		public Builder setScriptPubKey(String scriptPubKey) {
			this.scriptPubKey = scriptPubKey;
			return this;
		}

		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}
	}
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(String confirmations) {
		this.confirmations = confirmations;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getScriptPubKey() {
		return scriptPubKey;
	}
	public void setScriptPubKey(String scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
