package com.Ease.API.V1.Clearbit;

import com.Ease.Context.Variables;
import com.Ease.Utils.Clearbit.EnrichmentAPI;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/clearbit/GetEmailRelatedInformation")
public class GetEmailRelatedInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String email = sm.getParam("email", true, false);
            EnrichmentAPI enrichmentAPI = new EnrichmentAPI(Variables.CLEARBIT_KEY);
            sm.setSuccess(enrichmentAPI.emailLookup(email));
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
