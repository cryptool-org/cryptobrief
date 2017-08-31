/**
 * 
 */
package sunset.gui.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Close a Window on click
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerCloseWindow implements ActionListener {

	private Window _dialog;

	/**
	 * 
	 * @param owner
	 */
	public ActionListenerCloseWindow(Window owner) {
		_dialog = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_dialog.setVisible(false);
		_dialog.dispose();		
	}

}
