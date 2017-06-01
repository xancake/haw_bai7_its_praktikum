package org.haw.its.praktikum4.vorgabe;

/**
 *  Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver TicketResponse-Klasse
 */
public class TicketResponse {
	private long mySessionKey;
	private long myNonce;
	private Ticket myResponseTicket;
	private long myResponseKey;       // Geheimer Schlüssel, mit dem diese Antwort (Response) (simuliert) verschlüsselt ist
	private boolean isEncryptedState; // Aktueller Zustand des Objekts

	public TicketResponse(long sessionKey, long nonce, Ticket responseTicket) {
		mySessionKey = sessionKey;
		myNonce = nonce;
		myResponseTicket = responseTicket;
		myResponseKey = -1;
		isEncryptedState = false;
	}

	public long getSessionKey() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsselte Ticket-Response (getSessionKey)");
		}
		return mySessionKey;
	}

	public long getNonce() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsselte Ticket-Response (getNonce)");
		}
		return myNonce;
	}

	public Ticket getResponseTicket() {
		if (isEncryptedState) {
			printError("Zugriff auf verschlüsselte Ticket-Response (getResponseTicket)");
		}
		return myResponseTicket;
	}

	/**
	 * TicketResponse mit dem Key verschlüsseln.
	 * Falls die TicketResponse bereits verschlüsselt ist, wird false zurückgegeben.
	 */
	public boolean encrypt(long key) {
		boolean encOK = false;
		if (isEncryptedState) {
			printError("TicketResponse ist bereits verschlüsselt");
		} else {
			myResponseKey = key;
			isEncryptedState = true;
			encOK = true;
		}
		return encOK;
	}

	/**
	 * TicketResponse mit dem Key entschlüsseln.
	 * Falls der Key falsch ist oder die TicketResponse bereits entschlüsselt ist, wird false zurückgegeben.
	 */
	public boolean decrypt(long key) {
		boolean decOK = false;
		if (!isEncryptedState) {
			printError("TicketResponse ist bereits entschlüsselt");
		}
		if (myResponseKey != key) {
			printError("TicketResponse-Entschlüsselung mit key " + key + " ist fehlgeschlagen");
		} else {
			isEncryptedState = false;
			decOK = true;
		}
		return decOK;
	}

	/**
	 * Aktuellen Zustand zurückgeben.
	 * @return verschlüsselt (true) / entschlüsselt (false)
	 */
	public boolean isEncrypted() {
		return isEncryptedState;
	}

	public void printError(String message) {
		throw new IllegalArgumentException("+++++++++++++++++++ Fehler +++++++++++++++++++ " + message + "! TicketResponse-Key: " + myResponseKey);
	}

	public void print() {
		System.out.println("********* TicketResponse *******");
		System.out.println("Session Key: " + mySessionKey);
		System.out.println("Nonce: " + myNonce);
		myResponseTicket.print();
		System.out.println("Response Key: " + myResponseKey);
		if (isEncryptedState) {
			System.out.println("TicketResponse-Zustand: verschlüsselt (encrypted)!");
		} else {
			System.out.println("TicketResponse-Zustand: entschlüsselt (decrypted)!");
		}
		System.out.println();
	}
}
