package ffapl.java.math.isomorphism.calculation.linearfactor.api;

import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class GaloisFieldElement {

    private final MyGaloisField _field;
    private PolynomialRCPrime _value;
    /**
     * the thread within the Galois Field is created
     */
    private final Thread _thread;

    public GaloisFieldElement(MyGaloisField field, Thread thread) throws FFaplAlgebraicException {
        _thread = thread;
        _field = field;
        _value = new PolynomialRCPrime(field.characteristic(), _thread);
    }

    public PolynomialRCPrime irrPolynomial() {
        return (PolynomialRCPrime) this._field.irrPolynomial().clone();
    }

    public PolynomialRCPrime value() {
        return _value;
    }

    public void setValue(Polynomial ply) throws FFaplAlgebraicException {
        if (!characteristicMatches(ply)) {
            return;
        }

        _value.setPolynomial(ply.polynomial());
        _value.mod(_field.irrPolynomial());
    }

    public PolynomialRC getValue() {
        return (PolynomialRC) _value.clone();
    }

    public BigInteger characteristic() {
        return _field.characteristic();
    }

    public BigInteger order() throws FFaplAlgebraicException {
        return _field.order();
    }

    public void add(GaloisFieldElement gf) throws FFaplAlgebraicException {
        if (!this.equalGF(gf)) {
            Object[] arguments = {this.classInfo(), gf.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE);
        }

        _value.add(gf.value());
         _value.mod(_field.irrPolynomial());
    }

    public void add(BigInteger summand) throws FFaplAlgebraicException {
        _value.add(summand, ZERO);
         _value.mod(_field.irrPolynomial());
    }

    public void multiply(GaloisFieldElement gf) throws FFaplAlgebraicException {
        if (!this.equalGF(gf)) {
            Object[] arguments = {this.classInfo(), gf.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE);
        }

        _value.multiply(gf.value());
         _value.mod(_field.irrPolynomial());
    }

    public void multiply(BigInteger factor) throws FFaplAlgebraicException {
        _value.multiply(factor, ZERO);
         _value.mod(_field.irrPolynomial());
    }

    public void divide(GaloisFieldElement gf) throws FFaplAlgebraicException {
        if (!this.equalGF(gf)) {
            Object[] arguments = {this.classInfo(), gf.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.TYPES_INCOMPATIBLE);
        }
        gf = GaloisFieldElement.inverse(gf);
        this.multiply(gf);
    }

    public boolean equalGF(GaloisFieldElement other) throws FFaplAlgebraicException {
        return this._field.equalGF(other._field);
    }

    public String toString() {
        return Polynomial.plyToString(_value);// + " in GF(" + _p + ", " + Polynomial.plyToString(_irrply) + ")";
    }

    public GaloisFieldElement clone() {
        try {
            GaloisFieldElement gf =  new GaloisFieldElement(_field, _thread);
            gf.setValue(_value.clone());
            return gf;
        } catch (FFaplAlgebraicException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GaloisFieldElement inverse(GaloisFieldElement val) throws FFaplAlgebraicException {
        GaloisFieldElement result;
        PolynomialRC[] tmp;

        if (val.value().isZero()) {
            Object[] arguments = {val.value(), val.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.NO_MULTINVERSE);
        }

        tmp = Algorithm.eea(val.value(), val.irrPolynomial());
        //tmp = [d, s, t] .... d = s*g + t*h
        if (!tmp[0].isOne()) {
            //d <> 1
            if (tmp[0].degree().compareTo(ZERO) > 0) {
                Object[] arguments = {"Error in GF inverse - EEA"};
                throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
            }
        }
        result = new GaloisFieldElement(val._field, val.getThread());
        result.setValue(tmp[1]);
        return result;
    }

    public Thread getThread() {
        return _thread;
    }

    public GaloisFieldElement negate() throws FFaplAlgebraicException {
        GaloisFieldElement gf = this.clone();
        gf.multiply(ONE.negate());
        return gf;
    }

    public BigInteger calculate(BInteger val) throws FFaplAlgebraicException {
        return _value.calculate(val);
    }


    public GaloisFieldElement addR(GaloisFieldElement summand) throws FFaplAlgebraicException {
        GaloisFieldElement sum = this.clone();
        sum.add(summand);
        return sum;
    }

    public GaloisFieldElement divR(GaloisFieldElement divisor) throws FFaplAlgebraicException {
        GaloisFieldElement quotient = this.clone();
        quotient.divide(divisor);
        return quotient;
    }

    public boolean characteristicMatches(Polynomial ply) throws FFaplAlgebraicException {
        if (ply instanceof PolynomialRC && !_value.characteristic().equals(((PolynomialRC) ply).characteristic())) {
            Object[] arguments = {this.characteristic(), this.classInfo(),
                    ((PolynomialRC) ply).characteristic(), ply.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.CHARACTERISTIC_UNEQUAL);
        }
        return true;
    }

    public String classInfo() {
        return "GF(" + _value.characteristic() + ", " + Polynomial.plyToString(_field.irrPolynomial()) + ")";
    }

    public MyGaloisField field() {
        return _field;
    }
}
