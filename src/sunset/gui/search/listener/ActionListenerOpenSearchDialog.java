package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchReplaceDialogOwner;

public class ActionListenerOpenSearchDialog implements ActionListener {
	private SearchReplaceDialogOwner _owner;
	
	public ActionListenerOpenSearchDialog(SearchReplaceDialogOwner owner) {
		_owner = owner;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		_owner.showDialog(false);
	}
}
