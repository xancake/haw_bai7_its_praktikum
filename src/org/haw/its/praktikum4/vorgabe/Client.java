package org.haw.its.praktikum4.vorgabe;

/** 
 * Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver Client-Klasse
 */
public class Client extends Object {
	private KDC myKDC;          // Konstruktor-Parameter
	private String currentUser; // Speicherung bei Login nötig
	private Ticket tgsTicket;   // Speicherung bei Login nötig
	private long tgsSessionKey; // K(C,TGS), Speicherung bei Login nötig

	public Client(KDC kdc) {
		myKDC = kdc;
	}

	public boolean login(String userName, char[] password) {
		// TODO
		return false;
	}

	public boolean showFile(Server fileServer, String filePath) {
		// TODO
		return false;
	}

	/* *********** Hilfsmethoden **************************** */

	/**
	 * Liefert einen eindeutig aus dem Passwort abgeleiteten Schlüssel zurück, hier simuliert als long-Wert
	 */
	private long generateSimpleKeyFromPassword(char[] passwd) {
		long pwKey = 0;
		if (passwd != null) {
			for (int i = 0; i < passwd.length; i++) {
				pwKey = pwKey + passwd[i];
			}
		}
		return pwKey;
	}

	/**
	 * Liefert einen neuen Zufallswert
	 */
	private long generateNonce() {
		return (long)(100000000 * Math.random());
	}
}
