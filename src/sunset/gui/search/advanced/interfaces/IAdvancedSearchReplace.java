package sunset.gui.search.advanced.interfaces;

import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.logic.SearchContext;

public interface IAdvancedSearchReplace {
	
	
	public boolean find(SearchContext context, boolean showBalancingError) throws InvalidPatternException, UnbalancedStringException;
	
	public boolean matches(String text, String pattern, boolean bMatchCase) 
			throws InvalidPatternException;
	
	public String replaceVariables(String pattern, String[] contents) 
			throws UndeclaredVariableException;
	
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
