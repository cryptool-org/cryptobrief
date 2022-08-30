package ffapl.java.math.isomorphism.calculation.linearfactor.api;

import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.classes.PolynomialRCPrime;
import ffapl.java.classes.Prime;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;

public class MyGaloisField implements IJavaType<MyGaloisField> {

    private Prime _p;
    private PolynomialRC _irrply;
    private Thread _thread;

    private BigInteger _order;

    /**
     * @param characteristic
     * @param polynomial
     * @param thread
     * @throws FFaplAlgebraicException
     */
    public MyGaloisField(BigInteger characteristic, Polynomial polynomial, Thread thread) throws FFaplAlgebraicException {
        _thread = thread;
        _p = new Prime(characteristic, _thread);
        _irrply = new PolynomialRCPrime(polynomial.polynomial(), _p, _thread);
        _order = (BigInteger) _p.pow(_irrply.degree());
        if (!Algorithm.isIrreducible(_irrply)) {
            Object[] arguments = {_irrply};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.NOTIRREDUCIBLE);
        }
    }

    /**
     * @param characteristic
     * @param polynomial
     * @throws FFaplAlgebraicException
     */
    public MyGaloisField(long characteristic, Polynomial polynomial, Thread thread) throws FFaplAlgebraicException {
        this(BigInteger.valueOf(characteristic), polynomial, thread);
    }


    /**
     * Return a copy of the irreducible polynomial
     *
     * @return
     */
    public PolynomialRCPrime irrPolynomial() {
        return (PolynomialRCPrime) this._irrply;
    }

    /**
     * Returns the characteristic of the GF
     *
     * @return
     */
    public BigInteger characteristic() {
        return this._p;
    }

    /**
     * Returns the order of the GF
     *
     * @return
     */
    public BigInteger order() throws FFaplAlgebraicException {
        return _order;
    }

    public boolean equals(IJavaType val) throws FFaplAlgebraicException {
        if (val instanceof MyGaloisField) {
            return this.equalGF((MyGaloisField) val);
        } else {
            String[] arguments = {"GaloisField equals operation try to compare " +
                    this.classInfo() + " with " + val.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
        }
    }

    /**
     * Returns true if GaloisFields are isomorphic
     *
     * @return true if characteristics are equal and irreducible polynomials have the same degree, false otherwise.
     */
    public boolean isIsomorphicTo(MyGaloisField other) {
        return this.characteristic().equals(other.characteristic())
                && this._irrply.degree().equals(other._irrply.degree());
    }

    /**
     * @return the thread within the GaloisField is created
     */
    public Thread getThread() {
        return _thread;
    }

    @Override
    public int typeID() {
        return IJavaType.GALOISFIELD;
    }

    @Override
    public String classInfo() {
        return "GF(" + _p + ", " + Polynomial.plyToString(_irrply) + ")";
    }

    @Override
    public MyGaloisField clone() {
        try {
            return new MyGaloisField(this._p.clone(), this._irrply.clone(), this._thread);
        } catch (FFaplAlgebraicException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equalGF(MyGaloisField gf) throws FFaplAlgebraicException {
        return this._irrply.equals(gf.irrPolynomial());
    }

    @Override
    public boolean equalType(Object type) {
        if (type instanceof MyGaloisField) {
            try {
                return equalGF((MyGaloisField) type);
            } catch (FFaplAlgebraicException e) {
                //
            }
        }
        return false;
    }
}
