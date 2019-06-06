package sunset.gui.util;

public class StringUtil {
	
	private static final StringUtil instance = new StringUtil();
	
	private StringUtil() {
		
	}
	
	public static boolean isBlank(String str) {
		if (str == null)
			return true;
		
		if (str.trim().length() == 0)
			return true;
		
		return false;
	}

}
