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
import sunset.gui.search.logic.MatcherLogic;
import sunset.gui.search.logic.ReplaceLogic;
import sunset.gui.search.logic.SearchContext;
import sunset.gui.search.logic.SearchLogic;
import sunset.gui.search.logic.interfaces.IMatcherLogic;
import sunset.gui.search.logic.interfaces.IReplaceLogic;
import sunset.gui.search.logic.interfaces.ISearchLogic;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;

public class SearchReplaceCoordinator implements ISearchReplaceCoordinator {
	private ISearchReplaceDialog _dialog;
	
	public SearchReplaceCoordinator(ISearchReplaceDialog dialogSearchReplace) {
		_dialog = dialogSearchReplace;
	}
	
	@Override
	public void resetCaretPosition() {
		if (_dialog.replaceAllFromStart()) {
			getTextPane().setCaretPosition(0);
		} else {
			int start = getTextPane().getSelectionStart();
			getTextPane().setCaretPosition(start);
		}
	}
	
	private JTextPane getTextPane() {
		return FFaplJFrame.getCurrentCodePanel().getCodePane();
	}
	
	@Override
	public boolean findString(boolean ignoreWrapAroundFlag) {
		try {
			JTextPane textPane = getTextPane();
			Document doc = textPane.getDocument();
			String text = doc.getText(0, doc.getLength());
			int caretPos = textPane.getCaretPosition();
			
			ISearchLogic searchLogic = new SearchLogic();
			String pattern = handleEscapes(_dialog.searchPattern());
			boolean matchCase = _dialog.matchCase();
			boolean wrapAround = _dialog.wrapAround() && !ignoreWrapAroundFlag;
			SearchContext context = new SearchContext(text, pattern, caretPos, matchCase);
			boolean found;
			
			if (_dialog.useRegEx()) {
				boolean dotAll = _dialog.dotMatchesNewLine();
				
				found = searchLogic.searchRegex(context, wrapAround, dotAll);
			} else if (_dialog.useAdvancedSearch()){
				String matchingPairs = getMatchingPairs();
				boolean showBalancingError = _dialog.showBalancingError();
				
				found = searchLogic.searchAdvanced(context, wrapAround, matchingPairs, showBalancingError);
			} else {
				found = searchLogic.search(context, wrapAround);
			}
			
			if (found) {
				textPane.setCaretPosition(searchLogic.getStart());
				textPane.moveCaretPosition(searchLogic.getEnd());
				
				setStatus(searchLogic.getMessage() + getLineNumber(doc, searchLogic.getStart()), SearchStatus.SEARCH_SUCCESS);
				
				return true;
			} else {
				if (searchLogic.getError()) {
					if (searchLogic.getStart() != -1 && searchLogic.getEnd() != -1) {
						textPane.setCaretPosition(searchLogic.getStart());
						textPane.moveCaretPosition(searchLogic.getEnd());
					}
					setStatus(searchLogic.getMessage(), SearchStatus.FAILURE);
				} else {
					setStatus(searchLogic.getMessage() + getLineNumber(doc, caretPos), SearchStatus.FAILURE);
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
		String selectedText = getTextPane().getSelectedText();
		
		if (selectedText == null) {
			return false;
		}
		
		IMatcherLogic matcherLogic = new MatcherLogic();
		String pattern = handleEscapes(_dialog.searchPattern());
		boolean matchCase = _dialog.matchCase();
		
		if (_dialog.useRegEx()) {		
			boolean dotAll = _dialog.dotMatchesNewLine();
			
			return matcherLogic.matchesRegex(selectedText, pattern, matchCase, dotAll);
		} else if (_dialog.useAdvancedSearch()){
			String matchingPairs = getMatchingPairs();
			
			return matcherLogic.matchesAdvanced(selectedText, pattern, matchingPairs, matchCase);
		} else {
			return matcherLogic.equals(selectedText, pattern, matchCase);
		}
	}
	
	@Override
	public boolean replaceText() {
		JTextPane textPane = getTextPane();
		String selectedText = textPane.getSelectedText();
		
		if (selectedText == null) {
			return false;
		}
		
		IReplaceLogic replaceLogic = new ReplaceLogic();
		String pattern = handleEscapes(_dialog.searchPattern());
		String replace = handleEscapes(_dialog.replaceText());
		boolean matchCase = _dialog.matchCase();
		boolean dotAll = _dialog.dotMatchesNewLine();
		boolean showBalancingError = _dialog.showBalancingError();
		
		if (_dialog.useRegEx()) {
			try {
				replace = replaceLogic.replaceRegex(selectedText, pattern, replace, matchCase, dotAll);
			} catch (Exception e) {
				setStatus(e.getMessage(), SearchStatus.FAILURE);
				return false;
			}
		} else if (_dialog.useAdvancedSearch()) {
			try {
				String matchingPairs = getMatchingPairs();
				replace = replaceLogic.replaceAdvanced(selectedText, pattern, replace, matchingPairs, matchCase, showBalancingError);
			} catch (UndeclaredVariableException e) {
				setStatus(e.getMessage(), SearchStatus.FAILURE);
				return false;
			}
		}
		
		if (replace == null) {
			setStatus(replaceLogic.getMessage(), SearchStatus.FAILURE);
			return false;
		}
		
		textPane.replaceSelection(replace);
		
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