package com.Ease.Servlet.Hibernate;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.NewDashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.NewDashboard.App.WebsiteApp.ClassicApp.AccountInformation;
import com.Ease.NewDashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.NewDashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.NewDashboard.Profile.Profile;
import com.Ease.NewDashboard.Profile.ProfileApp;
import com.Ease.NewDashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.ServletManagerHibernate;
import com.Ease.Website.Catalog;
import com.Ease.Website.Website;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class AddBookMark
 */
@WebServlet("/ServletAddClassicApp")
public class ServletAddClassicApp extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAddClassicApp() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletManagerHibernate sm = new ServletManagerHibernate(this.getClass().getName(), request, response, true);
        User user = sm.getUser();
        try {
            sm.needToBeConnected();
            String name = sm.getServletParam("name", true);
            String websiteIdsParam = sm.getServletParam("websiteIds", true);
            String profileId = sm.getServletParam("profileId", true);

            if (name == null || name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
            if (websiteIdsParam == null || websiteIdsParam.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty websiteIds.");
            if (profileId == null || profileId.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty profileId.");

            JSONParser parser = new JSONParser();
            JSONArray websiteIds = null;
            websiteIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(websiteIdsParam));
            JSONArray res = new JSONArray();
            Profile profile = user.getProfileManager().getProfileWithId(Integer.parseInt(profileId));
            Catalog catalog = (Catalog) sm.getContextAttr("catalog_hibernate");
            HibernateQuery query = new HibernateQuery();
            for (Object websiteId : websiteIds) {
                Website website = catalog.getWebsite(Integer.parseInt((String) websiteId));
                Map<String, String> infos = website.getNeededInformation(sm);
                Account account = new Account();
                List<AccountInformation> accountInformationList = AccountInformation.createInformationsFromMapForAccount(infos, account);
                account.setAccountInformationList(accountInformationList);
                AppInformation appInformation = new AppInformation(name);
                ClassicApp classicApp = new ClassicApp(appInformation, website, account);
                query.saveOrUpdateObject(classicApp);
                ProfileApp profileApp = profile.addApp(classicApp);
                query.saveOrUpdateObject(profileApp);
                res.add(classicApp.getDb_id());
            }
            query.commit();
            sm.setResponse(ServletManager.Code.Success, String.valueOf(res.toString()));
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

}
