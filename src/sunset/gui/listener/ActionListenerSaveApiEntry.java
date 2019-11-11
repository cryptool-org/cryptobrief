/**
 * 
 */
package sunset.gui.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import sunset.gui.dialog.JDialogAPICode;

/**
 * Close a Window on click
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerSaveApiEntry implements ActionListener {

	private JDialogAPICode _dialog;

	/**
	 * 
	 * @param owner
	 */
	public ActionListenerSaveApiEntry(JDialogAPICode owner) {
		_dialog = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			_dialog.saveApiEntry();
			_dialog.dispose();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
