/**
 * 
 */
package ffapl.java.logging;

import java.util.Observable;

import ffapl.exception.FFaplWarning;
import ffapl.java.interfaces.ILevel;

/**
 * Used for FFaplLogging
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplLogger extends Observable {

	private String _name;

	
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
	 * Returns name of the Logger
	 * @return
	 */
	public String name(){
		return _name;
	}
	
}
