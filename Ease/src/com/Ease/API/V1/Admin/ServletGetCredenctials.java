package com.Ease.API.V1.Admin;

import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/GetCredentials")
public class ServletGetCredenctials extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            DatabaseResult rs = sm.getDB().prepareRequest("SELECT url, login, password, publicKey FROM customerCredentialsReception JOIN serverPublicKeys ON customerCredentialsReception.serverPublicKey_id = serverPublicKeys.id;").get();
            JSONArray res = new JSONArray();
            while (rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("url", rs.getString("url"));
                tmp.put("login", rs.getString("login"));
                tmp.put("password", rs.getString("password"));
                tmp.put("publicKey", rs.getString("password"));
                res.add(tmp);
            }
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
