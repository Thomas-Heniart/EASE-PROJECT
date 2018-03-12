package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.User.User;
import com.Ease.User.UserPostRegistrationEmails;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class PostRegistrationEmailScheduledTask extends TimerTask {
    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.queryString("SELECT u FROM User u");
            List<User> users = hibernateQuery.list();
            Calendar calendar = Calendar.getInstance();
            MailJetBuilder mailJetBuilder;
            for (User user : users) {
                if (!user.getUserStatus().isRegistered())
                    continue;
                Calendar user_calendar = Calendar.getInstance();
                user_calendar.setTime(user.getRegistration_date());
                user_calendar.add(Calendar.DAY_OF_YEAR, 2);
                UserPostRegistrationEmails userPostRegistrationEmails = user.getUserPostRegistrationEmails();
                if (DateUtils.isSameDay(calendar, user_calendar) && !userPostRegistrationEmails.isEmail_j2_sent()) {
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setTemplateId(287037);
                    mailJetBuilder.addTo(user.getEmail());
                    mailJetBuilder.addVariable("url", Variables.URL_PATH + "#/main/catalog/website");
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                    mailJetBuilder.sendEmail();
                    userPostRegistrationEmails.setEmail_j2_sent(true);
                    hibernateQuery.saveOrUpdateObject(userPostRegistrationEmails);
                    continue;
                }
                user_calendar.add(Calendar.DAY_OF_YEAR, 2);
                if (DateUtils.isSameDay(calendar, user_calendar) && !userPostRegistrationEmails.isEmail_j4_sent()) {
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setTemplateId(287040);
                    mailJetBuilder.addTo(user.getEmail());
                    mailJetBuilder.addVariable("url_dashboard", Variables.URL_PATH);
                    mailJetBuilder.addVariable("url_settings", Variables.URL_PATH + "#/main/settings");
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                    mailJetBuilder.sendEmail();
                    userPostRegistrationEmails.setEmail_j4_sent(true);
                    hibernateQuery.saveOrUpdateObject(userPostRegistrationEmails);
                    continue;
                }
                user_calendar.add(Calendar.DAY_OF_YEAR, 2);
                if (DateUtils.isSameDay(calendar, user_calendar) && !userPostRegistrationEmails.isEmail_j6_sent()) {
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setTemplateId(287043);
                    mailJetBuilder.addTo(user.getEmail());
                    mailJetBuilder.addVariable("url_security", Variables.URL_PATH + "security");
                    mailJetBuilder.addVariable("url_team_preview", Variables.URL_PATH + "#/main/teamsPreview");
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                    mailJetBuilder.sendEmail();
                    userPostRegistrationEmails.setEmail_j6_sent(true);
                    hibernateQuery.saveOrUpdateObject(userPostRegistrationEmails);
                    continue;
                }
                if (!userPostRegistrationEmails.isEmail_use_seven_on_fourteen_days_sent() && user.wasConnected(7, 14, hibernateQuery)) {
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setTemplateId(287233);
                    mailJetBuilder.addTo(user.getEmail());
                    mailJetBuilder.setFrom("benjamin@ease.space", "Benjamin Prigent");
                    mailJetBuilder.activateTemplateLanguage();
                    mailJetBuilder.sendEmail();
                    userPostRegistrationEmails.setEmail_use_seven_on_fourteen_days_sent(true);
                    hibernateQuery.saveOrUpdateObject(userPostRegistrationEmails);
                }
                user_calendar.add(Calendar.DAY_OF_YEAR, 7);
                if (DateUtils.isSameDay(calendar, user_calendar) && !userPostRegistrationEmails.isEmail_j13_sent()) {
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setTemplateId(287223);
                    mailJetBuilder.addTo(user.getEmail());
                    mailJetBuilder.setFrom("benjamin@ease.space", "Benjamin Prigent");
                    mailJetBuilder.activateTemplateLanguage();
                    mailJetBuilder.sendEmail();
                    userPostRegistrationEmails.setEmail_j13_sent(true);
                    hibernateQuery.saveOrUpdateObject(userPostRegistrationEmails);
                }
            }
            hibernateQuery.commit();
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }
}
