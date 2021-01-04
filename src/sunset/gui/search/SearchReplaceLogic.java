package sunset.gui.search;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.interfaces.ISearchReplaceLogic;
import sunset.gui.util.SunsetBundle;

public class SearchReplaceLogic implements ISearchReplaceLogic {
	
	private IAdvancedSearchReplace _advSearchReplace;
	private int _matchStart;
	private int _matchEnd;
	private String _message;
	
	public SearchReplaceLogic() {
		_advSearchReplace = new AdvancedSearchReplace();
	}
	
	@Override
	public boolean search(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround) {
		if (bMatchCase) {
			_matchStart = text.indexOf(pattern, fromIndex);
		} else {
			_matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), fromIndex);
		}
		
		// if not found starting fromIndex, fromIndex was not 0, and wrap around is activated, search again from 0
		if (_matchStart == -1 && fromIndex != 0 && bWrapAround) {
			return search(text, pattern, 0, bMatchCase, bWrapAround);
		}
		
		if (_matchStart != -1) {
			_matchEnd = _matchStart + pattern.length();
		}
		
		_message = generateMessage(pattern, _matchStart != -1);
		
		return _matchStart != -1;
	}
	
	@Override
	public boolean searchRegex(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround, boolean bDotAll) {
		Matcher m = getMatcher(text, pattern, bMatchCase, bDotAll);
		
		if (m != null) {
			if (m.find(fromIndex) || bWrapAround && fromIndex != 0 && m.find(0)) {
				_matchStart = m.start();
				_matchEnd = m.end();
			} else {
				_matchStart = -1;
				_matchEnd = -1;
			}

			_message = generateMessage(pattern, _matchStart != -1);
			return _matchStart != -1;
		}
		
		return false;
	}
	
	@Override
	public boolean searchAdvanced(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround) {
		try {
			boolean bFound = _advSearchReplace.find(text, pattern, fromIndex, bMatchCase);
			
			// if not found starting fromIndex, fromIndex was not 0, and wrap around is activated, search again from 0
			if (!bFound && fromIndex != 0 && bWrapAround) {
				bFound = _advSearchReplace.find(text, pattern, 0, bMatchCase);
			}
			
			if (!bFound) {
				return search(text, pattern, fromIndex, bMatchCase, bWrapAround);
			} else {
				_matchStart = _advSearchReplace.getStart();
				_matchEnd = _advSearchReplace.getEnd();
			}
			
			_message = generateMessage(pattern, bFound);
			
			return bFound;
		} catch (InvalidPatternException e) {
			_message = e.getMessage();
			return false;
		}
	}
	
	/**
	 * Returns a Matcher object corresponding to the specified text and regular expression pattern
	 * @param text the text the matcher is based on
	 * @param pattern the pattern the matcher is based on
	 * @param bMatchCase the flag indicating if case sensitive regular expression matching is required
	 * @param bDotAll the flag indicating if . should match newline characters in the regular expression
	 * @return a Matcher object corresponding to the given parameters, null if an invalid regular expression was specified
	 */
	private Matcher getMatcher(String text, String pattern, boolean bMatchCase, boolean bDotAll) {
		try {
			final int flags = (bMatchCase ? 0 : Pattern.CASE_INSENSITIVE) | (bDotAll ? Pattern.DOTALL : 0);
			Pattern p = Pattern.compile(pattern, flags);
			Matcher m = p.matcher(text);
			
			return m;
		} catch (PatternSyntaxException e) {	// bad regular expression pattern specified 
			_message = e.getMessage();
			return null;
		}
	}
	
	/**
	 * Generates a message depending on the search outcome (success/failure)
	 * @param pattern the pattern to search for
	 * @param bSuccess the flag indicating if the search was successful or not
	 * @return the generated message
	 */
	private String generateMessage(String pattern, boolean bSuccess) {
		String message = "";
		
		if (SunsetBundle.getInstance().getLocale().getLanguage().equals(new Locale("en").getLanguage())) {
			message = "\"" + pattern + "\"" + (bSuccess ? " found at line " : " not found from line ");
		} else if (SunsetBundle.getInstance().getLocale().getLanguage().equals(new Locale("de").getLanguage())) {
			message = "\"" + pattern + "\"" + (bSuccess ? " gefunden in Zeile " : " nicht gefunden ab Zeile ");
		}
		
		return message;
	}
	
	@Override
	public int getStart() {
		return _matchStart;
	}
	
	@Override
	public int getEnd() {
		return _matchEnd;
	}
	
	@Override
	public String getMessage() {
		return _message;
	}

	@Override
	public boolean matchesRegex(String text, String pattern, boolean bMatchCase, boolean bDotAll) {
		Matcher m = getMatcher(text, pattern, bMatchCase, bDotAll);
		
		if (m != null) {
			return m.matches();
		}
		
		return false;
	}

	@Override
	public boolean matchesAdvanced(String text, String pattern, boolean bMatchCase) {
		try {
			boolean bResult = _advSearchReplace.matches(text, pattern, bMatchCase);
			
			return bResult;
		} catch (InvalidPatternException e) {
			_message = e.getMessage();
			return false;
		}
	}

	@Override
	public boolean equals(String text, String pattern, boolean bMatchCase) {
		if (bMatchCase) {
			return text.equals(pattern);
		} else {
			return text.toLowerCase().equals(pattern.toLowerCase());
		}
	}

	@Override
	public String replaceRegex(String text, String pattern, String replaceWith, boolean bMatchCase, boolean bDotAll) {
		Matcher m = getMatcher(text, pattern, bMatchCase, bDotAll);
		return m.replaceAll(replaceWith);
	}

	@Override
	public String replaceAdvanced(String text, String pattern, String replaceWith, boolean bMatchCase) throws UndeclaredVariableException {
		String replaceString = _advSearchReplace.getReplaceString(replaceWith);
		return replaceString;
	}
}
