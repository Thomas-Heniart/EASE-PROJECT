package com.Ease.API.V1.Update;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Update.Update;
import com.Ease.Update.UpdateFactory;
import com.Ease.User.User;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            User user = sm.getUser();
            String url = sm.getStringParam("url", true, false);
            if (url.equals("") || url.length() > 2000 || !Regex.isSimpleUrl(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Set<String> informationNameSet = new HashSet<>();
            account_information.keySet().forEach(o -> informationNameSet.add((String) o));
            Website website = catalog.getPublicWebsiteWithUrl(url, informationNameSet, hibernateQuery);
            List<Update> updates;
            if (website != null) {
                /* Find update(s) with this website */
                hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id AND website_id = :website_id");
                hibernateQuery.setParameter("user_id", user.getDb_id());
                hibernateQuery.setParameter("website_id", website.getDb_id());
            } else {
                /* Find update(s) with same URL */
                hibernateQuery.queryString("SELECT u FROM Update u WHERE u.user.db_id = :user_id AND url = :url");
                hibernateQuery.setParameter("user_id", user.getDb_id());
                hibernateQuery.setParameter("url", url);
            }
            updates = hibernateQuery.list();
            /* Decipher updates and check if credentials are the same except for password */
            String privateKey = sm.getUserPrivateKey();
            JSONArray res = new JSONArray();
            for (Update update : updates) {
                update.decipher(privateKey);
                if (update.accountMatch(account_information)) {
                    update.edit(account_information, user.getUserKeys().getPublicKey());
                    res.put(update.getJson());
                }
            }
            if (res.length() == 0) {
                if (website != null) {
                    hibernateQuery.queryString("SELECT w FROM WebsiteApp w WHERE w.website.db_id = :website_id AND w.profile.user.db_id = :user_id");
                    hibernateQuery.setParameter("website_id", website.getDb_id());
                    hibernateQuery.setParameter("user_id", user.getDb_id());
                    List<WebsiteApp> websiteApps = hibernateQuery.list();
                    if (websiteApps.isEmpty()) {
                        Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, website);
                        sm.saveOrUpdate(tmp);
                        res.put(tmp.getJson());
                    } else
                        for (WebsiteApp websiteApp : websiteApps) {
                            Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, websiteApp);
                            sm.saveOrUpdate(tmp);
                            res.put(tmp.getJson());
                        }
                } else {
                    Update tmp = UpdateFactory.getInstance().createUpdate(user, account_information, url);
                    sm.saveOrUpdate(tmp);
                    res.put(tmp.getJson());
                }
            }
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
