package ffapl.java.util;

import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;
import ffapl.java.enums.LoggerMode;

public class LoggerUtil {

	/**
	 * Returns logger Mode from Property file
	 * ALL is default if nothing is found
	 * @return
	 */
	public static LoggerMode getLoggerMode() {
		Integer mode = GUIPropertiesLogic.getInstance().getIntegerProperty(IProperties.LOGGER_MODE);
		for(LoggerMode lMode : LoggerMode.values()){
			if(lMode.getMode() == mode){
				return lMode;
			}
		}
		return LoggerMode.ALL;
	}

}
