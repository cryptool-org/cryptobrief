package ffapl.java.interfaces;

import ffapl.java.exception.FFaplAlgebraicException;

public interface IAlgebraicOperations<A> {

	/**
	 * Modulo the modulus
	 * @param modulus
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public A mod(Object modulus) throws FFaplAlgebraicException;
	
	/**
	 * Multiplies with a factor
	 * @param factor
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public A mul(Object factor) throws FFaplAlgebraicException;
	
	/**
	 * Divides with a divisor
	 * @param divisor
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public A div(Object divisor) throws FFaplAlgebraicException;
	
	/**
	 * Adds a summand
	 * @param summand
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public A add(Object summand) throws FFaplAlgebraicException;
	
	/**
	 * Subtracts subtrahend
	 * @param subtrahend
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public A sub(Object subtrahend) throws FFaplAlgebraicException;
	
	/**
	 * To the power of the exponent
	 * @param exponent
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public A pow(Object exponent) throws FFaplAlgebraicException;
	
}
