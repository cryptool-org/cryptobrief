package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.SearchStatus;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

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
			_coordinator.setStatus("Replace All: " + count + " occurrence" + (count != 1 ? "s" : "") + " replaced", SearchStatus.REPLACE_SUCCESS);
		}
	}
}
