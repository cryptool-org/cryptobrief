package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchStatus;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;
import sunset.gui.search.util.SearchReplaceMessageHandler;

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
				
				String msg = "";
				
				if (found) {
					msg = SearchReplaceMessageHandler.getInstance().getMessage("replace_done_nextfound");
				} else {
					msg = SearchReplaceMessageHandler.getInstance().getMessage("replace_done_nofurtherfound");
				}
				
				_coordinator.setStatus(msg, SearchStatus.REPLACE_SUCCESS);
			}
		} else {
			_coordinator.findString(false);
		}
	}
}