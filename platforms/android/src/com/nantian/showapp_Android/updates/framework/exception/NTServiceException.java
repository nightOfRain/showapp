package com.nantian.showapp_Android.updates.framework.exception;

/**
 * $Id: NTServiceException.java 2741 2014-05-28 08:01:57Z zhanggangyi $
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTServiceException extends Exception {

	/**  */
	private static final long serialVersionUID = 1834261889069642221L;

	/** 错误码 */
	private String code;

	/**
	 * 
	 */
	public NTServiceException() {
	}

	/**
	 * @param detailMessage
	 */
	public NTServiceException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public NTServiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NTServiceException(String code, String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		this.code = code;
	}

	/**
	 * @param throwable
	 */
	public NTServiceException(Throwable throwable) {
		super(throwable);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
