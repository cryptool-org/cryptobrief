package ffapl.java.logging;

/**
 * Represents a FFaplLogMessage
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplLogMessage {

	private int _kind;
	private String _message;
	private int _column;
	private int _line;
	
	/**
	 * 
	 * @param kind
	 * @param message
	 */
	public FFaplLogMessage(int kind, String message){
		_kind = kind;
		_message = message;
		_line = -1;
		_column = -1;
	}
	
	/**
	 * 
	 * @param kind
	 * @param message
	 * @param line
	 * @param column
	 */
	public FFaplLogMessage(int kind, String message, int line, int column){
		this(kind, message);
		_line = line;
		_column = column;
	}
	
	/**
	 * Returns the message
	 * @return
	 */
	public String message(){
		return _message;
	}
	
	/**
	 * Returns the kind of the message
	 * @return
	 */
	public int kind(){
		return _kind;
	}
	
	/**
	 * returns line of log if applicable
	 * @return
	 */
	public int line(){
		return _line;
	}
	
	/**
	 * Returns column of log if applicable
	 * @return
	 */
	public int column(){
		return _column;
	}
}
