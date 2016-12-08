package com.Ease.Utils;

import java.io.IOException;
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

import com.Ease.Dashboard.User.User;
import com.Ease.websocket.WebsocketMessage;

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
	protected String				tabId;
	protected List<WebsocketMessage> messages;
	
	public ServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) {
		this.args = new HashMap<>();
		this.servletName = servletName;
		this.retMsg = "No message";
		this.retCode = 0;
		this.request = request;
		this.response = response;
		this.user = (User)request.getSession().getAttribute("User");
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
			try {
				response.getWriter().print("1 Sorry an internal problem occurred. We are solving it asap.");
			} catch (IOException e1) {
				e1.printStackTrace();
				System.err.println("Send response failed.");
			}
		}
		
	}
	
	public void needToBeConnected() throws GeneralException {
		if (user == null) {
			throw new GeneralException(Code.ClientWarning, "You need to be connected to do that.");
		} else if ((tabId = request.getParameter("tabId"))== null) {
			throw new GeneralException(Code.ClientError, "No tabId.");
		} else if (user.getWebsockets().containsKey(tabId) == false) {
			throw new GeneralException(Code.ClientError, "Wrong tabId.");
		}
	}
	
	public String getServletParam(String paramName, boolean saveInLogs) {
		String param = request.getParameter(paramName);
		if (saveInLogs)
			args.put(paramName, param);
		return param;
	}
	
	public String[] getServletParamArray(String paramName, boolean saveInLogs) {
		String param[] = request.getParameterValues(paramName);
		if (saveInLogs)
			args.put(paramName, (param != null) ? param.toString() : null);
		return param;
	}
	
	public Map<String, String[]> getServletParametersMap(boolean saveInLogs) {
		Map<String, String[]> params = request.getParameterMap();
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
	
	public void redirect(String url) {
		this.redirectUrl = url;
		this.retCode = 200;
		this.retMsg = "Redirected to " + url;
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
		db.set("insert into logs values('" + this.servletName + "', " + this.retCode + ", " + ((this.user != null) ? this.user.getDBid() : "NULL") + ", '" + argsString + "', '" + this.logResponse + "', '" + this.date + "');");
	}
	
	public void sendResponse() {
		user = ((User)request.getSession().getAttribute("User") == null) ? user : (User)request.getSession().getAttribute("User");
	
		if (this.saveLogs) {
			try {
				saveLogs();
			} catch (GeneralException e) {
				System.err.println("Logs not sended to database.");
			}
		}
		if (this.retCode != Code.Success.getValue() && this.retCode != Code.UserMiss.getValue() && this.retCode != Code.ClientWarning.getValue()) {
			retMsg = "Sorry an internal problem occurred. We are solving it asap.";
			try {
				this.db.rollbackTransaction();
			} catch (GeneralException e) {
				System.err.println("Rollback transaction failed.");
			}
		}
		try {
			for (WebsocketMessage msg: this.messages) {
				this.user.getWebsockets().forEach((key, socket) -> {
					if (msg.getWho() == WebsocketMessage.Who.ALLTABS ||
						(msg.getWho() == WebsocketMessage.Who.OTHERTABS && key != tabId) ||
						(msg.getWho() == WebsocketMessage.Who.THISTAB && key == tabId)) {
						try {
							socket.sendMessage(msg);
						} catch (IOException e) {
							this.user.removeWebsocket(socket);
						}
					}
				});
			}
			if (this.redirectUrl != null) {
				response.sendRedirect(this.redirectUrl);
			} else {
				response.setCharacterEncoding("UTF-8");
				String resp = retCode + " " + retMsg;
				response.getWriter().print(resp);
			}
		} catch (IOException e) {
			System.err.println("Send response failed.");
		}	
	}
	
	public Object getContextAttr(String attr) {
		return request.getServletContext().getAttribute(attr);
	}
	
	public void addToSocket(WebsocketMessage msg) {
		messages.add(msg);
	}
}
