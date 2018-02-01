package com.Ease.API.V1.Onboarding;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Onboarding.OnboardingRoom;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/OnboardingRooms")
public class ServletOnboardingRooms extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT o FROM OnboardingRoom o");
            List<OnboardingRoom> onboardingRoomList = hibernateQuery.list();
            JSONArray res = new JSONArray();
            onboardingRoomList.forEach(onboardingRoom -> res.put(onboardingRoom.getJson()));
            sm.setSuccess(res);
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
