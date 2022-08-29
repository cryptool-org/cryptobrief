package ffapl.java.math.isomorphism.calculation.linearfactor;

import ffapl.FFaplInterpreter;
import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.Polynomial;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;
import java.util.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;


/**
 * @author Dominic Weinberger
 * @version 1.0
 *
 *  Represents a polynomial with coefficients in a Galois field. Therefore it needs two indeterminates, X and Y, and is
 *  of the form a_n * X^n + a_(n-1) * X^(n-1) + ... + a_0 * X^0 where a_0, ..., a_n are the elements in
 *  the Galois field with Y as the indeterminate.
 *  An example: (y+1)*X^3 + (2y^2+1)*X + (y+2) with coefficients in GF(3^3)
 */

public class PolynomialGF {

    protected TreeMap<BigInteger, GaloisField> _polynomialMap;
    protected Thread _thread;
    private final GaloisField _field; // All coefficients must be elements of this field

    /**
     * Default Constructor
     *
     * @param thread
     */
    public PolynomialGF(GaloisField field, Thread thread) {
        _polynomialMap = new TreeMap<>();
        _field = field;
        _thread = thread;
    }

    /**
     * Constructor
     * @param c coefficient
     * @param e exponent
     * @param field GaloisField
     * @param thread
     */
    public PolynomialGF(GaloisField c, BigInteger e, GaloisField field, Thread thread) {
        _field = field;
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
    public PolynomialGF(GaloisField field, TreeMap<BigInteger, GaloisField> polynomialTable, Thread thread) {
        _polynomialMap = (TreeMap<BigInteger, GaloisField>) polynomialTable.clone();
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
    public void add(PolynomialGF ply) throws FFaplAlgebraicException {
        GaloisField firstCoefficient, secondCoefficient;
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
    public void subtract(PolynomialGF ply) throws FFaplAlgebraicException {
        PolynomialGF secondPolynomial = ply.clone();
        this.add(secondPolynomial.negate());
    }

    /**
     * Multiplies the specified Polynomial <Code>ply</Code> to the current Polynomial
     *
     * @param ply
     * @throws FFaplAlgebraicException
     */
    public void multiply(PolynomialGF ply) throws FFaplAlgebraicException {
        PolynomialGF p1, p2;
        p2 = new PolynomialGF(_field, _thread);
        for (BigInteger currentExponent : ply.exponents()) {
            isRunning();
            GaloisField currentCoefficient = ply._polynomialMap.get(currentExponent);
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
    public void multiply(GaloisField coefficient, BigInteger exponent) throws FFaplAlgebraicException{
        TreeMap<BigInteger, GaloisField> polynomialTable = new TreeMap<>();
        for(BigInteger currentExponent : this.exponents()) {
            isRunning();
            GaloisField currentCoefficient = this._polynomialMap.get(currentExponent);
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
    public PolynomialGF pow(BigInteger exponent) throws FFaplAlgebraicException {
        if (exponent.compareTo(ZERO) < 0) {
            String[] arguments = {exponent.toString(),
                    "PolynomialGF -> " + this +
                            FFaplInterpreter.tokenImage[FFaplInterpreter.POWER].replace("\"", "") + exponent};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.NEGATIVE_EXPONENT);
        }
        if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0) {
            this.setPolynomial(Algorithm.squareAndMultiply(this, exponent).polynomial());
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
    public TreeMap<BigInteger, GaloisField> polynomial() {
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
    public GaloisField field() {
        return this._field.clone();
    }

    /**
     * Sets the Polynomial
     *
     * @param polynomTable
     */
    public void setPolynomial(TreeMap<BigInteger, GaloisField> polynomTable) {
        _polynomialMap = polynomTable;
    }

    /**
     * Sets the Polynomial to c*x^e
     * @param c
     * @param e
     */
    public void setPolynomial(GaloisField c, BigInteger e){
        _polynomialMap.clear();
        _polynomialMap.put(e, c);
    }

    /**
     * @param val
     * @return the result of the polynomial if x = val
     * @throws FFaplAlgebraicException
     */
    public GaloisField calculate(BInteger val) throws FFaplAlgebraicException {
        GaloisField result = _field.clone();
        result.setValue(new Polynomial(_thread));   // Initialize result to "0"
        for (BigInteger currentExponent : _polynomialMap.keySet()) {
            GaloisField currentCoefficient = _polynomialMap.get(currentExponent);
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
    public GaloisField leadingCoefficient() throws FFaplAlgebraicException {
        if (_polynomialMap.isEmpty()) {
            GaloisField result = _field.clone();
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
    public PolynomialGF getMonic() throws FFaplAlgebraicException{
        if (this.isMonic()) {
            return this.clone();
        }

        PolynomialGF result = this.clone();
        result.divide(this.leadingCoefficient());
        return result;
    }

    /**
     * Returns the coefficient of x^e
     *
     * @param e
     * @return the coefficient of x^e in the polynomial
     */
    public GaloisField coefficientAt(BigInteger e) throws FFaplAlgebraicException {
        if (_polynomialMap.containsKey(e)) {
            return this._polynomialMap.get(e).clone();
        } else {
            GaloisField result = _field.clone();
            result.setValue(new Polynomial(_thread));
            return result;
        }
    }

    @Override
    public PolynomialGF clone() {
        TreeMap<BigInteger, GaloisField> polynomialTable = new TreeMap<>();
        for (BigInteger coefficient : _polynomialMap.keySet()) {
            polynomialTable.put(coefficient, _polynomialMap.get(coefficient).clone());
        }

        return new PolynomialGF(_field, polynomialTable, _thread);
    }

    @Override
    public String toString() {
        return PolynomialGF.plyToString(this);
    }

    /**
     * Returns the negate of a polynomial
     *
     * @return
     * @throws FFaplAlgebraicException
     */
    public PolynomialGF negate() throws FFaplAlgebraicException {
        TreeMap<BigInteger, BigInteger> polynomialTable = new TreeMap<>();
        polynomialTable.put(ZERO, ONE);

        GaloisField minusOne = _field.clone();
        minusOne.setValue(new Polynomial(polynomialTable, _thread));
        minusOne = minusOne.negate();

        PolynomialGF ply = this.clone();
        ply.multiply(minusOne, ZERO);
        return ply;
    }

    /**
     * polynomial divided by divisor
     *
     * @param divisor
     * @throws FFaplAlgebraicException
     */
    public void divide(GaloisField divisor) throws FFaplAlgebraicException {
        PolynomialGF ply = this.clone();
        for (BigInteger currentExponent : ply.exponents()) {
            isRunning();
            GaloisField currentCoefficient = ply.coefficientAt(currentExponent);
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
    public static PolynomialGF[] divide(PolynomialGF ply1, PolynomialGF ply2) throws FFaplAlgebraicException{
        PolynomialGF r, p, tmp;
        GaloisField lambda, my;//leading coefficient
        BigInteger n, m; //degrees
        PolynomialGF[] result = new PolynomialGF[2]; //0=result 1=rest
        //check if same fields
        if(! ply1.field().equalGF(ply2.field())){
            Object[] arguments ={ply1.field(), "PolynomialGF",
                    ply2.field(), "PolynomialGF"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.FIELDS_UNEQUAL);
        }

        r = ply1.clone();
        p = ply1.clone();
        p.setPolynomial(new GaloisField(ply1.characteristic(), ply1.field().irrPolynomial(), ply1._thread), ZERO);
        lambda = ply2.leadingCoefficient();
        n = ply2.degree();
        try{
            while(r.degree().compareTo(n) >= 0 && !r.leadingCoefficient().value().isZero()){
                m = r.degree();
                my = r.leadingCoefficient();
                //is ~~ my/lambda X^(m-n)~~
                tmp = new PolynomialGF(
                        my.divR(lambda),
                        m.subtract(n),
                        ply1.field(), ply1.getThread());
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
    public static String plyToString(PolynomialGF ply) {
        ply.clearZeroCoefficients();
        TreeMap<BigInteger, GaloisField> polynomialTable = ply.polynomial();
        StringBuilder stringBuilder = new StringBuilder();

        if (polynomialTable.isEmpty()) {
            return "0";
        }

        for (BigInteger currentExponent : ply.exponents()) {
            GaloisField currentCoefficient = ply._polynomialMap.get(currentExponent);
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
}
