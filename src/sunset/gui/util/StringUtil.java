package sunset.gui.util;

import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;

import java.math.BigInteger;

public class StringUtil {

    private static final StringUtil instance = new StringUtil();

    private StringUtil() {

    }

    public static StringUtil getInstance() {
        return instance;
    }

    public boolean isBlank(String str) {
        if (str == null)
            return true;

        if (str.trim().length() == 0)
            return true;

        return false;
    }

    public static String toScientific(BigInteger i) {
        if (i != null) {
            String string = i.toString();
            return toScientific(string);
        }
        return "null";
    }

    public static String toScientific(String s) {
        if (s != null) {
            int length = s.length();
            int threshold = 10;

            try {
                threshold = Integer.parseInt(GUIPropertiesLogic.getInstance().getProperty(IProperties.DEBUGGER_INTEGER_THRESHOLD));
            } catch (NumberFormatException ignored) {
            }
            if (length > threshold) {
                return s.charAt(0) + "." + s.charAt(1) + "E" + (s.length() - 1);
            } else {
                return s;
            }
        }
        return "null";
    }
}