package web;

import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

import ffapl.FFaplInterpreter;
import ffapl.java.interfaces.ILevel;
import ffapl.java.logging.FFaplConsoleHandler;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.util.FFaplRuntimeProperties;
import ffapl.utils.FFaplProperties;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSClass;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSExportClasses;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSPromise;
import org.teavm.jso.core.JSString;
import org.teavm.jso.function.JSConsumer;

/**
 * Export wrapper for JS/WASM version translated with TeaVM
 * @author Bernd Busse
 * @version 1.0
 */
@JSExportClasses({ TeaVMWrapper.LogLevel.class })
public class TeaVMWrapper {

    @JSClass(name = "LOG_LEVEL")
    public static final class LogLevel {
        @JSExport
        @JSProperty("RESULT")
        public static int getLogLevelResult() {
            return ILevel.RESULT;
        }

        @JSExport
        @JSProperty("ERROR")
        public static int getLogLevelError() {
            return ILevel.ERROR;
        }

        @JSExport
        @JSProperty("WARNING")
        public static int getLogLevelWarning() {
            return ILevel.WARNING;
        }
    }

    /**
     * Initalize and export constants and functions to JavaScript.
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            FFaplProperties properties = FFaplProperties.getInstance();
            properties.setLocale(new Locale(args[0]));
        } else if (args.length > 1) {
            throw new IllegalArgumentException("Too many arguments");
        }
    }

    /**
     * Set the language used for localization.
     */
    @JSExport
    public static void setLocale(String lang) {
        FFaplProperties properties = FFaplProperties.getInstance();
        properties.setLocale(new Locale(lang));
    }

    /**
     * Asynchronously execute a program.
     */
    @JSExport
    public static JSPromise execute(String code, CallbackConsoleHandler.Callback cb, JSNumber timeoutMs) {
        Reader reader = new StringReader(code);

        FFaplLogger logger = new FFaplLogger("FFaplLog");
        logger.addObserver(new CallbackConsoleHandler(cb));

        FFaplRuntimeProperties properties = FFaplRuntimeProperties.defaults();
        FFaplInterpreter interpreter = new FFaplInterpreter(logger, properties, null, reader);

        int timeout = JSObjects.isUndefined(timeoutMs) ? 0 : timeoutMs.intValue();
        ThreadExecutor executor = new ThreadExecutor(interpreter, timeout);

        var promise = new JSPromise<Object>(executor);
        return promise;
    }
}
