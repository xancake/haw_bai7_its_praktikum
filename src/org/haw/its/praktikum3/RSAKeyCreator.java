package org.haw.its.praktikum3;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAKeyCreator {
	public static void main(String[] args) throws Exception {
		String user = "lars";
		
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(2048);
		KeyFactory x509  = KeyFactory.getInstance("X.509");
		KeyFactory pkcs8 = KeyFactory.getInstance("PKCS8");
		
		KeyPair keyPair = keyGenerator.generateKeyPair();
		Key publicKey   = x509.translateKey(keyPair.getPublic());
		Key privateKey  = pkcs8.translateKey(keyPair.getPrivate());
		
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream("res/praktikum3/" + user + ".pub.txt"))) {
			os.writeInt(user.length());
			os.write(user.getBytes());
			os.writeInt(publicKey.getEncoded().length);
			os.write(publicKey.getEncoded());
		}
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream("res/praktikum3/" + user + ".prv.txt"))) {
			os.write(user.length());
			os.write(user.getBytes());
			os.write(privateKey.getEncoded().length);
			os.write(privateKey.getEncoded());
		}
	}
}
