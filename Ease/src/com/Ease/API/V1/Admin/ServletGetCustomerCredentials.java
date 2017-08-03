package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
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

@WebServlet("/api/v1/admin/GetCustomerCredentials")
public class ServletGetCustomerCredentials extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT url, login, password, publicKey FROM customerCredentialsReception JOIN serverPublicKeys ON customerCredentialsReception.serverPublicKey_id = serverPublicKeys.id ORDER BY customerCredentialsReception.serverPublicKey_id;");
            JSONArray res = new JSONArray();
            for (Object result : hibernateQuery.list()) {
                JSONObject tmp = new JSONObject();
                Object[] columns = (Object[]) result;
                tmp.put("url", columns[0]);
                tmp.put("login", columns[1]);
                tmp.put("password", columns[2]);
                tmp.put("public_key", columns[3]);
                res.add(tmp);
            }
            hibernateQuery.commit();
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
