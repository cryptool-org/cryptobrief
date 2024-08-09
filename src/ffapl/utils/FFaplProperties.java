package ffapl.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import ffapl.java.enums.Language;

public class FFaplProperties {
	
	private static final String BASENAME = "ffapl.bundles.ffapl";
	
	private Locale locale = Language.GERMAN.getLocale();
	
	private static FFaplProperties instance = new FFaplProperties();
	
	public static FFaplProperties getInstance(){
		return instance;
	}
	
	private FFaplProperties(){
		
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

	public String getProperty(int errorNr) {
		return getProperty(String.valueOf(errorNr));
	}
}
