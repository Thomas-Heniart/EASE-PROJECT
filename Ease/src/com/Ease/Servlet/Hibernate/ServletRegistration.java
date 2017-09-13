package com.Ease.Servlet.Hibernate;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.User.Keys;
import com.Ease.NewDashboard.User.Options;
import com.Ease.NewDashboard.User.Status;
import com.Ease.NewDashboard.User.User;
import com.Ease.Utils.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet implementation class NewUser
 */
@WebServlet("/ServletRegistration")
public class ServletRegistration extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRegistration() {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        ServletManagerHibernate sm = new ServletManagerHibernate(this.getClass().getName(), request, response, true);
        //User user = sm.getUser();
        try {
            String invitationCode = sm.getServletParam("invitationCode", false);
            String fname = sm.getServletParam("fname", true);
            String email = sm.getServletParam("email", true);
            String password = sm.getServletParam("password", false);
            String confirmPassword = sm.getServletParam("confirmPassword", false);
            User user = sm.getUser();
            if (user != null)
              user.logout(sm);
            if (fname == null || fname.length() < 2)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Your name is too short.");
            else if (email == null || Regex.isEmail(email) == false)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Incorrect email format.");
            else if (password == null || Regex.isPassword(password) == false)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Password must be at least 8 characters, contains 1 uppercase, 1 lowercase and 1 digit.");
            else if (confirmPassword == null || password.equals(confirmPassword) == false)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Passwords are not the same.");
            else {
                HibernateQuery query1 = new HibernateQuery();
                query1.queryString("SELECT u FROM User u WHERE u.email = :email");
                query1.setParameter("email", email);
                User existingUser  = (User) query1.getSingleResult();
                if (existingUser != null)
                    query1.deleteObject(existingUser);
                query1.commit();
                HibernateQuery query = new HibernateQuery();
                Keys userKeys = Keys.createKeys(password, sm);
                Options userOptions = Options.createDefaultOptions();
                Status userStatus = Status.createDefaultStatus();
                User newUser = new User(fname, "", email, new Date(), userKeys, userOptions, userStatus);
                query.saveOrUpdateObject(newUser);
                query.commit();
                newUser.createFirstProfiles();
                userKeys.decryptUserKey(password);
                session.setAttribute("user", newUser);
                sm.setResponse(ServletManager.Code.Success, "Registered successfully");
            }
            DatabaseRequest db_request = sm.getDB().prepareRequest("DELETE FROM pendingRegistrations WHERE email = ?;");
            db_request.setString(email);
            db_request.set();
        } catch (GeneralException e) {
            e.printStackTrace();
            sm.setResponse(e);
        } catch (Exception e) {
            e.printStackTrace();
            sm.setResponse(e);
        }

        sm.sendResponse();
    }
}