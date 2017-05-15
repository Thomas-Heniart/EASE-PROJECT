package com.Ease.API.Utils;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by thomas on 15/05/2017.
 */
@WebServlet("/api/utils/GenerateAlgoliaJson")
public class ServletGenerateAlgoliaJson extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/javascript");
        response.setHeader("Content-disposition","attachment; filename=catalogAlgolia.json");
        Catalog catalog = (Catalog) request.getServletContext().getAttribute("catalog");
        JSONArray jsonArray = new JSONArray();
        for (Website website : catalog.getWebsites())
            jsonArray.add(website.getSimpleJson());
        byte[] buffer = jsonArray.toString().getBytes(Charset.forName("UTF-8"));

        for (int i = 0; i < buffer.length; i++) {
            out.write(buffer[i]);
        }
        out.flush();
    }
}
