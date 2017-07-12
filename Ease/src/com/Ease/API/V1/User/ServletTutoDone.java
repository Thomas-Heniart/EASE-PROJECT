package com.Ease.API.V1.User;

import com.Ease.Dashboard.User.Status;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/user/TutoDone")
public class ServletTutoDone extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Status status = sm.getUser().getStatus();
            if (status.tutoIsDone())
                throw new HttpServletException(HttpStatus.BadRequest, "Tuto already done.");
            DatabaseRequest databaseRequest = sm.getDB().prepareRequest("UPDATE status SET tuto_done = 1 WHERE id = ?;");
            databaseRequest.setInt(status.getDbId());
            databaseRequest.set();
            status.set_tuto_done(true);
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
