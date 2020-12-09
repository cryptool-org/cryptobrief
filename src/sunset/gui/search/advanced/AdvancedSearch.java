package sunset.gui.search.advanced;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.Assert;

import sunset.gui.search.exception.InvalidPatternException;

public class AdvancedSearch {
	private final static int MAX_VARS = 10;
	private final static char VAR_START = '%';
	private final static char VAR_ESC = '%';
	private final static String VARIABLE = VAR_START + "[0-9]";
	
	private final static String NON_ESC_VAR = "([^" + VAR_ESC + "])" + VARIABLE;
	
	// invalid pattern regex: ^%[0-9]%[0-9]|[^%]%[0-9]%[0-9]
	private final static String INVALID_PATTERN = "^" + VARIABLE + VARIABLE +
						 "|" + "[^" + VAR_ESC + "]" + VARIABLE + VARIABLE;
	
	private String[] _captures = new String[MAX_VARS];
	private int _matchStart = -1;
	private int _matchEnd = -1;
	
	public boolean find(String text, String pattern, int fromIndex, boolean bMatchCase) throws InvalidPatternException {
		reset();
		
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		if (!isPreCheckSuccessful(varPositions.size(), text, pattern, fromIndex, bMatchCase)) {
			return false;
		}
		
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
		
		for (String s : array) {
			result += s + ",";
		}
		
		return result.substring(0, result.length()-1);
	}
	
	private ArrayList<Integer> getVariablePositions(String pattern) throws InvalidPatternException {
		// check if there are two consecutive variables without delimiter
		Pattern p = Pattern.compile(INVALID_PATTERN);
		Matcher m = p.matcher(pattern);
		
		if (m.find()) {
			String msg = "Invalid search pattern! Missing delimiter between variables: ";
			throw new InvalidPatternException(msg + pattern.substring(m.end()-4, m.end()));
		}
		
		ArrayList<Integer> idx = new ArrayList<Integer>();
		
		// check if pattern starts with a variable
		p = Pattern.compile("^" + VARIABLE);
		m = p.matcher(pattern);
		if (m.find()) {
			idx.add(m.start());
		}
		
		// find variables (with index from 0-9) which are not escaped
		p = Pattern.compile(NON_ESC_VAR);
		m = p.matcher(pattern);
		
		int startFrom = 0;
		
		while (m.find(startFrom)) {
			idx.add(m.start()+1);
			startFrom = m.end()-1;
		}
		
		return idx;
	}
	
	private boolean isPreCheckSuccessful(int cntVars, String text, String pattern, int fromIndex, boolean bMatchCase) {
		if (cntVars == 0) {	// no variables found, normal search is sufficient
			return false;
		}
		
		System.out.println("Pattern: " + pattern);
		
		// non escaped variables are replaced by .*
		pattern = pattern.replaceAll("^" + VARIABLE, "\\\\E.*\\\\Q");
		
		// non escaped variables are replaced by .*
		pattern = pattern.replaceAll(NON_ESC_VAR, "$1\\\\E.*\\\\Q");
		
		// escaped variables are replaced by %[0-9]
		pattern = pattern.replaceAll(VAR_ESC + "(" + VARIABLE + ")", "$1");
		String pattern_regex = "\\Q" + pattern + "\\E";
		
		System.out.println("PreCheck Regex Pattern: " + pattern_regex);
		
		Matcher m = getMatcher(text, pattern_regex, bMatchCase, true);
		
		if (!m.find(fromIndex)) {	// use regex to perform a first check if text contains the literals of the pattern
			return false;
		}
		
		return true;
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
