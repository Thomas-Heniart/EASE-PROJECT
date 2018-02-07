package com.Ease.API.V1.Update;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Update.Update;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@WebServlet("/api/v1/updates")
public class ServletUpdate extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id");
            hibernateQuery.setParameter("user_id", sm.getUser().getDb_id());
            List<Update> updates = hibernateQuery.list();
            String private_key = sm.getUserPrivateKey();
            JSONArray res = new JSONArray();
            for (Update update : updates) {
                update.decipher(private_key);
                res.put(update.getJson());
            }
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String url = sm.getStringParam("url", true, false);
            if (url.equals("") || url.length() > 2000 || !Regex.isSimpleUrl(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Website website = catalog.getPublicWebsiteWithUrl(url, new HashSet<>(), hibernateQuery);
            if (website != null) {
                /* Find update(s) with this website */
            } else {
                /* Find update(s) with same URL */
            }
            /* Decipher updates and check if credentials are the same except for password */
            /* if one or more */
                /* delete them */
                /* save new update and team card if exists */
            /* else */

        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Long id = sm.getLongParam("id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("DELETE FROM Update u WHERE u.id = :id AND u.user.db_id = :user_id");
            hibernateQuery.setParameter("id", id);
            hibernateQuery.setParameter("user_id", sm.getUser().getDb_id());
            hibernateQuery.executeUpdate();
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
