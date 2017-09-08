package com.Ease.API.V1.Common;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/GetMyInformation")
public class ServletGetMyInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            JSONObject res = new JSONObject();
            res.put("user", (user == null) ? null : user.getJson());
            String session_public_key = (String) sm.getSession().getAttribute("public_key");
            if (session_public_key == null)
                session_public_key = "";
            res.put("session_public_key", session_public_key);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
