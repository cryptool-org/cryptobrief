package web.teavm.classlib.javax.crypto;

import java.security.GeneralSecurityException;

public class TIllegalBlockSizeException extends GeneralSecurityException {

    public TIllegalBlockSizeException() {
        super();
    }

    public TIllegalBlockSizeException(String message) {
        super(message);
    }
}
