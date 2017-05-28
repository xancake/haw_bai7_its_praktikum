package org.haw.its.praktikum4.vorgabe;

import java.util.*;

/**
 * Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver
 */
public class KerberosSim {
	private KDC myKDC;
	private Client myClient;
	private Server myFileserver;

	public void initKerberos(String userName, char[] password, String serverName, String tgsName) {
		myKDC = new KDC(tgsName);

		// Server initialisieren
		myFileserver = new Server(serverName);
		myFileserver.setupService(myKDC); // Schlüsselerzeugung und -austausch

		// User-Account und Client erzeugen
		myKDC.userRegistration(userName, password);
		myClient = new Client(myKDC);
	}

	/**
	 * Passworteingabe über modalen Dialog.
	 * @return Liefert ein Passwort oder null bei Abbruch durch den Benutzer
	 */
	public char[] readPasswd(String userName) {
		PasswordDialog pwDialog = new PasswordDialog(userName);
		if (pwDialog.statusOK()) {
			return pwDialog.getPassword();
		}
		return null;
	}

	public static void main(String args[]) {
		// Simulation einer Benutzer-Session: Anmeldung und Zugriff auf Fileserver

		// -------- Start Initialisierung des Systems ------------------
		String userName = "axz467";
		char[] password = { 'S', 'e', 'c', 'r', 'e', 't', '!' };
		String serverName = "myFileserver";
		String tgsName = "myTGS";
		String filePath = "C:/Temp/ITS.txt";

		KerberosSim thisSession = new KerberosSim();

		// KDC + alle Server + Client initialisieren
		thisSession.initKerberos(userName, password, serverName, tgsName);
		// -------- Ende Initialisierung des Systems ------------------

		/* -------- Benutzersession simulieren ------ */
		// Passwort vom Benutzer holen
		System.out.println("Starte Login-Session für Benutzer: " + userName);
		password = thisSession.readPasswd(userName);
		if (password != null) {

			// Benutzeranmeldung beim KDC
			boolean loginOK = thisSession.myClient.login(userName, password);

			// Passwort im Hauptspeicher löschen (überschreiben)!!
			Arrays.fill(password, ' ');

			if (!loginOK) {
				System.out.println("Login fehlgeschlagen!");
			} else {
				System.out.println("Login erfolgreich!\n");

				// Zugriff auf Fileserver
				boolean serviceOK = thisSession.myClient.showFile(thisSession.myFileserver, filePath);
				if (!serviceOK) {
					System.out.println("Zugriff auf Server " + serverName + " ist fehlgeschlagen!");
				}
			}
		}
	}
}
