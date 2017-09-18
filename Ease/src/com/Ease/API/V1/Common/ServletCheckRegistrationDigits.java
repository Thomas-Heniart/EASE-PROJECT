package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/CheckRegistrationDigits")
public class ServletCheckRegistrationDigits extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, true);
            String digits = sm.getStringParam("digits", true, true);
            if (email == null || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email.");
            if (digits == null || digits.length() != 6)
                throw new HttpServletException(HttpStatus.BadRequest, "That code wasn't valid. Give it another go!");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT digits FROM userPendingRegistrations WHERE email = ?");
            hibernateQuery.setParameter(1, email);
            String db_digits = (String) hibernateQuery.getSingleResult();
            if (db_digits == null || db_digits.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "You didn't ask for an account.");
            if (!db_digits.equals(digits))
                throw new HttpServletException(HttpStatus.BadRequest, "That code wasn't valid. Give it another go!");
            JSONObject res = new JSONObject();
            res.put("valid_digits", true);
            sm.setSuccess(res);
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
