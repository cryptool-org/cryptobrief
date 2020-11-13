package sunset.gui.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogSearchReplace;
import sunset.gui.logic.SearchReplaceLogic;

public class ActionListenerFindString implements ActionListener {

	private JDialogSearchReplace _dialogSearch;
	
	public ActionListenerFindString(JDialogSearchReplace dialogSearch) {
		_dialogSearch = dialogSearch;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String searchFor = _dialogSearch.getSearchString();
		String text;
		
		try {
			JTextPane textPaneCode = _dialogSearch.getFrame().getCurrentCodePanel().getCodePane();
			
			textPaneCode.grabFocus();
			Document doc = textPaneCode.getDocument();
			text = doc.getText(0, doc.getLength());
			int caretPos = textPaneCode.getCaretPosition();
			
			int foundIndex = SearchReplaceLogic.getIntance().getIndexOf(text, searchFor, caretPos);
			
			if (foundIndex != -1) {
				_dialogSearch.setStatus("\"" + searchFor + "\" found at position " + foundIndex, Color.black);
				textPaneCode.setCaretPosition(foundIndex);
				textPaneCode.moveCaretPosition(foundIndex + searchFor.length());
			} else {
				_dialogSearch.setStatus("\"" + searchFor + "\" not found from position " + caretPos, Color.red);
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
}