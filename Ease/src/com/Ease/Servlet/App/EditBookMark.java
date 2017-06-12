package com.Ease.Servlet.App;

import java.io.IOException;

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

import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class EditBookMark
 */
@WebServlet("/EditBookMark")
public class EditBookMark extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditBookMark() {
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
            String link = sm.getServletParam("link", true);
            String appIdsString = sm.getServletParam("appIds", true);

            if (name == null || name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
            if (link == null || link.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty link.");

            JSONParser parser = new JSONParser();
            JSONArray appIds = null;
            appIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(appIdsString));
            try {
                DataBaseConnection db = sm.getDB();
                int transaction = db.startTransaction();
                for (Object appId : appIds) {
                    LinkApp app = (LinkApp) user.getDashboardManager().getAppWithID(Integer.parseInt((String) appId));
                    app.setName(name, sm.getDB());
                    app.getLinkAppInformations().setLink(link, db);
                }
                db.commitTransaction(transaction);
                sm.setResponse(ServletManager.Code.Success, "Links edited.");
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
