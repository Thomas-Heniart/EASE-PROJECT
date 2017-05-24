package com.Ease.API.V1.Catalog;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
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
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String website_id = sm.getServletParam("id", true);
            if (website_id == null || website_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Website is null");
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithSingleId(Integer.parseInt(website_id));
            sm.setResponse(ServletManager.Code.Success, website.getInformationJson().toString());
            sm.setLogResponse("GetWebsiteInformation done.");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
