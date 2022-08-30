package ffapl.java.math;

import ffapl.FFaplInterpreter;
import ffapl.exception.FFaplException;
import ffapl.exception.FFaplWarning;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.ILevel;
import ffapl.java.interfaces.IRandomGenerator;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.math.isomorphism.calculation.linearfactor.PolynomialGF;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static java.math.BigInteger.*;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Algorithms needed for Algebraic calculations
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Algorithm {

	//for prime evaluation
	//private static int _certainty = 100;

	/**
	 * Cache for results of integer factorizations.
	 * Implemented as a nested TreeMap.
	 * Contains mappings of the form
	 * <p>
	 * n -> {p1->e1,p2->e2,...,pn->en},
	 * where n = p1^e1 * p2^e2 * ... * pn^en.
	 * <p>
	 * Furthermore, a mapping of n -> null indicates that n is composite,
	 * but no factorization is given. This happens, if a number is checked for primality only,
	 * as a full factorization is much more computation-intensive.
	 */
	private static TreeMap<BigInteger, TreeMap<BigInteger, BigInteger>> factorizationCache;

	/**
	 * Cache for results of tests for irreducibility on polynomials.
	 * Implemented as a HashMap.
	 * Contains mappings of the form
	 * <p>
	 * f -> irreducible(f),
	 * where irreducible(f) is true iff f is an irreducible polynomial modulo its module.
	 */
	private static HashMap<PolynomialRC, Boolean> irreduciblePolyCache;
	
	private static byte[] zeroPadding(BigInteger k) {
		byte[] keyInput = k.toByteArray();
    	byte[] keyBytes = null;
       	int len = 0;
    	if (keyInput.length <= 16) {
    		len = 16;
    	}
    	else if (keyInput.length > 16 && keyInput.length <= 24) {
    		len = 24;
    	}
    	else if (keyInput.length > 24) {
    		len = 32;
    	}
    	keyBytes = new byte[len];
		System.arraycopy(keyInput, 0, keyBytes, len - keyInput.length, keyInput.length);
		return keyBytes;    	
	}
	
	/**
	 * AES encryption of m under k in ECB mode (with standardized padding); note that we use the ECB mode here to avoid having to pass an initialization vector along with the key (simplifies matters for students)
	 * @param m message (as an integer)
	 * @param k key as an integer (depending on the bitsize, either AES128, AES192 or AES256 is used, with zero padding on the most significant bits. 
	 * For keys larger than 256 bit, the higher order bits are discarded
	 * @return the AES ciphertext as an 128 bit integer
	 */
	public static BigInteger AESEncrypt(BigInteger m, BigInteger k, boolean withPadding, BigInteger IV) throws FFaplAlgebraicException {
		BigInteger result = null;
        try {
        	Cipher cipher = Cipher.getInstance((withPadding) ? "AES/CBC/PKCS5Padding" : "AES/ECB/NoPadding");
        	
        	// depending on the length of the key, we choose a different AES instance
        	byte[] kb = zeroPadding(k);
    		SecretKeySpec secret = new SecretKeySpec(kb, 0, kb.length, "AES");
    		IvParameterSpec ivspec = null;
    		if (IV != null) {
    			ivspec = new IvParameterSpec(zeroPadding(IV));
    		}
    		
			
			byte[] input = m.toByteArray();
			if (!withPadding) {
				// without padding, we work only on a single block, so throw an exception for anything more than 16 bytes
				if (input.length > 16) {
					throw new FFaplAlgebraicException(null, IAlgebraicError.ILLEGAL_AES_INPUT);
				}
				cipher.init(Cipher.ENCRYPT_MODE, secret);
			}
			else {
				cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
			}
			if (input.length < 16 && !withPadding) { 
				input = zeroPadding(m);
			}
			
			result = new BigInteger(cipher.doFinal(input));
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return result;
	}
	
	/**
	 * AES decryption of m under k in ECB mode (with standardized padding); note that we use the ECB mode here to avoid having to pass an initialization vector along with the key (simplifies matters for students)
	 * @param c ciphertext (as an integer)
	 * @param k key as an integer (depending on the bitsize, either AES128, AES192 or AES256 is used, with zero padding on the most significant bits. 
	 * For keys larger than 256 bit, the higher order bits are discarded
	 * @return the decrypted plaintext as an integer
	 * @throws FFaplAlgebraicException in case that the input block for decryption is too short (< 128 bit)
	 */
	public static BigInteger AESDecrypt(BigInteger c, BigInteger k, boolean withPadding, BigInteger IV) throws FFaplAlgebraicException {
		BigInteger result = null;
        try {
        	Cipher cipher = Cipher.getInstance((withPadding) ? "AES/CBC/PKCS5Padding" : "AES/ECB/NoPadding");
        	
        	// depending on the length of the key, we choose a different AES instance
        	byte[] kb = zeroPadding(k);
    		SecretKeySpec secret = new SecretKeySpec(kb, 0, kb.length, "AES");
    		IvParameterSpec ivspec = null;
    		if (IV != null) {
    			ivspec = new IvParameterSpec(zeroPadding(IV));
    		}
		
			byte[] input = c.toByteArray();
			if (!withPadding) {
				// without padding, we work only on a single block, so throw an exception for anything less or more than 16 bytes
				if (input.length > 16) {
					throw new FFaplAlgebraicException(null, IAlgebraicError.ILLEGAL_AES_INPUT);
				}
				cipher.init(Cipher.DECRYPT_MODE, secret);
			}
			else {
				cipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
			}
			if (input.length < 16 && !withPadding) { input = zeroPadding(c); }
			
			result = new BigInteger(cipher.doFinal(input));
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			throw new FFaplAlgebraicException(null, IAlgebraicError.ILLEGAL_AES_INPUT);
			//e.printStackTrace();
		} catch (BadPaddingException e) {
			throw new FFaplAlgebraicException(null, IAlgebraicError.ILLEGAL_AES_INPUT);
			//e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		return result;
	}
	
	/**
	 * Great common divisor of g(x) and h(x)
	 * see: Handbook of applied Cryptography Algorithm: 2.218
	 * Euclidean gcd 
	 * @param g
	 * @param h
	 * @return
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of g and h are unequal
	 */
	public static PolynomialRC gcd(PolynomialRC g, 
			               PolynomialRC h) throws FFaplAlgebraicException{
		PolynomialRC r;
		Thread thread = g.getThread();
		if(! g.characteristic().equals(h.characteristic())){
			Object[] messages ={g.characteristic(), g.classInfo(), 
		            h.characteristic(), h.classInfo()};
			throw new FFaplAlgebraicException(messages, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		
		while(! h.isZero()){
			isRunning(thread);
			r = PolynomialRC.divide(g, h)[1];
			g = h;
			h = r;
		}
		
		return g;
	}

	/**
	 * Great common divisor of g(x) and h(x)
	 * based on: Handbook of applied Cryptography Algorithm: 2.218
	 * Euclidean gcd 
	 * @param g
	 * @param h
	 * @return
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of g and h are unequal
	 */
	public static BInteger gcd(BInteger g, 
			               BInteger h) throws FFaplAlgebraicException{
		BInteger r;
		Thread thread = g.getThread();
				
		while(! h.equals(ZERO)){
			isRunning(thread);
			r = (BInteger) g.mod(h);
			g = h;
			h = r;
		}
		return g;
	}
	
	/**
	 * Extended Euclidean Algorithm for Z()[x]
	 * see: Handbook of applied Cryptography Algorithm: 2.221
	 * @param g
	 * @param h
	 * @return an array with(d,s,t) were  s*g + t*h = gcd(g,h) = d
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of g and h are unequal 
	 */
	public static PolynomialRC[] eea(PolynomialRC g, PolynomialRC h) throws FFaplAlgebraicException{
		PolynomialRC d, s, t, q, r, s1, s2, t1, t2, ht;
		BInteger dinverse;
		PolynomialRC[] tmp, result;
		Thread thread = g.getThread();
		if(! g.characteristic().equals(h.characteristic())){
			Object[] messages ={g.characteristic(), g.classInfo(), 
		            h.characteristic(), h.classInfo()};
			throw new FFaplAlgebraicException(messages, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		
		ht = (PolynomialRC) h.clone();
		
		if(h.isZero()){
			d = (PolynomialRC) g.clone();
			s = new PolynomialRC(ONE, ZERO, g.characteristic(), thread);
			t = new PolynomialRC(ZERO, ZERO, g.characteristic(), thread);
			
		}else{
			try{
				s2 = new PolynomialRC(ONE, ZERO, g.characteristic(), thread);
				s1 = new PolynomialRC(ZERO, ZERO, g.characteristic(), thread);
				t2 = (PolynomialRC) s1.clone();
				t1 = (PolynomialRC) s2.clone();
				while(!h.isZero()){
					isRunning(thread);//if Thread is interrupted
					tmp = PolynomialRC.divide(g, h);
					q = tmp[0];
					r = tmp[1];//rest
					s = PolynomialRC.subtract(s2, PolynomialRC.multiply(q, s1));
					t = PolynomialRC.subtract(t2, PolynomialRC.multiply(q, t1));
					g = h;
					h = r;
					s2 = s1;
					s1 = s;
					t2 = t1;
					t1 = t; 
				}
				d = g;
				s = s2;
				t = t2;
			}catch(ArithmeticException e){
				Object[] arguments ={ht, ht.classInfo()};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.NO_MULTINVERSE_COEFF); 
			}
		}
		result = new PolynomialRC[3];
		
		//minimize
		if(! d.isOne()){
			//d <> 1
			if(d.degree().compareTo(ZERO) == 0){
				try{
					dinverse = new BInteger(d.polynomial().get(ZERO).modInverse(d.characteristic()), ht.getThread());
					d.multiply(dinverse, ZERO);
					s.multiply(dinverse, ZERO);
					t.multiply(dinverse, ZERO);
				}catch(ArithmeticException e){
					//do nothing 
				}
			}
		}
		
		result[0] = d;
		result[1] = s;
		result[2] = t;
		return result;
	}
	
	/**
	 * Extended Euclidean Algorithm for Integer
	 * see: Handbook of applied Cryptography Algorithm: 2.107
	 * @param a
	 * @param b
	 * @return an array with(d,s,t) were  s*a + t*b = gcd(a,b) = d
	 */
	public static BInteger[] eea(BInteger a, BInteger b) throws FFaplAlgebraicException{
		BInteger d, x, y;
		BInteger x1, x2, y1, y2, q, r;
		BInteger[] result = new BInteger[3];
		
		if(b.equals(ZERO)){
			d = a;
			x = new BInteger(ONE, a.getThread());
			y = new BInteger(ZERO, a.getThread());
		}else{
			x2 = new BInteger(ONE, a.getThread());
			x1 = new BInteger(ZERO, a.getThread());
			y2 = new BInteger(ZERO, a.getThread());
			y1 = new BInteger(ONE, a.getThread());
			
			while (b.compareTo(ZERO) > 0){
				q = (BInteger) a.divide(b);
				r = (BInteger) a.subtract(q.multiply(b));
				x = (BInteger) x2.subtract(q.multiply(x1));
				y = (BInteger) y2.subtract(q.multiply(y1));
				
				a = b;
				b = r;
				x2 = x1;
				x1 = x;
				y2 = y1;
				y1 = y;				
			}
			d = a;
			x = x2;
			y = y2;
			
		}
		
		result[0] = d;
		result[1] = x;
		result[2] = y;
		
		return result;
	}
		
	
	
	
	
	/**
	 * Repeated square-and-multiply algorithm for exponentiation in F_p^m
	 * calculates g^k mod f
	 * see: Handbook of applied Cryptography Algorithm: 2.227
	 * @param g polynomial
	 * @param k exponent
	 * @param f polynomial
	 * @return
	 * @throws FFaplAlgebraicException
	 *         <CHARACTERISTIC_UNEQUAL> if characteristics of g and f are unequal
	 * @throws FFaplAlgebraicException        
	 *         <INTERRUPTED> if execution is interrupted within thread
	 */
	
	
	
	public static PolynomialRC squareAndMultiply(PolynomialRC g, 
			 BigInteger k, PolynomialRC f) throws FFaplAlgebraicException{
		PolynomialRC s, G;
		Thread thread = g.getThread();
		if(! g.characteristic().equals(f.characteristic())){
			Object[] messages ={g.characteristic(), g.classInfo(), 
		            f.characteristic(), f.classInfo()};
			throw new FFaplAlgebraicException(messages, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
		}
		//s=1
		s = new PolynomialRC(ONE, ZERO, g.characteristic(), thread);
		//If k = 0 then return (s)
		if(k.equals(ZERO)){
			return s;
		}
		//G <- g
		G = (PolynomialRC) g.clone();
		
		//if k0 = 1 then s <- g
		//if(k.testBit(k.bitLength() - 1)){
	    if(k.testBit(0)){
			s = (PolynomialRC) g.clone();
		}
		
		for (int i = 1; i < k.bitLength() ; i++){
			//G <- G^2 mod f
			isRunning(thread);
			G.multiply(G);
			G = PolynomialRC.divide(G, f)[1];
			//System.out.println("i " + i + " bit:" + k.testBit(i));
			//if ki = 1
			if(k.testBit(i)){
				//s <- G*s mod f
				s.multiply(G);
				s = PolynomialRC.divide(s, f)[1];
			}
		}		
		return s;
	}


	/**
	 * Repeated square-and-multiply algorithm for exponentiation
	 * calculates g^k
	 * @param g
	 * @param k
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public static PolynomialGF squareAndMultiply(PolynomialGF g,
											   BigInteger k) throws FFaplAlgebraicException{
		PolynomialGF s, G;
		Thread thread = g.getThread();
		//s=1
		GaloisField oneCoefficient = g.field();
		oneCoefficient.setValue(new Polynomial(ONE, ZERO, thread));
		s = new PolynomialGF(oneCoefficient, ZERO, g.field(), thread);;

		//If k = 0 then return (s)
		if(k.equals(ZERO)){
			return s;
		}
		//G <- g
		G = g.clone();

		if(k.testBit(0)){
			s = g.clone();
		}

		for (int i = 1; i < k.bitLength() ; i++){
			//G <- G^2
			isRunning(thread);//to interrupt calculation
			G.multiply(G);
			//if ki = 1
			if(k.testBit(i)){
				//s <- G*s
				s.multiply(G);
			}
		}
		return s;
	}


	/**
	 * Repeated square-and-multiply algorithm for exponentiation 
	 * calculates g^k
	 * @param g
	 * @param k
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public static Polynomial squareAndMultiply(Polynomial g, 
			 BigInteger k) throws FFaplAlgebraicException{
		Polynomial s, G;
		Thread thread = g.getThread();
		//s=1
		s = g.clone();
		s.setPolynomial((new Polynomial(ONE, ZERO, thread)).polynomial());
		
		//If k = 0 then return (s)
		if(k.equals(ZERO)){
			return s;
		}
		//G <- g
		G = (Polynomial) g.clone();
		
		//if k0 = 1 then s <- g
		//if(k.testBit(k.bitLength() - 1)){
	    if(k.testBit(0)){
			s = (Polynomial) g.clone();
		}
		
		for (int i = 1; i < k.bitLength() ; i++){
			//G <- G^2
			isRunning(thread);//to interrupt calculation
			G.multiply(G);
			//if ki = 1
			if(k.testBit(i)){
				//s <- G*s
				s.multiply(G);
			}
		}		
		return s;
	}
	
	
	
	
	
	/**
	 * Legendre Symbol for BigInteger
	 * @param a
	 * @param p
	 * @return
	 * 
	 */
	public static BigInteger legendreSymbol(BigInteger a, BigInteger p)
	{
		a = a.modPow((p.subtract(ONE)).divide(new BigInteger("2")), p);
		
		if (a.mod(p).equals(ONE)) return ONE;
		if (a.mod(p).equals(p.subtract(ONE))) return ONE.negate();
		return ZERO;
	}
	

	
	
	/**
	 * Legendre Symbol for polynomial rings and galois fields
	 * @param f
	 * @param g
	 * @return
	 */
	public static BigInteger legendreSymbol(PolynomialRC f, PolynomialRC g) throws FFaplAlgebraicException
	{
		BigInteger alpha;
		BigInteger result;
		Prime p;
		Thread thrd = f.getThread();

		f = (PolynomialRC) f.clone();
		g = (PolynomialRC) g.clone();
		
		f.divide(f.leadingCoefficient());
		g.divide(g.leadingCoefficient());
		
		if (Algorithm.gcd(f, g).degree().intValue() > 0) //int value range problem
		{
			return ZERO;
		}
		
		p = new Prime(g.characteristic(), thrd);
		f.mod(g);
		alpha = f.leadingCoefficient();
		if (f.degree().intValue() == 0) //int value range problem
		{
			return legendreSymbol(alpha, p).pow(g.degree().intValue()); //int value range problem
		}
		else
		{
			f.divide(alpha);
			result = legendreSymbol(alpha, p).pow(g.degree().intValue()).multiply(legendreSymbol(g, f));//int value range problem
			if ((p.subtract(ONE).divide(new BigInteger("2")).mod(new BigInteger("2")).equals(ONE)) && f.degree().mod(new BigInteger("2")).equals(ONE) && g.degree().mod(new BigInteger("2")).equals(ONE))
			{
				result = result.negate();
			}
			
		}
		
		return result;
	}
	
	
	
	/**
	 * square root for gf elements
	 * @throws FFaplAlgebraicException 
	 */
	public static GaloisField sqrt(GaloisField a, FFaplLogger logger) throws FFaplAlgebraicException
	{
		a = a.clone(); //don't change the original value
		GaloisField g,b,h, aux3, aux4, aux5;
		g = a.clone();
		b = a.clone();
		h = a.clone();
		Thread thrd = a.getThread();

		BigInteger t,q,s,e,p;
		PolynomialRC aux1, aux2;
		aux1 = new PolynomialRC(a.characteristic(), thrd);
		aux2 = new PolynomialRC(a.characteristic(), thrd);
		
		b.setValue(new Polynomial(ONE, ZERO,a.getThread()));
		p = a.characteristic();
		if (p.equals(new BigInteger("2")))
		{
			a.pow(new BigInteger("2").pow(a.irrPolynomial().degree().subtract(ONE).intValue()));
			return a;
		}
		else
		{
			q = p.pow(a.irrPolynomial().degree().intValue()); //int value range problem
			aux1 = Algorithm.getRandomPolynomial(new BInteger(a.irrPolynomial().degree().subtract(ONE), thrd), new BInteger(p, thrd), true);

			g.setValue(aux1);
			t = q.subtract(ONE);
			s = ZERO;
			while (t.mod(new BigInteger("2")).equals(ZERO))
			{
				s = s.add(ONE);
				t = t.divide(new BigInteger("2"));
			}
			
			e = ZERO;
			for (int i = 2; i <= s.intValue(); i++) //int value range problem
			{
				aux3 = a.clone();
				aux4 = g.clone();
				aux4.pow(e.negate());
				
				aux3.multiply(aux4);
				aux3.pow(q.subtract(ONE).divide(new BigInteger("2").pow(i)));
				
				aux4.setValue(new Polynomial(1,0,thrd));
				
				if (!aux3.equals(aux4))
				{
					e = e.add(new BigInteger("2").pow(i-1));
				}
			}
			
			aux3 = a.clone();
			aux4 = g.clone();
			aux4.pow(e.negate());
			aux3.multiply(aux4); //h
			aux3.pow(t.add(ONE).divide(new BigInteger("2"))); //h^((t+1)/2)
			
			aux4 = g.clone();
			aux4.pow(e.divide(new BigInteger("2"))); // g^(e/2)
			
			b = aux4.clone();
			b.multiply(aux3);
			
			aux5 = b.clone();
			aux5.multiply(aux5);
			
			// check if b is square root of a (b^2==a)
			if (aux5.compareTo(a) != 0) {
				// if not, a has no square root, therefore return 0
				b.setValue(new Polynomial(ZERO,ZERO,a.getThread()));
				
				if (logger != null) {
					logger.log(ILevel.WARNING, "Warning: " + a + " has no square root. Returning 0.\n");
				}
			}
		}

		return b;
	}
	
	
	
	
	
	/**
	 * square root for Z(p) elements
	 * @throws FFaplAlgebraicException 
	 * 
	 */
	
	public static BigInteger sqrtMod(BigInteger a, BigInteger modulus, boolean checkForError) throws FFaplAlgebraicException
	{
		if (a.equals(ZERO))
		{
			return ZERO;
		}
		
		BigInteger b;
		if (!modulus.isProbablePrime(20)) {
			// modulus is a composite; throw exception to advise user to do a factorization first
			Object[] arguments ={"Modulus ", modulus.toString(), " is (probably) composite; sqrt is implemented only for finite fields"};
			throw new FFaplAlgebraicException(arguments,IAlgebraicError.SQRT_COMPOSITE_MODULUS);
		}
		PolynomialRC f = new PolynomialRC(modulus,null);
		PolynomialRC r = new PolynomialRC(modulus,null);
		RNG_Placebo X = new RNG_Placebo(ZERO,modulus.subtract(ONE), null);
		
  		b = X.next();
		
		while (!legendreSymbol(b.pow(2).subtract(a.multiply(new BigInteger("4"))),modulus).equals(new BigInteger("-1")))
		{
			b = X.next();
		}
		
		TreeMap<BigInteger, BigInteger> fmap = new TreeMap<BigInteger,BigInteger>();
		fmap.put(new BigInteger("2"), ONE);
		fmap.put(ONE, b.negate());
		fmap.put(ZERO, a);
		
		f.setPolynomial(fmap);

		TreeMap<BigInteger,BigInteger> rmap = new TreeMap<BigInteger,BigInteger>();
		rmap.put(ONE, ONE);
		
		r.setPolynomial(rmap);
		r.pow(modulus.add(ONE).divide(new BigInteger("2")));
		r.mod(f);
		
		
		BigInteger result = r.coefficientAt(ZERO);
		
		if (checkForError && !result.modPow(new BigInteger("2"), modulus).equals(a))
		{
			Object[] arguments = {"Error in PolynomialRC monic function"};
			throw new FFaplAlgebraicException(arguments,IAlgebraicError.SQUARE_ROOT_DOES_NOT_EXIST);
		}
		
		return result;
	}
	
	
	
	/**
	 * TraceOfanElement
	 */
	public static int traceOfGaloisFieldElement(GaloisField gf)
	{
		TreeMap<BigInteger, BigInteger> ply = gf.value().polynomial();
		int oneCount = 0;
		
		for (Map.Entry<BigInteger, BigInteger> entry : ply.entrySet())
		{
			if (entry.getValue().equals(ONE)) oneCount++;
		}
		
		return oneCount%2;
	}
	
	
	
	/**
	 * Repeated square-and-multiply algorithm for exponentiation 
	 * calculates g^k
	 * @param g
	 * @param k
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public static EllipticCurve squareAndMultiply(EllipticCurve g, BigInteger k) throws FFaplAlgebraicException{
		EllipticCurve s, G;
		Thread thread = g.getThread();
		//s=1
		s = g.clone();
		s.setPAI(true); //s = 1
		
		//If k = 0 then return (s)
		if(k.equals(ZERO)){
			return s;
		}
		//G <- g
		G = (EllipticCurve) g.clone();
		
		//if k0 = 1 then s <- g
		//if(k.testBit(k.bitLength() - 1)){
	    if(k.testBit(0)){
			s = (EllipticCurve) g.clone();
		}
		
		for (int i = 1; i < k.bitLength() ; i++){
			//G <- G*2
			isRunning(thread);//to interrupt calculation
			G.add(G);
			//if ki = 1
			if(k.testBit(i)){
				//s <- G*s
				s.add(G);
			}
		}		
		return s;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Repeated square-and-multiply algorithm for exponentiation 
	 * calculates g^k
	 * @param g 
	 * @param k
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public static BigInteger squareAndMultiply(BigInteger g, 
			 BigInteger k, Thread thread) throws FFaplAlgebraicException{
		BigInteger s, G;
		//Thread thread = g.getThread();
		//s=1
		s = new BInteger(ONE, thread);
		
		//If k = 0 then return (s)
		if(k.equals(ZERO)){
			return s;
		}
		//G <- g
		G = g;
		
		//if k0 = 1 then s <- g
		//if(k.testBit(k.bitLength() - 1)){
	    if(k.testBit(0)){
			s = g;
		}
		
		for (int i = 1; i < k.bitLength() ; i++){
			//G <- G^2
			isRunning(thread);//to interrupt calculation
			G = G.multiply(G);
			//if ki = 1
			if(k.testBit(i)){
				//s <- G*s
				s = s.multiply(G);
			}
		}		
		return s;
	}

	/**
	 * Testing a polynomial for irreducibility
	 * see: Handbook of applied Cryptography Algorithm: 4.69
	 * <p>
	 * Uses the {@link Algorithm#irreduciblePolyCache} to avoid unnecessary computations
	 * by saving results and looking up before calculating.
	 *
	 * @param f the polynomial to test
	 * @return true iff f is irreducible
	 * @throws FFaplAlgebraicException
	 */
	public static boolean isIrreducible(PolynomialRC f) throws FFaplAlgebraicException{
		PolynomialRC u, d, x, g;
		BigInteger m;
		Prime p;
		Thread thread = f.getThread();

		// to make caching easier, use return-at-end pattern
		Boolean result;

		// look up poly in cache
		if (irreduciblePolyCache != null && (result = irreduciblePolyCache.get(f)) != null)
			return result;
		if (f.characteristic() instanceof Prime) {
			p = (Prime) f.characteristic();
		} else {
			p = new Prime(f.characteristic(), thread);
		}

		f = f.getMonic();
		m = f.degree();
		//degree must be >= 1
		if(m.equals(ZERO))
			return false;

		if (m.compareTo(valueOf(100)) > 0 && (Thread.currentThread() instanceof FFaplInterpreter))
			((FFaplInterpreter) (Thread.currentThread())).getLogger().displaySlowOperationWarning();


		x = new PolynomialRC(ONE, ONE, p, thread);
		//System.out.println("f = " + f);
		//System.out.println("x = " + x);
		u = (PolynomialRC) x.clone();
		result = true;
		for(long i = 1; i <= m.divide(new BigInteger("2")).longValue(); i++){
			//System.out.println("u = " + u);
			u = squareAndMultiply(u, p, f);
			//System.out.println("u = " + u);
			g = (PolynomialRC) u.clone();
			//System.out.println("g = " + g);
			g.subtract(x);
			//System.out.println("g - x = " + g);
			d = gcd(f, g);
			//System.out.println("d = " + d);
			//System.out.println("--");
			if(! (!d.isZero() && d.degree().equals(ZERO))){
				//Grad von Polynom is 0 d.h. einheit im Körper
				result = false;
				break;
			}
		}

		// put result into cache, initialize cache if necessary
		if (irreduciblePolyCache == null)
			irreduciblePolyCache = new HashMap<>();
		irreduciblePolyCache.put(f,result);
		return result;
	}
	
	/**
	 * Testing whether an irreducible polynomial is primitive
	 * see: Handbook of applied Cryptography Algorithm: 4.77
	 * @param f
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public static boolean isPrimitive(PolynomialRC f, Vector<BigInteger> primeFactors) throws FFaplAlgebraicException{
		PolynomialRC x;
		BigInteger m, p, e;
		GaloisField l;
		Thread thread = f.getThread();
		//TODO check irreducible
		if(f.degree().compareTo(ONE) < 0 || !f.isMonic() || !isIrreducible(f)){
			//degree must be >= 1
			return false;
		}
		
		m = new BInteger(f.degree(), thread);
		p = new BInteger(f.characteristic(), thread);
		
		
		//tmp1 = multiplyElements(primeFactors, thread);
		//tmp2 = squareAndMultiply(p, m, thread).subtract(BigInteger.ONE);
		//if(tmp1.compareTo(tmp2) != 0){
		//	Object[] arguments = {primeFactors, p + "^" + m + " - 1"};
		//	throw new FFaplAlgebraicException(arguments, IAlgebraicError.PRIMITIVE);
		//}
		//System.out.println(p + "," + m);
		if(p.compareTo(valueOf(2)) == 0 && m.compareTo(ONE) == 0){
			//spezialfall GF(2) und x+1 oder x
			return true;
		}
		
		l = new GaloisField(p, f.getPolynomial(), thread);
		
		for(Iterator<BigInteger> itr = primeFactors.iterator(); itr.hasNext();){
			isRunning(thread);
			e = p.pow(m.intValue()).subtract(ONE).divide(itr.next());
			x = new PolynomialRC(ONE, e, p, thread);
			//System.out.println("x = " + x);
			//x.pow(e);
			l.setValue(x.getPolynomial());
			if(l.value().isOne()){
				return false;
			}
		}
		//System.out.println("f = " + f);
		
		
		//
		return true;
	}
	
	/**
	 * Generates an irreducible polynomial of degree n in Z(p)[x]
	 * @param p
	 * @param n
	 * @return an irreducible polynomial of degree n in Z(p)[x]
	 * @throws FFaplAlgebraicException
	 */
	public static PolynomialRC getIrreduciblePolynomial(BInteger n, BInteger p) throws FFaplAlgebraicException{
		PolynomialRC plyInit;
		Thread thread = p.getThread();
		if (n.compareTo(valueOf(Integer.MAX_VALUE)) <= 0){
			plyInit = new PolynomialRC(ONE, n, p, thread);
			return irreduciblePolynomial(plyInit, ZERO, new Prime(p, thread), n);
		}else{
			Object[] messages = {n};
			throw new FFaplAlgebraicException(messages,
					IAlgebraicError.TO_HIGH_EXPONENT);
		}
	}

	/**
	 * Finds a primitive polynomial (which generates a field in which x is primitive)
	 * for a given Finite Field GF(p^n) where p is prime
	 * <p>
	 * From: COMPUTING PRIMITIVE POLYNOMIALS - THEORY AND ALGORITHM
	 * By: Sean E. O'Connor
	 * URL: http://www.seanerikoconnor.freeservers.com/Mathematics/AbstractAlgebra/PrimitivePolynomials/theory.html#AlgoforFinding
	 *
	 * @param p characteristic
	 * @param n degree
	 * @return primitive polynomial
	 */
	public static PolynomialRCPrime getPrimitivePolynomial(BigInteger p, BigInteger n, TreeMap<BigInteger, BigInteger> factorsOfR, TreeMap<BigInteger, BigInteger> factorsOfPMinusOne, Thread _thread)
			throws FFaplAlgebraicException {
		if (p == null || n == null || n.compareTo(TWO) < 0)
			throw new FFaplAlgebraicException(new Object[0], IAlgebraicError.VALUE_IS_NULL);

		// [Step 0]

		// the constructor of Prime (called in the constructor of PolyRCPrime)
		// will throw an exception if p is not prime
		PolynomialRCPrime f = new PolynomialRCPrime(ONE, n, p, _thread); // start with just x^n

		// r := ( p^n - 1 ) / ( p - 1 )
		BigInteger r = p.pow(n.intValue()).subtract(ONE).divide(p.subtract(ONE));

		// factorize r, if necessary
		if (factorsOfR == null)
			factorsOfR = Algorithm.FactorInteger(new BInteger(r,_thread));

		// factorize p-1
		if (factorsOfPMinusOne == null)
			factorsOfPMinusOne = Algorithm.FactorInteger(new BInteger(p.subtract(ONE),_thread));

		// [Step 1]
		// iterate over possible polynomials (monic, of order n)
		boolean allPossiblePolynomialsIterated = false;

		while (!allPossiblePolynomialsIterated) {

			// [Step 2 - 7]
			if (f.isPrimitivePolynomial(factorsOfR, factorsOfPMinusOne, _thread)) {
				// [Step 8]
				return f;
			}

			// [Step 1] find next polynomial by counting through coefficients
			boolean nextPolynomialFound = false;
			BigInteger current = ZERO;
			// iterate until done or no more coefficients left
			while (!nextPolynomialFound && current.compareTo(n) < 0) {
				// add one to current, check if it is modulo reducible
				// if it is, reduce, then go to next coefficient
				BigInteger currentItem = f.coefficientAt(current).add(ONE);
				if (currentItem.compareTo(p) == 0) {
					if (current.compareTo(ZERO) == 0)
						// polynomials with constant term a_0 = 0 are reducible (divisible by x)
						// therefore not primitive and will be skipped
						currentItem = ONE;
					else
						currentItem = ZERO;
				} else {
					nextPolynomialFound = true;
				}

				f.polynomial().put(current, currentItem);
				current = current.add(ONE);
			}

			if (!nextPolynomialFound) {
				allPossiblePolynomialsIterated = true;
			}
		}

		return null;
	}

	/**
	 * Generates a random polynomial modulo p of degree up to n.
	 * Setting the {@code forceLeadingCoefficient} parameter to true will
	 * guarantee the returned polynomial to have the exact degree n.
	 *
	 * @param n                       maximal degree
	 * @param p                       module
	 * @param forceLeadingCoefficient if set to true, the returned polynomial is guaranteed
	 *                                to have a non-zero leading coefficient for x^n
	 * @return a random polynomial modulo p of degree up to n
	 * @throws FFaplAlgebraicException
	 */
	public static PolynomialRC getRandomPolynomial(BInteger n, BInteger p, boolean forceLeadingCoefficient) throws FFaplAlgebraicException {
		Thread thread = p.getThread();
		PolynomialRC ply = new PolynomialRC(p, thread);

		// generate coefficients less than p
		BigInteger maxVal = p.subtract(ONE);
		// for normal coefficients (can be zero)
		RNG_Placebo rnd = new RNG_Placebo(ZERO, maxVal, thread);

		// force a non-zero leading coefficient if requested
		if (forceLeadingCoefficient)
			ply.add(new RNG_Placebo(ONE, maxVal, thread).next(), n);
		else
			ply.add(rnd.next(), n);

		// assign coefficients randomly
		for (BigInteger i = ZERO; i.compareTo(n) < 0; i = i.add(ONE)) {
			BigInteger c = rnd.next();
			if (!c.equals(ZERO))
				ply.add(c, i);
		}

		return ply;
	}

	/**
	 * Tests if a given integer is probably prime.
	 * Correct up to a false-positive rate
	 * of no more than 2<sup>-c</sup>, where c is the certainty parameter.
	 * <p>
	 * Thus, if a call to this method returns false,
	 * the integer is guaranteed to be composite,
	 * but if true is returned, it may still be composite
	 * with a chance of no more than 2<sup>-c</sup>.
	 * <p>
	 * Note: this method uses the {@link BigInteger#isProbablePrime(int)} method
	 * but additionally implements caching via the {@link Algorithm#factorizationCache}.
	 *
	 * @param value     the value to check
	 * @param certainty the certainty c to which to test.
	 * @return true if the value is probably prime
	 */
	public static boolean isProbablePrime(BigInteger value, int certainty) {
		if (value == null)
			return false;
		TreeMap<BigInteger, BigInteger> factors;
		Boolean result;

		if (factorizationCache == null) {
			// case 1: no factorization cache: create new, then add result to cache
			factorizationCache = new TreeMap<>();
			result = value.isProbablePrime(certainty);

		} else if ((factors = factorizationCache.get(value)) != null) {
			// case 2: factorization cache exists and factorization != null: check factorization
			return factors.size() == 1 && factors.get(value).equals(ONE);

		} else if (factorizationCache.containsKey(value)) {
			// case 3: factorization is null: check if there is a mapping
			return false;

		} else {
			// case 4: no mapping: check primality, then add result to cache
			result = value.isProbablePrime(certainty);
		}

		// add result to cache
		if (result) {
			// value is prime: add known factorization to cache
			factors = new TreeMap<>();
			factors.put(value, ONE);
			factorizationCache.put(value, factors);

		} else {
			// value is not prime: factorization is unknown. thus, add null to cache as marker.
			factorizationCache.put(value, null);
		}

		return result;
	}

	/**
	 * Search for prime factors with Pollard's rho, Pollard's p-1 and linear search.
	 * @param n
	 * @return prime factors of n
	 * @throws FFaplAlgebraicException
	 */
	public static TreeMap<BigInteger, BigInteger> FactorInteger(BInteger n) throws FFaplAlgebraicException{
		BInteger fact1, fact2, B, Bdefault, nB, val;
		BigInteger min, max;
		Thread thread = n.getThread();
		//boolean fact1Prime = false;
		Stack<BInteger> factors = new Stack<BInteger>();
		TreeMap<BigInteger, BigInteger> result = new TreeMap<BigInteger, BigInteger>();
		TreeMap<BigInteger, BigInteger> primeFact;

		if (n.compareTo(ONE) <= 0) {
			if (n.compareTo(ZERO) >= 0) {
				// for one and zero the factorization consists only of the number itself
				addPrimeFactor(result, n);
				return result;

			} else {
				// for negative numbers, factorize the absolute value and add negative one
				result = FactorInteger(n.negateR());
				addPrimeFactor(result, new BInteger(valueOf(-1), thread));
			}
		} else {

			TreeMap<BigInteger, BigInteger> tmp;
			if (factorizationCache != null && ((tmp = factorizationCache.get(n)) != null)) {
				return (TreeMap<BigInteger, BigInteger>) tmp.clone();

			} else if (isProbablePrime(n, 100)) {
				addPrimeFactor(result, n);
				return result;

			} else {

				if (n.bitLength() > 35 && (Thread.currentThread() instanceof FFaplInterpreter))
					((FFaplInterpreter) (Thread.currentThread())).getLogger().displaySlowOperationWarning();

				Bdefault = BInteger.valueOf(100000, thread);
				//prework find small prime factors
				min = valueOf(2);
				max = valueOf(997);//try first 168 primes

				fact2 = n;
				while (min.compareTo(max) <= 0) {
					isRunning(thread);
					primeFact = Algorithm.primeFactorInteger(fact2, min, max);

					if (primeFact != null) {
						//prime factor <= max found
						fact1 = primeFactorValue(primeFact, thread);
						combinePrimeFactor(result, primeFact);
						min = primeFactor(primeFact, thread);
						fact2 = (BInteger) fact2.divide(fact1);
						//System.out.println(fact2);
						if (isProbablePrime(fact2, 100)) {
							addPrimeFactor(result, fact2);
							break;//finished
						} else if (fact2.compareTo(ONE) == 0) {
							break;//finished
						}
					} else {
						//prime factor higher than max
						factors.push(fact2);
						break;
					}
				}

				while (factors.size() > 0) {
					isRunning(thread);
					val = factors.pop();
					//PollardRho
					fact1 = Algorithm.PollardRho(val, thread);

					if (fact1 != null &&
							(val.compareTo(fact1) != 0 || isProbablePrime(fact1, 100))) {
						//factor found
						//System.out.println("Pollard Rho " +  fact1);
						fact2 = (BInteger) val.divide(fact1);
						if (!(val.compareTo(fact1) != 0)) {
							addPrimeFactor(result, fact1);
						} else if (fact1.compareTo(ONE) > 0) {
							factors.push(fact1);
						}
						if (isProbablePrime(fact2, 100)) {
							addPrimeFactor(result, fact2);
						} else if (fact2.compareTo(ONE) > 0) {
							factors.push(fact2);
						}
					} else {
						//PollardP-1
						nB = (BInteger) val.divide(valueOf(2));
						B = min(Bdefault, nB);
						fact1 = Algorithm.PollardPMinusOne(val, B);

						if (fact1 != null &&
								(val.compareTo(fact1) != 0 || isProbablePrime(fact1, 100))) {
							//factor found
							//System.out.println("Pollard p-1: " + fact1);
							fact2 = (BInteger) val.divide(fact1);
							if (!(val.compareTo(fact1) != 0)) {
								addPrimeFactor(result, fact1);
							} else if (fact1.compareTo(ONE) > 0) {
								factors.push(fact1);
							}
							if (isProbablePrime(fact2, 100)) {
								addPrimeFactor(result, fact2);
							} else if (fact2.compareTo(ONE) > 0) {
								factors.push(fact2);
							}
						} else {
							//Iteration
							//return prime
							primeFact = Algorithm.primeFactorInteger(val, min);

							fact1 = primeFactorValue(primeFact, thread);
							//adds result to table
							combinePrimeFactor(result, primeFact);
							if (fact1 != null) {
								//System.out.println("Iteration " +  fact1);
								//factor found
								fact2 = (BInteger) val.divide(fact1);

								if (isProbablePrime(fact2, 100)) {
									addPrimeFactor(result, fact2);
								} else if (fact2.compareTo(ONE) > 0) {
									factors.push(fact2);
								}
							} else {
								System.out.println("error");
							}
						}
					}
				}
			}
		}

		if (factorizationCache == null)
			factorizationCache = new TreeMap<>();
		factorizationCache.put(n, result);

		return result;
	}
	
	/**
	 * search by iteration over all possible primes (slow)
	 * @param n
	 * @param thread
	 * @return all prime factors from n
	 * @throws FFaplAlgebraicException
	 */
	public static TreeMap<BigInteger, BigInteger> FactorIntegerBruteForce(BigInteger n, Thread thread) throws FFaplAlgebraicException{
		BInteger p, d;
		TreeMap<BigInteger, BigInteger> result = new TreeMap<BigInteger, BigInteger>();
		
		p = new BInteger(valueOf(2), thread);
		if(isProbablePrime(n, 100)){
			result.put(n, new BInteger(ONE, thread));
		}else{
			while(n.compareTo(ONE) > 0 && p.compareTo(n) <= 0){
				isRunning(thread);
				d = gcd(p, (BInteger)n);
				if(d.compareTo(ONE) == 0){
					p = (BInteger) p.nextProbablePrime();
				}else{
					if(result.containsKey(p)){
						result.put(p, result.get(p).add(ONE));
					}else{
						result.put(p, new BInteger(ONE, thread));
					}
					n = n.divide(p);
				}
				
			}	
		}
		return result;
	}
	
	/**
	 * According Handbook of Applied Cryptography 
	 * 3.110 Algorithm Square-free factorization
	 * @param f
	 * @return square free factorization of f
	 * @throws FFaplAlgebraicException
	 */
	public static TreeMap<GaloisField, BigInteger> SquareFree(GaloisField f) throws FFaplAlgebraicException{
		Thread thread = f.getThread();
		BigInteger p = f.characteristic();
		BInteger i;
		//System.out.println(f.value().leadingCoefficient());
		GaloisField fderivate, g, h, hcap, l, tmp, tmp1;
		TreeMap<GaloisField, BigInteger> factor = new TreeMap<GaloisField, BigInteger>();
		TreeMap<GaloisField, BigInteger> factorTmp;
		Vector<GaloisField> toDelete = new Vector<GaloisField>();
		
		//control if Monic
		if(!f.value().isMonic()){
			tmp = f.clone();
			tmp.setValue(new Polynomial(f.value().leadingCoefficient(), ZERO, thread));
			
			factor.put(tmp, ONE);
			f.setValue(f.value().getMonic());
		}
		
		
		
		i = new BInteger(ONE, thread);
		//F = f.clone();
		fderivate = f.clone();
		//F.setValue(new Polynomial(BigInteger.ONE, BigInteger.ZERO, thread));
		fderivate.setValue(f.value().getDerivation()); //f'
		isRunning(thread);
		//System.out.println("f:" + f + " f':" + fderivate);
		if(fderivate.value().isZero()){
			f = PRoot(f);
			//System.out.println("fproot:" + f);
			//System.out.println("("+ f + ")^" + p + " ");
			factor = SquareFree(f);
			PowFactor(factor, p);
		}else{
			g = f.clone();
			g.setValue(gcd(f.value(), fderivate.value()));
			h = f.clone();
			h.divide(g);
			//System.out.println("h:" + h + " g:" + g);
			while(!h.value().isOne()&& h.value().degree().compareTo(ZERO) > 0){
				isRunning(thread);
				hcap = f.clone();
				hcap.setValue(gcd(h.value(), g.value()));
				//hcap.setValue(hcap.value().getMonic());
				l = h.clone();
				//System.out.println("h " + h + " hcap:" + hcap + " g:" + g + " f:" + f);
				l.divide(hcap);
				System.out.println("("+ l + ")");
				if(!l.value().isOne()){
					tmp = l.clone();
					if(l.value().isMonic()){
						factor.put(tmp, i);
					}else{
						tmp.setValue(new Polynomial(l.value().leadingCoefficient(), ZERO, thread));
						tmp1 = tmp.clone();
						tmp1.pow(i);
						if(!tmp1.value().isOne()){
							factor.put(tmp, i);
						}						
						tmp = l.clone();
						tmp.setValue(tmp.value().getMonic());
						factor.put(tmp, i);
					}
				}
				//l.pow(i);
				//F.multiply(l);
				//System.out.println("F" + F);
				i = (BInteger) i.add(ONE);
				h = hcap;
				g.divide(hcap);				
			}
			//System.out.println("g:" + g);
			if(!g.value().isOne() && g.value().degree().compareTo(ZERO) > 0){
				g = PRoot(g);
				//System.out.println("("+ g + ")^" + p + " ");
				factorTmp = SquareFree(g);
				System.out.println("-" + factorTmp );
				PowFactor(factorTmp, p);
				factor.putAll(factorTmp);
				
			}
		}
		
		for(Iterator<GaloisField> itr = factor.keySet().iterator(); itr.hasNext(); ){
			tmp = itr.next();
			tmp1 = tmp.clone();
			if(tmp1.value().degree().compareTo(ZERO) == 0){
				tmp1.pow(factor.get(tmp));
				if(tmp1.value().isOne()){
					toDelete.add(tmp);
				}
			}
		}
		for(Iterator<GaloisField> itr = toDelete.iterator(); itr.hasNext(); ){
			factor.remove(itr.next());
		}		
		return factor;
	}
	
	/**
	 * @param f
	 * @return factorization of f
	 * @throws FFaplAlgebraicException
	 */
	public static TreeMap<GaloisField, BigInteger> Factor(GaloisField f) throws FFaplAlgebraicException{
		TreeMap<GaloisField, BigInteger> factor, squareFreeFactor;
		Vector<GaloisField> irreducibleFactor;
		Vector<GaloisField> units = new Vector<GaloisField>();
		GaloisField key, key2, unit, tmp;
		BigInteger e;
		squareFreeFactor = SquareFree(f);
		factor = new TreeMap<GaloisField, BigInteger>();
		//System.out.println(squareFreeFactor);
		for(Iterator<GaloisField> itr = squareFreeFactor.keySet().iterator(); itr.hasNext(); ){
			key = itr.next();
			e = squareFreeFactor.get(key);
			irreducibleFactor = DDFactorization(key);
			for(Iterator<GaloisField> itrIrr = irreducibleFactor.iterator(); itrIrr.hasNext(); ){
				key2 = itrIrr.next();
				if(factor.containsKey(key2)){
					factor.put(key2, e.add(factor.get(key2)));
				}else{
					factor.put(key2, e);
				}
				
			}
		}
		unit = f.clone();
		unit.setValue(new Polynomial(ONE, ZERO, f.getThread()));
		for(Iterator<GaloisField> itr = factor.keySet().iterator(); itr.hasNext(); ){
			key = itr.next();
			//System.out.println(key);
			if(key.value().degree().equals(ZERO)){
				e = factor.get(key);
				tmp = key.clone();
				tmp.pow(e.intValue());
				unit.multiply(tmp);
				units.add(key);
			}
		}
		for(Iterator<GaloisField> itr = units.iterator(); itr.hasNext(); ){
			factor.remove(itr.next());
		}
		if(!unit.value().isOne()){
			factor.put(unit, ONE);
		}
		//System.out.println(factor);
		return factor;
	}
	
	/**
	 * Return a hashed Version of the given Input String
	 * @param inString
	 * @return theBInteger of the hash
	 */
	public static BInteger hashSHA256(JString inString) {
		MessageDigest _digest = null;
		BInteger hash;
		
		
		byte[] digestResult = null;
			try{
				_digest = MessageDigest.getInstance("SHA-256");
				_digest.update(inString.toString().getBytes(UTF_8));
				
			} catch( NoSuchAlgorithmException e){
				e.printStackTrace();
			} finally{
				digestResult = _digest.digest();
			}
		// hash = new BInteger(new BigInteger(digestResult).abs(), null);
		hash = new BInteger(new BigInteger(1, digestResult), new Thread());
		return hash;
		
	}
	
	/**
	 * Distinct degree factorization of f according 
	 * Algorithms for Computer Algebra - Algorithm 8.8
	 * @param f
	 * @return distinct degree factorization of f
	 * @throws FFaplAlgebraicException
	 */
	private static Vector<GaloisField> DDFactorization(GaloisField f) throws FFaplAlgebraicException{
		//f must be monic and square free
		Thread thread = f.getThread();
		Vector<GaloisField> result = new Vector<GaloisField>();
		GaloisField w, a0, ai, wx, tmp;
		BigInteger p;
		BigInteger degree;
		BigInteger i = new BInteger(ONE, thread);
		//q = f.characteristic().pow(f.irrPolynomial().degree().intValue());
		p = f.characteristic();
		w = f.clone();
		a0 = f.clone();
		w.setValue(new Polynomial(ONE, ONE, thread)); //x
		a0.setValue(new Polynomial(ONE, ZERO, thread)); //1
		degree = f.value().degree();
		
		while(i.compareTo(degree) <= 0){
			//w.setValue(new Polynomial(BigInteger.ONE, BigInteger.ONE, thread));
			w.pow(p);
			w.mod(f);
			//System.out.println("fa:" + f + " w:" + w);
			wx = w.clone();
			wx.subtract(new Polynomial(ONE, ONE, thread));
			ai = f.clone();
			ai.setValue(gcd(f.value(), wx.value()));
			
			/*if(!ai.value().isMonic() && ai.value().degree().compareTo(BigInteger.ZERO) > 0){
					//non Monic Polynomial
					tmp = ai.clone();
					tmp.setValue(new Polynomial(ai.value().leadingCoefficient(), BigInteger.ZERO, thread));
					result.add(tmp);
			}*/
			ai.setValue(ai.value().getMonic());
			
			//System.out.println("ai:" + ai);
			if(!ai.value().isOne()){// && ai.value().degree().compareTo(BigInteger.ZERO) > 0){
				result.add(ai);
				f.divide(ai);
				w.mod(f);
				
			}
			i = i.add(ONE);
		}
		
		if(f.value().isMonic()){
			//Monic Polynomial
			result.add(f);
		}else{
			//non Monic Polynomial
			//unit
			tmp = f.clone();
			tmp.setValue(new Polynomial(f.value().leadingCoefficient(), ZERO, thread));
			//tmp = GaloisField.inverse(tmp);
			//System.out.print(tmp + " -> ");
			result.add(tmp);
			//monic polynomial
			tmp = f.clone();
			tmp.setValue(tmp.value().getMonic());
			result.add(tmp);
			//System.out.println(tmp);
		}
		//System.out.print(result);		
		return result;
	}
	
	
	/**
	 * f = a^p, returns a in GF(p^m)
	 * @param f
	 * @return pth-root of f
	 * @throws FFaplAlgebraicException
	 */
	private static GaloisField PRoot(GaloisField f) throws FFaplAlgebraicException{
		
		PolynomialRC ply = f.value();
		GaloisField proot = f.clone();
		BigInteger p = ply.characteristic();
		PolynomialRC plyRes = new PolynomialRC(p, f.getThread());
		ResidueClass val = new ResidueClass(p);
		BigInteger m1 = f.irrPolynomial().degree().subtract(ONE);//m-1
		BigInteger pm1 = p.pow(m1.intValue());//p^m-1
		BigInteger e,c;
		TreeMap<BigInteger, BigInteger> plyView = ply.polynomial();
		
		for (Iterator<BigInteger> itr = plyView.keySet().iterator(); itr.hasNext(); ){
			e = itr.next();
			c = plyView.get(e);
			val.setValue(c);
			val.pow(pm1);
			plyRes.add(val.value(), e.divide(p));
		}
		
		proot.setValue(plyRes);
		return proot;
	}
	
	
	
	
	
	private static void PowFactor(TreeMap<GaloisField, BigInteger> table, BigInteger e){
		GaloisField key;
		for(Iterator<GaloisField> itr = table.keySet().iterator(); itr.hasNext();){
			key = itr.next();
			table.put(key, table.get(key).multiply(e));
		}
	}
	
	/**
	 * Adds a prime factor to the table, increase exponent if needed
	 * @param table
	 * @param p
	 */
	private static void addPrimeFactor(TreeMap<BigInteger, BigInteger> table, BInteger p){
		if(table.containsKey(p)){
			table.put(p, table.get(p).add(ONE));
		}else{
			table.put(p, new BInteger(ONE, p.getThread()));
		}
	}
	
	/**
	 * Combines to prime factor hash tables
	 * @param table
	 * @param toAdd
	 */
	private static void combinePrimeFactor(TreeMap<BigInteger, BigInteger> table, TreeMap<BigInteger, BigInteger> toAdd){
		BigInteger key;
		for(Iterator<BigInteger>  itr = toAdd.keySet().iterator(); itr.hasNext(); ){
			key = itr.next();
			if(table.containsKey(key)){
				table.put(key, table.get(key).add(toAdd.get(key)));
			}else{
				table.put(key, toAdd.get(key));
			}
		}
	}
	
	/**
	 * @param table
	 * @param thread
	 * @return the product of the prime factors
	 */
	private static BInteger primeFactorValue(TreeMap<BigInteger, BigInteger> table, Thread thread){
		BigInteger key;
		BInteger result = new BInteger(ONE, thread);
		for(Iterator<BigInteger>  itr = table.keySet().iterator(); itr.hasNext(); ){
			key = itr.next();
			result = (BInteger) result.multiply(key.pow(table.get(key).intValue()));
		}
		return result;
	}
	
	/**
	 * @param table
	 * @param thread
	 * @return the product of the prime factors
	 */
	private static BInteger primeFactor(TreeMap<BigInteger, BigInteger> table, Thread thread){
		BInteger key;
		BInteger result = new BInteger(ONE, thread);
		for(Iterator<BigInteger>  itr = table.keySet().iterator(); itr.hasNext(); ){
			key = new BInteger(itr.next(), thread);
			return key;
		}
		return result;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return minimum of a and b
	 */
	private static BInteger min(BInteger a, BInteger b){
		return new BInteger(a.add(b).subtract(a.subtract(b).abs()).shiftRight(1), a.getThread());
	}
	
	/**
	 * Pollard's 
	 * see: Handbook of applied Cryptography Algorithm: 3.14
	 * @param n
	 * @param B
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	private static BInteger PollardPMinusOne(BInteger n, BInteger B) throws FFaplAlgebraicException{
		BInteger d, q;
		BInteger l;
		ResidueClass a;
		Thread thread = n.getThread();
		RNG_Placebo rand;
		if(isProbablePrime(n, 100)){
			return n;
		}		
		rand = new RNG_Placebo(valueOf(2), n.subtract(ONE), n.getThread());
		a = new ResidueClass(rand.next(), n);
		d = gcd((BInteger)a.value(), n);
		q = BInteger.valueOf(2, n.getThread());
		if(d.compareTo(ONE) <= 0){
			while(q.compareTo(B) <= 0){
				isRunning(thread);
				l = (BInteger) BInteger.valueOf((long)Math.ceil((Math.log10(n.longValue())/Math.log10(q.longValue()))), n.getThread());
				a.pow(q);
				a.pow(l);
				q = q.nextProbablePrime();
			}
			d = gcd((BInteger)a.value().subtract(ONE), n);
			if(d.compareTo(ONE) == 0 ||
					d.compareTo(n) == 0){
				return null;//failure
			}
		}		
		return d;
	}
	
	/**
	 * Pollard's Rho
	 * @param n
	 * @param thread
	 * @return a factor of n or null
	 * @throws FFaplAlgebraicException
	 */
	private static BInteger PollardRho(BigInteger n, Thread thread) throws FFaplAlgebraicException{
		BigInteger d, a;
		ResidueClass x, y, q;
		int i;
		
		d = new BInteger(ONE, thread);
		
		a = BInteger.valueOf(2, thread);
		
		IRandomGenerator fx;
		fx = new RNG_Placebo(ONE, n, thread);
		x = new ResidueClass(fx.next(), n);
		y = x.clone();
		q = new ResidueClass(ONE, n);
		i = 0;
		while(i < 20000){
			isRunning(thread);
			x.multiply(x);
			x.subtract(a);
			y.multiply(y);
			y.subtract(a);
			y.multiply(y);
			y.subtract(a);
			q.multiply(x.value().subtract(y.value()));
			
			if(i % 20 == 0){
				d = gcd(new BInteger(q.value(), thread), (BInteger)n);
				if(d.compareTo(ONE) > 0){
					//System.out.println("i " + d);
					return new BInteger(d, thread);
				}
			}
			i++;
		}		
		return null;	
	}
	
	
	
	/**
	 * search by iteration over all possible primes (slow)
	 * @param n
	 * @return a prime factor of n
	 * @throws FFaplAlgebraicException
	 
	private static TreeMap<BigInteger, BigInteger> primeFactorInteger(BInteger n) throws FFaplAlgebraicException{
		BInteger p, d;
		Thread thread = n.getThread();
		TreeMap<BigInteger, BigInteger> result = new TreeMap<BigInteger, BigInteger>();
		if(isProbablePrime(n, 100)){
			result.put(n, BigInteger.ONE);
			return result;
		}
		p = new BInteger(BigInteger.valueOf(2), thread);		
		while(n.compareTo(BigInteger.ONE) > 0 && p.compareTo(n) <= 0){
				isRunning(thread);
				d = gcd(p, (BInteger)n);
				if(d.compareTo(BigInteger.ONE) == 0){
					p = (BInteger) p.nextProbablePrime();
				}else{
					while(d.compareTo(BigInteger.ONE) > 0){
						addPrimeFactor(result, p);
						n = (BInteger) n.divide(p);
						d = gcd(p, (BInteger)n);
					}
					return result;
				}
				
		}	
		return null;
	}
	*/
	
	/**
	 * search by iteration over all possible primes (slow), start with min
	 * @param n
	 * @param min minimum of prime to find
	 * @return a prime factor of n
	 * @throws FFaplAlgebraicException
	 */
	private static TreeMap<BigInteger, BigInteger> primeFactorInteger(BInteger n, BigInteger min) throws FFaplAlgebraicException{
		BInteger p, d;
		Thread thread = n.getThread();
		TreeMap<BigInteger, BigInteger> result = new TreeMap<BigInteger, BigInteger>();
		if(isProbablePrime(n, 100)){
			result.put(n, ONE);
			return result;
		}
		if(isProbablePrime(min, 100)){
			p = new BInteger(min, thread);
		}else{
			p = new BInteger(min.nextProbablePrime(), thread);
		}		
		while(n.compareTo(ONE) > 0 && p.compareTo(n) <= 0){
				isRunning(thread);
				d = gcd(p, (BInteger)n);
				if(d.compareTo(ONE) == 0){
					p = (BInteger) p.nextProbablePrime();
				}else{
					while(d.compareTo(ONE) > 0){
						addPrimeFactor(result, p);
						n = (BInteger) n.divide(p);
						d = gcd(p, (BInteger)n);
					}
					return result;
				}
				
		}	
		return null;
	}
	
	/**
	 * search by iteration over all possible primes between min and max
	 * @param n
	 * @param min minimum of prime to find
	 * @param max maximum of prime to find
	 * @return a prime factor of n
	 * @throws FFaplAlgebraicException
	 */
	private static TreeMap<BigInteger, BigInteger> primeFactorInteger(BInteger n, BigInteger min, BigInteger max) throws FFaplAlgebraicException{
		BInteger p, d;
		Thread thread = n.getThread();
		TreeMap<BigInteger, BigInteger> result = new TreeMap<BigInteger, BigInteger>();
		if(isProbablePrime(n, 100)){
			result.put(n, ONE);
			return result;
		}
		if(isProbablePrime(min, 100)){
			p = new BInteger(min, thread);
		}else{
			p = new BInteger(min.nextProbablePrime(), thread);
		}
				
		while(n.compareTo(ONE) > 0 && p.compareTo(n) <= 0 && p.compareTo(max) <= 0){
				isRunning(thread);
				d = gcd(p, (BInteger)n);
				if(d.compareTo(ONE) == 0){
					p = (BInteger) p.nextProbablePrime();
				}else{
					while(d.compareTo(ONE) > 0){
						addPrimeFactor(result, p);
						n = (BInteger) n.divide(p);
						d = gcd(p, (BInteger)n);
					}
					return result;
				}		
		}	
		return null;
	}
	
	
	
	/**
	 * determines irreducible Polynomial recursively
	 * @param ply
	 * @param index
	 * @param p
	 * @param n
	 * @return irreducible Polynomial
	 * @throws FFaplAlgebraicException
	 */
	private static PolynomialRC irreduciblePolynomial(PolynomialRC ply, BigInteger index, Prime p, BigInteger n) throws FFaplAlgebraicException{
		PolynomialRC ply2;
		int start;
		    isRunning(p.getThread());
		    
			if(index.compareTo(ZERO) == 0){
				//first index must not be zero
				start = 1;
			}else{
				start = 0;
			}
			
			    if(index.compareTo(n) == 0){
			    	if(isIrreducible(ply)){
			    		return ply;
			    	}else{
			    		return null;
			    	}
			    }
			    
				for(int i = start; p.compareTo(valueOf(i)) > 0; i++){
					ply2 = (PolynomialRC) ply.clone();
					ply2.add(valueOf(i), index);
					//System.out.println(ply2);
					ply2 = irreduciblePolynomial(ply2, index.add(ONE), p, n);
					if (ply2 != null){
						return ply2;
					}
				}	
		return null;
	}
	
	
	
	
	
	/**
	  * throws an interrupt exception if not running
	  * @param thread
	  * @throws FFaplException
	  */
	  public static void isRunning(Thread thread) throws FFaplAlgebraicException
	  {
		if(thread != null){
			if(thread.isInterrupted()){
				throw new FFaplAlgebraicException(null, IAlgebraicError.INTERRUPT);
			}
			
		}
	  }
	  
	  /*/**
	   * multiply elements of a Vector
	   * @param vals
	   * @param thread
	   * @return multiplication of Elements;
	   
	  private static BInteger multiplyElements(Vector<BigInteger> vals, Thread thread){
	
			BInteger result;
			if(vals.size() > 0){
				result = new BInteger(BigInteger.ONE, thread);
				for(Iterator<BigInteger> itr = vals.iterator(); itr.hasNext(); ){
					result = (BInteger) result.multiply(itr.next());
				}
				
			}else{
				result = new BInteger(BigInteger.ZERO, thread);
			}			
			return result;
		}
	 * @throws FFaplAlgebraicException 
*/
	
	  
	  
	  
	  public static BigInteger log2(BigInteger a)
	  {
		  BigInteger x = new BigInteger("-1");
		  BigInteger k = ONE;
		  
		  while (a.compareTo(k) >= 0)
		  {
			  k = k.multiply(new BigInteger("2"));
			  x = x.add(ONE);
		  }
		  
		  return x;
	  }
	  
	  

  public static GaloisField tatePairing(EllipticCurve P, EllipticCurve Q) throws FFaplAlgebraicException
  {
		if (!Q.isGf())
		{
			throw new FFaplAlgebraicException(null, IAlgebraicError.EC_PAIRING_PARAMETER_NOT_VALID);
		}
		
	  if (P.getPAI() || Q.getPAI())
	  {
		  GaloisField gf;
  
		  if (P.isGf())
		  {
			  gf = P.getGF();
		  }
		  else
		  {
			  gf = Q.getGF();
		  }
		  gf.setValue(new Polynomial(1,0,null));
  
		  return gf;
	  }
  
		BigInteger order = P.getOrder();
	  	return tatePairing(P, Q, order);
  }
	  
	  
  	public static GaloisField tatePairing(EllipticCurve P, EllipticCurve Q, BigInteger orderOfP) throws FFaplAlgebraicException
  	{
  		if (!Q.isGf())
  		{
  			throw new FFaplAlgebraicException(null, IAlgebraicError.EC_PAIRING_PARAMETER_NOT_VALID);
		}
  		
  		if (!P.isGf())
  		{
  			P = P.convertToGF(Q.getGF());
  			if (!P.equalEC(Q))
  	  			throw new FFaplAlgebraicException(null, IAlgebraicError.EC_PAIRING_PARAMETER_NOT_VALID);
  		}
		
		
		
  		if (P.getPAI() || Q.getPAI())
  		{
  			if (P.getGF() == null)
  			{
			  GaloisField result = Q.getGF().clone();
			  result.setValue(new Polynomial(1, 0, null));
			  return result;
  			}
	  		else if (Q.getGF() == null)
	  		{
	  			GaloisField result = P.getGF().clone();
	  			result.setValue(new Polynomial(1,0,null));
	  			return result;
	  		}
	}
		  
		  
		  
		  
		  BigInteger order = orderOfP;
		  GaloisField f1 = Q.getGF().clone();
		  GaloisField f2 = Q.getGF().clone();
		  GaloisField lambda = Q.getGF().clone();
		  GaloisField temp1 = Q.getGF().clone();
		  GaloisField temp2 = Q.getGF().clone();
		  EllipticCurve T = P.clone();

		  f1.setValue(new Polynomial(1,0,null));
 		  f2.setValue(new Polynomial(1,0,null));
		  
		  int l = (int) Math.ceil(Math.log10(order.doubleValue()+1)/Math.log10(2));
	
		  
		  for (int i = l-2; i >= 0; i--)
		  {
			  lambda = T.getSlope(T.getX_gf(), T.getY_gf());
			  if (lambda == null) //Divide by zero
			  {
				  f1.multiply(f1);
				  f2.multiply(f2);
				  
				  temp1.setValue(Q.getX_gf());
				  temp1.subtract(T.getX_gf());
				  
				  temp1.multiply(f1);
				  if(!temp1.value().isZero())
				  	f1 = temp1.clone();
				  //f2 bleibt gleich
				  
			  }
			  else
			  {
				  temp1.setValue(T.getX_gf()); //x3
				  temp1.subtract(Q.getX_gf()); //x3-x2
				  temp1.multiply(lambda); // l(x3-x2)
				  
				  temp1.add(Q.getY_gf()); //y2 + l(x3-x2)
				  
				  temp1.subtract(T.getY_gf()); //y2 + l(x3-x2) - y3
				  
				  temp1.multiply(f1);
				  temp1.multiply(f1);
				  
				  if (!temp1.value().isZero())
					  f1 = temp1.clone();
				  
				  
				  
				  
				  temp1 = lambda.clone(); //l
				  temp1.add(Q.get_a1()); //l+a1
				  temp1.multiply(lambda); //l^2 + l*a1
				  temp1 = temp1.negate(); //-l^2 - l*a1
				  temp1.add(Q.get_a2()); // -l^2 - l*a1 + a2
	
				  temp1.add(Q.getX_gf()); //x2 - l^2 - l*a1 + a2
				  temp1.add(T.getX_gf()); //x2 + x3 - l^2 - l*a1 + a2
				  temp1.add(T.getX_gf()); //x2 + x3 + x3 - l^2 - l*a1 + a2
	
				  temp1.multiply(f2);
				  temp1.multiply(f2);
				  
				  if (!temp1.value().isZero())
					  f2 = temp1.clone();
			  }


			  T.add(T);
			  if (order.testBit(i))
			  {

				  lambda.setValue(P.getY_gf());
				  lambda.subtract(T.getY_gf());
				  
				  temp1.setValue(P.getX_gf());
				  temp1.subtract(T.getX_gf());
				  
				  temp2 = temp1.clone();
				  temp2.setValue(new Polynomial(0, 0, null));
				  
				  if (temp1.equals(temp2))
				  {
					  temp1.setValue(Q.getX_gf());
					  temp1.subtract(T.getX_gf());
					  
					  temp1.multiply(f1);
					  if(!temp1.value().isZero())
					  	f1 = temp1.clone();
					  //f2 bleibt gleich
				  }
				  else
				  {
					  lambda.divide(temp1);
					  
					  
	
					  temp1.setValue(T.getX_gf()); //x3
					  temp1.subtract(Q.getX_gf()); //x3-x2
					  temp1.multiply(lambda); // l(x3-x2)
					  
					  temp1.add(Q.getY_gf()); //y2 + l(x3-x2)
					  
					  temp1.subtract(T.getY_gf()); //y2 + l(x3-x2) - y3
					  
					  temp1.multiply(f1);
					  
					  if (!temp1.value().isZero())
						  f1 = temp1.clone();
					  
					  
					  
					  
					  temp1 = lambda.clone(); //l
					  temp1.add(Q.get_a1()); //l+a1
					  temp1.multiply(lambda); //l^2 + l*a1
					  temp1 = temp1.negate(); //-l^2 - l*a1
					  temp1.add(Q.get_a2()); // -l^2 - l*a1 + a2
	
					  temp1.add(Q.getX_gf()); //x2 - l^2 - l*a1 + a2
					  temp1.add(P.getX_gf()); //x2 + x1 - l^2 - l*a1 + a2
					  temp1.add(T.getX_gf()); //x2 + x1 + x3 - l^2 - l*a1 + a2
	
					  temp1.multiply(f2);

					  if (!temp1.value().isZero())
						  f2 = temp1.clone();
				  }

				  T.add(P);
			  }

			  
		  }

		  BigInteger exp = P.getGF().characteristic();
		  exp = exp.pow(Q.getGF().irrPolynomial().degree().intValue());//q^k
		  exp = exp.subtract(ONE);
		  exp = exp.divide(order);
		  
		  f1.divide(f2);
		  f1.pow(exp);


		  

		  return f1;
	  }
	  
	  
	  
	  
	
}
