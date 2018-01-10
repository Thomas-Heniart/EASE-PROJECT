package com.Ease.API.V1.Admin;

import com.Ease.Context.Variables;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;

@WebServlet("/api/v1/admin/UploadBackground")
public class ServletUploadBackground extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONArray backgrounds = sm.getArrayParam("backgrounds", false, false);
            if (backgrounds.length() != 7)
                throw new HttpServletException(HttpStatus.BadRequest, "You must give 7 pictures");
            for (int i=0; i<backgrounds.length(); i++) {
                String base64_string = backgrounds.getString(i);
                FileOutputStream file = new FileOutputStream(Variables.BACKGROUND_PATH + "background_" + i + ".jpeg");
                file.write(Base64.decodeBase64(base64_string));
                file.close();
            }
            sm.setSuccess("done");
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
