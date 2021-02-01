package sunset.gui.search.logic.interfaces;

public interface IMatcherLogic extends IBaseLogic {
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
}
