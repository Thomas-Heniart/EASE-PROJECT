package com.Ease.Mail;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.Context.Variables;
import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.sendgrid.ASM;
import com.sendgrid.Attachments;
import com.sendgrid.BccSettings;
import com.sendgrid.ClickTrackingSetting;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.FooterSetting;
import com.sendgrid.GoogleAnalyticsSetting;
import com.sendgrid.Mail;
import com.sendgrid.MailSettings;
import com.sendgrid.Method;
import com.sendgrid.OpenTrackingSetting;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.Setting;
import com.sendgrid.SpamCheckSetting;
import com.sendgrid.SubscriptionTrackingSetting;
import com.sendgrid.TrackingSettings;

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

	public void enableMailSettings() {
		mail.setIpPoolId("23");
		MailSettings mailSettings = new MailSettings();
		Setting sandBoxMode = new Setting();
		sandBoxMode.setEnable(true);
		mailSettings.setSandboxMode(sandBoxMode);
		Setting bypassListManagement = new Setting();
		bypassListManagement.setEnable(true);
		mailSettings.setBypassListManagement(bypassListManagement);
		SpamCheckSetting spamCheckSetting = new SpamCheckSetting();
		spamCheckSetting.setEnable(true);
		spamCheckSetting.setSpamThreshold(1);
		spamCheckSetting.setPostToUrl("https://spamcatcher.sendgrid.com");
		mailSettings.setSpamCheckSetting(spamCheckSetting);
		mail.setMailSettings(mailSettings);
	}

	public void enableMailTracking() {
		TrackingSettings trackingSettings = new TrackingSettings();
		ClickTrackingSetting clickTrackingSetting = new ClickTrackingSetting();
		clickTrackingSetting.setEnable(true);
		clickTrackingSetting.setEnableText(true);
		trackingSettings.setClickTrackingSetting(clickTrackingSetting);
		OpenTrackingSetting openTrackingSetting = new OpenTrackingSetting();
		openTrackingSetting.setEnable(true);
		openTrackingSetting
				.setSubstitutionTag("Optional tag to replace with the open image in the body of the message");
		trackingSettings.setOpenTrackingSetting(openTrackingSetting);
		SubscriptionTrackingSetting subscriptionTrackingSetting = new SubscriptionTrackingSetting();
		subscriptionTrackingSetting.setEnable(true);
		subscriptionTrackingSetting.setText("text to insert into the text/plain portion of the message");
		subscriptionTrackingSetting
				.setHtml("<html><body>html to insert into the text/html portion of the message</body></html>");
		subscriptionTrackingSetting
				.setSubstitutionTag("Optional tag to replace with the open image in the body of the message");
		trackingSettings.setSubscriptionTrackingSetting(subscriptionTrackingSetting);
		GoogleAnalyticsSetting googleAnalyticsSetting = new GoogleAnalyticsSetting();
		googleAnalyticsSetting.setEnable(true);
		googleAnalyticsSetting.setCampaignSource("some source");
		googleAnalyticsSetting.setCampaignTerm("some term");
		googleAnalyticsSetting.setCampaignContent("some content");
		googleAnalyticsSetting.setCampaignName("some name");
		googleAnalyticsSetting.setCampaignMedium("some medium");
		trackingSettings.setGoogleAnalyticsSetting(googleAnalyticsSetting);
		mail.setTrackingSettings(trackingSettings);
	}
	
	public void sendEmail() throws GeneralException {
		//enableMailSettings();
		//enableMailTracking();
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
	
	public void sendInvitationEmail(String userName, String userEmail, String code) throws GeneralException {
		mail.setTemplateId("ea9b5440-35b2-48e1-9366-f5fd48f61937");
		Personalization personalization = this.createNewPersonalization();
		this.addTo(personalization, userName, userEmail);
		personalization.addSubstitution("#link", Variables.URL_PATH + "discover?email=" + userEmail + "&name=" + userName + "&invitationCode=" + code);
		this.setReplyTo("Victor", "victor@ease.space");
		this.sendEmail();
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

	public void sendAppsArrivedEmail(String userName, String userEmail, List<String> websiteNames) throws GeneralException {
		mail.setTemplateId("cfe20be6-31c1-427f-b94c-02aacedd2619");
		Personalization personalization = this.createNewPersonalization();
		this.addTo(personalization, userName, userEmail);
		personalization.addSubstitution("#username", userName);
		String integratedWebsitesString = "";
		for (String websiteName : websiteNames) {
			integratedWebsitesString += websiteName;
			if (websiteNames.indexOf(websiteName) != (websiteNames.size() - 1))
				integratedWebsitesString += ", ";
		}
		personalization.addSubstitution("#appName", integratedWebsitesString);
		personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "?catalogOpen=true");
		this.sendEmail();
	}
	
	public void sendFailToIntegrateWebsitesEmail(String userName, String userEmail, List<String> websiteNames) throws GeneralException {
		mail.setTemplateId("2616932d-5d41-49df-9897-62d9aaeb9501");
		Personalization personalization = this.createNewPersonalization();
		this.addTo(personalization, userName, userEmail);
		personalization.addSubstitution("#username", userName);
		String integratedWebsitesString = "";
		for (String websiteName : websiteNames) {
			integratedWebsitesString += websiteName;
			if (websiteNames.indexOf(websiteName) != (websiteNames.size() - 1))
				integratedWebsitesString += ", ";
		}
		personalization.addSubstitution("#appName", integratedWebsitesString);
		personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "?catalogOpen=true");
		this.sendEmail();
	}
	
	public void sendAwesomeUserEmail(Website site, ServletManager sm) throws GeneralException {
		mail.setTemplateId("2f8b2828-6e6f-42bd-a568-3da1b83ed835");
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT firstName, email FROM users WHERE id IN (SELECT user_id FROM integrateWebsitesAndUsersMap WHERE website_id = ?);");
		request.setInt(site.getDb_id());
		DatabaseResult rs = request.get();
		while (rs.next()) {
			Personalization personalization = this.createNewPersonalization();
			String username = rs.getString(1);
			String userEmail = rs.getString(2);
			this.addTo(personalization, username, userEmail);
			personalization.addSubstitution("#username", username);
			personalization.addSubstitution("#appName", site.getName());
		}
		this.sendEmail();
		request = db.prepareRequest("DELETE FROM integrateWebsitesAndUsersMap WHERE website_id = ?;");
		request.setInt(site.getDb_id());
		request.set();
	}
	
	public void sendPasswordLostEmail(String userEmail, String username, String code) throws GeneralException {
		mail.setTemplateId("815afa39-6d33-4dfc-a95b-e931007f6d98");
		Personalization personalization = this.createNewPersonalization();
		this.addTo(personalization, username, userEmail);
		personalization.addSubstitution("#linkUrl", Variables.URL_PATH + "resetPassword?email=" + userEmail + "&code=" + code);
		this.sendEmail();
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

    public void sendCreateTeamEmail(String firstName, String email, String code) throws GeneralException {
		mail.setTemplateId("8ee79c52-b2bc-422e-b401-54a89de8f37b");
		Personalization personalization = this.createNewPersonalization();
		this.addTo(personalization, firstName, email);
		personalization.addSubstitution("#link", Variables.URL_PATH + "CreateTeam?email=" + email + "&code=" + code);
		personalization.addSubstitution("#firstName", firstName);
		this.sendEmail();
	}

	public void sendInvitationToJoinTeamEmail(String teamName, String adminName, String firstName, String email, String code) throws GeneralException {
        mail.setTemplateId("04a2b7b7-87db-4b12-8a2c-792359585c1b");
        Personalization personalization = this.createNewPersonalization();
        this.addTo(personalization, firstName, email);
        personalization.addSubstitution("#link", Variables.URL_PATH + "CreateTeamUser?email=" + email + "&code=" + code);
        personalization.addSubstitution("#adminName", adminName);
        personalization.addSubstitution("#teamName", teamName);
        personalization.addSubstitution("#firstName", firstName);
        this.sendEmail();
    }

    public void sendJoinChannelEmail(String teamName, String channelName, Map<String, String> administratorsUsernameAndEmail, String username, String email, String code) throws GeneralException {
		mail.setTemplateId("6edb3495-0d9e-4e35-a3d6-a8c3d2c1c222");
		for (Map.Entry<String, String> entry : administratorsUsernameAndEmail.entrySet()) {
			Personalization personalization = this.createNewPersonalization();
			this.addTo(personalization, entry.getKey(), entry.getValue());
			personalization.addSubstitution("#link", Variables.URL_PATH + "ConfirmJoinChannel?email=" + email + "&code=" + code);
			personalization.addSubstitution("#adminName", entry.getKey());
			personalization.addSubstitution("#teamName", teamName);
            personalization.addSubstitution("#channelName", channelName);
			personalization.addSubstitution("#username", username);
		}
		this.sendEmail();
    }

    public void sendJoinTeamEmail(String teamName, Map<String, String> administratorsUsernameAndEmail, String username, String email, String code) throws GeneralException {
        mail.setTemplateId("e501354e-3321-48c9-9e5a-bebb7ce59df6");
        for (Map.Entry<String, String> entry : administratorsUsernameAndEmail.entrySet()) {
            Personalization personalization = this.createNewPersonalization();
            this.addTo(personalization, entry.getKey(), entry.getValue());
            personalization.addSubstitution("#link", Variables.URL_PATH + "ConfirmJoinTeam?email=" + email + "&code=" + code);
            personalization.addSubstitution("#adminName", entry.getKey());
            personalization.addSubstitution("#teamName", teamName);
            personalization.addSubstitution("#username", username);
        }
        this.sendEmail();
    }
}
