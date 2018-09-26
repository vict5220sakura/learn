package com.bithaw.zbt.entity;

/**
 * Etherscan第三方结构获取一个账号的交易信息实体
 * @author WangWei
 * @date: 2018年8月24日 上午10:17:51
 * @version: v1.0.0
 * @Description: Etherscan第三方结构获取一个账号的交易信息实体
 */
public class EtherscanTxByTxlist {
	private String blockNumber;//确认区块高度
	private String timeStamp;//时间戳 秒
	private String hash;//交易hash
	private String nonce;//交易nonce
	private String blockHash;//区块hash
	private String transactionIndex;//随机数
	private String from;//fromAddress
	private String to;//toAddress
	private String value;//value wei
	private String gas;//gaslimit
	private String gasPrice;//wei
	private String isError;//0没错,1有错
	private String txreceipt_status;//收据状态 1确认
	private String input;//输入数据
	private String contractAddress;//合约地址 一般为空
	private String cumulativeGasUsed;//不明
	private String gasUsed;//花费gas数
	private String confirmations;//目前确认数
	
	
	public EtherscanTxByTxlist(String blockNumber, String timeStamp, String hash, String nonce, String blockHash, String transactionIndex, String from, String to, String value, String gas, String gasPrice, String isError, String txreceipt_status, String input, String contractAddress, String cumulativeGasUsed, String gasUsed, String confirmations) {
		super();
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.nonce = nonce;
		this.blockHash = blockHash;
		this.transactionIndex = transactionIndex;
		this.from = from;
		this.to = to;
		this.value = value;
		this.gas = gas;
		this.gasPrice = gasPrice;
		this.isError = isError;
		this.txreceipt_status = txreceipt_status;
		this.input = input;
		this.contractAddress = contractAddress;
		this.cumulativeGasUsed = cumulativeGasUsed;
		this.gasUsed = gasUsed;
		this.confirmations = confirmations;
	}
	public static class Builder{
		public EtherscanTxByTxlist build(){
			return new EtherscanTxByTxlist(blockNumber, timeStamp, hash, nonce, blockHash, transactionIndex, from, to, value, gas, gasPrice, isError, txreceipt_status, input, contractAddress, cumulativeGasUsed, cumulativeGasUsed, confirmations);
		}
		private String blockNumber;//确认区块高度
		private String timeStamp;//时间戳 秒
		private String hash;//交易hash
		private String nonce;//交易nonce
		private String blockHash;//区块hash
		private String transactionIndex;//随机数
		private String from;//fromAddress
		private String to;//toAddress
		private String value;//value wei
		private String gas;//gaslimit
		private String gasPrice;//wei
		private String isError;//0没错,1有错
		private String txreceipt_status;//收据状态 1确认
		private String input;//输入数据
		private String contractAddress;//合约地址 一般为空
		private String cumulativeGasUsed;//不明
		private String gasUsed;//花费gas数
		private String confirmations;//目前确认数
		public Builder setBlockNumber(String blockNumber) {
			this.blockNumber = blockNumber;
			return this;
		}
		public Builder setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
			return this;
		}
		public Builder setHash(String hash) {
			this.hash = hash;
			return this;
		}
		public Builder setNonce(String nonce) {
			this.nonce = nonce;
			return this;
		}
		public Builder setBlockHash(String blockHash) {
			this.blockHash = blockHash;
			return this;
		}
		public Builder setTransactionIndex(String transactionIndex) {
			this.transactionIndex = transactionIndex;
			return this;
		}
		public Builder setFrom(String from) {
			this.from = from;
			return this;
		}
		public Builder setTo(String to) {
			this.to = to;
			return this;
		}
		public Builder setValue(String value) {
			this.value = value;
			return this;
		}
		public Builder setGas(String gas) {
			this.gas = gas;
			return this;
		}
		public Builder setGasPrice(String gasPrice) {
			this.gasPrice = gasPrice;
			return this;
		}
		public Builder setIsError(String isError) {
			this.isError = isError;
			return this;
		}
		public Builder setTxreceipt_status(String txreceipt_status) {
			this.txreceipt_status = txreceipt_status;
			return this;
		}
		public Builder setInput(String input) {
			this.input = input;
			return this;
		}
		public Builder setContractAddress(String contractAddress) {
			this.contractAddress = contractAddress;
			return this;
		}
		public Builder setCumulativeGasUsed(String cumulativeGasUsed) {
			this.cumulativeGasUsed = cumulativeGasUsed;
			return this;
		}
		public Builder setGasUsed(String gasUsed) {
			this.gasUsed = gasUsed;
			return this;
		}
		public Builder setConfirmations(String confirmations) {
			this.confirmations = confirmations;
			return this;
		}
	}
	
	public String getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	public String getTransactionIndex() {
		return transactionIndex;
	}
	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getGas() {
		return gas;
	}
	public void setGas(String gas) {
		this.gas = gas;
	}
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	public String getIsError() {
		return isError;
	}
	public void setIsError(String isError) {
		this.isError = isError;
	}
	public String getTxreceipt_status() {
		return txreceipt_status;
	}
	public void setTxreceipt_status(String txreceipt_status) {
		this.txreceipt_status = txreceipt_status;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getContractAddress() {
		return contractAddress;
	}
	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}
	public String getCumulativeGasUsed() {
		return cumulativeGasUsed;
	}
	public void setCumulativeGasUsed(String cumulativeGasUsed) {
		this.cumulativeGasUsed = cumulativeGasUsed;
	}
	public String getGasUsed() {
		return gasUsed;
	}
	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}
	public String getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(String confirmations) {
		this.confirmations = confirmations;
	}
	
}
