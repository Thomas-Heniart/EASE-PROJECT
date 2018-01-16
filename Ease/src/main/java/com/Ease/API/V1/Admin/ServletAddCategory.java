package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Category;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/AddCategory")
public class ServletAddCategory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            String name = sm.getStringParam("name", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            List<Category> categoryList = catalog.getCategories(sm.getHibernateQuery());
            for (Category category : categoryList) {
                if (category.getName().equals(name))
                    throw new HttpServletException(HttpStatus.BadRequest, "Already a category with this name.");
            }
            Category category = new Category(name, (categoryList.size() + 1) * 10);
            sm.saveOrUpdate(category);
            sm.setSuccess(category.getJson());
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
