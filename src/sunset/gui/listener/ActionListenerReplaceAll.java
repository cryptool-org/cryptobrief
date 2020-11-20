package sunset.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.interfaces.ISearchReplaceCoordinator;

public class ActionListenerReplaceAll implements ActionListener {
	private ISearchReplaceCoordinator _searchReplaceCoordinator;
	
	public ActionListenerReplaceAll(ISearchReplaceCoordinator coordinator) {
		_searchReplaceCoordinator = coordinator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_searchReplaceCoordinator.resetCaretPosition();
		
		while (_searchReplaceCoordinator.findString()) {
			_searchReplaceCoordinator.replaceText();
		}
	}
}
