package com.Ease.Mail;

import com.Ease.Context.Variables;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.ContactManagecontactslists;
import com.mailjet.client.resource.Contactdata;
import com.mailjet.client.resource.ContactslistManageContact;
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
                        .put("Value", user.getPersonalInformation().getFirst_name().equals("") ? StringUtils.capitalize(user.getUsername().toLowerCase()) : user.getPersonalInformation().getFirst_name()))));
    }

    public void updateUserContactLists(User user) throws MailjetSocketTimeoutException, MailjetException {
        JSONArray lists = new JSONArray();
        lists.put(new JSONObject()
                .put("ListId", "31729")
                .put("Action", "addnoforce"));
        if (!user.getTeamUsers().isEmpty()) {
            lists.put(new JSONObject()
                    .put("ListId", "33212")
                    .put("Action", "remove"));
            boolean must_add_owner_list = false;
            boolean must_add_team_list = false;
            for (TeamUser teamUser : user.getTeamUsers()) {
                if (teamUser.isTeamOwner())
                    must_add_owner_list = true;
                else
                    must_add_team_list = true;
            }
            lists.put(new JSONObject()
                    .put("ListId", "33211")
                    .put("Action", must_add_team_list ? "addnoforce" : "remove"));
            lists.put(new JSONObject()
                    .put("ListId", "33213")
                    .put("Action", must_add_owner_list ? "addnoforce" : "remove"));
        } else {
            lists.put(new JSONObject()
                    .put("ListId", "33211")
                    .put("Action", "remove"));
            lists.put(new JSONObject()
                    .put("ListId", "33213")
                    .put("Action", "remove"));
            lists.put(new JSONObject()
                    .put("ListId", "33212")
                    .put("Action", "addnoforce"));
        }
        mailjetResponse = mailjetClient.post(new MailjetRequest(ContactManagecontactslists.resource, user.getEmail())
                .property(ContactManagecontactslists.CONTACTSLISTS, lists));
    }

    public void updateUserEmail(String old_email, User user) throws MailjetSocketTimeoutException, MailjetException {
        this.deleteUserEmail(old_email);
        mailjetClient.post(new MailjetRequest(Contact.resource, user.getEmail()));
        this.updateUserData(user);
        this.updateUserContactLists(user);
    }

    public void deleteUserEmail(String email) throws MailjetSocketTimeoutException, MailjetException {
        mailjetClient.delete(new MailjetRequest(Contactdata.resource, email));
    }

    public void addToIesegList(String email) throws MailjetSocketTimeoutException, MailjetException {
        JSONArray lists = new JSONArray();
        lists.put(new JSONObject()
                .put("ListId", "34722")
                .put("Action", "addnoforce"));
        mailjetResponse = mailjetClient.post(new MailjetRequest(ContactManagecontactslists.resource, email)
                .property(ContactManagecontactslists.CONTACTSLISTS, lists));
    }

    public void addEmailToList(String email, Long listId) throws MailjetSocketTimeoutException, MailjetException {
        mailjetResponse = mailjetClient.post(new MailjetRequest(ContactslistManageContact.resource, listId).setBody(new JSONObject()
                .put("Email", email)
                .put("Action", "addnoforce")));
        System.out.println(mailjetResponse.getStatus());
        System.out.println(mailjetResponse.getData());
    }
}
