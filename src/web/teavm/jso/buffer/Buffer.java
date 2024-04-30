package web.teavm.jso.buffer;

import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

public abstract class Buffer implements JSObject {

    private Buffer() {}

    @JSProperty
    @NoSideEffects
    public abstract int getLength();

    @JSIndexer
    @NoSideEffects
    public abstract byte get(int index);

    @JSIndexer
    public abstract void set(int index, byte value);

    @JSBody(params = "size", script = "return Buffer.alloc(size);")
    public static native Buffer alloc(int size);

    @JSBody(params = "array", script = "return Buffer.from(array);")
    public static native Buffer from(JSArray<JSNumber> array);

    @JSBody(params = "arrayBuffer", script = "return Buffer.from(arrayBuffer);")
    public static native Buffer from(ArrayBuffer arrayBuffer);

    @JSBody(params = "buffer", script = "return Buffer.from(buffer);")
    public static native Buffer from(Uint8Array buffer);

    @JSBody(params = "object", script = "return Buffer.isBuffer(object);")
    @NoSideEffects
    public static native boolean isBuffer(JSObject object);
}
