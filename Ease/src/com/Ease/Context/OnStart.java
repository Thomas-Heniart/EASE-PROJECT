package com.Ease.Context;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateDatabase;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Test.TestA;
import com.Ease.Utils.Test.TestB;
import com.stripe.Stripe;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OnStart implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        System.out.println("ServletContextListener destroyed");
        HibernateDatabase.getSessionFactory().close();
    }

    /* @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            HibernateQuery hibernateQuery = new HibernateQuery();
            hibernateQuery.queryString("SELECT t FROM teams t where t.id = 35");
            Team team = (Team) hibernateQuery.getSingleResult();
            System.out.println(team.getDefaultChannel().getTeamUsers().contains(team.getTeamUserWithId(220)));
            hibernateQuery.queryString("SELECT t FROM testA t WHERE t.id= 1");
            TestA testA = (TestA) hibernateQuery.getSingleResult();
            testA.bar();
            System.out.println("Je suis là");
            System.out.println(testA.getTestBSet().get(0) == null);
            hibernateQuery.queryString("SELECT t FROM testB t WHERE t.id = 1");
            TestB testB = (TestB) hibernateQuery.getSingleResult();
            System.out.println(testA.getTestBSet().get(0) == testB);
            System.out.println(testA == testB.getTestA());
            hibernateQuery.commit();
        } catch (Exception e) {
            e.printStackTrace();
    } */

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
                com.Ease.Catalog.Catalog catalog = new com.Ease.Catalog.Catalog();
                catalog.populate();
                context.setAttribute("catalog", catalog);

                TeamManager teamManager = new TeamManager(context, db);
                /* for (Team team : teamManager.getTeams()) {
                    System.out.println(team.getName());
                    TeamUser teamUser = team.getTeamUserOwner();
                    System.out.println(team.getDefaultChannel().getTeam() == teamUser.getTeam());
                    System.out.println(team.getDefaultChannel().getTeamUsers().contains(teamUser));
                } */
                context.setAttribute("teamManager", teamManager);

                Stripe.apiKey = Variables.STRIPE_API_KEY;
                Stripe.apiVersion = "2017-08-15";

                Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
                context.setAttribute("publicKey", publicAndPrivateKey.getKey());
                context.setAttribute("privateKey", publicAndPrivateKey.getValue());

                context.setAttribute("userManager", new UserManager());

                context.setAttribute("metrics", new Metrics(db));

                Timer time = new Timer();
                Calendar delay = Calendar.getInstance();
                int hour = delay.get(Calendar.HOUR_OF_DAY);
                int minutes = delay.get(Calendar.MINUTE);
                if (hour > 9 || (hour == 9 && minutes > 30))
                    delay.add(Calendar.DAY_OF_YEAR, 1);
                delay.set(Calendar.HOUR_OF_DAY, 9);
                delay.set(Calendar.MINUTE, 30);
                long next_clock = delay.getTimeInMillis() - new Date().getTime();
                StripeScheduledTask st = new StripeScheduledTask(teamManager);
                time.schedule(st, 0, 12 * 60 * 60 * 1000);
                WebsiteScheduledTask websiteScheduledTask = new WebsiteScheduledTask(catalog);
                time.schedule(websiteScheduledTask, 0, 24 * 60 * 60 * 1000);
                RemindersScheduledTask reminders = new RemindersScheduledTask(teamManager);
                time.schedule(reminders, next_clock, 24 * 60 * 60 * 1000);

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
                Map<String, User> usersMap = new ConcurrentHashMap<>();
                context.setAttribute("users", usersMap);
                Map<String, User> sessionIdUserMap = new ConcurrentHashMap<>();
                context.setAttribute("sessionIdUserMap", sessionIdUserMap);
                Map<String, User> sIdUserMap = new ConcurrentHashMap<>();
                context.setAttribute("sIdUserMap", sIdUserMap);
                Map<String, User> tokenUserMap = new ConcurrentHashMap<>();
                context.setAttribute("tokenUserMap", tokenUserMap);
                byte[] bytes = Base64.getDecoder().decode("dv10ARxtwGifQ+cLHLlBdv7BhvF0YOT7zRDyvaId1OkMmAb2beTM+BGc7z8z+6xcGcq1TOd7FlOaFR8LFimrgw==");
                context.setAttribute("secret", new SecretKeySpec(bytes, SignatureAlgorithm.HS512.getJcaName()));
                System.out.println("done.");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date mydate = new Date();
                String date = dateFormat.format(mydate);
                request = db.prepareRequest("INSERT INTO logs values('Server Start', 200, NULL, '', 'Server started correctly', ?);");
                request.setString(date);

                HibernateQuery hibernateQuery = new HibernateQuery();
                hibernateQuery.queryString("SELECT t FROM testA t WHERE t.id= 1");
                TestA testA = (TestA) hibernateQuery.getSingleResult();
                testA.bar();
                try {
                    System.out.println("Je suis là");
                    System.out.println(testA.getTestBSet().get(0) == null);
                    hibernateQuery.queryString("SELECT t FROM testB t WHERE t.id = 1");
                    TestB testB = (TestB) hibernateQuery.getSingleResult();
                    System.out.println(testA.getTestBSet().get(0) == testB);
                    System.out.println(testA == testB.getTestA());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hibernateQuery.commit();
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