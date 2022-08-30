package ffapl.java.math.crt;

import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;

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
public class IntegerSimultaneousCongruencesProblem {

    private final BInteger[] congruences;
    private final BInteger[] moduli;
    private final Thread thread;

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
     */
    public IntegerSimultaneousCongruencesProblem(BInteger[] congruences, BInteger[] moduli) throws FFaplAlgebraicException {
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
        for (BInteger currentModule : moduli) {
            isRunning(thread);
            if (currentModule.equals(ZERO)) {
                throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_ZERO_OR_NEGATIVE_MODULES);
            }
        }
    }

    /**
     * Solving simultaneous congruences with given congruences and moduli.
     * Reduces the given system of simultaneous congruences to a single one using a combination of the Chinese remainder theorem
     * and an algorithm for solving a pair of simultaneous congruences with noncoprime moduli or throws an exception if
     * no solution exists.
     * @throws FFaplAlgebraicException
     */
    public BInteger[] solve() throws FFaplAlgebraicException {
        List<BInteger> currentCongruences = new ArrayList<>(Arrays.asList(congruences));
        List<BInteger> currentModuli = new ArrayList<>(Arrays.asList(moduli));

        int[] currentNonCoprimeModuliIndices;

        // As long as congruence-moduli pairs with non-coprime moduli are found ...
        while ((currentNonCoprimeModuliIndices = findTwoNonCoprimeModuli(currentModuli)) != null) {
            int firstModuleIndex = currentNonCoprimeModuliIndices[0];
            int secondModuleIndex = currentNonCoprimeModuliIndices[1];

            // TODO - DOMINIC - 14.12.2021: Works, but could use some refactoring
            BInteger firstCongruence = currentCongruences.get(firstModuleIndex);
            BInteger secondCongruence = currentCongruences.get(secondModuleIndex);
            BInteger firstModule = currentModuli.get(firstModuleIndex);
            BInteger secondModule = currentModuli.get(secondModuleIndex);

            // ... try to solve them directly, and ...
            BInteger[] directCongruenceResult = solveSimultaneousCongruenceDirectlyFor(
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
    private BInteger[] solveForCoprimeModuli(List<BInteger> congruences, List<BInteger> moduli) throws FFaplAlgebraicException {
        BInteger moduliProduct = productSum(moduli);
        BInteger result = new BInteger(ZERO, thread);
        BInteger n;
        for (int i = 0; i < moduli.size(); i++) {
            isRunning(thread);
            n = new BInteger(moduliProduct.divide(moduli.get(i)), thread);
            result = (BInteger) result.add(
                    eea(n, moduli.get(i))[1]
                            .multiply(n)
                            .multiply(congruences.get(i))
                            .mod(moduliProduct));
        }
        result = (BInteger)result.mod(moduliProduct);
        return new BInteger[] {result, moduliProduct};
    }

    /**
     * This function returns the indices of the first to moduli of the given array which are not coprime, if there are any.
     * If there are non or it's impossible for them to exist due to the array being smaller than 2, null is returned.
     */
    private int[] findTwoNonCoprimeModuli(List<BInteger> moduli) throws FFaplAlgebraicException {
        if(moduli.size() < 2) {
            return null;
        }

        for (int i = 0; i < moduli.size(); i++) {
            for (int j = i+1; j < moduli.size(); j++) {
                isRunning(thread);
                if (!gcd(moduli.get(i), moduli.get(j)).equals(ONE)) {
                    return new int[] {i,j};
                }
            }
        }

        return null;
    }

    /**
     * Returns all values multiplied together, "1" if array is empty
     * @throws FFaplAlgebraicException
     * 			<INTERRUPT> if thread is interrupted
     */
    private BInteger productSum(Collection<BInteger> values) throws FFaplAlgebraicException {
        BInteger result = new BInteger(ONE, thread);
        for (BInteger value : values) {
            isRunning(thread);
            result = result.multR(value);
        }
        return result;
    }

    /** If a = b mod d with d = gcd(m1, m2), then x = a mod m1 and x = b mod m2 is equivalent to
     *  x = a - y * m1 * (a-b)/d mod (m1*m2)/d with d = y * m1 + z * m2 given by the extended Euclidian algorithm
     *  In this case, this function returns a two-dimensional array containing the new congruence to be fulfilled by x
     *  and the respective modulus for this congruence. This congruence-module pair can be reduced to replace the two
     *  pairs given as input to this function.
     */
    private BInteger[] solveSimultaneousCongruenceDirectlyFor(BInteger a, BInteger m1, BInteger b, BInteger m2) throws FFaplAlgebraicException {
        BInteger[] eeaResult = eea(m1, m2);
        BInteger d = eeaResult[0];
        BInteger y = eeaResult[1];

        // first, check whether a solution exists;
        if (!a.mod(d).equals(b.mod(d))) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_NO_SOLUTION);
        }

        // Calculate newModulus := (m1*m2)/d
        BInteger newModulus = (BInteger) m1.multiply(m2).divide(d);

        // return {a - y * m1 * (a-b)/d mod newModulus; newModulus}
        return new BInteger[] {
                (BInteger) a.subtract(
                        y.multiply(m1).multiply(a.subtract(b).divide(d)))
                        .mod(newModulus),
                newModulus};
    }
}
