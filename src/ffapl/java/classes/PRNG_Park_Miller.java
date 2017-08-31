/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPseudoRandomGenerator;
import ffapl.types.FFaplTypeCrossTable;

/**
 * Pseudo Random Generator according Park Miller
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class PRNG_Park_Miller implements IPseudoRandomGenerator {

	
	private BigInteger _n = new BigInteger("2147483647");
	private BigInteger _g = new BigInteger("16807");
	private BigInteger _max;
	private BigInteger _min;
	private BigInteger _seed;
	private BigInteger _x;
	private Thread _thread;
	
	/**
	 * default constructor full range 0 - 2.147.483.646
	 * @param seed
	 * @param thread
	 */
	public PRNG_Park_Miller(BigInteger seed, Thread thread) throws FFaplAlgebraicException{
		_max = BigInteger.ONE.negate();
		_min = BigInteger.ONE.negate();
		_thread = thread;
		_seed = seed;
		_x = _seed;
		if(_seed.compareTo(BigInteger.ZERO) < 0){
			Object[] arguments = {this.classInfo(), "seed: " + _seed}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_ZERO);
		}
	}
	
	/**
	 * Constructor
	 * @param seed
	 * @param max
	 * @param thread
	 */
	public PRNG_Park_Miller(BigInteger seed, BigInteger max, Thread thread) throws FFaplAlgebraicException{
		this(seed, BigInteger.ZERO, max, thread);
	}
	
	/**
	 * Constructor
	 * @param seed
	 * @param min
	 * @param max
	 * @param thread
	 */
	public PRNG_Park_Miller(BigInteger seed, BigInteger min, BigInteger max, Thread thread) throws FFaplAlgebraicException{
		_max = max.abs();
		_min = min.abs();
		_seed = seed.abs();
		_x = _seed;
		_thread = thread;
		if(_seed.compareTo(BigInteger.ZERO) <= 0){
			Object[] arguments = {this.classInfo(), "seed: " + _seed}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
		}
		if(_min.compareTo(BigInteger.ZERO) < 0){
			Object[] arguments = {this.classInfo(), "min: " + _min}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_ZERO);
		}
		if(_max.compareTo(BigInteger.ZERO) <= 0){
			Object[] arguments = {this.classInfo(), "max: " + _min}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
		}
		if(_min.compareTo(_max) >= 0 ){
			Object[] arguments = {this.classInfo(), min + ">=" + max}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.MIN_GREATER_EQUAL_MAX);
		}
	}
	
	
	
	/**
	 * Constructor
	 * @param seed
	 * @param max
	 * @param thread
	 */
	public PRNG_Park_Miller(long seed, long max, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(seed), BigInteger.valueOf(max), thread);
	}
	
	/**
	 * Constructor
	 * @param seed
	 * @param min
	 * @param max
	 * @param thread
	 */
	public PRNG_Park_Miller(long seed, long min, long max, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(seed), BigInteger.valueOf(min), BigInteger.valueOf(max), thread);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IPseudoRandomGenerator#next()
	 */
	@Override
	public BigInteger next() throws FFaplAlgebraicException {
		_x = _x.multiply(_g).mod(_n);
		if(_max.equals(BigInteger.ONE.negate())){
			return new BInteger(_x, _thread);
		}else{
			return new BInteger(_x.mod(_max.subtract(_min)).add(_min), _thread);
		}
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
	public boolean equalType(Object type) {
		if(type instanceof IPseudoRandomGenerator){
			return true;
		}
		return false;
	}
	
	

}
