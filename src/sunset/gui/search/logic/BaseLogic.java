package sunset.gui.search.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.search.logic.interfaces.IBaseLogic;

public abstract class BaseLogic implements IBaseLogic {
	protected String _message;
	protected boolean _error;
	
	/**
	 * Returns a Matcher object corresponding to the specified text and regular expression pattern
	 * @param text the text the matcher is based on
	 * @param pattern the pattern the matcher is based on
	 * @param matchCase the flag indicating if case sensitive regular expression matching is required
	 * @param dotAll the flag indicating if . should match newline characters in the regular expression
	 * @return a Matcher object corresponding to the given parameters, null if an invalid regular expression was specified
	 */
	protected Matcher getMatcher(String text, String pattern, boolean matchCase, boolean dotAll) {
		try {
			final int flags = (matchCase ? 0 : Pattern.CASE_INSENSITIVE) | (dotAll ? Pattern.DOTALL : 0);
			Pattern p = Pattern.compile(pattern, flags);
			_error = false;
			return p.matcher(text);
		} catch (PatternSyntaxException e) {	// bad regular expression pattern specified 
			_message = e.getMessage();
			_error = true;
			return null;
		}
	}
	
	@Override
	public String getMessage() {
		return _message;
	}
	
	@Override
	public boolean getError() {
		return _error;
	}
}
