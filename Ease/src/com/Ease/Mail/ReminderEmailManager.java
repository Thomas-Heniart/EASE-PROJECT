package com.Ease.Mail;

import com.Ease.Hibernate.HibernateQuery;

import java.util.List;

public class ReminderEmailManager {
    public ReminderEmailManager() {

    }

    private void reminderSixDays() {
        System.out.println("reminderSixDays start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(apps.id) AS appCount FROM users JOIN status ON (users.status_id = status.id) JOIN profiles ON (profiles.user_id = users.id)  JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) JOIN websiteApps ON (apps.id = websiteApps.app_id) WHERE DATE(last_connection) = DATE_SUB(CURDATE(), INTERVAL 6 DAY) AND websiteApps.type <> 'websiteApp' GROUP BY firstName, email) AS t WHERE appCount > 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderSixDays to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderSixDays to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderSixDaysEmail(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderSixDays end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }

    private void reminderSixDaysLessThanFourApps() {
        System.out.println("reminderSixDaysLessThanFourApps start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(websiteApps.id) AS appCount FROM users JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) LEFT JOIN websiteApps ON (apps.id = websiteApps.app_id AND websiteApps.type <> 'websiteApp') WHERE DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 6 DAY) GROUP BY firstName, email) AS t WHERE appCount <= 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderSixDaysLessThanFourApps to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderSixDaysLessThanFourApps to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderSixDaysLessThanFourAppsEmail(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderSixDaysLessThanFourApps end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderSixDaysLessThanFourApps error...");
        }
    }

    private void reminderOneWeekRegistration() {
        System.out.println("reminderOneWeekRegistration start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT DISTINCT user_id, firstName, email, COUNT(distinct DATE(date)) AS dateCount FROM logs JOIN users ON (users.id = user_id) WHERE servlet_name LIKE '%AskInfo' AND DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND date BETWEEN DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND NOW() GROUP BY user_id, firstName, email) AS t WHERE t.dateCount >= 2;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderOneWeekRegistration to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderOneWeekRegistration to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderOneWeekRegistration(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderOneWeekRegistration end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderOneWeekRegistration error...");
        }
    }

    private void reminderThirtyDays() {
        System.out.println("reminderThirtyDays start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(apps.id) AS appCount FROM users JOIN status ON (users.status_id = status.id) JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) JOIN websiteApps ON (apps.id = websiteApps.app_id) WHERE DATE(last_connection) = DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND websiteApps.type <> 'websiteApp' GROUP BY firstName, email) AS t WHERE appCount > 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderThirtyDays to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderThirtyDays to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderThirtyDays(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderThirtyDays end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderThirtyDays error...");
        }
    }

    private void reminderThirtyDaysLessThanFourApps() {
        System.out.println("reminderThirtyDaysLessThanFourApps start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(websiteApps.id) AS appCount FROM users JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) LEFT JOIN websiteApps ON (apps.id = websiteApps.app_id AND websiteApps.type <> 'websiteApp') WHERE DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 30 DAY) GROUP BY firstName, email) AS t WHERE appCount <= 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderThirtyDaysLessThanFourApps to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderThirtyDaysLessThanFourApps to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderThirtyDaysLessThanFourApps(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderThirtyDaysLessThanFourApps end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderThirtyDaysLessThanFourApps error...");
        }
    }

    private void reminderEmailThreeDaysLessThanFourApps() {
        System.out.println("reminderEmailThreeDaysLessThanFourApps start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(websiteApps.id) AS appCount FROM users JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) LEFT JOIN websiteApps ON (apps.id = websiteApps.app_id AND websiteApps.type <> 'websiteApp') WHERE DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 30 DAY) GROUP BY firstName, email) AS t WHERE appCount <= 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderEmailThreeDaysLessThanFourApps to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderEmailThreeDaysLessThanFourApps to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderThreeDaysLessThanFourApps(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderEmailThreeDaysLessThanFourApps end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderEmailThreeDaysLessThanFourApps error...");
        }
    }

    private void reminderEmailTwentyDays() {
        System.out.println("reminderEmailTwentyDays start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(apps.id) AS appCount FROM users JOIN status ON (users.status_id = status.id) JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) JOIN websiteApps ON (apps.id = websiteApps.app_id) WHERE DATE(last_connection) = DATE_SUB(CURDATE(), INTERVAL 20 DAY) AND websiteApps.type <> 'websiteApp' GROUP BY firstName, email) AS t WHERE appCount > 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderEmailTwentyDays to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderEmailTwentyDays to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderTwentyDays(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderEmailTwentyDays end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderEmailTwentyDays error...");
        }
    }

    private void reminderEmailTwentyDaysLessThanFourApps() {
        System.out.println("reminderEmailTwentyDaysLessThanFourApps start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(websiteApps.id) AS appCount FROM users JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) LEFT JOIN websiteApps ON (apps.id = websiteApps.app_id AND websiteApps.type <> 'websiteApp') WHERE DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 20 DAY) GROUP BY firstName, email) AS t WHERE appCount <= 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderEmailTwentyDaysLessThanFourApps to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderEmailTwentyDaysLessThanFourApps to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderTwentyDaysLessThanFourApps(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderEmailTwentyDaysLessThanFourApps end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderEmailTwentyDaysLessThanFourApps error...");
        }
    }

    private void reminderEmailTwoDaysLessThanFourApps() {
        System.out.println("reminderEmailTwoDaysLessThanFourApps start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.querySQLString("SELECT firstName, email FROM (SELECT firstName, email, count(websiteApps.id) AS appCount FROM users JOIN profiles ON (profiles.user_id = users.id) JOIN profileAndAppMap ON (profiles.id = profileAndAppMap.profile_id) JOIN apps ON (apps.id = profileAndAppMap.app_id) LEFT JOIN websiteApps ON (apps.id = websiteApps.app_id AND websiteApps.type <> 'websiteApp') WHERE DATE(registration_date) = DATE_SUB(CURDATE(), INTERVAL 2 DAY) GROUP BY firstName, email) AS t WHERE appCount <= 3;");
            List<Object> rs = hibernateQuery.list();
            System.out.println("reminderEmailTwoDaysLessThanFourApps to " + rs.size() + " people");
            for (Object object : rs) {
                Object[] firstNameAndEmail = (Object[]) object;
                String firstName = (String) firstNameAndEmail[0];
                String email = (String) firstNameAndEmail[1];
                System.out.println("reminderEmailTwoDaysLessThanFourApps to " + firstName + " " + email);
                SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
                sendGridMail.sendReminderTwoDaysLessThanFourApps(firstName, email);
            }
            hibernateQuery.commit();
            System.out.println("reminderEmailTwoDaysLessThanFourApps end...");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
            System.out.println("reminderEmailTwoDaysLessThanFourApps error...");
        }
    }

    public void lunchReminders() {
        this.reminderEmailThreeDaysLessThanFourApps();
        this.reminderEmailTwentyDays();
        this.reminderEmailTwentyDaysLessThanFourApps();
        this.reminderEmailTwoDaysLessThanFourApps();
        this.reminderOneWeekRegistration();
        this.reminderSixDays();
        this.reminderSixDaysLessThanFourApps();
        this.reminderThirtyDays();
        this.reminderThirtyDaysLessThanFourApps();
    }

}
