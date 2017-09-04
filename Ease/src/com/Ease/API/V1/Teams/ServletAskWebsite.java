package com.Ease.API.V1.Teams;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Catalog.WebsiteAttributes;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
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
            String url = sm.getStringParam("url", false);
            Boolean is_public = sm.getBooleanParam("is_public", false);
            String login = sm.getStringParam("login", false);
            String password = sm.getStringParam("password", false);
            if (url == null || url.equals("") || !Regex.isValidLink(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url.");
            if (is_public == null)
                is_public = true;
            if (login == null || login.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty login.");
            if (password == null || password.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty password.");
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            WebsiteAttributes websiteAttributes = WebsiteAttributes.createWebsiteAttributes(is_public, false, db);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String[] urlParsed = url.split("\\.");
            String host;
            if (urlParsed.length != 2)
                host = urlParsed[1];
            else {
                host = urlParsed[0];
                if (host.startsWith("http")) {
                    host = host.split("//")[1];
                }
            }
            if (catalog.getWebsiteWithHost(host) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "This website already exists");
            Website website = Website.createWebsite(url, host, websiteAttributes, sm.getServletContext(), db);
            catalog.addWebsite(website);
            db.commitTransaction(transaction);
            /* Decipher login and password */
            /*
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("INSERT INTO customerCredentialsReception VALUES (null, ?, ?, ?, ?, ?, default);");
            hibernateQuery.setParameter(1, sm.getUser().getEmail());
            hibernateQuery.setParameter(2, sm.getStringParam("url", false));
            hibernateQuery.setParameter(3, sm.getStringParam("login", false));
            hibernateQuery.setParameter(4, sm.getStringParam("password", false));
            hibernateQuery.setParameter(5, sm.getIntParam("serverPublicKey_id", false));
            hibernateQuery.executeUpdate();
            */
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
