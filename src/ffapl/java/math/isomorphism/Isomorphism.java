package ffapl.java.math.isomorphism;

import ffapl.exception.FFaplException;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;

import java.math.BigInteger;
import java.util.TreeMap;

/**
 * This class represents an isomorphism between two isomorphic finite field extensions
 *
 * @author Dominic Weinberger
 * @version 1.0
 */
public class Isomorphism {

    private final GaloisField _sourceField;
    private final GaloisField _targetField;
    private final Matrix<BInteger> _transformationMatrix;
    private final Thread _thread;

    public Isomorphism(GaloisField sourceField, GaloisField targetField, Matrix<BInteger> transformationMatrix, Thread thread) {
        _sourceField = sourceField;
        _targetField = targetField;
        _transformationMatrix = transformationMatrix;
        _thread = thread;
    }

    /**
     * Applies the isomorphism transformation matrix to a field element.
     *
     * @param fieldElement which is the element to which the isomorphism is to be applied
     * @return transformed field element
     * @throws FFaplAlgebraicException when operations on underlying objects fail
     */
    public GaloisField applyTo(GaloisField fieldElement) throws FFaplAlgebraicException {
        if (!fieldElement.equalGF(this._sourceField)) {
            Object[] arguments ={_sourceField, "applyTo argument",
                    fieldElement, "Isomorphism"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.FIELDS_UNEQUAL);
        }

        GaloisField returnValue = _targetField.clone();
        returnValue.setValue(
                vectorToPolynomial(
                        _transformationMatrix.multiply(polynomialToVector(fieldElement.getValue())),
                        fieldElement.characteristic()));

        return returnValue;
    }

    /**
     * Transforms a polynomial to vector form.
     * The element in row "k" of the vector is the coefficient "a" of "a * x^(k-1)" in the polynomial.
     * Note that the Matrix class starts indexing at "1", not at zero.
     * Also note that the dimension of the vector is not determined by the degree of the polynomial but by the
     * amount of columns in the isomorphism transformation matrix.
     * E.g. the Polynomial "4 + x" would be transformed to (4 1 0)^T, if the transformation matrix had 3 columns
     *
     * @param polynomial which is to be transformed in vector form
     * @return Polynomial in vector form
     * @throws FFaplAlgebraicException thrown by the constructor of the Matrix class and
     *                                 thrown if deg(polynomial) is higher than the amount of columns in the transformation matrix - 1
     */
    private Matrix<BInteger> polynomialToVector(Polynomial polynomial) throws FFaplAlgebraicException {
        if (polynomial.degree().compareTo(BigInteger.valueOf(_transformationMatrix.getN() - 1)) > 0) {
            // It is an internal error since this code snippet being reached means there is a bug in the code.
            // Should not be reached by user error.
            throw new FFaplAlgebraicException(
                    new Object[]{"Polynomial degree > transformation matrix degree"},
                    IAlgebraicError.INTERNAL);
        }

        int numberOfColumns = Math.toIntExact(_transformationMatrix.getN());
        BInteger[][] matrix = new BInteger[numberOfColumns][1];

        for (int i = 0; i < numberOfColumns; i++) {
            matrix[i][0] = (BInteger) polynomial.coefficientAt(BigInteger.valueOf(i));
        }

        try {
            return new Matrix<>(matrix, BInteger.valueOf(0, _thread));
        } catch (FFaplException e) {
            throw new FFaplAlgebraicException(new Object[]{"isomorphism application"}, IAlgebraicError.INTERNAL);
        }
    }

    /**
     * Transforms a vector to a polynomial.
     * The element in row "k" of the vector is the coefficient "a" of "a * x^(k-1)" in the polynomial.
     * Note that the Matrix class starts indexing at "1", not at zero.
     * E.g. The vector (4 1 0)^T would be transformed to the Polynomial "4 + x"
     *
     * @param vector         which holds the polynomial to be retrieved in a matrix with one column
     * @param characteristic of the polynomial
     * @return Polynomial
     * @throws FFaplAlgebraicException thrown by the constructor of the PolynomialRC class
     */
    private PolynomialRC vectorToPolynomial(Matrix<BInteger> vector, BigInteger characteristic)
            throws FFaplAlgebraicException {
        TreeMap<BigInteger, BigInteger> polynomialMap = new TreeMap<>();
        int numberOfVectorRows = Math.toIntExact(vector.getM());

        for (int i = 1; i < numberOfVectorRows + 1; i++) {
            polynomialMap.put(BigInteger.valueOf(i - 1), vector.get(i, 1));
        }
        return new PolynomialRC(polynomialMap, characteristic, _thread);
    }
}
