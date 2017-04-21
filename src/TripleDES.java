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
			}
		}
	}
}
