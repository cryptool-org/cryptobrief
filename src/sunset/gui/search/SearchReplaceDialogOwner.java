package sunset.gui.search;

import javax.swing.JFrame;

import sunset.gui.dialog.JDialogSearchReplace;
import sunset.gui.search.interfaces.ISearchReplaceShowDialog;

public class SearchReplaceDialogOwner {
	private ISearchReplaceShowDialog _dialogSearchReplace;
	private JFrame _owner;
	
	public SearchReplaceDialogOwner(JFrame owner) {
		_owner = owner;
		_dialogSearchReplace = new JDialogSearchReplace(_owner);
	}
	
	public void setFileOpened(boolean fileOpen) {
		_dialogSearchReplace.enableDisableButtons(fileOpen);
	}
	
	public void showDialog(boolean replaceMode) {
		_dialogSearchReplace.prepareAndShowDialog(replaceMode, _owner);
	}
}