package ffapl.java.math.isomorphism.calculation;

import ffapl.exception.FFaplException;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.isomorphism.Isomorphism;
import ffapl.java.math.isomorphism.calculation.linearfactor.RootFindingStrategy;

import java.math.BigInteger;

/**
 * This class calculates an isomorphism between two isomorphic finite fields by first searching for a root
 * of the source fields' irreducible polynomial in the target field. This root is then used to
 * calculate an isomorphism (see Bach, Eric, et al. Algorithmic number theory: Efficient algorithms. Vol. 1. MIT press, 1996 on page 173)
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class LinearFactorIsomorphismCalculation implements IsomorphismCalculation {

    private final RootFindingStrategy _rootFindingStrategy;

    public LinearFactorIsomorphismCalculation(RootFindingStrategy rootFindingStrategy) {
        this._rootFindingStrategy = rootFindingStrategy;
    }

    /**
     * Calculates isomorphism using a root of the source fields' irreducible polynomial in the
     * target field which is found by a root finding strategy passed to the constructor.
     * @param sourceField source field
     * @param targetField target field
     * @return Isomorphism
     * @throws FFaplAlgebraicException if underlying operations fail
     */
    @Override
    public Isomorphism calculate(GaloisField sourceField, GaloisField targetField) throws FFaplAlgebraicException {
        return new Isomorphism(
                sourceField,
                targetField,
                calculatePhiFromRoot(
                        _rootFindingStrategy.findRootOf(sourceField.irrPolynomial(), targetField), targetField),
                targetField.getThread());
    }

    /**
     * Calculates isomorphism transformation matrix "phi" from field A to field B given "alpha" which is a root
     * of the source field A's irreducible polynomial in the target field B.
     * I.e. irred_ply_A(alpha) = 0 mod irred_ply_B.
     * "Phi" is a square m x m - matrix with m = deg(irred_ply_B).
     * Also Phi = [phi_1  phi_2 ... phi_m] = [alpha^0  alpha^1 ... alpha^(m-1)] all terms mod irred_ply_B
     * @param alpha the alpha in (X - alpha), where (X - alpha) is a linear factor of the source fields' irreducible polynomial in the target field.
     * @param targetField target field
     * @return Matrix<BInteger> phi
     * @throws FFaplAlgebraicException if underlying operations fail
     */
    private Matrix<BInteger> calculatePhiFromRoot(Polynomial alpha, GaloisField targetField) throws FFaplAlgebraicException {
        int matrixSize = targetField.irrPolynomial().degree().intValue();
        BInteger[][] phi = new BInteger[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            Polynomial currentResult = alpha.powR(BigInteger.valueOf(i));
            PolynomialRC currentResultRC = new PolynomialRC(
                    currentResult.polynomial(),
                    targetField.characteristic(),
                    targetField.getThread());
            currentResultRC.mod(targetField.irrPolynomial());

            for (int j = 0; j < matrixSize; j++) {
                phi[j][i] = (BInteger) currentResultRC.coefficientAt(BigInteger.valueOf(j));
            }
        }

        try {
            return new Matrix<>(phi, BInteger.valueOf(0, targetField.getThread()));
        } catch (FFaplException e) {
            throw new FFaplAlgebraicException(new Object[]{"isomorphism calculation"}, IAlgebraicError.INTERNAL);
        }
    }

    @Override
    public String toString() {
        return "LinearFactorIsomorphismCalculation with " + _rootFindingStrategy;
    }
}
