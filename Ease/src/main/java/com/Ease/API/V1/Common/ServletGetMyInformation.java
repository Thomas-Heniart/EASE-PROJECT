package com.Ease.API.V1.Common;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.User.UserFactory;
import com.Ease.User.UserKeys;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.ServletManager;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/v1/common/GetMyInformation")
public class ServletGetMyInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            JSONObject res = new JSONObject();
            try {
                sm.needToBeConnected();
                User user = sm.getUser();
                Long now = new Date().getTime();
                for (Team team : user.getTeams()) {
                    if (!team.isActive())
                        continue;
                    sm.initializeTeamWithContext(team);
                    for (TeamUser teamUser : team.getTeamUsers().values()) {
                        if (team.getTeamUsers().values().stream().filter(teamUser1 -> teamUser1.getTeamUserStatus().isInvitation_sent()).count() >= (Team.MAX_MEMBERS + team.getInvitedFriendMap().size()) && !team.isValidFreemium())
                            break;
                        if (teamUser.getArrival_date() != null && !teamUser.getTeamUserStatus().isInvitation_sent() && teamUser.getArrival_date().getTime() < now)
                            sendTeamUserInvitation(teamUser, team, sm);
                    }
                }
                res.put("user", user.getJson());
                sm.setSuccess(res);
            } catch (HttpServletException e) {
                if (e.getHttpStatus() == HttpStatus.AccessDenied) {
                    res.remove("user");
                    sm.setSuccess(res);
                } else
                    throw e;
            }
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    public void sendTeamUserInvitation(TeamUser teamUser, Team team, ServletManager sm) throws HttpServletException {
        TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
        MailJetBuilder mailJetBuilder = new MailJetBuilder();
        mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
        mailJetBuilder.setTemplateId(179023);
        teamUser.getTeamUserStatus().setInvitation_sent(true);
        sm.saveOrUpdate(teamUser.getTeamUserStatus());
        if (!team.isInvitations_sent()) {
            team.setInvitations_sent(true);
            sm.saveOrUpdate(team);
        }
        HibernateQuery hibernateQuery = sm.getHibernateQuery();
        hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
        hibernateQuery.setParameter("email", teamUser.getEmail());
        User user = (User) hibernateQuery.getSingleResult();
        String access_code = Base64.encodeBase64String(UUID.randomUUID().toString().getBytes(Charset.forName("UTF8")));
        if (user == null) {
            user = UserFactory.getInstance().createUser(teamUser.getEmail(), access_code, teamUser.getUsername());
            teamUser.setTeamKey(AES.encrypt(sm.getTeamKey(team), user.getUserKeys().getDecipheredKeyUser(access_code)));
        } else {
            if (!user.getUserStatus().isRegistered()) {
                Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
                String saltPerso = AES.generateSalt();
                String keyUser = AES.keyGenerator();
                UserKeys userKeys = user.getUserKeys();
                userKeys.setAccess_code_hash(Hashing.hash(access_code));
                userKeys.setSaltPerso(saltPerso);
                userKeys.setKeyUser(AES.encryptUserKey(keyUser, access_code, saltPerso));
                userKeys.setPublicKey(publicAndPrivateKey.getKey());
                userKeys.setPrivateKey(AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
                teamUser.setTeamKey(AES.encrypt(sm.getTeamKey(team), keyUser));
            } else
                teamUser.setTeamKey(RSA.Encrypt(sm.getTeamKey(team), user.getUserKeys().getPublicKey()));
        }
        user.addTeamUser(teamUser);
        teamUser.setUser(user);
        sm.saveOrUpdate(user);
        mailJetBuilder.addTo(teamUser.getEmail());
        mailJetBuilder.addVariable("team_name", team.getName());
        mailJetBuilder.addVariable("first_name", teamUser_admin.getUser().getPersonalInformation().getFirst_name());
        mailJetBuilder.addVariable("last_name", teamUser_admin.getUser().getPersonalInformation().getLast_name());
        mailJetBuilder.addVariable("email", teamUser_admin.getEmail());
        mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/teamJoin/" + teamUser.getInvitation_code() + "/" + access_code);
        mailJetBuilder.sendEmail();
    }
}
