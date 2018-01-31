package com.Ease.Mail;

import com.Ease.Context.Variables;
import com.Ease.Team.TeamUser;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;

public class MailjetMessageWrapper {
    private MailjetClient mailjetClient = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE, new ClientOptions("v3.1"));
    private MailjetRequest mailjetRequest = new MailjetRequest(Emailv31.resource);
    private JSONArray messsages = new JSONArray();
    private JSONObject message = new JSONObject();
    private JSONObject variables = new JSONObject();
    private JSONArray to = new JSONArray();

    public JSONArray getMesssages() {
        return messsages;
    }

    public void setMesssages(JSONArray messsages) {
        this.messsages = messsages;
    }

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    public JSONObject getVariables() {
        return variables;
    }

    public void setVariables(JSONObject variables) {
        this.variables = variables;
    }

    public void setCurrentMessage(int index) {
        this.setMessage(this.getMesssages().getJSONObject(index));
    }

    public void setFrom(String email, String name) {
        JSONObject from = new JSONObject().put("Email", email).put("Name", name);
        this.getMessage().put(Emailv31.Message.FROM, from);
    }

    public void addTo(String email, String name) {
        to.put(new JSONObject().put("Email", email).put("Name", name));
    }

    public void addTo(String email) {
        to.put(new JSONObject().put("Email", email));
    }

    public void setTemplateId(Integer id) {
        this.getMessage().put(Emailv31.Message.TEMPLATEID, id);
    }

    public void setVariable(String key, Object value) {
        this.getMessage().put(Emailv31.Message.TEMPLATELANGUAGE, true);
        this.getVariables().put(key, value);
    }

    public void setErrorReporting() {
        this.getMessage().put(Emailv31.Message.TEMPLATELANGUAGE, true);
        this.getMessage().put(Emailv31.Message.TEMPLATEERROR_DELIVERY, true);
        this.getMessage().put(Emailv31.Message.TEMPLATEERROR_REPORTING, new JSONObject().put("Email", "thomas@ease.space").put("Name", "Thomas Heniart"));
    }

    public void send() {
        message.put(Emailv31.Message.TO, to);
        message.put(Emailv31.Message.VARIABLES, this.getVariables());
        System.out.println(message.getJSONObject(Emailv31.Message.VARIABLES));
        mailjetRequest.property(Emailv31.MESSAGES, new JSONArray().put(message));
        try {
            MailjetResponse mailjetResponse = mailjetClient.post(mailjetRequest);
            System.out.println(mailjetResponse.getStatus());
            System.out.println(mailjetResponse.getData());
        } catch (MailjetException e) {
            e.printStackTrace();
        } catch (MailjetSocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void newAccountsMail(TeamUser teamUser, JSONArray apps, Integer number_of_apps) throws MailjetSocketTimeoutException, MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "contact@ease.space")
                                        .put("Name", "Ease.Space"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", teamUser.getEmail())
                                                .put("Name", teamUser.getUsername())))
                                .put(Emailv31.Message.TEMPLATEID, 301785)
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("apps", apps)
                                        .put("number_of_apps", number_of_apps)
                                        .put("link", teamUser.isVerified() ? Variables.URL_PATH : (Variables.URL_PATH + "#/teamJoin/" + teamUser.getInvitation_code()))
                                        .put("link_name", teamUser.isVerified() ? "Check your new apps" : "Activate account & check new apps"))
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.TEMPLATEERROR_DELIVERY, true)
                                .put(Emailv31.Message.TEMPLATEERROR_REPORTING, new JSONObject()
                                        .put("Email", "thomas@ease.space")
                                        .put("Name", "Thomas"))));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

    public static void deleteTeamUserMail(String email, String first_name, String last_name, String team_name, JSONArray singleCards, JSONArray enterpriseCards) throws MailjetSocketTimeoutException, MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "contact@ease.space")
                                        .put("Name", "Ease.Space"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)))
                                .put(Emailv31.Message.TEMPLATEID, 180165)
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("single_cards", singleCards)
                                        .put("enterprise_cards", enterpriseCards)
                                        .put("first_name", first_name)
                                        .put("last_name", last_name)
                                        .put("team_name", team_name))
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.TEMPLATEERROR_DELIVERY, true)
                                .put(Emailv31.Message.TEMPLATEERROR_REPORTING, new JSONObject()
                                        .put("Email", "thomas@ease.space")
                                        .put("Name", "Thomas"))));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

}
