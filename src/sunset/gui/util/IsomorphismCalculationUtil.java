package sunset.gui.util;

import ffapl.java.math.isomorphism.calculation.linearfactor.RootFindingStrategyType;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;

public class IsomorphismCalculationUtil {

    /**
     * Returns root finding strategy type for isomorphism calculation from Property file.
     * Default value is the RandomRootFindingStrategy
     * @return root finding strategy
     */
    public static RootFindingStrategyType getRootFindingStrategyType() {
        try {
            return RootFindingStrategyType.valueOf(GUIPropertiesLogic.getInstance()
                    .getProperty(IProperties.ISOMORPHISM_CALCULATION_ROOT_FINDING_STRATEGY));
        } catch (Exception ex) {
            return RootFindingStrategyType.defaultElement();
        }
    }

    /**
     * Returns isomorphism time limit in seconds from Property file
     * 10 seconds is default if nothing is found
     * @return
     */
    public static int getTimeLimitInSeconds() {
        try {
            Integer timeLimitInSeconds = GUIPropertiesLogic.getInstance()
                    .getIntegerProperty(IProperties.ISOMORPHISM_CALCULATION_TIME_LIMIT);
            if (timeLimitInSeconds == null || timeLimitInSeconds <= 0) {
                return 10;
            }
            return timeLimitInSeconds;
        } catch (Exception ex) {
            return 10;
        }
    }
}
