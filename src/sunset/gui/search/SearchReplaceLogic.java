package sunset.gui.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.interfaces.ISearchReplaceLogic;

public class SearchReplaceLogic implements ISearchReplaceLogic {
	
	private int _matchStart;
	private int _matchEnd;
	private String _message;
	private boolean _error;
	
	public SearchReplaceLogic() {
	}
	
	@Override
	public boolean search(String text, String pattern, int fromIndex, boolean matchCase, boolean wrapAround) {
		if (matchCase) {
			_matchStart = text.indexOf(pattern, fromIndex);
		} else {
			_matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), fromIndex);
		}
		
		// if not found starting fromIndex, fromIndex was not 0, and wrap around is activated, search again from 0
		if (_matchStart == -1 && fromIndex != 0 && wrapAround) {
			return search(text, pattern, 0, matchCase, wrapAround);
		}
		
		if (_matchStart != -1) {
			_matchEnd = _matchStart + pattern.length();
		} else {
			_matchEnd = -1;
		}
		
		_message = generateMessage(pattern);
		_error = false;
		
		return _matchStart != -1;
	}
	
	@Override
	public boolean searchRegex(String text, String pattern, int fromIndex, boolean matchCase, boolean wrapAround, boolean dotAll) {
		Matcher m = getMatcher(text, pattern, matchCase, dotAll);
		
		if (m != null) {
			if (m.find(fromIndex) || wrapAround && fromIndex != 0 && m.find(0)) {
				_matchStart = m.start();
				_matchEnd = m.end();
			} else {
				_matchStart = -1;
				_matchEnd = -1;
			}
			
			_message = generateMessage(pattern);
			_error = false;
			return _matchStart != -1;
		} else {
			_error = true;
			return false;
		}
	}
	
	@Override
	public boolean searchAdvanced(String text, String pattern, String matchingPairs, 
			int fromIndex, boolean matchCase, boolean wrapAround, boolean showBalancingError) {
		IAdvancedSearchReplace advSearchReplace = null;
		
		try {
			advSearchReplace = new AdvancedSearchReplace(matchingPairs);
			boolean found = advSearchReplace.find(text, pattern, fromIndex, matchCase, showBalancingError);
			
			// if not found starting fromIndex, fromIndex was not 0, and wrap around is activated, search again from 0
			if (!found && fromIndex != 0 && wrapAround) {
				found = advSearchReplace.find(text, pattern, 0, matchCase, showBalancingError);
			}

			_matchStart = advSearchReplace.getStart();
			_matchEnd = advSearchReplace.getEnd();
			_message = generateMessage(pattern);
			_error = false;
			return found;
		} catch (InvalidPatternException | IndexOutOfBoundsException | UnbalancedStringException | MatchingPairConfigurationException e) {
			if (advSearchReplace != null) {
				_matchStart = advSearchReplace.getStart();
				_matchEnd = advSearchReplace.getEnd();
			}
			_message = e.getMessage();
			_error = true;
			return false;
		}
	}
	
	/**
	 * Returns a Matcher object corresponding to the specified text and regular expression pattern
	 * @param text the text the matcher is based on
	 * @param pattern the pattern the matcher is based on
	 * @param matchCase the flag indicating if case sensitive regular expression matching is required
	 * @param dotAll the flag indicating if . should match newline characters in the regular expression
	 * @return a Matcher object corresponding to the given parameters, null if an invalid regular expression was specified
	 */
	private Matcher getMatcher(String text, String pattern, boolean matchCase, boolean dotAll) {
		try {
			final int flags = (matchCase ? 0 : Pattern.CASE_INSENSITIVE) | (dotAll ? Pattern.DOTALL : 0);
			Pattern p = Pattern.compile(pattern, flags);
			return p.matcher(text);
		} catch (PatternSyntaxException e) {	// bad regular expression pattern specified 
			_message = e.getMessage();
			return null;
		}
	}
	
	private String generateMessage(String pattern) {
		boolean found = _matchStart != -1;
		
		if (found && _matchStart == _matchEnd) {
			return "Zero length match at line ";
		}
		
		return "\"" + handleEscapes(pattern) + "\"" + (found ? " found at line " : " not found from line ");
	}
	
	private String handleEscapes(String s) {
		return s.replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r").replace("\b",  "\\b");
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
	public boolean getError() {
		return _error;
	}

	@Override
	public boolean matchesRegex(String text, String pattern, boolean matchCase, boolean dotAll) {
		Matcher m = getMatcher(text, pattern, matchCase, dotAll);
		
		return m != null ? m.matches() : false;
	}

	@Override
	public boolean matchesAdvanced(String text, String pattern, String matchingPairs, boolean matchCase) {
		try {
			IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace(matchingPairs);
			_error = false;
			return advSearchReplace.matches(text, pattern, matchCase);
		} catch (InvalidPatternException | MatchingPairConfigurationException e) {
			_message = e.getMessage();
			_error = true;
			return false;
		}
	}

	@Override
	public boolean equals(String text, String pattern, boolean matchCase) {
		return matchCase ? text.equals(pattern) : text.toLowerCase().equals(pattern.toLowerCase());
	}

	@Override
	public String replaceRegex(String text, String pattern, String replaceWith, boolean matchCase, boolean dotAll) throws Exception {
		Matcher m = getMatcher(text, pattern, matchCase, dotAll);
		
		return m != null ? m.replaceAll(replaceWith) : null;
	}

	@Override
	public String replaceAdvanced(String text, String pattern, String replaceWith, String matchingPairs, 
			boolean matchCase, boolean showBalancingError) throws UndeclaredVariableException {
		try {
			IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace(matchingPairs);
			_error = false;
			if (advSearchReplace.find(text, pattern, 0, matchCase, showBalancingError)) {
				String prefix = text.substring(0, advSearchReplace.getStart());
				String suffix = text.substring(advSearchReplace.getEnd(), text.length());
				String[] contents = advSearchReplace.getCaptures();

				return prefix + advSearchReplace.replaceVariables(replaceWith, contents) + suffix;
			}
		} catch (InvalidPatternException | UnbalancedStringException | MatchingPairConfigurationException e) {
			_message = e.getMessage();
			_error = true;
		}
		
		return null;
	}
}