package ffapl.java.math.isomorphism;

import ffapl.java.classes.GaloisField;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.math.isomorphism.calculation.IsomorphismCalculation;

/**
 * Maps an element of a Galois Field to another field which is isomorphic to the first one
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class GaloisFieldIsomorphismCast {

    private final IsomorphismCalculation _isomorphismCalculation;

    public GaloisFieldIsomorphismCast(IsomorphismCalculation isomorphismCalculation) {
        _isomorphismCalculation = isomorphismCalculation;
    }

    /**
     * This method calculates an isomorphism - if possible - between the source field and the target field and
     * then applies said isomorphism to the source field element, mapping it to an element of the target field.
     * This resulting element is then returned
     *
     * @param sourceFieldAndElement source field and the element to which an isomorphism is to be applied
     * @param targetField target field of the isomorphism
     * @return transformed field element
     * @throws FFaplAlgebraicException when operations on underlying objects fail
     */
    public GaloisField cast(GaloisField sourceFieldAndElement, GaloisField targetField) throws FFaplAlgebraicException {
        return _isomorphismCalculation
                .calculate(sourceFieldAndElement, targetField)
                .applyTo(sourceFieldAndElement);
    }
}
