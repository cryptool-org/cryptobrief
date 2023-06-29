package ffapl.java.matcher;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher extends FFaplMatcher {
	protected Pattern pattern;

	public RegexMatcher(String regex) {
		super();
		this.pattern = Pattern.compile(regex);
	}

	public RegexMatcher(String regex, int font, Color color) {
		this(regex);
		setFontWeight(font);
		setColor(color);
	}

	public RegexMatcher(String regex, int font) {
		this(regex);
		setFontWeight(font);
	}

	public RegexMatcher(String regex, Color color) {
		this(regex);
		setColor(color);
	}

	public boolean find() {
		boolean result = false;

		Matcher matcher = pattern.matcher(getText());
		if (matcher.find(super.end())) {
			setStart(matcher.start());
			setEnd(matcher.end());
			result = true;
		}
		return result;
	}
}
