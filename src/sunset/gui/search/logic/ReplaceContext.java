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
	
	public String toString() {
		return (_text.length() < 20 ? "Text: " + _text : "") 
				+ "\tPattern: " + _pattern + "\treplaceWith: " + _replaceWith + "\tmatchCase: " + _matchCase;
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
	
	public String getReplaceWith() {
		return _replaceWith;
	}
	
	public void setReplaceWith(String replaceWith) {
		this._replaceWith = replaceWith;
	}
	
	public boolean isMatchCase() {
		return _matchCase;
	}
	
	public void setMatchCase(boolean matchCase) {
		this._matchCase = matchCase;
	}
}