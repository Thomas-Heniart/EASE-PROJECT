package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/DeleteServerPublicKey")
public class ServletDeleteServerPublicKey extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer id = sm.getIntParam("id", false, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("DELETE FROM customerCredentialsReception WHERE serverPublicKey_id = ?");
            hibernateQuery.setParameter(1, id);
            hibernateQuery.executeUpdate();
            hibernateQuery.querySQLString("DELETE FROM serverPublicKeys WHERE id = ?;");
            hibernateQuery.setParameter(1, id);
            hibernateQuery.executeUpdate();
            sm.setSuccess("Public key deleted");
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
