package ffapl.java.math.isomorphism.calculation.cache;

import ffapl.java.classes.GaloisField;

/**
 * This class is used as a key for the Map used in the IsomorphismCache. Therefore the methods "equals" and "hashCode"
 * have been overwritten. It consists of two GaloisFieldSpecification objects: One for the domain finite field,
 * one for the image finite field of the stored isomorphism.
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
class IsomorphismCacheKey {

        private final GaloisFieldSpecification from;
        private final GaloisFieldSpecification to;

        public IsomorphismCacheKey(GaloisFieldSpecification from, GaloisFieldSpecification to) {
            this.from = from;
            this.to = to;
        }

        public IsomorphismCacheKey(GaloisField from, GaloisField to) {
            this(new GaloisFieldSpecification(from), new GaloisFieldSpecification(to));
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof IsomorphismCacheKey)) {
                return false;
            }

            if (!((IsomorphismCacheKey)other).from.equals(this.from)) {
                return false;
            }

            if (!((IsomorphismCacheKey) other).to.equals(this.to)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (from == null ? 0 : from.hashCode());
            hash = 31 * hash + (to == null ? 0 : to.hashCode());
            return hash;
        }
}
