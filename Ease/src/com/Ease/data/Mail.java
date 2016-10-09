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
	
	public Mail () throws MessagingException {
		
		try {
			props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			Session msession = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("sergii@ease-app.co","xaYsgG4-");
				}
			});
			message = new MimeMessage(msession);
			message.setFrom(new InternetAddress("sergii@ease-app.co", "Ease Team"));
		} catch (UnsupportedEncodingException e) {
			throw new MessagingException();
		} 		
	}
	
	public void sendPasswordLostMail(String email, String code, String userName) throws MessagingException {
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Mot de passe perdu - Ease", "utf-8", null));
			String link = "https://ease.space/ResetUser?email=" + email + "&linkCode=" + code;
			message.setContent("<p>Bonjour " + userName + ",</p>"
					+ "<p></p>"
					+ "<p>Tu as perdu ton mot de passe ?</p>"
					+ "<p>Nous venons de recevoir ta demande de ré-initialisation. (Si tu n’as pas fait cette demande, tu peux ignorer cet e-mail.)</p>"
					+ "<p></p>"
					+ "<p>Tu es sur le point de réinitialiser ton mot de passe Ease, tes sites et tes profils resteront au même endroit, cependant, les codes confidentiels que tu avais sauvegardé dans ton espace personnel seront perdus pour des raisons de sécurité. Tu vas devoir simplement les rentrer à nouveau.</p>"
					+ "<p>Pour le choix de ton nouveau mot de passe Ease, un petit conseil : ne choisis pas le nom de ton chien ou 123456 ; un mot de passe complexe assure une meilleure sécurité de tes données. Le mieux est d’utiliser des lettres majuscules, minuscules et des chiffres.</p>"
					+ "<p style='margin: 0px;'>Pour ré-initialiser ton mot de passe, c’est par ici: <a href='"+link+"'>https://ease.space/...</a> (lien valide 2h)</p>"
					+ "<p></p>"
					+ "<p>À très vite !</p>"
					+ "<p>La team Ease</p>"
					+ "<p></p>"
					+ "<p style='margin: 0px;'>PS: Si tu souhaites plus de conseils sur la sécurité des mots de passe, tu peux consulter notre article: <a href='https://medium.com/@Ease./les-5-mots-de-passe-%C3%A0-%C3%A9viter-et-le-mot-de-passe-ultime-8c7b88285b19#.f2i1muio5'>« Les 5 mots de passe à éviter… et le mot de passe ultime ! »</a></p>"
					, "text/html;charset=utf-8");		
			Transport.send(message);
		} catch (AddressException e) {
			throw new MessagingException();
		} catch (UnsupportedEncodingException e) {
			throw new MessagingException();
		}
	}
}
