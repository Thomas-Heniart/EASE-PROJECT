package com.Ease.Servlet.App;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class WebsiteAppToClassicApp
 */
@WebServlet("/WebsiteAppToClassicApp")
public class WebsiteAppToClassicApp extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebsiteAppToClassicApp() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) (session.getAttribute("user"));
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String appIdsString = sm.getServletParam("appIds", true);
            String name = sm.getServletParam("name", true);
            String password = sm.getServletParam("password", false);
            Map<String, String> infos = null;
            if (name == null || name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
            if (appIdsString == null || appIdsString.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong app id.");
            if (password == null || password.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong password");
            try {
                JSONParser parser = new JSONParser();
                JSONArray appIds = null;
                appIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(appIdsString));

                DataBaseConnection db = sm.getDB();
                int transaction = db.startTransaction();
                for (Object appId : appIds) {
                    App app = user.getDashboardManager().getAppWithId(Integer.parseInt((String) appId));
                    if (!app.getType().equals("WebsiteApp"))
                        throw new GeneralException(ServletManager.Code.ClientError, "This is not an empty app.");
                    infos = ((WebsiteApp) app).getSite().getNeededInfos(sm);
                    ClassicApp.createFromWebsiteApp((WebsiteApp) app, name, infos, sm, user);
                }
                db.commitTransaction(transaction);
                sm.setResponse(ServletManager.Code.Success, "Classic app created instead of website app.");
            } catch (NumberFormatException e) {
                sm.setResponse(ServletManager.Code.ClientError, "Wrong numbers.");
            }
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }
}
