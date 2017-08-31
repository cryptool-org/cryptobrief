package ffapl.java.matcher;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sunset.gui.editor.FFaplRegex;


public class RegexMatcher extends FFaplMatcher{
	protected Pattern pattern;

	public RegexMatcher(String regex, int font, Color color){
		this(regex);
		setFontWeight(font);
		setColor(color);
	}
	
	public RegexMatcher(String regex){
		super();
		setFontWeight(Font.PLAIN);
		setColor(FFaplRegex.BLACK);
		this.pattern = Pattern.compile(regex);
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
