package com.Ease.API.V1.Teams.Settings;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.InvitedFriend;
import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/teams/InviteFriend")
public class ServletInvitedFriend extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeOwnerOfTeam(team);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            if (team.getInvitedFriendMap().size() >= Team.MAX_MEMBERS)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot invite more than " + Team.MAX_MEMBERS + " friends");
            String email = sm.getStringParam("email", true, false);
            if (!Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "You must provide an email");
            if (email.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Email cannot be longer than 255 characters");
            if (team.getInvitedFriend(email) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already invited this person");
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", email);
            if (!hibernateQuery.list().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "This person already uses Ease.space, please referre another one!");
            hibernateQuery.queryString("SELECT tu FROM TeamUser tu WHERE tu.email = :email");
            hibernateQuery.setParameter("email", email);
            if (!hibernateQuery.list().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "This person already uses Ease.space, please referre another one!");
            InvitedFriend invitedFriend = new InvitedFriend(email, team);
            team.addInvitedFriend(invitedFriend);
            sm.saveOrUpdate(team);
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT friend FROM InvitedFriend friend");
            List<InvitedFriend> invitedFriends = hibernateQuery.list();
            JSONObject res = new JSONObject();
            invitedFriends.forEach(invitedFriend -> {
                Integer team_id = invitedFriend.getTeam().getDb_id();
                JSONArray tmp = res.optJSONArray(team_id.toString());
                if (tmp == null)
                    tmp = new JSONArray();
                tmp.put(invitedFriend.getJson());
                res.put(team_id.toString(), tmp);
            });
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Long id = sm.getLongParam("id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            InvitedFriend invitedFriend = (InvitedFriend) hibernateQuery.get(InvitedFriend.class, id);
            if (invitedFriend == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid id");
            Team team = invitedFriend.getTeam();
            team.removeInvitedFriend(invitedFriend);
            sm.saveOrUpdate(team);
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
