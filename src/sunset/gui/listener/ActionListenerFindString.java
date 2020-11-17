package sunset.gui.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogSearchReplace;
import sunset.gui.interfaces.IDialogSearchReplace;
import sunset.gui.interfaces.ISearchReplaceLogic;
import sunset.gui.logic.SearchReplaceLogic;

public class ActionListenerFindString implements ActionListener {

	private IDialogSearchReplace _dialogSearch;
	private ISearchReplaceLogic  _searchReplace;
	
	public ActionListenerFindString(JDialogSearchReplace dialogSearchReplace) {
		_dialogSearch = dialogSearchReplace;
		_searchReplace = new SearchReplaceLogic();
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
			
			boolean bWrapAround = _dialogSearch.wrapAround();
			boolean bFound;
			
			if (_dialogSearch.useRegEx()) {
				boolean bDotAll = _dialogSearch.dotMatchesNewLine();
				bFound = _searchReplace.searchRegex(text, pattern, caretPos, bMatchCase, bWrapAround, bDotAll);
			} else {
				bFound = _searchReplace.search(text, pattern, caretPos, bMatchCase, bWrapAround);
			}
			
			if (bFound) {
				textPaneCode.setCaretPosition(_searchReplace.getStart());
				textPaneCode.moveCaretPosition(_searchReplace.getEnd());
				
				_dialogSearch.setStatus(_searchReplace.getMessage() + " in line " + getLineNumber(doc, _searchReplace.getStart()), Color.black);
			} else {
				_dialogSearch.setStatus(_searchReplace.getMessage(), Color.red);
			}
		} catch (BadLocationException e1) {
			_dialogSearch.setStatus(e1.getMessage(), Color.red);
		}
	}
	
	private int getLineNumber(Document doc, int position) {
		Element root, element;
		root = doc.getRootElements()[0];
		
		for(int i = 0; i < root.getElementCount(); i++) {
			element = root.getElement(i);
			
			if(element.getStartOffset() <= position && position < element.getEndOffset()) {
				return i+1;
			}
		}
		
		return 0;
	}
}