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
	public boolean findString(boolean bIgnoreWrapAroundFlag) {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			try {
				JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
				Document doc = textPaneCode.getDocument();
				String text = doc.getText(0, doc.getLength());
				int caretPos = textPaneCode.getCaretPosition();
				
				String pattern = handleEscapes(_dialog.searchPattern());
				
				boolean bMatchCase = _dialog.matchCase();
				boolean bWrapAround = _dialog.wrapAround() && !bIgnoreWrapAroundFlag;
				boolean bDotAll = _dialog.dotMatchesNewLine();
				boolean bFound;
				
				if (_dialog.useRegEx()) {
					bFound = _logic.searchRegex(text, pattern, caretPos, bMatchCase, bWrapAround, bDotAll);
				} else if (_dialog.useAdvancedSearch()){
					bFound = _logic.searchAdvanced(text, pattern, caretPos, bMatchCase, bWrapAround);
				} else {
					bFound = _logic.search(text, pattern, caretPos, bMatchCase, bWrapAround);
				}
				
				if (bFound) {
					if (_logic.getStart() == _logic.getEnd()) {
						setStatus("Zero length match!", SearchStatus.FAILURE);
						return false;
					}
					
					textPaneCode.setCaretPosition(_logic.getStart());
					textPaneCode.moveCaretPosition(_logic.getEnd());
					
					setStatus(_logic.getMessage() + " at line " + getLineNumber(doc, _logic.getStart()), SearchStatus.SEARCH_SUCCESS);
					
					return true;
				} else {
					setStatus(_logic.getMessage() + " at line " + getLineNumber(doc, caretPos), SearchStatus.FAILURE);
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
			String pattern = _dialog.searchPattern();
			boolean bMatchCase = _dialog.matchCase();
			
			if (selectedText == null) {
				return false;
			}
			
			if (_dialog.useRegEx()) {		
				boolean bDotAll = _dialog.dotMatchesNewLine();
				
				return _logic.matchesRegex(selectedText, pattern, bMatchCase, bDotAll);
			} else {
				return _logic.equals(selectedText, pattern, bMatchCase);
			}
		}
		
		return false;
	}
	
	@Override
	public boolean replaceText() {
		if (FFaplJFrame.getCurrentCodePanel() != null) {
			JTextPane textPaneCode = FFaplJFrame.getCurrentCodePanel().getCodePane();
			String replaceText = handleEscapes(_dialog.replaceText());
			
			if (_dialog.useRegEx()) {
				String pattern = handleEscapes(_dialog.searchPattern());
				String selectedText = textPaneCode.getSelectedText();
				try {
					boolean bMatchCase = _dialog.matchCase();
					boolean bDotAll = _dialog.dotMatchesNewLine();
					
					replaceText = _logic.replaceRegex(selectedText, pattern, replaceText, bMatchCase, bDotAll);
				} catch (Exception e) {
					setStatus(e.getMessage(), SearchStatus.FAILURE);
					return false;
				}
			}
			
			textPaneCode.replaceSelection(replaceText);
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