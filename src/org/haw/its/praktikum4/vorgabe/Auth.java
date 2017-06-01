package org.haw.its.praktikum4.vorgabe;

import java.util.*;

/**
 * Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver Auth-Klasse (Authentification)
 */
public class Auth extends Object {
	private String myClientName; // Konstruktor-Parameter
	private long myCurrentTime;  // Konstruktor-Parameter
	private long myAuthKey;      // Geheimer Schlüssel, mit dem die Authentifikation (simuliert) verschlüsselt ist
	private boolean isEncryptedState; // Aktueller Zustand des Objekts

	public Auth(String clientName, long currentTime) {
		myClientName = clientName;
		myCurrentTime = currentTime;
		myAuthKey = -1;
		isEncryptedState = false;
	}

	public String getClientName() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsselte Authentifikation (getClientName)");
		}
		return myClientName;
	}

	public long getCurrentTime() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsselte Authentifikation (getCurrentTime)");
		}
		return myCurrentTime;
	}

	/**
	 * Authentifikation mit dem Key verschlüsseln.
	 * Falls die Authentifikation bereits verschlüsselt ist, wird false zurückgegeben.
	 */
	public boolean encrypt(long key) {
		boolean encOK = false;
		if (isEncryptedState) {
			printError("Auth ist bereits verschlüsselt");
		} else {
			myAuthKey = key;
			isEncryptedState = true;
			encOK = true;
		}
		return encOK;
	}

	/**
	 * Authentifikation mit dem Key entschlüsseln.
	 * Falls der Key falsch ist oder die Authentifikation bereits entschlüsselt ist, wird false zurückgegeben.
	 */
	public boolean decrypt(long key) {
		boolean decOK = false;
		if (!isEncryptedState) {
			printError("Auth ist bereits entschlüsselt");
		}
		if (myAuthKey != key) {
			printError("Auth-Entschlüsselung mit key " + key + " ist fehlgeschlagen");
		} else {
			isEncryptedState = false;
			decOK = true;
		}
		return decOK;
	}

	/**
	 * Aktuellen Zustand zurückgeben:
	 * @return verschlüsselt (true) / entschlüsselt (false)
	 */
	public boolean isEncrypted() {
		return isEncryptedState;
	}

	public void printError(String message) {
		throw new IllegalArgumentException("+++++++++++++++++++ Fehler +++++++++++++++++++ " + message + "! Auth-Key: " + myAuthKey);
	}

	public void print() {
		System.out.println("********* Authentifikation für " + myClientName + " *******");
		System.out.println("CurrentTime: " + getDateString(myCurrentTime));
		System.out.println("Auth Key: " + myAuthKey);
		if (isEncryptedState) {
			System.out.println("Auth-Zustand: verschlüsselt (encrypted)!");
		} else {
			System.out.println("Auth-Zustand: entschlüsselt (decrypted)!");
		}
		System.out.println();
	}

	/**
	 * Umrechnung der Zeitangabe time (Millisek. seit 1.1.1970) in einen Datumsstring
	 */
	private String getDateString(long time) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		return new StringBuilder()
				.append(cal.get(Calendar.DAY_OF_MONTH)).append(".")
				.append(cal.get(Calendar.MONTH) + 1).append(".")
				.append(cal.get(Calendar.YEAR)).append(" ")
				.append(cal.get(Calendar.HOUR_OF_DAY)).append(":")
				.append(cal.get(Calendar.MINUTE)).append(":")
				.append(cal.get(Calendar.SECOND)).append(":")
				.append(cal.get(Calendar.MILLISECOND))
				.toString();
	}
}
