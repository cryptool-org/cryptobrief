package ffapl.java.math.isomorphism.calculation.linearfactor;

import ffapl.java.classes.BInteger;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Searching for a root by randomly trying out different polynomials.
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class RandomRootFindingStrategy implements RootFindingStrategy {

    /**
     * Creates random polynomials "alpha" with deg(alpha) < deg(f_q) and char(alpha) = char(f_q) until one
     * "alpha" is found such that f(alpha) = 0 in f_q.
     * Polynomials which did not work before are stored in a set. In every iteration, polynomials are created randomly
     * until one is found which is not yet in the set. If the amount of elements in the set is the order of the field,
     * the algorithm terminates, and returns null, since no root has been found and every element of the field has
     * been tested.
     * @param f which is the polynomial for which a root of f in the field f_q is to be found
     * @param f_q which is the field containing the root a
     * @return alpha, such that f(alpha) = 0 in f_q
     * @throws FFaplAlgebraicException if underlying operations fail
     */
    @Override
    public PolynomialRC findRootOf(PolynomialRC f, GaloisField f_q) throws FFaplAlgebraicException {
        Set<PolynomialRC> randomElementsCache = new HashSet<>();
        while (BigInteger.valueOf(randomElementsCache.size()).compareTo(f_q.order()) < 0 ) {
            PolynomialRC alpha;

            do {
                alpha = Algorithm.getRandomPolynomial(
                        new BInteger(f_q.irrPolynomial().degree().subtract(BigInteger.ONE), f_q.getThread()),
                        new BInteger(f_q.characteristic(), f_q.getThread()),
                        false);
            } while (randomElementsCache.contains(alpha));

            PolynomialRC currentResult = f.calculate(alpha);
            currentResult.mod(f_q.irrPolynomial());

            if (currentResult.isZero()) {
                return alpha;
            }
            randomElementsCache.add(alpha);
        }
        return null;
    }

    @Override
    public String toString() {
        return "RandomRootFindingStrategy";
    }
}


