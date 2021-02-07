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
