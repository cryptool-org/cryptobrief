package sunset.gui.search.interfaces;

public interface ISearchLogic {
	
	/**
	 * Searches for a pattern in a given text from a specified position
	 * @param text the subject string in which the pattern is searched
	 * @param pattern the pattern to search for
	 * @param fromIndex the index the search should start from
	 * @param bMatchCase the flag to specify if case sensitive search is used
	 * @param bWrapAround the flag to specify if wrap around search is used
	 * @return true if the pattern was found in the text considering the options, otherwise false
	 */
	public boolean search(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround);
	
	/**
	 * Searches for a pattern in a given text from a specified position using regular expressions
	 * @param text the subject string in which the pattern is searched
	 * @param pattern the regular expression pattern to search for
	 * @param fromIndex the index the search should start from
	 * @param bMatchCase the flag to specify if case sensitive search is used
	 * @param bWrapAround the flag to specify if wrap around search is used
	 * @param bDotAll the flag to specify if a dot should also match newline characters
	 * @return true if the pattern was found in the text using regular expressions and considering the options, otherwise false
	 */
	public boolean searchRegex(String text, String pattern, int fromIndex, boolean bMatchCase, boolean bWrapAround, boolean bDotAll);
	
	/**
	 * Compares two strings for equality considering the specified flags
	 * @param text the first string for comparison
	 * @param pattern the second string for comparison
	 * @param bMatchCase the flag indicating if case sensitive comparison is required
	 * @return true if the two strings are equal considering the specified options, otherwise false
	 */
	public boolean equals(String text, String pattern, boolean bMatchCase);
	
	/**
	 * Compares a string to a given regular expression considering the specified flags
	 * @param text the string for comparison
	 * @param pattern the regular expression pattern
	 * @param bMatchCase the flag indicating if case sensitive regular expression matching is required
	 * @param bDotAll the flag indicating if . should match newline characters in the regular expression
	 * @return true if the string matches the regular expression pattern considering the specified options, otherwise false
	 */
	public boolean matchesRegex(String text, String pattern, boolean bMatchCase, boolean bDotAll);
	
	/**
	 * Returns the start position of the match, -1 if match failed
	 * @return start position of the match, -1 if match failed
	 */
	public int getStart();
	
	/**
	 * Returns the end position of the match
	 * @return end position of the match
	 */
	public int getEnd();
	
	/**
	 * Returns the message of the search operation or the exception message if a invalid regular expression was used
	 * @return the message of the search operation or the exception message if a invalid regular expression was used
	 */
	public String getMessage();
}
