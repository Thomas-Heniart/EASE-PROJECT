package com.Ease.API.V1.Schools;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/isc-paris")
public class ServletRedirectISCParis extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("schoolLandingTemplate.jsp?schoolImageSrcs=/resources/landing/school/edhec.png&schoolName=ISC%20Paris&formAction=IscParisRegistration&commentFile=edhecComments.jsp&emailPlaceholder=iscparis.com&video=edhec.mp4");
        rd.forward(request, response);
    }
}
