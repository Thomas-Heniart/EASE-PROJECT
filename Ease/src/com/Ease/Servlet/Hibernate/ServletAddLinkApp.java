 package com.Ease.Servlet.Hibernate;

 import com.Ease.Context.Catalog.Catalog;
 import com.Ease.Context.Catalog.Website;
 import com.Ease.Hibernate.HibernateQuery;
 import com.Ease.NewDashboard.App.AppInformation;
 import com.Ease.NewDashboard.App.LinkApp.LinkApp;
 import com.Ease.NewDashboard.App.LinkApp.LinkAppInformation;
 import com.Ease.NewDashboard.Profile.Profile;
 import com.Ease.NewDashboard.User.User;
 import com.Ease.Utils.GeneralException;
 import com.Ease.Utils.ServletManager;
 import com.Ease.Utils.ServletManagerHibernate;

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
  * Servlet implementation class AddBookMark
  */
 @WebServlet("/ServletAddLinkApp")
 public class ServletAddLinkApp extends HttpServlet {
     private static final long serialVersionUID = 1L;

     /**
      * @see HttpServlet#HttpServlet()
      */
     public ServletAddLinkApp() {
         super();
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
         ServletManagerHibernate sm = new ServletManagerHibernate(this.getClass().getName(), request, response, true);
         User user = sm.getUser();
         try {
             sm.needToBeConnected();
             String name = sm.getServletParam("name", true);
             String websiteId = sm.getServletParam("websiteId", true);
             String profileId = sm.getServletParam("profileId", true);
             String link = sm.getServletParam("link", true);

             Website site = null;
             if (name == null || name.equals(""))
                 throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
             if (websiteId == null || websiteId.equals(""))
                 throw new GeneralException(ServletManager.Code.ClientWarning, "Empty websiteId.");
             if (profileId == null || profileId.equals(""))
                 throw new GeneralException(ServletManager.Code.ClientWarning, "Empty profileId.");
             if (link == null || link.equals(""))
                 throw new GeneralException(ServletManager.Code.ClientWarning, "Empty link.");
             try {
                 Profile profile = user.getProfileManager().getProfileWithId(Integer.parseInt(profileId));
                 site = ((Catalog)sm.getContextAttr("catalog")).getWebsiteWithSingleId(Integer.parseInt(websiteId));
                 HibernateQuery query = new HibernateQuery();
                 AppInformation appInformation = new AppInformation(name);
                 LinkAppInformation linkAppInformation = new LinkAppInformation(link, site.getFolder() + "logo.png");
                 LinkApp linkApp = new LinkApp("linkApp", new Date(), appInformation, linkAppInformation);
                 query.saveOrUpdateObject(linkApp);
                 query.commit();
                 profile.addApp(linkApp);
                 sm.setResponse(ServletManager.Code.Success, String.valueOf(linkApp.getDb_id()));
             } catch (NumberFormatException e) {
                 throw new GeneralException(ServletManager.Code.ClientError, "Wrong numbers.");
             }
         } catch (GeneralException e) {
             sm.setResponse(e);
         } catch (Exception e) {
             sm.setResponse(e);
         }
         sm.sendResponse();
     }

 }
