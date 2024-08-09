package sunset.gui.search.logic.interfaces;

import sunset.gui.search.logic.ReplaceContext;

public interface IReplaceLogic extends IBaseLogic {

	/**
	 * Performs a regex-based replace operation inside the context considering the dotAll flag 
	 * @param context The replace context
	 * @param dotAll The flag indicating if . should match newline
	 * @return The result of the replace regex operation
	 * @throws Exception If there was an error during the replace regex operation
	 */
	public String replaceRegex(ReplaceContext context, boolean dotAll) throws Exception;
	
	/**
	 * Performs an advanced replace operation inside the context considering the showBalancingError flag
	 * @param context The replace context
	 * @param matchingPairs The string containing the matching pair configuration for balancing
	 * @param showBalancingError The flag indicating if a balancing error should be highlighted
	 * @return The result of the advanced replace operation
	 * @throws Exception If there was an error during the advanced replace operation
	 */
	public String replaceAdvanced(ReplaceContext context, String matchingPairs, boolean showBalancingError) throws Exception;
}
