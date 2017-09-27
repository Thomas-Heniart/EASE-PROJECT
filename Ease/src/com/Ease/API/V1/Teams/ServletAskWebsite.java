package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Catalog.WebsiteAttributes;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/AskWebsite")
public class ServletAskWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String url = sm.getStringParam("url", true, false);
            Boolean is_public = sm.getBooleanParam("is_public", true, false);
            String login = sm.getStringParam("login", false, false);
            String password = sm.getStringParam("password", false, false);
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            if (url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url.");
            if (url.length() >= 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Url cannot exceed 255 characters.");
            if (is_public == null)
                is_public = true;
            if (login == null || login.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty login.");
            if (password == null || password.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty password.");
            String private_key = (String) sm.getContextAttr("privateKey");
            login = RSA.Decrypt(login, private_key);
            password = RSA.Decrypt(password, private_key);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            WebsiteAttributes websiteAttributes = WebsiteAttributes.createWebsiteAttributes(is_public, false, db);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String[] urlParsed = url.split("\\.");
            Website website = Website.createWebsite(team_id, url, url, websiteAttributes, sm.getServletContext(), db);
            catalog.addWebsite(website);
            db.commitTransaction(transaction);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT id, publicKey FROM serverPublicKeys LIMIT 1");
            Object[] idAndPublicKey = (Object[]) hibernateQuery.getSingleResult();
            Integer id = (Integer) idAndPublicKey[0];
            String key = (String) idAndPublicKey[1];
            hibernateQuery.querySQLString("INSERT INTO customerCredentialsReception VALUES (null, ?, ?, ?, ?, ?, default);");
            hibernateQuery.setParameter(1, sm.getUser().getEmail());
            hibernateQuery.setParameter(2, url);
            login = RSA.Encrypt(login, key);
            password = RSA.Encrypt(password, key);
            hibernateQuery.setParameter(3, login);
            hibernateQuery.setParameter(4, password);
            hibernateQuery.setParameter(5, id);
            hibernateQuery.executeUpdate();
            JSONObject res = website.getInformationJson();
            res.put("id", website.getDb_id());
            sm.setSuccess(res);
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
