package sunset.gui.search.advanced.interfaces;

import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.logic.SearchContext;

public interface IAdvancedSearchReplace {
	
	/**
	 * Performs an advanced search within the given context
	 * @param context The search context
	 * @param showBalancingError true if an UnbalancedStringException should be thrown if an unbalanced string is found 
	 * @return true if the pattern is found in the text considering the given options of the context and the balancing error flag
	 * @throws InvalidPatternException if the given advanced search pattern inside the context is invalid
	 * @throws UnbalancedStringException if the matched substring is unbalanced and the showBalancingError flag is true
	 */
	public boolean find(SearchContext context, boolean showBalancingError) throws InvalidPatternException, UnbalancedStringException;
	
	/**
	 * Performs an advanced match of the text and the pattern considering the matchCase flag
	 * @param text The text to be matched
	 * @param pattern The pattern to be matched
	 * @param matchCase The flag indicating if the match should be performed case sensitive
	 * @return true if the text and the pattern match considering the matchCase flag
	 * @throws InvalidPatternException if the given advanced search pattern is invalid
	 */
	public boolean matches(String text, String pattern, boolean matchCase) throws InvalidPatternException;
	
	/**
	 * Replaces all variables in the pattern with the given contents
	 * @param pattern The advanced pattern which variables have to be replaced
	 * @param contents The contents of the variables
	 * @return The pattern with all variables replaced with according contents
	 * @throws UndeclaredVariableException if the content of a variable is null
	 */
	public String replaceVariables(String pattern, String[] contents) throws UndeclaredVariableException;
	
	/**
	 * Validates the given pattern, i.e. checks if the pattern fulfills the following conditions:
	 * - the pattern includes at least one variable
	 * - all variables in the pattern are used only once
	 * - the pattern does not comprise two consecutive variables (e.g. %1%2) without delimiter	 
	 * @param pattern The pattern to be validated
	 * @throws InvalidPatternException if the pattern is invalid, i.e. fulfills one of the conditions:
	 * - the pattern comprises no variable
	 * - the pattern comprises a variable more than once
	 * - the pattern comprises two variables without delimiter in between
	 */
	public void validatePattern(String pattern) throws InvalidPatternException;
	
	/**
	 * Converts the given pattern into a regular expression pattern
	 * Example: The pattern a%1b is converted into the regex \Qa\E(?<g1>.*?)\Qb\E 
	 * @param pattern The advanced search pattern to be converted to a regular expression pattern
	 * @return a regex representation of the pattern where all literals are quoted and variables are replaced by a named capturing group (?<g[0-9]>.*?)
	 */
	public String convertPatternToRegex(String pattern);
	
	/**
	 * Returns the captures of the advanced search
	 * @return the captures of the advanced search
	 */
	public String[] getCaptures();
	
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
