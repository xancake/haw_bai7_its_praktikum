package org.haw.its.praktikum3;

import java.util.Objects;
import java.util.Scanner;

public class ParameterData {
	public final String _sender;
	public final String _receiver;
	public final String _inputFile;
	public final String _outputFile;
	
	public ParameterData(String sender, String receiver, String fileToEncrypt, String encryptedOutputFile) {
		_sender = Objects.requireNonNull(sender);
		_receiver = Objects.requireNonNull(receiver);
		_inputFile = Objects.requireNonNull(fileToEncrypt);
		_outputFile = Objects.requireNonNull(encryptedOutputFile);
	}
	
	/**
	 * Erzeugt ein {@link ParameterData}-Objekt aus den Übergabeparametern. Die Einträge entsprechen folgenden Datenfeldern:
	 * <ul>
	 * <li>args[0] -> Sender
	 * <li>args[1] -> Empfänger
	 * <li>args[2] -> Eingabedatei
	 * <li>args[3] -> Ausgabedatei
	 * </ul>
	 * Wenn ein leeres Übergabeparameter-Array übergeben wird, erhält der Benutzer eine Abfrage über den Standart-
	 * Ein-/-Ausgabe-Strom.
	 * </pre>
	 * @param args Die Übergabeparameter, muss {@code args.length == 4} oder {@code args.length == 0} sein
	 * @return Ein {@link ParameterData}-Objekt oder {@code null}, wenn keins geparsed werden konnte
	 */
	public static ParameterData fromCommandLine(String[] args) {
		switch(args.length) {
			case 0: {
				try(Scanner key = new Scanner(System.in)) {
					System.out.print("Sender: ");
					String sender = key.nextLine();
					System.out.print("Empfänger: ");
					String receiver = key.nextLine();
					System.out.print("Eingabedatei (zu ver-/entschlüsseln): ");
					String fileToEncrypt = key.nextLine();
					System.out.print("Ausgabedatei (ver-/entschlüsselt): ");
					String encryptedOutputFile = key.nextLine();
					return new ParameterData(sender, receiver, fileToEncrypt, encryptedOutputFile);
				}
			}
			case 4: {
				return new ParameterData(args[0], args[1], args[2], args[3]);
			}
			default: {
				return null;
			}
		}
	}
}
