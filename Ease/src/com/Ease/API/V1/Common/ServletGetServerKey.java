package com.Ease.API.V1.Common;

import com.Ease.Utils.Crypto.RSA;
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

@WebServlet("/api/v1/common/GetServerKey")
public class ServletGetServerKey extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String public_key = sm.getParam("public_key", false);
            String server_public_key = (String) sm.getContextAttr("publicKey");
            String server_private_key = (String) sm.getContextAttr("privateKey");
            JSONArray res_private_key = new JSONArray();
            int index = 0;
            while (index < server_private_key.length()) {
                res_private_key.add(RSA.Encrypt(server_private_key.substring(index, Math.min(index + 50, server_private_key.length())), public_key));
                index += 50;
            }
            JSONObject res = new JSONObject();
            res.put("server_public_key", server_public_key);
            res.put("server_private_key", res_private_key);
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
