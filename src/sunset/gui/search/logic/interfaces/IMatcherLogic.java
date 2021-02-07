package sunset.gui.search.logic.interfaces;

public interface IMatcherLogic extends IBaseLogic {
	
	/**
	 * Compares two strings for equality considering the specified flag
	 * @param text The first string for comparison
	 * @param pattern The second string for comparison
	 * @param matchCase The flag indicating if case sensitive comparison is required
	 * @return true if the two strings are equal considering the specified option, otherwise false
	 */
	public boolean equals(String text, String pattern, boolean matchCase);
	
	/**
	 * Compares a string with a given regular expression considering the specified flags
	 * @param text The text for comparison
	 * @param pattern The regular expression pattern
	 * @param matchCase The flag indicating if case sensitive regular expression matching is required
	 * @param dotAll The flag indicating if a dot should match newline characters in the regular expression
	 * @return true if the text matches the regular expression pattern considering the specified options, otherwise false
	 */
	public boolean matchesRegex(String text, String pattern, boolean matchCase, boolean dotAll);
	
	/**
	 * Compares a string with a given advanced search pattern considering the specified flag and the matching pair configuration
	 * @param text The text for comparison
	 * @param pattern The advanced search pattern
	 * @param matchCase The flag indicating if case sensitive matching is required
	 * @param matchingPairs The string containing the matching pair configuration
	 * @return true if the text matches the advanced search pattern considering the option and the matching pair configuration
	 */
	public boolean matchesAdvanced(String text, String pattern, boolean matchCase, String matchingPairs);
}
