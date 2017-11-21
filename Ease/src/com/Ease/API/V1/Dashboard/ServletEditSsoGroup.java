package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.SsoGroup;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/EditSsoGroup")
public class ServletEditSsoGroup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer sso_group_id = sm.getIntParam("sso_group_id", true, false);
            SsoGroup ssoGroup = user.getSsoGroup(sso_group_id);
            JSONObject account_information = sm.getJsonParam("account_information", false, true);
            String keyUser = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
            ssoGroup.setAccount(AccountFactory.getInstance().createAccountFromJson(account_information, keyUser, 0));
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
