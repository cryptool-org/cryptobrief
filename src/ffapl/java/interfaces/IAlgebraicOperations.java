package ffapl.java.interfaces;

import ffapl.java.exception.FFaplAlgebraicException;

import java.math.BigInteger;

public interface IAlgebraicOperations<T> extends IJavaType {

    /**
     * Adds a summand
     *
     * @param summand summand
     * @return sum
     * @throws FFaplAlgebraicException
     */
    T addR(T summand) throws FFaplAlgebraicException;

    /**
     * Subtracts subtrahend
     *
     * @param subtrahend subtrahend
     * @return difference
     * @throws FFaplAlgebraicException
     */
    T subR(T subtrahend) throws FFaplAlgebraicException;

    /**
     * Multiplies with a factor
     *
     * @param factor factor
     * @return product
     * @throws FFaplAlgebraicException
     */
    T multR(T factor) throws FFaplAlgebraicException;

    /**
     * Multiplies with a scalar factor
     *
     * @param factor factor
     * @return product
     * @throws FFaplAlgebraicException
     */
    T scalarMultR(BigInteger factor) throws FFaplAlgebraicException;

    /**
     * Divides by a dividend
     *
     * @param divisor divisor
     * @return quotient
     * @throws FFaplAlgebraicException
     */
    T divR(T divisor) throws FFaplAlgebraicException;

    /**
     * Negates the value
     *
     * @return negation
     * @throws FFaplAlgebraicException
     */
    T negateR() throws FFaplAlgebraicException;

    /**
     * Raises by a power
     *
     * @param exponent exponent
     * @return power
     * @throws FFaplAlgebraicException
     */
    T powR(BigInteger exponent) throws FFaplAlgebraicException;
}
