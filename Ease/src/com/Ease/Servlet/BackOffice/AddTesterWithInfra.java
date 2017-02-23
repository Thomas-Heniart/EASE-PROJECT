package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.App.AppPermissions;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.App.LinkApp.GroupLinkApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.Profile.ProfilePermissions;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class AddTesterWithInfra
 */
@WebServlet("/AddTesterWithInfra")
public class AddTesterWithInfra extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTesterWithInfra() {
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
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			if (user.isAdmin() == false) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			}
			String email = sm.getServletParam("email", true);
			String univName = sm.getServletParam("infra", true);
			String name = sm.getServletParam("name", true);
			String color = sm.getServletParam("profileColor", true);
			//String img_path = sm.getServletParam("imgPath", true);
			System.out.println(email + " " + name + " " + univName + " " + color);
			if (email == null || !Regex.isEmail(email)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email.");
			} else if (univName == null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong infrastructure name.");
			} else if (name == null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong user name.");
			} else if (color == null || !Regex.isColor(color)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong profile color.");
			} /*else if (img_path == null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong image path.");
			}*/
			
			Infrastructure infra = Infrastructure.createInfrastructure(univName, "estice.png", sm);
			
			int transaction = db.startTransaction();
			Group groupTester = Group.createGroup("Tester", null, infra, sm);
			List<Group> groups = new LinkedList<Group>();
			groups.add(groupTester);
			infra.setGroups(groups);
			
			GroupProfile groupProfile = GroupProfile.createGroupProfile(groupTester, ProfilePermissions.Perm.ALL.getValue(), univName, color, false, sm);
			List<GroupProfile> groupProfiles = new LinkedList<GroupProfile>();
			groupProfiles.add(groupProfile);
			groupTester.setGroupProfiles(groupProfiles);
			
			List<GroupApp> groupApps = new LinkedList<GroupApp>();
			GroupApp intranet = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "Intranet", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/books.png", sm);
			groupApps.add(intranet);
			
			GroupApp mail = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "Mail", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/mail.png", sm);
			groupApps.add(mail);
			
			GroupApp alumni = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "Alumni", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/alumni.png", sm);
			groupApps.add(alumni);
			
			GroupApp calendar = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "Calendar", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/calendar.png", sm);
			groupApps.add(calendar);
			
			GroupApp elearning1 = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "E-learning1", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/elearning.png", sm);
			groupApps.add(elearning1);
			
			GroupApp elearning2 = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "E-learning2", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/web.png", sm);
			groupApps.add(elearning2);
			
			GroupApp others = GroupLinkApp.createGroupLinkApp(groupProfile, groupTester, AppPermissions.Perm.ALL.getValue(), "Others", false, "https://ease.space/addDemo.jsp", "/resources/images/linkApp/plus.png", sm);
			groupApps.add(others);
			
			groupTester.setGroupApps(groupApps);
			
			groupTester.addUser(email, name, true, sm);
			db.commitTransaction(transaction);
			sm.setResponse(ServletManager.Code.Success, "Success.");
			
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
