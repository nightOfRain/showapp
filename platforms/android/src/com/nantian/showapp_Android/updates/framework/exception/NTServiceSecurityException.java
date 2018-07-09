package com.nantian.showapp_Android.updates.framework.exception;

/**
 * $Id: ServiceSecurityException.java 584 2014-03-19 08:38:18Z zhangyb $
 * 
 * 服务安全异常，在发生该异常的情况下需要用户登录
 * 
 * @author Genty
 * 
 */
public class NTServiceSecurityException extends NTServiceException {

	/**  */
	private static final long serialVersionUID = 1721212524382752075L;

	public NTServiceSecurityException(String code, String detailMessage, Throwable throwable) {
		super(code, detailMessage, throwable);
	}

}
