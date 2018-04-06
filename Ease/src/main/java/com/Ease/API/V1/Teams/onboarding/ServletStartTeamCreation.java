package com.Ease.API.V1.Teams.onboarding;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.Utils.Slack.SlackAPIWrapper;
import com.Ease.onboarding.OnboardingCustomerInformation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/StartTeamCreation")
public class ServletStartTeamCreation extends HttpServlet {

    private static final String CHANNEL = "CA1PULNGZ";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String teamName = sm.getStringParam("team_name", true, false);
            String email = sm.getStringParam("email", true, false);
            email = email.toLowerCase();
            Integer planId = sm.getIntParam("plan_id", true, false);
            Integer teamSize = sm.getIntParam("team_size", true, false);
            String firstName = sm.getStringParam("first_name", true, false);
            String lastName = sm.getStringParam("last_name", true, false);
            String phoneNumber = sm.getStringParam("phone_number", true, false);
            String digits = sm.getStringParam("digits", false, true);
            if (teamName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "teamName is needed.");
            if (teamSize < 1)
                throw new HttpServletException(HttpStatus.BadRequest, "Company size must be greater than 0");
            if (email.equals("") || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "email is needed.");
            if (firstName.isEmpty() || !Regex.isValidName(firstName))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid firstName");
            if (lastName.isEmpty() || !Regex.isValidName(lastName))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid lastName");
            if (phoneNumber.isEmpty() || !Regex.isPhoneNumber(phoneNumber))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid phoneNumber");
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT id FROM userPendingRegistrations WHERE email = ? AND digits = ?");
            query.setParameter(1, email);
            query.setParameter(2, digits);
            Object id = query.getSingleResult();
            if (id == null)
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot create a team.");
            query.querySQLString("DELETE FROM userPendingRegistrations WHERE id = ?");
            query.setParameter(1, id);
            query.executeUpdate();
            OnboardingCustomerInformation onboardingCustomerInformation = new OnboardingCustomerInformation(email, firstName, lastName, teamName, teamSize, phoneNumber, planId);
            sm.saveOrUpdate(onboardingCustomerInformation);
            onboardingCustomerInformation.generateTeamCreationLink();
            sm.saveOrUpdate(onboardingCustomerInformation);
            String message = firstName +
                    " " +
                    lastName +
                    "\n" +
                    email +
                    "\n" +
                    teamName +
                    " with " +
                    teamSize +
                    " collaborators and wants to subscribe for " +
                    Team.plansMap.get(planId) +
                    " plan\n" +
                    phoneNumber +
                    "\nCreation link: " +
                    onboardingCustomerInformation.getTeamCreationLink() +
                    "\n=======\n=======\n=======";
            SlackAPIWrapper.getInstance().postMessage(CHANNEL, message);
            sm.setSuccess("Success");
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
