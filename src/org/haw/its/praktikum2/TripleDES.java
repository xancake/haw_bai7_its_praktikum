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
		_key1    = Arrays.copyOf(key1,    key1.length);
		_key2    = Arrays.copyOf(key2,    key2.length);
		_key3    = Arrays.copyOf(key3,    key3.length);
		_initial = Arrays.copyOf(initial, initial.length);
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
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
		) {
			DES des1 = new DES(_key1);
			DES des2 = new DES(_key2);
			DES des3 = new DES(_key3);
			
			byte[] plain = new byte[8];
			byte[] cipher = Arrays.copyOf(_initial, _initial.length);
			byte[] bce = new byte[8];
			while(is.read(plain) != -1) {
				des1.encrypt(cipher, 0, bce, 0);
				des2.decrypt(bce, 0, bce, 0);
				des3.encrypt(bce, 0, bce, 0);
				cipher = xor(bce, plain);
				os.write(cipher);
				plain = new byte[8];
			}
		}
	}
	
	public void decrypt(File fileIn, File fileOut) throws FileNotFoundException, IOException {
		if(!fileIn.exists()) {
			throw new FileNotFoundException("Die Eingabe-Datei existiert nicht: " + fileIn.getAbsolutePath());
		}
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
		) {
			DES des1 = new DES(_key1);
			DES des2 = new DES(_key2);
			DES des3 = new DES(_key3);
			
			byte[] buffer = new byte[8];
			byte[] cipher = Arrays.copyOf(_initial, _initial.length);
			byte[] bce = new byte[8];
			byte[] plain = new byte[8];
			while(is.read(buffer) != -1) {
				des1.encrypt(cipher, 0, bce, 0);
				des2.decrypt(bce, 0, bce, 0);
				des3.encrypt(bce, 0, bce, 0);
				plain = xor(bce, buffer);
				cipher = buffer;
				os.write(plain);
				buffer = new byte[8];
			}
		}
	}
	
	private byte[] xor(byte[] a, byte[] b) {
		if(a.length != b.length) {
			throw new IllegalArgumentException("");
		}
		byte[] out = new byte[a.length];
		for(int i=0; i<a.length; i++) {
			out[i] = (byte)(a[i] ^ b[i]);
		}
		return out;
	}
	
	public static void main(String... args) throws Exception {
		String keyFileStr = null;
		String inputFileStr = null;
		String outputFileStr = null;
		String encrypt = null;
		switch(args.length) {
			case 0: {
				Scanner keyboard = new Scanner(System.in);
				System.out.print("Key-File: ");
				keyFileStr = keyboard.nextLine();
				System.out.print("Input-File: ");
				inputFileStr = keyboard.nextLine();
				System.out.print("Output-File: ");
				outputFileStr = keyboard.nextLine();
				System.out.print("encrypt/decrypt: ");
				encrypt = keyboard.nextLine();
				keyboard.close();
				break;
			}
			case 1: {
				if("default".equals(args[0])) {
					keyFileStr    = "3DESTest.key";
					inputFileStr  = "3DESTest.enc";
					outputFileStr = "3DESTest.pdf";
					encrypt       = "encrypt";
				} else {
					System.out.println("Usage: TripleDES [[<keyFile>] {<inputFile>} {<outputFile>} {encrypt|decrypt}]");
					System.exit(1);
				}
				break;
			}
			case 3: {
				keyFileStr    = "3DESTest.key";
				inputFileStr  = args[0];
				outputFileStr = args[1];
				encrypt       = args[2];
				break;
			}
			case 4: {
				keyFileStr    = args[0];
				inputFileStr  = args[1];
				outputFileStr = args[2];
				encrypt       = args[3];
				break;
			}
			default: {
				System.out.println("Usage: TripleDES [[<keyFile>] {<inputFile>} {<outputFile>} {encrypt|decrypt}]");
				System.exit(1);
			}
		}
		
		File keyFile    = new File(ClassLoader.getSystemClassLoader().getResource(keyFileStr).toURI());
		File inputFile  = new File(ClassLoader.getSystemClassLoader().getResource(inputFileStr).toURI());
		File outputFile = new File(outputFileStr);
		TripleDES tripleDES = TripleDES.createFromKeyFile(keyFile);
		if("encrypt".equals(encrypt)) {
			tripleDES.encrypt(inputFile, outputFile);
		} else {
			tripleDES.decrypt(inputFile, outputFile);
		}
	}
}
