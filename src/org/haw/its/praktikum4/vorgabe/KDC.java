package org.haw.its.praktikum4.vorgabe;

import java.util.*;

/** 
 * Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver KDC-Klasse
 */
public class KDC extends Object {
	private final long tenHoursInMillis    = 36000000; // 10 Stunden in Millisekunden
	private final long fiveMinutesInMillis = 300000;   // 5 Minuten in Millisekunden

	/* *********** Datenbank-Simulation **************************** */

	private String tgsName;  // TGS
	private long tgsKey;     // K(TGS)
	
	private String user;          // C
	private long userPasswordKey; // K(C)

	private String serverName; // S
	private long serverKey;    // K(S)

	public KDC(String name) {
		tgsName = name;
		tgsKey = generateSimpleKey(); // Eigenen Key für TGS erzeugen (streng geheim!!!)
	}

	public String getName() {
		return tgsName;
	}

	/* *********** Initialisierungs-Methoden **************************** */

	/**
	 * Server in der Datenbank registrieren.
	 * @return ein neuer geheimer Schlüssel für den Server
	 */
	public long serverRegistration(String sName) {
		serverName = sName;
		serverKey = generateSimpleKey(); // Eigenen Key für Server erzeugen (streng geheim!!!)
		return serverKey;
	}

	/**
	 * User registrieren --> Eintrag des Usernamens in die Benutzerdatenbank
	 */
	public void userRegistration(String userName, char[] password) {
		user = userName;
		userPasswordKey = generateSimpleKeyForPassword(password);

		System.out.println("Principal: " + user);
		System.out.println("Password-Key: " + userPasswordKey);
	}

	/* *********** AS-Modul: TGS - Ticketanfrage **************************** */

	/**
	 * Anforderung eines TGS-Tickets bearbeiten.
	 * @return TicketResponse für die Anfrage
	 */
	public TicketResponse requestTGSTicket(String userName, String tgsServerName, long nonce) {
		// Usernamen und Userpasswort in der Datenbank suchen!
		if(!userName.equals(user) || !tgsServerName.equals(tgsName)) {
			return null;
		}
		
		// TGS-Antwort zusammenbauen
		// OK, neuen Session Key für Client und TGS generieren
		long tgsSessionKey = generateSimpleKey(); // K(C,TGS)
		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit 1.1.1970

		// Zuerst TGS-Ticket basteln ...
		Ticket tgsTicket = new Ticket(user, tgsName, currentTime, currentTime + tenHoursInMillis, tgsSessionKey);

		// ... dann verschlüsseln ...
		tgsTicket.encrypt(tgsKey);

		// ... dann Antwort erzeugen
		TicketResponse tgsTicketResp = new TicketResponse(tgsSessionKey, nonce, tgsTicket);

		// ... und verschlüsseln
		tgsTicketResp.encrypt(userPasswordKey);
		
		return tgsTicketResp;
	}

	/*
	 * *********** TGS-Modul: Server - Ticketanfrage ****************************
	 */

	public TicketResponse requestServerTicket(Ticket tgsTicket, Auth tgsAuth, String serverName, long nonce) {
		// TODO
		return null;
	}

	/* *********** Hilfsmethoden **************************** */

	/**
	 * Liefert den zugehörigen Serverkey für den Servernamen zurück
	 * Wenn der Servername nicht bekannt, wird -1 zurückgegeben
	 */
	private long getServerKey(String sName) {
		if (sName.equalsIgnoreCase(serverName)) {
			System.out.println("Serverkey ok");
			return serverKey;
		} else {
			System.out.println("Serverkey unbekannt!!!!");
			return -1;
		}
	}

	/**
	 * Liefert einen Schlüssel für ein Passwort zurück, hier simuliert als long-Wert
	 */
	private long generateSimpleKeyForPassword(char[] pw) {
		long pwKey = 0;
		for (int i = 0; i < pw.length; i++) {
			pwKey = pwKey + pw[i];
		}
		return pwKey;
	}

	/**
	 * Liefert einen neuen geheimen Schlüssel, hier nur simuliert als long-Wert
	 */
	private long generateSimpleKey() {
		return (long)(100000000 * Math.random());
	}

	boolean timeValid(long lowerBound, long upperBound) {
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
