package com.Ease.Servlet;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.JsonProcessing.WebsiteIntegrator;

/**
 * Servlet implementation class AddUserEmail
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebsiteIntegrator wi = new WebsiteIntegrator();
		
		String html2 = "<body><div><a href='#'></a></div><p class='toto' type='submit'>Parsed HTML into a doc.</p></body>";
		String html3 = "<body><p class='toto lala' type='submit'>Parsed HTML into a doc.</p></body>";
		
		Document doc = Jsoup.parse(html2);
		Document doc2 = Jsoup.parse(html3);

		Element text = doc.select("p.toto").get(0);
		Element text2 = doc2.select("p").get(0);

		System.out.println(doc.siblingElements().size());
		System.out.println(doc.getAllElements().size());
		System.out.println(doc.select("body").get(0).children().size());
		System.out.println(wi.isSameElement(text, text2));
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

}