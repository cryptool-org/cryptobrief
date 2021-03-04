package sunset.gui.search.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import sunset.gui.dialog.JDialogSearchSettings;

public class ActionListenerOpenSettingsDialog implements ActionListener {
	private JDialogSearchSettings _dialogSettings;
	private JDialog _owner;
	
	public ActionListenerOpenSettingsDialog(JDialog owner) {
		_owner = owner;
		_dialogSettings = new JDialogSearchSettings(_owner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_dialogSettings.prepareAndSwowDialog(_owner);
	}

}
