package web.teavm.classlib.java.security;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.core.JSString;

import web.teavm.jso.buffer.Buffer;
import web.teavm.jso.crypto.CryptoProvider;

public class TMessageDigest {

    private static final Map<String, String> SUPPORTED_ALGORITHMS;
    static {
        Map<String, String> map = new HashMap<String, String>(5);
        map.put("MD5", "md5");
        map.put("SHA-1", "sha1");
        map.put("SHA-224", "sha224");
        map.put("SHA-256", "sha256");
        map.put("SHA-384", "sha384");
        map.put("SHA-512", "sha512");

        SUPPORTED_ALGORITHMS = Collections.unmodifiableMap(map);
    }

    private final String _algorithm;
    private final CryptoProvider.Hash _engine;

    protected TMessageDigest(String algorithm) {
        _algorithm = algorithm;

        String jsAlgorithm = SUPPORTED_ALGORITHMS.get(algorithm);
        if (!CryptoProvider.isSupported()) {
            throw new RuntimeException("CryptoProvider is not supported. Did you forget to provide 'cryptoProvider' in the global context?");
        }
        _engine = CryptoProvider.current().createHash(JSString.valueOf(jsAlgorithm));
    }

    public static TMessageDigest getInstance(String algorithm) throws NoSuchAlgorithmException {
        if (algorithm == null || algorithm.isEmpty()) {
            throw new IllegalArgumentException("missing algorithm");
        }
        if (!SUPPORTED_ALGORITHMS.containsKey(algorithm)) {
            throw new NoSuchAlgorithmException(algorithm + " MessageDigest not available");
        }

        TMessageDigest md = new TMessageDigest(algorithm);
        return md;
    }

    public final String getAlgorithm() {
        return _algorithm;
    }

    public void update(byte[] input, int offset, int len) {
        if (offset + len > input.length) {
            throw new IllegalArgumentException("buffer too small for offset and length");
        }
        Buffer buffer = Buffer.alloc(len);
        for (int idx = 0; idx < len; ++idx) {
            buffer.set(idx, input[offset + idx]);
        }

        _engine.update(buffer);
    }

    public void update(byte input) {
        update(new byte[] { input }, 0, 1);
    }

    public void update(byte[] input) {
        update(input, 0, input.length);
    }

    public byte[] digest() {
        Buffer digest = _engine.digest();

        byte[] buf = new byte[digest.getLength()];
        for (int idx = 0; idx < digest.getLength(); ++idx) {
            buf[idx] = digest.get(idx);
        }

        return buf;
    }

    public byte[] digest(byte[] input) {
        update(input);
        return digest();
    }

    public int digest(byte[] buffer, int offset, int len) {
        if (buffer == null) {
            throw new IllegalArgumentException("missing buffer");
        }
        if (offset + len > buffer.length) {
            throw new IllegalArgumentException("buffer too small for offset and length");
        }

        byte[] digestBytes = digest();
        int bytesWritten = Math.min(digestBytes.length, len);
        System.arraycopy(digestBytes, 0, buffer, offset, bytesWritten);

        return bytesWritten;
    }

    public static boolean isEqual(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return false;
        }

        int result = 0;
        result |= a.length - b.length; // == 0, if both are the same length

        for (int i = 0; i < a.length; i++) {
            int ib = i * (((i - b.length) >>> 31) & 1); // i if < b.length, else 0
            result |= a[i] ^ b[ib]; // == 0, if both entries are identical
        }

        return result == 0; // still 0 if same length and all entries identical
    }
}
