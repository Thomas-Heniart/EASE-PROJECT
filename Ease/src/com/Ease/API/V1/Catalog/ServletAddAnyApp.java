package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteAttributes;
import com.Ease.Catalog.WebsiteInformation;
import com.Ease.Context.Variables;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.NewDashboard.Profile;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/api/v1/catalog/AddAnyApp")
public class ServletAddAnyApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String url = sm.getStringParam("url", false, false);
            if (!Regex.isSimpleUrl(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = sm.getUser().getProfile(profile_id);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithUrl(url, sm.getHibernateQuery());
            App app;
            String symmetric_key = sm.getKeyUser();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            if (website != null && website.getWebsiteAttributes().isIntegrated())
                app = AppFactory.getInstance().createClassicApp(name, website, symmetric_key, account_information);
            else {
                if (website == null) {
                    String image_bytes = sm.getStringParam("image", false, false);
                    InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(image_bytes));
                    ImageIO.read(inputStream);
                    String folder = url.split("//")[1].split("/")[0].replaceAll("\\.", "_");
                    WebsiteAttributes websiteAttributes = new WebsiteAttributes(true);
                    website = new Website(url, folder, folder, url, websiteAttributes);
                    String uploadPath = Variables.PROJECT_PATH + Variables.WEBSITES_PATH + folder;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists())
                        uploadDir.mkdir();
                    String filePath = uploadPath + File.separator + "logo.png";
                    OutputStream outputStream = new FileOutputStream(new File(filePath));
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    while ((read = inputStream.read(bytes)) != -1)
                        outputStream.write(bytes, 0, read);
                    WebsiteInformation loginInformation = new WebsiteInformation("login", "text", 0, "Login", "fa-user-o", website);
                    WebsiteInformation passwordInformation = new WebsiteInformation("password", "password", 1, "Password", "fa-lock", website);
                    Set<WebsiteInformation> websiteInformationSet = new HashSet<>();
                    websiteInformationSet.add(loginInformation);
                    websiteInformationSet.add(passwordInformation);
                    website.setWebsiteInformationList(websiteInformationSet);
                    sm.saveOrUpdate(websiteAttributes);
                    sm.saveOrUpdate(website);
                }
                app = AppFactory.getInstance().createAnyApp(name, website, symmetric_key, account_information);
            }
            app.setProfile(profile);
            app.setPosition(profile.getSize());
            sm.saveOrUpdate(app);
            profile.addApp(app);
            sm.setSuccess(app.getJson());
        } catch (IIOException e) {
            sm.setError(new HttpServletException(HttpStatus.BadRequest, "You must provide a valid image"));
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
