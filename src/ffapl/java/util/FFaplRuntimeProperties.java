package ffapl.java.util;

import ffapl.java.math.isomorphism.calculation.linearfactor.RootFindingStrategyType;

public class FFaplRuntimeProperties {

    private RootFindingStrategyType isomophismCalculationRootFindingStrategyType;
    private int isomophismCalculationTimeLimit;

    public FFaplRuntimeProperties(RootFindingStrategyType isomophismCalculationRootFindingStrategyType, int isomophismCalculationTimeLimit) {
        this.isomophismCalculationRootFindingStrategyType = isomophismCalculationRootFindingStrategyType;
        this.isomophismCalculationTimeLimit = isomophismCalculationTimeLimit;
    }

    public static FFaplRuntimeProperties defaults() {
        return new FFaplRuntimeProperties(RootFindingStrategyType.defaultElement(), 10);
    }

    public RootFindingStrategyType getIsomorphismCalculationRootFindingStrategyType() {
        return this.isomophismCalculationRootFindingStrategyType;
    }

    public int getIsomorphismCalculationTimeLimit() {
        return this.isomophismCalculationTimeLimit;
    }
}
