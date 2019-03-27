/**
 * 
 */
package sunset.gui.interfaces;

import java.util.prefs.BackingStoreException;

import sunset.gui.logic.GUIPropertiesLogic;

/**
 * @author Alexander Ortner
 * @version 1.0
 * 
 */
public class IProperties {

	public static String PROPERTYFILEPATH = "sunset/gui/gui.properties";
	public static String PROPERTYFILE_PATH_USER = getUserHomePath()
			+ "sunset_user.properties";
	public static String PROPERTYFILE_DIRPATH_USER = getUserHomePath();
	public final static String LANGUAGE = "LANGUAGE";
	public final static String FILEEXTENTION = ".ffapl";
	public final static String LASTDIROPEN = "LAST.DIR.OPEN";
	public final static String LASTDIRSAVE = "LAST.DIR.SAVED";
	public final static String GUIFontFamily = "Sans-Serif";
	public final static String IMAGEPATH = "sunset/gui/images/";
	public final static String APPTITLE = "FFapl {0} Sunset";
	public final static String APPVERSION = "2.0";
	public final static String SHOW_API = "SHOW.API";
	public final static String GUI_WIDTH = "GUI.WIDTH";
	public final static String GUI_HEIGHT = "GUI.HEIGHT";
	public final static String GUI_DIVIDER_CONSOLE = "DIVIDER.CONSOLE";
	public final static String GUI_DIVIDER_API = "DIVIDER.API";
	public final static String GUI_MAXIMIZED = "GUI.MAXIMIZED";
	public final static String LOGGER_MODE = "LOGGER.MODE";
	public final static String SYSTEM = "SYSTEM";

	public static String getUserHomePath() {
		return System.getProperty("user.home")
				+ System.getProperty("file.separator") + ".sunset"
				+ System.getProperty("file.separator");
	}
}
