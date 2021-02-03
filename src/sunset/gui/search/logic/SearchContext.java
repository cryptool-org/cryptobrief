package sunset.gui.search.logic;

import sunset.gui.search.exception.SearchIndexOutOfBoundsException;
import sunset.gui.search.util.SearchReplaceMessageHandler;

public class SearchContext {
	private String _text;
	private String _pattern;
	private int _fromIndex;
	private boolean _matchCase;
	
	public SearchContext(String text, String pattern, int fromIndex, boolean matchCase) throws SearchIndexOutOfBoundsException {
		_text = text;
		_pattern = pattern;
		_fromIndex = fromIndex;
		_matchCase = matchCase;
		
		if (fromIndex < 0 || fromIndex > text.length()) {
			String msg = SearchReplaceMessageHandler.getInstance().
					getMessage("exception_searchindexoutofbounds", String.valueOf(fromIndex));
			throw new SearchIndexOutOfBoundsException(msg);
		}
	}
	
	public String toString() {
		return (_text.length() < 20 ? "Text: " + _text : "") 
				+ "\tPattern: " + _pattern + "\tfromIndex: " + _fromIndex + "\tmatchCase: " + _matchCase;
	}
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		this._text = text;
	}
	
	public String getPattern() {
		return _pattern;
	}
	
	public void setPattern(String pattern) {
		this._pattern = pattern;
	}
	
	public int getFromIndex() {
		return _fromIndex;
	}
	
	public void setFromIndex(int fromIndex) {
		this._fromIndex = fromIndex;
	}
	
	public boolean isMatchCase() {
		return _matchCase;
	}
	
	public void setMatchCase(boolean matchCase) {
		this._matchCase = matchCase;
	}
}