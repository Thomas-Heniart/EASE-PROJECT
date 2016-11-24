package com.Ease.websocket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.Ease.data.ServletItem;
import com.Ease.model.Device;

/**
 * Servlet implementation class AddDeviceServlet
 */
@WebServlet("/addDevice")
public class AddDeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDeviceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DeviceSessionHandler sessionHandler = DeviceSessionHandler.getInstance();
		ServletItem SI = new ServletItem(ServletItem.Type.UpdateAdminMessage, request, response, null);
		String name = SI.getServletParam("name");
		String type = SI.getServletParam("type");
		String description = SI.getServletParam("description");
		Device device = new Device();
		device.setName(name);
		device.setDescription(description);
		device.setType(type);
		device.setStatus("Off");
		JSONObject res = sessionHandler.createAddMessage(device);
		SI.setResponse(200, res.toString());
		sessionHandler.addDevice(device);
		SI.sendResponse();
	}

}
