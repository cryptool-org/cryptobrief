package ffapl.java.matcher;

import java.awt.Font;
import java.util.regex.Pattern;

import sunset.gui.editor.FFaplRegex;


public class VariableMatcher extends RegexMatcher{

	public VariableMatcher(String sufixRegex){
		super("");
		String regex = "\\b[a-zA-Z][a-zA-z0-9_]*\\b[ \t]*:[ \t]*(" + sufixRegex + ")";
		setFontWeight(Font.PLAIN);
		setColor(FFaplRegex.BLUE);
		super.pattern = Pattern.compile(regex);
	}
	
	public boolean find() {
		boolean result = super.find();
		
		return result;
	}
}
