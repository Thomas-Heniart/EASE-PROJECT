package com.Ease.API.V1.Teams;

import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 09/05/2017.
 */
@WebServlet("/api/v1/teams/CheckTeamCreationDigits")
public class ServletCheckTeamCreationDigits extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, true);
            String digits = sm.getStringParam("digits", true, true);
            if (email == null || email.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty email");
            if (digits == null || digits.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty digits");
            DataBaseConnection db = sm.getDB();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT date FROM pendingTeamCreations WHERE email = ? AND digits = ?;");
            databaseRequest.setString(email);
            databaseRequest.setString(digits);
            DatabaseResult rs = databaseRequest.get();
            if (!rs.next())
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid code or email.");
            sm.setSuccess("Code is valid");
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
