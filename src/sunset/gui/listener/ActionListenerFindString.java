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
import sunset.gui.interfaces.IDialogSearch;
import sunset.gui.logic.SearchReplaceLogic;

public class ActionListenerFindString implements ActionListener {

	private IDialogSearch _dialogSearch;
	
	public ActionListenerFindString(JDialogSearchReplace dialogSearch) {
		_dialogSearch = dialogSearch;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String pattern = _dialogSearch.searchPattern();
		boolean bMatchCase = _dialogSearch.matchCase();
		String text;
		
		try {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			Document doc = textPaneCode.getDocument();
			text = doc.getText(0, doc.getLength());
			int caretPos = textPaneCode.getCaretPosition();
			
			SearchReplaceLogic searchReplace = new SearchReplaceLogic();
			boolean bWrapAround = _dialogSearch.wrapAround();
			boolean bFound;
			
			if (_dialogSearch.useRegEx()) {
				boolean bDotMatchesNewLine = _dialogSearch.dotMatchesNewLine();
				bFound = searchReplace.searchRegex(text, pattern, caretPos, bMatchCase, bWrapAround, bDotMatchesNewLine);
			} else {
				bFound = searchReplace.search(text, pattern, caretPos, bMatchCase, bWrapAround);
			}
			
			if (bFound) {
				_dialogSearch.setStatus(searchReplace.getMessage(), Color.black);
				textPaneCode.setCaretPosition(searchReplace.getStart());
				textPaneCode.moveCaretPosition(searchReplace.getEnd());
			} else {
				_dialogSearch.setStatus(searchReplace.getMessage(), Color.red);
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
}