package ffapl.java.logging;

import ffapl.utils.Observable;
import ffapl.utils.Observer;

import ffapl.java.enums.LoggerMode;

/**
 * Console Handler, prints all to log messages to stdout (shell)
 * @author Alexander Ortner
 * @version 1.0
 * Update (26.05.2023) by Bernd Busse: Moved GUI special handling into separate logging handler sunset.gui.logging.JTextPaneConsoleHandler
 */
public class FFaplConsoleHandler implements Observer {

    private LoggerMode _mode;

    /**
     *
     */
    public FFaplConsoleHandler() {
        this(LoggerMode.ALL);
    }

    /**
     *
     * @param mode
     */
    public FFaplConsoleHandler(LoggerMode mode){
        _mode = mode;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable looger, Object arguments) {
        FFaplLogMessage logMessage;
        logMessage = ((FFaplLogMessage) arguments);

        if (logMessage.kind() <= _mode.getMode()) {
            // TODO: Print errors and warnings to stderr instead (if preferrable)
            System.out.println(logMessage.message());
        }
    }
}
