package sunset.gui.search.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.interfaces.ISearchLogic;

public class SearchLogic extends BaseLogic implements ISearchLogic {
	
	private int _matchStart;
	private int _matchEnd;
	
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
	
	private String generateMessage(String pattern) {
		boolean found = _matchStart != -1;
		
		if (found && _matchStart == _matchEnd) {
			return "Zero length match at line ";
		}
		
		pattern = pattern.replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r");
		
		return "\"" + pattern + "\"" + (found ? " found at line " : " not found from line ");
	}
	
	@Override
	public int getStart() {
		return _matchStart;
	}
	
	@Override
	public int getEnd() {
		return _matchEnd;
	}
}