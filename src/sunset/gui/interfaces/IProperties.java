/**
 * 
 */
package sunset.gui.interfaces;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Alexander Ortner
 * @version 1.0
 * 
 */
public class IProperties {

	public static String PROPERTYFILEPATH = "sunset/gui/gui.properties";
	public static String PROPERTYFILE_PATH_INSTALL = getInstallPath() + "sunset.properties";
	public static String PROPERTYFILE_PATH_USER = getUserHomePath()	+ "sunset_user.properties";
	public static String PROPERTYFILE_DIRPATH_USER = getUserHomePath();
	public final static String LANGUAGE = "LANGUAGE";
	public final static String FILEEXTENTION = ".ffapl";
	public final static String LASTDIROPEN = "LAST.DIR.OPEN";
	public final static String LASTDIRSAVE = "LAST.DIR.SAVED";
	public final static String GUIFontFamily = "Sans-Serif";
	public final static String IMAGEPATH = "sunset/gui/images/";
	public final static String APPTITLE = "FFapl {0} Sunset";
	public final static String APPVERSION = "2.1";
	public final static String SHOW_API = "SHOW.API";
	public final static String GUI_WIDTH = "GUI.WIDTH";
	public final static String GUI_HEIGHT = "GUI.HEIGHT";
	public final static String GUI_DIVIDER_CONSOLE = "GUI.DIVIDER.CONSOLE";
	public final static String GUI_DIVIDER_API = "GUI.DIVIDER.API";
	public final static String GUI_MAXIMIZED = "GUI.MAXIMIZED";
	public final static String LOGGER_MODE = "LOGGER.MODE";
	public final static String SYSTEM = "SYSTEM";

	public static String getInstallPath() {
		String installPath = "";
		URL url = getURL(IProperties.class);

		if (url == null)
			return installPath;
		
		try {
			File file = new File(url.toURI());
			installPath = file.getParentFile().getPath() + System.getProperty("file.separator");
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException in load properties. Failed to create file from URL.");
		}
		
		return installPath;
	}
	
	private static URL getURL(final Class<?> clazz) {
		if (clazz == null)
			return null;
		
		try {
			URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
			
			if (url != null)
				return url;
			
		} catch (SecurityException e) {	
			System.out.println("SecurityException in load properties. Failed to get path from protection domain.");
		}
		
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");
		
		if (url == null)
			return null;
		
		String urlString = url.toString();
		
		if (urlString.startsWith("jar:") && urlString.contains(".jar")) {
			urlString = urlString.substring(4, urlString.indexOf(".jar") + 4);
			
			try {
				URL newURL = new URL(urlString);
				
				return newURL;
			} catch (MalformedURLException e) {
				System.out.println("MalformedURLException in load properties. Failed to get path from resource.");
			}
		}
		
		return null;
	}
	
	public static String getUserHomePath() {
		return System.getProperty("user.home")
				+ System.getProperty("file.separator") + ".sunset"
				+ System.getProperty("file.separator");
	}
}
