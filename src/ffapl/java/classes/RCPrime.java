/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;

import ffapl.java.exception.FFaplAlgebraicException;

/**
 * Residue Class Prime
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class RCPrime extends ResidueClass {

	
	/**
	 * default constructor value = 0 modulo <Code> modulus </Code>;
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime 
	 */
	public RCPrime(BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(new Prime(modulus, thread));
	}
	
	/**
	 * default constructor value = 0 modulo <Code> modulus </Code>;
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime
	 */
	public RCPrime(long modulus, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(modulus), thread);
	}

	/**
	 * @param value
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime 
	 */
	public RCPrime(BigInteger value, BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(value, new Prime(modulus, thread));
	}
	
	/**
	 * @param value
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime 
	 */
	public RCPrime(long value, long modulus, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(value), BigInteger.valueOf(modulus), thread);
	}
	
	

}
