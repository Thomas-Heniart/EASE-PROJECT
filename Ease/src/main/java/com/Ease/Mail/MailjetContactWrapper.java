package com.Ease.Mail;

import com.Ease.Context.Variables;
import com.Ease.User.User;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.ContactManagecontactslists;
import com.mailjet.client.resource.Contactdata;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class MailjetContactWrapper {

    private MailjetClient mailjetClient;
    private MailjetResponse mailjetResponse;
    private MailjetRequest mailjetRequest;

    public MailjetContactWrapper() {
        mailjetClient = new MailjetClient(Variables.MJ_APIKEY_PUBLIC, Variables.MJ_APIKEY_PRIVATE);
    }

    public void updateUserData(User user) throws MailjetSocketTimeoutException, MailjetException {
        mailjetClient.put(new MailjetRequest(Contactdata.resource, user.getEmail()).property("Data", new JSONArray()
                .put(new JSONObject()
                        .put("Name", "prénom")
                        .put("Value", StringUtils.capitalize(user.getUsername().toLowerCase())))));
    }

    public void updateUserContactLists(User user) throws MailjetSocketTimeoutException, MailjetException {
        JSONArray lists = new JSONArray();
        lists.put(new JSONObject()
                .put("ListId", "31729")
                .put("Action", "addnoforce"));
        mailjetResponse = mailjetClient.post(new MailjetRequest(ContactManagecontactslists.resource, user.getEmail())
                .property(ContactManagecontactslists.CONTACTSLISTS, lists));
    }

    public void updateUserEmail(String old_email, User user) throws MailjetSocketTimeoutException, MailjetException {
        mailjetClient.delete(new MailjetRequest(Contactdata.resource, old_email));
        mailjetClient.post(new MailjetRequest(Contact.resource, user.getEmail()));
        this.updateUserData(user);
        this.updateUserContactLists(user);
    }
}
