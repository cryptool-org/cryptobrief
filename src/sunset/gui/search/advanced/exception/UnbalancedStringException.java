package sunset.gui.search.advanced.exception;

@SuppressWarnings("serial")
public class UnbalancedStringException extends Exception {
	public UnbalancedStringException(String msg) {
		super("Matched string is unbalanced: " + msg);
	}
}
