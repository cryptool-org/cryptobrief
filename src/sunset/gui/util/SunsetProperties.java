package sunset.gui.util;

import java.util.Locale;
import java.util.ResourceBundle;

import ffapl.java.enums.Language;

public class SunsetProperties {
	
	private static final String BASENAME = "sunset.bundles.sunset";
	
	private Locale locale = Language.GERMAN.getLocale();
	
	private static SunsetProperties instance = new SunsetProperties();
	
	public static SunsetProperties getInstance(){
		return instance;
	}
	
	private SunsetProperties(){
		
	}
	
	public String getProperty(String key, Locale locale){
		return ResourceBundle.getBundle(BASENAME, locale).getString(key);
	}
	
	public String getProperty(String key){
		return ResourceBundle.getBundle(BASENAME, locale).getString(key);
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}
	
}
