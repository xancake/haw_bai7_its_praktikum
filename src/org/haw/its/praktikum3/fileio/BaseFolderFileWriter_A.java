package org.haw.its.praktikum3.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class BaseFolderFileWriter_A {
	private File _baseFolder;
	
	public BaseFolderFileWriter_A() throws FileNotFoundException {
		this(".");
	}
	
	public BaseFolderFileWriter_A(String baseFolder) throws FileNotFoundException {
		this(new File(Objects.requireNonNull(baseFolder)));
	}
	
	public BaseFolderFileWriter_A(File baseFolder) throws FileNotFoundException {
		if(!Objects.requireNonNull(baseFolder).exists()) {
			throw new FileNotFoundException("Der angegebene Basisordner konnte nicht gefunden werden: " + baseFolder.getAbsolutePath());
		}
		_baseFolder = baseFolder;
	}
	
	public final File getBaseFolder() {
		return _baseFolder;
	}
}
