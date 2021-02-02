package sunset.gui.search.logic;

import java.util.regex.Matcher;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.interfaces.IReplaceLogic;

public class ReplaceLogic extends BaseLogic implements IReplaceLogic {	
	
	@Override
	public String replaceRegex(String text, String pattern, String replaceWith, boolean matchCase, 
			boolean dotAll) throws Exception {
		Matcher m = getMatcher(text, pattern, matchCase, dotAll);
		
		return m != null ? m.replaceAll(replaceWith) : null;
	}

	@Override
	public String replaceAdvanced(String text, String pattern, String replaceWith, String matchingPairs, 
			boolean matchCase, boolean showBalancingError) throws UndeclaredVariableException {
		try {
			IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace(matchingPairs);
			_error = false;
			
			if (advSearchReplace.find(new SearchContext(text, pattern, 0, matchCase), showBalancingError)) {
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
