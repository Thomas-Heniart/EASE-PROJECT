package com.Ease.API.V1.Catalog;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 15/05/2017.
 */
@WebServlet("/api/v1/catalog/GetWebsiteInformation")
public class ServletGetWebsiteInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer website_id = sm.getIntParam("id", true);
            if (website_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Missing website id.");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id);
            sm.setSuccess(website.getInformationJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
