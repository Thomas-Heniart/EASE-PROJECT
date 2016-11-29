package com.Ease.utils;

import java.util.Random;

public class CodeGenerator {
	public static String generateNewCode() {
		String			codeGenerated = "";
		Random r = new Random();
		for (int i = 0;i < 126 ; ++i) {
			codeGenerated += alphabet.charAt(r.nextInt(alphabet.length()));			
		}
		return codeGenerated;
	}
	
	private final static String alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
	
}
