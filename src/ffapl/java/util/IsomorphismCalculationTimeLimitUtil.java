package ffapl.java.util;

import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;

public class IsomorphismCalculationTimeLimitUtil {

    /**
     * Returns isomorphism time limit in seconds from Property file
     * 10 seconds is default if nothing is found
     * @return
     */
    // TODO - DOMINIC - 05.01.2022: Maybe this property should not be accessed in the ffapl.java package but rather be passed to FFaplInterpreter constructor from the outside
    public static int getIsomorphismCalculationTimeLimitInSeconds() {
        try {
            Integer timeLimitInSeconds =
                    GUIPropertiesLogic.getInstance().getIntegerProperty(IProperties.ISOMORPHISM_CALCULATION_TIME_LIMIT);
            if (timeLimitInSeconds == null || timeLimitInSeconds <= 0) {
                return 10;
            }
            return timeLimitInSeconds;
        } catch (Exception ex) {
            return 10;
        }
    }
}
