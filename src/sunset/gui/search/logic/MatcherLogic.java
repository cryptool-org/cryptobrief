package sunset.gui.search.logic;

import java.util.regex.Matcher;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.interfaces.IMatcherLogic;

public class MatcherLogic extends BaseLogic implements IMatcherLogic {
	
	@Override
	public boolean matchesRegex(String text, String pattern, boolean matchCase, boolean dotAll) {
		Matcher m = getMatcher(text, pattern, matchCase, dotAll);
		
		return m != null ? m.matches() : false;
	}

	@Override
	public boolean matchesAdvanced(String text, String pattern, boolean matchCase, String matchingPairs) {
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
}
