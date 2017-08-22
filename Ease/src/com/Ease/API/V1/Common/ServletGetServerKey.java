package com.Ease.API.V1.Common;

import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.Crypto.ServerAES;
import com.Ease.Utils.Servlets.GetServletManager;
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
            ServerAES serverAES = (ServerAES) sm.getContextAttr("serverAES");
            JSONObject res = new JSONObject();
            res.put("passphrase", RSA.Encrypt(serverAES.getPassphrase(), public_key));
            res.put("salt", RSA.Encrypt(serverAES.getSalt(), public_key));
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
