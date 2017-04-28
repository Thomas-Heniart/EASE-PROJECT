package com.Ease.Servlet.Hibernate;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 24/04/2017.
 */
@WebServlet("/ServletRemoveDuplicates")
public class ServletRemoveDuplicates extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            /* ClassicApp website_app_id */
            DatabaseRequest db_request = db.prepareRequest("SELECT website_app_id FROM classicApps GROUP BY website_app_id HAVING Count(*) > 1;");
            DatabaseResult rs = db_request.get();
            List<Integer> ids = new LinkedList<Integer>();
            while(rs.next())
                ids.add(rs.getInt(1));
            /* Save account_id to delete it */
            List<Integer> account_ids_to_delete = new LinkedList<Integer>();
            for (Integer id : ids) {
                db_request = db.prepareRequest("SELECT account_id FROM classicApps WHERE website_app_id = ?;");
                db_request.setInt(id);
                rs = db.get();
                while (rs.next())
                    account_ids_to_delete.add(rs.getInt(1));
                db_request = db.prepareRequest("DELETE FROM classicApps WHERE website_app_id = ?;");
                db_request.setInt(id);
                db_request.set();
            }
            for (Integer id : account_ids_to_delete) {
                db_request = db.prepareRequest("DELETE FROM accountsInformations WHERE account_id = ?;");
                db_request.setInt(id);
                db_request.set();
                db_request = db.prepareRequest("DELETE FROM accounts WHERE id = ?;");
                db_request.setInt(id);
                db_request.set();
            }
            /* Get Apps ids */
            List<Integer> appIds = new LinkedList<Integer>();
            for (Integer id : ids) {
                db_request = db.prepareRequest("SELECT app_id FROM websiteApps WHERE id = ?;");
                db_request.setInt(id);
                rs = db.get();
                while (rs.next())
                    appIds.add(rs.getInt(1));
                db_request = db.prepareRequest("DELETE FROM websiteApps WHERE id = ?;");
                db_request.setInt(id);
                db_request.set();
            }
            /* Get appInfo ids to delete */
            List<Integer> app_info_id = new LinkedList<Integer>();
            db_request = db.prepareRequest("ALTER TABLE profileAndAppMap DROP FOREIGN KEY profileandappmap_ibfk_2;");
            db_request.set();
            for (Integer id : appIds) {
                db_request = db.prepareRequest("SELECT app_info_id FROM apps WHERE id = ?;");
                db_request.setInt(id);
                rs = db_request.get();
                while(rs.next())
                    app_info_id.add(rs.getInt(1));
                db_request = db.prepareRequest("DELETE FROM apps WHERE id = ?;");
                db_request.setInt(id);
                db_request.set();
            }
            db_request = db.prepareRequest("DELETE FROM profileAndAppMap WHERE app_id NOT IN (SELECT id FROM apps);");
            db_request.set();
            db_request = db.prepareRequest("ALTER TABLE profileAndAppMap ADD FOREIGN KEY (app_id) REFERENCES apps(id);");
            db_request.set();
            for (Integer id : app_info_id) {
                db_request = db.prepareRequest("DELETE FROM appsInformations WHERE id = ?;");
                db_request.setInt(id);
                db_request.set();
            }
            db.commitTransaction(transaction);
            sm.setResponse(ServletManager.Code.Success, "Remove duplicated classic apps");
        } catch(GeneralException e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }
}
