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

	// XXX: Angepasst
	public boolean login(String userName, char[] password) {
		System.out.println("[C] Einloggen...");
		long nonce = generateNonce();
		TicketResponse response = myKDC.requestTGSTicket(userName, "myTGS", nonce);
		if(response != null) {
			if(!response.isEncrypted()) {
				throw new IllegalArgumentException("Das Response ist nicht verschlüsselt!");
			}
			if(!response.decrypt(generateSimpleKeyFromPassword(password))) {
				throw new IllegalArgumentException("Response konnte nicht entschlüsselt werden!");
			}
			if(response.getNonce() != nonce) {
				throw new IllegalArgumentException("Nonce nicht gleich, eigener: " + nonce + ", server: " + response.getNonce() + "!");
			}
			System.out.println("[C] Login erfolgreich!");
			currentUser = userName;
			tgsTicket = response.getResponseTicket();
			tgsSessionKey = response.getSessionKey();
			return true;
		}
		return false;
	}

	// XXX: Angepasst
	public boolean showFile(Server fileServer, String filePath) {
		System.out.println("[C] Request Service von " + fileServer.getName());
		long nonce = generateNonce();
		Auth auth = new Auth(currentUser, System.currentTimeMillis());
		auth.encrypt(tgsSessionKey);
		TicketResponse response = myKDC.requestServerTicket(tgsTicket, auth, fileServer.getName(), nonce);
		if(response != null) {
			if(!response.decrypt(tgsSessionKey)) {
				throw new IllegalArgumentException("Response konnte nicht entschlüsselt werden!");
			}
			if(response.getNonce() != nonce) {
				throw new IllegalArgumentException("Nonce nicht gleich, eigener: " + nonce + ", server: " + response.getNonce() + "!");
			}
			Auth serverAuth = new Auth(currentUser, System.currentTimeMillis());
			serverAuth.encrypt(response.getSessionKey());
			return fileServer.requestService(response.getResponseTicket(), serverAuth, Server.COMMAND_SHOW_FILE, filePath);
		}
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
