package sunset.gui.search.interfaces;

import javax.swing.JTextPane;

public interface ISearchReplaceCoordinator {
	
	/**
	 * Gets the text from the {@link JTextPane} and the inputs using the {@link ISearchReplaceDialog}.
	 * Afterwards the {@link ISearchLogic} is used to perform the search operation.
	 * Depending on the result, the caret position in the {@link JTextPane} and the dialog status are set.
	 * @param ignoreWrapAroundFlag if true, the wrap around flag is ignored. This is needed in the Replace All case.
	 * @return true if the search string from the dialog was found in the text pane considering the options of the {@link JSearchReplaceDialog}, false otherwise
	 */
	public boolean findString(boolean ignoreWrapAroundFlag);
	
	/**
	 * Determines if the search pattern is already selected in the {@link JTextPane}
	 * @return true if the pattern is selected in the {@link JTextPane}, false otherwise
	 */
	public boolean isSearchPatternSelected();
	
	/**
	 * Replaces the selected text in the {@link JTextPane} with the replacement text
	 * @return true if the replacement was successful, false otherwise
	 */
	public boolean replaceText();
	
	/**
	 * Sets the caret position of the {@link JTextPane} depending on the option fromStart to prepare for a Replace All command
	 */
	public void resetCaretPosition();
	
	/**
	 * Checks if the given pattern is a valid advanced search pattern and then converts it into an equivalent regex pattern
	 */
	public void convertPatternToRegex();
	
	/**
	 * Sets the status of the JSearchReplaceDialog
	 * @param text the message to display
	 * @param status the {@Link SearchStatus}
	 */
	public void setStatus(String text, int status);
}
