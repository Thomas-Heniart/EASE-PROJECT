package com.Ease.API.V1.Common;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/EditEmail")
public class ServletEditEmail extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String password = sm.getStringParam("password", false, false);
            String private_key = (String) sm.getContextAttr("privateKey");
            password = RSA.Decrypt(password, private_key);
            User user = sm.getUser();
            if (!user.getKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong password");
            String new_email = sm.getStringParam("new_email", true, false).toLowerCase();
            String digits = sm.getStringParam("digits", true, false);
            if (!Regex.isEmail(new_email))
                throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid email.");
            if (new_email.length() >= 100)
                throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid email.");
            if (user.getEmail().equals(new_email))
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already your reference email.");
            if (!new_email.equals(user.getStatus().getEmail_requested()))
                throw new HttpServletException(HttpStatus.BadRequest, "This is not the email you requested.");
            if (!digits.equals(user.getStatus().getEdit_email_code()))
                throw new HttpServletException(HttpStatus.BadRequest, "This is not the code we provide you");
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT id FROM users WHERE email = ?");
            databaseRequest.setString(new_email);
            if (databaseRequest.get().next())
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already used for another Ease.space account.");
            user.setEmail(new_email, db);
            user.getStatus().setEmail_requested(null, db);
            user.getStatus().setEdit_email_code(null, db);
            db.commitTransaction(transaction);
            sm.setSuccess("Email edited");
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
