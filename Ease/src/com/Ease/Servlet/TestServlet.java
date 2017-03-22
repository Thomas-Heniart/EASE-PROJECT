package com.Ease.Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.Ease.Utils.JsonProcessing.DOMDescriber;
import com.Ease.Utils.JsonProcessing.DOMMapProcessing;
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

    protected String readFile(String path){
    	String ret = "";
    	
    	InputStream st= getServletContext().getResourceAsStream(path);
    	        BufferedReader in = new BufferedReader(new InputStreamReader(st));
    	        String line;
    	        try {
					while((line = in.readLine()) != null)
					{
						ret += line;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
    	return ret;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebsiteIntegrator wi = new WebsiteIntegrator();
		
//		String html2 = "<body><div><a href='#'></a></div><p class='toto' type='submit' data-source='lolo'>Parsed HTML into a doc.</p></body>";
//		String html3 = "<body><p class='toto lala' type='submit' data-source='lolo'>Parsed HTML into a doc.</p></body>";

		String html4 = this.readFile("/dom1.html");
		String html5 = this.readFile("/dom2.html");
		
//		Document doc = Jsoup.parse(html2);
//		Document doc2 = Jsoup.parse(html3);

		Document doc3 = Jsoup.parse(html4);
		Document doc4 = Jsoup.parse(html5);
		
//		Element body1 = doc.select("body").get(0);
//		Element body2 = doc2.select("body").get(0);
		Element docTest = Jsoup.parse("<input name='q' type='text' placeholder='Search...' value='' tabindex='1' autocomplete='off' maxlength='240' class='f-input js-search-field'>").select("body > :first-child").get(0);
		Element body1 = doc3.select("body").get(0);
		Element body2 = doc4.select("body").get(0);
		
//		wi.printElement(body1, 0);
//		System.out.println("----------------------------");
//		wi.recursiveDOM(body1, body2, myBody);
		DOMDescriber test = new DOMDescriber(docTest);
		DOMDescriber dd1 = new DOMDescriber(body1);
		DOMDescriber dd2 = new DOMDescriber(body2);
		String		tmpSelector = DOMMapProcessing.getStringSelectorFromElement(test.getDOM());
		DOMMapProcessing.printAllMapWithWriter(test.getMap(), response.getWriter());

		response.getWriter().println(tmpSelector);
		Elements  elems = test.executeQuery(tmpSelector);
		Element  tmpElem = test.getDOM().clone();
		
		response.getWriter().println(elems.size());
		response.getWriter().println(test.isElementPresentInMap(tmpElem));
		response.getWriter().println("###########################################################################");
		DOMMapProcessing.printAllMapWithWriter(dd1.getCommonMapWith(dd2), response.getWriter());
		response.getWriter().println("###########################################################################");
		DOMMapProcessing.printAllMapWithWriter(dd1.getDiffMapWith(dd2), response.getWriter());
		response.getWriter().println("###########################################################################");
		DOMMapProcessing.printAllMapWithWriter(dd2.getDiffMapWith(dd1), response.getWriter());
		//RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		//rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

}