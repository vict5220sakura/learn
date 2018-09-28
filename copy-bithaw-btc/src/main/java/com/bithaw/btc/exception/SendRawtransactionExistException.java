/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月6日  下午7:59:40
 * @version   V 1.0
 */
package com.bithaw.btc.exception;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月6日 下午7:59:40
 * @version  V 1.0
 */
public class SendRawtransactionExistException extends RuntimeException {

	public SendRawtransactionExistException() {
		super();
	}

	public SendRawtransactionExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SendRawtransactionExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SendRawtransactionExistException(String message) {
		super(message);
	}

	public SendRawtransactionExistException(Throwable cause) {
		super(cause);
	}

}
