package com.Ease.Servlet.App;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class RemoveApp
 */
@WebServlet("/RemoveApp")
public class RemoveApp extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveApp() {
        super();
        // TODO Auto-generated constructor stub
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
            String appId = sm.getServletParam("appId", true);
            if (appId == null || appId.isEmpty())
                throw new GeneralException(ServletManager.Code.ClientError, "Wrong appId.");
            App app = user.getDashboardManager().getAppWithId(Integer.parseInt(appId));
            if (app.isPinned())
                app.unpin(sm.getDB());
            else
                user.getDashboardManager().removeAppWithId(Integer.parseInt(appId), sm.getDB());
            sm.setResponse(ServletManager.Code.Success, "App removed.");
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (NumberFormatException e) {
            sm.setResponse(ServletManager.Code.ClientError, "Wrong numbers.");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

}
