/**
 * 
 */
package ffapl.java.interfaces;

import java.math.BigInteger;

import ffapl.java.exception.FFaplAlgebraicException;

/**
 * Random GeneratorInterface
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface IRandomGenerator extends IJavaType{

	/**
	 * Returns the next Random number
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public BigInteger next() throws FFaplAlgebraicException;
	
}
