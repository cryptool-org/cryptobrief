package sunset.gui.search.logic.interfaces;

import sunset.gui.search.logic.SearchContext;

public interface ISearchLogic extends IBaseLogic {

	/**
	 * Performs a search operation within the given context considering the wrapAround flag
	 * @param context The search context
	 * @param wrapAround The flag indicating if search should start from beginning of document if no more occurrence is found
	 * @return true if the pattern was found in the text considering the options
	 */
	public boolean search(SearchContext context, boolean wrapAround);
	
	/**
	 * Performs a regex-based search operation within the given context considering the given flags
	 * @param context The search context
	 * @param wrapAround The flag indicating if search should start from beginning of document if no more occurrence is found
	 * @param dotAll The flag indicating if . should match new line
	 * @return true if the regex pattern was found in the text considering the options
	 */
	public boolean searchRegex(SearchContext context, boolean wrapAround, boolean dotAll);
	
	/**
	 * Performs an advanced search operation within the given context considering the given flags
	 * @param context The search context
	 * @param wrapAround The flag indicating if search should start from beginning of document if no more occurrence is found
	 * @param matchingPairs The string representation of the matching pair configuration used for balancing
	 * @param showBalancingError The flag indicating if balancing errors should be highlighted
	 * @return true if the advanced search pattern was found in the text considering the options
	 */
	public boolean searchAdvanced(SearchContext context, boolean wrapAround, String matchingPairs, boolean showBalancingError);
	
	/**
	 * Returns the start position of the match, -1 if match failed
	 * @return the start position of the match, -1 if match failed
	 */
	public int getStart();
	
	/**
	 * Returns the end position of the match
	 * @return the end position of the match
	 */
	public int getEnd();
}