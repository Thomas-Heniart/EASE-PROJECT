package com.Ease.context;

public class AdminMessage {
	protected String message;
	protected String color;
	protected boolean visible;

	public AdminMessage() {
		message = "";
		visible = false;
		color = "";
	}
	
	public void setColor(String color){
		this.color = color;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public void setVisibility(boolean visible){
		this.visible = visible;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean visible(){
		return visible;
	}
}
