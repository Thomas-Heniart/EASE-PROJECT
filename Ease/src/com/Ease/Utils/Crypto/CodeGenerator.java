package com.Ease.Utils.Crypto;

import java.util.Random;

public class CodeGenerator {
	public static String generateNewCode() {
		String			codeGenerated = "";
		for (int i = 0;i < 126 ; ++i) {
			codeGenerated += alphabet.charAt(r.nextInt(alphabet.length()));			
		}
		return codeGenerated;
	}

	public static String generateDigits(int length) {
		String digits = "";
		for (int i=0; i < length; i++)
			digits += digits.charAt(r.nextInt(digits.length()));
		return digits;
	}

	private final static Random r = new Random();
	private final static String alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
	private final static String digits = "0123456789";
	
}
