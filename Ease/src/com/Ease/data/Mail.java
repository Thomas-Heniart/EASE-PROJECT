package com.Ease.data;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class Mail {

	protected Properties props;
	protected MimeMessage message;

	public Mail() throws MessagingException {
		try {
			props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			Session msession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("benjamin@ease-app.co", "bpease.P2211");
				}
			});
			message = new MimeMessage(msession);
			message.setFrom(new InternetAddress("benjamin@ease-app.co", "Ease Team"));
		} catch (UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void sendPasswordLostMail(String email, String code, String userName) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Mot de passe perdu - Ease", "utf-8", null));
			String link = "https://ease.space/ResetUser?email=" + email + "&linkCode=" + code;
			message.setContent(
					"<p>*French Version Below*</p>" + "<p></p>" + "<p>Hello,</p>" + "<p></p>"
							+ "<p>You have just asked for a password renewal. If you didn’t, you can ignore this email.</p>"
							+ "<p></p>"
							+ "<p>By continuing this process, your websites and profiles will be kept, however, for security purposes, the passwords that you had saved on your space will be lost. You will have to add them again.</p>"
							+ "<p></p>" + "<p>To renew your Ease password, click here: <a href='" + link
							+ "'>https://ease.space/...</a>. (valid link for 2h)</p>" + "<p></p>"
							+ "<p>A little advice to choose your new Ease password : don’t take your dog’s name or 123456 ; a complex password ensures a higher level of online security.</p>"
							+ "<p></p>" + "<p>See you soon !</p>" + "<p></p>" + "<p>The Ease team</p>" + "<p></p>"
							+ "<p></p>" + "<hr>" + "<p></p>" + "<p>Hello,</p>" + "<p></p>"
							+ "<p>Nous venons de recevoir votre demande de ré-initialisation. Si vous n'en avez pas faite, vous pouvez ignorer cet e-mail.</p>"
							+ "<p></p>"
							+ "<p>En continuant le processus, vos sites intégrés et vos profiles seront gardés, mais les mots de passe de vos comptes seront perdus définitivement. Vous devrez donc les ajouter à nouveau. </p>"
							+ "<p></p>" + "<p>Pour ré-initialiser ton mot de passe, c’est par ici: <a href='" + link
							+ "'>https://ease.space/...</a>. (lien valide 2h)</p>" + "<p></p>"
							+ "<p>Pour le choix de ton nouveau mot de passe Ease, un petit conseil : ne choisis pas le nom de ton chien ou 123456 ; un mot de passe complexe assure une meilleure sécurité de tes données.</p>"
							+ "<p></p>" + "<p>A très vite !</p>" + "<p></p>" + "<p>La Team Ease</p>",
					"text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void sendGameEmail(String email) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Mr. Ammeux got one, time to get yours", "utf-8", null));
			String link = "https://goo.gl/forms/TBTvUDfwpoelcmV22";
			message.setContent("<p>Hello dear Ease user!</p>" + "<p></p>"
					+ "<p>We would like to thank you for being in one of the first 1000 users of Ease! </p>" + "<p></p>"
					+ "<p>To celebrate we offer you a chance to win your Ease sweat-shirt (the one like JP Ammeux 😉. You must invite 3 IESEG friends to join!</p>"
					+ "<p>Click here to get your chance: <a href='" + link + "'>ease.space/...</a></p>" + "<p></p>"
					+ "<p>See you soon,</p>" + "<p></p>" + "<p>The Ease Team</p>", "text/html;charset=utf-8");
			Transport.send(message);

		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void sendVerificationEmail(String email, String link) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Validation email !", "utf-8", null));
			message.setContent("<div style='color: black;'><p>Hello !<br /></p>"
					+ "<p>To validate your email in order to receive updates, click on the link <a href='" + link
					+ "'>here</a>.</p>"
					+ "<p>If you have not asked for a validation on <a href='https://ease.space'>https://ease.space</a>, you can ignore this email.</p>"
					+ "<p>See you soon !</p>" + "<p>The Ease team</p>" + "</div>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void sendTheFamilyInvitation(String email, String invitationCode) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Active ton compte Ease !", "utf-8", null));
			String link = "https://ease.space/thefamily?email=" + email + "&invitationCode=" + invitationCode;
			message.setContent("<p>*French version below*</p>" + "<p></p>" + "<p>Hello & welcome to Ease !</p>"
					+ "<p></p>"
					+ "<p>Ease.space gathers the websites on which you have an account and allows you to login & logout to them in 1 click. All this from a secured personal homepage on your browser. For now, it works on Chrome and Safari.</p>"
					+ "<p></p>"
					+ "<p>This means that once you have added the credentials of an account on your space, you’ll never have to use them again, regardless of the computer or device you have in front of you. The only thing you need is Internet.</p>"
					+ "<p></p>" + "<p>To activate your space : click on the link and follow the steps : <a href='"
					+ link + "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>See you soon !</p>" + "<p></p>"
					+ "<p>The Ease team</p>" + "<p></p>" + "<p></p>" + "<hr>" + "<p></p>"
					+ "<p>Hello & bienvenue sur Ease.space !</p>" + "<p></p>"
					+ "<p>Ease est ton espace personnel intelligent et sécurisé qui regroupe l'ensemble des sites sur lesquels tu as un compte, et qui t’y connecte et déconnecte automatiquement! Ease fonctionne sur Chrome et Safari.</p>"
					+ "<p></p>"
					+ "<p>Autrement dit, une fois tes identifiants enregistrés sur ton espace Ease, tu n’auras plus jamais à les utiliser. Et ce, où que tu sois, quelque soit l’ordinateur et en toute sécurité. Seul pré-requis: avoir internet !</p>"
					+ "<p></p>"
					+ "<p>Pour activer ton espace, clique sur le lien suivant et laisse toi guider: <a href='" + link
					+ "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>A bientôt !</p>" + "<p></p>"
					+ "<p>La team Ease</p>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void getEmailLinkMail(String email, String invitationCode) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Active ton compte Ease !", "utf-8", null));
			String link = "https://ease.space/ieseg?email=" + email + "&invitationCode=" + invitationCode;
			message.setContent("<p>*French version below*</p>" + "<p></p>" + "<p>Hello & welcome to Ease !</p>"
					+ "<p></p>"
					+ "<p>Ease.space gathers the websites on which you have an account and allows you to login & logout to them in 1 click. All this from a secured personal homepage on your browser. For now, it works on Chrome and Safari.</p>"
					+ "<p></p>"
					+ "<p>This means that once you have added the credentials of an account on your space, you’ll never have to use them again, regardless of the computer or device you have in front of you. The only thing you need is Internet.</p>"
					+ "<p></p>" + "<p>To activate your space : click on the link and follow the steps : <a href='"
					+ link + "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>See you soon !</p>" + "<p></p>"
					+ "<p>The Ease team</p>" + "<p></p>" + "<p></p>" + "<hr>" + "<p></p>"
					+ "<p>Hello & bienvenue sur Ease.space !</p>" + "<p></p>"
					+ "<p>Ease est ton espace personnel intelligent et sécurisé qui regroupe l'ensemble des sites sur lesquels tu as un compte, et qui t’y connecte et déconnecte automatiquement! Ease fonctionne sur Chrome et Safari.</p>"
					+ "<p></p>"
					+ "<p>Autrement dit, une fois tes identifiants enregistrés sur ton espace Ease, tu n’auras plus jamais à les utiliser. Et ce, où que tu sois, quelque soit l’ordinateur et en toute sécurité. Seul pré-requis: avoir internet !</p>"
					+ "<p></p>"
					+ "<p>Pour activer ton espace, clique sur le lien suivant et laisse toi guider: <a href='" + link
					+ "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>A bientôt !</p>" + "<p></p>"
					+ "<p>La team Ease</p>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
	
	public void sendVerificationEmail(String newEmail, String askingEmail, String link) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(newEmail));
			message.setSubject(MimeUtility.encodeText("Validation email !", "utf-8", null));
			message.setContent("<div style='color: black;'><p>Hello !<br /></p>"
					+ "<p>A validation email has been asked by " + askingEmail + ". "
					+ "<br /><br />To validate this new email in order to receive updates, click on the link <a href='" + link
					+ "'>here</a>.<br></p>"
					+ "<p>(If you have not asked for a validation on \"<span style='text-decoration: underline'>ease.space</span>\", you can ignore this email.)</p>"
					+ "<p>See you soon !</p>" + "<p>The Ease team</p>" + "</div>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
}
