package ffapl.debugger;

import ffapl.java.logging.FFaplLogger;

public class FFaplDebuggerOutputLogger extends FFaplLogger {
    public FFaplDebuggerOutputLogger(String name) {
        super(name);
    }

    private String lastMessage = "Error";

    public String getMessage() {
        return lastMessage;
    }

    @Override
    public void log(int kind, String message) {
        lastMessage = message;
    }
}
