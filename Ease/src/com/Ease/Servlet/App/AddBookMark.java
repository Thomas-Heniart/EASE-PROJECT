package com.Ease.Servlet.App;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.Profile.Profile;
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
 * Servlet implementation class AddBookMark
 */
@WebServlet("/AddBookMark")
public class AddBookMark extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddBookMark() {
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
        User user = (User) (session.getAttribute("user"));
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String name = sm.getServletParam("name", true);
            String websiteId = sm.getServletParam("websiteId", true);
            String profileId = sm.getServletParam("profileId", true);
            String link = sm.getServletParam("link", true);

            Website site = null;
            if (name == null || name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
            if (websiteId == null || websiteId.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty websiteId.");
            if (profileId == null || profileId.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty profileId.");
            if (link == null || link.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty link.");
            Profile profile = user.getDashboardManager().getProfile(Integer.parseInt(profileId));
            site = ((Catalog) sm.getContextAttr("catalog")).getWebsiteWithId(Integer.parseInt(websiteId));
            LinkApp linkApp = LinkApp.createLinkApp(profile, profile.getApps().size(), name, link, site.getLogo(), sm);
            profile.addApp(linkApp);
            sm.setResponse(ServletManager.Code.Success, String.valueOf(linkApp.getDBid()));
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

}
