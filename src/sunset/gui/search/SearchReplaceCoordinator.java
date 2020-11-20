package sunset.gui.search;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import sunset.gui.FFaplJFrame;
import sunset.gui.dialog.JDialogSearchReplace;
import sunset.gui.interfaces.IDialogSearchReplace;
import sunset.gui.interfaces.ISearchLogic;
import sunset.gui.interfaces.ISearchReplaceCoordinator;

public class SearchReplaceCoordinator implements ISearchReplaceCoordinator {
	private IDialogSearchReplace _dialogSearchReplace;
	private ISearchLogic  		 _searchLogic;
	
	public SearchReplaceCoordinator(IDialogSearchReplace dialogSearchReplace) {
		_dialogSearchReplace = dialogSearchReplace;
		_searchLogic = new SearchLogic();
	}
	
	@Override
	public void resetCaretPosition() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			textPaneCode.setCaretPosition(0);
		}
	}
	
	@Override
	public boolean findString() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
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
					bFound = _searchLogic.searchRegex(text, pattern, caretPos, bMatchCase, bWrapAround, bDotAll);
				} else {
					bFound = _searchLogic.search(text, pattern, caretPos, bMatchCase, bWrapAround);
				}
				
				if (bFound) {
					textPaneCode.setCaretPosition(_searchLogic.getStart());
					textPaneCode.moveCaretPosition(_searchLogic.getEnd());
					
					_dialogSearchReplace.setStatus(_searchLogic.getMessage() + getLineNumber(doc, _searchLogic.getStart()), Color.black);
					
					return true;
				} else {
					_dialogSearchReplace.setStatus(_searchLogic.getMessage() + getLineNumber(doc, caretPos), Color.red);
				}
			} catch (BadLocationException e1) {
				_dialogSearchReplace.setStatus(e1.getMessage(), Color.red);
			}
		}
		
		return false;
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
	
	@Override
	public boolean isSearchPatternSelected() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			String pattern = _dialogSearchReplace.searchPattern();
			String selectedText = textPaneCode.getSelectedText();
			boolean bMatchCase = _dialogSearchReplace.matchCase();
			
			if (selectedText == null) {
				return false;
			}
			
			if (_dialogSearchReplace.useRegEx()) {		
				boolean bDotAll = _dialogSearchReplace.dotMatchesNewLine();
				
				return _searchLogic.matchesRegex(selectedText, pattern, bMatchCase, bDotAll);
			} else {
				return _searchLogic.equals(selectedText, pattern, bMatchCase);
			}
		}
		
		return false;
	}
	
	@Override
	public void replaceText() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
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
}