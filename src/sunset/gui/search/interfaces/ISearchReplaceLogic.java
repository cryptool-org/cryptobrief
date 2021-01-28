package sunset.gui.search.interfaces;

import sunset.gui.search.advanced.exception.UndeclaredVariableException;

public interface ISearchReplaceLogic {
	
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
	 * Compares two strings for equality considering the specified flags
	 * @param text the first string for comparison
	 * @param pattern the second string for comparison
	 * @param matchCase the flag indicating if case sensitive comparison is required
	 * @return true if the two strings are equal considering the specified options, otherwise false
	 */
	public boolean equals(String text, String pattern, boolean matchCase);
	
	/**
	 * Compares a string to a given regular expression considering the specified flags
	 * @param text the string for comparison
	 * @param pattern the regular expression pattern
	 * @param matchCase the flag indicating if case sensitive regular expression matching is required
	 * @param dotAll the flag indicating if . should match newline characters in the regular expression
	 * @return true if the string matches the regular expression pattern considering the specified options, otherwise false
	 */
	public boolean matchesRegex(String text, String pattern, boolean matchCase, boolean dotAll);
	
	public boolean matchesAdvanced(String text, String pattern, String matchingPairs, boolean matchCase);
	
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
	
	/**
	 * Returns the message of the search operation or the exception message if a invalid regular expression was used
	 * @return the message of the search operation or the exception message if a invalid regular expression was used
	 */
	public String getMessage();
	
	public boolean getError();
	
	/**
	 * Replaces all occurrences of the pattern in a given text with the replaceWith text,
	 * starting from a specified position using regular expressions
	 * @param text the subject string in which the pattern is searched
	 * @param pattern the regular expression pattern to search for
	 * @param replaceWith the replacement text
	 * @param matchCase the flag to specify if case sensitive search is used
	 * @param dotAll the flag to specify if a dot should also match newline characters
	 * @return the result string of the replacement operation
	 */
	public String replaceRegex(String text, String pattern, String replaceWith, boolean matchCase, boolean dotAll)  throws Exception;
	
	public String replaceAdvanced(String text, String pattern, String replaceWith, String matchingPairs, 
			boolean matchCase, boolean showBalancingError) throws UndeclaredVariableException;
}