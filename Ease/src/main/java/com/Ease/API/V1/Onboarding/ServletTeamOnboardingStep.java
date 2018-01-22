package com.Ease.API.V1.Onboarding;

import com.Ease.Team.Onboarding.OnboardingSteps;
import com.Ease.Team.OnboardingStatus;
import com.Ease.Team.Team;
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

@WebServlet("/api/v1/TeamOnboardingStep")
public class ServletTeamOnboardingStep extends HttpServlet {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeOwnerOfTeam(team);
            OnboardingStatus onboardingStatus = team.getOnboardingStatus();
            Integer step = sm.getIntParam("step", true, false);
            if (step < onboardingStatus.getStep() || step > OnboardingSteps.TEAM_CARDS_CREATED.ordinal())
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter step");
            onboardingStatus.setStep(step);
            sm.saveOrUpdate(onboardingStatus);
            sm.setSuccess("Done.");
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
