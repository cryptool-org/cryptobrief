package sunset.gui.enums;

import java.util.Locale;

public enum Language {
	GERMAN("de"),
	ENGLISH("en");
	
	private Locale locale;
	
	private Language(String lang){
		locale = new Locale(lang);
	}
	
	public Locale getLocale(){
		return locale;
	}
}
