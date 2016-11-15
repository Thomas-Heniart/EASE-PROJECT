package come.Ease.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class MailThread extends Thread {
	private InternetAddress from;
	private String toEmail;
	private Session msession;
	private String invitationCode;

	public MailThread(String name, InternetAddress from, String toEmail, String invitationCode, Session msession) {
		super(name);
		this.from = from;
		this.toEmail = toEmail;
		this.invitationCode = invitationCode;
		this.msession = msession;
	}
	
	public void run() {
		try {
			MimeMessage message = new MimeMessage(msession);
			String link = "https://ease.space/ieseg?email=" + toEmail + "&invitationCode=" + invitationCode;
			message.setFrom(from);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(MimeUtility.encodeText("Your new IESEG passwords", "utf-8", null));
			message.setContent("<div style='color: black;'><p>***English version below***<br /></p>"
					+ "<p>Bonjour, <br /></p>"
					+ "<p>L' IÉSEG utilise maintenant un outil permettant d’accéder aux services web de l'école sans mot de passe, il s’agit d’Ease. Cette plateforme vous connecte automatiquement, en 1 clic, à votre calendrier personnel, Ieseg-online, Ieseg mail, Unify et autres sites que vous souhaiteriez ajouter (outils, réseaux sociaux, e-learnings etc).<br /></p>"
					+ "<p>Le service informatique a déjà pré-créé votre compte avec votre email IÉSEG, pour le finaliser veuillez cliquer sur votre lien personnel: <a href='" + link + "' target='_blank'>https://ease.space/ieseg</a><br /></p>"
					+ "<p>Vous pouvez maintenant accéder à Ease depuis n'importe quel ordinateur ainsi qu'en page d’accueil de tous les ordinateurs des deux campus.<br /></p>"
					+ "<p>Bien cordialement,<br /></p>"
					+ "<p>*************************<br /></p>"
					+ "<p>Dear students and staff,<br /></p>"
					+ "<p>IÉSEG is now using the tool EASE in order to access IÉSEG web services without password concerns. This is a web platform which connects you automatically in one click to your personal Calendar, Ieseg-online, Ieseg mail, Unify and other websites you would like to add (tools, social networks, e-learnings etc).<br /></p>"
					+ "<p>The IT department already pre-created your account with your IÉSEG email. To finalize it, please click on your personal link: <a href='" + link + "' target='_blank'>https://ease.space/ieseg</a><br /></p>"
					+ "<p>You will now be able to access Ease from anywhere as well as from the homepage of the computers on both campuses.<br /></p>"
					+ "<p>Best regards,<br /></p>"
					+ "<p>IÉSEG Communication<br /></p>"
					+ "</div>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (MessagingException e) {
			this.run();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
