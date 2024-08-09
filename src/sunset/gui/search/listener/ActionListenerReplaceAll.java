package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchStatus;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;
import sunset.gui.search.util.SearchReplaceMessageHandler;

public class ActionListenerReplaceAll implements ActionListener {
	private ISearchReplaceCoordinator _coordinator;
	
	public ActionListenerReplaceAll(ISearchReplaceCoordinator coordinator) {
		_coordinator = coordinator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int count = 0;
		
		_coordinator.resetCaretPosition();
		
		while (_coordinator.findString(true)) {
			if (_coordinator.replaceText()) {
				count++;
			} else {
				break;
			}
		}
		
		if (count > 0) {
			// if at least one replacement was done, overwrite the last message from search/replace with according information
			String msg = SearchReplaceMessageHandler.getInstance().getMessage("replaceall_success", String.valueOf(count));
			_coordinator.setStatus(msg, SearchStatus.REPLACE_SUCCESS);
		}
	}
}
