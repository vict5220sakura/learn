/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月27日  上午10:45:43
 * @version   V 1.0
 */
package com.bithaw.zbt.exception;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月27日 上午10:45:43
 * @version  V 1.0
 */
public class FindUnUseNonceException extends RuntimeException {

	/**
	 * serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -7689972084951940093L;

	public FindUnUseNonceException() {
		super();
	}

	public FindUnUseNonceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FindUnUseNonceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FindUnUseNonceException(String message) {
		super(message);
	}

	public FindUnUseNonceException(Throwable cause) {
		super(cause);
	}
	
}
