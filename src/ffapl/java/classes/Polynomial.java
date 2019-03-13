/**
 * 
 */
package ffapl.java.classes;

import ffapl.FFaplInterpreter;
import ffapl.exception.FFaplException;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IAlgebraicOperations;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.math.Algorithm;
import ffapl.types.FFaplTypeCrossTable;

import java.math.BigInteger;
import java.util.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Polynomial implements IJavaType<Polynomial>, IAlgebraicOperations<Polynomial> {

	protected TreeMap<BigInteger, BigInteger> _polynomialMap;
	/** the thread within the Galois Field is created */
	protected Thread _thread; 
	
	/**
	 * Default Constructor
	 * @param thread
	 */
	public Polynomial(Thread thread) {
		_polynomialMap = new TreeMap<BigInteger, BigInteger>();
		_thread = thread;
	}
	
	/**
	 * Constructor
	 * @param polynomTable
	 * @param thread
	 */
	@SuppressWarnings("unchecked")
	public Polynomial(TreeMap<BigInteger, BigInteger> polynomTable, Thread thread){
		_polynomialMap = (TreeMap<BigInteger, BigInteger>) polynomTable.clone();
		_thread = thread;
	}
	
	/**
	 * Constructor
	 * @param c coefficient
	 * @param e exponent
	 * @param thread
	 */
	public Polynomial(BigInteger c, BigInteger e, Thread thread) {
		_polynomialMap = new TreeMap<BigInteger, BigInteger>();
		_thread = thread;
		//initial polynom c^e
		if(! c.equals(ZERO)){
			_polynomialMap.put(e,c);
		}
		
	}
	
	/**
	 * Constructor
	 * @param c coefficient
	 * @param e exponent
	 * @param thread
	 */
	public Polynomial(long c, long e, Thread thread){
		this(BigInteger.valueOf(c), BigInteger.valueOf(e), thread);
	}
	
	public Polynomial(String s, Thread thread) throws FFaplAlgebraicException {
		this(thread);
		ArrayList<Polynomial> list = extractValuesfromString(s);
		//System.out.println(list.toString());
		for(Polynomial p : list){
			add(p);
		}
	}

	private ArrayList<Polynomial> extractValuesfromString(String s) throws FFaplAlgebraicException {
		
		ArrayList<Polynomial> list = new ArrayList<>();
		//System.out.println("Wert der übergeben wurde: " +s);
		String withoutSquareB = s.replaceAll("\\[|\\]", "");
		//System.out.println(withoutSquareB);
                //split at "+"
		String[] d = withoutSquareB.trim().split("\\+");
                //split at "-"
                Vector<String> minusSplit = new Vector<>();
                for(int i = 0; i < d.length; i++){
                        String[] temp = d[i].split("-");
                        for(int j = 0; j < temp.length; j++){
                                if(!temp[j].equals("")){
                                        if(j == 0)
                                                minusSplit.add(temp[j]);
                                        else
                                                minusSplit.add("-" + temp[j]);
                                }
                        }
                }
                d = new String[minusSplit.size()];
                minusSplit.toArray(d);
                
		for(int i = 0; i < d.length; i++){
			//System.out.println(d[i]);
			if(d[i].contains("x")){
				int index = d[i].indexOf("x");
				long c = 1, e = 1;
                                
				if(index > 0){
					if(d[i].charAt(0) == '-' && index == 1)
						c = -c;
					else
						c = Long.parseLong(d[i].substring(0, index));	
				}
                                
				if(d[i].contains("^"))
					e = Long.parseLong(d[i].substring(index+2).trim());
		
                                list.add(new Polynomial(c, e, this._thread));
			} else{
				Polynomial temp = new Polynomial(this._thread);
				if(d[i].contains("^")){
					String[] secondsplit = d[i].split("\\^");
					BigInteger base = BigInteger.valueOf(Long.parseLong(secondsplit[0]));
					BigInteger exp = BigInteger.valueOf(Long.parseLong(secondsplit[1]));
					temp.setPolynomial(Algorithm.squareAndMultiply(base, exp, this._thread), ZERO);
				} else {
					temp.setPolynomial(BigInteger.valueOf(Long.parseLong(d[i])), ZERO);
				}
				list.add(temp);
			}
		}
		return list;
	}


	/**
	 * Returns the degree of the Polynomial
	 * @return
	 */
	public BigInteger degree() {
		clearZeroCoefficients();

		if (_polynomialMap.keySet().size() > 0)
			return new BInteger(_polynomialMap.lastKey(), _thread);
		else
			return ZERO;
	}

	/**
	 * Cleans up the polynomials exponent -> coefficient map,
	 * removing any coefficients that equal to zero.
	 *
	 * @return true if any zero-valued coefficients were removed
	 */
	private boolean clearZeroCoefficients() {
		return this.polynomial().values().removeAll(Collections.singleton(ZERO));
	}

	/**
	 * Adds the specified Polynom <Code> ply </Code> to the current Polynom
	 * @param ply
	 */
	public void add(Polynomial ply){
		BigInteger e1, c1, c2;
		for(Iterator<BigInteger> itr = ply.exponents().iterator(); itr.hasNext(); ){
			e1 = itr.next();
			c1 = ply.polynomial().get(e1);
			if(this._polynomialMap.containsKey(e1)){
				c2 = this._polynomialMap.get(e1);
				c2 = c2.add(c1);
				if(c2.equals(ZERO)){
					//sum is zero
					this._polynomialMap.remove(e1);
				}else{
					this._polynomialMap.put(e1, c2);
				}
			}else{
				this._polynomialMap.put(e1, c1);
			}
		}
		postTask();
	}
	
	/**
	 * @return returns the derivation of the polynomial
	 */
	public Polynomial getDerivation(){
		Polynomial ply = this.clone();
		ply.setPolynomial(ZERO, ZERO);
		BigInteger e, c;
		for(Iterator<BigInteger> itr = _polynomialMap.keySet().iterator(); itr.hasNext();){
			e = itr.next();
			if(e.compareTo(ZERO) > 0){
				c = _polynomialMap.get(e);
				c = c.multiply(e);
				e = e.subtract(ONE);
				ply.add(c, e);
			}
		}
		return ply;
	}
	
	/**
	 * polynomial + c*x^e
	 * @param c
	 * @param e
	 */
	public void add(BigInteger c, BigInteger e){
		this.add(new Polynomial(c, e, _thread));
	}
	
	/**
	 * subtracts the specified Polynom <Code> ply </Code> from the current Polynom
	 * @param ply
	 */
	public void subtract(Polynomial ply){
		BigInteger e1, c1, c2;
		for(Iterator<BigInteger> itr = ply.exponents().iterator(); itr.hasNext(); ){
			e1 = itr.next();
			c1 = ply.polynomial().get(e1);
			if(this._polynomialMap.containsKey(e1)){
				c2 = this._polynomialMap.get(e1);
				c2 = c2.subtract(c1);
				if(c2.equals(ZERO)){
					//sum is zero
					this._polynomialMap.remove(e1);
				}else{
					this._polynomialMap.put(e1, c2);
				}
			}else{
				this._polynomialMap.put(e1, c1.negate());
			}
		}
		postTask();
	}
	
	/**
	 * polynomial - c*e^x
	 * @param c
	 * @param e
	 */
	public void subtract(BigInteger c, BigInteger e){
		this.subtract(new Polynomial(c, e, _thread));
	}
	
	/**
	 * Multiplies the specified Polynom <Code>ply</Code> to the specified
	 * @param ply
	 * @throws FFaplAlgebraicException
	 */
	public void multiply(Polynomial ply) throws FFaplAlgebraicException{
		BigInteger c1, e1;
		Polynomial p1, p2;
		p2 = new Polynomial(_thread);
		for(Iterator<BigInteger> itr1 = ply.exponents().iterator(); itr1.hasNext(); ){
			isRunning();
			e1 = itr1.next();
			c1 = ply.polynomial().get(e1);
			p1 = this.clone();
			p1.multiply(c1, e1);
			p2.add(p1);	
		}
		this.setPolynomial(p2.polynomial());
		//postTask();
	}
	
	/**
	 * Multiplies c*x^e to the specified Polynom
	 * @param c coefficient
	 * @param e exponent
	 * @throws FFaplAlgebraicException 
	 */
	public void multiply(BigInteger c, BigInteger e) throws FFaplAlgebraicException{
		TreeMap<BigInteger, BigInteger> polynomTable = new TreeMap<BigInteger, BigInteger>();
		BigInteger c1, e1;
		for(Iterator<BigInteger> itr = this. _polynomialMap.keySet().iterator(); itr.hasNext(); ){
			isRunning();
			e1 = itr.next();
			c1 = _polynomialMap.get(e1);
			e1 = e1.add(e);//add exponent
			c1 = c1.multiply(c); // multiply coefficient
			if(!c1.equals(ZERO)){
				polynomTable.put(e1, c1);
			}			
		}
		_polynomialMap = polynomTable;
		postTask();
	}
	
	
	
	/**
	 * calculates polynomial^exponent
	 * @param exponent
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public Polynomial pow(BigInteger exponent) throws FFaplAlgebraicException{
		if(exponent.compareTo(ZERO) < 0){
			String[] arguments = {exponent.toString(), 
								 this.classInfo() + " -> " + this + 
										FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + exponent };
			throw new FFaplAlgebraicException(arguments,
					IAlgebraicError.NEGATIVE_EXPONENT);
		}
		if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0){
			this.setPolynomial(Algorithm.squareAndMultiply(this, exponent).polynomial());
		}else{
			String[] arguments = {this + 
					FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + exponent};
			throw new FFaplAlgebraicException(arguments,
					IAlgebraicError.TO_HIGH_EXPONENT);
		}
		return this;
	}
	
	/**
	 * calculates polynomial^exponent
	 * @param exponent
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public Polynomial pow(long exponent) throws FFaplAlgebraicException{
		return pow(BigInteger.valueOf(exponent));
	}
	
	/**
	 * Returns the TreeMap of the Polynomial
	 * @return
	 */
	public  TreeMap<BigInteger, BigInteger> polynomial(){
		return this._polynomialMap;
	}
	
	/**
	 * Returns the sorted Exponents of the polynomial
	 * @return
	 */
	public Set<BigInteger> exponents(){
		clearZeroCoefficients();
		return this._polynomialMap.keySet();
	}
	
	/**
	 * Sets the Polynomial
	 * @param polynomTable
	 */
	public void setPolynomial(TreeMap<BigInteger, BigInteger> polynomTable){
		_polynomialMap = polynomTable;
		postTask();
	}
	
	/**
	 * Sets the Polynomial to c*x^e
	 * @param c
	 * @param e
	 */
	public void setPolynomial(BigInteger c, BigInteger e){
		_polynomialMap.clear();
		_polynomialMap.put(e, c);
		postTask();
	}
	
	/**
	 * @param val
	 * @return the result of the polynomial if x = val
	 * @throws FFaplAlgebraicException 
	 */
	public BigInteger calculate(BInteger val) throws FFaplAlgebraicException {
		BigInteger c, e, result;
		result = ZERO;
		for(Iterator<BigInteger> itr =  _polynomialMap.keySet().iterator(); itr.hasNext(); ){
			e = itr.next();
			c = _polynomialMap.get(e);
			result = result.add(Algorithm.squareAndMultiply(val, e, _thread).multiply(c));
		}
		return new BInteger(result, _thread);
	}
	
	/**
	 * Returns true if the polynomial is monic
	 * @return
	 */
	public boolean isMonic(){
		clearZeroCoefficients();
		return ONE.equals(this._polynomialMap.lastEntry().getValue());
	}
	
	/**
	 * Returns true if polynomial = 0
	 * @return
	 */
	public boolean isZero(){
		clearZeroCoefficients();
		return _polynomialMap.isEmpty();
	}
	
	/**
	 * Returns true if polynomial = 1
	 * @return
	 */
	public boolean isOne(){
		return this.isMonic() && this.isConstant();
	}

	/**
	 * Returns true if polynomial is constant, i.e. has degree 0.
	 * @return
	 */
	public boolean isConstant() {
		clearZeroCoefficients();
		return ZERO.equals(degree());
	}
	
	/**
	 * Return the leading coefficient of the polynomial
	 * @return
	 */
	public BigInteger leadingCoefficient(){
		if (_polynomialMap.isEmpty()) {
			return new BInteger(ZERO, _thread);
		} else {
			return new BInteger(this._polynomialMap.get(this.degree()), _thread);
		}
	}
	
	 /**
	   * Returns the coefficient of x^e
	   * @param e
	   * @return the coefficient of x^e in the polynomial
	   */
	public BigInteger coefficientAt(BigInteger e) {
		if(_polynomialMap.containsKey(e)){
			return new BInteger(this._polynomialMap.get(e), _thread);
		}else{
			return new BInteger(ZERO, _thread);
		}
	}
	
	@Override
	public Polynomial clone(){	
		@SuppressWarnings("unchecked")
		TreeMap<BigInteger, BigInteger> polynomTable = (TreeMap<BigInteger, BigInteger>) _polynomialMap.clone();
		return new Polynomial(polynomTable, _thread);
	}
	
	
		
	@Override
	public String toString(){
		return Polynomial.plyToString(this);
	}
	
	/**
	 * deletes all coefficients which are zero
	 * @param ply
	 * @return the minimized polynomial
	 */
	protected static Polynomial minimizePolynomial(Polynomial ply){
		@SuppressWarnings("unchecked")
		Set<BigInteger> tmp =( (TreeMap<BigInteger, BigInteger> )ply.polynomial().clone()).keySet();
		BigInteger e;
		for(Iterator<BigInteger> itr = tmp.iterator(); itr.hasNext(); ){
			e = itr.next();
			if(ply.polynomial().get(e).equals(ZERO)){
				ply.polynomial().remove(e);
			}
		}
		return ply;
	}
	
	/**
	 * Returns true if polynomials are equal
	 * @param val
	 * @return
	 * @throws FFaplAlgebraicException 
	 */
	public boolean equals(IJavaType val) throws FFaplAlgebraicException{
		Polynomial ply;
		
		if(val instanceof BigInteger){
			ply = new Polynomial((BigInteger) val, ZERO, _thread);
		}else if(val instanceof PolynomialRC){
			return ((PolynomialRC) val).equals(this);	
		}else if(val instanceof Polynomial){
			ply = (Polynomial) val;
		}else{
			String[] arguments = {"polynomial equals operation try to compare " + 
					this.classInfo() + " with " + val.classInfo()};
			throw new FFaplAlgebraicException(arguments ,IAlgebraicError.INTERNAL);
		}		
		return plyequal(ply);
	}
	
	protected boolean plyequal(Polynomial ply){
		BigInteger e;
		if( _polynomialMap.keySet().size() == ply.exponents().size()){
			for(Iterator<BigInteger> itr =  _polynomialMap.keySet().iterator(); itr.hasNext(); ){
				e = itr.next();
				if(ply.exponents().contains(e)){
					if(_polynomialMap.get(e).compareTo(ply.polynomial().get(e)) != 0){
						//different coefficient
						return false;
					}
				}else{
					//different exponent
					return false;
				}
			}
		}else{
			//different size of elements
			return false;
		}
		return true;
	}
	
	/**
	 * Execute Task after calculations and assignment
	 */
	protected void postTask(){
		//Polynomial p;
		//p = Polynomial.minimizePolynomial(this);
		//this._polynomTable = p.polynomial();
		//this._keyset = p.exponents();
	}
	
	/**
	 * Returns the negate of a polynomial
	 * @return
	 * @throws FFaplAlgebraicException 
	 */
	public Polynomial negate() throws FFaplAlgebraicException{
		Polynomial ply = this.clone();
		ply.multiply(ONE.negate(), ZERO);
		return ply;
	}
	
	/**
	 * polynomial mod modulus
	 * @param modulus
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public Polynomial mod(BigInteger modulus) throws FFaplAlgebraicException{
		Polynomial ply = this.clone();
		BigInteger c, e;
		for(Iterator<BigInteger> itr =  _polynomialMap.keySet().iterator(); itr.hasNext(); ){
			e = itr.next();
			c = ply.polynomial().get(e).mod(modulus);
			if(c.equals(ZERO)){
				ply.polynomial().remove(e);
			}else{
				ply.polynomial().put(e, c);
			}	
		}
		return ply;
	}
	
	/**
	 * polynomial divided by divisor
	 * @param divisor
	 * @throws FFaplAlgebraicException
	 */
	public void divide(BigInteger divisor) throws FFaplAlgebraicException{
		Polynomial ply = this.clone();
		BigInteger c, e;
		for(Iterator<BigInteger> itr =  _polynomialMap.keySet().iterator(); itr.hasNext(); ){
			isRunning();
			e = itr.next();
			c = ply.polynomial().get(e).divide(divisor);
			if(c.equals(ZERO)){
				ply.polynomial().remove(e);
			}else{
				ply.polynomial().put(e, c);
			}	
		}
	}
	
	/**
	 * Divide with divisor <Code>ply</Code>
	 * @param ply
	 * @throws FFaplAlgebraicException
	 */
	public void divide(Polynomial ply) throws FFaplAlgebraicException{
		if(ply.degree().compareTo(ONE) > 0){
			String[]arguments = {this + " " + FFaplInterpreter.tokenImage[FFaplInterpreter.DIVIDE].replace("\"", "") + " " + ply};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.DIVISION_NOT_REASONABLE);
		}else{
			
		}
	}

	@Override
	public int typeID() {
		return IJavaType.POLYNOMIAL;
	}
	
	/**
	 * Return String representation of a Polynomial
	 * @param ply
	 * @return
	 */
	public static String plyToString(Polynomial ply){
		BigInteger e, c;
		TreeMap<BigInteger, BigInteger> polynomialTable = ply.polynomial();
		String str = "" , tmp = "";
		//int counter = 1;
		//int max = Integer.MAX_VALUE;//max character in a line;
		if( polynomialTable.keySet().size() > 0){	
			for(Iterator<BigInteger> itr = polynomialTable.keySet().iterator(); itr.hasNext(); ){   	
		    	e = itr.next();
		    	c = polynomialTable.get(e);
		    	//if(str.length() > counter * max){
		    	//	str = str + "\n";
		    	//	counter ++;//reset counter
		    	//}
		    	if(c.compareTo(ZERO) < 0){
		    		if(! str.equals("")){
			    		//negative coefficient
			    		str = str + " - ";
		    		}else{
		    			str = str + " -";
		    		}
		    	}else{
		    		if(! str.equals("")){
		    		//positive coeffiecient
		    			str = str + " + ";
		    		}
		    	}
		    	tmp = "";
		    	if(! e.equals(ZERO)){
		    		if(! e.equals(ONE)){
		    			tmp = "x^" + e;
		    		}else{
		    			tmp = "x";
		    		}
		    	}
		    	if(! (c.abs().equals(ONE) && !e.equals(ZERO))){
		    		tmp = c.abs() + tmp;
		    	}
		    	
		    	str = str + tmp;
		    }
		}else{
			//Polynom is Zero
			str = "0";
		}
		return str;
	}

	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPOLYNOMIAL];
	}

	/**
	 * Modulo Operation 
	 * @param ply
	 * @throws FFaplAlgebraicException
	 */
	public void mod(Polynomial ply) throws FFaplAlgebraicException {
		String[] arguments = {this + " " + FFaplInterpreter.tokenImage[FFaplInterpreter.MODULO].replace("\"", "") + " " + ply};
		throw new FFaplAlgebraicException(arguments, IAlgebraicError.MOD_NOT_REASONABLE);
	}
	
	@Override
	public boolean equalType(Object type) {
		if(type instanceof Polynomial){
			return true;
		}
		return false;
	}
	
	/**
	 * @return the thread within the Class was created
	 */
	public Thread getThread(){
		return _thread;
	}

	/**
	 * throws an interrupt exception if not running
	 *
	 * @throws FFaplException
	 */
	protected void isRunning() throws FFaplAlgebraicException {
		if (_thread != null) {
			if (_thread.isInterrupted()) {
				throw new FFaplAlgebraicException(null, IAlgebraicError.INTERRUPT);
			}
		}
	}

	@Override
	public Polynomial addR(Polynomial summand) {
		Polynomial sum = this.clone();
		sum.add(summand);
		return sum;
	}

	@Override
	public Polynomial subR(Polynomial subtrahend) {
		Polynomial difference = this.clone();
		difference.subtract(subtrahend);
		return difference;
	}

	@Override
	public Polynomial multR(Polynomial factor) throws FFaplAlgebraicException {
		Polynomial product = this.clone();
		product.multiply(factor);
		return product;
	}

	@Override
	public Polynomial scalarMultR(BigInteger factor) throws FFaplAlgebraicException {
		Polynomial product = this.clone();
		product.multiply(factor, ZERO);
		return product;
	}

	@Override
	public Polynomial divR(Polynomial divisor) throws FFaplAlgebraicException {
		Polynomial quotient = this.clone();
		quotient.divide(divisor);
		return quotient;
	}

	@Override
	public Polynomial negateR() throws FFaplAlgebraicException {
		return this.negate();
	}

	@Override
	public Polynomial powR(BigInteger exponent) throws FFaplAlgebraicException {
		return this.clone().pow(exponent);
	}
}
