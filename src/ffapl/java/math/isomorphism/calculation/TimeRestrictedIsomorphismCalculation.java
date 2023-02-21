package ffapl.java.math.isomorphism.calculation;

import ffapl.java.classes.GaloisField;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.math.isomorphism.Isomorphism;

import java.util.concurrent.*;

/**
 * This class uses an internal IsomorphismCalculation object to calculate an isomorphism between two isomorphic finite
 * fields but wraps this operation in such a way that it is aborted if no isomorphism has been found in a given amount
 * of time
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class TimeRestrictedIsomorphismCalculation implements IsomorphismCalculation{

    private final IsomorphismCalculation _isomorphismCalculation;
    private final long _maximumSecondsForCalculation;

    public TimeRestrictedIsomorphismCalculation(IsomorphismCalculation isomorphismCalculation, long maximumSecondsForCalculation) {
        _isomorphismCalculation = isomorphismCalculation;
        _maximumSecondsForCalculation = maximumSecondsForCalculation;
    }

    /**
     * This method calculates an isomorphism - if possible - between the source field and the target field and
     * then applies said isomorphism to the source field element, mapping it to an element of the target field.
     * This resulting element is then returned
     *
     * @param sourceField source field
     * @param targetField target field
     * @return transformed field element
     * @throws FFaplAlgebraicException
     *              <ISO_TIMEOUT> if no isomorphism is calculated in the given amount of time.
     *              <INTERNAL> if underlying operations fail
     */
    @Override
    public Isomorphism calculate(GaloisField sourceField, GaloisField targetField) throws FFaplAlgebraicException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Isomorphism> future =
                executorService.submit(() -> _isomorphismCalculation.calculate(sourceField, targetField));
        try {
            return future.get(_maximumSecondsForCalculation, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            throw new FFaplAlgebraicException(new Object[]{_maximumSecondsForCalculation}, IAlgebraicError.ISO_TIMEOUT);
        } catch (InterruptedException e) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.INTERRUPT);
        }
    }
}
