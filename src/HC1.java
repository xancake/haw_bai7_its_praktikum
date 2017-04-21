import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

public class HC1 {

	public void encryptLCG(long start, String fileIn, String fileOut) throws FileNotFoundException, IOException {
		LCG lcg = new LCG(start);
		try (InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = (byte) (buffer[i] ^ (lcg.nextInt() & 0x000000FF));
				}
				os.write(buffer, 0, length);
			}
		}

	}
	
	public void encryptSecureRandom(long start, String fileIn, String fileOut) throws FileNotFoundException, IOException {
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.setSeed(start);
		try (InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = (byte) (buffer[i] ^ (secureRandom.nextInt() & 0x000000FF));
				}
				os.write(buffer, 0, length);
			}
		}

	}

}
