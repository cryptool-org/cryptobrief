package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchStatus;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

public class ActionListenerReplaceString implements ActionListener {
	private ISearchReplaceCoordinator _searchReplaceCoordinator;
	
	public ActionListenerReplaceString(ISearchReplaceCoordinator coordinator) {
		_searchReplaceCoordinator = coordinator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_searchReplaceCoordinator.isSearchPatternSelected()) {
			_searchReplaceCoordinator.replaceText();
			boolean found = _searchReplaceCoordinator.findString();
			
			if (found) {
				_searchReplaceCoordinator.setStatus("Replace: 1 occurrence replaced, next occurrence found", SearchStatus.REPLACE_SUCCESS);
			} else {
				_searchReplaceCoordinator.setStatus("Replace: 1 occurrence replaced, no further occurrences", SearchStatus.REPLACE_SUCCESS);
			}
		} else {
			_searchReplaceCoordinator.findString();
		}
	}
}