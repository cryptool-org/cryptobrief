package ffapl.java.math.isomorphism.calculation.linearfactor;

import ffapl.java.classes.GaloisField;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;

/**
 * Interface for different strategies of finding a root of "f" in "f_q".
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public interface RootFindingStrategy {

    PolynomialRC findRootOf(PolynomialRC f, GaloisField f_q) throws FFaplAlgebraicException;
}
