package com.Ease.API.V1.Common;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/bz")
public class ServletCheckConnection extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
            try {
                JSONObject res = new JSONObject();
                HttpSession session = sm.getSession();
                Integer user_id = (Integer) session.getAttribute("user_id");
                if (user_id == null)
                    res.put("connected", false);
                else {
                    if (sm.getUserProperties(user_id).get("keyUser") == null)
                        res.put("connected", false);
                    else {
                        try {
                            sm.needToBeConnected();
                            res.put("connected", true);
                        } catch (HttpServletException e) {
                            if (e.getHttpStatus() == HttpStatus.AccessDenied)
                                res.put("connected", false);
                            else
                                throw e;
                        }
                    }
                }
                sm.setSuccess(res);
            } catch (Exception e) {
                e.printStackTrace();
                sm.setError(e);
            }
            sm.sendResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
