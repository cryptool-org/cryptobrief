package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

public class ActionListenerFindString implements ActionListener {
	private ISearchReplaceCoordinator _coordinator;

	public ActionListenerFindString(ISearchReplaceCoordinator coordinator) {
		_coordinator = coordinator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_coordinator.findString(false);
	}
}