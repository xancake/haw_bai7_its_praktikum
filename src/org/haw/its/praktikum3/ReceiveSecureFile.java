package org.haw.its.praktikum3;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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
		PublicKey  publicKey  = rsaGenerator.generatePublic( new X509EncodedKeySpec( reader.readKeyFile(parameter._sender,   "pub")));
		PrivateKey privateKey = rsaGenerator.generatePrivate(new PKCS8EncodedKeySpec(reader.readKeyFile(parameter._receiver, "prv")));
		
		// c) Einlesen einer .ssf‐Datei gemäß Aufgabenteil 2, Entschlüsselung des geheimen Schlüssels mit dem privaten
		// RSA‐Schlüssel, Entschlüsselung der Dateidaten mit dem geheimen Schlüssel (AES im Counter‐Mode) – mit
		// Anwendung der übermittelten algorithmischen Parameter – sowie Erzeugung einer Klartext‐Ausgabedatei.
		try(
				DataInputStream is = new DataInputStream(new FileInputStream(new File("res/praktikum3", parameter._inputFile)));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(new File("res/praktikum3", parameter._outputFile)));
		) {
			int encryptedAesKeyLength = is.readInt();
			byte[] encryptedAesKey = new byte[encryptedAesKeyLength];
			is.read(encryptedAesKey);
			
			int aesKeySignatureLength = is.readInt();
			byte[] aesKeySignature = new byte[aesKeySignatureLength];
			is.read(aesKeySignature);
			
			int aesParametersLength = is.readInt();
			byte[] aesParameters = new byte[aesParametersLength];
			is.read(aesParameters);
			
			// Entschlüsselung des geheimen Schlüssels mit dem privaten RSA‐Schlüssel
			Cipher rsaCipher = Cipher.getInstance("RSA");
			rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
			
			// d) Überprüfung der Signatur für den geheimen Schlüssel aus c) mit dem öffentlichen RSA‐Schlüssel (Algorithmus: „SHA256withRSA“)
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initVerify(publicKey);
			signature.update(aesKeyBytes);
			if(!signature.verify(aesKeySignature)) {
				throw new IllegalArgumentException("Die Signatur für den AES-Schlüssel ist ungültig!");
			}

			// Entschlüsselung der Dateidaten mit dem geheimen Schlüssel (AES im Counter‐Mode) – mit Anwendung der
			// übermittelten algorithmischen Parameter – sowie Erzeugung einer Klartext‐Ausgabedatei
			SecretKeySpec aesKey = new SecretKeySpec(aesKeyBytes, "AES");
			AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
			aesParams.init(aesParameters);
			Cipher aesCipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
			aesCipher.init(Cipher.DECRYPT_MODE, aesKey, aesParams);
			
			byte[] buffer = new byte[8];
			while(is.read(buffer) != -1) {
				os.write(aesCipher.update(buffer));
			}
			os.write(aesCipher.doFinal());
		}
	}
}
