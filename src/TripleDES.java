import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripleDES {

	public void tripleEncrypt(String fileIn, String keyFile, String fileOut) throws FileNotFoundException, IOException {

		try (InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
				InputStream keyIs = new BufferedInputStream(new FileInputStream(keyFile))) {
			byte[] key1 = new byte[8];
			byte[] key2 = new byte[8];
			byte[] key3 = new byte[8];
			byte[] initial = new byte[8];

			keyIs.read(key1, 0, 8);
			keyIs.read(key2, 0, 8);
			keyIs.read(key3, 0, 8);
			keyIs.read(initial, 0, 8);

			byte[] buffer = new byte[8];
			int length;
			List<byte[]> temp = new ArrayList<>();

			byte[] cipher = Arrays.copyOf(initial, initial.length);
			while ((length = is.read(buffer)) != -1) {
				cipher = encryptCFB(buffer, length, new DES(key1), cipher);
				temp.add(cipher);
			}

			List<byte[]> temp2 = new ArrayList<>();
			cipher = Arrays.copyOf(initial, initial.length);
			for (byte[] bs : temp) {
				bs = decryptCFB(bs, bs.length, new DES(key2), cipher);
				temp2.add(bs);
			}

			cipher = Arrays.copyOf(initial, initial.length);
			for (byte[] bs : temp2) {
				bs = encryptCFB(bs, bs.length, new DES(key3), cipher);
				os.write(bs, 0, bs.length);
			}

		}
	}

	public void tripleDecrypt(String fileIn, String keyFile, String fileOut) throws FileNotFoundException, IOException {

		try (InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
				InputStream keyIs = new BufferedInputStream(new FileInputStream(keyFile))) {
			byte[] key1 = new byte[8];
			byte[] key2 = new byte[8];
			byte[] key3 = new byte[8];
			byte[] initial = new byte[8];

			keyIs.read(key1, 0, 8);
			keyIs.read(key2, 0, 8);
			keyIs.read(key3, 0, 8);
			keyIs.read(initial, 0, 8);

			byte[] buffer = new byte[8];
			int length;

			List<byte[]> temp = new ArrayList<>();
			byte[] cipher = Arrays.copyOf(initial, initial.length);
			byte[] plain = new byte[8];
			DES des1 = new DES(key1);
			while ((length = is.read(buffer)) != -1) {
				plain = Arrays.copyOf(encryptCFB(buffer, length, des1, cipher), length);
				temp.add(plain);
				cipher = Arrays.copyOf(buffer, length);
			}

			DES des2 = new DES(key2);
			List<byte[]> temp2 = new ArrayList<>();
			cipher = Arrays.copyOf(initial, initial.length);
			for (byte[] bs : temp) {
				plain = Arrays.copyOf(decryptCFB(bs, bs.length, des2, cipher), bs.length);
				temp2.add(plain);
				cipher = Arrays.copyOf(bs, bs.length);

			}

			DES des3 = new DES(key3);
			cipher = Arrays.copyOf(initial, initial.length);
			for (byte[] bs : temp2) {
				plain = Arrays.copyOf(encryptCFB(bs, bs.length, des3, cipher), bs.length);
				cipher = Arrays.copyOf(bs, bs.length);
				os.write(plain, 0, plain.length);
			}

		}
	}

	public byte[] encryptCFB(byte[] message, int length, DES des, byte[] cipher) {
		byte[] bce = new byte[8];

		des.encrypt(cipher, 0, bce, 0);
		for (int i = 0; i < cipher.length; i++) {
			cipher[i] = (byte) (message[i] ^ bce[i]);
		}

		return cipher;
	}

	public byte[] decryptCFB(byte[] message, int length, DES des, byte[] cipher) {
		byte[] bce = new byte[8];

		des.decrypt(cipher, 0, bce, 0);
		for (int i = 0; i < cipher.length; i++) {
			cipher[i] = (byte) (message[i] ^ bce[i]);
		}

		return cipher;
	}
}
