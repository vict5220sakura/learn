/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月6日  下午4:04:38
 * @version   V 1.0
 */
package com.bithaw.btc.exception;

/**
 * @Description 查找零钱临时不足
 * @author   WangWei
 * @date     2018年9月6日 下午4:04:38
 * @version  V 1.0
 */
public class NotEnoughTempMoney extends RuntimeException {

	/**
	 * serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -1058630708037202386L;

	public NotEnoughTempMoney() {
		super();
	}

	public NotEnoughTempMoney(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotEnoughTempMoney(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughTempMoney(String message) {
		super(message);
	}

	public NotEnoughTempMoney(Throwable cause) {
		super(cause);
	}

}
