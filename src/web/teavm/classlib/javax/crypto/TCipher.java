package web.teavm.classlib.javax.crypto;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.IvParameterSpec;

import org.teavm.jso.core.JSString;
import org.teavm.jso.core.JSError;
import org.teavm.jso.typedarrays.Uint8Array;

import web.teavm.jso.buffer.Buffer;
import web.teavm.jso.crypto.CryptoProvider;

public class TCipher {

    public static final int ENCRYPT_MODE = 1;
    public static final int DECRYPT_MODE = 2;
    public static final int WRAP_MODE = 3;
    public static final int UNWRAP_MODE = 4;

    public static final int PUBLIC_KEY = 1;
    public static final int PRIVATE_KEY = 2;
    public static final int SECRET_KEY = 3;

    private static final Map<String, String> SUPPORTED_ALGORITHMS;
    static {
        Map<String, String> map = new HashMap<String, String>(12);
        map.put("AES/ECB", "aes-128-ecb");
        map.put("AES/CBC", "aes-128-cbc");
        map.put("AES_128/ECB", "aes-128-ecb");
        map.put("AES_128/CBC", "aes-128-cbc");
        map.put("AES_192/ECB", "aes-192-ecb");
        map.put("AES_192/CBC", "aes-192-cbc");
        map.put("AES_256/ECB", "aes-256-ecb");
        map.put("AES_256/CBC", "aes-256-cbc");
        map.put("DES/ECB", "des-ecb");
        map.put("DES/CBC", "des-cbc");
        map.put("DESede/ECB", "des-ede");
        map.put("DESede/CBC", "des-ede-cbc");

        SUPPORTED_ALGORITHMS = Collections.unmodifiableMap(map);
    }

    private static SecureRandom _defaultRg = null;

    private final String _algorithm;
    private int _opmode;
    private byte[] _iv;
    private int _numBytesBuffered;

    private final Transform _transform;
    private CryptoProvider.CipherEngine _engine;

    protected TCipher(String transformation) throws NoSuchAlgorithmException, TNoSuchPaddingException {
        _algorithm = transformation;
        _transform = Transform.parseTransform(transformation);
    }

    private static class Transform {
        String algorithm;
        String mode;
        String padding;

        private Transform(String algorithm, String mode, String padding) throws NoSuchAlgorithmException, TNoSuchPaddingException {
            String algo = algorithm + "/" + mode;
            if (!SUPPORTED_ALGORITHMS.containsKey(algo)) {
                throw new NoSuchAlgorithmException(algo + " Cipher algorithm not available");
            }
            if (!padding.equals("PKCS5Padding") && !padding.equals("NoPadding")) {
                throw new TNoSuchPaddingException(padding + " Cipher padding not available");
            }

            this.algorithm = algorithm;
            this.mode = mode;
            this.padding = padding;
        }

        static Transform parseTransform(String transformation) throws NoSuchAlgorithmException, TNoSuchPaddingException {
            if (transformation == null || transformation.isEmpty()) {
                throw new IllegalArgumentException("missing transformation");
            }

            String[] parts = transformation.split("/");
            if (parts.length == 1) {
                return new Transform(parts[0], "ECB", "PKCS5Padding");
            } else if (parts.length == 2) {
                return new Transform(parts[0], parts[1], "PKCS5Padding");
            } else if (parts.length == 3) {
                return new Transform(parts[0], parts[1], parts[2]);
            } else {
                throw new NoSuchAlgorithmException("invalid Cipher transform: " + transformation);
            }
        }
    }

    public static TCipher getInstance(String transformation) throws NoSuchAlgorithmException, TNoSuchPaddingException {
        return new TCipher(transformation);
    }

    public final String getAlgorithm() {
        return _algorithm;
    }

    public final byte[] getIV() {
        if (_iv != null) {
            byte[] encoded = new byte[_iv.length];
            System.arraycopy(_iv, 0, encoded, 0, _iv.length);
            return encoded;
        }
        return null;
    }

    public final int getBlockSize() {
        String algo = _transform.algorithm.toUpperCase();
        if (algo.equals("AES") || algo.startsWith("AES_")) {
            return 16;
        } else if (algo.equals("DES") || algo.equals("DESEDE")) {
            return 8;
        }
        return 0;
    }

    public final int getOutputSize(int inputLen) {
        int bs = getBlockSize();
        int numBytes = _numBytesBuffered + inputLen;

        int numBlocks = (numBytes + bs - 1) / bs;
        return numBlocks * bs;
    }

    private static SecureRandom getDefaultSecureRandom() {
        if (_defaultRg == null) {
            _defaultRg = new SecureRandom();
        }
        return _defaultRg;
    }

    private void verifyMode(int opmode) {
        if (opmode == WRAP_MODE || opmode == UNWRAP_MODE) {
            throw new UnsupportedOperationException("wrapping/unwrapping not supported");
        }
        if (opmode < ENCRYPT_MODE || opmode > UNWRAP_MODE) {
            throw new InvalidParameterException("invalid mode");
        }
        _opmode = opmode;
    }

    private byte[] verifyKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new IllegalArgumentException("missing key");
        }

        String keyAlgo = key.getAlgorithm();
        if (keyAlgo.equalsIgnoreCase("AES")) {
            keyAlgo = "AES_128";
        } else if (keyAlgo.equalsIgnoreCase("TripleDES")) {
            keyAlgo = "DESede";
        }

        if (!(keyAlgo.equalsIgnoreCase(_transform.algorithm) || (keyAlgo.equalsIgnoreCase("AES_128") && _transform.algorithm.equalsIgnoreCase("AES")))) {
            throw new InvalidKeyException("Key algorithm " + keyAlgo + " does not match Cipher " + _algorithm);
        }

        byte[] keyData = key.getEncoded();
        if (keyData == null) {
            throw new InvalidKeyException("Key does not support encoding");
        }

        if (keyAlgo.equalsIgnoreCase("AES_128") && keyData.length != 16) {
            throw new InvalidKeyException("Unsupported Key length " + keyData.length + ". Expected " + 16);
        } else if (keyAlgo.equalsIgnoreCase("AES_192") && keyData.length != 24) {
            throw new InvalidKeyException("Unsupported Key length " + keyData.length + ". Expected " + 24);
        } else if (keyAlgo.equalsIgnoreCase("AES_256") && keyData.length != 32) {
            throw new InvalidKeyException("Unsupported Key length " + keyData.length + ". Expected " + 32);
        } else if (keyAlgo.equalsIgnoreCase("DES") && keyData.length != 8) {
            throw new InvalidKeyException("Unsupported Key length " + keyData.length + ". Expected " + 8);
        } else if (keyAlgo.equalsIgnoreCase("DESede") && keyData.length != 24) {
            throw new InvalidKeyException("Unsupported Key length " + keyData.length + ". Expected " + 24);
        }

        return keyData;
    }

    public final void init(int opmode, Key key) throws InvalidKeyException, InvalidAlgorithmParameterException {
        SecureRandom rg = getDefaultSecureRandom();
        init(opmode, key, rg);
    }

    public final void init(int opmode, Key key, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        IvParameterSpec iv = null;
        if (!_transform.mode.equalsIgnoreCase("ECB")) {
            byte[] ivData = new byte[getBlockSize()];
            random.nextBytes(ivData);

            iv = new IvParameterSpec(ivData);
        }
        init(opmode, key, iv, random);
    }

    public final void init(int opmode, Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
        SecureRandom rg = getDefaultSecureRandom();
        init(opmode, key, params, rg);
    }

    public final void init(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        verifyMode(opmode);

        byte[] keyData = verifyKey(key);
        Uint8Array jsKey = new Uint8Array(keyData.length);
        for (int i = 0; i < keyData.length; ++i) {
            jsKey.set(i, keyData[i]);
        }

        String algo = _transform.algorithm + "/" + _transform.mode;
        JSString jsAlgorithm = JSString.valueOf(SUPPORTED_ALGORITHMS.get(algo));

        if (params != null && !(params instanceof IvParameterSpec)) {
            throw new InvalidAlgorithmParameterException("only supports IV parameters");
        }
        IvParameterSpec ivParams = (IvParameterSpec)params;

        Uint8Array jsIv = new Uint8Array(0);
        if (params != null) {
            byte[] ivData = ivParams.getIV();
            if (ivData.length != getBlockSize()) {
                throw new InvalidParameterException("IV has wrong length " + ivData.length + ". Expected " + getBlockSize());
            }
            _iv = ivData;

            jsIv = new Uint8Array(ivData.length);
            for (int i = 0; i < ivData.length; ++i) {
                jsIv.set(i, ivData[i]);
            }
        }

        if (!CryptoProvider.isSupported()) {
            throw new RuntimeException("CryptoProvider is not supported. Did you forget to provide 'cryptoProvider' in the global context?");
        }

        if (opmode == ENCRYPT_MODE) {
            _engine = CryptoProvider.current().createCipheriv(jsAlgorithm, jsKey, jsIv);
        } else if (opmode == DECRYPT_MODE) {
            _engine = CryptoProvider.current().createDecipheriv(jsAlgorithm, jsKey, jsIv);
        }

        _numBytesBuffered = 0;
        if (_transform.padding.equalsIgnoreCase("PKCS5Padding")) {
            _engine.setAutoPadding(true);
        } else {
            _engine.setAutoPadding(false);
        }
    }

    //public final void init(int opmode, Key key, AlgorithmParameters params) throws InvalidKeyException, InvalidAlgorithmParameterException {
    //    SecureRandom rg = getDefaultSecureRandom();
    //    init(opmode, key, params, rg);
    //}

    //public final void init(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
    //    verifyMode(opmode);
    //}

    public final byte[] doFinal() throws TIllegalBlockSizeException, TBadPaddingException {
        byte[] buf;

        try {
            Buffer outputBuffer = _engine.final0();
            int outputSize = outputBuffer.getLength();
            _numBytesBuffered = Math.max(_numBytesBuffered - outputSize, 0);

            buf = new byte[outputSize];
            for (int idx = 0; idx < buf.length; ++idx) {
                buf[idx] = outputBuffer.get(idx);
            }

        } catch (RuntimeException err) {
            if (_opmode == ENCRYPT_MODE) {
                throw new TIllegalBlockSizeException(err.toString());
            } else if (_opmode == DECRYPT_MODE) {
                throw new TBadPaddingException(err.toString());
            } else {
                throw err;
            }
        }

        return buf;
    }

    public final byte[] doFinal(byte[] input) throws TIllegalBlockSizeException, TBadPaddingException {
        return doFinal(input, 0, input.length);
    }

    public final byte[] doFinal(byte[] input, int offset, int len) throws TIllegalBlockSizeException, TBadPaddingException {
        byte[] blocks = update(input, offset, len);
        byte[] lastBlock = doFinal();

        byte[] buf = new byte[blocks.length + lastBlock.length];
        System.arraycopy(blocks, 0, buf, 0, blocks.length);
        System.arraycopy(lastBlock, 0, buf, blocks.length, lastBlock.length);

        return buf;
    }

    public final int doFinal(byte[] output, int outputOffset) throws TIllegalBlockSizeException, TBadPaddingException, TShortBufferException {
        int bufferSize = output.length - outputOffset;
        if (bufferSize < getOutputSize(0)) {
            throw new TShortBufferException("buffer too small for offset and blocksize");
        }

        byte[] buffer = doFinal();
        assert bufferSize >= buffer.length : "Got more data than anticipated";
        System.arraycopy(buffer, 0, output, outputOffset, buffer.length);

        return buffer.length;
    }

    public final int doFinal(byte[] input, int offset, int len, byte[] output) throws TIllegalBlockSizeException, TBadPaddingException, TShortBufferException {
        return doFinal(input, offset, len, output, 0);
    }

    public final int doFinal(byte[] input, int offset, int len, byte[] output, int outputOffset) throws TIllegalBlockSizeException, TBadPaddingException, TShortBufferException {
        int bufferSize = output.length - outputOffset;
        if (bufferSize < getOutputSize(input.length)) {
            throw new TShortBufferException("buffer too small for offset and blocksize");
        }

        byte[] buffer = doFinal(input, offset, len);
        assert bufferSize >= buffer.length : "Got more data than anticipated";
        System.arraycopy(buffer, 0, output, outputOffset, buffer.length);

        return buffer.length;
    }

    public final byte[] update(byte[] input, int offset, int len) {
        if (offset + len > input.length) {
            throw new IllegalArgumentException("buffer too small for offset and length");
        }
        Buffer buffer = Buffer.alloc(len);
        for (int idx = 0; idx < len; ++idx) {
            buffer.set(idx, input[offset + idx]);
        }

        Buffer outputBuffer = _engine.update(buffer);
        int outputSize = outputBuffer.getLength();
        _numBytesBuffered = input.length - outputSize;

        byte[] buf = new byte[outputSize];
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = outputBuffer.get(idx);
        }

        return buf;
    }

    public final byte[] update(byte[] input) {
        return update(input, 0, input.length);
    }
}
