package com.Ease.API.V1.Admin.onboarding;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.onboarding.OnboardingCustomerInformation;
import org.json.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/onboarding/onboarding-customer-information")
public class ServletOnboardingCustomerInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT o FROM OnboardingCustomerInformation o ORDER BY o.creationDate");
            List<OnboardingCustomerInformation> onboardingCustomerInformationList = hibernateQuery.list();
            JSONArray res = new JSONArray();
            onboardingCustomerInformationList.forEach(onboardingCustomerInformation -> res.put(onboardingCustomerInformation.getJson()));
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
