package com.Ease.Servlet.BackOffice;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Servlet implementation class AddApp
 */
@WebServlet("/addWebsite")
public class AddWebsite extends HttpServlet {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddWebsite() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) (session.getAttribute("user"));
        Catalog catalog = (Catalog) session.getAttribute("catalog");
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

        // Get Parameters
        String url = sm.getServletParam("siteUrl", false);
        String name = sm.getServletParam("siteName", true);
        String homePage = sm.getServletParam("homePage", false);
        String folder = sm.getServletParam("siteFolder", false);
        String haveLoginButtonString = sm.getServletParam("haveLoginButton", false);
        String noLoginString = sm.getServletParam("noLogin", false);
        String noScrapString = sm.getServletParam("noScrap", false);
        String ssoIdString = sm.getServletParam("sso", true);
        Integer ssoId = null;
        if (ssoIdString != null && !ssoIdString.equals(""))
            ssoId = Integer.parseInt(ssoIdString);
        String team_id = sm.getServletParam("team_id", true);
        boolean noLogin = true;
        boolean haveLoginButton = true;
        boolean noScrap = true;
        if (haveLoginButtonString == null)
            haveLoginButton = false;
        if (noLoginString == null)
            noLogin = false;
        if (noScrapString == null)
            noScrap = false;

        String[] haveLoginWith = sm.getServletParamArray("haveLoginWith", false);
        String[] infoNames = sm.getServletParamArray("infoName", false);
        String[] infoTypes = sm.getServletParamArray("infoType", false);
        String[] placeholders = sm.getServletParamArray("infoPlaceholder", false);
        String[] placeholderIcons = sm.getServletParamArray("infoPlaceholderIcon", false);
        // --

        try {
            sm.needToBeConnected();
            if (!user.isAdmin())
                throw new GeneralException(ServletManager.Code.ClientWarning, "You ain't admin");
            if (url == null || url.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty url");
            if (homePage == null || homePage.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty homepage");
            if (name == null || name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name");
            if (folder == null || folder.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty folder");
            if ((infoNames.length - infoTypes.length) != (placeholders.length - placeholderIcons.length))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Missing informations");
            Website website = catalog.addWebsite(url, name, homePage, folder, haveLoginButton, noLogin, !noScrap, haveLoginWith, infoNames, infoTypes, placeholders, placeholderIcons, ssoId, team_id, sm);
            //GroupApp groupApp = GroupWebsiteApp.createGroupEmptyApp(groupProfile, group, AppPermissions.Perm.ALL.getValue(), name, false, website, sm)
            sm.setResponse(ServletManager.Code.Success, "Success");
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();

    }
}