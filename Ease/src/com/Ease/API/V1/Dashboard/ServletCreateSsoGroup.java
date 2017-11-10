package com.Ease.API.V1.Dashboard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Sso;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.SsoGroup;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/CreateSsoGroup")
public class ServletCreateSsoGroup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer sso_id = sm.getIntParam("sso_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Sso sso = catalog.getSsoWithId(sso_id);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            if (account_information.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "account information cannot be empty");
            Account account = AccountFactory.getInstance().createAccountFromJson(account_information, sm.getUser().getKeys().getKeyUser(), 0);
            SsoGroup ssoGroup = new SsoGroup(Integer.valueOf(sm.getUser().getDBid()), sso, account);
            sm.saveOrUpdate(ssoGroup);
            sm.setSuccess(ssoGroup.getJson());
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
