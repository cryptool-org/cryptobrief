package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchStatus;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

public class ActionListenerReplaceAll implements ActionListener {
	private ISearchReplaceCoordinator _searchReplaceCoordinator;
	
	public ActionListenerReplaceAll(ISearchReplaceCoordinator coordinator) {
		_searchReplaceCoordinator = coordinator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int count = 0;
		
		_searchReplaceCoordinator.resetCaretPosition();
		
		while (_searchReplaceCoordinator.findString()) {
			_searchReplaceCoordinator.replaceText();
			count++;
		}
		
		_searchReplaceCoordinator.setStatus("Replace All: " + count + " occurrences were replaced", SearchStatus.REPLACE_SUCCESS);
	}
}
