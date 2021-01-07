package sunset.gui.search;

import java.awt.Color;
import java.util.regex.Matcher;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import sunset.gui.FFaplJFrame;
import sunset.gui.search.interfaces.ISearchReplaceDialog;
import sunset.gui.search.interfaces.ISearchReplaceLogic;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

public class SearchReplaceCoordinator implements ISearchReplaceCoordinator {
	private ISearchReplaceDialog _dialog;
	private ISearchReplaceLogic  _logic;
	
	public SearchReplaceCoordinator(ISearchReplaceDialog dialogSearchReplace) {
		_dialog = dialogSearchReplace;
		_logic = new SearchReplaceLogic();
	}
	
	@Override
	public void resetCaretPosition() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			textPaneCode.setCaretPosition(0);
		}
	}
	
	@Override
	public boolean findString(boolean ignoreWrapAroundFlag) {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			try {
				JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
				Document doc = textPaneCode.getDocument();
				String text = doc.getText(0, doc.getLength());
				int caretPos = textPaneCode.getCaretPosition();
				
				String pattern = handleEscapes(_dialog.searchPattern());
				
				boolean matchCase = _dialog.matchCase();
				boolean wrapAround = _dialog.wrapAround() && !ignoreWrapAroundFlag;
				boolean dotAll = _dialog.dotMatchesNewLine();
				boolean found;
				
				if (_dialog.useRegEx()) {
					found = _logic.searchRegex(text, pattern, caretPos, matchCase, wrapAround, dotAll);
				} else if (_dialog.useAdvancedSearch()){
					found = _logic.searchAdvanced(text, pattern, caretPos, matchCase, wrapAround);
				} else {
					found = _logic.search(text, pattern, caretPos, matchCase, wrapAround);
				}
				
				if (found) {
					textPaneCode.setCaretPosition(_logic.getStart());
					
					if (_logic.getStart() == _logic.getEnd()) {
						setStatus("Zero length match", SearchStatus.FAILURE);
						return false;
					}
					
					textPaneCode.moveCaretPosition(_logic.getEnd());
					
					setStatus(_logic.getMessage() + getLineNumber(doc, _logic.getStart()), SearchStatus.SEARCH_SUCCESS);
					
					return true;
				} else {
					setStatus(_logic.getMessage() + getLineNumber(doc, caretPos), SearchStatus.FAILURE);
				}
			} catch (BadLocationException e) {
				setStatus(e.getMessage(), SearchStatus.FAILURE);
			}
		} else {
			setStatus("No file opened", SearchStatus.FAILURE);
		}
		
		return false;
	}
	
	/**
	 * Calculates the line number from the caret position inside a document
	 * @param doc the subject document
	 * @param position the current caret position
	 * @return the corresponding line number
	 */
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
			String selectedText = textPaneCode.getSelectedText();
			
			if (selectedText == null) {
				return false;
			}
			
			String pattern = handleEscapes(_dialog.searchPattern());
			boolean matchCase = _dialog.matchCase();
			
			if (_dialog.useRegEx()) {		
				boolean dotAll = _dialog.dotMatchesNewLine();
				
				return _logic.matchesRegex(selectedText, pattern, matchCase, dotAll);
			} else if (_dialog.useAdvancedSearch()){
				return _logic.matchesAdvanced(selectedText, pattern, matchCase);
			} else {
				return _logic.equals(selectedText, pattern, matchCase);
			}
		}
		
		return false;
	}
	
	@Override
	public boolean replaceText() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			String pattern = handleEscapes(_dialog.searchPattern());
			String replace = handleEscapes(_dialog.replaceText());
			String selectedText = textPaneCode.getSelectedText();
			
			if (selectedText == null) {
				return false;
			}
			
			boolean matchCase = _dialog.matchCase();
			boolean dotAll = _dialog.dotMatchesNewLine();
			
			if (_dialog.useRegEx()) {
				try {		
					replace = _logic.replaceRegex(selectedText, pattern, replace, matchCase, dotAll);
				} catch (Exception e) {
					setStatus(e.getMessage(), SearchStatus.FAILURE);
					return false;
				}
			} else if (_dialog.useAdvancedSearch()) {
				try {
					replace = _logic.replaceAdvanced(selectedText, pattern, replace, matchCase);
				} catch (UndeclaredVariableException e) {
					setStatus(e.getMessage(), SearchStatus.FAILURE);
					return false;
				}
			}
			
			if (replace == null) {
				setStatus(_logic.getMessage(), SearchStatus.FAILURE);
				return false;
			}
			
			textPaneCode.replaceSelection(replace);
			return true;
		}
		
		return false;
	}
	
	private String handleEscapes(String s) {
		return s.replace("\\n", "\n").replace("\\t", "\t").replace("\\r", "\r").replace("\\b",  "\b");
	}
	
	@Override
	public void setStatus(String message, int status) {
		Color c = Color.black;
		
		if (status == SearchStatus.FAILURE) {
			c = Color.red;
		} else if (status == SearchStatus.SEARCH_SUCCESS) {
			c = Color.black;
		} else if (status == SearchStatus.REPLACE_SUCCESS) {
			c = Color.blue;
		}
		
		_dialog.setStatus(message, c);
	}
}