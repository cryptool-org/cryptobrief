package sunset.gui.search.interfaces;

import javax.swing.JFrame;

public interface ISearchReplaceShowDialog {
	
	/**
	 * Prepares the SearchReplaceDialog for replace or search mode and then shows the dialog
	 * @param replaceMode The flag indicating if the dialog should be opened in replace mode
	 * @param frame The owner frame of the dialog
	 */
	public void prepareAndShowDialog(boolean replaceMode, JFrame frame);
	
	/**
	 * Enables or disables the search and replace buttons
	 * @param enable The flag indicating if the buttons should be enabled
	 */
	public void enableDisableButtons(boolean enable);
}