package sunset.gui.interfaces;

import java.awt.Color;

public interface IDialogSearchReplace {
	/**
	 * Returns the value of the search textbox
	 * @return the pattern to search for
	 */
	public String searchPattern();
	
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
	 * Returns the value of the use regular expression flag, i.e. if regular expression search should be used
	 * @return status of regular expression checkbox
	 */
	public boolean useRegEx();
	
	/**
	 * Returns the value of the . matches newline flag, i.e. if a . in the regular expression should match new line characters (e.g. \n)
	 * @return status of . matches newline checkbox
	 */
	public boolean dotMatchesNewLine();	
}
