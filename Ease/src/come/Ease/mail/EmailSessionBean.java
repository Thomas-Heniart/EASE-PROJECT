package come.Ease.mail;

public class EmailSessionBean {
	private int port = 465;
	private String host = "ease.space";
	private String from = "matt@exemple.com";
	private boolean auth = true;
	private String username = "matt@exemple.com";
	private String passord = "secretpw";
	private Protocol protocol = Protocol.SMTP;
	private boolean debug = true;
	
	public void sendEmail(String to, String subject, String body) {
		
	}
}
