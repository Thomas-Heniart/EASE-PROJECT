package com.Ease.API.V1.Admin;

import com.Ease.Context.Variables;
import com.Ease.Utils.Servlets.PostServletManager;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

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
            JSONObject backgrounds = sm.getJsonParam("backgrounds", false, false);
            for (Object key : backgrounds.keySet()) {
                String index = (String) key;
                System.out.println(index);
                Integer value_index = Integer.valueOf(index);
                if (value_index < 1 || value_index > 7)
                    continue;
                String base64_string = backgrounds.getString(index);
                FileOutputStream file = new FileOutputStream(Variables.BACKGROUND_PATH + "background_" + index + ".jpeg");
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
