/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.security.SecureRandom;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.ITrueRandomGenerator;
import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class TRNG_Placebo implements ITrueRandomGenerator {

	
	private BigInteger _min;
	private BigInteger _max;
	private Thread _thread;
	
	
	/**
	 * default constructor
	 * @param thread
	 */
	public TRNG_Placebo(Thread thread){
		_min = BigInteger.ZERO;
		_max = BigInteger.ONE.negate();//full range
		_thread = thread;
	}
	
	/**
	 * 
	 * @param max
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <WRONG_INPUT> if 0 >= abs(max)
	 */
	public TRNG_Placebo(BigInteger max, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.ZERO, max, thread);
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <WRONG_INPUT> if abs(min) >= abs(max)
	 */
	public TRNG_Placebo(BigInteger min, BigInteger max, Thread thread) throws FFaplAlgebraicException{
		_max = max;
		_min = min;
		_thread = thread;
		if(_min.compareTo(BigInteger.ZERO) < 0){
			Object[] arguments = {this.classInfo(), "min: " + _min}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_ZERO);
		}
		if(_max.compareTo(BigInteger.ZERO) <= 0){
			Object[] arguments = {this.classInfo(), "max: " + _max}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
		}
		if(_min.compareTo(_max) >= 0 ){
			Object[] arguments = {this.classInfo(), min + ">=" + max}; 
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.MIN_GREATER_EQUAL_MAX);
		}
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <WRONG_INPUT> if abs(min) >= abs(max)
	 */
	public TRNG_Placebo(long min, long max, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(min), BigInteger.valueOf(max), thread);
	}
	
	/**
	 * 
	 * @param max
	 * @param thread
	 * @throws FFaplAlgebraicException
	 *         <WRONG_INPUT> if 0 >= abs(max)
	 */
	public TRNG_Placebo(long max, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.ZERO, BigInteger.valueOf(max), thread);
	}
	
	

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.ITrueRandomGenerator#next()
	 */
	@Override
	public BigInteger next() throws FFaplAlgebraicException{
		//SecureRandom rg = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes());
		SecureRandom rg = new SecureRandom();
		BigInteger val = BigInteger.valueOf(rg.nextLong());
		if(_max.equals(BigInteger.ONE.negate())){
			//nothing todo
		}else{
			val = val.mod(_max.subtract(_min).add(BigInteger.ONE)).add(_min);
		}
		if(val.compareTo(BigInteger.ZERO) < 0){
			val = val.negate();
		}
		return new BInteger(val, _thread);
	}

	@Override
	public int typeID() {
		return IJavaType.RANDOMGENERATOR;
	}
	
	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLRANDOMG];
	}
	
	@Override
	public ITrueRandomGenerator clone(){
		/*if(_max.compareTo(BigInteger.ZERO) < 0){
			return new TRNG_Placebo();
		}
		try {
			return new TRNG_Placebo(_min, _max);
		} catch (FFaplAlgebraicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}*/
		//Only a copy
		return this;
	}
	
	@Override
	public boolean equalType(Object type) {
		if(type instanceof ITrueRandomGenerator){
			return true;
		}
		return false;
	}

}
