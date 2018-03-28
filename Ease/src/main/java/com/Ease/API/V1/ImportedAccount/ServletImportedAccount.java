package com.Ease.API.V1.ImportedAccount;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Importation.ImportedAccount;
import com.Ease.Importation.ImportedAccountInformation;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.AES;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/api/v1/importedAccounts")
public class ServletImportedAccount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String url = sm.getStringParam("url", true, false);
            if (url.equals("") || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter URL");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            User user = sm.getUser();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getPublicWebsiteWithUrl(url, account_information.keySet(), hibernateQuery);
            if (website != null && (!website.getWebsiteAttributes().isIntegrated() || website.getWebsiteInformationList().size() > 2))
                website = null;
            String name = sm.getStringParam("name", true, false);
            if (name.isEmpty() || name.length() > 255) {
                if (website != null)
                    name = website.getName();
                else {
                    URL aUrl;
                    try {
                        aUrl = new URL(url);
                    } catch (MalformedURLException e) {
                        throw new HttpServletException(HttpStatus.BadRequest, "Malformated url");
                    }
                    String[] host_parsed = aUrl.getHost().split("\\.");
                    if (host_parsed.length == 2)
                        name = host_parsed[0];
                    else if (host_parsed.length > 2) {
                        if (host_parsed[0].equals("www"))
                            name = host_parsed[1] + " " + host_parsed[2];
                        else
                            name = host_parsed[0] + " " + host_parsed[1];
                    } else
                        throw new HttpServletException(HttpStatus.BadRequest, "Malformated url");
                }
            }
            ImportedAccount importedAccount = new ImportedAccount(url, website, name, user);
            String symmetric_key = sm.getKeyUser();
            for (Object object : account_information.keySet()) {
                String information_name = (String) object;
                JSONObject value = account_information.getJSONObject(information_name);
                importedAccount.getImportedAccountInformationMap().put(information_name, new ImportedAccountInformation(information_name, AES.encrypt(value.getString("value"), symmetric_key), importedAccount));
            }
            sm.saveOrUpdate(importedAccount);
            user.addImportedAccount(importedAccount);
            importedAccount.decipher(symmetric_key);
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
            String keyUser = sm.getKeyUser();
            if (id == null) {
                JSONArray jsonArray = new JSONArray();
                for (ImportedAccount importedAccount : user.getImportedAccountMap().values()) {
                    importedAccount.decipher(keyUser);
                    jsonArray.put(importedAccount.getJson());
                }
                sm.setSuccess(jsonArray);
            } else {
                ImportedAccount importedAccount = user.getImportedAccount(Long.valueOf(id));
                if (importedAccount == null)
                    throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
                importedAccount.decipher(keyUser);
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
            String url = sm.getStringParam("url", true, false);
            if (url.equals("") || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Website website = catalog.getPublicWebsiteWithUrl(url, account_information.keySet(), hibernateQuery);
            if (website != null && (!website.getWebsiteAttributes().isIntegrated() || website.getWebsiteInformationList().size() > 2))
                website = null;
            User user = sm.getUser();
            String name = sm.getStringParam("name", true, false);
            ImportedAccount importedAccount = user.getImportedAccount(id);
            if (importedAccount == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such imported account");
            importedAccount.setName(name);
            importedAccount.setUrl(url);
            importedAccount.setWebsite(website);
            String symmetric_key = sm.getKeyUser();
            for (Object object : account_information.keySet()) {
                String information_name = (String) object;
                JSONObject value = account_information.getJSONObject(information_name);
                ImportedAccountInformation importedAccountInformation = importedAccount.getImportedAccountInformation(information_name);
                if (importedAccountInformation == null)
                    importedAccount.addImportedAccountInformation(new ImportedAccountInformation(information_name, AES.encrypt(value.getString("value"), symmetric_key), importedAccount));
                else
                    importedAccountInformation.setValue(AES.encrypt(value.getString("value"), symmetric_key));
            }
            Set<String> keysToRemove = new HashSet<>();
            for (String key : importedAccount.getImportedAccountInformationMap().keySet()) {
                if (account_information.optJSONObject(key) == null)
                    keysToRemove.add(key);
            }
            keysToRemove.forEach(importedAccount::removeImportedAccountInformation);
            sm.saveOrUpdate(importedAccount);
            user.addImportedAccount(importedAccount);
            importedAccount.decipher(sm.getKeyUser());
            sm.setSuccess(importedAccount.getJson());
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
