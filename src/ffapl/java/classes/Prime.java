/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.util.Random;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Prime extends BInteger{

	/**
	 * 
	 */
	private static final long serialVersionUID = -114089618008010463L;
	private int _certainty = 100; //certainty to be prime

	/**
	 * @param val
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(byte[] val, Thread thread) throws FFaplAlgebraicException{
		super(val, thread);
		checkIfPrime();
	}

	/**
	 * @param val
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(String val, Thread thread) throws FFaplAlgebraicException {
		super(val, thread);
		checkIfPrime();
	}

	/**
	 * @param signum
	 * @param magnitude
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(int signum, byte[] magnitude, Thread thread) throws FFaplAlgebraicException {
		super(signum, magnitude, thread);
		checkIfPrime();
	}

	/**
	 * @param val
	 * @param radix
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(String val, int radix, Thread thread) throws FFaplAlgebraicException {
		super(val, radix, thread);
		checkIfPrime();
	}

	/**
	 * @param numBits
	 * @param rnd
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(int numBits, Random rnd, Thread thread) throws FFaplAlgebraicException {
		super(numBits, rnd, thread);
		checkIfPrime();
	}

	/**
	 * @param bitLength
	 * @param certainty
	 * @param rnd
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(int bitLength, int certainty, Random rnd, Thread thread) throws FFaplAlgebraicException {
		super(bitLength, certainty, rnd, thread);
		checkIfPrime();
	}
	
	/**
	 * Constructor
	 * @param value
	 * @param thread
	 * @throws FFaplAlgebraicException if not Prime
	 */
	public Prime(BigInteger value, Thread thread) throws FFaplAlgebraicException {
		super(value.toByteArray(), thread);
		checkIfPrime();
	}
	
	/**
	 * Checks if Value of integer is Prime
	 * @return true if value is Prime
	 * @throws FFaplAlgebraicException if value is not Prime
	 */
	private boolean checkIfPrime() throws FFaplAlgebraicException{
		if(this.isProbablePrime(_certainty)){
			return true;
		}else{
			Object[] arguments = {this};
			throw new FFaplAlgebraicException(arguments ,IAlgebraicError.NOTPRIME);
		}
	}

	@Override
	public int typeID() {
		return IJavaType.PRIME;
	}
	
	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME];
	}

	@Override
	public BInteger clone(){
		try {
			return new Prime(this, _thread);
		} catch (FFaplAlgebraicException e) {
			return null;
		}
	}
}
