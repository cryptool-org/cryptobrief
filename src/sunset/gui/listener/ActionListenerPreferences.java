/**
 * 
 */
package sunset.gui.listener;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogPreference;



/**
 * Opens a Dialog
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerPreferences implements ActionListener {

	private FFaplJFrame _frame;

	/**
	 * 
	 * @param frame
	 */
	public ActionListenerPreferences(FFaplJFrame frame) {
		_frame = frame;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		JDialog dialog = new JDialogPreference(_frame);
		dialog.setModal(true);
		dialog.setVisible(true);		
	}

}
