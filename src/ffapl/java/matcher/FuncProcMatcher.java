package ffapl.java.matcher;

import java.awt.Color;

public class FuncProcMatcher extends RegexMatcher {

	public FuncProcMatcher(String regex) {
		super(regex);
	}

	public FuncProcMatcher(String regex, int font, Color color) {
		this(regex);
		setFontWeight(font);
		setColor(color);
	}

	public FuncProcMatcher(String regex, int font) {
		this(regex);
		setFontWeight(font);
	}

	public FuncProcMatcher(String regex, Color color) {
		this(regex);
		setColor(color);
	}

	public boolean find() {
		boolean result = super.find();
		if (result) {
			setEnd(end()-1);
		}
		return result;
	}
}
