package sunset.gui.search.advanced;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.search.exception.InvalidPatternException;

public class AdvancedSearch {
	private final static int MAX_VARS = 10;
	private final static char VAR_START = '%';
	private final static char VAR_ESC = '%';
	private final static String VARIABLE = VAR_START + "[0-9]";
	
	// non escaped variable regex: 	([^%])%[0-9] - braces are needed for replacement (using $1)
	private final static String NON_ESC_VAR = 
			"([^" + VAR_ESC + "])" + VARIABLE;
	
	// supported pattern regex: 	.*(^%[0-9]|[^%]%[0-9])((.+%[0-9])|.)*
	private final static String SUPPORTED_PATTERN = 
			".*(^" + VARIABLE + "|" + NON_ESC_VAR + ")((.+" + VARIABLE + ")|.)*";
	
	// invalid pattern regex: 		(^%[0-9]%[0-9])|([^%]%[0-9]%[0-9])
	private final static String INVALID_PATTERN = 
			"(^" + VARIABLE + VARIABLE + ")|(" + NON_ESC_VAR + VARIABLE + ")";
	
	private String[] _captures = new String[MAX_VARS];
	private int _matchStart = -1;
	private int _matchEnd = -1;
	
	public boolean find(String text, String pattern, int fromIndex, boolean bMatchCase) throws InvalidPatternException {
		reset();
		
		System.out.println("Find executed. " + (text.length() < 20 ? "Text: " + text : "") 
				+ " Pattern: " + pattern + " fromIndex: " + fromIndex + " matchCase: " + bMatchCase);
		
		if (!isPreCheckSuccessful(text, pattern, fromIndex, bMatchCase)) {
			return false;
		}
		
		ArrayList<Integer> varPositions = getVariablePositions(pattern);	
		
		int currVarPosition, nextVarPosition, prevMatch, varIndex;
		currVarPosition = varPositions.get(0);
		String nextPattern = removeEscapes(pattern.substring(0, currVarPosition));
		_matchStart = getIndexOf(text, nextPattern, fromIndex, true, bMatchCase);
		prevMatch = _matchStart + nextPattern.length();
		
		for (int i = 0; i < varPositions.size(); i++) {
			currVarPosition = varPositions.get(i);			
			nextVarPosition = (i+1 < varPositions.size()) ? varPositions.get(i+1) : pattern.length();
			varIndex = getVarIndex(pattern, currVarPosition);
			nextPattern = removeEscapes(pattern.substring(currVarPosition+2, nextVarPosition));
			_matchEnd = getIndexOf(text, nextPattern, prevMatch, false, bMatchCase);
			performMatch(text, prevMatch, _matchEnd, varIndex);
			_matchEnd += nextPattern.length();
			prevMatch = _matchEnd;
		}
		
		System.out.println("Find result: " + _matchStart + "," + _matchEnd + "," + printArray(_captures));
		
		return true;
	}
	
	private void reset() {
		_matchStart = -1;
		_matchEnd = -1;
		_captures = null;
		_captures = new String[MAX_VARS];
	}
	
	private String printArray(String[] array) {
		String result = "";
		
		if (array.length == 0) {
			return "";
		}
		
		for (String s : array) {
			result += s + ",";
		}
		
		return result.substring(0, result.length()-1);
	}
	
	private ArrayList<Integer> getVariablePositions(String pattern) {
		ArrayList<Integer> varIndexes = new ArrayList<Integer>();
		
		// check if pattern starts with a variable
		Pattern p = Pattern.compile("^" + VARIABLE);
		Matcher m = p.matcher(pattern);
		
		if (m.find()) {
			varIndexes.add(m.start());
		}
		
		// find variables (with index from 0-9) which are not escaped
		p = Pattern.compile(NON_ESC_VAR);
		m = p.matcher(pattern);
		
		int startFrom = 0;
		
		while (m.find(startFrom)) {
			varIndexes.add(m.start()+1);
			startFrom = m.end()-1;
		}
		
		return varIndexes;
	}
	
	private boolean isPreCheckSuccessful(String text, String pattern, int fromIndex, boolean bMatchCase) throws InvalidPatternException {
		// check if there are two consecutive variables without delimiter
		Pattern p = Pattern.compile(INVALID_PATTERN);
		Matcher m = p.matcher(pattern);
		
		if (m.find()) {
			String msg = "Invalid search pattern! Missing delimiter between variables: ";
			throw new InvalidPatternException(msg + pattern.substring(m.end()-4, m.end()));
		}
		
		if (!pattern.matches(SUPPORTED_PATTERN)) {
			return false;
		}
		
		String pattern_regex = "\\Q" + convertPatternToRegex(pattern) + "\\E";
		
		System.out.println("PreCheck Regex Pattern: " + pattern_regex);
		
		m = getMatcher(text, pattern_regex, bMatchCase, true);
		
		if (!m.find(fromIndex)) {	// use regex to perform a first check if text contains the literals of the pattern
			return false;
		}
		
		return true;
	}
	
	private String convertPatternToRegex(String pattern) {
		// non escaped variables are replaced by .* (pattern starts with variable)
		pattern = pattern.replaceAll("^" + VARIABLE, "\\\\E.*\\\\Q");
		
		// non escaped variables are replaced by .* (variables which are not at start of pattern)
		pattern = pattern.replaceAll(NON_ESC_VAR, "$1\\\\E.*\\\\Q");
		
		// escaped variables %%[0-9] are replaced by the string %[0-9]
		pattern = pattern.replaceAll(VAR_ESC + "(" + VARIABLE + ")", "$1");
		
		return pattern;
	}
	
	private Matcher getMatcher(String text, String pattern, boolean bMatchCase, boolean bDotAll) {
		try {
			final int flags = (bMatchCase ? 0 : Pattern.CASE_INSENSITIVE) | (bDotAll ? Pattern.DOTALL : 0);
			Pattern p = Pattern.compile(pattern, flags);
			Matcher m = p.matcher(text);
			
			return m;
		} catch (PatternSyntaxException e) {	// bad regular expression pattern specified 
			return null;
		}
	}
	
	private String removeEscapes(String pattern) {
		return pattern.replaceAll(VAR_ESC + "(" + VARIABLE + ")", "$1");
	}
	
	private int getIndexOf(String text, String subPattern, int startFrom, boolean isStart, boolean bMatchCase) {
		if (subPattern.isEmpty()) {	// pattern can only be empty at start or end
			return isStart ? startFrom : text.length();
		}
		
		if (bMatchCase) {
			return text.indexOf(subPattern, startFrom);
		} else {
			return text.toLowerCase().indexOf(subPattern.toLowerCase(), startFrom);
		}
	}
	
	private int getVarIndex(String pattern, int varPosition) {
		char cVarIndex = pattern.charAt(varPosition+1);
		
		return Character.getNumericValue(cVarIndex);
	}
	
	private void performMatch(String text, int prevMatch, int nextMatch, int varIndex) {
		_captures[varIndex] = "";
		
		for (int i = prevMatch; i < nextMatch; i++) {
			_captures[varIndex] += text.charAt(i);
		}
	}
	
	public String[] getCaptures() {
		return _captures;
	}
	
	public int getStart() {
		return _matchStart;
	}
	
	public int getEnd() {
		return _matchEnd;
	}
}
