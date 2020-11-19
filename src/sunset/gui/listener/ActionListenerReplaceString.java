package sunset.gui.listener;

import java.awt.event.ActionEvent;

import sunset.gui.dialog.JDialogSearchReplace;

public class ActionListenerReplaceString extends ActionListenerFindReplace {

	public ActionListenerReplaceString(JDialogSearchReplace dialogSearchReplace) {
		super(dialogSearchReplace);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (isSearchPatternSelected()) {
			replaceText();
		} else {		
			findString();
		}
	}
}