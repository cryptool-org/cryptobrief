package sunset.gui.util;

import java.util.Locale;
import java.util.ResourceBundle;

import sunset.gui.enums.Language;

public class SunsetBundle {
	
	private static final String BASENAME = "sunset.bundles.sunset";
	
	private Locale locale = Language.GERMAN.getLocale();
	
	private static SunsetBundle instance = new SunsetBundle();
	
	public static SunsetBundle getInstance(){
		return instance;
	}
	
	private SunsetBundle(){
		
	}
	
	public String getProperty(String key, Locale locale){
		String result = "";
		if(!StringUtil.getInstance().isBlank(key)){
			try{
				result = ResourceBundle.getBundle(BASENAME, locale).getString(key);
			}catch (Exception e) {
				result = key;
			}
		}
		return result;
	}
	
	public String getProperty(String key){
		return getProperty(key, locale);
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}
	
}
