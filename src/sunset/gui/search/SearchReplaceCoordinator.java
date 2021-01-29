package sunset.gui.search;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import sunset.gui.FFaplJFrame;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;
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
		if (_dialog.replaceAllFromStart()) {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			textPaneCode.setCaretPosition(0);
		}
	}
	
	@Override
	public boolean findString(boolean ignoreWrapAroundFlag) {
		try {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			Document doc = textPaneCode.getDocument();
			String text = doc.getText(0, doc.getLength());
			int caretPos = textPaneCode.getCaretPosition();
			
			String pattern = handleEscapes(_dialog.searchPattern());
			
			boolean matchCase = _dialog.matchCase();
			boolean wrapAround = _dialog.wrapAround() && !ignoreWrapAroundFlag;
			boolean found;
			
			if (_dialog.useRegEx()) {
				boolean dotAll = _dialog.dotMatchesNewLine();
				
				found = _logic.searchRegex(text, pattern, caretPos, matchCase, wrapAround, dotAll);
			} else if (_dialog.useAdvancedSearch()){
				String matchingPairs = getMatchingPairs();
				boolean showBalancingError = _dialog.showBalancingError();
				
				found = _logic.searchAdvanced(text, pattern, matchingPairs, caretPos, matchCase, wrapAround, showBalancingError);
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
				if (_logic.getError()) {
					if (_logic.getStart() != -1 && _logic.getEnd() != -1) {
						textPaneCode.setCaretPosition(_logic.getStart());
						textPaneCode.moveCaretPosition(_logic.getEnd());
					}
					setStatus(_logic.getMessage(), SearchStatus.FAILURE);
				} else {
					setStatus(_logic.getMessage() + getLineNumber(doc, caretPos), SearchStatus.FAILURE);
				}
			}
		} catch (BadLocationException e) {
			setStatus(e.getMessage(), SearchStatus.FAILURE);
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
			String matchingPairs = getMatchingPairs();
			
			return _logic.matchesAdvanced(selectedText, pattern, matchingPairs, matchCase);
		} else {
			return _logic.equals(selectedText, pattern, matchCase);
		}
	}
	
	@Override
	public boolean replaceText() {
		JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
		String pattern = handleEscapes(_dialog.searchPattern());
		String replace = handleEscapes(_dialog.replaceText());
		String selectedText = textPaneCode.getSelectedText();
		
		if (selectedText == null) {
			return false;
		}
		
		boolean matchCase = _dialog.matchCase();
		boolean dotAll = _dialog.dotMatchesNewLine();
		boolean showBalancingError = _dialog.showBalancingError();
		
		if (_dialog.useRegEx()) {
			try {		
				replace = _logic.replaceRegex(selectedText, pattern, replace, matchCase, dotAll);
			} catch (Exception e) {
				setStatus(e.getMessage(), SearchStatus.FAILURE);
				return false;
			}
		} else if (_dialog.useAdvancedSearch()) {
			try {
				String matchingPairs = getMatchingPairs();
				replace = _logic.replaceAdvanced(selectedText, pattern, replace, matchingPairs, matchCase, showBalancingError);
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
	
	private String getMatchingPairs() {
		return GUIPropertiesLogic.getInstance().getProperty(IProperties.GUI_SEARCH_PAIRS);
	}
	
	private String handleEscapes(String s) {
		return _dialog.useSpecialSymbols() ? s.replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t") : s;
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