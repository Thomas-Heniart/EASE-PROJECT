package com.Ease.Context;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateDatabase;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.*;
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
                context.setAttribute("teamManager", teamManager);

                Stripe.apiKey = Variables.STRIPE_API_KEY;
                Stripe.apiVersion = "2017-08-15";

                context.setAttribute("publicKey", "UxJguNs0XrWs81eJLyiD+1M6UhH1/gFIuGim1xsBalKiuKO4q/g7W9cmK2QUWPrfLkC3eT3Ldg3liMpPYFmV0ygHit11rSLkn9uqrsUiBVd1NEyMHNdDPxyimCdm9XZ5/DlPwcsaAl7gZ2KVYb42bUc2JxofKfqRRfBst/4tRJY=");
                context.setAttribute("privateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIxSIq8SyhAkccc/R5Ew1F6bBkQlc5HHBkbhn/4q2WUo9A1z2TYrJnWkH/LZP2oIVo9oJvzN2Cwj4fwE9M0X3HnPiltlyzTRzBygQWPiLYckR4gcHmf6emZFFsITsp9FG/DDelUnxE1Wx8hxnHe7xE9eUy8aweB" +
                        "64X5Ygs1juv+dAgMBAAECgYBOCDsLevptYab4M4N8BViER9eDPAR50+D0t/H+KRv9+nlFpd/sFLDtiNjPduqh74MbesL7oQkO+uFoAeWUzCkprEGhR4ASXaosDfaFLZIkBvN6C5TKiQMEBmJZQpCqmLsklw6L2dok3Cg3JOs+zfAQOG0fR3pi2ty6zBZonGD7oQJBANJrqs8b6k" +
                        "pE7Eev5j8sopAqkjvP9uw8OjI6YZlgKQoAeIYzORBa2758cCXvK7sUJvPJhcNvVN8LN5HkHQA33GkCQQCqt0R75g92zKsClH04vrLowlx8jHcZEDur+wa8dpnDkePLNaB/E4ggbnFxDOgGV/W5uiNc3CRF09zNQaIotTMVAkEAxUT9SNex6dj97yNAZ9+vtBrFJ+ALbWP1Z1uGX" +
                        "qVhNChG1gG1sNiNhOLivR2h0OzOq+U1S4jXd5frjBHhAnBcaQJAEXyORkathNS4G56MKRjXiKIICTX2KRoRkq825G0dmPvDopgOpZWhlvjZI+RXi/bMk8auQp/GE2T9NAUqEBNndQJAE6+lsecozNONV1D4iKPX6eAhjZZn6iP+l8nht5Q9nxocj0hEQhd+uFLcB+KDRJjgsVzc" +
                        "mWyig4wiTBOq+BRDzg==");

                context.setAttribute("userManager", new UserManager());

                context.setAttribute("metrics", new Metrics(db));

                /* Timers */
                Timer time = new Timer(); // Instantiate Timer Object
                Calendar delay = Calendar.getInstance();
                int hour = delay.get(Calendar.HOUR_OF_DAY);
                int minutes = delay.get(Calendar.MINUTE);
                if (hour > 9 || (hour == 9 && minutes > 30))
                    delay.add(Calendar.DAY_OF_YEAR, 1);
                delay.set(Calendar.HOUR_OF_DAY, 9);
                delay.set(Calendar.MINUTE, 30);
                long next_clock = delay.getTimeInMillis() - new Date().getTime();
                StripeScheduledTask st = new StripeScheduledTask(teamManager); // Instantiate SheduledTask class
                time.schedule(st, 0, 12 * 60 * 60 * 1000); // Create Repetitively task for every 12 hours */
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