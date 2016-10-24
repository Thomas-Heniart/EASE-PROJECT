package com.Ease.data;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.context.DataBase;
import com.Ease.session.User;

public class ServletItem {
	public enum Type
	{
		AddApp,
		AddAppSso,
		AddLogWith,
		AddProfile,
		AddTag,
		AddWebsite,
		AskForNewApp,
		AskInfo,
		CatalogSearchServlet,
		ConnectionServlet,
		CreateInvitation,
		DeleteApp,
		DeleteProfile,
		EditApp,
		EditProfileColor,
		EditProfileName,
		EditUserEmail,
		EditUserName,
		EditUserPassword,
		EraseRequestedWebsiteServlet,
		GetMailLink,
		GetTags,
		Logout,
		MoveApp,
		MoveProfile,
		NewUser,
		PasswordLost,
		RegistrationByInvitation,
		RequestedWebsitesServlet,
		ResetUser,
		SetTags,
		UploadWebsiteServlet,
		AdminStats,
		ChangeBackground,
		SaveSessionServlet,
		AddEmail
	}
	public enum Code
	{
		NotConnected,
		DatabaseNotConnected,
		BadParameters,
		LogicError,
		NoPermission,
		AlreadyConnected,
		EMailNotSended,
		Constraints
	}
	
	protected Map<String, String> 	args;
	protected User					user;
	protected Type 					type;
	protected String 				retMsg;
	protected Integer				retCode;
	protected HttpServletRequest 	request;
	protected HttpServletResponse	response;
	
	
	public ServletItem(Type type, HttpServletRequest request, HttpServletResponse response, User user) {
		args = new HashMap<>();
		this.type = type;
		retMsg = "No message";
		retCode = 0;
		this.request = request;
		this.response = response;
		this.user = user;
		DataBase db = (DataBase)request.getSession().getServletContext().getAttribute("DataBase");
		db.connect();
	}
	
	//Getter
	
	public Map<String, String> getArgs() {
		return args;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getMsg() {
		return retMsg;
	}
	
	public Integer getCode() {
		return retCode;
	}
	
	//Setter
	
	public String getServletParam(String paramName) {
		String param = request.getParameter(paramName);
		args.put(paramName, param);
		return param;
	}
	
	public String[] getServletParamValues(String paramName) {
		String param[] = request.getParameterValues(paramName);
		args.put(paramName, (param != null) ? param.toString() : null);
		return param;
	}
	
	public void setResponse(Code code, String msg){
		this.retCode = code.ordinal();
		this.retMsg = msg;
	}
	public void setResponse(Integer code, String msg){
		this.retCode = code;
		this.retMsg = msg;
	}
	
	protected void saveInDB(DataBase db) {
		String argsString = "";
		Set<Entry<String, String>> setHm = args.entrySet();
	    Iterator<Entry<String, String>> it = setHm.iterator();
	    while (it.hasNext()){
	    	Entry<String, String> e = it.next();
	        argsString += "<" + e.getKey() + ":" + e.getValue() + ">";
	    }
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		db.set("insert into logs values(" + type.ordinal() + ", " + retCode + ", " + ((user != null) ? user.getId() : "NULL") + ", '" + argsString + "', '" + retMsg + "', '" + dateFormat.format(date) + "');");
	}
	
	public void sendResponse() throws IOException {
		DataBase db = (DataBase)request.getSession().getServletContext().getAttribute("DataBase");
		user = ((User)request.getSession().getAttribute("User") == null) ? user : (User)request.getSession().getAttribute("User");
		String ret = retMsg;
		if (type == Type.AskInfo && retCode == 200) {
			retMsg = "Info sended.";
		} else if (type == Type.SaveSessionServlet && retCode == 200) {
			retMsg = "Session saved for user_id "+user.getId();
		}
		if (retCode != Code.DatabaseNotConnected.ordinal() && type != Type.CatalogSearchServlet && type != Type.RequestedWebsitesServlet)
			saveInDB(db);

		if (retCode == Code.LogicError.ordinal()) {
			retMsg = "Sorry, an intern problem as occured. We attempt to resolve this.";
		} else {
			retMsg = ret;
		}
		response.setCharacterEncoding("UTF-8");
		String resp = retCode + " " + retMsg;
		response.getWriter().print(resp);
	}
}