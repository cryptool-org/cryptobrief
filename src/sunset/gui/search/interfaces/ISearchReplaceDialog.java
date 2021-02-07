package sunset.gui.search.interfaces;

import java.awt.Color;

public interface ISearchReplaceDialog {
	/**
	 * Returns the value of the search textbox
	 * @return the pattern to search for
	 */
	public String searchPattern();
	
	/**
	 * Returns the value of the replace textbox
	 * @return the replacement text
	 */
	public String replaceText();
	
	/**
	 * Sets the status of the dialog
	 * @param status the new status of the dialog
	 * @param color the used color for the status
	 */
	public void setStatus(String status, Color color);
	
	/**
	 * Returns the value of the match case flag, i.e. if case sensitive search should be used
	 * @return status of match case checkbox
	 */
	public boolean matchCase();
	
	/**
	 * Returns the value of the wrap around flag, i.e. if search should start from beginning if pattern was not found
	 * @return status of wrap around checkbox
	 */
	public boolean wrapAround();
	
	/**
	 * Returns the value of the replace all from start flag, i.e. if replace all should start from start of the text
	 * @return status of the from start checkbox
	 */
	public boolean replaceAllFromStart();
	
	/**
	 * Returns the value of the use regular expression flag, i.e. if regular expression search should be used
	 * @return status of regular expression checkbox
	 */
	public boolean useRegEx();
	
	/**
	 * Returns the value of the . matches newline flag, i.e. if a . in the regular expression should match new line characters (e.g. \n)
	 * @return status of . matches newline checkbox
	 */
	public boolean dotMatchesNewLine();	

	/**
	 * Returns the value of the use special symbols flag, i.e. if \n, \r and \t should be treated as symbols
	 * @return status of the use special symbols checkbox
	 */
	public boolean useSpecialSymbols();
	
	/**
	 * Returns the value of the show balancing error flag, i.e. if a balancing error should be highlighted
	 * @return status of the show balancing error checkbox
	 */
	public boolean showBalancingError();
	
	/**
	 * Returns the value of the use advanced search flag, i.e. if advanced search should be used
	 * @return status of advanced search checkbox
	 */
	public boolean useAdvancedSearch();
}
