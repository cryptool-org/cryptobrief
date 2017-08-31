/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ffapl.FFaplInterpreter;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.math.Algorithm;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class PolynomialRC extends Polynomial {

	protected BigInteger _characteristic = null;

	/**
	 * Constructor
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRC(BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(thread);
		_characteristic = modulus;
	}

	/**
	 * Constructor
	 * @param polynomTable
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRC(TreeMap<BigInteger, BigInteger> polynomTable, 
			BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(polynomTable, thread);
		_characteristic = modulus;
	}

	/**
	 * Constructor
	 * @param c coefficient
	 * @param e exponent
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRC(BigInteger c, BigInteger e, BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(c.mod(modulus), e, thread);
		_characteristic = modulus;
	}
	
	/**
	 * Returns the pure Polynomial from Z()[x]
	 * @return
	 */
	public Polynomial getPolynomial() {
		return new Polynomial(this.polynomial(), this._thread);
	}
	
	/**
	 * Constructor
	 * @param c
	 * @param e
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRC(long c, long e, long modulus, Thread thread) throws FFaplAlgebraicException{
		super(c % modulus, e, thread);
		_characteristic = BigInteger.valueOf(modulus);
	}
	
	/**
	 * Returns the monic representation of the Polynomial 
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRC getMonic() throws FFaplAlgebraicException{
		PolynomialRC l, result;
		PolynomialRC[] tmp;
		
		if(! this.isMonic()){
			//leading coefficient
			l = new PolynomialRC(this.leadingCoefficient(), BigInteger.ZERO, _characteristic, _thread);
			tmp =  PolynomialRC.divide(this, l);
			result = tmp[0];
			if(! tmp[1].isZero()){
				Object[] arguments ={"Error in PolynomialRC monic function"};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
			}
			//System.out.println(tmp[0] + " + " + tmp[1]);
		}else{
			//already monic
			result = (PolynomialRC) this.clone();
		}
		
		return result;
	}
	
	@Override
	public Polynomial clone(){	
		try {
			return new PolynomialRC(polynomial(), _characteristic, _thread);
		} catch (FFaplAlgebraicException e) {
			return null; //error
		}
	}
	
		
	/**
	 * Returns the characteristic of the Polynomial
	 * @return
	 */
	public BigInteger characteristic(){
		return _characteristic;
	}
	
	/**
	 * @return the thread within the Class was created
	 */
	public Thread getThread(){
		return _thread;
	}
	
	/**
	 * return (ply1 - ply2)
	 * @param ply1
	 * @param ply2
	 * @return (ply1 - ply2)
	 * @throws FFaplAlgebraicException
	 */
	public static PolynomialRC subtract(PolynomialRC ply1, PolynomialRC ply2) throws FFaplAlgebraicException{
		PolynomialRC ply = new PolynomialRC(ply1._polynomialMap, ply1.characteristic(), ply1.getThread());
		ply.subtract(ply2);		
		return ply;
	}
	
	/**
	 * return (ply1 + ply2)
	 * @param ply1
	 * @param ply2
	 * @return (ply1 + ply2)
	 * @throws FFaplAlgebraicException
	 */
	public static PolynomialRC add(PolynomialRC ply1, PolynomialRC ply2) throws FFaplAlgebraicException{
		PolynomialRC ply = new PolynomialRC(ply1._polynomialMap, ply1.characteristic(), ply1.getThread());
		ply.add(ply2);		
		return ply;
	}
	
	
	/**
	 * returns ply1 * ply2
	 * @param ply1
	 * @param ply2
	 * @return (ply1 * ply2)
	 * @throws FFaplAlgebraicException
	 */
	public static PolynomialRC multiply(PolynomialRC ply1, PolynomialRC ply2) throws FFaplAlgebraicException{
		PolynomialRC ply = new PolynomialRC(ply1._polynomialMap, ply1.characteristic(), ply1.getThread());
		ply.multiply(ply2);		
		return ply;
	}
	
	/**
	 * Divides a polynomial of a residue class with this polynomial
	 * @param polinomial
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal 
	 */
	public static PolynomialRC[] divide(PolynomialRC ply1, PolynomialRC ply2) throws FFaplAlgebraicException{
		PolynomialRC r, p, tmp;
		BigInteger lambda, my;//leading coefficient
		BigInteger n, m; //degrees
		BigInteger c; //characteristic
		PolynomialRC[] result = new PolynomialRC[2]; //0=result 1=rest
		//check if same characteristic
		if(! ply1.characteristic().equals(ply2.characteristic())){
			Object[] arguments ={ply1.characteristic(), ply1.classInfo(), 
					ply2.characteristic(), ply2.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		
		//Algorithm according Endliche K�rper von Hans Kurzweil 2.5
			c = ply1.characteristic();
			r = (PolynomialRC) ply1.clone();
			p = (PolynomialRC) ply1.clone();
			p.setPolynomial(BigInteger.ZERO, BigInteger.ZERO);
			lambda = ply2.leadingCoefficient();
			n = ply2.degree();
			try{
				while(r.degree().compareTo(n) >= 0 && r.leadingCoefficient().compareTo(BigInteger.ZERO) > 0){
					m = r.degree();
					my = r.leadingCoefficient();
					//is ~~ my/lambda X^(m-n)~~
					tmp = new PolynomialRC(
							my.multiply(lambda.modInverse(c)),
							m.subtract(n),
							c, ply1.getThread());
					p.add(tmp);
					tmp.multiply(ply2);
					r.subtract(tmp);
					//p =  (PolynomialRC) Polynomial.minimizePolynomial(p);
					//r =  (PolynomialRC) Polynomial.minimizePolynomial(r);
				}
			}catch(ArithmeticException e){
				Object[] arguments ={ply2, ply2.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.NO_MULTINVERSE_COEFF); 
			}
			result[0] = p;
			result[1] = r;
		
		return result;
	}
	
	/**
	 * Adds the specified Polynomial <Code> ply </Code> to the current Polynom
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 */
	public void add(PolynomialRC ply) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(ply.characteristic())){
			Object[] arguments ={this.characteristic(), this.classInfo(), 
					ply.characteristic(), ply.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.add(ply);
	}
	
	/**
	 * Add
	 * @param rc
	 * @throws FFaplAlgebraicException 
	 */
	public void add(ResidueClass rc) throws FFaplAlgebraicException {
		if(! this.characteristic().equals(rc.modulus())){
			Object[] arguments ={this.characteristic(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.add(rc.value(), BigInteger.ZERO);
	}
	
	/**
	 * subtracts the specified Polynomial ring <Code> ply </Code> from the current Polynomial
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 */
	public void subtract(PolynomialRC ply) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(ply.characteristic())){
			Object[] arguments ={this.characteristic(), this.classInfo(), 
		            ply.characteristic(), ply.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.subtract(ply);
	}
	
	/**
	 * subtracts the specified residue class <Code> rc </Code> from the current Polynomial
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 */
	public void subtract(ResidueClass rc) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(rc.modulus())){
			Object[] arguments ={this.characteristic(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		
		super.subtract(rc.value(), BigInteger.ZERO);
	}
	
	/**
	 * Multiplies the specified Polynomial <Code>ply</Code>
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 */
	public void multiply(PolynomialRC ply) throws FFaplAlgebraicException{
			if(! this.characteristic().equals(((PolynomialRC)ply).characteristic())){
				Object[] arguments ={this.characteristic(), this.classInfo(), 
			            ply.characteristic(), ply.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
			}
		super.multiply(ply);
	}
	
	/**
	 * Multiplies the specified residue class value <Code>rc</Code>
	 * @param rc
	 * @throws FFaplAlgebraicException
	 */
	public void multiply(ResidueClass rc) throws FFaplAlgebraicException {
		if(! this.characteristic().equals(rc.modulus())){
			Object[] arguments ={this.characteristic(), this.classInfo(), 
		            rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.multiply(rc.value(), BigInteger.ZERO);
	}
	
	@Override
	public void divide(Polynomial ply) throws FFaplAlgebraicException{
		PolynomialRC plyrc;
		if(ply instanceof PolynomialRC){
			plyrc = (PolynomialRC) ply;
		}else{
			plyrc = (PolynomialRC) this.clone();
			plyrc.setPolynomial(ply.polynomial());
		}
		this.setPolynomial(PolynomialRC.divide(this, plyrc)[0].polynomial());
	}
	
	/**
	 * Divide by ResidueClass
	 * @param rc
	 * @throws FFaplAlgebraicException
	 */
	public void divide(ResidueClass rc) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(rc.modulus())){
			Object[] arguments ={this.characteristic(), this.classInfo(), 
					rc.modulus(), rc.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		this.divide(rc.value());
	}
	
	@Override
	public void divide(BigInteger divisor) throws FFaplAlgebraicException{
		ResidueClass rc = new ResidueClass(divisor, this._characteristic);
		multiply(rc.inverse()._value, BigInteger.ZERO);
	}
	
	@Override
	public void mod(Polynomial ply) throws FFaplAlgebraicException {	
		PolynomialRC plyrc;
		if(ply instanceof PolynomialRC){
			if(! this.characteristic().equals(((PolynomialRC)ply).characteristic())){
				Object[] arguments ={this.characteristic(), this.classInfo(), 
						((PolynomialRC)ply).characteristic(), ply.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
			}
			plyrc = (PolynomialRC) ply;
		}else{
			plyrc = (PolynomialRC) this.clone();
			plyrc.setPolynomial(ply.polynomial());
		}
		this.setPolynomial(PolynomialRC.divide(this, plyrc)[1].polynomial());
	}
	
	@Override
	public Polynomial pow(BigInteger exponent) throws FFaplAlgebraicException{
		ResidueClass rc;
		if(exponent.compareTo(BigInteger.ZERO) < 0){
			if(this.degree().equals(BigInteger.ZERO)){
				rc = new ResidueClass(leadingCoefficient(), _characteristic);
				this.setPolynomial(rc.inverse().value(), BigInteger.ZERO);
			}else{
				Object[] arguments = {exponent, 
						 this.classInfo() + " -> " + this + 
								FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + exponent };
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.NEGATIVE_EXPONENT);
			}
		}
		this.setPolynomial(Algorithm.squareAndMultiply(this, exponent.abs()).polynomial());
		return this;
	}
	
	
	
	@Override
	public boolean equals(IJavaType val) throws FFaplAlgebraicException{
		PolynomialRC ply;
		
		if(val instanceof BigInteger){
			ply = new PolynomialRC((BigInteger) val, BigInteger.ZERO, _characteristic, _thread);
		}else if(val instanceof PolynomialRC){
			ply = (PolynomialRC) val;
			if(! this.characteristic().equals(ply.characteristic())){
				Object[] arguments ={this.characteristic(), this.classInfo(), 
						ply.characteristic(), ply.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
			}
		}else if(val instanceof Polynomial){
			ply = new PolynomialRC(((Polynomial) val).polynomial(), _characteristic, _thread);
		}else if (val instanceof ResidueClass) {
			if(! this.characteristic().equals(((ResidueClass) val).modulus())){
				Object[] arguments ={this.characteristic(), this.classInfo(), 
						((ResidueClass) val).modulus(), val.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
			}
			ply = new PolynomialRC(((ResidueClass) val).value(), BigInteger.ZERO, _characteristic, _thread);
		}else{
			String[] arguments = {"polynomial ring equals operation try to compare " + 
					this.classInfo() + " with " + val.classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		}		
		return plyequal(ply);
	}
	
	/**
	 * @param val
	 * @return the result of the polynomial if x = val
	 * @throws FFaplAlgebraicException 
	 */
	public BigInteger calculate(BInteger val) throws FFaplAlgebraicException {
		BigInteger e;
		ResidueClass c, result, x;
		result = new ResidueClass(_characteristic);
		c = new ResidueClass(_characteristic);
		x = new ResidueClass(_characteristic);
		for(Iterator<BigInteger> itr = _polynomialMap.keySet().iterator(); itr.hasNext(); ){
			e = itr.next();
			x.setValue(val);
			c.setValue(_polynomialMap.get(e));
			
			//fix by Johannes Winkler to prevent 0^0
			if (x.compareTo(BigInteger.ZERO) == 0 && e.compareTo(BigInteger.ZERO) == 0)
			{
				//nothing happens
			}
			else
			{
				x.pow(e);
				c.multiply(x);
			}
			result.add(c);
		}
		return result.value();
	}
	
	/**
	 * Calculates coefficients modulus characteristic
	 */
	@SuppressWarnings("unchecked")
	protected void postTask(){
		BigInteger e;
		BigInteger c;
		Set<BigInteger> keyset;
		keyset = (Set<BigInteger>) (new TreeSet<BigInteger>(_polynomialMap.keySet())).clone();
		for(Iterator<BigInteger> itr = keyset.iterator(); itr.hasNext(); ){
			e = itr.next();
			c = this._polynomialMap.get(e).mod(_characteristic);
			if(!c.equals(BigInteger.ZERO)){
				this._polynomialMap.put(e, c);
			}else{
				this._polynomialMap.remove(e);
			}
		}
		//super.postTask();
	}
	
	@Override
	public String toString(){
		return super.toString();
	}
	
	@Override
	public int typeID() {
		return IJavaType.POLYNOMIALRC;
	}

	@Override
	public String classInfo() {
		return "Z(" + this._characteristic + ")[x]";
	}

	@Override
	public boolean equalType(Object type) {
		if(type instanceof PolynomialRC){
			return _characteristic.equals(((PolynomialRC) type).characteristic());
		}
		return false;
	}
	
	

}
