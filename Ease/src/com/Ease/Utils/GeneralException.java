package com.Ease.Utils;

public class GeneralException extends Exception{
	private static final long serialVersionUID = 1L;
	protected String 	msg;
	protected ServletManager.Code	code;

	public GeneralException(ServletManager.Code code, String msg){
		this.msg = msg;
		this.code = code;
	}
	
	public GeneralException(ServletManager.Code code, Exception e){
		this.msg = e.toString()+".\nStackTrace :";
		for(int i = 0; i < e.getStackTrace().length; i++){
			this.msg += "\n" + e.getStackTrace()[i];
		}
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public ServletManager.Code getCode() {
		return code;
	}
}
