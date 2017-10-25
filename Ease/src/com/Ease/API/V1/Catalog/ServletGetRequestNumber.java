package com.Ease.API.V1.Catalog;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/catalog/GetRequestNumber")
public class ServletGetRequestNumber extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            DataBaseConnection db = sm.getDB();
            DatabaseResult rs = db.prepareRequest("SELECT count(*) FROM websiteRequests;").get();
            if (!rs.next())
                throw new HttpServletException(HttpStatus.InternError);
            Integer request_number = rs.getInt(1);
            JSONObject res = new JSONObject();
            res.put("request_number", request_number);
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
