package web.teavm.classlib.javax.crypto;

import java.security.GeneralSecurityException;

public class TBadPaddingException extends GeneralSecurityException {

    public TBadPaddingException() {
        super();
    }

    public TBadPaddingException(String message) {
        super(message);
    }
}
