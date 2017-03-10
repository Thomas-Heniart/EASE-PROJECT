package com.Ease.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;

import com.Ease.Dashboard.User.User;
import com.Ease.websocket.WebsocketMessage;
import com.Ease.websocket.WebsocketSession;

public class ServletManager {
	
	public enum Code {
	    InternError(1),
	    ClientError(2),
	    ClientWarning(3),
	    UserMiss(4),
	    Success(200);

	    private final int code;
	    Code(int code) { this.code = code; }
	    public int getValue() { return code; }
	}
	
	protected HttpServletRequest 	request;
	protected HttpServletResponse	response;
	protected Integer				retCode;
	protected String 				retMsg;
	protected String				redirectUrl;
	protected String 				servletName;
	protected Map<String, String> 	args;
	protected User					user;
	protected DataBaseConnection	db;
	protected boolean				saveLogs;
	protected String				logResponse;
	protected String				date;
	protected String				socketId;
	protected List<WebsocketMessage> messages;
	
	public Map<String, WebsocketSession> websockets = new HashMap<String, WebsocketSession>();
	public static boolean debug = true;
	
	public ServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) {
		this.args = new HashMap<>();
		this.servletName = servletName;
		this.retMsg = "No message";
		this.retCode = 0;
		this.request = request;
		this.response = response;
		this.user = (User)request.getSession().getAttribute("user");
		this.logResponse = null;
		this.redirectUrl = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mydate = new Date();
		this.saveLogs = saveLogs;
		this.date = dateFormat.format(mydate);
		this.messages = new LinkedList<WebsocketMessage>();
		try {
			this.db = new DataBaseConnection(DataBase.getConnection());
		} catch (SQLException e) {
			/* Do nothing */
		}
	}
	
	public void needToBeConnected() throws GeneralException {
		if (user == null) {
			throw new GeneralException(Code.ClientWarning, "You need to be connected to do that.");
		} else {
			/*socketId = request.getParameter("socketId");
			if (!debug && socketId == null) {
				throw new GeneralException(Code.ClientError, "No socketId.");
			} else if (user.getWebsockets().containsKey(socketId) == false) {
				System.out.println(user.getWebsockets().size());
				throw new GeneralException(Code.ClientError, "Wrong socketId.");
			}*/
		}
	}
	
	public HttpServletRequest getRequest(){
		return request;
	}
	
	public HttpServletResponse getResponse(){
		return response;
	}
	
	public String getServletParam(String paramName, boolean saveInLogs) {
		String param = StringEscapeUtils.escapeHtml4(request.getParameter(paramName));
		if (saveInLogs)
			args.put(paramName, param);
		return param;
	}
	
	public String[] getServletParamArray(String paramName, boolean saveInLogs) {
		String[] param = request.getParameterValues(paramName);
		for(int i=0; i<param.length; i++)
			param[i] = StringEscapeUtils.escapeHtml4(param[i]);
		if (saveInLogs)
			args.put(paramName, (param != null) ? param.toString() : null);
		return param;
	}
	
	public Map<String, String[]> getServletParametersMap(boolean saveInLogs) {
		Map<String, String[]> params = request.getParameterMap();
		for(Map.Entry<String, String[]> entry : params.entrySet()) {
			for(int i=0; i < entry.getValue().length; entry.getValue())
				entry.getValue()[i] = StringEscapeUtils.escapeHtml4(entry.getValue()[i]);
		}
		if (saveInLogs)
			args.put("parameters map", (params != null) ? params.toString() : null);
		return params;
	}
	
	public DataBaseConnection getDB() {
		return this.db;
	}
	
	public void setResponse(Code code, String msg) {
		this.retCode = code.getValue();
		this.retMsg = msg;
	}
	
	public void setResponse(GeneralException e) {
		System.out.println(e.getMsg());
		this.retCode = e.getCode().getValue();
		this.retMsg = e.getMsg();
	}
	
	public void setResponse(Exception e) {
		this.retCode = Code.InternError.getValue();
		this.retMsg = e.toString()+".\nStackTrace :";
		for(int i = 0; i < e.getStackTrace().length; i++){
			this.retMsg += "\n" + e.getStackTrace()[i];
		}
		e.printStackTrace();
	}
	
	public void setRedirectUrl(String url) {
		this.redirectUrl = url;
	}
	
	public void setLogResponse(String msg) {
		this.logResponse = msg;
	}
	
	private void saveLogs() throws GeneralException {
		String argsString = "";
		Set<Entry<String, String>> setHm = args.entrySet();
	    Iterator<Entry<String, String>> it = setHm.iterator();
	    while (it.hasNext()){
	    	Entry<String, String> e = it.next();
	        argsString += "<" + e.getKey() + ":" + e.getValue() + ">";
	    }
		if (this.logResponse == null)
			this.logResponse = retMsg;
		try {
			this.logResponse = URLEncoder.encode(this.logResponse, "UTF-8");
			argsString = URLEncoder.encode(argsString, "UTF-8");
			System.err.println("insert into logs values('" + this.servletName + "', " + this.retCode + ", " + ((this.user != null) ? this.user.getDBid() : "NULL") + ", '" + argsString + "', '" + this.logResponse + "', '" + this.date + "');");
			db.set("insert into logs values('" + this.servletName + "', " + this.retCode + ", " + ((this.user != null) ? this.user.getDBid() : "NULL") + ", '" + argsString + "', '" + this.logResponse + "', '" + this.date + "');");
		} catch (UnsupportedEncodingException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
	
	public void sendResponse() {
		user = ((User)request.getSession().getAttribute("user") == null) ? user : (User)request.getSession().getAttribute("user");
	
		if (this.saveLogs) {
			try {
				saveLogs();
			} catch (GeneralException e) {
				System.out.println(e.getMsg());
				System.err.println("Logs not sended to database.");
			}
		}
		
		if (this.retCode != Code.Success.getValue() && this.retCode != Code.UserMiss.getValue() && this.retCode != Code.ClientWarning.getValue()) {
			retMsg = "Sorry an internal problem occurred. We are solving it asap.";	
		}
		
		if (this.retCode != Code.Success.getValue()) {
			try {
				this.db.rollbackTransaction();
			} catch (GeneralException e) {
				System.err.println("Rollback transaction failed.");
			}
		}
		
		try {
			//System.out.println("wMessages loop start");
			for (WebsocketMessage msg : this.messages) {
				websockets.forEach((key, socket) -> {
					System.out.println( (user == null ? "No user" : user.getFirstName()) + " client socketId : " + key + ", sm socketId : " + socketId);
					if (msg.getWho() == WebsocketMessage.Who.ALLTABS ||
							(msg.getWho() == WebsocketMessage.Who.OTHERTABS && (! key.equals(socketId))) ||
							(msg.getWho() == WebsocketMessage.Who.THISTAB && key.equals(socketId))) {
							try {
								System.out.println("Send message to " + key);
								socket.sendMessage(msg);
								System.out.println("Message sent to " + key);
							} catch (IOException e) {
								websockets.remove(key, socket);
							}
						}
				});
			}
			//System.out.println("wMessages loop done");
			if (this.redirectUrl != null) {
				System.out.println("redirect to "+this.redirectUrl);
				response.sendRedirect(this.redirectUrl);
			} else {
				response.setCharacterEncoding("UTF-8");
				String resp = retCode + " " + retMsg;
				response.getWriter().print(resp);
			}
		} catch (IOException e) {
			System.err.println("Send response failed.");
		}
		db.close();
	}
	
	public Object getContextAttr(String attr) {
		return request.getServletContext().getAttribute(attr);
	}
	
	public User getUser(){
		return user;
	}
	
	public void setSocketId(String socketId) throws GeneralException {
		if (debug && socketId == null)
			return;
		else if (socketId == null)
			throw new GeneralException(Code.ClientError, "Socket id is null");
		this.socketId = socketId;
	}
	
	public void addWebsockets(Map<String, WebsocketSession> websocketsMap) throws GeneralException {
		if (debug && websocketsMap == null)
			return;
		else if (websocketsMap == null)
			throw new GeneralException(Code.ClientError, "WebsocketsMap is null");
		websockets.putAll(websocketsMap);
	}
	
	public void addWebsocket(String tabId, WebsocketSession websocket) throws GeneralException {
		if (debug && (websocket == null || tabId == null))
			return;
		else if (websocket == null || tabId == null)
			throw new GeneralException(Code.ClientError, "Websocket or tabId is null");
		websockets.put(tabId, websocket);
	}
	
	public void removeWebsocket(String tabId) throws GeneralException {
		if (debug && tabId == null)
			return;
		else if (tabId == null)
			throw new GeneralException(Code.ClientError, "tabId is null");
		websockets.remove(tabId);
	}
	
	public void addToSocket(WebsocketMessage msg) throws GeneralException {
		if (debug && msg == null)
			return;
		else if (msg == null)
			throw new GeneralException(Code.ClientError, "msg is null");
		messages.add(msg);
	}
	
	public HttpSession getSession() {
		return request.getSession();
	}
}
