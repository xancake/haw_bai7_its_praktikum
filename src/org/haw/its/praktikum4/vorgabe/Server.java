package org.haw.its.praktikum4.vorgabe;

import java.util.*;
import java.io.*;

/**
 * Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver Server-Klasse
 */
public class Server {
	private final long fiveMinutesInMillis = 300000; // 5 Minuten in Millisekunden

	private String myName;
	private KDC myKDC;     // wird bei KDC-Registrierung gespeichert
	private long myKey;    // wird bei KDC-Registrierung gespeichert

	public Server(String name) {
		myName = name;
	}

	public String getName() {
		return myName;
	}

	/**
	 * Anmeldung des Servers beim KDC
	 */
	public void setupService(KDC kdc) {
		myKDC = kdc;
		myKey = myKDC.serverRegistration(myName);
		System.out.println("Server " + myName + " erfolgreich registriert bei KDC " + myKDC.getName() + " mit ServerKey " + myKey);
	}

	public boolean requestService(Ticket srvTicket, Auth srvAuth, String command, String parameter) {
			/* TODO */
	}

	/* *********** Services **************************** */

	/**
	 * Angegebene Datei auf der Konsole ausgeben.
	 * @return Status der Operation
	 */
	private boolean showFile(String filePath) {
		String lineBuf = null;
		File myFile = new File(filePath);
		boolean status = false;

		if (!myFile.exists()) {
			System.out.println("Datei " + filePath + " existiert nicht!");
		} else {
			try {
				// Datei öffnen und zeilenweise lesen
				BufferedReader inFile = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));
				lineBuf = inFile.readLine();
				while (lineBuf != null) {
					System.out.println(lineBuf);
					lineBuf = inFile.readLine();
				}
				inFile.close();
				status = true;
			} catch (IOException ex) {
				System.out.println("Fehler beim Lesen der Datei " + filePath + ex);
			}
		}
		return status;
	}

	/* *********** Hilfsmethoden **************************** */

	/**
	 * Wenn die aktuelle Zeit innerhalb der übergebenen Zeitgrenzen liegt, wird true zurückgegeben
	 */
	private boolean timeValid(long lowerBound, long upperBound) {
		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit 1.1.1970
		if (currentTime >= lowerBound && currentTime <= upperBound) {
			return true;
		} else {
			System.out.println("-------- Time not valid: " + currentTime + " not in (" + lowerBound + "," + upperBound + ")!");
			return false;
		}
	}

	/**
	 * Wenn die übergebene Zeit nicht mehr als 5 Minuten von der aktuellen Zeit abweicht, wird true zurückgegeben
	 */
	boolean timeFresh(long testTime) {
		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit 1.1.1970
		if (Math.abs(currentTime - testTime) < fiveMinutesInMillis) {
			return true;
		} else {
			System.out.println("-------- Time not fresh: " + currentTime + " is current, " + testTime + " is old!");
			return false;
		}
	}
}
