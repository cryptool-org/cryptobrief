package sunset.gui.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.interfaces.ISearchReplaceLogic;

public class SearchReplaceLogic implements ISearchReplaceLogic {
	
	private int matchStart;
	private int matchEnd;
	private String message;
	
	public SearchReplaceLogic() {
	}

	@Override
	public boolean search(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround) {
		matchStart = -1;
		
		if (bMatchCase) {
			matchStart = text.indexOf(pattern, fromIndex);
		} else {
			matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), fromIndex);
		}
		
		// if not found from pos, pos was not beginning (0), and wrap around is activated, search again from 0
		if (matchStart == -1 && fromIndex != 0 && bWrapAround) {
			if (bMatchCase) {
				matchStart = text.indexOf(pattern, 0);
			} else {
				matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), 0);
			}
		}
		
		if (matchStart != -1) {
			matchEnd = matchStart + pattern.length();
		}
		
		message = "\"" + pattern + "\"" + (matchStart == -1 ? " not" : "") + " found";
		
		return matchStart != -1;
	}
	
	@Override
	public boolean searchRegex(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround, boolean bDotAll) {
		try {
			final int flags = (bMatchCase ? 0 : Pattern.CASE_INSENSITIVE) | (bDotAll ? Pattern.DOTALL : 0);
			Pattern p = Pattern.compile(pattern, flags);
			Matcher m = p.matcher(text);

			if (m.find(fromIndex) || bWrapAround && m.find(0)) {
				matchStart = m.start();
				matchEnd = m.end();
			} else {
				matchStart = -1;
			}
		} catch (PatternSyntaxException e) {	// bad regular expression specified 
			message = e.getMessage();
			return false;
		}
		
		message = "\"" + pattern + "\"" + (matchStart == -1 ? " not" : "") + " found";
		
		return matchStart != -1;
	}
	
	@Override
	public int getStart() {
		return matchStart;
	}
	
	@Override
	public int getEnd() {
		return matchEnd;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
