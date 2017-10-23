package com.Ease.Mail;

import com.Ease.Context.Variables;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.Resource;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Email;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by thomas on 29/06/2017.
 */
public class MailJetBuilder {

    protected MailjetRequest request;
    protected MailjetClient client;
    protected JSONObject vars = null;
    protected MailjetResponse response;
    protected String cc = "";
    protected String to = "";
    protected JSONArray recipients = new JSONArray();

    public MailJetBuilder() {
        try {
            client = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE);
            request = new MailjetRequest(Email.resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MailJetBuilder(Resource resource) {
        try {
            client = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE);
            request = new MailjetRequest(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MailJetBuilder(Resource resource, Integer id) {
        try {
            client = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE);
            request = new MailjetRequest(resource, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFrom(String email, String name) {
        request.property(Email.FROMEMAIL, email);
        request.property(Email.FROMNAME, name);
    }

    public void setTemplateId(int id) {
        request.property(Email.MJTEMPLATEID, id);
    }

    public void addTo(String email) {
        if (!this.to.isEmpty())
            this.to += ", ";
        this.to += ("<" + email + ">");
    }

    public void addTo(String email, String name) {
        if (!this.to.isEmpty())
            this.to += ", ";
        this.to += (name + " <" + email + ">");
    }

    public void addCc(String email) {
        if (!this.cc.isEmpty())
            this.cc += ", ";
        this.cc += ("<" + email + ">");
    }

    public void addCc(String email, String name) {
        if (!this.cc.isEmpty())
            this.cc += ", ";
        this.cc += (name + "<" + email + ">");
    }

    public void addRecipient(String email, JSONObject vars) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Email", email);
        jsonObject.put("Vars", vars);
        this.recipients.put(jsonObject);
    }

    public void addVariable(String key, Object value) {
        if (this.vars == null)
            this.vars = new JSONObject();
        this.vars.put(key, value);
    }

    public void sendEmail() {
        if (this.recipients.length() != 0) {
            request.property(Email.RECIPIENTS, this.recipients);
        } else {
            request.property(Email.TO, this.to);
            request.property(Email.CC, this.cc);
        }
        if (this.vars != null) {
            request.property(Email.MJTEMPLATELANGUAGE, true);
            request.property(Email.VARS, this.vars);
        }
        try {
            response = client.post(request);
            System.out.println("MailJet status: " + response.getStatus());
            System.out.println(response.getData());
        } catch (MailjetException e) {
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    public void post() {
        try {
            response = client.post(request);
            System.out.println("MailJet status: " + response.getStatus());
            System.out.println(response.getData());
        } catch (MailjetException e) {
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    public void setSubject(String subject) {
        request.property(Email.SUBJECT, subject);
    }

    public void setTextPart(String textPart) {
        request.property(Email.TEXTPART, textPart);
    }

    public void setHtmlPart(String htmlPart) {
        request.property(Email.HTMLPART, htmlPart);
    }

    public void property(String key, Object value) {
        request.property(key, value);
    }

    public void setTemplateErrorDeliver() {
        request.property(Email.MJTEMPLATEERRORDELIVERY, true);
    }

    public void setTemplateErrorReporting() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Email", "thomas@ease.space");
        jsonObject.put("Name", "Thomas Heniart");
        request.property(Email.MJTEMPLATEERRORREPORTING, jsonObject);
    }
}
