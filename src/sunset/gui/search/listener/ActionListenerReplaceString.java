package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchStatus;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

public class ActionListenerReplaceString implements ActionListener {
	private ISearchReplaceCoordinator _coordinator;
	
	public ActionListenerReplaceString(ISearchReplaceCoordinator coordinator) {
		_coordinator = coordinator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_coordinator.isSearchPatternSelected()) {
			if (_coordinator.replaceText()) {
				boolean found = _coordinator.findString(false);
				
				_coordinator.setStatus("Replace: 1 occurrence replaced, " + 
				(found ? "next occurrence found" : "no further occurrences found"), SearchStatus.REPLACE_SUCCESS);
			}
		} else {
			_coordinator.findString(false);
		}
	}
}