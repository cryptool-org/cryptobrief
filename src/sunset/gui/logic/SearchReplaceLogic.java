package sunset.gui.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchReplaceLogic {
	
	private int matchStart = -1;
	private int matchEnd = -1;
	private String message;
	
	public SearchReplaceLogic() {
	}

	public boolean search(String text, String pattern, int pos, boolean bMatchCase, boolean bWrapAround) {
		matchStart = -1;
		matchEnd = -1;
		
		if (bMatchCase) {
			matchStart = text.indexOf(pattern, pos);
		} else {
			matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), pos);
		}
		
		if (matchStart == -1 && bWrapAround) {
			if (bMatchCase) {
				matchStart = text.indexOf(pattern, 0);
			} else {
				matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), 0);
			}
		}
		
		if (matchStart != -1) {
			matchEnd = matchStart + pattern.length();
			this.message = "\"" + pattern + "\" found at position " + matchStart;
		} else {
			this.message = "\"" + pattern + "\" not found from position " + pos;
		}
		
		return matchStart != -1;
	}
	
	public boolean searchRegex(String text, String pattern, int pos, boolean bMatchCase, boolean bWrapAround, boolean bDotMatchesNewLine) {
		try {
			final int flags = (bMatchCase?0:Pattern.CASE_INSENSITIVE) | (bDotMatchesNewLine?Pattern.DOTALL:0);
			Pattern p = Pattern.compile(pattern, flags);
			Matcher m = p.matcher(text);

			if (m.find(pos) || bWrapAround && m.find(0)) {
				matchStart = m.start();
				matchEnd = m.end();
				this.message = "\"" + pattern + "\" found at position " + matchStart;
				return true;
			} else {
				matchStart = -1;
				matchEnd = -1;
				this.message = "\"" + pattern + "\" not found from position " + pos;
				return false;
			}
		} catch (PatternSyntaxException e) {
			this.message = e.getMessage();
			return false;
		}
	}
	
	public int getStart() {
		return matchStart;
	}
	
	public int getEnd() {
		return matchEnd;
	}
	
	public String getMessage() {
		return message;
	}
}
