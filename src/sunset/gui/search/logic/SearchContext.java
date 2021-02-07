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
	
	public String getText() {
		return _text;
	}
	
	public String getPattern() {
		return _pattern;
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
}