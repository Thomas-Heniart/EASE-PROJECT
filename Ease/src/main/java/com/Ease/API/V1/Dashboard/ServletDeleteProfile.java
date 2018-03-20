package com.Ease.API.V1.Dashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/DeleteProfile")
public class ServletDeleteProfile extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Profile profile = (Profile) hibernateQuery.get(Profile.class, profile_id);
            User user = profile.getUser();
            sm.needToBeConnected();
            if (!user.equals(sm.getUser()))
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!profile.getAppSet().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "You can only delete a profile without apps");
            TeamUser teamUser = profile.getTeamUser();
            if (teamUser != null) {
                //profile.setTeamUser(null);
                teamUser.removeProfile(profile);
            }
            Channel channel = profile.getChannel();
            if (channel != null) {
                //profile.setChannel(null);
                channel.removeProfile(profile);
            }
            user.removeProfileAndUpdatePositions(profile, hibernateQuery);
            sm.setSuccess("Profile deleted");
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
