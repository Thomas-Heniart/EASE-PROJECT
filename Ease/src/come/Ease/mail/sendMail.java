package come.Ease.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.simplejavamail.*;
import org.simplejavamail.email.Email;

public class sendMail {
	public sendMail(){
		
	}
	
	void sendMail(){
		Email email = new Email();
		
		email.setFromAddress("Michel Druqer", "fisun.serge76@gmail.com");
		email.addRecipient("dad", "victor@ease-app.co", RecipientType.TO);
		email.setSubject("My Bakery is finally open!");
		email.setText("Mom, Dad. We did the opening ceremony of our bakery!!!");
		
		
	}
}