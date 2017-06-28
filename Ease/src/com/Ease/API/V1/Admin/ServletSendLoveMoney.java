package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/SendLoveMoney")
public class ServletSendLoveMoney extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            String email = sm.getStringParam("email", true);
            if (email == null || email.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty email.");
            email = email.toLowerCase();
            Integer credit = Integer.parseInt(sm.getStringParam("credit", true));
            if (credit < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Don't be an asshole ^^ ");
            HibernateQuery hiberateQuery = sm.getHibernateQuery();
            hiberateQuery.querySQLString("SELECT team_id FROM teamUsers WHERE email = ?");
            hiberateQuery.setParameter(1, email);
            Integer team_id = (Integer) hiberateQuery.getSingleResult();
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = null;
            if (team_id != null)
                team = teamManager.getTeamWithId(team_id);
            if (team != null && team.getCustomer_id() != null)
                team.increaseAccountBalance(credit);
            else {
                hiberateQuery.querySQLString("SELECT id FROM waitingCredits WHERE email = ?;");
                hiberateQuery.setParameter(1, email);
                Integer id = (Integer) hiberateQuery.getSingleResult();
                if (id == null) {
                    hiberateQuery.querySQLString("INSERT INTO waitingCredits values (null, ?, ?);");
                    hiberateQuery.setParameter(1, email);
                    hiberateQuery.setParameter(2, credit);
                } else {
                    hiberateQuery.querySQLString("UPDATE waitingCredits SET credit = ? WHERE id = ?;");
                    hiberateQuery.setParameter(1, credit);
                    hiberateQuery.setParameter(2, id);
                }
                hiberateQuery.executeUpdate();
            }
            sm.setSuccess("Account credited");
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
