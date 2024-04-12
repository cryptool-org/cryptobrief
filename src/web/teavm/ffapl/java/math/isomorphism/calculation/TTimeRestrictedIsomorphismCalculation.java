package web.teavm.ffapl.java.math.isomorphism.calculation;

import ffapl.java.classes.GaloisField;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.isomorphism.Isomorphism;
import ffapl.java.math.isomorphism.calculation.IsomorphismCalculation;

/**
 * This class uses an internal IsomorphismCalculation object to calculate an isomorphism between two isomorphic finite
 * fields but wraps this operation in such a way that it is aborted if no isomorphism has been found in a given amount
 * of time
 */
public class TTimeRestrictedIsomorphismCalculation implements IsomorphismCalculation {

    private final IsomorphismCalculation _isomorphismCalculation;
    private final long _maximumSecondsForCalculation;

    public TTimeRestrictedIsomorphismCalculation(IsomorphismCalculation isomorphismCalculation, long maximumSecondsForCalculation) {
        _isomorphismCalculation = isomorphismCalculation;
        _maximumSecondsForCalculation = maximumSecondsForCalculation;
    }

    @Override
    public Isomorphism calculate(GaloisField sourceField, GaloisField targetField) throws FFaplAlgebraicException {
        Object[] tmpArguments = {"implicit isomorphism cast not supported"};
        throw new FFaplAlgebraicException(tmpArguments, IAlgebraicError.INTERNAL);
    }

}
