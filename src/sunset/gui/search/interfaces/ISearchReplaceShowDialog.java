package sunset.gui.search.interfaces;

public interface ISearchReplaceShowDialog {
	/**
	 * Prepares the JSearchReplaceDialog for the next search or replacement
	 * @param replaceMode specifies if the dialog should be opened in replacement mode
	 */
	public void prepareDialog(boolean replaceMode);
	
	/**
	 * Sets the visibility flag of the JDialog
	 * @param visible the flag specifying if the dialog should be visible
	 */
	public void setVisible(boolean visible);
}