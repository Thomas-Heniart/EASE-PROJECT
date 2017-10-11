package com.Ease.API.V1.Catalog;

import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/catalog/GetCategories")
public class GetCategories extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            JSONObject res = new JSONObject();
            JSONArray categories = new JSONArray();
            JSONObject cat_one = new JSONObject();
            cat_one.put("id", 1);
            cat_one.put("name", "Category 1");
            categories.add(cat_one);
            JSONObject cat_two = new JSONObject();
            cat_two.put("id", 2);
            cat_two.put("name", "Category 2");
            categories.add(cat_two);
            res.put("categories", categories);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
