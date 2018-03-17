package ffapl.java.interfaces;

import ffapl.java.exception.FFaplAlgebraicException;

import java.math.BigInteger;

public interface IAlgebraicOperations<T> extends IJavaType {

    /**
     * Adds a summand
     *
     * @param summand
     * @return sum
     * @throws FFaplAlgebraicException
     */
    T addR(T summand) throws FFaplAlgebraicException;

    /**
     * Subtracts subtrahend
     *
     * @param subtrahend
     * @return difference
     * @throws FFaplAlgebraicException
     */
    T subR(T subtrahend) throws FFaplAlgebraicException;

    /**
     * Multiplies with a factor
     *
     * @param factor
     * @return product
     * @throws FFaplAlgebraicException
     */
    T mulR(T factor) throws FFaplAlgebraicException;

    /**
     * Multiplies with a scalar factor
     *
     * @param factor
     * @return product
     * @throws FFaplAlgebraicException
     */
    T scalarMulR(BigInteger factor) throws FFaplAlgebraicException;

}
