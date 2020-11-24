package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchReplaceDialogOwner;

public class ActionListenerOpenReplaceDialog implements ActionListener {
	private SearchReplaceDialogOwner _owner;
	
	public ActionListenerOpenReplaceDialog(SearchReplaceDialogOwner owner) {
		_owner = owner;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		_owner.showDialog(true);
	}
}
