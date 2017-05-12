package org.haw.its.praktikum3;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Scanner;
import org.haw.its.praktikum3.fileio.KeyFileWriter;

public class RSAKeyCreator {
	public static void main(String[] args) throws Exception {
		String user = getUsernameFromArgs(args);
		
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(2048);
		KeyFactory x509  = KeyFactory.getInstance("X.509");
		KeyFactory pkcs8 = KeyFactory.getInstance("PKCS8");
		
		KeyPair keyPair = keyGenerator.generateKeyPair();
		Key publicKey   = x509.translateKey(keyPair.getPublic());
		Key privateKey  = pkcs8.translateKey(keyPair.getPrivate());
		
		// TODO: X.509-encode publicKey
		// TODO: PKCS8-encode privateKey
		
		KeyFileWriter keyFileWriter = new KeyFileWriter("res/praktikum3");
		keyFileWriter.writeKeyFile(user, publicKey.getEncoded(), "pub");
		keyFileWriter.writeKeyFile(user, privateKey.getEncoded(), "prv");
	}
	
	private static String getUsernameFromArgs(String[] args) {
		String user = null;
		switch(args.length) {
			case 0: {
				try(Scanner key = new Scanner(System.in)) {
    				System.out.print("Benutzername: ");
    				user = key.nextLine();
				}
				break;
			}
			case 1: {
				user = args[0];
				break;
			}
			default: {
				System.out.println("Usage: java RSAKeyCreator <username>");
				System.exit(1);
			}
		}
		return user;
	}
}
