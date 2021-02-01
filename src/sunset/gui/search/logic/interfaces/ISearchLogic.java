package sunset.gui.search.logic.interfaces;

public interface ISearchLogic extends IBaseLogic {
	
	/**
	 * Searches for a pattern in a given text from a specified position
	 * @param text the subject string in which the pattern is searched
	 * @param pattern the pattern to search for
	 * @param fromIndex the index the search should start from
	 * @param matchCase the flag to specify if case sensitive search is used
	 * @param wrapAround the flag to specify if wrap around search is used
	 * @return true if the pattern was found in the text considering the options, otherwise false
	 */
	public boolean search(String text, String pattern, int fromIndex, boolean matchCase, boolean wrapAround);

	/**
	 * Searches for a pattern in a given text from a specified position using regular expressions
	 * @param text the subject string in which the pattern is searched
	 * @param pattern the regular expression pattern to search for
	 * @param fromIndex the index the search should start from
	 * @param matchCase the flag to specify if case sensitive search is used
	 * @param wrapAround the flag to specify if wrap around search is used
	 * @param dotAll the flag to specify if a dot should also match newline characters
	 * @return true if the pattern was found in the text using regular expressions and considering the options, otherwise false
	 */
	public boolean searchRegex(String text, String pattern, int fromIndex, boolean matchCase, boolean wrapAround, boolean dotAll);
	
	/**
	 * Searches for a pattern in a given text from a specified position using advanced search
	 * @param text the subject string in which the pattern is searched
	 * @param pattern the pattern to search for
	 * @param fromIndex the index the search should start from
	 * @param matchCase the flag to specify if case sensitive search is used
	 * @param wrapAround the flag to specify if wrap around search is used
	 * @return true if the pattern was found in the text using advanced search and considering the options, otherwise false
	 */
	public boolean searchAdvanced(String text, String pattern, String matchingPairs, 
			int fromIndex, boolean matchCase, boolean wrapAround, boolean showBalancingError);
	
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