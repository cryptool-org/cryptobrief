package sunset.gui.api.binary;

import java.io.InputStream;

public class BinaryFileLocator {

	private static BinaryFileLocator instance = new BinaryFileLocator();
	
	public static BinaryFileLocator getInstance(){
		return instance;
	}
	
	public InputStream getResource(String fileName) {
		return getClass().getResourceAsStream(fileName);
	}
}
