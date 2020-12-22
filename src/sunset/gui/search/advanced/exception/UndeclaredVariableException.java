package sunset.gui.search.advanced.exception;

@SuppressWarnings("serial")
public class UndeclaredVariableException extends Exception {
	public UndeclaredVariableException(int varIndex) {
		super("Variable %" + varIndex + " from replace text has not been used in search pattern!");
	}
}
