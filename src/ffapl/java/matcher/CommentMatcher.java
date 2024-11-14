package ffapl.java.matcher;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentMatcher extends FFaplMatcher{

	private enum MODE {
		MULTILINE,
		SINGLELINE;
	}

	private String startTag;
	private String endTag;
	private MODE mode;
	private Pattern pattern;


	public CommentMatcher(String startTag, String endTag) {
		super();
		this.startTag = startTag;
		this.endTag = endTag;

		if (endTag == null || "".equals(endTag)) {
			mode = MODE.SINGLELINE;
			pattern = Pattern.compile(this.startTag + ".*$", Pattern.MULTILINE);
		} else {
			mode = MODE.MULTILINE;
		}
	}

	public CommentMatcher(String startTag, String endTag, int font, Color color) {
		this(startTag, endTag);
		setFontWeight(font);
		setColor(color);
	}

	public CommentMatcher(String startTag, String endTag, int font) {
		this(startTag, endTag);
		setFontWeight(font);
	}

	public CommentMatcher(String startTag, String endTag, Color color) {
		this(startTag, endTag);
		setColor(color);
	}

	public boolean find() {
		boolean result = false;
		switch (mode) {
		case MULTILINE:
			setStart(getText().indexOf(this.startTag, end()));
			if (start() >= 0) {
				setEnd(getText().indexOf(this.endTag, start()));
				if (end() < 0) {
					setEnd(getText().length());
				} else {
					setEnd(end() + this.endTag.length());
				}
				result = true;
			}
			break;
		case SINGLELINE:
			Matcher matcher = pattern.matcher(getText());
			if (matcher.find(end())) {
				setStart(matcher.start());
				setEnd(matcher.end());
				result = true;
			}
			break;
		}
		return result;
	}
}
