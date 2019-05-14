/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.security.SecureRandom;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPseudoRandomGenerator;
import ffapl.java.math.Algorithm;
import ffapl.types.FFaplTypeCrossTable;

/**
 * Build in PRNG
 * @author Alexander Ortner
 * @version 1.0
 * Update (14.05.2019) by Stefan Rass: replaced SecureRandom by a humble hash chain, in order to get reproducible sequences upon the same seed
 *
 */
public class PRNG_Standard implements IPseudoRandomGenerator {



	private Thread _thread;
	private BInteger _seed;
	private SecureRandom rg ;
	private BInteger _max;
	private JString _current; // current value of the hash chain
	private int _exp;  // exponent to stretch the 256 bit hash into the sought length (not claiming this to be anyhow cryptographically justified or advisable)
	
	/**
	 * Constructor
	 * @param seed start value
	 * @param max 
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime
	 *         <WRONG_INPUT> if seed is ZERO mod modulus
	 */
	public PRNG_Standard (BigInteger seed, BigInteger max, Thread thread) throws FFaplAlgebraicException{
		
		rg = new SecureRandom();
		rg.setSeed(seed.toByteArray());
		
		_thread = thread;
		_seed = new BInteger(seed, thread);	
		_max = new BInteger(max, thread);
		_exp = 1 + max.bitLength() / 256; // we will use this factor to stretch the 256 bit value of the hash into the sought bitlength
		_current = new JString(seed.toString());
	}
	
	/**
	 * Constructor
	 * @param seed start value
	 * @param max
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <NOTPRIME> if modulus is not prime
	 *         <WRONG_INPUT> if seed is ZERO mod modulus
	 */
	public PRNG_Standard (long seed, long max, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(seed), BigInteger.valueOf(max), thread);
	}
	
	/**
	 * Return the seed of the PRNG
	 * @return
	 */
	public BigInteger seed(){
		return _seed;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IPseudoRandomGenerator#next()
	 */
	@Override
	public BigInteger next() throws FFaplAlgebraicException{
		//BigInteger val = new BigInteger(_max.bitLength(), rg); // this would give an irreproducible sequence, since "seed" only supplements, but does not replace the inner seed of SecureRandom
		BigInteger val = Algorithm.hashSHA256(_current).pow(_exp); // use a humble hash chain and expand the result
		val = val.mod(_max.add(BigInteger.ONE));
		_current = new JString(val.toString());
	    return new BInteger(val, _thread);
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
