package com.Ease.API.V1.Common;

import com.Ease.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/EditPassword")
public class ServletEditPassword extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String password = sm.getStringParam("password", false, false);
            String new_password = sm.getStringParam("new_password", false, false);
            String private_key = (String) sm.getContextAttr("privateKey");
            password = RSA.Decrypt(password, private_key);
            new_password = RSA.Decrypt(new_password, private_key);
            if (!user.getUserKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong password.");
            if (user.getUserKeys().isGoodPassword(new_password))
                throw new HttpServletException(HttpStatus.BadRequest, "Your new password cannot be as same as your current password");
            if (!Regex.isPassword(new_password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password must be at least 8 characters, one uppercase and one digit.");
            user.getUserKeys().changePassword(new_password, sm.getKeyUser());
            sm.saveOrUpdate(user);
            sm.setSuccess("Password edited");
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
