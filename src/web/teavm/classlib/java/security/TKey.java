package web.teavm.classlib.java.security;

import java.io.Serializable;

public interface TKey extends Serializable {
    String getAlgorithm();

    String getFormat();

    byte[] getEncoded();
}
