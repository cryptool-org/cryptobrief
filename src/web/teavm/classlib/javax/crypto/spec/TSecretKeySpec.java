package web.teavm.classlib.javax.crypto.spec;

import java.security.MessageDigest;
import java.util.Arrays;

import web.teavm.classlib.java.security.spec.TKeySpec;
import web.teavm.classlib.javax.crypto.TSecretKey;

public class TSecretKeySpec implements TKeySpec, TSecretKey {

    private final byte[] _key;
    private final String _algorithm;

    public TSecretKeySpec(byte[] key, String algorithm) throws IllegalArgumentException {
        this(key, 0, key == null ? 0 : key.length, algorithm);
    }

    public TSecretKeySpec(byte[] key, int offset, int len, String algorithm) throws IllegalArgumentException {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("missing/empty key");
        }
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("offset is negative");
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("length is negative");
        }
        if (offset + len > key.length) {
            throw new IllegalArgumentException("key too small for offset and length");
        }

        if (algorithm == null) {
            throw new IllegalArgumentException("missing algorithm");
        }

        _key = new byte[len];
        System.arraycopy(key, offset, _key, 0, len);
        _algorithm = algorithm;
    }

    public String getAlgorithm() {
        return _algorithm;
    }

    public String getFormat() {
        return "RAW";
    }

    public byte[] getEncoded() {
        byte[] encoded = new byte[_key.length];
        System.arraycopy(_key, 0, encoded, 0, _key.length);
        return encoded;
    }

    public int hashCode() {
        int hashcode = 0;
        for (int i = 0; i < _key.length; ++i) {
            hashcode += _key[i] * i;
        }

        if (_algorithm.equalsIgnoreCase("TripleDES")) {
            return hashcode ^ "desede".hashCode();
        } else {
            return hashcode ^ _algorithm.toLowerCase().hashCode();
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TSecretKeySpec)) {
            return false;
        }

        TSecretKeySpec otherSpec = (TSecretKeySpec)other;

        String otherAlgo = otherSpec.getAlgorithm();
        if (!otherAlgo.equalsIgnoreCase(_algorithm) && !(
                (otherAlgo.equalsIgnoreCase("TripleDES") && _algorithm.equalsIgnoreCase("DESede"))
                || (otherAlgo.equalsIgnoreCase("DESede") && _algorithm.equalsIgnoreCase("TripleDES"))
        )) {
            return false;
        }

        byte[] otherKey = otherSpec.getEncoded();
        try {
            return MessageDigest.isEqual(_key, otherKey);
        } finally {
            Arrays.fill(otherKey, (byte)0);
        }
    }
}
