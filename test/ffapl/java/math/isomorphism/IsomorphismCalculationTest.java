package ffapl.java.math.isomorphism;

import ffapl.java.classes.GaloisField;
import ffapl.java.classes.Polynomial;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.math.isomorphism.calculation.IsomorphismCalculation;
import ffapl.java.math.isomorphism.calculation.LinearFactorIsomorphismCalculation;
import ffapl.java.math.isomorphism.calculation.linearfactor.RabinRootFindingStrategy;
import ffapl.java.math.isomorphism.calculation.linearfactor.RandomRootFindingStrategy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IsomorphismCalculationTest {
    private static class GaloisFieldPair{
        public final GaloisField sourceField;
        public final GaloisField targetField;

        public GaloisFieldPair(GaloisField sourceField, GaloisField targetField) {
            this.sourceField = sourceField;
            this.targetField = targetField;
        }
    }

    private IsomorphismVerification verification;

    @BeforeEach
    public void initialize() {
        verification = new IsomorphismVerification();
    }

    public static Collection<Object[]> fieldPairsAndIsomorphismCalculationStrategies() throws FFaplAlgebraicException {
        Thread thread = new Thread();
        List<GaloisFieldPair> fieldPairs = Arrays.asList(
                new GaloisFieldPair(
                        new GaloisField(2, new Polynomial("x^3+x^2+1", thread), thread),
                        new GaloisField(2, new Polynomial("x^3+x+1", thread), thread)),
                new GaloisFieldPair(
                        new GaloisField(3, new Polynomial("x^2+1", thread), thread),
                        new GaloisField(3, new Polynomial("x^2+x+2", thread), thread)));

        List<IsomorphismCalculation> isomorphismCalculationList = Arrays.asList(
                new LinearFactorIsomorphismCalculation(new RandomRootFindingStrategy()),
                new LinearFactorIsomorphismCalculation(new RabinRootFindingStrategy()));

        List<Object[]> testCases = new ArrayList<>();
        for (IsomorphismCalculation isomorphismCalculation: isomorphismCalculationList) {
            for (GaloisFieldPair fieldPair : fieldPairs) {
                testCases.add(
                        new Object[]{fieldPair, isomorphismCalculation});
            }
        }

        return testCases;
    }

    @ParameterizedTest
    @MethodSource("fieldPairsAndIsomorphismCalculationStrategies")
    public void testIsomorphismCalculation(GaloisFieldPair fieldPair, IsomorphismCalculation isomorphismCalculation) throws FFaplAlgebraicException {
        Isomorphism isomorphism = isomorphismCalculation.calculate(fieldPair.sourceField, fieldPair.targetField);
        assertTrue(verification.verifyIsomorphism(fieldPair.sourceField, fieldPair.targetField, isomorphism));
    }
}
