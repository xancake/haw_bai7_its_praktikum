package org.haw.its.praktikum3;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.haw.its.praktikum3.fileio.KeyFileReader;

public class ReceiveSecureFile {
	public static void main(String[] args) throws Exception {
		ParameterData parameter = ParameterData.fromCommandLine(args);
		if(parameter == null) {
			System.out.println("Usage: java ReceiveSecureFile <sender> <receiver> <fileToEncrypt> <encryptedOutputFile>");
			System.exit(1);
		}
		
		// a) Einlesen eines öffentlichen RSA‐Schlüssels aus einer Datei gemäß Aufgabenteil 1.
		// b) Einlesen eines privaten RSA‐Schlüssels aus einer Datei gemäß Aufgabenteil 1.
		KeyFileReader reader = new KeyFileReader("res/praktikum3");
		KeyFactory rsaGenerator = KeyFactory.getInstance("RSA");
		PublicKey publicKey   = rsaGenerator.generatePublic( new X509EncodedKeySpec( reader.readKeyFile(parameter._sender,   "pub")));
		PrivateKey privateKey = rsaGenerator.generatePrivate(new PKCS8EncodedKeySpec(reader.readKeyFile(parameter._receiver, "prv")));
		
		// TODO:
		// c) Einlesen einer .ssf‐Datei gemäß Aufgabenteil 2, Entschlüsselung des geheimen Schlüssels mit dem privaten
		// RSA‐Schlüssel, Entschlüsselung der Dateidaten mit dem geheimen Schlüssel (AES im Counter‐Mode) – mit
		// Anwendung der übermittelten algorithmischen Parameter – sowie Erzeugung einer Klartext‐Ausgabedatei.
		
		
		// TODO:
		// d) Überprüfung der Signatur für den geheimen Schlüssel aus c) mit dem öffentlichen RSA‐Schlüssel (Algorithmus: „SHA256withRSA“)
		
		
	}
}
