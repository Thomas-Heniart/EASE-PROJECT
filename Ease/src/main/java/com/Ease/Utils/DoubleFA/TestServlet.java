package com.Ease.Utils.DoubleFA;

import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/TestDoubleFactor")
public class TestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String seed = sm.getParam("key", false).toUpperCase();
            long seconds = System.currentTimeMillis() / 1000l;
            long remain = 30 - seconds % 30L;
            long gTime = seconds / 30L;
            if (seconds >= 0) {
                gTime =  seconds / 30L;
            } else {
                gTime = (seconds - (30L - 1L)) / 30L;
            }
            String gTme = Long.toHexString(gTime).toUpperCase();
            while (gTme.length() < 16) gTme = "0" + gTme;
            String totpCode = TOTP.generateTOTP(seed, gTme, "6", "HMACSHA1");
            sm.setSuccess(totpCode);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
