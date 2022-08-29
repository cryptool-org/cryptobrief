package ffapl.java.math.isomorphism.calculation;

import ffapl.java.classes.GaloisField;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.math.isomorphism.Isomorphism;
import ffapl.java.math.isomorphism.calculation.cache.IsomorphismCache;

/**
 * This class uses an internal IsomorphismCalculation object to calculate an isomorphism between two isomorphic finite
 * fields and stores the calculated result in a cache for later reuse.
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class CachingIsomorphismCalculation implements IsomorphismCalculation {

    private final IsomorphismCalculation _isomorphismCalculation;
    private final IsomorphismCache _cache;

    public CachingIsomorphismCalculation(IsomorphismCalculation isomorphismCalculation, IsomorphismCache cache) {
        _isomorphismCalculation = isomorphismCalculation;
        _cache = cache;
    }

    /**
     * Checks the cache whether an isomorphism between the source field and the target field has been calculated and stored before.
     * If not, the internal IsomorphismCalculation object is used to calculate such an isomorphism and then store it for
     * later reuse.
     * @param sourceField source field
     * @param targetField target field
     * @return Isomorphism
     * @throws FFaplAlgebraicException if underlying operations fail
     */
    @Override
    public Isomorphism calculate(GaloisField sourceField, GaloisField targetField) throws FFaplAlgebraicException {
        if (_cache.containsIsomorphismBetween(sourceField, targetField)) {
            return _cache.getIsomorphismBetween(sourceField, targetField);
        } else {
            Isomorphism isomorphism = _isomorphismCalculation.calculate(sourceField, targetField);
            _cache.putIsomorphismBetween(sourceField, targetField, isomorphism);
            return isomorphism;
        }
    }
}
