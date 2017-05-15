/**
 * Created by thomas on 15/05/2017.
 */
@javax.servlet.annotation.WebServlet("/ServletGenerateAlgoliaJson")
public class ServletGenerateAlgoliaJson extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {

        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
