/**
 * 
 */
package sunset.gui.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import sunset.gui.FFaplJFrame;
import sunset.gui.api.spec.ApiEntry;
import sunset.gui.api.spec.Snippet;
import sunset.gui.dialog.JDialogAPI;
import sunset.gui.dialog.JDialogAPICode;
import sunset.gui.logic.ApiLogic;
import sunset.gui.util.SunsetBundle;

/**
 * Close a Window on click
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerDeleteApiEntry implements ActionListener {

	private FFaplJFrame _dialog;
	private Snippet snippet;

	/**
	 * 
	 * @param owner
	 * @param snippet 
	 */
	public ActionListenerDeleteApiEntry(FFaplJFrame owner, Snippet snippet) {
		_dialog = owner;
		this.snippet = snippet;
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String title = SunsetBundle.getInstance().getProperty("question_delete_title");
			String msg = SunsetBundle.getInstance().getProperty("question_delete");
			msg = MessageFormat.format(msg, "'" + snippet.getName() + "'");
			Integer answer = JOptionPane.showConfirmDialog(_dialog, msg, title, JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION){
				ApiLogic.getInstance().deleteSnippetCode(this.snippet);
				_dialog.initLanguage();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
		

}
