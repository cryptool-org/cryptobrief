package web.teavm.jso.crypto;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSBodyImport;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

import web.teavm.jso.buffer.Buffer;

public abstract class CryptoProvider implements JSObject {

    private CryptoProvider() {}

    public abstract class Hash implements JSObject {
        private Hash() {}

        public abstract void update(Uint8Array data);

        public abstract Buffer update(Buffer data);

        public abstract Buffer digest();
    }

    public abstract interface CipherEngine extends JSObject {
        abstract Buffer update(Uint8Array data);

        abstract Buffer update(Buffer data);

        @JSMethod("final")
        abstract Buffer final0();

        abstract CipherEngine setAutoPadding(boolean autoPadding);
    }

    public abstract class Cipher implements CipherEngine {
        private Cipher() {}
    }

    public abstract class Decipher implements CipherEngine {
        private Decipher() {}
    }

    @JSBody(script = "return cryptoProvider != null;", imports = @JSBodyImport(alias = "cryptoProvider", fromModule = "cryptoProvider.mjs"))
    public static native boolean isSupported();

    @JSBody(script = "return cryptoProvider;", imports = @JSBodyImport(alias = "cryptoProvider", fromModule = "cryptoProvider.mjs"))
    public static native CryptoProvider current();

    public abstract Hash createHash(JSString algorithm);

    public abstract Cipher createCipheriv(JSString algorithm, JSObject key, JSObject iv);

    public abstract Decipher createDecipheriv(JSString algorithm, JSObject key, JSObject iv);
}
