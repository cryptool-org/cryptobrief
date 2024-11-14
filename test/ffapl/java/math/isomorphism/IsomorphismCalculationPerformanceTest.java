package ffapl.java.math.isomorphism;

import ffapl.java.classes.BInteger;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.math.Algorithm;
import ffapl.java.math.isomorphism.calculation.IsomorphismCalculation;
import ffapl.java.math.isomorphism.calculation.LinearFactorIsomorphismCalculation;
import ffapl.java.math.isomorphism.calculation.cache.GaloisFieldSpecification;
import ffapl.java.math.isomorphism.calculation.linearfactor.RootFindingStrategyType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;


public class IsomorphismCalculationPerformanceTest {

    private static class GaloisFieldPair {
        public final GaloisField sourceField;
        public final GaloisField targetField;

        public GaloisFieldPair(GaloisField sourceField, GaloisField targetField) {
            this.sourceField = sourceField;
            this.targetField = targetField;
        }

        private static PolynomialRC irreduciblePolynomialFor(int characteristic, int degree, Thread thread) throws FFaplAlgebraicException {
            BInteger p = new BInteger(BigInteger.valueOf(characteristic), thread);
            BInteger n = new BInteger(BigInteger.valueOf(degree), thread);

            PolynomialRC currentPolynomial;
            do {
                currentPolynomial = Algorithm.getRandomPolynomial(n, p, true);
            } while (!Algorithm.isIrreducible(currentPolynomial));

            return currentPolynomial;
        }

        private static GaloisFieldPair with(int characteristic, int degree, Thread thread) throws FFaplAlgebraicException {
            PolynomialRC firstIrreduciblePolynomial = irreduciblePolynomialFor(characteristic, degree, thread);
            PolynomialRC secondIrreduciblePolynomial = firstIrreduciblePolynomial;

            int amountOfTries = 0;
            while (secondIrreduciblePolynomial.equals(firstIrreduciblePolynomial)) {
                if (amountOfTries > 100) {
                    throw new IllegalStateException(
                            "Too many tries for finding an irreducible polynomial which is different from the first one. " +
                                    "First: " + firstIrreduciblePolynomial + ", Second:" + secondIrreduciblePolynomial);
                }
                secondIrreduciblePolynomial = irreduciblePolynomialFor(characteristic, degree, thread);
                amountOfTries++;
            }

            return new GaloisFieldPair(
                    new GaloisField(BigInteger.valueOf(characteristic), firstIrreduciblePolynomial, thread),
                    new GaloisField(BigInteger.valueOf(characteristic), secondIrreduciblePolynomial, thread));
        }
    }

    private static class TestCase {
        private final long testCaseNumber;
        private final GaloisFieldPair fieldPair;
        private final IsomorphismCalculation isomorphismCalculationStrategy;

        public TestCase(long testCaseNumber, GaloisFieldPair fieldPair, IsomorphismCalculation isomorphismCalculationStrategy) {
            this.testCaseNumber = testCaseNumber;
            this.fieldPair = fieldPair;
            this.isomorphismCalculationStrategy = isomorphismCalculationStrategy;
        }
    }

    private static class TestCaseResult {
        private final TestCase testCase;
        private final long durationInMilliseconds;

        public TestCaseResult(TestCase testCase, long durationInMilliseconds) {
            this.testCase = testCase;
            this.durationInMilliseconds = durationInMilliseconds;
        }

        @Override
        public String toString() {
            return String.format("%d;%d;%d;%s;%s;%s;%d",
                    testCase.testCaseNumber,
                    testCase.fieldPair.sourceField.characteristic(),
                    testCase.fieldPair.sourceField.irrPolynomial().degree(),
                    new GaloisFieldSpecification(testCase.fieldPair.sourceField),
                    new GaloisFieldSpecification(testCase.fieldPair.targetField),
                    testCase.isomorphismCalculationStrategy,
                    durationInMilliseconds);
        }
    }

    private IsomorphismVerification verification;

    @BeforeEach
    public void initialize() {
        verification = new IsomorphismVerification();
    }

    public static TestCase[] generateTestCases() throws FFaplAlgebraicException {
        Thread thread = new Thread();
        int amountOfRunsPerOrderAndStrategy = 1;
        int[] characteristics = {3,5};
        int[] powers = {2,3};
        IsomorphismCalculation[] isomorphismCalculationStrategies = new IsomorphismCalculation[] {
                new LinearFactorIsomorphismCalculation(RootFindingStrategyType.RANDOM.strategy()),
                new LinearFactorIsomorphismCalculation(RootFindingStrategyType.RABIN.strategy()),
                new LinearFactorIsomorphismCalculation(RootFindingStrategyType.RABIN_NEW.strategy())};

        TestCase[] testCaseArray = new TestCase[amountOfRunsPerOrderAndStrategy
                * characteristics.length * powers.length * isomorphismCalculationStrategies.length];

        int currentTestCaseNumber = 0;
        for (int characteristic : characteristics) {
            for (int power : powers) {
                for (IsomorphismCalculation calculationStrategy : isomorphismCalculationStrategies) {
                    for (int i = 0; i < amountOfRunsPerOrderAndStrategy; i++) {
                        testCaseArray[currentTestCaseNumber] = new TestCase(
                                currentTestCaseNumber,
                                GaloisFieldPair.with(characteristic, power, thread),
                                calculationStrategy);
                        currentTestCaseNumber++;
                    }
                }
            }
        }

        return testCaseArray;
    }

    @Test
    public void testIsomorphismCalculation() throws FFaplAlgebraicException {
        var testCases = generateTestCases();

        for (var testCase : testCases) {
            long start = System.currentTimeMillis();
            var iso = testCase.isomorphismCalculationStrategy.calculate(testCase.fieldPair.sourceField, testCase.fieldPair.targetField);
            long end = System.currentTimeMillis();
            long elapsedMillis = end - start;
            if (!verification.verifyIsomorphism(testCase.fieldPair.sourceField, testCase.fieldPair.targetField, iso)) {
                throw new RuntimeException(
                        String.format("There seems to be a bug in isomorphism calculation strategy '%s' since the " +
                                        "result '%s' it produced for the fields '%s' and '%s' is not an isomorphism",
                                testCase.isomorphismCalculationStrategy, iso, testCase.fieldPair.sourceField,
                                testCase.fieldPair.targetField));
            }

            TestCaseResult result = new TestCaseResult(testCase, elapsedMillis);

            System.out.println(result);
        }
    }
}
