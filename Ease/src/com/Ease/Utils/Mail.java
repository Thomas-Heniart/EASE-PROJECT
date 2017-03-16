package com.Ease.Utils;

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

import com.Ease.Context.Variables;
import com.Ease.Dashboard.User.User;

public class Mail {

	protected Properties props;
	protected MimeMessage message;
	protected Session msession;
	
	public Mail() throws MessagingException {
		try {
			props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			msession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("benjamin@ease.space", "bpease.P2211");
				}
			});
			message = new MimeMessage(msession);
			message.setFrom(new InternetAddress("benjamin@ease.space", "Ease Team"));
		} catch (UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
	
	public Mail(String sender, String password, String who) throws GeneralException {
		try {
			props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			msession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sender, password);
				}
			});
			message = new MimeMessage(msession);
			message.setFrom(new InternetAddress(sender, who));
		} catch (UnsupportedEncodingException | MessagingException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	/*public void testEmail(String email) throws GeneralException {
		try {
			InternetAddress[] emails = InternetAddress.parse(email);
			for(int i=0; i < emails.length; i++) {
				try {
					emails[i].validate();
				} catch()
				
			}
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Test", "utf-8", null));
			message.setContent(
					"<p>Test email</p>",
					"text/html;charset=utf-8");
			
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.ClientError, e);
		} catch (MessagingException e) {
			throw new GeneralException(ServletManager.Code.ClientError, e);
		}
	}*/
	
	public void sendPasswordLostMail(String email, String code) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Mot de passe perdu - Ease", "utf-8", null));
			String link = Variables.URL_PATH + "newPassword.jsp?email=" + email + "&linkCode=" + code;
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
			String link = Variables.URL_PATH + "thefamily?email=" + email + "&invitationCode=" + invitationCode;
			//String link = "http://localhost:8080/thefamily?email=" + email + "&invitationCode=" + invitationCode;
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

	public void sendVerificationMainEmail(String email, String code) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Confirm your email address", "utf-8", null));
			String link = Variables.URL_PATH + "VerifieEmail?email=" + email + "&code=" + code;
			message.setContent("<p>*French version below*</p>" + "<p></p>" + "<p>Hello & welcome to Ease !</p>"
					+ "<p></p>"
					+ "<p>Ease.space gathers the websites on which you have an account and allows you to login & logout to them in 1 click. All this from a secured personal homepage on your browser. For now, it works on Chrome and Safari.</p>"
					+ "<p></p>"
					+ "<p>This means that once you have added the credentials of an account on your space, you’ll never have to use them again, regardless of the computer or device you have in front of you. The only thing you need is Internet.</p>"
					+ "<p></p>" + "<p>To confirm your email, click to the link (valid only 3 days) : <a href='"
					+ link + "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>See you soon !</p>" + "<p></p>"
					+ "<p>The Ease team</p>" + "<p></p>" + "<p></p>" + "<hr>" + "<p></p>"
					+ "<p>Hello & bienvenue sur Ease.space !</p>" + "<p></p>"
					+ "<p>Ease est ton espace personnel intelligent et sécurisé qui regroupe l'ensemble des sites sur lesquels tu as un compte, et qui t’y connecte et déconnecte automatiquement! Ease fonctionne sur Chrome et Safari.</p>"
					+ "<p></p>"
					+ "<p>Autrement dit, une fois tes identifiants enregistrés sur ton espace Ease, tu n’auras plus jamais à les utiliser. Et ce, où que tu sois, quelque soit l’ordinateur et en toute sécurité. Seul pré-requis: avoir internet !</p>"
					+ "<p></p>"
					+ "<p>Pour confirmer ton email, clique sur le lien suivant (valide seulement 3 jours) : <a href='" + link
					+ "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>A bientôt !</p>" + "<p></p>"
					+ "<p>La team Ease</p>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
	
	public void sendVerificationEmail(String newEmail, String userName, String code) throws MessagingException {
		try {
			String link = Variables.URL_PATH + "VerifieEmail?email=" + newEmail + "&code=" + code;

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(newEmail));
			message.setSubject(MimeUtility.encodeText("Validation email !", "utf-8", null));
			message.setContent("<div style='color: black;'><p>Hello !<br /></p>"
					+ "<p>A validation email has been asked by " + userName + ". "
					+ "<br /><br />To validate this new email in order to receive updates, click on the link <a href='" + link
					+ "'>here</a>.<br></p>"
					+ "<p>(If you have not asked for a validation on \"<span style='text-decoration: underline'>ease.space</span>\", you can ignore this email.)</p>"
					+ "<p>See you soon !</p>" + "<p>The Ease team</p>" + "</div>", "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
	
	public void sendIntegratedWebsitesMail(String email, String[] websites, String userName) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("We added what you asked for !", "utf-8", null));
			String link = Variables.URL_PATH + "index.jsp?openCatalog=true";
			boolean multiple = websites.length>1;
			String wholeMessage = "<p>Hello"+ ((userName!=null)? " "+userName : "") +", we hope everything is going smoothly on your Ease platform !</p>" + "<p></p>"
					+ "<p>You recently asked us to integrate ";
			if(multiple){
				for(int i=0;i<websites.length-1;i++){
					if(i!=0)
						wholeMessage +=", ";
					wholeMessage += websites[i];
				}
				wholeMessage += " and "+websites[websites.length-1];
			} else {
				wholeMessage += websites[0];
			}
			wholeMessage += " to our catalog. Here is a little message to let you know that " + ((multiple)? "they have":"it has")+" arrived on Ease just for you !! <a href='" + link + "'>Check it out now ;)</a>"
					+ "<p></p>"
					+ "<p>Thanks to you, other users can add " + ((multiple)? "them":"it") + " to their own platform. To add it to your own now, <a href='" + link + "'>click here !</a></p>"
					+ "<p></p>"
					+ "<p>Do not hesitate to contact us if you find anything we could improve on Ease.</p>"
					+ "<p></p>"
					+ "<p>Take care, talk to you soon. :)</p>"
					+ "<p>The Ease team</p>";
			
			message.setContent(wholeMessage,"text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void sendInvitationEmail(String email, String name, String invitationCode) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Active ton compte Ease !", "utf-8", null));
			//String link = "https://ease.space/discover?email=" + email + "&name=" + name + "&invitationCode=" + invitationCode;
			String link = Variables.URL_PATH + "discover?email=" + email + "&name=" + name + "&invitationCode=" + invitationCode;
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
	
	public void sendContactEmail(String email, User user, boolean isUser, String emailBy, String msg) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Contact ease.space", "utf-8", null));
			message.setContent("<p>Vous avez reçu un message depuis 'ContactUs': </p>"
					+ "<p>Est un utilisateur: " + ((isUser) ? "OUI" : "NON") + "</p>"
					+ "<p>Utilisateur connecté: " + ((user != null) ? user.getEmail() : "NON") + "</p>"
					+ "<p>Email entré: " + emailBy + "</p>"
					+ "<p></p>"
					+ "<p>" + msg + "</p>"
					, "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
	
	public void sendValidationContactEmail(String email, String msg) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Message envoyé à l’équipe Ease", "utf-8", null));
			message.setContent("<p>Bonjour,</p>" 
					+ "<p>Merci pour votre message. Nous allons faire en sorte de revenir vers vous dans les meilleurs délais.</p>"
					+ "<p>Voici une copie du message que vous nous avez fait parvenir:</p>"
					+ "<p></p>"
					+ "<p>" + msg + "</p>"
					, "text/html;charset=utf-8");
			Transport.send(message);
		} catch (AddressException | UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}

	public void sendFriendInvitationEmail(String email, String friendName, User user, String invitationCode) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Active ton compte Ease !", "utf-8", null));
			//String link = "https://ease.space/discover?email=" + email + "&name=" + name + "&invitationCode=" + invitationCode;
			String link = Variables.URL_PATH + "discover?email=" + email + "&name=" + friendName + "&invitationCode=" + invitationCode;
			message.setContent("<p>*French version below*</p>" + "<p></p>" + "<p>Hello & welcome to Ease !</p>"
					+ "<p></p>"
					+ "<p>Ease.space gathers the websites on which you have an account and allows you to login & logout to them in 1 click. All this from a secured personal homepage on your browser. For now, it works on Chrome and Safari.</p>"
					+ "<p></p>"
					+ "<p>This means that once you have added the credentials of an account on your space, you’ll never have to use them again, regardless of the computer or device you have in front of you. The only thing you need is Internet.</p>"
					+ "<p></p>" + "<p>To activate your space : click on the link and follow the steps : <a href='"
					+ link + "'>https://ease.space/...</a></p>" + "<p></p>" + "<p>See you soon !</p>" + "<p></p>"
					+ "<p>The Ease team</p>" + "<p></p>" + "<p></p>" + "<hr>" + "<p></p>"
					+ "<p>Hello " + friendName + " & bienvenue sur Ease.space !</p>" + "<p></p>"
					+ "<p>Vous avez été invité par " + user.getFirstName() + "(" + user.getEmail() + ")</p>" + "<p></p>"
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
}
