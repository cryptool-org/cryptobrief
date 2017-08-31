package sunset.gui.api.xml;

import java.io.InputStream;

public class XmlLoader {

	private static XmlLoader instance = new XmlLoader();
	
	public static XmlLoader getInstance(){
		return instance;
	}
	
	public InputStream getResource(String fileName) {
		return getClass().getResourceAsStream(fileName);
	}

}
