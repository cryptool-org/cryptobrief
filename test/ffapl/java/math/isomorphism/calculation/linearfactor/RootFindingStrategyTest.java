package ffapl.java.math.isomorphism.calculation.linearfactor;

import ffapl.java.classes.GaloisField;
import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RootFindingStrategyTest {
    private static class ProblemInstance {
        public final PolynomialRC polynomial;
        public final GaloisField field;

        public ProblemInstance(PolynomialRC polynomial, GaloisField field) {
            this.polynomial = polynomial;
            this.field = field;
        }
    }

    public static Collection<Object[]> fieldPairsAndIsomorphismCalculationStrategies() throws FFaplAlgebraicException {
        Thread thread = new Thread();
        List<ProblemInstance> problemInstances = Arrays.asList(
                new ProblemInstance(
                        new PolynomialRC(new Polynomial("x^3+x^2+1", thread).polynomial(), BigInteger.TWO, thread),
                        new GaloisField(2, new Polynomial("x^3+x+1", thread), thread)),
                new ProblemInstance(
                        new PolynomialRC(new Polynomial("x^2+1", thread).polynomial(), BigInteger.valueOf(3), thread),
                        new GaloisField(3, new Polynomial("x^2+x+2", thread), thread)));

        List<RootFindingStrategy> searchStrategies = Arrays.asList(
                new RandomRootFindingStrategy(),
                new RabinRootFindingStrategy(),
                new NewRabinRootFindingStrategy());

        List<Object[]> testCases = new ArrayList<>();
        for (RootFindingStrategy searchStrategy: searchStrategies) {
            for (ProblemInstance problemInstance : problemInstances) {
                testCases.add(
                        new Object[]{problemInstance, searchStrategy});
            }
        }

        return testCases;
    }

    @ParameterizedTest
    @MethodSource("fieldPairsAndIsomorphismCalculationStrategies")
    public void testLinearFactorSearchStrategy(ProblemInstance problemInstance, RootFindingStrategy searchStrategy) throws FFaplAlgebraicException {
        PolynomialRC root = searchStrategy.findRootOf(problemInstance.polynomial, problemInstance.field);

        PolynomialRC result = problemInstance.polynomial.calculate(root);
        result.mod(problemInstance.field.irrPolynomial());

        assertTrue(result.isZero());
    }
}
