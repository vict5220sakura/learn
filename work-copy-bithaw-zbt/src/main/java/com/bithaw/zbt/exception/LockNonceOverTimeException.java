package com.bithaw.zbt.exception;

/**
 * @Description redis分布式锁超时异常
 * @author   WangWei
 * @date     2018年8月24日 上午11:01:00
 * @version  V 1.0
 */
public class LockNonceOverTimeException extends Exception {
	private static final long serialVersionUID = 3374230300108111468L;

	public LockNonceOverTimeException(String message) {
		super(message);
	}

}
