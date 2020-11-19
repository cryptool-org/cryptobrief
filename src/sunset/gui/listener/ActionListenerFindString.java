package sunset.gui.listener;

import java.awt.event.ActionEvent;
import sunset.gui.dialog.JDialogSearchReplace;

public class ActionListenerFindString extends ActionListenerFindReplace {

	public ActionListenerFindString(JDialogSearchReplace dialogSearchReplace) {
		super(dialogSearchReplace);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		findString();
	}
}