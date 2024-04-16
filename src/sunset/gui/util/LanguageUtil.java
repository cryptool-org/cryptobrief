package sunset.gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ffapl.java.enums.Language;

public class LanguageUtil {

	public static List<Locale> getAvailableLanguages(){
		List<Locale> locales = new ArrayList<Locale>();
		locales.add(Language.GERMAN.getLocale());
		locales.add(Language.ENGLISH.getLocale());
		return locales;
	}
}
