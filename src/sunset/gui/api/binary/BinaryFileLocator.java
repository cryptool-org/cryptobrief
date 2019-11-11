package sunset.gui.api.binary;

import java.io.InputStream;

public class BinaryFileLocator {

	private static BinaryFileLocator instance = new BinaryFileLocator();
	
	public static BinaryFileLocator getInstance(){
		return instance;
	}
	
	/*
	 * @param fileName the file name which should be located
	 * @returns the corresponding InputStream object of the specified file
	 */
	public InputStream getResource(String fileName) {
		return getClass().getResourceAsStream(fileName);
	}
}
