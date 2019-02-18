/**
 * 
 */
package ffapl.java.classes;

import ffapl.exception.FFaplException;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigInteger.*;

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

	/**
	 * Checks if a polynomial is primitive mod a Prime p (the modulus of this polynomial).
	 * <p>
	 * From: COMPUTING PRIMITIVE POLYNOMIALS - THEORY AND ALGORITHM <br />
	 * By: Sean E. O'Connor <br />
	 * URL: http://www.seanerikoconnor.freeservers.com/Mathematics/AbstractAlgebra/PrimitivePolynomials/theory.html#AlgoforFinding
	 *
	 * @param _thread
	 * @return true if f is a primitive polynomial mod p
	 */
	public boolean isPrimitivePolynomial(Thread _thread)
			throws FFaplException {
		return isPrimitivePolynomial(null, null, _thread);
	}

	/**
	 * Checks if a polynomial is primitive mod a Prime p (the modulus of this polynomial).
	 * Accepts precomputed factorizations of r and p-1 to speed up checking multiple polynomials with the same parameters.
	 * <p>
	 * From: COMPUTING PRIMITIVE POLYNOMIALS - THEORY AND ALGORITHM <br />
	 * By: Sean E. O'Connor <br />
	 * URL: http://www.seanerikoconnor.freeservers.com/Mathematics/AbstractAlgebra/PrimitivePolynomials/theory.html#AlgoforFinding
	 *
	 * @param factorsOfR         precomputed factorization of r := (p^n - 1)/(p - 1)
	 * @param factorsOfPMinusOne precomputed factorization of (p - 1)
	 * @return true if f is a primitive polynomial mod p
	 */
	public boolean isPrimitivePolynomial(Map<BigInteger, BigInteger> factorsOfR, Map<BigInteger, BigInteger> factorsOfPMinusOne, Thread _thread)
			throws FFaplException {

		BigInteger p = this.characteristic();
		BigInteger n = this.degree();
		BigInteger pMinusOne = p.subtract(ONE);
		BigInteger r = p.pow(n.intValue()).subtract(ONE).divide(pMinusOne);

		if (factorsOfR == null)
			factorsOfR = Algorithm.FactorInteger(new BInteger(r, _thread));
		if (factorsOfPMinusOne == null)
			factorsOfPMinusOne = Algorithm.FactorInteger(new BInteger(pMinusOne, _thread));

		// [Step 2]
		BigInteger a0 = this.coefficientAt(ZERO);
		// a_0 * (-1)^n
		BigInteger a0TimesMinusOneToTheN = (n.mod(TWO).compareTo(ZERO) == 0) ? a0 : a0.negate();
		// create galois field element object with value
		GaloisField tmpGF = new GaloisField(p, new Polynomial(1, 1, _thread), _thread);
		tmpGF.setValue(new Polynomial(a0TimesMinusOneToTheN, ZERO, _thread));

		// assert that a_0 * (-1)^n is a primitive root of p
		if (!tmpGF.isPrimitiveRoot(factorsOfPMinusOne))
			return false;

		// [Step 3]
		// assert that f has no linear factors (as linear factors imply reducibility)
		if (this.hasLinearFactors())
			return false;

		// [Step 4]
		// apply berlekamp polynomial factorization to check for reducibility
		// assert that the Q-matrix of f has a nullity less than 2 (nullity of two or greater implies reducibility)
		if (this.findQMatrix(_thread).nullity(2) == 2)
			return false;

		// [Step 5]
		// assert that x^r is an integer (a) without any other coefficients
		GaloisField x = new GaloisField(p, this, _thread);
		// x = 1 * x^1
		x.setValue(new Polynomial(ONE, ONE, _thread));
		// x^r
		GaloisField xToTheR = x.powR(r);
		// assert that x^r === a, where a is an integer
		if (xToTheR.value().degree().compareTo(ZERO) != 0)
			return false;

		// [Step 6]
		BigInteger a = xToTheR.value().coefficientAt(ZERO);
		// assert that a === (-1)^n * a0 mod f, p
		if (!a.equals(a0TimesMinusOneToTheN))
			return false;

		// [Step 7]
		for (BigInteger factor : factorsOfR.keySet())
			// skip test if p_i | (p-1) (divides)
			if (!factor.mod(pMinusOne).equals(ZERO))
				// otherwise, assert that x^(r/p_i) is not an integer
				if (x.powR(r.divide(factor)).value().degree().compareTo(ZERO) == 0)
					return false;

		// [Step 8]
		return true;
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
