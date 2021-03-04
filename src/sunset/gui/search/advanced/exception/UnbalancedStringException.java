package sunset.gui.search.advanced.exception;

@SuppressWarnings("serial")
public class UnbalancedStringException extends Exception {
	private int _start;
	private int _end;
	
	public UnbalancedStringException(String msg, int start, int end) {
		super(msg);
		_start = start;
		_end = end;
	}

	public int getStart() {
		return _start;
	}

	public int getEnd() {
		return _end;
	}
}
