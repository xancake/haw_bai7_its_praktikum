package org.haw.its.praktikum4.vorgabe;

import java.util.*;

/**
 * Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver Ticket-Klasse
 */
public class Ticket {
	private String myClientName;
	private String myServerName;
	private long myStartTime;
	private long myEndTime;
	private long mySessionKey;
	private long myTicketKey;         // Geheimer Schlüssel, mit dem das Ticket (simuliert) verschlüsselt ist
	private boolean isEncryptedState; // Aktueller Zustand des Objekts

	public Ticket(String clientName, String serverName, long startTime, long endTime, long sessionKey) {
		myClientName = clientName;
		myServerName = serverName;
		myStartTime = startTime;
		myEndTime = endTime;
		mySessionKey = sessionKey;
		myTicketKey = -1;
		isEncryptedState = false;
	}

	public String getClientName() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsseltes Ticket (getClientName)");
		}
		return myClientName;
	}

	public String getServerName() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsseltes Ticket (getServerName)");
		}
		return myServerName;
	}

	public long getStartTime() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsseltes Ticket (getStartTime)");
		}
		return myStartTime;
	}

	public long getEndTime() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsseltes Ticket (getEndTime)");
		}
		return myEndTime;
	}

	public long getSessionKey() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsseltes Ticket (getSessionKey)");
		}
		return mySessionKey;
	}

	/**
	 * Ticket mit dem Key verschlüsseln.
	 * Falls das Ticket bereits verschlüsselt ist, wird false zurückgegeben.
	 */
	public boolean encrypt(long key) {
		boolean encOK = false;
		if (isEncryptedState) {
			printError("Ticket ist bereits verschlüsselt");
		} else {
			myTicketKey = key;
			isEncryptedState = true;
			encOK = true;
		}
		return encOK;
	}

	/**
	 * Ticket mit dem Key entschlüsseln.
	 * Falls der Key falsch ist oder das Ticket bereits entschlüsselt ist, wird false zurückgegeben.
	 */
	public boolean decrypt(long key) {
		boolean decOK = false;
		if (!isEncryptedState) {
			printError("Ticket ist bereits entschlüsselt");
		}
		if (myTicketKey != key) {
			printError("Ticket-Entschlüsselung mit key " + key + " ist fehlgeschlagen");
		} else {
			isEncryptedState = false;
			decOK = true;
		}
		return decOK;
	}

	/**
	 * Aktuellen Zustand zurückgeben
	 * @return verschlüsselt (true) / entschlüsselt (false)
	 */
	public boolean isEncrypted() {
		return isEncryptedState;
	}

	public void print() {
		System.out.println("********* Ticket für " + myClientName + " / " + myServerName + " *******");
		System.out.println("StartTime: " + getDateString(myStartTime) + " - EndTime: " + getDateString(myEndTime));
		System.out.println("Session Key: " + mySessionKey);
		System.out.println("Ticket Key: " + myTicketKey);
		if (isEncryptedState) {
			System.out.println("Ticket-Zustand: verschlüsselt (encrypted)!");
		} else {
			System.out.println("Ticket-Zustand: entschlüsselt (decrypted)!");
		}
	}

	public void printError(String message) {
		System.out.println("+++++++++++++++++++");
		System.out.println("+++++++++++++++++++ Fehler +++++++++++++++++++ " + message + "! Ticket-Key: " + myTicketKey);
		System.out.println("+++++++++++++++++++");
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
