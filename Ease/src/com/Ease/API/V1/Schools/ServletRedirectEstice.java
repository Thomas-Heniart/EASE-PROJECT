package com.Ease.API.V1.Schools;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ServletRedirectEstice
 */
@WebServlet({"/espas-estice-icm-school"})
public class ServletRedirectEstice extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRedirectEstice() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("schoolLandingTemplate.jsp?schoolImageSrcs=/resources/landing/school/Espas.png,/resources/landing/school/Estice.png,/resources/landing/school/ICM.jpg&schoolName=ESPAS,%20ESTICE%20et%20ICM&school=Estice&commentFile=esticeComments.jsp&emailPlaceholder=something&video=estice.mp4");
        rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
