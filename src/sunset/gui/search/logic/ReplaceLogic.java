package sunset.gui.search.logic;

import java.util.regex.Matcher;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.interfaces.IReplaceLogic;

public class ReplaceLogic extends BaseLogic implements IReplaceLogic {	
	
	@Override
	public String replaceRegex(ReplaceContext context, boolean dotAll) throws Exception {
		Matcher m = getMatcher(context.getText(), context.getPattern(), context.isMatchCase(), dotAll);
		
		return m != null ? m.replaceAll(context.getReplaceWith()) : null;
	}

	@Override
	public String replaceAdvanced(ReplaceContext context, String matchingPairs, boolean showBalancingError) throws Exception {
		String text = context.getText();
		
		IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace(matchingPairs);
		_error = false;
			
		if (advSearchReplace.find(new SearchContext(text, context.getPattern(), 0, context.isMatchCase()), showBalancingError)) {
			String prefix = text.substring(0, advSearchReplace.getStart());
			String suffix = text.substring(advSearchReplace.getEnd(), text.length());
			String[] contents = advSearchReplace.getCaptures();

			return prefix + advSearchReplace.replaceVariables(context.getReplaceWith(), contents) + suffix;
		}
		
		return null;
	}
}
