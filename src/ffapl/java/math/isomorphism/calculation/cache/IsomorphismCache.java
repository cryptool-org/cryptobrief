package ffapl.java.math.isomorphism.calculation.cache;

import ffapl.java.classes.GaloisField;
import ffapl.java.math.isomorphism.Isomorphism;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores isomorphisms between finite field and manages access to those isomorphisms.
 * It provides methods for adding isomorphisms, retrieving isomorphisms and for checking whether an isomorphism
 * between two fields is stored.
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class IsomorphismCache {
    private final Map<IsomorphismCacheKey, Isomorphism> _cache;

    private IsomorphismCache(Map<IsomorphismCacheKey, Isomorphism> _cache) {
        this._cache = _cache;
    }

    public IsomorphismCache() {
        this(new HashMap<>());
    }

    public boolean containsIsomorphismBetween(GaloisField source, GaloisField target) {
        return _cache.containsKey(
                new IsomorphismCacheKey(source, target));
    }

    public Isomorphism getIsomorphismBetween(GaloisField source, GaloisField target) {
        return _cache.get(
                new IsomorphismCacheKey(source, target));
    }

    public void putIsomorphismBetween(GaloisField source, GaloisField target, Isomorphism isomorphism) {
        _cache.put(new IsomorphismCacheKey(source, target), isomorphism);
    }
}
