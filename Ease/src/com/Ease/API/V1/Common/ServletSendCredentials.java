package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/SendCustomerCredentials")
public class ServletSendCredentials extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("INSERT INTO customerCredentialsReception VALUES (null, ?, ?, ?, ?, ?, default);");
            hibernateQuery.setParameter(1, sm.getUser().getEmail());
            hibernateQuery.setParameter(2, sm.getStringParam("url", false));
            hibernateQuery.setParameter(3, sm.getStringParam("login", false));
            hibernateQuery.setParameter(4, sm.getStringParam("password", false));
            hibernateQuery.setParameter(5, sm.getIntParam("serverPublicKey_id", false));
            hibernateQuery.executeUpdate();
            sm.setSuccess("Credentials sent");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
