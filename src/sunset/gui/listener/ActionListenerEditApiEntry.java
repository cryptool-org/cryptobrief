/**
 * 
 */
package sunset.gui.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import sunset.gui.FFaplJFrame;
import sunset.gui.api.spec.Snippet;
import sunset.gui.dialog.JDialogAPI;
import sunset.gui.dialog.JDialogAPICode;

/**
 * Close a Window on click
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerEditApiEntry implements ActionListener {

	private FFaplJFrame _dialog;
	private Snippet snippet;
	private JDialogAPICode codeDialog;
	private JDialog subDialog;

	/**
	 * 
	 * @param owner
	 */
	public ActionListenerEditApiEntry(FFaplJFrame owner, JDialog subDialog) {
		_dialog = owner;
		this.subDialog = subDialog;
		codeDialog = new JDialogAPICode(_dialog, snippet);
		
	}
	
	public void setSnippet(Snippet snippet){
		this.snippet = snippet;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		codeDialog.reInit(snippet);
		codeDialog.setVisible(true);
		if(subDialog != null){
			subDialog.dispose();
		}
		
	}
		

}
