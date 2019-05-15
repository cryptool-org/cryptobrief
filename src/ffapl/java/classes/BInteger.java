/**
 *
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.util.Random;

import ffapl.FFaplInterpreter;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IAlgebraicOperations;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.math.Algorithm;
import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BInteger extends BigInteger implements IJavaType<BInteger>, IAlgebraicOperations<BInteger> {

	/** the thread within the Galois Field is created */
	protected Thread _thread;

	/**
	 * @param val
	 */
	public BInteger(byte[] val, Thread thread) {
		super(val);
		_thread = thread;
	}

	/**
	 * @param val
	 * @param thread
	 */
	public BInteger(String val, Thread thread) {
		super(val);
		_thread = thread;
	}

	/**
	 * @param signum
	 * @param magnitude
	 * @param thread
	 */
	public BInteger(int signum, byte[] magnitude, Thread thread) {
		super(signum, magnitude);
		_thread = thread;
	}

	/**
	 * @param val
	 * @param radix
	 * @param thread
	 */
	public BInteger(String val, int radix, Thread thread) {
		super(val, radix);
		_thread = thread;
	}

	/**
	 * @param numBits
	 * @param rnd
	 * @param thread
	 */
	public BInteger(int numBits, Random rnd, Thread thread) {
		super(numBits, rnd);
		_thread = thread;
	}

	/**
	 * @param bitLength
	 * @param certainty
	 * @param rnd
	 * @param thread
	 */
	public BInteger(int bitLength, int certainty, Random rnd, Thread thread) {
		super(bitLength, certainty, rnd);
		_thread = thread;
	}

	/**
	 * @param value
	 * @param thread
	 */
	public BInteger(BigInteger value, Thread thread){
		super(value.toByteArray());
		_thread = thread;
	}

	@Override
	public int typeID() {
		return IJavaType.INTEGER;
	}

	@Override
	public BigInteger multiply(BigInteger val){
		return new BInteger(super.multiply(val), _thread);
	}

	@Override
	public BigInteger divide(BigInteger val){
		return new BInteger(super.divide(val), _thread);
	}

	@Override
	public BigInteger add(BigInteger val){
		return new BInteger(super.add(val), _thread);
	}

	@Override
	public BigInteger subtract(BigInteger val){
		return new BInteger(super.subtract(val), _thread);
	}

	@Override
	public BigInteger mod(BigInteger val){
		//System.out.println(this + " MOD " + val);
		return new BInteger(super.mod(val), _thread);
	}

	@Override
	public BigInteger modInverse(BigInteger val){
		return new BInteger(super.modInverse(val), _thread);
	}

	@Override
	public BigInteger negate(){
		return new BInteger(super.negate(), _thread);
	}

	@Override
	public BigInteger gcd(BigInteger val){
		return new BInteger(super.gcd(val), _thread);
	}

	@Override
	public BigInteger min(BigInteger val){
		return new BInteger(super.min(val), _thread);
	}

	@Override
	public BigInteger max(BigInteger val){
		return new BInteger(super.max(val), _thread);
	}

	@Override
	public BigInteger modPow(BigInteger exponent, BigInteger m){
		return new BInteger(super.modPow(exponent, m), _thread);
	}

	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER];
	}

	/**
	 * Power Operation
	 * @param b
	 * @return
	 * @throws FFaplAlgebraicException
	 * 			<TO_HIGH_EXPONENT> if exponent is greater then Integer.MAX_VALUE
	 */
	public Object pow(BigInteger b) throws FFaplAlgebraicException {
		if(b.compareTo(BigInteger.ZERO) < 0){
			String[] arguments = {b.toString(),
								 this.classInfo() + " -> " + this +
										FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + b };
			throw new FFaplAlgebraicException(arguments,
					IAlgebraicError.NEGATIVE_EXPONENT);
		}

		if (b.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0){
			return Algorithm.squareAndMultiply(this, b, _thread);
			//return this.pow(b.intValue());
		}else{
			String[] messages = {this +
					FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + b };
			throw new FFaplAlgebraicException(messages,
					IAlgebraicError.TO_HIGH_EXPONENT);
		}

	}


	@Override
	public BInteger clone(){
		return new BInteger(this, _thread);
	}

    /**
     * Returns a BInteger whose value is equal to
     * that of the specified long.
     * @param val
     * @param thread
     * @return
     */
	public static BInteger valueOf(long val, Thread thread){
		return new BInteger(BigInteger.valueOf(val), thread);
	}

	@Override
	public boolean equalType(Object type) {
		if(type instanceof BInteger){
			return true;
		}
		return false;
	}

	@Override
	public BInteger nextProbablePrime(){
		return new BInteger(super.nextProbablePrime(), _thread);
	}

	/**
	 * Returns the thread within the GaloisField is created
	 * @return
	 */
	public Thread getThread(){
		return _thread;
	}

	@Override
	public BInteger addR(BInteger summand) {
		return (BInteger) this.add(summand);
	}

	@Override
	public BInteger subR(BInteger subtrahend) {
		return (BInteger) this.subtract(subtrahend);
	}

	@Override
	public BInteger multR(BInteger factor) {
		return (BInteger) this.multiply(factor);
	}

	@Override
	public BInteger scalarMultR(BigInteger factor) {
		// in this case equal to multR
		return (BInteger) this.multiply(factor);
	}

	@Override
	public BInteger divR(BInteger divisor) {
		return (BInteger) this.divide(divisor);
	}

	@Override
	public BInteger negateR() {
		return (BInteger) this.negate();
	}

	@Override
	public BInteger powR(BigInteger exponent) throws FFaplAlgebraicException {
		return (BInteger) this.pow(exponent);
	}
}
