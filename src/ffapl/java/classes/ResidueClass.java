package ffapl.java.classes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ffapl.FFaplInterpreter;
import ffapl.exception.FFaplException;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ResidueClass implements IJavaType{

	protected BigInteger _modulus;
	protected BigInteger _value;
	protected Thread _thread;
	
	/**
	 * default constructor value = 0 modulo <Code> modulus </Code>;
	 * @param modulus
	 */
	public ResidueClass(BigInteger modulus) throws FFaplAlgebraicException{
		_modulus = modulus;
		_value = BigInteger.ZERO;
                if (modulus.signum() < 1)
                {
                        Object[] arguments ={};
                        throw new FFaplAlgebraicException(arguments,
                        IAlgebraicError.RESIDUE_CLASS_CHARACTERISTIC_POSITIVE);
                }
	}
	
	/**
	 * default constructor value = 0 modulo <Code> modulus </Code>;
	 * @param modulus
	 */
	public ResidueClass(long modulus) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(modulus));
	}
	
	/**
	 * Constructor
	 * @param modulus
	 * @param value
	 */
	public ResidueClass(BigInteger value, BigInteger modulus){
		_modulus = modulus;
		_value = value.mod(_modulus);
	}
	
	/**
	 * Constructor
	 * @param value
	 * @param modulus
	 */
	public ResidueClass(long value, long modulus){ 
		this(BigInteger.valueOf(value), BigInteger.valueOf(modulus));
	}
	
	public ResidueClass() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns the value of the residue class
	 * @return
	 */
	public BigInteger value(){
		return new BInteger(_value, _thread);
	}
	
	/**
	 * Return the modulus value of the residue class
	 * @return
	 */
	public BigInteger modulus(){
		return _modulus;
	}
	
	/**
	 * Sets the value of the residue class
	 * @param value
	 */
	public void setValue(BigInteger value){
		_value = value.mod(_modulus);
	}
	
	/**
	 * Adds the BigInteger value to the Residue class
	 * @param value
	 */
	public void add(BigInteger value){
		_value = _value.add(value).mod(_modulus);
	}
	
	/**
	 * Subtract the ResidueClass value from the Residue class
	 * if compatible
	 * @param rc
	 */
	public void subtract(ResidueClass rc) throws FFaplAlgebraicException{
		if(this.isCompatibleTo(rc)){
			this.add(rc.value().negate());
		}else{
			Object[] arguments ={this.modulus(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
	}
	
	/**
	 * Subtract the BigInteger value from the Residue class
	 * @param value
	 */
	public void subtract(BigInteger value){
		this.add(value.negate());
	}
	
	/**
	 * Adds the ResidueClass value to the Residue class
	 * if compatible
	 * @param rc
	 */
	public void add(ResidueClass rc) throws FFaplAlgebraicException{
		if(this.isCompatibleTo(rc)){
			_value = _value.add(rc.value()).mod(_modulus);
		}else{
			Object[] arguments ={this.modulus(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
	}
	
	/**
	 * Multiplies the BigInteger value to the Residue class
	 * @param value
	 */
	public void multiply(BigInteger value){
		_value = _value.multiply(value).mod(_modulus);
	}
	
	/**
	 * multiplies the ResidueClass value to the Residue class
	 * if compatible
	 * @param rc
	 */
	public void multiply(ResidueClass rc) throws FFaplAlgebraicException{
		if(this.isCompatibleTo(rc)){
			_value = _value.multiply(rc.value()).mod(_modulus);
		}else{
			Object[] arguments ={this.modulus(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
	}
	
	/**
	 * Divides the ResidueClass by the specified BigInteger value
	 * @param value
	 */
	public void divide(BigInteger value) throws FFaplAlgebraicException{
		ResidueClass rc = this.clone();
		rc.setValue(value);
		this.divide(rc);		
	}
	
	/**
	 * Divides the ResidueClass by the specified Residue class
	 * if compatible
	 * @param rc
	 */
	public void divide(ResidueClass rc) throws FFaplAlgebraicException{
		if(this.isCompatibleTo(rc)){
			//multiply the inverse
			this.multiply(rc.inverse());
		}else{
			Object[] arguments ={this.modulus(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
	}
	
	/**
	 * Exponentiate the ResidueClass by the specified BigInteger Value
	 * @param value
	 * @throws FFaplAlgebraicException 
	 */
	public void pow(BigInteger value) throws FFaplAlgebraicException{
		//Algorithm from Montgomery Powering Ladder 
		List<Byte> v  = toByteList(value.abs());
     	BigInteger r0 = BigInteger.ONE;
		BigInteger r1;
		
		if(value.compareTo(BigInteger.ZERO) <= 0){
			 r1 = this.inverse().value();
		}else{
			 r1 = _value;
		}

		for (Byte bit : v) {
			isRunning();
			if (bit == 0) {
				r1 = r0.multiply(r1).mod(_modulus);
				r0 = r0.multiply(r0).mod(_modulus);
			} else {
				r0 = r1.multiply(r0).mod(_modulus);
				r1 = r1.multiply(r1).mod(_modulus);
			}
		}
        _value = r0;	
	}
	
	/**
	 * Exponentiate the ResidueClass by the specified BigInteger Value
	 * @param value
	 * @throws FFaplAlgebraicException 
	 */
	public void pow(long value) throws FFaplAlgebraicException{
		this.pow(BigInteger.valueOf(value));
	}
	
	/**
	 * Converts BigInteger in Bit Vector; Bit representation
	 * of BigInteger 
	 * @param value
	 * @return
	 */
	private List<Byte> toByteList (BigInteger value){
		List<Byte> result = new ArrayList<Byte>();
		for (int i = value.bitLength() - 1; i >= 0; i--) {
			result.add(value.testBit(i) ? (byte) 1 : (byte) 0);
		}
		return result;
	}
	
	/**
	 * Returns true if the actual residue class is compatible
	 * to the specified class <Code> residue </Code>, i.e. module
	 * are equal.
	 * @param rc
	 * @return
	 */
	public boolean isCompatibleTo(ResidueClass rc){
		return _modulus.equals(rc.modulus());
	}
	
    /**
     * Returns true if value of Residue class is equal to the input
     * @param val
     * @return
     * @throws FFaplAlgebraicException 
     */
	public boolean equals(IJavaType val) throws FFaplAlgebraicException{
		if(val instanceof BigInteger){
			return ((BigInteger) val).mod(_modulus).equals(_value);
		}else if(val instanceof ResidueClass){
			if(this.isCompatibleTo((ResidueClass)val)){
				return ((ResidueClass) val).modulus().equals(_modulus);
				
			}else{
				Object[] arguments ={this.modulus(), this.classInfo(), 
			           ((ResidueClass) val).modulus(), val.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
			}
		}else{
			String[] arguments = {"residue class equals operation try to compare " + 
					this.classInfo() + " with " + val.classInfo()};
			throw new FFaplAlgebraicException(arguments ,IAlgebraicError.INTERNAL);
		}		
	}
	
	@Override
	public String toString(){
		return _value.toString();// + " mod " + _modulus;
	}

	@Override
	public ResidueClass clone(){
            try {
                ResidueClass result = new ResidueClass(_modulus);
                result.setValue(_value);
                return result;
            } catch (FFaplAlgebraicException ex) {
                ex.printStackTrace();       //should NEVER happen! But needs to be catched.
            }
            return null;                    //same here
	}
	
	@Override
	public int typeID() {
		return IJavaType.RESIDUECLASS;
	}
	
	/** 
	 * Returns additive inverse of value
	 * @return
	 */
	public ResidueClass negate()  throws FFaplAlgebraicException{
		ResidueClass result = this.clone();
		result.setValue(result.value().negate());
		return result;
	}
	
	@Override
	public String classInfo() {
		return "Z(" + this._modulus + ")";
	}

	/**
	 * Return inverse of Residue Class Value
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public ResidueClass inverse() throws FFaplAlgebraicException{
		ResidueClass rc = this.clone();
		try{
			rc.setValue(this._value.modInverse(this._modulus));
			return rc;
		}catch (ArithmeticException e){
			Object[] arguments ={rc.value(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.NO_MULTINVERSE);
		}
	}

	/**
	 * Modulo Operation
	 * @param rc
	 * @throws FFaplAlgebraicException
	 */
	public void mod(ResidueClass rc) throws FFaplAlgebraicException {
		if(this.isCompatibleTo(rc)){
			if((rc.value()).compareTo(BigInteger.ZERO) <= 0){
				Object[] arguments = {_value + " " +
						FFaplInterpreter.tokenImage[FFaplInterpreter.MODULO].replace("\"", "") + " " + rc.value(), rc.value()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
			}
			this.setValue(this._value.mod(rc.value()));
		}else{
			Object[] arguments ={this.modulus(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		
	}

	/**
	 * Compares this BigInteger with the specified BigInteger. 
	 * This method is provided in preference to individual methods 
	 * for each of the six boolean comparison operators (<, ==, >, >=, !=, <=). 
	 * The suggested idiom for performing these comparisons is: (x.compareTo(y) <op> 0), 
	 * where <op> is one of the six comparison operators.
	 * @param val
	 * @return -1, 0 or 1 as this BigInteger is numerically less than, equal to, or greater than val.
	 */
	public int compareTo(BigInteger val) {
		return _value.compareTo(val.mod(_modulus));
	}
	
	/**
	 * Compares this BigInteger with the specified BigInteger. 
	 * This method is provided in preference to individual methods 
	 * for each of the six boolean comparison operators (<, ==, >, >=, !=, <=). 
	 * The suggested idiom for performing these comparisons is: (x.compareTo(y) <op> 0), 
	 * where <op> is one of the six comparison operators.
	 * @param rc
	 * @return -1, 0 or 1 as this BigInteger is numerically less than, equal to, or greater than val.
	 * @throws FFaplAlgebraicException 
	 */
	public int compareTo(ResidueClass rc) throws FFaplAlgebraicException {
		if(this.isCompatibleTo(rc)){
			return _value.compareTo(rc.value());
		}else{
			Object[] arguments ={this.modulus(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
	}
	
	@Override
	public boolean equalType(Object obj){
		if(obj instanceof ResidueClass){
			return ((ResidueClass)obj).modulus().equals(_modulus);
		}
		return false;
	}
	
	 /**
	  * throws an interrupt exception if not running
	  * @throws FFaplException
	  */
	  protected void isRunning() throws FFaplAlgebraicException
	  {
		if(_thread != null){
			if(_thread.isInterrupted()){
				throw new FFaplAlgebraicException(null, IAlgebraicError.INTERRUPT);
			}
			
		}
	  }
}
