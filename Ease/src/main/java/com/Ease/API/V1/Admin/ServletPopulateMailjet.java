package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/PopulateMailjet")
public class ServletPopulateMailjet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.userStatus.registered is true AND u.email NOT LIKE '%@ieseg.fr'");
            List<User> users = hibernateQuery.list();
            System.out.println(users.size());
            for (User user : users) {
                MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
                mailjetContactWrapper.addEmailToList(user.getEmail(), 36734L);
            }
            hibernateQuery.queryString("SELECT t FROM TeamUser t WHERE t.team.active is true AND t.user is not null AND t.user.userStatus.registered is true");
            List<TeamUser> teamUsers = hibernateQuery.list();
            for (TeamUser teamUser : teamUsers) {
                MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
                mailjetContactWrapper.addEmailToList(teamUser.getUser().getEmail(), 36735L);
            }
            sm.setSuccess("Done");
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
