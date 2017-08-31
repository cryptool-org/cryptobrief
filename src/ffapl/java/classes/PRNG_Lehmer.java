/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.util.Random;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPseudoRandomGenerator;
import ffapl.types.FFaplTypeCrossTable;

/**
 * Lehmer random number generator x_(i+1) = g*x_i mod p
 * restricted form
 * p must be a prime
 * g is randomly choosen out of Zp
 * x_0 is seed
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class PRNG_Lehmer implements IPseudoRandomGenerator {

	private RCPrime _seed;
	private RCPrime _x;
	private RCPrime _multiplier;
	private Thread _thread;
	
	/**
	 * Constructor
	 * @param seed start value
	 * @param modulus prime number
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime
	 *         <WRONG_INPUT> if seed is ZERO mod modulus
	 */
	public PRNG_Lehmer (BigInteger seed, BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		
		Random r = new Random();
		
		_thread = thread;
		
		_seed = new RCPrime(seed, modulus, _thread);
		_x = _seed;
		if(_seed.compareTo(BigInteger.ZERO) <= 0){
			Object[] arguments = {this.classInfo(), "seed: " + _seed}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
		}
				
		_multiplier = new RCPrime(BigInteger.ZERO, modulus, _thread);
		  
		while(_multiplier.equals(BigInteger.ZERO) || _multiplier.equals(BigInteger.ONE)
				|| _multiplier.equals(_seed.value()) || _multiplier.equals(modulus.subtract(BigInteger.ONE))){
			_multiplier.setValue(BigInteger.valueOf(r.nextLong()));
		}				
	}
	
	/**
	 * Constructor
	 * @param seed start value
	 * @param modulus prime number
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime
	 *         <WRONG_INPUT> if seed is ZERO mod modulus
	 */
	public PRNG_Lehmer (long seed, long modulus, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(seed), BigInteger.valueOf(modulus), thread);
	}
	
	/**
	 * Return the seed of the PRNG
	 * @return
	 */
	public BigInteger seed(){
		return _seed.value();
	}
	
	/**
	 * Returns the modulus of the PRNG
	 * @return
	 */
	public BigInteger modulus(){
		return _x.modulus();
	}
	
	/**
	 * Returns the multiplier of the PRNG
	 * @return
	 */
	public BigInteger multiplier(){
		return _multiplier.value();
	}
	
	
	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IPseudoRandomGenerator#next()
	 */
	@Override
	public BigInteger next() throws FFaplAlgebraicException{
		   _x.multiply(_multiplier);
			return new BInteger(_x.value(), _thread);	
	}

	@Override
	public int typeID() {
		return IJavaType.PSRANDOMGENERATOR;
	}

	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPSRANDOMG];
	}
	
	@Override
	public IPseudoRandomGenerator clone(){
		//Only a copy
		return this;
	}
	
	@Override
	public boolean equalType(Object obj){
		if(obj instanceof IPseudoRandomGenerator){
			return true;
		}
		return false;
	}

}
