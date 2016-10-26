package com.Ease.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern VALID_EASE_PASSWORD_REGEX = 
		    Pattern.compile("^[-A-Za-z0-9$@$!%.*#?&'()\\[\\]{}_<>=+\\/:;,]{8,}$");
	public static final Pattern VALID_COLOR_REGEX = 
		    Pattern.compile("^#[0-9a-fA-F]{6}$");
	public static final Pattern VALID_URL_REGEX = 
		    Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);
	
	public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
	}
	
	public static boolean isPassword(String passStr) {
        Matcher matcher = VALID_EASE_PASSWORD_REGEX .matcher(passStr);
        return matcher.find();
	}
	
	public static boolean isColor(String colorStr) {
        Matcher matcher = VALID_COLOR_REGEX .matcher(colorStr);
        return matcher.find();
	}
	public static boolean isUrl(String urlStr) {
		 Matcher matcher = VALID_URL_REGEX .matcher(urlStr);
	        return matcher.find();
	}
}
