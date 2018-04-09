package com.Ease.API.V1.Admin.onboarding;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserRole;
import com.Ease.User.User;
import com.Ease.User.UserFactory;
import com.Ease.User.UserKeys;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.onboarding.OnboardingCustomerInformation;
import com.Ease.websocketV1.WebSocketMessage;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
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
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/v1/admin/onboarding/transfer-ownership")
public class ServletTransferOwnership extends HttpServlet {

    private static final int MAIL_ID = 356500;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer teamId = sm.getIntParam("teamId", true, false);
            Team team = sm.getTeam(teamId);
            TeamUser teamUserConnected = sm.getTeamUser(team);
            Integer teamUserId = teamUserConnected.getDb_id();
            Long onboardingId = sm.getLongParam("onboardingId", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            OnboardingCustomerInformation onboardingCustomerInformation = (OnboardingCustomerInformation) hibernateQuery.get(OnboardingCustomerInformation.class, onboardingId);
            if (onboardingCustomerInformation == null)
                throw new HttpServletException(HttpStatus.BadRequest, "OnboardingCustomerInformation not found");
            TeamUser owner = team.getTeamUserByEmail(onboardingCustomerInformation.getEmail());
            owner.getTeamUserStatus().setInvitation_sent(true);
            owner.getTeamUserRole().setRole(TeamUserRole.Role.OWNER);
            sm.saveOrUpdate(owner);
            String emailContent = sm.getStringParam("emailContent", true, false);

            /* Handle stripe free trial */
            User easeAdmin = sm.getUser();
            String accessCode = Base64.encodeBase64String(UUID.randomUUID().toString().getBytes(Charset.forName("UTF8")));
            createUser(sm, team, hibernateQuery, owner, accessCode, onboardingCustomerInformation);
            deleteEaseAdmin(sm, team, teamUserConnected, hibernateQuery, owner);
            easeAdmin.removeTeamUser(teamUserConnected);
            sm.deleteObject(onboardingCustomerInformation);
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom(easeAdmin.getEmail(), easeAdmin.getPersonalInformation().getFirst_name() + " " + easeAdmin.getPersonalInformation().getLast_name());
            mailJetBuilder.addTo(owner.getEmail());
            mailJetBuilder.setTemplateId(MAIL_ID);
            mailJetBuilder.addVariable("emailContent", emailContent);
            /* @TODO change url when front done */
            mailJetBuilder.addVariable("url", Variables.URL_PATH + "#/teamJoin/" + owner.getInvitation_code() + "/" + accessCode + "?email=" + owner.getEmail());
            mailJetBuilder.sendEmail();
            JSONObject wsObj = new JSONObject();
            wsObj.put("team_id", teamId);
            wsObj.put("team_user_id", teamUserId);
            WebSocketMessage webSocketMessage = WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.REMOVED, wsObj);
            sm.addWebSocketMessage(webSocketMessage);
            sm.setSuccess("Done");
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void createUser(PostServletManager sm, Team team, HibernateQuery hibernateQuery, TeamUser owner, String accessCode, OnboardingCustomerInformation onboardingCustomerInformation) throws HttpServletException {
        hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
        hibernateQuery.setParameter("email", owner.getEmail());
        User user = (User) hibernateQuery.getSingleResult();
        if (user == null) {
            user = UserFactory.getInstance().createUser(owner.getEmail(), accessCode, owner.getUsername());
            owner.setTeamKey(AES.encrypt(sm.getTeamKey(team), user.getUserKeys().getDecipheredKeyUser(accessCode)));
        } else {
            if (!user.getUserStatus().isRegistered()) {
                Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
                String saltPerso = AES.generateSalt();
                String keyUser = AES.keyGenerator();
                UserKeys userKeys = user.getUserKeys();
                userKeys.setAccess_code_hash(Hashing.hash(accessCode));
                userKeys.setSaltPerso(saltPerso);
                userKeys.setKeyUser(AES.encryptUserKey(keyUser, accessCode, saltPerso));
                userKeys.setPublicKey(publicAndPrivateKey.getKey());
                userKeys.setPrivateKey(AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
                owner.setTeamKey(AES.encrypt(sm.getTeamKey(team), keyUser));
            } else
                owner.setTeamKey(RSA.Encrypt(sm.getTeamKey(team), user.getUserKeys().getPublicKey()));
        }
        user.getPersonalInformation().setPhone_number(onboardingCustomerInformation.getPhoneNumber());
        user.getPersonalInformation().setLast_name(onboardingCustomerInformation.getLastName());
        user.getPersonalInformation().setFirst_name(onboardingCustomerInformation.getFirstName());
        user.addTeamUser(owner);
        owner.setUser(user);
        sm.saveOrUpdate(user);
    }

    private void deleteEaseAdmin(PostServletManager sm, Team team, TeamUser teamUserConnected, HibernateQuery hibernateQuery, TeamUser owner) {
        for (Channel channel : team.getChannelsForTeamUser(teamUserConnected)) {
            if (channel.getRoom_manager().equals(teamUserConnected))
                channel.setRoom_manager(owner);
            sm.saveOrUpdate(channel);
        }
        for (TeamUser teamUser : team.getTeamUsers().values()) {
            if (teamUser.getAdmin_id() == null)
                continue;
            if (teamUser.getAdmin_id().equals(teamUserConnected.getDb_id())) {
                teamUser.setAdmin_id(owner.getDb_id());
                sm.saveOrUpdate(teamUser);
            }
        }
        hibernateQuery.queryString("DELETE FROM Update u WHERE u.teamUser.db_id = :teamUserId");
        hibernateQuery.setParameter("teamUserId", teamUserConnected.getDb_id());
        hibernateQuery.executeUpdate();
        team.getTeamCardSet().stream().filter(teamCard -> teamUserConnected.equals(teamCard.getTeamUser_sender())).forEach(teamCard -> {
            teamCard.setTeamUser_sender(owner);
            sm.saveOrUpdate(teamCard);
        });
        for (TeamCardReceiver teamCardReceiver : teamUserConnected.getTeamCardReceivers()) {
            App app = teamCardReceiver.getApp();
            hibernateQuery.queryString("DELETE FROM Update u WHERE u.app.db_id = :app_id");
            hibernateQuery.setParameter("app_id", app.getDb_id());
            hibernateQuery.executeUpdate();
        }
        for (TeamCardReceiver teamCardReceiver : teamUserConnected.getTeamCardReceivers()) {
            App app = teamCardReceiver.getApp();
            TeamCard teamCard = teamCardReceiver.getTeamCard();
            if (app.isWebsiteApp()) {
                WebsiteApp websiteApp = (WebsiteApp) app;
                websiteApp.getLogWithAppSet().forEach(logWithApp -> {
                    Profile profile1 = logWithApp.getProfile();
                    profile1.removeAppAndUpdatePositions(logWithApp, sm.getHibernateQuery());
                    sm.deleteObject(logWithApp);
                });
            }
            Profile profile = app.getProfile();
            if (profile != null)
                profile.removeAppAndUpdatePositions(app, sm.getHibernateQuery());
            teamCard.removeTeamCardReceiver(teamCardReceiver);
            sm.deleteObject(teamCardReceiver);
        }
        team.getChannels().values().forEach(channel -> {
            channel.removeTeamUser(teamUserConnected);
            channel.removePendingTeamUser(teamUserConnected);
        });
        teamUserConnected.getProfiles().forEach(profile -> {
            profile.setChannel(null);
            profile.setTeamUser(null);
            sm.saveOrUpdate(profile);
        });
        team.removeTeamUser(teamUserConnected);
        sm.deleteObject(teamUserConnected);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
