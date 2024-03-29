package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/UploadWebsite")
public class ServletUploadWebsite extends HttpServlet {

    // upload settings
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            if (!ServletFileUpload.isMultipartContent(request))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong form enctype");
            // configures upload settings
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // sets memory threshold - beyond which files are stored in disk
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // sets temporary location to store files
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);

            // sets maximum size of upload file
            upload.setFileSizeMax(MAX_FILE_SIZE);

            // sets maximum size of request (include file + form data)
            upload.setSizeMax(MAX_REQUEST_SIZE);

            // constructs the directory path to store upload file
            // this path is relative to application's directory
            String uploadPath = "";

            // creates the directory if it does not exist

            // parses the request's content to extract file data
            List<FileItem> formItems = upload.parseRequest(request);
            Website website = null;
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    if (item.isFormField()) {
                        if (item.getFieldName().equals("website_id")) {
                            Catalog catalog = (Catalog) request.getServletContext().getAttribute("catalog");
                            website = catalog.getWebsiteWithId(Integer.parseInt(item.getString()), hibernateQuery);
                            String folder = website.getFolder();
                            uploadPath = Variables.WEBSITES_FOLDER_PATH + folder;
                            File uploadDir = new File(uploadPath);
                            if (!uploadDir.exists()) {
                                uploadDir.mkdir();
                            }
                        }
                    }
                }
                if (website == null)
                    throw new HttpServletException(HttpStatus.BadRequest, "No website");
                // processes only fields that are not form fields
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath;
                        File storeFile;
                        if (fileName.endsWith(".json")) {

                            filePath = uploadPath + File.separator + "connect.json";
                            storeFile = new File(filePath);
                            if (storeFile.exists())
                                storeFile.renameTo(new File(uploadPath + File.separator + "connect_old.json"));
                            item.write(storeFile);
                        }
                        if (fileName.endsWith(".png")) {
                            filePath = uploadPath + File.separator + "logo.png";
                            storeFile = new File(filePath);
                            if (storeFile.exists())
                                storeFile.renameTo(new File(uploadPath + File.separator + "logo_old.png"));
                            website.getWebsiteAttributes().setLogo_url(null);
                            website.setLogo_version(website.getLogo_version() + 1);
                            hibernateQuery.saveOrUpdateObject(website);
                            item.write(storeFile);
                        }
                    }
                }
            }
            hibernateQuery.commit();
            sm.setRedirectUrl("/admin");
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
