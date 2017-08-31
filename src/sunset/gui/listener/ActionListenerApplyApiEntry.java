/**
 * 
 */
package sunset.gui.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import sunset.gui.dialog.JDialogAPICode;

/**
 * Close a Window on click
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerApplyApiEntry implements ActionListener {

	private JDialogAPICode _dialog;

	/**
	 * 
	 * @param owner
	 */
	public ActionListenerApplyApiEntry(JDialogAPICode owner) {
		_dialog = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			_dialog.saveApiEntry();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
