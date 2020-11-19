package sunset.gui.listener;

import java.awt.Color;
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

public abstract class ActionListenerFindReplace implements ActionListener {
	protected IDialogSearchReplace _dialogSearchReplace;
	protected ISearchReplaceLogic  _searchReplace = null;
	
	public ActionListenerFindReplace(JDialogSearchReplace dialogSearchReplace) {
		_dialogSearchReplace = dialogSearchReplace;
		
		if (_searchReplace == null) {
			_searchReplace = new SearchReplaceLogic();
		}
	}
	
	protected void findString() {
		try {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			Document doc = textPaneCode.getDocument();
			String text = doc.getText(0, doc.getLength());
			int caretPos = textPaneCode.getCaretPosition();
			
			String pattern = _dialogSearchReplace.searchPattern();
			boolean bMatchCase = _dialogSearchReplace.matchCase();
			boolean bWrapAround = _dialogSearchReplace.wrapAround();
			boolean bDotAll = _dialogSearchReplace.dotMatchesNewLine();
			boolean bFound;
			
			if (_dialogSearchReplace.useRegEx()) {
				bFound = _searchReplace.searchRegex(text, pattern, caretPos, bMatchCase, bWrapAround, bDotAll);
			} else {
				bFound = _searchReplace.search(text, pattern, caretPos, bMatchCase, bWrapAround);
			}
			
			if (bFound) {
				textPaneCode.setCaretPosition(_searchReplace.getStart());
				textPaneCode.moveCaretPosition(_searchReplace.getEnd());
				
				_dialogSearchReplace.setStatus(_searchReplace.getMessage() + getLineNumber(doc, _searchReplace.getStart()), Color.black);
			} else {
				_dialogSearchReplace.setStatus(_searchReplace.getMessage() + getLineNumber(doc, caretPos), Color.red);
			}
		} catch (BadLocationException e1) {
			_dialogSearchReplace.setStatus(e1.getMessage(), Color.red);
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
	
	protected boolean isSearchPatternSelected() {
		JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
		String pattern = _dialogSearchReplace.searchPattern();
		String selectedText = textPaneCode.getSelectedText();
		boolean bMatchCase = _dialogSearchReplace.matchCase();
		
		if (selectedText == null) {
			return false;
		}
		
		if (_dialogSearchReplace.useRegEx()) {		
			boolean bDotAll = _dialogSearchReplace.dotMatchesNewLine();
			
			return _searchReplace.matchesRegex(selectedText, pattern, bMatchCase, bDotAll);
		} else {
			return _searchReplace.equals(selectedText, pattern, bMatchCase);
		}
	}
	
	protected void replaceText() {
		JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
		String replaceText = _dialogSearchReplace.replaceText();
		
		if (_dialogSearchReplace.useRegEx()) {
			String pattern = _dialogSearchReplace.searchPattern();
			String selectedText = textPaneCode.getSelectedText();
			String result = selectedText.replaceAll(pattern, replaceText);
			
			textPaneCode.replaceSelection(result);	
		} else {
			textPaneCode.replaceSelection(replaceText);			
		}		
	}
}