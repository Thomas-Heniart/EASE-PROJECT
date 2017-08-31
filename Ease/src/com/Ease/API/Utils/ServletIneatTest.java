package com.Ease.API.Utils;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.mailjet.client.resource.ContactslistManageContact;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@WebServlet("/IneatTest")
public class ServletIneatTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user != null)
                user.logoutFromSession(sm.getSession().getId(), sm.getServletContext(), sm.getDB());
            String username = "Ineat";
            String email = "ineat@test.com";
            String password = "L0tmTHRl";
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            User newUser = User.createUser(email, username, password, new Date().getTime(), sm.getServletContext(), db);
            db.commitTransaction(transaction);
            sm.setSuccess(newUser.getJson());
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
