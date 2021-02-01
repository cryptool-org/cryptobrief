package sunset.gui.search.logic.interfaces;

import sunset.gui.search.logic.SearchContext;

public interface ISearchLogic extends IBaseLogic {

	public boolean search        (SearchContext context, boolean wrapAround);
	public boolean searchRegex   (SearchContext context, boolean wrapAround, boolean dotAll);
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