package ffapl.java.matcher;

import java.awt.Color;

public class StringMatcher extends FFaplMatcher {

	private String delimiter;
	private String escape;

	public StringMatcher() {
		super();
		delimiter = "\"";
		escape = "\\";
	}

	public StringMatcher(int font, Color color) {
		this();
		setFontWeight(font);
		setColor(color);
	}

	public StringMatcher(int font) {
		this();
		setFontWeight(font);
	}

	public StringMatcher(Color color) {
		this();
		setColor(color);
	}

	public boolean find() {
		setStart(getIndex(end()));
		if (start() >= 0) {
			setEnd(getIndex(start() + 1));
			if (end() < 0) {
				setEnd(getText().length());
			} else {
				setEnd(end() + delimiter.length());
			}
			return true;
		}
		return false;
	}

	private int getIndex(int start) {
		int delimiterIndex = getText().indexOf(delimiter, start);
		int escapeIndex = getText().indexOf(escape + delimiter, start);
		while (escapeIndex >= 0 && escapeIndex + 1 == delimiterIndex) {
			delimiterIndex = getText().indexOf(delimiter, delimiterIndex + 1);
			escapeIndex = getText().indexOf(escape + delimiter, delimiterIndex + 1);
		}
		return delimiterIndex;
	}
}
