package org.haw.its.praktikum3.fileio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class KeyFileReader extends BaseFolderFileWriter_A {
	public KeyFileReader() throws FileNotFoundException {
		super();
	}
	
	public KeyFileReader(String baseFolder) throws FileNotFoundException {
		super(baseFolder);
	}
	
	public KeyFileReader(File baseFolder) throws FileNotFoundException {
		super(baseFolder);
	}
	
	/**
	 * Liest Schlüsselinformationen aus einer Datei mit dem Namen {@code <user>.<fileSuffix>.txt} in dem Verzeichnis von
	 * {@link #getBaseFolder()}.
	 * @param user Ein Benutzername nach {@code [a-zA-Z0-9]*}
	 * @param fileSuffix Die Dateiendung nach {@code [a-zA-Z0-9]*}
	 * @return Der Schlüssel
	 * @throws IllegalArgumentException Wenn der Name aus der Datei nicht mit dem übergebenen Namen übereinstimmt
	 * @throws FileNotFoundException Wenn die Datei nicht gefunden werden konnte
	 * @throws IOException Wenn ein Fehler beim Lesen der Datei aufgetreten ist
	 */
	public byte[] readKeyFile(String user, String fileSuffix) throws FileNotFoundException, IOException {
		checkString(user);
		checkString(fileSuffix);
		
		File inputFile = new File(getBaseFolder(), user + "." + fileSuffix + ".txt");
		try(DataInputStream is = new DataInputStream(new FileInputStream(inputFile))) {
			int len = is.readInt();
			byte[] userBytes = new byte[len];
			is.read(userBytes);
			
			String userFromFile = new String(userBytes);
			if(!userFromFile.equals(user)) {
				throw new IllegalArgumentException("Der Benutzername aus der Datei '" + userFromFile + "' stimmte nicht mit dem erwarteten Namen '" + user + "' überein!");
			}
			
			len = is.readInt();
			byte[] key = new byte[len];
			is.read(key);
			
			return key;
		}
	}
	
	private void checkString(String string) {
		if(!Pattern.matches("[a-zA-Z0-9]*", Objects.requireNonNull(string))) {
			throw new IllegalArgumentException("Der String '" + string + "' entspricht nicht dem Muster '[a-zA-Z0-9]*'!");
		}
	}
}
