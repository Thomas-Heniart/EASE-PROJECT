package com.Ease.API.V1.Common;

import com.Ease.User.User;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/PopupConnectionLifetimeSeen")
public class ServletPopupConnectionLifetimeSeen extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            user.getUserStatus().setPopupChooseConnectionLifetimeSeen(true);
            sm.saveOrUpdate(user.getUserStatus());
            sm.setSuccess(user.getJson());
        } catch (Exception e) {
            sm.setError(e);
        } finally {
            sm.sendResponse();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
