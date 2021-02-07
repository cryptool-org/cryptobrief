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
import sunset.gui.search.logic.ReplaceContext;
import sunset.gui.search.logic.ReplaceLogic;
import sunset.gui.search.logic.SearchContext;
import sunset.gui.search.logic.SearchLogic;
import sunset.gui.search.logic.interfaces.IMatcherLogic;
import sunset.gui.search.logic.interfaces.IReplaceLogic;
import sunset.gui.search.logic.interfaces.ISearchLogic;
import sunset.gui.search.exception.SearchIndexOutOfBoundsException;
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
				found = searchLogic.searchRegex(context, wrapAround, _dialog.dotMatchesNewLine());
			} else if (_dialog.useAdvancedSearch()){				
				found = searchLogic.searchAdvanced(context, wrapAround, getMatchingPairs(), _dialog.showBalancingError());
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
					// search pattern not found and an error occurred
					if (searchLogic.getStart() != -1 && searchLogic.getEnd() != -1) {
						// if the error was an unbalanced string error, select the unbalanced string
						textPane.setCaretPosition(searchLogic.getStart());
						textPane.moveCaretPosition(searchLogic.getEnd());
					}
					setStatus(searchLogic.getMessage(), SearchStatus.FAILURE);
				} else {	// search pattern not found and no error occurred
					setStatus(searchLogic.getMessage() + getLineNumber(doc, caretPos), SearchStatus.FAILURE);
				}
			}
		} catch (BadLocationException | SearchIndexOutOfBoundsException e) {
			setStatus(e.getMessage(), SearchStatus.FAILURE);
		}
		
		return false;
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
			return matcherLogic.matchesRegex(selectedText, pattern, matchCase, _dialog.dotMatchesNewLine());
		} else if (_dialog.useAdvancedSearch()){
			return matcherLogic.matchesAdvanced(selectedText, pattern, matchCase, getMatchingPairs());
		} else {
			return matcherLogic.equals(selectedText, pattern, matchCase);
		}
	}
	
	@Override
	public boolean replaceText() {
		String selectedText = getTextPane().getSelectedText();
		
		if (selectedText == null) {
			return false;
		}
		
		IReplaceLogic replaceLogic = new ReplaceLogic();
		String pattern = handleEscapes(_dialog.searchPattern());
		String replace = handleEscapes(_dialog.replaceText());
		boolean matchCase = _dialog.matchCase();
		ReplaceContext context = new ReplaceContext(selectedText, pattern, replace, matchCase);
		
		try {
			if (_dialog.useRegEx()) {
				replace = replaceLogic.replaceRegex(context, _dialog.dotMatchesNewLine());
			} else if (_dialog.useAdvancedSearch()) {
				replace = replaceLogic.replaceAdvanced(context, getMatchingPairs(), _dialog.showBalancingError());
			}
		} catch (Exception e) {
			setStatus(e.getMessage(), SearchStatus.FAILURE);
			return false;
		}
		
		if (replace == null) {
			return false;
		} else {		
			getTextPane().replaceSelection(replace);
			return true;
		}
	}
	
	@Override
	public void setStatus(String message, int status) {
		Color color = Color.black;
		
		if (status == SearchStatus.FAILURE) {
			color = Color.red;
		} else if (status == SearchStatus.SEARCH_SUCCESS) {
			color = Color.black;
		} else if (status == SearchStatus.REPLACE_SUCCESS) {
			color = Color.blue;
		}
		
		_dialog.setStatus(message, color);
	}
	
	/**
	 * Gets the text pane of the main frame
	 * @return The JTextPane of the main frame
	 */
	private JTextPane getTextPane() {
		return FFaplJFrame.getCurrentCodePanel().getCodePane();
	}
	
	/**
	 * Calculates the line number from the caret position inside a document
	 * @param doc The subject document
	 * @param position The current caret position
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
	
	/**
	 * Gets the matching pair configuration string from properties class
	 * @return the value of the matching pair configuration
	 */
	private String getMatchingPairs() {
		return GUIPropertiesLogic.getInstance().getProperty(IProperties.GUI_SEARCH_PAIRS);
	}
	
	/**
	 * Depending on the state of the dialog option useSpecialSymbols treats \r, \n and \t as symbols or as text (escaped)
	 * @param s The subject string
	 * @return The modified string
	 */
	private String handleEscapes(String s) {
		return _dialog.useSpecialSymbols() ? s.replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t") : s;
	}
}