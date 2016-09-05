package com.Ease.session;

public class SessionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String msg;
	public SessionException(String msg){
		  this.msg = msg;
		  System.out.println(msg);
	}
	public String getMsg() {
		return msg;
	}
}
