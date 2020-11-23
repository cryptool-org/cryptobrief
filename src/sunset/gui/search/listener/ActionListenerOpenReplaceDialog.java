package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchReplaceDialogOwner;

public class ActionListenerOpenReplaceDialog implements ActionListener {
	private SearchReplaceDialogOwner _searchReplaceDialogOwner;
	
	public ActionListenerOpenReplaceDialog(SearchReplaceDialogOwner owner) {
		_searchReplaceDialogOwner = owner;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		_searchReplaceDialogOwner.showDialog(true);
	}
}
