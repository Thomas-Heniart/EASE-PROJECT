package com.Ease.Mail;

import com.Ease.Context.Variables;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.sendgrid.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SendGridMail {

    private static final String API_KEY = "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";

    protected SendGrid sg;
    protected Request request;
    protected Response response;
    protected Email fromEmail;
    protected Mail mail;
    protected List<Personalization> personalizations;

    public SendGridMail(String senderName, String senderEmail) {
        sg = new SendGrid(API_KEY);
        sg.addRequestHeader("X-Mock", "true");
        request = new Request();
        mail = new Mail();

        fromEmail = new Email();
        fromEmail.setName(senderName);
        fromEmail.setEmail(senderEmail);
        mail.setFrom(fromEmail);
        personalizations = new LinkedList<Personalization>();
    }

    private Personalization createNewPersonalization() {
        Personalization personalization = new Personalization();
        personalization.addHeader("X-Mock", "true");
        this.personalizations.add(personalization);
        return personalization;
    }

    private Email createEmail(String name, String email) {
        Email newEmail = new Email();
        newEmail.setName(name);
        newEmail.setEmail(email);
        return newEmail;
    }

    private void addTo(Personalization personalization, String name, String email) {
        Email recipient = createEmail(name, email);
        personalization.addTo(recipient);
    }

    private void addCc(Personalization personalization, String name, String email) {
        Email recipient = createEmail(name, email);
        personalization.addCc(recipient);
    }

    private void addBcc(Personalization personalization, String name, String email) {
        Email recipient = createEmail(name, email);
        personalization.addBcc(recipient);
    }

    public void sendEmail() throws GeneralException {
        if (request.body == null && mail.getTemplateId() == null)
            throw new GeneralException(ServletManager.Code.InternError, "Empty email");
        try {
            for (Personalization personalization : this.personalizations)
                this.mail.addPersonalization(personalization);
            request.method = Method.POST;
            request.endpoint = "mail/send";
            if (mail.getTemplateId() != null)
                request.body = mail.build();
            response = sg.api(request);
            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);
        } catch (IOException e) {
            throw new GeneralException(ServletManager.Code.InternError, e);
        }
    }

    public void sendWelcomeEmail(String userName, String userEmail, String code) throws GeneralException {
        mail.setTemplateId("14d671a6-9a3e-482b-8dc1-39cac80b7bd8");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, userName, userEmail);
        personalization.addSubstitution("#username", userName);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "VerifieEmail?email=" + userEmail + "&code=" + code);
        this.setReplyTo("Victor", "victor@ease.space");
        this.sendEmail();
    }

    private void setReplyTo(String userName, String userEmail) {
        Email replyEmail = this.createEmail(userName, userEmail);
        this.mail.setReplyTo(replyEmail);
    }



    public void sendVerificationMainEmail(String username, String userEmail, String code) throws GeneralException {
        mail.setTemplateId("a3c330b0-8e7b-40ae-9e47-5b320a417caf");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, username, userEmail);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "VerifieEmail?email=" + userEmail + "&code=" + code);
        personalization.addSubstitution("#username", username);
        this.sendEmail();
    }


    public void sendVerificationEmail(String username, String userEmail, String code) throws GeneralException {
        mail.setTemplateId("616f29d7-5750-46b9-8581-480f177e41ef");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, username, userEmail);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "VerifieEmail?email=" + userEmail + "&code=" + code);
        personalization.addSubstitution("#username", username);
        this.sendEmail();
    }

    void sendReminderSixDaysEmail(String firstName, String email) throws GeneralException {
        mail.setTemplateId("2c03ac41-648f-49c4-95dc-7057a09de38b");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        this.sendEmail();
    }

    void sendReminderSixDaysLessThanFourAppsEmail(String firstName, String email) throws GeneralException {
        mail.setTemplateId("c3653947-71f3-447f-9674-469022880e85");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "home?importAccounts=true");
        this.sendEmail();
    }

    void sendReminderOneWeekRegistration(String firstName, String email) throws GeneralException {
        mail.setTemplateId("435078ae-41d8-4cb1-8dbf-63302f070545");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        this.sendEmail();
    }

    void sendReminderThirtyDays(String firstName, String email) throws GeneralException {
        mail.setTemplateId("8a3569be-b33d-4255-b4b3-a04844e2bc6f");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        this.sendEmail();
    }

    void sendReminderThirtyDaysLessThanFourApps(String firstName, String email) throws GeneralException {
        mail.setTemplateId("30d07b2e-36e7-4570-9bb7-bef38cb1378b");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "home?importAccounts=true");
        this.sendEmail();
    }

    void sendReminderThreeDaysLessThanFourApps(String firstName, String email) throws GeneralException {
        mail.setTemplateId("a0c968a0-9e78-43e5-8287-6e5359c67cac");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "home?importAccounts=true");
        this.sendEmail();
    }

    void sendReminderTwentyDays(String firstName, String email) throws GeneralException {
        mail.setTemplateId("74a00bd1-7cc0-4f40-9566-91342bdac7c2");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        this.sendEmail();
    }

    void sendReminderTwentyDaysLessThanFourApps(String firstName, String email) throws GeneralException {
        mail.setTemplateId("60c9fb19-7403-402a-b275-031f646be86e");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "home?importAccounts=true");
        this.sendEmail();
    }


    public void sendReminderTwoDaysLessThanFourApps(String firstName, String email) throws GeneralException {
        mail.setTemplateId("b85f9bea-c111-452e-926f-6e279449fb0c");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#username", firstName);
        personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "home?importAccounts=true");
        this.sendEmail();
    }
}
