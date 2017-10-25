package com.Ease.API.V1.Teams;

import com.Ease.Catalog.*;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Crypto.RSA;
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
import java.util.HashSet;
import java.util.Set;

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
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            WebsiteAttributes websiteAttributes = new WebsiteAttributes(is_public);
            Website website = new Website(url, "In progress", "undefined", url, websiteAttributes);
            WebsiteInformation loginInformation = new WebsiteInformation("login", "text", 0, "Login", "fa-user-o", website);
            WebsiteInformation passwordInformation = new WebsiteInformation("password", "password", 1, "Password", "fa-lock", website);
            Set<WebsiteInformation> websiteInformationSet = new HashSet<>();
            websiteInformationSet.add(loginInformation);
            websiteInformationSet.add(passwordInformation);
            website.setWebsiteInformationList(websiteInformationSet);
            sm.saveOrUpdate(websiteAttributes);
            sm.saveOrUpdate(website);
            String email = sm.getUser().getEmail();
            WebsiteRequest websiteRequest = new WebsiteRequest(url, email, website);
            website.addWebsiteRequest(websiteRequest);
            sm.saveOrUpdate(websiteRequest);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT key FROM ServerPublicKey key");
            ServerPublicKey serverPublicKey = (ServerPublicKey) hibernateQuery.getSingleResult();
            WebsiteCredentials websiteCredentials = new WebsiteCredentials(RSA.Encrypt(login, serverPublicKey.getPublicKey()), RSA.Encrypt(password, serverPublicKey.getPublicKey()), website, serverPublicKey);
            sm.saveOrUpdate(websiteCredentials);
            catalog.addWebsite(website);
            JSONObject res = website.getCatalogJson();
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
