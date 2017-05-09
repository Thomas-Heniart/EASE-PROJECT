package com.Ease.API.V1.Teams;

import com.Ease.API.Utils.ServletManager;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thomas on 09/05/2017.
 */
@WebServlet("/ServletCheckTeamCreationCode")
public class ServletCheckTeamCreationCode extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getServletParam("email", true);
            String digits = sm.getServletParam("digits", true);
            if (email == null || email.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Empty email");
            if (digits == null || digits.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Empty digits");
            DataBaseConnection db = sm.getDB();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT expiration_date FROM createTeamInvitations WHERE email = ? AND code = ?;");
            databaseRequest.setString(email);
            databaseRequest.setString(digits);
            DatabaseResult rs = databaseRequest.get();
            if (!rs.next())
                throw new GeneralException(ServletManager.Code.ClientWarning, "Invalid code or email.");
            String dateString = rs.getString(1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expiration_date = dateFormat.parse(dateString);
            Date now = new Date();
            if (now.compareTo(expiration_date) > 0)
                    throw new GeneralException(ServletManager.Code.ClientWarning, "Your code has expired.");
            sm.setResponse(ServletManager.Code.Success, "Code is valid");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
