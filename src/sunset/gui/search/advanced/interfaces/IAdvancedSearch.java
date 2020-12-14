package sunset.gui.search.advanced.interfaces;

import sunset.gui.search.exception.InvalidPatternException;

public interface IAdvancedSearch {
	
	/**
	 * 
	 * @param text
	 * @param pattern
	 * @param fromIndex
	 * @param bMatchCase
	 * @return
	 * @throws InvalidPatternException
	 */
	public boolean find(String text, String pattern, int fromIndex, boolean bMatchCase) throws InvalidPatternException;
	
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
