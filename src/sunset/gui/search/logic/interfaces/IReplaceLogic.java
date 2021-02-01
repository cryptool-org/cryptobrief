package sunset.gui.search.logic.interfaces;

import sunset.gui.search.advanced.exception.UndeclaredVariableException;

public interface IReplaceLogic extends IBaseLogic {
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
