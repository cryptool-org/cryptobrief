package sunset.gui.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogSearchReplace;

public class ActionListenerSearchReplace implements ActionListener {

	private FFaplJFrame _frame;
	private JDialogSearchReplace searchReplaceDialog;
	private Vector<Component> replaceComp;
	
	public ActionListenerSearchReplace(FFaplJFrame frame, Vector<Component> replaceComp) {
		this._frame = frame;
		this.replaceComp = replaceComp;
		searchReplaceDialog = new JDialogSearchReplace(_frame);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean isReplaceAction = replaceComp.contains(e.getSource());
		searchReplaceDialog.prepareDialog(isReplaceAction);
		searchReplaceDialog.setVisible(true);
	}
	
	public JDialogSearchReplace getSearchReplaceDialog() {
		return searchReplaceDialog;
	}

}
