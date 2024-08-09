package ffapl.java.math.isomorphism;

import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;

import java.math.BigInteger;
import java.util.TreeMap;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class IsomorphismVerification {

    public boolean verifyIsomorphism(
            GaloisField sourceField,
            GaloisField targetField,
            Isomorphism allegedIsomorphism) throws FFaplAlgebraicException {
        if (!sourceField.isIsomorphicTo(targetField)) {
            return false;
        }

        // Check phi(0) = 0
        GaloisField sourceFieldZeroElement = sourceField.clone();
        sourceFieldZeroElement.setValue(new PolynomialRC(sourceField.characteristic(), sourceField.getThread()));
        if (!allegedIsomorphism.applyTo(sourceFieldZeroElement).getValue().isZero()) {
            return false;
        }

        // Check phi(1) = 1
        GaloisField sourceFieldOneElement = sourceField.clone();
        sourceFieldOneElement.setValue(new PolynomialRC(ONE, ZERO, sourceField.characteristic(), sourceField.getThread()));
        if (!allegedIsomorphism.applyTo(sourceFieldOneElement).getValue().isOne()) {
            return false;
        }

        // Test phi(a + b) = phi(a) + phi(b) and phi(a * b) for all a,b of the source field
        GaloisField firstElement = sourceField.clone();
        firstElement.setValue(new PolynomialRC(sourceField.characteristic(), sourceField.getThread()));

        boolean finishedOutside;
        do {
            finishedOutside = isLastElement(firstElement);

            boolean finishedInside;
            GaloisField secondElement = firstElement.clone();
            do {
                finishedInside = isLastElement(secondElement);

                GaloisField firstAdditionResult = targetField.clone();
                GaloisField secondAdditionResult = targetField.clone();
                // phi(a + b);
                firstAdditionResult.setValue(
                        allegedIsomorphism.applyTo(firstElement.addR(secondElement)).getValue());
                // phi(a) + phi(b);
                secondAdditionResult.setValue(
                        allegedIsomorphism.applyTo(firstElement).getValue()
                                .addR(allegedIsomorphism.applyTo(secondElement).getValue()));

                if (!firstAdditionResult.equals(secondAdditionResult)) {
                    return false;
                }

                GaloisField firstMultiplicationResult = targetField.clone();
                GaloisField secondMultiplicationResult = targetField.clone();

                // phi(a * b);
                firstMultiplicationResult.setValue(
                        allegedIsomorphism.applyTo(firstElement.multR(secondElement)).getValue());
                // phi(a) * phi(b);
                secondMultiplicationResult.setValue(
                        allegedIsomorphism.applyTo(firstElement).getValue()
                                .multR(allegedIsomorphism.applyTo(secondElement).getValue()));

                if (!firstMultiplicationResult.equals(secondMultiplicationResult)) {
                    return false;
                }

                secondElement = getSuccessorOf(secondElement);
            } while (!finishedInside);

            firstElement = getSuccessorOf(firstElement);
        } while (!finishedOutside);

        return true;
    }

    private GaloisField getSuccessorOf(GaloisField element) throws FFaplAlgebraicException {
        // basically add +1 in a base-p-addition with k digits where p is the fields' characteristic and k is the degree of the irreducible polynomial
        Prime characteristic = (Prime) element.characteristic();
        BigInteger irreduciblePolynomialDegree = element.irrPolynomial().degree();

        TreeMap<BigInteger, BigInteger> newElementPolynomialMap = new TreeMap<>();
        boolean incrementingDone = false;
        for (BigInteger currentPower = ZERO;
             currentPower.compareTo(irreduciblePolynomialDegree) < 0;
             currentPower = currentPower.add(ONE)) {

            BigInteger currentCoefficient = element.value().coefficientAt(currentPower);
            if (!incrementingDone) {
                currentCoefficient = currentCoefficient
                        .add(ONE)
                        .mod(characteristic);
            }

            if (!currentCoefficient.equals(ZERO)) { //Incrementing is only done after there is no carryover left to handle
                incrementingDone = true;
            }

            newElementPolynomialMap.put(currentPower, currentCoefficient);
        }

        GaloisField newElement = element.clone();
        newElement.setValue(
                new PolynomialRC(newElementPolynomialMap, characteristic, element.getThread()));
        return newElement;
    }

    private boolean isLastElement(GaloisField element) {
        BigInteger highestPossibleCoefficient = element.characteristic().subtract(ONE);
        BigInteger highestPossiblePower = element.irrPolynomial().degree().subtract(ONE);

        for(BigInteger currentPower = ZERO;
            currentPower.compareTo(highestPossiblePower) <= 0;
            currentPower = currentPower.add(ONE)) {

            if(!element.value().coefficientAt(currentPower).equals(highestPossibleCoefficient)) {
                return false;
            }
        }

        return true;
    }

}
