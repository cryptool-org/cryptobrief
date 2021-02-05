package sunset.gui.search.logic;

public class ReplaceContext {
	private String _text;
	private String _pattern;
	private String _replaceWith;
	private boolean _matchCase;
	
	public ReplaceContext(String text, String pattern, String replaceWith, boolean matchCase) {
		_text = text;
		_pattern = pattern;
		_replaceWith = replaceWith;
		_matchCase = matchCase;
	}
	
	public String getText() {
		return _text;
	}
	
	public String getPattern() {
		return _pattern;
	}
	
	public String getReplaceWith() {
		return _replaceWith;
	}
	
	public boolean isMatchCase() {
		return _matchCase;
	}
}