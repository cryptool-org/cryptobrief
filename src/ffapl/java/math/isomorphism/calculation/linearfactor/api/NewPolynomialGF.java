package ffapl.java.math.isomorphism.calculation.linearfactor.api;

import ffapl.FFaplInterpreter;
import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.Polynomial;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;
import java.util.Set;
import java.util.TreeMap;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class NewPolynomialGF {

    protected TreeMap<BigInteger, GaloisFieldElement> _polynomialMap;
    protected Thread _thread;
    private final MyGaloisField _field; // All coefficients must be elements of this field

    /**
     * Default Constructor
     *
     * @param thread
     */
    public NewPolynomialGF(MyGaloisField field, Thread thread) {
        _polynomialMap = new TreeMap<>();
        _field = field;
        _thread = thread;
    }

    /**
     * Constructor
     * @param c coefficient
     * @param e exponent
     * @param thread
     */
    public NewPolynomialGF(GaloisFieldElement c, BigInteger e, Thread thread) {
        _field = c.field();
        _thread = thread;
        _polynomialMap = new TreeMap<>();
        _polynomialMap.put(e, c);
    }

    /**
     * Constructor
     *
     * @param polynomialTable
     * @param thread
     */
    @SuppressWarnings("unchecked")
    public NewPolynomialGF(MyGaloisField field, TreeMap<BigInteger, GaloisFieldElement> polynomialTable, Thread thread) {
        _polynomialMap = (TreeMap<BigInteger, GaloisFieldElement>) polynomialTable.clone();
        _field = field;
        _thread = thread;
    }

    /**
     * Returns the degree of the Polynomial
     *
     * @return
     */
    public BigInteger degree() {
        clearZeroCoefficients();

        if (_polynomialMap == null)
            return null;

        if (!_polynomialMap.keySet().isEmpty())
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
        return _polynomialMap != null && this._polynomialMap.values()
                .removeIf((galoisField -> galoisField.value().isZero()));
    }

    /**
     * Adds the specified Polynomial <Code> ply </Code> to the current Polynomial
     *
     * @param ply
     */
    public void add(NewPolynomialGF ply) throws FFaplAlgebraicException {
        GaloisFieldElement firstCoefficient, secondCoefficient;
        for (BigInteger currentExponent : ply.exponents()) {
            firstCoefficient = ply._polynomialMap.get(currentExponent);
            if (this._polynomialMap.containsKey(currentExponent)) {
                secondCoefficient = this._polynomialMap.get(currentExponent);
                secondCoefficient = secondCoefficient.addR(firstCoefficient);
                if (secondCoefficient.value().isZero()) {
                    this._polynomialMap.remove(currentExponent);
                } else {
                    this._polynomialMap.put(currentExponent, secondCoefficient);
                }
            } else {
                this._polynomialMap.put(currentExponent, firstCoefficient);
            }
        }
    }

    /**
     * subtracts the specified Polynomial <Code> ply </Code> from the current Polynomial
     *
     * @param ply
     */
    public void subtract(NewPolynomialGF ply) throws FFaplAlgebraicException {
        NewPolynomialGF secondPolynomial = ply.clone();
        this.add(secondPolynomial.negate());
    }

    /**
     * Multiplies the specified Polynomial <Code>ply</Code> to the current Polynomial
     *
     * @param ply
     * @throws FFaplAlgebraicException
     */
    public void multiply(NewPolynomialGF ply) throws FFaplAlgebraicException {
        NewPolynomialGF p1, p2;
        p2 = new NewPolynomialGF(_field, _thread);
        for (BigInteger currentExponent : ply.exponents()) {
            isRunning();
            GaloisFieldElement currentCoefficient = ply._polynomialMap.get(currentExponent);
            p1 = this.clone();
            p1.multiply(currentCoefficient, currentExponent);
            p2.add(p1);
        }
        this.setPolynomial(p2.polynomial());
    }

    /**
     * Multiplies coefficient*x^exponent to the specified Polynomial
     * @param coefficient coefficient
     * @param exponent exponent
     * @throws FFaplAlgebraicException
     */
    public void multiply(GaloisFieldElement coefficient, BigInteger exponent) throws FFaplAlgebraicException{
        TreeMap<BigInteger, GaloisFieldElement> polynomialTable = new TreeMap<>();
        for(BigInteger currentExponent : this.exponents()) {
            isRunning();
            GaloisFieldElement currentCoefficient = this._polynomialMap.get(currentExponent);
            currentExponent = currentExponent.add(exponent);//add exponent
            currentCoefficient.multiply(coefficient); // multiply coefficient

            if(!currentCoefficient.value().isZero()){
                polynomialTable.put(currentExponent, currentCoefficient);
            }
        }
        _polynomialMap = polynomialTable;
    }

    /**
     * calculates polynomial^exponent
     *
     * @param exponent
     * @return
     * @throws FFaplAlgebraicException
     */
    public NewPolynomialGF pow(BigInteger exponent) throws FFaplAlgebraicException {
        if (exponent.compareTo(ZERO) < 0) {
            String[] arguments = {exponent.toString(),
                    "PolynomialGF -> " + this +
                            FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + exponent};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.NEGATIVE_EXPONENT);
        }
        if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0) {
            this.setPolynomial(squareAndMultiply(this, exponent).polynomial());
        } else {
            String[] arguments = {this +
                    FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + exponent};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.TO_HIGH_EXPONENT);
        }
        return this;
    }

    /**
     * Returns the TreeMap of the Polynomial
     *
     * @return
     */
    public TreeMap<BigInteger, GaloisFieldElement> polynomial() {
        return this._polynomialMap;
    }

    /**
     * Returns the sorted Exponents of the polynomial
     *
     * @return
     */
    public Set<BigInteger> exponents() {
        clearZeroCoefficients();
        return this._polynomialMap.keySet();
    }

    /**
     * Returns the characteristic of the GF of the coefficients
     * @return
     */
    public BigInteger characteristic(){
        return this._field.characteristic();
    }

    /**
     * Returns a copy of the Galois Field over which this Polynomial is defined.
     * @return
     */
    public MyGaloisField field() {
        return this._field;
    }

    /**
     * Sets the Polynomial
     *
     * @param polynomTable
     */
    public void setPolynomial(TreeMap<BigInteger, GaloisFieldElement> polynomTable) {
        _polynomialMap = polynomTable;
    }

    /**
     * Sets the Polynomial to c*x^e
     * @param c
     * @param e
     */
    public void setPolynomial(GaloisFieldElement c, BigInteger e){
        _polynomialMap.clear();
        _polynomialMap.put(e, c);
    }

    /**
     * @param val
     * @return the result of the polynomial if x = val
     * @throws FFaplAlgebraicException
     */
    public GaloisFieldElement calculate(BInteger val) throws FFaplAlgebraicException {
        GaloisFieldElement result = new GaloisFieldElement(_field, _thread);
        result.setValue(new Polynomial(_thread));   // Initialize result to "0"
        for (BigInteger currentExponent : _polynomialMap.keySet()) {
            GaloisFieldElement currentCoefficient = _polynomialMap.get(currentExponent);
            result.add(currentCoefficient.calculate(val));
        }
        return result;
    }

    /**
     * Returns true if the polynomial is monic
     *
     * @return
     */
    public boolean isMonic() {
        clearZeroCoefficients();
        return _polynomialMap != null && _polynomialMap.lastEntry() != null &&
                _polynomialMap.lastEntry().getValue().value().isOne();
    }

    /**
     * Returns true if polynomial = 0
     *
     * @return
     */
    public boolean isZero() {
        clearZeroCoefficients();
        return _polynomialMap == null || _polynomialMap.isEmpty();
    }

    /**
     * Returns true if polynomial = 1
     *
     * @return
     */
    public boolean isOne() {
        return this.isMonic() && this.isConstant();
    }

    /**
     * Returns true if polynomial is constant, i.e. has degree 0.
     *
     * @return
     */
    public boolean isConstant() {
        clearZeroCoefficients();
        return _polynomialMap == null || degree().equals(ZERO);
    }

    /**
     * Return the leading coefficient of the polynomial
     *
     * @return
     */
    public GaloisFieldElement leadingCoefficient() throws FFaplAlgebraicException {
        if (_polynomialMap.isEmpty()) {
            GaloisFieldElement result = new GaloisFieldElement(_field, _thread);
            result.setValue(new Polynomial(_thread));
            return result;
        } else {
            return this._polynomialMap.get(this.degree()).clone();
        }
    }

    /**
     * Returns the monic representation of the Polynomial
     * @return
     * @throws FFaplAlgebraicException
     */
    public NewPolynomialGF getMonic() throws FFaplAlgebraicException{
        if (this.isMonic()) {
            return this.clone();
        }

        NewPolynomialGF result = this.clone();
        result.divide(this.leadingCoefficient());
        return result;
    }

    /**
     * Returns the coefficient of x^e
     *
     * @param e
     * @return the coefficient of x^e in the polynomial
     */
    public GaloisFieldElement coefficientAt(BigInteger e) throws FFaplAlgebraicException {
        if (_polynomialMap.containsKey(e)) {
            return this._polynomialMap.get(e).clone();
        } else {
            GaloisFieldElement result = new GaloisFieldElement(_field, _thread);
            result.setValue(new Polynomial(_thread));
            return result;
        }
    }

    @Override
    public NewPolynomialGF clone() {
        TreeMap<BigInteger, GaloisFieldElement> polynomialTable = new TreeMap<>();
        for (BigInteger coefficient : _polynomialMap.keySet()) {
            polynomialTable.put(coefficient, _polynomialMap.get(coefficient).clone());
        }

        return new NewPolynomialGF(_field, polynomialTable, _thread);
    }

    @Override
    public String toString() {
        return NewPolynomialGF.plyToString(this);
    }

    /**
     * Returns the negate of a polynomial
     *
     * @return
     * @throws FFaplAlgebraicException
     */
    public NewPolynomialGF negate() throws FFaplAlgebraicException {
        TreeMap<BigInteger, BigInteger> polynomialTable = new TreeMap<>();
        polynomialTable.put(ZERO, ONE);

        GaloisFieldElement minusOne = new GaloisFieldElement(_field, _thread);
        minusOne.setValue(new Polynomial(polynomialTable, _thread));
        minusOne = minusOne.negate();

        NewPolynomialGF ply = this.clone();
        ply.multiply(minusOne, ZERO);
        return ply;
    }

    /**
     * polynomial divided by divisor
     *
     * @param divisor
     * @throws FFaplAlgebraicException
     */
    public void divide(GaloisFieldElement divisor) throws FFaplAlgebraicException {
        NewPolynomialGF ply = this.clone();
        for (BigInteger currentExponent : ply.exponents()) {
            isRunning();
            GaloisFieldElement currentCoefficient = ply.coefficientAt(currentExponent);
            currentCoefficient.divide(divisor);
            if (currentCoefficient.value().isZero()) {
                ply.polynomial().remove(currentExponent);
            } else {
                ply.polynomial().put(currentExponent, currentCoefficient);
            }
        }
        this.setPolynomial(ply.polynomial());
    }

    /**
     * Divides a polynomial of a residue class with this polynomial
     * @param ply1
     * @param ply2
     * @throws FFaplAlgebraicException
     *         <CHARACTERISTIC_UNEQUAL> if characteristics of the two polynomials are unequal
     */
    public static NewPolynomialGF[] divide(NewPolynomialGF ply1, NewPolynomialGF ply2) throws FFaplAlgebraicException{
        NewPolynomialGF r, p, tmp;
        GaloisFieldElement lambda, my;//leading coefficient
        BigInteger n, m; //degrees
        NewPolynomialGF[] result = new NewPolynomialGF[2]; //0=result 1=rest
        //check if same fields
        if(! ply1.field().equalGF(ply2.field())){
            Object[] arguments ={ply1.field(), "PolynomialGF",
                    ply2.field(), "PolynomialGF"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.FIELDS_UNEQUAL);
        }

        r = ply1.clone();
        p = ply1.clone();
        p.setPolynomial(new GaloisFieldElement(ply1.field(), ply1._thread), ZERO);
        lambda = ply2.leadingCoefficient();
        n = ply2.degree();
        try{
            while(r.degree().compareTo(n) >= 0 && !r.leadingCoefficient().value().isZero()){
                m = r.degree();
                my = r.leadingCoefficient();
                //is ~~ my/lambda X^(m-n)~~
                tmp = new NewPolynomialGF(
                        my.divR(lambda),
                        m.subtract(n),
                        ply1.getThread());
                p.add(tmp);
                tmp.multiply(ply2);
                r.subtract(tmp);
            }
        }catch(ArithmeticException e){
            Object[] arguments ={ply2, "PolynomialGF"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.NO_MULTINVERSE_COEFF);
        }
        result[0] = p;
        result[1] = r;

        return result;
    }

    /**
     * Return String representation of a bivariate Polynomial
     *
     * @param ply
     * @return
     */
    public static String plyToString(NewPolynomialGF ply) {
        ply.clearZeroCoefficients();
        TreeMap<BigInteger, GaloisFieldElement> polynomialTable = ply.polynomial();
        StringBuilder stringBuilder = new StringBuilder();

        if (polynomialTable.isEmpty()) {
            return "0";
        }

        for (BigInteger currentExponent : ply.exponents()) {
            GaloisFieldElement currentCoefficient = ply._polynomialMap.get(currentExponent);
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" + ");
            }
            String coefficientString = Polynomial.plyToString(currentCoefficient.value(), 'y');
            if (!currentCoefficient.value().isConstant()) {
                stringBuilder.append(String.format("(%s)", coefficientString));
            } else if (currentExponent.compareTo(ZERO) == 0 || !currentCoefficient.value().isOne()){
                stringBuilder.append(coefficientString);
            }

            if (!currentExponent.equals(ZERO)) {
                if (!currentExponent.equals(ONE)) {
                    stringBuilder.append("x^").append(currentExponent);
                } else {
                    stringBuilder.append("x");
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * @return the thread within the Class was created
     */
    public Thread getThread() {
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

    /**
     * Repeated square-and-multiply algorithm for exponentiation
     * calculates g^k
     * @param g
     * @param k
     * @return
     * @throws FFaplAlgebraicException
     */
    public static NewPolynomialGF squareAndMultiply(NewPolynomialGF g,
                                                    BigInteger k) throws FFaplAlgebraicException{
        NewPolynomialGF s, G;
        Thread thread = g.getThread();
        //s=1
        GaloisFieldElement oneCoefficient = new GaloisFieldElement(g.field(), thread);
        oneCoefficient.setValue(new Polynomial(ONE, ZERO, thread));
        s = new NewPolynomialGF(oneCoefficient, ZERO, thread);

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
            Algorithm.isRunning(thread);//to interrupt calculation
            G.multiply(G);
            //if ki = 1
            if(k.testBit(i)){
                //s <- G*s
                s.multiply(G);
            }
        }
        return s;
    }


}
