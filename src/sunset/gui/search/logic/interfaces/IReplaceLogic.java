package sunset.gui.search.logic.interfaces;

import sunset.gui.search.logic.ReplaceContext;

public interface IReplaceLogic extends IBaseLogic {

	public String replaceRegex(ReplaceContext context, boolean dotAll) throws Exception;
	
	public String replaceAdvanced(ReplaceContext context, String matchingPairs, boolean showBalancingError) throws Exception;
}
