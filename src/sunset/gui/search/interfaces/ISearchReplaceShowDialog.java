package sunset.gui.search.interfaces;

import javax.swing.JFrame;

public interface ISearchReplaceShowDialog {
	public void prepareAndShowDialog(boolean replaceMode, JFrame frame);
	public void enableButtons(boolean enable);
}