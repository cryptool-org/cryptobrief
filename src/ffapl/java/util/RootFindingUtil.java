package ffapl.java.util;

import ffapl.java.math.isomorphism.calculation.linearfactor.RootFindingStrategyType;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;

public class RootFindingUtil {

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
}
