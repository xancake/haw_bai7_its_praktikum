import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Scanner;

public class HC1 {
	public void encryptLCG(long start, String fileIn, String fileOut) throws FileNotFoundException, IOException {
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
		) {
			LCG lcg = new LCG(start);
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
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(fileIn));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOut));
		) {
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.setSeed(start);
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
	
	public static void main(String... args) throws Exception {
		try (Scanner scanner = new Scanner(System.in)) {
    		System.out.print("Modus (LCG|Secure): ");
    		String mode = scanner.nextLine();
    		
    		System.out.print("Seed: ");
    		long start = scanner.nextLong();
    		scanner.nextLine();
    		
    		System.out.print("Zu verschluesselnde Datei: ");
    		String fileIn = scanner.nextLine();
    		
    		System.out.print("Ausgabedatei (verschlüsselt): ");
    		String fileOut = scanner.nextLine();
    		
    		if("LCG".equals(mode)) {
    			new HC1().encryptLCG(start, fileIn, fileOut);
    		} else {
    			new HC1().encryptSecureRandom(start, fileIn, fileOut);
    		}
		}
	}
}
