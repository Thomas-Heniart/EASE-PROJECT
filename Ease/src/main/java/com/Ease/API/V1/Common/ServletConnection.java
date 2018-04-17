package com.Ease.API.V1.Common;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.JsonWebTokenFactory;
import com.Ease.User.User;
import com.Ease.User.UserFactory;
import com.Ease.User.UserKeys;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.Utils.Servlets.ServletManager;
import com.Ease.websocketV1.WebSocketManager;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/api/v1/common/Connection")
public class ServletConnection extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int max_attempts = 10;
    private static final long expiration_time = 5; // 5 minutes
    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        Map<Team, String> teamAndKeyMap = new HashMap<>();
        try {
            String email = sm.getStringParam("email", true, true);
            String password = sm.getStringParam("password", false, true);
            /* String client_ip = IpUtils.getIpAddr(request);
            DataBaseConnection db = sm.getDB();
            addIpInDataBase(client_ip, db);
            if (!canConnect(client_ip, db)) {
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(286063);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                mailJetBuilder.addTo(email);
                mailJetBuilder.sendEmail();
                throw new HttpServletException(HttpStatus.Forbidden, "Too much attempts to connect. Please retry in 5 minutes.");
            } */
            if (email == null || !Regex.isEmail(email) || password == null || password.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
            password = sm.decipher(password);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", email);
            User user = (User) hibernateQuery.getSingleResult();
            if (user == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
            String keyUser = user.getUserKeys().getDecipheredKeyUser(password);
            sm.getUserProperties(user.getDb_id()).put("keyUser", keyUser);
            sm.getUserProperties(user.getDb_id()).put("privateKey", user.getUserKeys().getDecipheredPrivateKey(keyUser));
            Key secret = (Key) sm.getContextAttr("secret");
            if (user.getJsonWebToken() == null) {
                user.setJsonWebToken(JsonWebTokenFactory.getInstance().createJsonWebToken(user, keyUser, secret));
                sm.saveOrUpdate(user.getJsonWebToken());
            } else {
                if (user.getJsonWebToken().getExpiration_date() < new Date().getTime()) {
                    user.getJsonWebToken().renew(keyUser, user, secret, user.getOptions().getConnection_lifetime());
                    sm.saveOrUpdate(user.getJsonWebToken());
                }
            }
            //removeIpFromDataBase(client_ip, db);
            String jwt = user.getJsonWebToken().getJwt(keyUser);
            Cookie cookie = new Cookie("JWT", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, user.getOptions().getConnection_lifetime());
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            cookie.setPath("/");
            cookie.setMaxAge(Math.toIntExact(calendar.getTimeInMillis() - new Date().getTime()) / 1000);
            response.addCookie(cookie);
            user.getCookies().forEach(response::addCookie);
            sm.setUser(user);
            Long now = new Date().getTime();
            for (Team team : user.getTeams()) {
                if (!team.isActive())
                    continue;
                sm.initializeTeamWithContext(team);
                for (TeamUser teamUser : team.getTeamUsers().values()) {
                    /* if (teamUser.isVerified() && teamUser.isDisabled() && teamUser.getTeamKey() != null) {
                        teamUser.setTeamKey(AES.encrypt(RSA.Decrypt(teamUser.getTeamKey(), user.getUserKeys().getDecipheredPrivateKey(keyUser)), keyUser));
                        teamUser.setDisabled(false);
                        sm.saveOrUpdate(teamUser);
                    } */
                    if (team.getTeamUsers().values().stream().filter(teamUser1 -> teamUser1.getTeamUserStatus().isInvitation_sent()).count() >= team.getExtraMembersCount() && !team.isValidFreemium())
                        break;
                    if (teamUser.getArrival_date() != null && !teamUser.getTeamUserStatus().isInvitation_sent() && teamUser.getArrival_date().getTime() < now)
                        sendTeamUserInvitation(teamUser, team, sm);
                }
            }
            for (TeamUser teamUser : user.getTeamUsers()) {
                if (teamUser.getState() == 1 && teamUser.getTeamKey() != null && !teamUser.getTeamKey().isEmpty()) {
                    teamUser.setTeamKey(AES.encrypt(RSA.Decrypt(teamUser.getTeamKey(), sm.getPrivateKey()), keyUser));
                    teamUser.setState(2);
                    teamUser.setDisabled(false);
                    sm.saveOrUpdate(teamUser);
                }
            }
            JSONObject res = user.getJson();
            res.put("JWT", jwt);
            WebSocketManager userWebSocketManager = sm.getUserWebSocketManager(user.getDb_id());
            List<WebSocketManager> teamWebSocketManagerList = user.getTeams().stream().map(team -> sm.getTeamWebSocketManager(team.getDb_id())).collect(Collectors.toList());
            WebSocketManager webSocketManager = sm.getSessionWebSocketManager();
            webSocketManager.getWebSocketSessions().forEach(webSocketSession -> {
                userWebSocketManager.addWebSocketSession(webSocketSession);
                teamWebSocketManagerList.forEach(webSocketManager1 -> webSocketManager1.addWebSocketSession(webSocketSession));
            });
            user.trackConnection(hibernateQuery);
            sm.getSession().setAttribute("webSocketManager", null);
            sm.setSuccess(res);
        } catch (HttpServletException e) {
            sm.setError(new HttpServletException(HttpStatus.BadRequest, "Wrong email or password."));
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(new HttpServletException(HttpStatus.BadRequest, "Wrong email or password."));
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    public void addIpInDataBase(String client_ip, DataBaseConnection db) throws HttpServletException {
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM askingIps WHERE ip= ?;");
        request.setString(client_ip);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            request = db.prepareRequest("UPDATE askingIps SET attempts = attempts + 1 WHERE ip = ?");
            request.setString(client_ip);
            request.set();
        } else {
            request = db.prepareRequest("INSERT INTO askingIps values (NULL, ?, 1, ?, ?);");
            request.setString(client_ip);
            request.setString(getCurrentTime());
            request.setString(getExpirationTime());
            request.set();
        }
        db.commitTransaction(transaction);
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getExpirationTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(new Date(date.getTime() + (expiration_time * ONE_MINUTE_IN_MILLIS)));
    }

    public void removeIpFromDataBase(String client_ip, DataBaseConnection db) throws HttpServletException {
        DatabaseRequest request = db.prepareRequest("DELETE FROM askingIps WHERE ip = ?;");
        request.setString(client_ip);
        request.set();
    }

    public boolean canConnect(String client_ip, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT attempts, expirationDate FROM askingIps WHERE ip= ?;");
            request.setString(client_ip);
            DatabaseResult rs = request.get();
            int attempts = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expirationDate = new Date();
            if (rs.next()) {
                attempts = rs.getInt(1);
                expirationDate = dateFormat.parse(rs.getString(2));
            }
            System.out.println(attempts);
            if (attempts >= max_attempts && expirationDate.getTime() > new Date().getTime()) {
                this.removeIpFromDataBase(client_ip, db);
                this.addIpInDataBase(client_ip, db);
                attempts = 1;
            }
            return attempts < max_attempts || expirationDate.compareTo(new Date()) <= 0;
        } catch (Exception e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
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
