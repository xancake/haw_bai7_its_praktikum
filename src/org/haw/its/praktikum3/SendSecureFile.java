package org.haw.its.praktikum3;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.haw.its.praktikum3.fileio.KeyFileReader;
import org.haw.its.util.ByteUtils;

public class SendSecureFile {
	public static void main(String[] args) throws Exception {
		ParameterData parameter = ParameterData.fromCommandLine(args);
		if(parameter == null) {
			System.out.println("Usage: java SendSecureFile <sender> <receiver> <fileToEncrypt> <encryptedOutputFile>");
			System.exit(1);
		}
		
		// a) Einlesen eines privaten RSA‐Schlüssels (.prv) aus einer Datei gemäß Aufgabenteil 1.
		// b) Einlesen eines öffentlichen RSA‐Schlüssels (.pub) aus einer Datei gemäß Aufgabenteil 1.
		KeyFileReader reader = new KeyFileReader("res/praktikum3");
		KeyFactory rsaGenerator = KeyFactory.getInstance("RSA");
		PublicKey publicKey   = rsaGenerator.generatePublic( new X509EncodedKeySpec( reader.readKeyFile(parameter._receiver, "pub")));
		PrivateKey privateKey = rsaGenerator.generatePrivate(new PKCS8EncodedKeySpec(reader.readKeyFile(parameter._sender,   "prv")));
		
		// c) Erzeugen eines geheimen Schlüssels für den AES‐Algorithmus mit der Schlüssellänge 128 Bit
		KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
		aesGenerator.init(128);
		SecretKey aesKey = aesGenerator.generateKey();
		
		// d) Erzeugung einer Signatur für den geheimen Schlüssel aus c) mit dem privaten RSA‐Schlüssel (Algorithmus: „SHA256withRSA“)
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(aesKey.getEncoded());
		byte[] aesKeySignature = signature.sign();
		
		// e) Verschlüsselung des geheimen Schlüssel aus c) mit dem öffentlichen RSA‐Schlüssel (Algorithmus: „RSA“)
		Cipher rsaCipher = Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] aesKeyEncrypted = rsaCipher.doFinal(aesKey.getEncoded());
		
		// f) Einlesen einer Dokumentendatei, Verschlüsseln der Dateidaten mit dem symmetrischen AES‐Algorithmus
		// (geheimer Schlüssel aus c) im Counter‐Mode („CTR“) und Erzeugen einer Ausgabedatei.
		try(
				DataOutputStream os = new DataOutputStream(new FileOutputStream(new File("res/praktikum3", parameter._outputFile)));
				InputStream is = new BufferedInputStream(new FileInputStream(new File("res/praktikum3", parameter._inputFile)))
		) {
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
			// TODO: Parameter für den aesCipher setzen?
			
			os.writeInt(aesKeyEncrypted.length);
			os.write(aesKeyEncrypted);
			os.writeInt(aesKeySignature.length);
			os.write(aesKeySignature);
			os.writeInt(aesCipher.getParameters().getEncoded().length);
			os.write(aesCipher.getParameters().getEncoded());
			
			byte[] buffer = new byte[8];
			int len = 0;
			long ctr = 0;
			while((len=is.read(buffer)) != -1) {
				byte[] rng = aesCipher.update(ByteUtils.toBytes(ctr++));
				os.write(ByteUtils.xor(rng, buffer), 0, len);
			}
			os.write(aesCipher.doFinal());
		}
	}
}
