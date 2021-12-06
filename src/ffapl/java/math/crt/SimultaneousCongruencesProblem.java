package ffapl.java.math.crt;

import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;

import static ffapl.java.math.Algorithm.*;
import static java.math.BigInteger.*;

/**
 * Represents a problem were simultaneous congruences need to be solved, which is done via the Chinese Remainder Theorem
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class SimultaneousCongruencesProblem {

    private final BInteger[] congruences;
    private final BInteger[] moduli;
    private final Thread thread;

    private static final BigInteger NO_SOLUTION_FOUND = new BigInteger("-1");

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
    public SimultaneousCongruencesProblem(BInteger[] congruences, BInteger[] moduli) throws FFaplAlgebraicException {
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
     * Solving simultaneous congruences with given congruences and moduli. Calculates the value which solves those congruences
     * using the Chinese remainder theorem or NO_SOLUTION_FOUND if no such value exists.
     * Uses different solution strategies based on whether the moduli are pairwise coprime or not.
     * @throws FFaplAlgebraicException
     */
    public BInteger solve() throws FFaplAlgebraicException {
        if (valuesArePairwiseCoprime(moduli)) {
            return solveForCoprimeModuli();

        } else {
            return solveForNonCoprimeModuli();
        }
    }

    /**
     * Since moduli are pairwise coprime, the simultaneous congruences can be solved directly
     * @throws FFaplAlgebraicException
     *          <INTERRUPT> if thread is interrupted
     */
    private BInteger solveForCoprimeModuli() throws FFaplAlgebraicException {
        BInteger moduliProduct = new BInteger(productSum(moduli), thread);
        BInteger result = new BInteger(ZERO, thread);
        BInteger n;
        for (int i = 0; i < moduli.length; i++) {
            isRunning(thread);
            n = new BInteger(moduliProduct.divide(moduli[i]), thread);
            result = (BInteger) result.add(
                    eea(n, moduli[i])[1]
                            .multiply(n)
                            .multiply(congruences[i])
                            .mod(moduliProduct));
        }
        return result;
    }

    /**
     * Since moduli are not pairwise coprime, we first need to check whether a solution exists.
     * If this is the case then the solution must be somewhere between 0 and the lcm of the moduli.
     * We find the solution by starting with the congruence given for the greatest module as a solution candidate.
     * If this solution candidate happens to fulfill all other congruences, we have found a solution.
     * If it does not, we replace it with solutionCandidate := solutionCandidate + greatest_module and check again.
     * This is done until a solution is found or solutionCandidate exceeds the lcm of all moduli.
     * @throws FFaplAlgebraicException
     */
    // Another idea: https://stackoverflow.com/questions/50081378/system-of-congruences-with-non-pairwise-coprime-moduli
    private BInteger solveForNonCoprimeModuli() throws FFaplAlgebraicException {
        if (!solutionExists()) {
            return new BInteger(NO_SOLUTION_FOUND, thread);
        }
        BInteger moduleLcm = lcm(moduli);	// the solution x is between 0 and lcm
        int largestModulePosition = positionOfLargestValue(moduli);
        BInteger largestModule = moduli[largestModulePosition];
        BInteger solutionCandidate = congruences[largestModulePosition];

        while (solutionCandidate.compareTo(moduleLcm) <= 0) {
            if (problemIsSolvedBy(solutionCandidate)) {
                return solutionCandidate;
            }

            solutionCandidate = solutionCandidate.addR(largestModule);
        }

        // This place should never be reached in theory, since we already checked and concluded that a solution should exist.
        return new BInteger(NO_SOLUTION_FOUND, thread);
    }

    /**
     * returns true if the passed solution candidate satisfies all simultaneous congruences; "false" otherwise
     */
    private boolean problemIsSolvedBy(BInteger solutionCandidate) {
        for (int i = 0; i < moduli.length; i++) {
            if (!solutionCandidate.mod(moduli[i]).equals(congruences[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks for all pairs of moduli (m,n) whether their respective congruences are equal mod gcd(m,n)
     * If yes, then a solution exists and "true" is returned; "false" otherwise.
     * @throws FFaplAlgebraicException
     *          <INTERRUPT> if thread is interrupted
     */
    private boolean solutionExists() throws FFaplAlgebraicException {
        for (int i = 0; i < moduli.length; i++) {
            for (int j = i+1; j < moduli.length; j++) {
                BInteger gcd = Algorithm.gcd(moduli[i], moduli[j]);

                isRunning(thread);
                if (!congruences[i].mod(gcd).equals(congruences[j].mod(gcd))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Returns all values multiplied together, "1" if array is empty
     * @throws FFaplAlgebraicException
     * 			<INTERRUPT> if thread is interrupted
     */
    private BigInteger productSum(BInteger[] values) throws FFaplAlgebraicException {
        BigInteger result = ONE;
        for (BInteger value : values) {
            isRunning(thread);
            result = result.multiply(value);
        }
        return result;
    }

    /**
     * Returns "true" if values are coprime or array is empty, "false" otherwise
     * @throws FFaplAlgebraicException
     * 			<INTERRUPT> if thread is interrupted
     */
    public boolean valuesArePairwiseCoprime(BInteger[] values) throws FFaplAlgebraicException {
        if(values.length == 0) {
            return true;
        }

        Thread thread = values[0].getThread();
        for (int i = 0; i < values.length; i++) {
            for (int j = i+1; j < values.length; j++) {
                isRunning(thread);
                if (!gcd(values[i], values[j]).equals(ONE)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns least common multiple for all values in the given integer array.
     * @throws FFaplAlgebraicException
     * 			<INTERRUPT> if thread is interrupted
     * 		    <CRT_ARRAY_EMPTY> if the "values" array is empty.
     */
    private BInteger lcm(BInteger[] values) throws FFaplAlgebraicException {
        if(values.length == 0) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_ARRAY_EMPTY);
        }

        Thread thread = values[0].getThread();
        BInteger currentResult = new BInteger(ONE, thread);
        for (BInteger value : values) {
            isRunning(thread);
            currentResult = (BInteger) currentResult
                    .multiply(value)
                    .abs()
                    .divide(gcd(currentResult, value));
        }
        return currentResult;
    }

    /**
     * returns position of the largest value. "-1" if array is empty
     */
    private int positionOfLargestValue(BInteger[] values) {
        int currentMaximumPosition = -1;
        for (int i = 0; i < values.length; i++) {
            if (currentMaximumPosition < 0) {
                currentMaximumPosition = i;
            }

            if (values[i].compareTo(values[currentMaximumPosition]) > 0) {
                currentMaximumPosition = i;
            }
        }
        return currentMaximumPosition;
    }
}
