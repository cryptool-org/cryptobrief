/**
 * 
 */
package sunset.gui.listener;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogAbout;



/**
 * Opens a Dialog
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerAbout implements ActionListener {

	private FFaplJFrame _frame;
	private JDialogAbout _dialog;

	/**
	 * 
	 * @param frame
	 */
	public ActionListenerAbout(FFaplJFrame frame) {
		_frame = frame;
		_dialog = new JDialogAbout(_frame);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		_dialog.reInit();
		_dialog.setModal(true);
		_dialog.setVisible(true);		
	}

}
