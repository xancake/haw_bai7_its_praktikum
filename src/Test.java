import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// testLCG();
		// testHC1();
		// testHC1SecureRandom();
		testTripleDES();
	}

	public static void testLCG() {
		System.out.println("----Test LCG Start----");
		LCG testLCG = new LCG(3529757982L);
		for (int i = 0; i < 255; i++) {
			System.out.println(testLCG.nextInt() & 0x000000FF);
		}
		System.out.println("----Test LCG End----");
	}

	public static void testHC1() {
		System.out.println("----LCG encrypt----");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Startwert: ");
		long start = scanner.nextLong();
		scanner.nextLine();
		System.out.println("Zu verschluesselnde Datei: ");
		String fileIn = scanner.nextLine();
		System.out.println("Speicherort: ");
		String fileOut = scanner.nextLine();

		try {
			new HC1().encryptLCG(start, fileIn, fileOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testHC1SecureRandom() {
		System.out.println("----SecureRandom encrypt----");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Startwert: ");
		long start = scanner.nextLong();
		scanner.nextLine();
		System.out.println("Zu verschluesselnde Datei: ");
		String fileIn = scanner.nextLine();
		System.out.println("Speicherort: ");
		String fileOut = scanner.nextLine();

		try {
			new HC1().encryptSecureRandom(start, fileIn, fileOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testTripleDES() {
		System.out.println("----TripleDES encrypt----");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Zu verschluesselnde Datei: ");
		String fileIn = scanner.nextLine();
		System.out.println("Schluessel-Datei: ");
		String keyFile = scanner.nextLine();
		System.out.println("Ausgabedatei: ");
		String fileOut = scanner.nextLine();
		System.out.println("Statusstring (encrypt|decrypt): ");
		String status = scanner.nextLine();
		try {
			File fKey = new File(ClassLoader.getSystemClassLoader().getResource(keyFile).toURI());
			File fIn = new File(ClassLoader.getSystemClassLoader().getResource(fileIn).toURI());
			File fOut = new File(fileOut);
			TripleDES trippleDes = TripleDES.createFromKeyFile(fKey);
			if (status.equals("encrypt")) {
				trippleDes.encrypt(fIn, fOut);
			} else {
				trippleDes.decrypt(fIn, fOut);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
