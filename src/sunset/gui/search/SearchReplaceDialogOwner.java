package sunset.gui.search;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogSearchReplace;
import sunset.gui.search.interfaces.ISearchReplaceShowDialog;

public class SearchReplaceDialogOwner {
	private ISearchReplaceShowDialog _dialogSearchReplace;
	
	public SearchReplaceDialogOwner(FFaplJFrame frame) {
		_dialogSearchReplace = new JDialogSearchReplace(frame);
	}
	
	public void showDialog(boolean replaceMode) {
		_dialogSearchReplace.prepareDialog(replaceMode);
		_dialogSearchReplace.setVisible(true);
	}
}