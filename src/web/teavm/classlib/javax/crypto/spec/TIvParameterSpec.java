package web.teavm.classlib.javax.crypto.spec;

import web.teavm.classlib.java.security.spec.TAlgorithmParameterSpec;

public class TIvParameterSpec implements TAlgorithmParameterSpec {

    private final byte[] _iv;

    public TIvParameterSpec(byte[] iv) {
        this(iv, 0, iv == null ? 0 : iv.length);
    }

    public TIvParameterSpec(byte[] iv, int offset, int len) {
        if (iv == null) {
            throw new IllegalArgumentException("missing iv");
        }
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("offset is negative");
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("length is negative");
        }
        if (offset + len > iv.length) {
            throw new IllegalArgumentException("iv too small for offset and length");
        }

        _iv = new byte[len];
        System.arraycopy(iv, offset, _iv, 0, len);
    }

    public byte[] getIV() {
        byte[] encoded = new byte[_iv.length];
        System.arraycopy(_iv, 0, encoded, 0, _iv.length);
        return encoded;
    }
}
