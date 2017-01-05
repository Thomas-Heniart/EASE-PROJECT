package com.Ease.Utils.Crypto;

public class TestClass {

	public static void main(String[] args) {
		/*String key = AES.keyGenerator(256);
		String cypher = AES.encrypt256("SALUT LES ENFANTS", key);
		String plain = AES.decrypt256(cypher, key);
		System.out.println("AES 256 : ");
		System.out.println("key : "+key);
		System.out.println("cypher : "+cypher);
		System.out.println("pain : " + plain);
		
		key = AES.keyGenerator(128);
		cypher = AES.encrypt128("SALUT LES ENFANTS", key);
		plain = AES.decrypt128(cypher, key);
		System.out.println("AES 128 : ");
		System.out.println("key : "+key);
		System.out.println("cypher : "+cypher);
		System.out.println("pain : " + plain);*/
		System.out.println(Hashing.hash("NFEU23AS"));
	}
}
