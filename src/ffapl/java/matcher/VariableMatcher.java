package ffapl.java.matcher;

import java.awt.Color;
import java.util.regex.Pattern;

public class VariableMatcher extends RegexMatcher {

	private static final String PATTERN_START = "\\b[a-zA-Z][a-zA-z0-9_]*\\b[ \t]*:[ \t]*(";
	private static final String PATTERN_END = ")";

	public VariableMatcher(String suffixRegex) {
		super(PATTERN_START + suffixRegex + PATTERN_END);
	}

	public VariableMatcher(String suffixRegex, int font, Color color) {
		this(suffixRegex);
		setFontWeight(font);
		setColor(color);
	}

	public VariableMatcher(String suffixRegex, int font) {
		this(suffixRegex);
		setFontWeight(font);
	}

	public VariableMatcher(String suffixRegex, Color color) {
		this(suffixRegex);
		setColor(color);
	}

	public boolean find() {
		boolean result = super.find();
		return result;
	}
}
