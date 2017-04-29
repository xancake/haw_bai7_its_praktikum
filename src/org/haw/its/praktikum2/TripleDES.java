package org.haw.its.praktikum2;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Scanner;

public class TripleDES {
	private final byte[] _key1;
	private final byte[] _key2;
	private final byte[] _key3;
	private final byte[] _initial;
	
	public TripleDES(byte[] key1, byte[] key2, byte[] key3, byte[] initial) {
		_key1 = Arrays.copyOf(key1, key1.length);
		_key2 = Arrays.copyOf(key2, key1.length);
		_key3 = Arrays.copyOf(key3, key1.length);
		_initial = Arrays.copyOf(initial, key1.length);
	}
	
	public static TripleDES createFromKeyFile(File keyFile) throws FileNotFoundException, IOException {
		try (InputStream is = new BufferedInputStream(new FileInputStream(keyFile))) {
			byte[] key1 = new byte[8];
			byte[] key2 = new byte[8];
			byte[] key3 = new byte[8];
			byte[] initial = new byte[8];
			
			is.read(key1, 0, 8);
			is.read(key2, 0, 8);
			is.read(key3, 0, 8);
			is.read(initial, 0, 8);
			
			return new TripleDES(key1, key2, key3, initial);
		}
	}

	public void encrypt(File fileIn, File fileOut) throws FileNotFoundException, IOException {
		if(!fileIn.exists()) {
			throw new FileNotFoundException("Die Eingabe-Datei existiert nicht: " + fileIn.getAbsolutePath());
		}
		if(fileOut.exists()) {
			throw new IOException("Die Ausgabe-Datei existiert bereits: " + fileOut.getAbsolutePath());
		}
		
		File temp1 = File.createTempFile("haw_bai7_its_3des_step1", null);
		File temp2 = File.createTempFile("haw_bai7_its_3des_step2", null);
		encryptSingle(_key1, fileIn, temp1);
		decryptSingle(_key2, temp1, temp2);
		encryptSingle(_key3, temp2, fileOut);
		temp1.delete();
		temp2.delete();
	}

	public void decrypt(File fileIn, File fileOut) throws FileNotFoundException, IOException {
		encrypt(fileIn, fileOut);
	}
	
	private void encryptSingle(byte[] key, File fileIn, File fileOut) throws FileNotFoundException, IOException {
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
		) {
			DES des = new DES(key);
			
			byte[] buffer = new byte[8];
			byte[] cipher = Arrays.copyOf(_initial, _initial.length);
			int len;
			while((len=is.read(buffer)) != -1) {
				byte[] bce = new byte[8];
				des.encrypt(cipher, 0, bce, 0);
				for(int i=0; i<bce.length; i++) {
					cipher[i] = (byte)(bce[i] ^ buffer[i]);
				}
				os.write(cipher);
				buffer = new byte[8];
			}
		}
	}
	
	private void decryptSingle(byte[] key, File fileIn, File fileOut) throws FileNotFoundException, IOException {
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
		) {
			DES des = new DES(key);
			
			byte[] buffer = new byte[8];
			byte[] cipher = Arrays.copyOf(_initial, _initial.length);
			int len;
			while((len=is.read(buffer)) != -1) {
				byte[] bce = new byte[8];
				byte[] plain = new byte[8];
				des.encrypt(cipher, 0, bce, 0);
				for(int i=0; i<bce.length; i++) {
					plain[i] = (byte)(bce[i] ^ buffer[i]);
				}
				cipher = buffer;
				os.write(plain);
				buffer = new byte[8];
			}
		}
	}
	
	public static void main(String... args) throws Exception {
		String keyFileStr = null;
		String inputFileStr = null;
		String outputFileStr = null;
		switch(args.length) {
			case 0: {
				Scanner keyboard = new Scanner(System.in);
				System.out.print("Key-File: ");
				keyFileStr = keyboard.nextLine();
				System.out.print("Input-File: ");
				inputFileStr = keyboard.nextLine();
				System.out.print("Output-File: ");
				outputFileStr = keyboard.nextLine();
				keyboard.close();
				break;
			}
			case 1: {
				if("default".equals(args[0])) {
					keyFileStr = "3DESTest.key";
					inputFileStr  = "3DESTest.enc";
					outputFileStr = "3DESTest.pdf";
				} else {
					System.out.println("Usage: TripleDES [{keyFile} {inputFile} {outputFile}]");
					System.exit(1);
				}
				break;
			}
			case 3: {
				keyFileStr    = args[0];
				inputFileStr  = args[1];
				outputFileStr = args[2];
				break;
			}
			default: {
				System.out.println("Usage: TripleDES [{keyFile} {inputFile} {outputFile}]");
				System.exit(1);
			}
		}
		
		File keyFile    = new File(ClassLoader.getSystemClassLoader().getResource(keyFileStr).toURI());
		File inputFile  = new File(ClassLoader.getSystemClassLoader().getResource(inputFileStr).toURI());
		File outputFile = new File(outputFileStr);
		TripleDES tripleDES = TripleDES.createFromKeyFile(keyFile);
		tripleDES.encrypt(inputFile, outputFile);
	}
}
