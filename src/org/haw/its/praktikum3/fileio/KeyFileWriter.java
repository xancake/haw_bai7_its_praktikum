package org.haw.its.praktikum3.fileio;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class KeyFileWriter extends BaseFolderFileWriter_A {
	public KeyFileWriter() throws FileNotFoundException {
		super();
	}
	
	public KeyFileWriter(String baseFolder) throws FileNotFoundException {
		super(baseFolder);
	}
	
	public KeyFileWriter(File baseFolder) throws FileNotFoundException {
		super(baseFolder);
	}
	
	/**
	 * Schreibt die Schlüsselinformationen und den Benutzernamen in eine Datei mit dem Namen
	 * {@code <user>.<fileSuffix>.txt} in dem Verzeichnis von {@link #getBaseFolder()}.
	 * @param user Ein Benutzername nach {@code [a-zA-Z0-9]*}
	 * @param key Der Schlüssel
	 * @param fileSuffix Die Dateiendung nach {@code [a-zA-Z0-9]*}
	 * @throws FileNotFoundException Wenn die Datei nicht gefunden werden konnte
	 * @throws IOException Wenn ein Fehler beim Schreiben der Datei aufgetreten ist
	 */
	public void writeKeyFile(String user, byte[] key, String fileSuffix) throws FileNotFoundException, IOException {
		checkString(user);
		Objects.requireNonNull(key);
		checkString(fileSuffix);
		
		File outputFile = new File(getBaseFolder(), user + "." + fileSuffix + ".txt");
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(outputFile))) {
			os.writeInt(user.length());
			os.write(user.getBytes());
			os.writeInt(key.length);
			os.write(key);
		}
	}
	
	private void checkString(String string) {
		if(!Pattern.matches("[a-zA-Z0-9]*", Objects.requireNonNull(string))) {
			throw new IllegalArgumentException("Der String '" + string + "' entspricht nicht dem Muster '[a-zA-Z0-9]*'!");
		}
	}
}
