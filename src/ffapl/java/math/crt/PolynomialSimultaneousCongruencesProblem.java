package ffapl.java.math.crt;

import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ffapl.java.math.Algorithm.*;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

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
     * using the Chinese remainder theorem or throws an exception if no solution exists.
     * If there are pairs of non-coprime moduli, those are tried to be solved directly. As soon as all moduli are coprime,
     * the CRT is applied directly.
     * @throws FFaplAlgebraicException
     */
    public PolynomialRC solve() throws FFaplAlgebraicException {
        List<PolynomialRC> currentCongruences = new ArrayList<>(Arrays.asList(congruences));
        List<PolynomialRC> currentModuli = new ArrayList<>(Arrays.asList(moduli));

        int[] currentNonCoprimeModuliIndices;

        // As long as congruence-moduli pairs with non-coprime moduli are found ...
        while ((currentNonCoprimeModuliIndices = findTwoNonCoprimeModuli(currentModuli)) != null) {
            int firstModuleIndex = currentNonCoprimeModuliIndices[0];
            int secondModuleIndex = currentNonCoprimeModuliIndices[1];

            // TODO - DOMINIC - 14.12.2021: Works, but could use some refactoring
            PolynomialRC firstCongruence = currentCongruences.get(firstModuleIndex);
            PolynomialRC secondCongruence = currentCongruences.get(secondModuleIndex);
            PolynomialRC firstModule = currentModuli.get(firstModuleIndex);
            PolynomialRC secondModule = currentModuli.get(secondModuleIndex);

            // ... try to solve them directly, and ...
            PolynomialRC[] directCongruenceResult = solveSimultaneousCongruenceDirectlyFor(
                    firstCongruence, firstModule, secondCongruence, secondModule);

            // ... replace the original congruence equation with the solution acquired in the previous step.
            currentCongruences.remove(firstCongruence);
            currentCongruences.remove(secondCongruence);
            currentModuli.remove(firstModule);
            currentModuli.remove(secondModule);

            currentCongruences.add(directCongruenceResult[0]);
            currentModuli.add(directCongruenceResult[1]);
        }

        return solveForCoprimeModuli(currentCongruences, currentModuli);
    }

    /**
     * Since moduli are pairwise coprime, the simultaneous congruences can be solved directly
     * @throws FFaplAlgebraicException
     *          <INTERRUPT> if thread is interrupted
     */
    private PolynomialRC solveForCoprimeModuli(List<PolynomialRC> congruences, List<PolynomialRC> moduli) throws FFaplAlgebraicException {
        PolynomialRC moduliProduct = productSum(moduli);
        PolynomialRC result = new PolynomialRC(ZERO, ZERO, characteristic, thread);
        PolynomialRC n, tmp;
        for (int i = 0; i < moduli.size(); i++) {
            isRunning(thread);
            n = PolynomialRC.divide(moduliProduct, moduli.get(i))[0];
            tmp = eea(n, moduli.get(i))[1];
            tmp.multiply(n);
            tmp.multiply(congruences.get(i));
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
    private PolynomialRC productSum(Collection<PolynomialRC> values) throws FFaplAlgebraicException {
        PolynomialRC result = new PolynomialRC(ONE, ZERO, characteristic, thread);
        for (PolynomialRC value : values) {
            isRunning(thread);
            result.multiply(value);
        }
        return result;
    }

    /**
     * This function returns the indices of the first to moduli of the given array which are not coprime, if there are any.
     * If there are non or it's impossible for them to exist due to the array being smaller than 2, null is returned.
     */
    private int[] findTwoNonCoprimeModuli(List<PolynomialRC> moduli) throws FFaplAlgebraicException {
        if(moduli.size() < 2) {
            return null;
        }

        for (int i = 0; i < moduli.size(); i++) {
            for (int j = i+1; j < moduli.size(); j++) {
                isRunning(thread);
                if (!gcd(moduli.get(i), moduli.get(j)).isOne()) {
                    return new int[] {i,j};
                }
            }
        }

        return null;
    }

    /** If a = b mod d with d = gcd(m1, m2), then x = a mod m1 and x = b mod m2 is equivalent to
     *  x = a - y * m1 * (a-b)/d mod (m1*m2)/d with d = y * m1 + z * m2 given by the extended Euclidian algorithm
     *  In this case, this function returns a two-dimensional array containing the new congruence to be fulfilled by x
     *  and the respective modulus for this congruence. This congruence-module pair can be reduced to replace the two
     *  pairs given as input to this function.
     */
    private PolynomialRC[] solveSimultaneousCongruenceDirectlyFor(PolynomialRC a, PolynomialRC m1, PolynomialRC b, PolynomialRC m2) throws FFaplAlgebraicException {
        PolynomialRC[] eeaResult = eea(m1, m2);
        PolynomialRC d = eeaResult[0];
        PolynomialRC y = eeaResult[1];

        // first, check whether a solution exists;
        PolynomialRC a_mod_d = (PolynomialRC) a.clone();
        a_mod_d.mod(d);
        PolynomialRC b_mod_d = (PolynomialRC) b.clone();
        b_mod_d.mod(d);

        if (!a_mod_d.equals( b_mod_d)) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_NO_SOLUTION);
        }

        // temp2 := (a - b) / d
        PolynomialRC temp2 = (PolynomialRC) a.clone();
        temp2.subtract(b);
        temp2.divide(d);  // Might throw an exception if there is no multiplicative inverse of d in the ring of the given characteristic

        // temp1 := y * m1 * temp2
        PolynomialRC temp1 = (PolynomialRC) y.clone();
        temp1.multiply(m1);
        temp1.multiply(temp2);

        // Calculate newModulus := (m1*m2)/d
        PolynomialRC newModulus = (PolynomialRC) m1.clone();
        newModulus.multiply(m2);
        newModulus.divide(d);   // Might throw an exception if there is no multiplicative inverse of d in the ring of the given characteristic

        // solution := a - temp1 (mod newModulus)
        PolynomialRC solution = (PolynomialRC) a.clone();
        solution.subtract(temp1);
        solution.mod(newModulus);

        // return {a - y * m1 * (a-b)/d mod newModulus; newModulus}
        return new PolynomialRC[] {solution, newModulus};
    }
}
