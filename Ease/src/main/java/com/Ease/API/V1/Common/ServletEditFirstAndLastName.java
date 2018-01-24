package com.Ease.API.V1.Common;

import com.Ease.User.PersonalInformation;
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

@WebServlet("/api/v1/common/EditFirstAndLastName")
public class ServletEditFirstAndLastName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String first_name = sm.getStringParam("first_name", true, false);
            String last_name = sm.getStringParam("last_name", true, false);
            if (!Regex.isValidName(first_name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid first name");
            if (!Regex.isValidName(last_name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid last name");
            PersonalInformation personalInformation = sm.getUser().getPersonalInformation();
            personalInformation.setFirst_name(first_name);
            personalInformation.setLast_name(last_name);
            sm.setSuccess(sm.getUser().getJson());
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
