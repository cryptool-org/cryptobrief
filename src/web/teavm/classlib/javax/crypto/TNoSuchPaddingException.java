package web.teavm.classlib.javax.crypto;

import java.security.GeneralSecurityException;

public class TNoSuchPaddingException extends GeneralSecurityException {

    public TNoSuchPaddingException() {
        super();
    }

    public TNoSuchPaddingException(String message) {
        super(message);
    }

    public TNoSuchPaddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TNoSuchPaddingException(Throwable cause) {
        super(cause);
    }
}
