package com.Ease.Servlet.Company;

import com.Ease.Dashboard.User.User;
import com.Ease.Team.Company;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by thomas on 12/04/2017.
 */
@WebServlet("/CreateCompany")
public class CreateCompany extends HttpServlet {

    public CreateCompany() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            if (!user.isAdmin())
                throw new GeneralException(ServletManager.Code.ClientError, "You ain't admin dude");
            String companyName = sm.getServletParam("companyName", true);
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String email = sm.getServletParam("email", true);
            Company company = Company.createCompany(companyName, email, firstName, lastName, sm);
            Map<String, Company> companyMap = (Map<String, Company>) sm.getContextAttr("companyMap");
            companyMap.put(company.getDb_id(), company);
            sm.setResponse(ServletManager.Code.Success, company.getJson().toString());
            sm.setLogResponse("Company created");
        } catch (GeneralException e) {
            sm.setResponse(e);
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
