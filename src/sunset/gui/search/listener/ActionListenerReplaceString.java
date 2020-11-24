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
			boolean success = _coordinator.replaceText();
			
			if (success) {
				boolean found = _coordinator.findString(false);
				
				if (found) {
					_coordinator.setStatus("Replace: 1 occurrence replaced, next occurrence found", SearchStatus.REPLACE_SUCCESS);
				} else {
					_coordinator.setStatus("Replace: 1 occurrence replaced, no further occurrences found", SearchStatus.REPLACE_SUCCESS);
				}
			}
		} else {
			_coordinator.findString(false);
		}
	}
}