package com.Ease.Context;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.WebsitesVisitedManager;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateDatabase;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.stripe.Stripe;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class OnStart implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        System.out.println("ServletContextListener destroyed");
        HibernateDatabase.getSessionFactory().close();
    }

    // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent evt) {
        ServletContext context = evt.getServletContext();
        DataBaseConnection db;
        try {
            db = new DataBaseConnection(DataBase.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        try {
            DatabaseRequest request;
            try {
                System.out.println("ServletContextListener starting on \"" + Variables.ENVIRONNEMENT + "\" ...");
                context.setAttribute("serverKey", ServerKey.loadServerKey(db));
                context.setAttribute("idGenerator", new IdGenerator());
                context.setAttribute("catalog", new Catalog(db, context));
                context.setAttribute("groupManager", new GroupManager());
                context.setAttribute("websitesVisitedManager", new WebsitesVisitedManager(db, context));
                TeamManager teamManager = new TeamManager(context, db);
                context.setAttribute("teamManager", teamManager);
                Stripe.apiKey = "sk_test_4Qqw6xcv7VQDmXBS5CZ9rz5T";
                Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
                context.setAttribute("publicKey", publicAndPrivateKey.getKey());
                context.setAttribute("privateKey", publicAndPrivateKey.getValue());
                context.setAttribute("userManager", new UserManager());
                //context.setAttribute("tipManager", new TipManager());

                /* Stripe timer */
                Timer time = new Timer(); // Instantiate Timer Object
                StripeScheduledTask st = new StripeScheduledTask(teamManager); // Instantiate SheduledTask class
                RemindersScheduledTask reminders = new RemindersScheduledTask(teamManager);
                time.schedule(st, 0, 12 * 60 * 60 * 1000); // Create Repetitively task for every 12 hours
                time.schedule(reminders, 0, 24 * 60 * 60 * 1000);
                List<String> colors = new ArrayList<String>();
                colors.add("#373B60");
                colors.add("#9B59B6");
                colors.add("#3498DB");
                colors.add("#5FD747");
                colors.add("#F1C50F");
                colors.add("#FF9D34");
                colors.add("#E74C3C");
                colors.add("#FF5E88");
                context.setAttribute("colors", colors);

                Infrastructure.loadInfrastructures(db, evt.getServletContext());
                Map<String, User> usersMap = new HashMap<String, User>();
                context.setAttribute("users", usersMap);
                Map<String, com.Ease.NewDashboard.User.User> userMap = new HashMap<String, com.Ease.NewDashboard.User.User>();
                context.setAttribute("userMap", userMap);
                Map<String, User> sessionIdUserMap = new HashMap<String, User>();
                context.setAttribute("sessionIdUserMap", sessionIdUserMap);
                Map<String, User> sIdUserMap = new HashMap<String, User>();
                context.setAttribute("sIdUserMap", sIdUserMap);
                System.out.println("done.");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date mydate = new Date();
                String date = dateFormat.format(mydate);
                request = db.prepareRequest("INSERT INTO logs values('Server Start', 200, NULL, '', 'Server started correctly', ?);");
                request.setString(date);
            } catch (GeneralException e1) {
                System.out.println("Start failed");
                String logResponse = URLEncoder.encode(e1.getMsg(), "UTF-8");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date mydate = new Date();
                String date = dateFormat.format(mydate);
                request = db.prepareRequest("INSERT INTO logs values('Server Start', 1, NULL, '', ?, ?);");
                request.setString(logResponse);
                request.setString(date);
            }
            request.set();
        } catch (Exception e2) {
            e2.printStackTrace();
            return;
        }
    }
}