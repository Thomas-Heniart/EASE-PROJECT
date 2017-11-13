package com.Ease.API.V1.Dashboard;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.SsoApp;
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

@WebServlet("/api/v1/dashboard/EditSsoGroup")
public class ServletEditSsoGroup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer sso_group_id = sm.getIntParam("sso_group_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT s FROM SsoGroup s WHERE s.id = :id");
            hibernateQuery.setParameter("id", sso_group_id);
            SsoGroup ssoGroup = (SsoGroup) hibernateQuery.getSingleResult();
            if (ssoGroup == null || !ssoGroup.getUser_id().equals(Integer.valueOf(user.getDBid())))
                throw new HttpServletException(HttpStatus.BadRequest, "No such SsoGroup");
            JSONObject account_information = sm.getJsonParam("account_information", false, true);
            ssoGroup.setAccount(AccountFactory.getInstance().createAccountFromJson(account_information, user.getKeys().getKeyUser(), 0));
            for (SsoApp ssoApp : ssoGroup.getSsoAppMap().values())
                ((SsoApp) user.getDashboardManager().getApp(ssoApp.getDb_id())).setSsoGroup(ssoGroup);
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
