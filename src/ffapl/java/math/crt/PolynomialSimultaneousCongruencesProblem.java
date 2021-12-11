package ffapl.java.math.crt;

import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;

import java.math.BigInteger;

import static ffapl.java.math.Algorithm.*;
import static java.math.BigInteger.ONE;

/**
 * Represents a problem were simultaneous congruences need to be solved, which is done via the Chinese Remainder Theorem
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class PolynomialSimultaneousCongruencesProblem {

    private final PolynomialRC[] congruences;
    private final PolynomialRC[] moduli;
    private final Thread thread;

    private final BigInteger characteristic;

    /**
     * Constructor for the problem of solving simultaneous congruences.
     * @param congruences
     * @param moduli
     * @return
     * @throws FFaplAlgebraicException
     *         <VALUE_IS_NULL> if either "congruences" or "moduli" is null
     *         <CRT_DIFFERENT_ARRAY_LENGTHS> if lengths of  "congruences" or "moduli" arrays differ.
     *         <CRT_ARRAYS_EMPTY> if "congruences" or "moduli" arrays are empty.
     *         <CRT_ZERO_OR_NEGATIVE_MODULES> if "moduli" array contains values <= 0
     *         <CRT_CHARACTERISTICS_MISMATCH> if "moduli" characteristics != "congruences" characteristics.
     */
    public PolynomialSimultaneousCongruencesProblem(PolynomialRC[] congruences, PolynomialRC[] moduli) throws FFaplAlgebraicException {
        if (congruences == null) {
            throw new FFaplAlgebraicException(new Object[] {"Array of congruences"}, IAlgebraicError.VALUE_IS_NULL);
        }
        if (moduli == null) {
            throw new FFaplAlgebraicException(new Object[] {"Array of moduli"}, IAlgebraicError.VALUE_IS_NULL);
        }

        this.congruences = congruences;
        this.moduli = moduli;

        if (congruences.length != moduli.length) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_DIFFERENT_ARRAY_LENGTHS);
        }
        if (congruences.length == 0) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_ARRAY_EMPTY);
        }

        this.thread = congruences[0].getThread();
        this.characteristic = congruences[0].characteristic();
        if (!this.characteristic.equals(moduli[0].characteristic())) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_CHARACTERISTICS_MISMATCH);
        }
        for (PolynomialRC currentModule : moduli) {
            isRunning(thread);
            if (currentModule.isZero()) {
                throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_ZERO_OR_NEGATIVE_MODULES);
            }
        }
    }

    /**
     * Solving simultaneous congruences with given congruences and moduli. Calculates the value which solves those congruences
     * using the Chinese remainder theorem or throws an exception if no such value exists.
     * Uses different solution strategies based on whether the moduli are pairwise coprime or not.
     * @throws FFaplAlgebraicException
     *          <CRT_POLYNOMIAL_NONCOPRIME_NOTIMPLEMENTED> currently for non-coprime moduli, since this is case is not yet implemented for polynomials.
     */
    public Polynomial solve() throws FFaplAlgebraicException {
        if (valuesArePairwiseCoprime(moduli)) {
            return solveForCoprimeModuli();
        } else {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_POLYNOMIAL_NONCOPRIME_NOTIMPLEMENTED);
        }
    }

    /**
     * Since moduli are pairwise coprime, the simultaneous congruences can be solved directly
     * @throws FFaplAlgebraicException
     *          <INTERRUPT> if thread is interrupted
     */
    private PolynomialRC solveForCoprimeModuli() throws FFaplAlgebraicException {
        PolynomialRC moduliProduct = productSum(moduli);
        PolynomialRC result = new PolynomialRC(BigInteger.ZERO, BigInteger.ZERO, characteristic, thread);
        PolynomialRC n, tmp;
        for (int i = 0; i < moduli.length; i++) {
            isRunning(thread);
            n = PolynomialRC.divide(moduliProduct, moduli[i])[0];
            tmp = eea(n, moduli[i])[1];
            tmp.multiply(n);
            tmp.multiply(congruences[i]);
            tmp.mod(moduliProduct);
            result.add(tmp);
        }
        return result;
    }

    /**
     * Returns all values multiplied together, "1" if array is empty
     * @throws FFaplAlgebraicException
     * 			<INTERRUPT> if thread is interrupted
     */
    private PolynomialRC productSum(PolynomialRC[] values) throws FFaplAlgebraicException {
        PolynomialRC result = new PolynomialRC(ONE, ONE, characteristic, thread);
        for (PolynomialRC value : values) {
            isRunning(thread);
            result.multiply(value);
        }
        return result;
    }

    /**
     * Returns "true" if values are coprime or array is empty, "false" otherwise
     * @throws FFaplAlgebraicException
     * 			<INTERRUPT> if thread is interrupted
     */
    public boolean valuesArePairwiseCoprime(PolynomialRC[] values) throws FFaplAlgebraicException {
        if(values.length == 0) {
            return true;
        }

        Thread thread = values[0].getThread();
        for (int i = 0; i < values.length; i++) {
            for (int j = i+1; j < values.length; j++) {
                isRunning(thread);
                if (!gcd(values[i], values[j]).isOne()) {
                    return false;
                }
            }
        }
        return true;
    }
}
