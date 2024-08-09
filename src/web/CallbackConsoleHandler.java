package web;

import ffapl.utils.Observable;
import ffapl.utils.Observer;

import ffapl.java.enums.LoggerMode;
import ffapl.java.logging.FFaplLogMessage;

import org.teavm.jso.core.JSFunction;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

/**
 * Logging handler that forwards all messages to a JavaScript callback.
 * @author Bernd Busse
 * @version 1.0
 */
public class CallbackConsoleHandler implements Observer {

    /**
     * Callback interface for the log message handler.
     */
    public static abstract class Callback extends JSFunction {

        /**
         * Convert parameters to JS wrapper types and call this function.
         */
        void log(int level, String message, int line, int column) {
            JSNumber jsLevel = JSNumber.valueOf(level);
            JSString jsMessage = JSString.valueOf(message);
            JSNumber jsLine = JSNumber.valueOf(line);
            JSNumber jsColumn = JSNumber.valueOf(column);

            this.call(this, jsLevel, jsMessage, jsLine, jsColumn);
        }
    }

    private LoggerMode _mode;
    private Callback _callback;

    public CallbackConsoleHandler(Callback cb) {
        this(cb, LoggerMode.ALL);
    }

    public CallbackConsoleHandler(Callback cb, LoggerMode mode) {
        _callback = cb;
        _mode = mode;
    }

    @Override
    public void update(Observable logger, Object arguments) {
        FFaplLogMessage logMessage = (FFaplLogMessage) arguments;

        if (logMessage.kind() <= _mode.getMode()) {
            _callback.log(logMessage.kind(), logMessage.message(), logMessage.line(), logMessage.column());
        }
    }
}
