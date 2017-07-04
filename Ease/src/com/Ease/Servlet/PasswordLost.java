package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Variables;
import com.Ease.Dashboard.User.Keys;
import com.Ease.Dashboard.User.User;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.*;

/**
 * Servlet implementation class PasswordLost
 */
@WebServlet("/passwordLost")
public class PasswordLost extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordLost() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) (session.getAttribute("user"));
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

        String email = sm.getServletParam("email", true);

        try {
            if (user != null) {
                Logout.logoutUser(user, sm); //throw new GeneralException(ServletManager.Code.ClientWarning, "You are logged on Ease.");
            }
            if (email == null || !Regex.isEmail(email)) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email format.");
            }
            try {
                String userId = User.findDBid(email, sm);
                Keys.passwordLost(email, userId, sm);
                TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
                DataBaseConnection db = sm.getDB();
                DatabaseRequest databaseRequest = db.prepareRequest("SELECT firstName, lastName, team_id FROM teamUsers WHERE user_id = ?;");
                databaseRequest.setInt(userId);
                DatabaseResult rs = databaseRequest.get();
                while (rs.next()) {
                    String firstName = rs.getString(1);
                    String lastName = rs.getString(2);
                    Team team = teamManager.getTeamWithId(rs.getInt(3));
                    MailJetBuilder mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setTemplateId(0);
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                    for (Map.Entry<String, String> entry : team.getAdministratorsUsernameAndEmail().entrySet())
                        mailJetBuilder.addTo(entry.getValue(), entry.getKey());
                    mailJetBuilder.addVariable("first_name", firstName);
                    mailJetBuilder.addVariable("last_name", lastName);
                    mailJetBuilder.sendEmail();
                }
                sm.setResponse(ServletManager.Code.Success, "Email sent.");
            } catch (GeneralException e) {
                if (e.getCode() == ServletManager.Code.ClientError) {
                    throw new GeneralException(ServletManager.Code.UserMiss, "Email sent.");
                } else {
                    throw new GeneralException(ServletManager.Code.InternError, e);
                }
            }
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }
}
