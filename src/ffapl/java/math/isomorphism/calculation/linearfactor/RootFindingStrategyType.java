package ffapl.java.math.isomorphism.calculation.linearfactor;

import java.util.NoSuchElementException;

public enum RootFindingStrategyType {
    RANDOM,
    RABIN,
    RABIN_NEW;

    private static final RabinRootFindingStrategy RABIN_STRATEGY = new RabinRootFindingStrategy();
    private static final NewRabinRootFindingStrategy RABIN_NEW_STRATEGY = new NewRabinRootFindingStrategy();
    private static final RandomRootFindingStrategy RANDOM_STRATEGY = new RandomRootFindingStrategy();
    private static final String RANDOM_STRING = "Random root-finding strategy";
    private static final String RABIN_STRING = "Rabin's root-finding algorithm";
    private static final String RABIN_NEW_STRING = "Rabin's root-finding algorithm, improved";

    public RootFindingStrategy strategy()  {
        switch (this) {

            case RABIN: return RABIN_STRATEGY;
            case RABIN_NEW: return RABIN_NEW_STRATEGY;
            case RANDOM: return RANDOM_STRATEGY;
            default:
                throw new NoSuchElementException(
                        String.format("There is no root finding strategy registered for type '%s'", this.name()));
        }
    }

    public String stringRepresentation() {
        switch(this) {
            case RANDOM:
                return RANDOM_STRING;
            case RABIN:
                return RABIN_STRING;
            case RABIN_NEW:
                return RABIN_NEW_STRING;
            default:
                throw new NoSuchElementException(
                        String.format("There is no string representation defined for type '%s'", this.name()));
        }
    }

    public static RootFindingStrategyType fromStringRepresentation(String stringRepresentation) {
        if (RANDOM_STRING.equals(stringRepresentation)) {
            return RANDOM;
        }
        if (RABIN_STRING.equals(stringRepresentation)) {
            return RABIN;
        }
        if (RABIN_NEW_STRING.equals(stringRepresentation)) {
            return RABIN_NEW;
        }

        throw new NoSuchElementException(
                String.format("There is no enum value defined for string representation '%s'", stringRepresentation));
    }

    public static String[] stringRepresentations() {
        return new String[] {RANDOM_STRING, RABIN_STRING, RABIN_NEW_STRING};
    }

    public static RootFindingStrategyType defaultElement() {
        return RANDOM;
    }
}
