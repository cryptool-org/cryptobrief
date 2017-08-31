/**
 * 
 */
package ffapl.java.classes;

import java.math.BigInteger;
import java.util.TreeMap;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class PolynomialRCPrime extends PolynomialRC {

	
	
	/**
	 * Constructor
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRCPrime(BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(new Prime(modulus, thread), thread);
	}

	/**
	 * Constructor
	 * @param polynomTable
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRCPrime(TreeMap<BigInteger, BigInteger> polynomTable, 
			BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(polynomTable, new Prime(modulus, thread), thread);
	}

	/**
	 * Constructor
	 * @param c coefficient
	 * @param e exponent
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRCPrime(BigInteger c, BigInteger e, BigInteger modulus, Thread thread) throws FFaplAlgebraicException{
		super(c, e, new Prime(modulus, thread), thread);
	}
	
	/**
	 * Constructor
	 * @param c
	 * @param e
	 * @param modulus
	 * @param thread
	 * @throws FFaplAlgebraicException
	 */
	public PolynomialRCPrime(long c, long e, long modulus, Thread thread) throws FFaplAlgebraicException{
		this(BigInteger.valueOf(c), BigInteger.valueOf(e), BigInteger.valueOf(modulus), thread);
	}
	
	@Override
	public Polynomial clone(){	
		try {
			return new PolynomialRCPrime(polynomial(), _characteristic, _thread);
		} catch (FFaplAlgebraicException e) {
			return null; //error
		}
	}
	
	@Override
	public int typeID() {
		return IJavaType.POLYNOMIALRCPRIME;
	}

	
	/**
	 * Returns the monic representation of the Polynomial 
	 * @return
	 * @throws FFaplAlgebraicException
	 * /
	public PolynomialRCPrime getMonic() throws FFaplAlgebraicException{
		PolynomialRCPrime l, result;
		PolynomialRCPrime[] tmp;
		
		if(! this.isMonic()){
			//leading coefficient
			l = new PolynomialRCPrime(this.leadingCoefficient(), BigInteger.ZERO, _characteristic);
			tmp =  PolynomialRCPrime.divide(this, l);
			result = tmp[0];
			if(! tmp[1].isZero()){
				throw new FFaplAlgebraicException("Error in monic function: " + tmp[1], IAlgebraicError.INTERNAL);
			}
			//System.out.println(tmp[0] + " + " + tmp[1]);
		}else{
			//already monic
			result = (PolynomialRCPrime) this.clone();
		}
		
		return result;
	}
	
	
	
		
	/**
	 * Returns the characteristic of the Polynomial
	 * @return
	 * /
	public BigInteger characteristic(){
		return this._characteristic;
	}
	
	/**
	 * return (ply1 - ply2)
	 * @param ply1
	 * @param ply2
	 * @return (ply1 - ply2)
	 * @throws FFaplAlgebraicException
	 * /
	public static PolynomialRCPrime subtract(PolynomialRCPrime ply1, PolynomialRCPrime ply2) throws FFaplAlgebraicException{
		PolynomialRCPrime ply = new PolynomialRCPrime(ply1._polynomTable, ply1.characteristic());
		ply.subtract(ply2);		
		return ply;
	}
	
	/**
	 * return (ply1 + ply2)
	 * @param ply1
	 * @param ply2
	 * @return (ply1 + ply2)
	 * @throws FFaplAlgebraicException
	 * /
	public static PolynomialRCPrime add(PolynomialRCPrime ply1, PolynomialRCPrime ply2) throws FFaplAlgebraicException{
		PolynomialRCPrime ply = new PolynomialRCPrime(ply1._polynomTable, ply1.characteristic());
		ply.add(ply2);		
		return ply;
	}
	
	
	/**
	 * returns ply1 * ply2
	 * @param ply1
	 * @param ply2
	 * @return (ply1 * ply2)
	 * @throws FFaplAlgebraicException
	 * /
	public static PolynomialRCPrime multiply(PolynomialRCPrime ply1, PolynomialRCPrime ply2) throws FFaplAlgebraicException{
		PolynomialRCPrime ply = new PolynomialRCPrime(ply1._polynomTable, ply1.characteristic());
		ply.multiply(ply2);		
		return ply;
	}

	/**
	 * Divides a polynomial of a residue class with this polynomial
	 * @param polinomial
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal 
	 * /
	public static PolynomialRCPrime[] divide(PolynomialRCPrime ply1, PolynomialRCPrime ply2) throws FFaplAlgebraicException{
		PolynomialRCPrime r, p, tmp;
		BigInteger lambda, my;//leading coefficient
		BigInteger n, m; //degrees
		BigInteger c; //characteristic
		PolynomialRCPrime[] result= new PolynomialRCPrime[2]; //0=result 1=rest
		
		//check if same characteristic
		if(! ply1.characteristic().equals(ply2.characteristic())){
			throw new FFaplAlgebraicException("Characteristic '" + ply1.characteristic() + 
					"' of polynomial [" + ply1 + "] is not equal to characteristic '" + ply2.characteristic() + 
					"' of polynomial [" + ply2 + "]", IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		
		//Algorithm according Endliche Körper von Hans Kurzweil 2.5
		c = ply1.characteristic();
		r = (PolynomialRCPrime) ply1.clone();
		p = new PolynomialRCPrime(c);
		lambda = ply2.leadingCoefficient();
		n = ply2.degree();
		while(r.degree().compareTo(n) >= 0 && r.leadingCoefficient().compareTo(BigInteger.ZERO) > 0){
			m = r.degree();
			my = r.leadingCoefficient();
			//is ~~ my/lambda X^(m-n)~~
			tmp = new PolynomialRCPrime(
					my.multiply(lambda.modInverse(c)),
					m.subtract(n),
					c);
			p.add(tmp);
			tmp.multiply(ply2);
			r.subtract(tmp);
			//p =  (PolynomialRCPrime) Polynomial.minimizePolynomial(p);
			//r =  (PolynomialRCPrime) Polynomial.minimizePolynomial(r);
		}
		result[0] = p;
		result[1] = r;
		return result;
	}
	
	/**
	 * Adds the specified Polynom <Code> ply </Code> to the current Polynom
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 * /
	public void add(PolynomialRCPrime ply) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(ply.characteristic())){
			throw new FFaplAlgebraicException("Characteristic '" + this.characteristic() + 
					"' of polynomial [" + this + "] is not equal to characteristic '" + ply.characteristic() + 
					"' of polynomial [" + ply + "]", IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.add(ply);
	}
	
	/**
	 * subtracts the specified Polynom <Code> ply </Code> from the current Polynom
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 * /
	public void subtract(PolynomialRCPrime ply) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(ply.characteristic())){
			throw new FFaplAlgebraicException("Characteristic '" + this.characteristic() + 
					"' of polynomial [" + this + "] is not equal to characteristic '" + ply.characteristic() + 
					"' of polynomial [" + ply + "]", IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.subtract(ply);
	}
	
	/**
	 * Multiplies the specified Polynom <Code>ply</Code> to the specified
	 * @param ply
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
	 * /
	public void multiply(PolynomialRCPrime ply) throws FFaplAlgebraicException{
		if(! this.characteristic().equals(ply.characteristic())){
		    throw new FFaplAlgebraicException("Characteristic '" + this.characteristic() + 
				"' of polynomial [" + this + "] is not equal to characteristic '" + ply.characteristic() + 
				"' of polynomial [" + ply + "]", IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		super.multiply(ply);
	}
	
	/**
	 * Returns true if polynomials are equal
	 * @param ply
	 * @return
	 * /
	public boolean equals(PolynomialRCPrime ply){
		if(_characteristic.equals(ply.characteristic())){
			return super.equals(ply);
		}else{
			//characteristics unequal
			return false;
		}
	}
	
	/**
	 * Calculates coefficients modulus characteristic
	 * /
	@SuppressWarnings("unchecked")
	protected void postTask(){
		BigInteger e;
		BigInteger c;
		TreeSet<BigInteger> keyset;
		keyset = (TreeSet<BigInteger>) _keyset.clone();
		for(Iterator<BigInteger> itr = keyset.iterator(); itr.hasNext(); ){
			e = itr.next();
			c = this._polynomTable.get(e).mod(_characteristic);
			if(!c.equals(BigInteger.ZERO)){
				this._polynomTable.put(e, c);
			}else{
				this._polynomTable.remove(e);
				this._keyset.remove(e);
			}
		}
		//super.postTask();
	}
*/
}
