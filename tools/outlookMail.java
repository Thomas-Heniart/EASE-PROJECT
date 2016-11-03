public static void sendEmail2()
			throws UnsupportedEncodingException, MessagingException {
		//String link = "https://localhost:8080/HelloWorld/AddEmail?email=" + newEmail + "&code=" + verificationCode;
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		/*Session msession = Session.getInstance(props);*/
		Session msession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("benjamin.prigent@ieseg.fr", "bw9m1xo");
			}
		});
		MimeMessage message = new MimeMessage(msession);
		message.setFrom(new InternetAddress("benjamin.prigent@ieseg.fr", "Ieseg Communication"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("heniart.thomas@gmail.com"));
		message.setSubject(MimeUtility.encodeText("Validation email !", "utf-8", null));
		message.setContent("<div style='color: black;'><p>Hello !<br /></p>", "text/html;charset=utf-8");
		Transport.send(message);
	}
