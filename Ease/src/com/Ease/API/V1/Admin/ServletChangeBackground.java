package com.Ease.API.V1.Admin;

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

@WebServlet("/changeBackground")
public class ServletChangeBackground extends HttpServlet {

    // upload settings
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);

        try {
            sm.needToBeEaseAdmin();
            if (!ServletFileUpload.isMultipartContent(request))
                throw new HttpServletException(HttpStatus.Forbidden, "Not multipart request");

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
            String uploadPath = getServletContext().getRealPath("resources/backgrounds");

            // parses the request's content to extract file data
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String filePath;
                        File storeFile;
                        filePath = uploadPath + "/" + "background.jpeg";
                        storeFile = new File(filePath);
                        System.out.println(storeFile.getAbsoluteFile());
                        System.out.println(storeFile.getCanonicalPath());
                        if (storeFile.exists()) {
                            String newPathName = uploadPath + "/" + "background_old";
                            File temp = new File(newPathName + ".jpeg");
                            storeFile.renameTo(temp);
                        } else
                            storeFile.createNewFile();
                        item.write(storeFile);
                    }
                }
            }
            sm.getHibernateQuery().commit();
            sm.setRedirectUrl("/admin");
            sm.setSuccess("Background changed");
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
