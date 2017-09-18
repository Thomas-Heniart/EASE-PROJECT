package com.Ease.Servlet;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class EditName
 */
@WebServlet("/EditName")
public class EditName extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditName() {
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
        User user = (User) session.getAttribute("user");
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

        try {
            sm.needToBeConnected();
            String fname = sm.getServletParam("fname", true);
            checkUsernameIntegrity(fname);
            user.setFirstName(fname, sm);
            sm.setResponse(ServletManager.Code.Success, "Name changed.");
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    private void checkUsernameIntegrity(String username) throws HttpServletException {
        if (username == null || username.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Usernames can't be empty!");
        if (username.length() >= 22 || username.length() < 3)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must be between 3 and 21 characters.");
        if (!username.equals(username.toLowerCase()) || !Regex.isValidUsername(username))
            throw new HttpServletException(HttpStatus.BadRequest, "Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores");
    }
}