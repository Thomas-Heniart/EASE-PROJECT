package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Update.Update;
import com.Ease.Update.UpdateAccount;
import com.Ease.Update.UpdateAccountInformation;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/PopulateUpdates")
public class ServletPopulateUpdates extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("DELETE FROM Update");
            hibernateQuery.executeUpdate();
            User user = sm.getUser();
            String publicKey = user.getUserKeys().getPublicKey();

            /* app update */
            App testLink = (App) hibernateQuery.get(App.class, 32387);
            Website testLinkWebsite = ((WebsiteApp) testLink).getWebsite();
            Update update = new Update(user, testLinkWebsite, testLink);
            UpdateAccount updateAccount = new UpdateAccount();
            updateAccount.addUpdateAccountInformation(new UpdateAccountInformation("login", RSA.Encrypt("easetester1@gmail.com", publicKey), updateAccount));
            updateAccount.addUpdateAccountInformation(new UpdateAccountInformation("password", RSA.Encrypt("testest", publicKey), updateAccount));
            update.setUpdateAccount(updateAccount);
            hibernateQuery.saveOrUpdateObject(update);

            /* SingleCard update */
            App github = (App) hibernateQuery.get(App.class, 32377);
            Website githubWebsite = ((WebsiteApp) github).getWebsite();
            TeamCard teamCard = github.getTeamCardReceiver().getTeamCard();
            Update update1 = new Update(user, githubWebsite, github, teamCard);
            UpdateAccount updateAccount1 = new UpdateAccount();
            updateAccount1.addUpdateAccountInformation(new UpdateAccountInformation("login", RSA.Encrypt("easetester1@gmail.com", publicKey), updateAccount1));
            updateAccount1.addUpdateAccountInformation(new UpdateAccountInformation("password", RSA.Encrypt("testest1", publicKey), updateAccount1));
            update1.setUpdateAccount(updateAccount1);
            hibernateQuery.saveOrUpdateObject(update1);

            /* Fill  SingleCard */
            App survey = (App) hibernateQuery.get(App.class, 32380);
            Website surveyWebsite = ((WebsiteApp) survey).getWebsite();
            Update update2 = new Update(user, surveyWebsite, survey, survey.getTeamCardReceiver().getTeamCard());
            UpdateAccount updateAccount2 = new UpdateAccount();
            updateAccount2.addUpdateAccountInformation(new UpdateAccountInformation("login", RSA.Encrypt("easetester1@gmail.com", publicKey), updateAccount2));
            updateAccount2.addUpdateAccountInformation(new UpdateAccountInformation("password", RSA.Encrypt("testest2", publicKey), updateAccount2));
            update2.setUpdateAccount(updateAccount2);
            hibernateQuery.saveOrUpdateObject(update2);

            /* Fill AnySingleCard */
            App testAnyCardSingle = (App) hibernateQuery.get(App.class, 32382);
            Website testAnyCardSingleWebsite = ((WebsiteApp) testAnyCardSingle).getWebsite();
            Update update3 = new Update(user, testAnyCardSingleWebsite, testAnyCardSingle, testAnyCardSingle.getTeamCardReceiver().getTeamCard());
            UpdateAccount updateAccount3 = new UpdateAccount();
            updateAccount3.addUpdateAccountInformation(new UpdateAccountInformation("login", RSA.Encrypt("easetester1@gmail.com", publicKey), updateAccount3));
            updateAccount3.addUpdateAccountInformation(new UpdateAccountInformation("password", RSA.Encrypt("testest3", publicKey), updateAccount3));
            update3.setUpdateAccount(updateAccount3);
            hibernateQuery.saveOrUpdateObject(update3);

            /* Fill AnyEnterpriseCard */
            App testAnyEnterpriseCard = (App) hibernateQuery.get(App.class, 32385);
            Website testAnyEnterpriseCardWebsite = ((WebsiteApp)testAnyEnterpriseCard).getWebsite();
            Update update4 = new Update(user, testAnyEnterpriseCardWebsite, testAnyEnterpriseCard, testAnyEnterpriseCard.getTeamCardReceiver().getTeamCard());
            UpdateAccount updateAccount4 = new UpdateAccount();
            updateAccount4.addUpdateAccountInformation(new UpdateAccountInformation("login", RSA.Encrypt("easetester1@gmail.com", publicKey), updateAccount4));
            updateAccount4.addUpdateAccountInformation(new UpdateAccountInformation("password", RSA.Encrypt("testest4", publicKey), updateAccount4));
            update4.setUpdateAccount(updateAccount);
            hibernateQuery.saveOrUpdateObject(update4);

            /* New account Any */
            Update update5 = new Update(user, "https://updatetestupdate.com");
            UpdateAccount updateAccount5 = new UpdateAccount();
            updateAccount5.addUpdateAccountInformation(new UpdateAccountInformation("login", RSA.Encrypt("easetester1@gmail.com", publicKey), updateAccount5));
            updateAccount5.addUpdateAccountInformation(new UpdateAccountInformation("password", RSA.Encrypt("testest5", publicKey), updateAccount5));
            update5.setUpdateAccount(updateAccount5);
            hibernateQuery.saveOrUpdateObject(update5);

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
