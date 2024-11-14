package web.teavm.classlib.javax.security.auth;

public interface TDestroyable {
    default void destroy() throws TDestroyFailedException {
        throw new TDestroyFailedException();
    }

    default boolean isDestroyed() {
        return false;
    }
}
