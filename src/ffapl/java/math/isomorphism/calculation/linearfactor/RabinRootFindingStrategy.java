package ffapl.java.math.isomorphism.calculation.linearfactor;

import ffapl.java.classes.BInteger;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.Algorithm;

import java.math.BigInteger;
import java.util.TreeMap;

import static ffapl.java.math.Algorithm.isRunning;
import static java.math.BigInteger.*;

/**
 * Searching for a root of a polynomial "f" in a field "f_q" using randomized GCD splitting
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class RabinRootFindingStrategy implements RootFindingStrategy {

    /**
     * Searches for a root of "f" in "f_q". First the trivial case f(0) is checked. Then it is checked whether
     * f has any roots in common with the polynomial X^q-X, where q is the order of the field F_q. Since all elements
     * of F_q are roots of X^q-X, g = gcd(X^q-X, f) must be of degree > 0 in order for f to have roots in F_q.
     * In a loop, which continues as long as deg(g) > 1, we select in each step a random element "alpha" of F_q and
     * construct another polynomial "h" from it. If the order of F_q is odd, then h = ((X - alpha)^((q-1)/2) - 1).
     * If the order is even, i.e. q = 2^e, then h = X*alpha + (X*alpha)^2^1 + ... + (X*alpha)^2^e.
     * We then calculate f_1 = gcd(g, h) and if 0 < deg(f_1) < h we also calculate f_2 = g / f_1. "g" is then replaced
     * by either f_1 or f_2, whichever has lower degree. When finally deg(g) == 1, we normalize it such that it has the
     * form "X - r", from which we can then extract "r" as a root and return it.
     * @param f which is the polynomial for which a root of f in the field f_q is to be found
     * @param f_q which is the field containing the root a
     * @return r, such that f(r) = 0 in f_q or null, if no such r exists
     * @throws FFaplAlgebraicException if underlying operations fail
     */
    @Override
    public PolynomialRC findRootOf(PolynomialRC f, GaloisField f_q) throws FFaplAlgebraicException {
        Thread thread = f.getThread();

        // If f(0) = 0, return 0;
        if (f.calculate(new BInteger(ZERO, thread)).equals(ZERO)) {
            return new PolynomialRC(f.characteristic(), thread);
        }

        PolynomialGF splittingFieldPolynomial = splittingFieldPolynomialForField(f_q, thread);  // X^q - X
        PolynomialGF g = gcd(transform(f,f_q), splittingFieldPolynomial);
        if (g.isConstant()) {
            // The polynomial f has no roots in common with the splitting field polynomial,
            // which contains all the roots of F_q. Therefore, f has no roots in F_q
            return null;
        }

        while (g.degree().compareTo(ONE) > 0) { //while degree of g is strictly greater than one
            GaloisField randomAlpha = randomElementOfField(f_q);
            PolynomialGF h = getHelperPolynomial(randomAlpha);
            h = PolynomialGF.divide(h, g)[1]; // h := h mod g
            PolynomialGF f_1 = gcd(g, h);
            if (f_1.degree().compareTo(ZERO) > 0 && f_1.degree().compareTo(g.degree()) < 0) {
                PolynomialGF f_2 = PolynomialGF.divide(g, f_1)[0];
                if (f_1.degree().compareTo(f_2.degree()) < 0) {
                    g = f_1;
                } else {
                    g = f_2;
                }
            }
        }

        // At this stage, g is linear, i.e. of the form X - r. We get r = -g(0);
        g = g.getMonic();
        return g.coefficientAt(ZERO).negate().value();
    }

    /**
     * Transform a polynomial "f" to an instance of "PolynomialGF" by transforming the Integer coefficients of f to
     * GaloisField coefficients in the field "f_q".
     * @param f the polynomial to be transformed
     * @param f_q the field in which the coefficients of "f" are to be embedded in
     * @return a PolynomialGF with the coefficients of "f" now embedded in the Galois field f_q
     */
    private PolynomialGF transform(Polynomial f, GaloisField f_q) throws FFaplAlgebraicException {
        TreeMap<BigInteger, GaloisField> polynomialTable = new TreeMap<>();
        for (BigInteger currentExponent : f.exponents()) {
            GaloisField currentNewCoefficient = f_q.clone();
            currentNewCoefficient.setValue(
                    new Polynomial(f.coefficientAt(currentExponent), ZERO, f_q.getThread()));
            polynomialTable.put(currentExponent, currentNewCoefficient);
        }
        return new PolynomialGF(f_q, polynomialTable, f_q.getThread());
    }

    /**
     * If q is odd, this function returns ((X - alpha)^((q-1)/2) - 1),
     * If q is even, i.e. q = 2^e, this function returns X*alpha + (X*alpha)^2^1 + ... + (X*alpha)^2^e
     * @param alpha which is a random element in the field F_q
     * @return a polynomial which form depends on whether q is even or odd
     */
    private PolynomialGF getHelperPolynomial(GaloisField alpha) throws FFaplAlgebraicException {
        if (alpha.order().mod(TWO).equals(ONE)) {
            // return ((X + alpha)^((q-1)/2) - 1)
            GaloisField oneElement = alpha.clone();
            oneElement.setValue(new Polynomial(ONE, ZERO, alpha.getThread()));

            PolynomialGF result = new PolynomialGF(alpha, alpha.getThread());
            TreeMap<BigInteger, GaloisField> resultInitializationMap = new TreeMap<>();
            resultInitializationMap.put(ZERO, alpha); // set coefficient of X^0 to (-alpha)
            resultInitializationMap.put(ONE, oneElement); // set coefficient of X^1 to 1
            result.setPolynomial(resultInitializationMap);

            BigInteger s = alpha.order().subtract(ONE).divide(TWO); // s = (q-1)/2;
            result = result.pow(s); // (X - alpha)^s

            PolynomialGF onePolynomial = new PolynomialGF(alpha, alpha.getThread());
            onePolynomial.setPolynomial(oneElement, ZERO); // onePolynomial = "1";
            result.subtract(onePolynomial); // result = ((X - alpha)^s - 1)
            return result;
        } else {
            // return X*alpha + (X*alpha)^2^1 + ... + (X*alpha)^2^e
            PolynomialGF result = new PolynomialGF(alpha, alpha.getThread());

            for (int i = 0; alpha.irrPolynomial().degree().compareTo(valueOf(i)) >= 0; i++) {
                PolynomialGF currentSummand = new PolynomialGF(alpha, alpha.getThread());
                currentSummand.setPolynomial(alpha, ONE); // currentSummand = X*alpha;
                currentSummand.pow(alpha.characteristic().pow(i));  // currentSummand = (X*alpha)^characteristic^i
                result.add(currentSummand);
            }

            return  result;
        }
    }

    /**
     * For a given field F_q of order q returns the polynomial X^q - X with coefficients in F_q
     * @param f_q the field
     * @param thread which is the thread in which the application is running
     * @return X^q-X
     */
    private PolynomialGF splittingFieldPolynomialForField (GaloisField f_q, Thread thread) throws FFaplAlgebraicException {
        TreeMap<BigInteger, GaloisField> polynomialCoefficientsMap = new TreeMap<>();

        GaloisField oneElement = f_q.clone();
        oneElement.setValue(new Polynomial(ONE, ZERO, thread));
        GaloisField minusOneElement = oneElement.clone();
        minusOneElement = minusOneElement.negate();

        polynomialCoefficientsMap.put(f_q.order(), oneElement);      // X^q
        polynomialCoefficientsMap.put(ONE, minusOneElement); // -X

        return new PolynomialGF(f_q, polynomialCoefficientsMap, thread);
    }

    /**
     * Returns a random element of a given field
     * @param field the field from which the element is selected
     * @return an element in the field
     */
    private GaloisField randomElementOfField(GaloisField field) throws FFaplAlgebraicException {
        GaloisField result = field.clone();
        result.setValue(
                Algorithm.getRandomPolynomial(
                    (BInteger) field.irrPolynomial().degree(),
                    (BInteger) field.characteristic(),
                    false));
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
    private PolynomialGF gcd(PolynomialGF g, PolynomialGF h) throws FFaplAlgebraicException {
        PolynomialGF r;
        Thread thread = g.getThread();
        if(! g.field().equalGF(h.field())){
            Object[] messages ={g.field(), "PolynomialGF",
                    h.field(),"PolynomialGF"};
            throw new FFaplAlgebraicException(messages, IAlgebraicError.FIELDS_UNEQUAL);
        }

        while(! h.isZero()){
            isRunning(thread);
            r = PolynomialGF.divide(g, h)[1];
            g = h;
            h = r;
        }

        return g;
    }

    @Override
    public String toString() {
        return "RabinRootFindingStrategy";
    }
}
