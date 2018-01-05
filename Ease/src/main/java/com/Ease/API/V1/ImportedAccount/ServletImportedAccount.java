package com.Ease.API.V1.ImportedAccount;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Importation.ImportedAccount;
import com.Ease.Importation.ImportedAccountInformation;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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
import java.util.Set;

@WebServlet("/api/v1/importedAccounts")
public class ServletImportedAccount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String url = sm.getStringParam("url", true, true);
            Integer website_id = sm.getIntParam("website_id", true, true);
            if (url == null && website_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You must specify an url or a website id");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = null;
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            User user = sm.getUser();
            if (website_id != null)
                website = catalog.getPublicWebsiteWithId(website_id, hibernateQuery, user.getTeams());
            String name = sm.getStringParam("name", true, false);
            ImportedAccount importedAccount = new ImportedAccount(url, website, name, user);
            for (Object object : account_information.keySet()) {
                String information_name = (String) object;
                JSONObject value = account_information.getJSONObject(information_name);
                importedAccount.getImportedAccountInformationMap().put(information_name, new ImportedAccountInformation(information_name, value.getString("value"), importedAccount));
            }
            sm.saveOrUpdate(importedAccount);
            user.addImportedAccount(importedAccount);
            sm.setSuccess(importedAccount.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer id = sm.getIntParam("id", true, true);
            if (id == null) {
                JSONArray jsonArray = new JSONArray();
                user.getImportedAccountMap().values().forEach(importedAccount -> jsonArray.put(importedAccount.getJson()));
                sm.setSuccess(jsonArray);
            } else {
                ImportedAccount importedAccount = user.getImportedAccount(Long.valueOf(id));
                if (importedAccount == null)
                    throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
                sm.setSuccess(importedAccount.getJson());
            }
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Long id = sm.getLongParam("id", true, false);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String url = sm.getStringParam("url", true, true);
            Integer website_id = sm.getIntParam("website_id", true, true);
            if (url == null && website_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You must specify an url or a website id");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = null;
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            User user = sm.getUser();
            if (website_id != null)
                website = catalog.getPublicWebsiteWithId(website_id, hibernateQuery, user.getTeams());
            String name = sm.getStringParam("name", true, false);
            ImportedAccount importedAccount = user.getImportedAccount(id);
            if (importedAccount == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
            importedAccount.setName(name);
            importedAccount.setUrl(url);
            importedAccount.setWebsite(website);
            for (Object object : account_information.keySet()) {
                String information_name = (String) object;
                JSONObject value = account_information.getJSONObject(information_name);
                ImportedAccountInformation importedAccountInformation = importedAccount.getImportedAccountInformation(information_name);
                if (importedAccountInformation == null)
                    importedAccount.addImportedAccountInformation(new ImportedAccountInformation(information_name, value.getString("value"), importedAccount));
                else
                    importedAccountInformation.setValue(value.getString("value"));
            }
            Set<String> keysToRemove = new HashSet<>();
            for (String key : importedAccount.getImportedAccountInformationMap().keySet()) {
                if (account_information.optJSONObject(key) == null)
                    keysToRemove.add(key);
            }
            keysToRemove.forEach(importedAccount::removeImportedAccountInformation);
            sm.saveOrUpdate(importedAccount);
            user.addImportedAccount(importedAccount);
            sm.setSuccess(importedAccount.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Long id = sm.getLongParam("id", true, false);
            ImportedAccount importedAccount = user.getImportedAccount(id);
            if (importedAccount == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
            user.removeImportedAccount(importedAccount);
            sm.deleteObject(importedAccount);
            sm.setSuccess("Imported account deleted");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
