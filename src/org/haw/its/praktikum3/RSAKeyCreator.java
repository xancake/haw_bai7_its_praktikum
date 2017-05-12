package org.haw.its.praktikum3;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import org.haw.its.praktikum3.fileio.KeyFileWriter;

public class RSAKeyCreator {
	public static void main(String[] args) throws Exception {
		String user = getUsernameFromArgs(args);
		
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(2048);
		
		KeyPair keyPair = keyGenerator.generateKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		// Keys sind standardmäßig in den Formaten X.509 bzw. PKCS8
		
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
