package ffapl.java.math.isomorphism.calculation.cache;

import ffapl.java.classes.GaloisField;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;

import java.math.BigInteger;
import java.text.MessageFormat;

/**
 * This class contains all information needed to identify a finite field extension: the characteristic and the
 * irreducible polynomial used for constructing the extension. It is used in the IsomorphismCacheKey which in turn is
 * used by the IsomorphismCache, which stores isomorphisms between isomorphic finite fields.
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class GaloisFieldSpecification {
    private final PolynomialRC irreduciblePolynomial;
    private final BigInteger characteristic;

    public GaloisFieldSpecification(PolynomialRC irreduciblePolynomial, BigInteger characteristic) {
        this.irreduciblePolynomial = irreduciblePolynomial;
        this.characteristic = characteristic;
    }

    public GaloisFieldSpecification(GaloisField field) {
        this(field.irrPolynomial(), field.characteristic());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GaloisFieldSpecification)) {
            return false;
        }

        if (!((GaloisFieldSpecification)other).characteristic.equals(this.characteristic)) {
            return false;
        }

        try {
            if (!((GaloisFieldSpecification) other).irreduciblePolynomial.equals(this.irreduciblePolynomial)) {
                return false;
            }
        } catch (FFaplAlgebraicException exception) {
            // FFaplAlgebraicException cannot be rethrown here as it is a checked exception. An unchecked exception
            // is needed since no "throws" clause can be added to the equals method inherited from Object.
            throw new RuntimeException(exception);
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (irreduciblePolynomial == null ? 0 : irreduciblePolynomial.hashCode());
        hash = 31 * hash + (characteristic == null ? 0 : characteristic.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return MessageFormat.format("GF({0}, {1})", characteristic, irreduciblePolynomial);
    }
}
