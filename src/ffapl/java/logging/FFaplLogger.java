/**
 * 
 */
package ffapl.java.logging;

import ffapl.exception.FFaplWarning;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.ILevel;
import ffapl.utils.FFaplProperties;
import ffapl.utils.Observable;

/**
 * Used for FFaplLogging
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplLogger extends Observable {

	private String _name;
	private boolean slowOperationWarningAlreadyDisplayed;
	
	public FFaplLogger(String name){
		_name = name;
	}
	/**
	 * 
	 * @param kind
	 * @param message
	 */
	public void log(int kind, String message){
		setChanged();
		notifyObservers(new FFaplLogMessage(kind, message));
	}
	
	/**
	 * 
	 * @param kind
	 * @param message
	 * @param row
	 * @param column
	 */
	public void log(int kind, String message, int row, int column){
		setChanged();
		notifyObservers(new FFaplLogMessage(kind, message, row, column));
	}
	
	public void log(FFaplWarning warning) {
		setChanged();
		notifyObservers(new FFaplLogMessage(ILevel.WARNING, warning.getWarningMessage(), warning.warningLine(), warning.warningColumn()));
	}

	/**
	 * Displays a warning that the execution may take a lot of time.
	 * Is only displayed the first time this method is called after pressing the "Run" button.
	 */
	public void displaySlowOperationWarning() {
		// only display if it is the first time since this logger was constructed
		if(slowOperationWarningAlreadyDisplayed)
			return;

		StringBuilder msg = new StringBuilder();
		msg.append(FFaplProperties.getInstance().getProperty("COMMON.WARNING")).append(": ");
		msg.append(FFaplProperties.getInstance().getProperty(IAlgebraicError.WARNING_OPERATION_SLOW));
		msg.append(System.getProperty("line.separator"));
		log(ILevel.WARNING, msg.toString());

		// flick switch
		slowOperationWarningAlreadyDisplayed = true;
	}
	
	/**
	 * Returns name of the Logger
	 * @return
	 */
	public String name(){
		return _name;
	}
	
}
