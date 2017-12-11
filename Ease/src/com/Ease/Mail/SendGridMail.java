package com.Ease.Mail;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

    public void sendEmail() throws HttpServletException {
        if (request.body == null && mail.getTemplateId() == null)
            throw new HttpServletException(HttpStatus.InternError);
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
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    private void setReplyTo(String userName, String userEmail) {
        Email replyEmail = this.createEmail(userName, userEmail);
        this.mail.setReplyTo(replyEmail);
    }
}
