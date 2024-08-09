package ffapl.java.math.isomorphism.calculation;

import ffapl.java.classes.GaloisField;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.math.isomorphism.Isomorphism;

/**
 * Interface for different strategies of calculating an isomorphism between finite fields.
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public interface IsomorphismCalculation {

    Isomorphism calculate(GaloisField sourceField, GaloisField targetField) throws FFaplAlgebraicException;
}
