package web.teavm.classlib.java.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TAlgorithmParameters {

    private String _algorithm;

    protected TAlgorithmParameters(String algorithm) {
        _algorithm = algorithm;
    }

    public static TAlgorithmParameters getInstance(String algorithm) throws NoSuchAlgorithmException {
        switch (algorithm) {
            case "AES":
            case "DES":
            case "DESede":
                return new TAlgorithmParameters(algorithm);
            default:
                throw new NoSuchAlgorithmException("Parameters for algorithm " + algorithm + " not supported");
        }
    }

    public final String getAlgorithm() {
        return _algorithm;
    }

    public final void init(byte[] params) throws IOException {
    }

    public final void init(byte[] params, String format) throws IOException {
    }

    public final byte getEncoded() throws IOException {
        throw new IOException("not intialized");
    }

    public final byte getEncoded(String format) throws IOException {
        throw new IOException("not intialized");
    }

}
