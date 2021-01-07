package sunset.gui.search.advanced;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;

public class AdvancedSearchReplace implements IAdvancedSearchReplace{
	private final static int MAX_VARS = 10;
	private final static char VAR_START = '%';
	private final static char VAR_ESC = '%';
	private final static String VARIABLE = VAR_START + "[0-9]";
	
	// non escaped variable regex: 	([^%])%[0-9] - braces are needed for replacement (using $1)
	private final static String NON_ESC_VAR = 
			"([^" + VAR_ESC + "])" + VARIABLE;
	
	// minimum one variable regex: 	.*(^%[0-9]|[^%]%[0-9])((.+%[0-9])|.)*
	private final static String MIN_ONE_VAR = 
			".*(^" + VARIABLE + "|" + NON_ESC_VAR + ")((.+" + VARIABLE + ")|.)*";
	
	// two consecutive variables regex:		(^%[0-9]%[0-9])|([^%]%[0-9]%[0-9])
	private final static String TWO_CONSECUTIVE_VARS = 
			"(^" + VARIABLE + VARIABLE + ")|(" + NON_ESC_VAR + VARIABLE + ")";
	
	private String[] _captures = new String[MAX_VARS];
	private int _matchStart = -1;
	private int _matchEnd = -1;
	
	public AdvancedSearchReplace() {
	}
	
	@Override
	public boolean find(String text, String pattern, int fromIndex, boolean matchCase) throws InvalidPatternException {
		reset();
		
		if (fromIndex < 0 || fromIndex > text.length()) {
			throw new IndexOutOfBoundsException(fromIndex);
		}
		
		System.out.println("\nFind executed:\t" + (text.length() < 20 ? "Text: " + text : "") 
				+ " Pattern: " + pattern + " fromIndex: " + fromIndex + " matchCase: " + matchCase);
		
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		validatePattern(pattern, varPositions);
		
		if (!containsPattern(text, pattern, fromIndex, matchCase)) {
			return false;
		}
		
		int currVarPosition, nextVarPosition, prevMatch, varIndex;
		currVarPosition = varPositions.get(0);
		String nextPattern = removeEscapes(pattern.substring(0, currVarPosition));
		_matchStart = getIndexOf(text, nextPattern, fromIndex, true, matchCase);
		prevMatch = _matchStart + nextPattern.length();
		
		for (int i = 0; i < varPositions.size(); i++) {
			currVarPosition = varPositions.get(i);			
			nextVarPosition = (i+1 < varPositions.size()) ? varPositions.get(i+1) : pattern.length();
			varIndex = getVarIndex(pattern, currVarPosition);
			nextPattern = removeEscapes(pattern.substring(currVarPosition+2, nextVarPosition));
			_matchEnd = getIndexOf(text, nextPattern, prevMatch, false, matchCase);
			performMatch(text, prevMatch, _matchEnd, varIndex);
			_matchEnd += nextPattern.length();
			prevMatch = _matchEnd;
		}
		
		System.out.println("Find result:\t" + _matchStart + "," + _matchEnd + "," + convertArrayToString(_captures));
		
		return true;
	}
	
	/**
	 * Resets the matcher
	 */
	private void reset() {
		_matchStart = -1;
		_matchEnd = -1;
		_captures = null;
		_captures = new String[MAX_VARS];
	}
	
	/**
	 * Puts the elements of the given array into a string separated by comma
	 * @param array the array to be printed
	 * @return string containing array elements separated by comma
	 */
	private String convertArrayToString(String[] array) {
		String result = "";
		
		if (array.length == 0) {
			return "";
		}
		
		for (String s : array) {
			result += s + ",";
		}
		
		return result.substring(0, result.length()-1);
	}
	
	/**
	 * Analyzes the given pattern and returns the positions of all non-escaped variables in the pattern 
	 * @param pattern the pattern to be analyzed
	 * @return An ArrayList of Integers containing start positions of all variables
	 */
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
			varIndexes.add(m.start()+1);	// +1 because of [^%]
			startFrom = m.end()-1;
		}
		
		return varIndexes;
	}
	
	/**
	 * Validates the given pattern. Checks if the pattern fulfills the following conditions:
	 * - the pattern does not comprise two consecutive variables without delimiter
	 * - the pattern comprises all variables only once
	 * - the pattern includes at least one variable
	 * @param pattern the pattern to be validated
	 * @param varPositions the ArrayList containing all variable positions in the pattern
	 * @return true if the pattern is valid, i.e. fulfills above conditions
	 * @throws InvalidPatternException if the pattern is invalid, i.e. fulfills one of the conditions:
	 * - the pattern comprises two variables without delimiter in between
	 * - the pattern comprises a variable more than once
	 */
	private void validatePattern(String pattern, ArrayList<Integer> varPositions) throws InvalidPatternException {
		// check if there are two consecutive variables without delimiter, i.e. if the regex TWO_CONSECUTIVE_VARS is found in the pattern
		Matcher m = getMatcher(pattern, TWO_CONSECUTIVE_VARS, false);
		
		if (m.find()) {
			String msg = "Missing delimiter between variables: ";
			throw new InvalidPatternException(msg + pattern.substring(m.end()-4, m.end()));
		}
		
		// check if a variable is used more than once
		ArrayList<Integer> varIndexes = new ArrayList<Integer>();
		int varIndex;
		
		for (int varPos : varPositions) {
			varIndex = getVarIndex(pattern, varPos);
			
			if (varIndexes.contains(varIndex)) {
				String msg = "Variable has been used more than once: ";
				throw new InvalidPatternException(msg + "%" + varIndex);
			} else {
				varIndexes.add(varIndex);
			}
		}
		
		// check if the pattern includes at least one variable, i.e. if the pattern matches the regex MIN_ONE_VAR 
		m = getMatcher(pattern, MIN_ONE_VAR, false);
		
		if (!m.matches()) {
			String msg = "No variable used in the pattern";
			throw new InvalidPatternException(msg);
		}
	}
	
	/**
	 * Checks if the given text contains the given pattern starting fromIndex and considering the bMatchCase flag
	 * @param text the text to be checked
	 * @param pattern the pattern to be searched in the text
	 * @param fromIndex the starting index of the text where the pattern is searched from 
	 * @param matchCase the flag indicating if case sensitive search should be used to find the pattern in the text
	 * @return true if the pattern converted to regular expressions is found in the given text considering fromIndex and bMatchCase
	 */
	private boolean containsPattern(String text, String pattern, int fromIndex, boolean matchCase) {		
		String patternRegex = convertPatternToRegex(pattern);
		
		System.out.println("Regex Pattern:\t" + patternRegex);
		
		Matcher m = getMatcher(text, patternRegex, matchCase);
		
		// use regex to check if text contains the converted pattern
		return m != null ? m.find(fromIndex) : null;
	}
	
	/**
	 * Converts the given pattern into a regular expression pattern
	 * @param pattern the advanced search pattern to be converted to a regular expression pattern
	 * @return a regex version of the pattern where all literals are escaped and variables are replaced by .*
	 */
	private String convertPatternToRegex(String pattern) {
		// non escaped variables are replaced by .* (pattern starts with variable)
		pattern = pattern.replaceAll("^" + VARIABLE, "\\\\E.*\\\\Q");
		
		// non escaped variables are replaced by .* (variables which are not at start of pattern)
		pattern = pattern.replaceAll(NON_ESC_VAR, "$1\\\\E.*\\\\Q");
		
		// escaped variables %%[0-9] are replaced by the string %[0-9]
		pattern = pattern.replaceAll(VAR_ESC + "(" + VARIABLE + ")", "$1");
		
		return "\\Q" + pattern + "\\E";
	}
	
	/**
	 * Returns a Matcher object corresponding to the specified text and regular expression pattern
	 * @param text the text the matcher is based on
	 * @param pattern the pattern the matcher is based on
	 * @param matchCase the flag indicating if case sensitive regular expression matching is required
	 * @return a Matcher object corresponding to the given parameters, null if an invalid regular expression was specified
	 */
	private Matcher getMatcher(String text, String pattern, boolean matchCase) {
		try {
			final int flags = (matchCase ? 0 : Pattern.CASE_INSENSITIVE) | Pattern.DOTALL;
			Pattern p = Pattern.compile(pattern, flags);
			return p.matcher(text);
		} catch (PatternSyntaxException e) {	// bad regular expression pattern specified 
			return null;
		}
	}
	
	/**
	 * Removes the escaping character from the given pattern
	 * @param pattern the pattern from which the escaping character should be removed
	 * @return the pattern with all escaped variables replaced by variables
	 */
	private String removeEscapes(String pattern) {
		return pattern.replaceAll(VAR_ESC + "(" + VARIABLE + ")", "$1");
	}
	
	/**
	 * Returns the index of the subPattern in the text, considering startFrom, isStart and bMatchCase parameters
	 * If the subPattern is empty, it returns the startFrom position if isStart is true, otherwise it returns the length of the text.
	 * @param text the text where the subPattern has to be searched in
	 * @param pattern the pattern which is searched in the text
	 * @param startFrom the index indicating the starting index of the search in the text
	 * @param isStart the flag indicating if the subPattern is at the start of the pattern
	 * @param matchCase the flag indicating if case sensitive search should be used
	 * @return the index of the pattern inside the text.
	 * If the subPattern is empty, it returns the startFrom position if isStart is true, otherwise it returns the length of the text.
	 */
	private int getIndexOf(String text, String pattern, int startFrom, boolean isStart, boolean matchCase) {
		if (pattern.isEmpty()) {	// pattern can only be empty at start or end
			return isStart ? startFrom : text.length();
		}
		
		return matchCase ? text.indexOf(pattern, startFrom) : text.toLowerCase().indexOf(pattern.toLowerCase(), startFrom);
	}
	
	/**
	 * Returns the index of a variable
	 * @param pattern the pattern where the position of the variable is checked
	 * @param varPosition the position of the variable in the pattern
	 * @return the index of the variable, i.e. for variable %2 it returns 2
	 */
	private int getVarIndex(String pattern, int varPosition) {
		char cVarIndex = pattern.charAt(varPosition+1);
		
		return Character.getNumericValue(cVarIndex);
	}
	
	/**
	 * 
	 * @param text
	 * @param prevMatch
	 * @param nextMatch
	 * @param varIndex
	 */
	private void performMatch(String text, int prevMatch, int nextMatch, int varIndex) {
		_captures[varIndex] = "";
		
		for (int i = prevMatch; i < nextMatch; i++) {
			_captures[varIndex] += text.charAt(i);
		}
	}
	
	@Override
	public String[] getCaptures() {
		return _captures;
	}
	
	@Override
	public int getStart() {
		return _matchStart;
	}
	
	@Override
	public int getEnd() {
		return _matchEnd;
	}

	@Override
	public String getReplaceString(String replaceWith) throws UndeclaredVariableException {
		StringBuilder replaceString = new StringBuilder();
		ArrayList<Integer> varPositions = getVariablePositions(replaceWith);
		
		if (varPositions.isEmpty()) {
			return removeEscapes(replaceWith);
		}
		
		int currVarPosition, nextVarPosition, varIndex;
		currVarPosition = varPositions.get(0);
		String nextPattern = removeEscapes(replaceWith.substring(0, currVarPosition));
		replaceString.append(nextPattern);
		
		for (int i = 0; i < varPositions.size(); i++) {
			currVarPosition = varPositions.get(i);			
			
			varIndex = getVarIndex(replaceWith, currVarPosition);
			
			if (_captures[varIndex] == null) {
				throw new UndeclaredVariableException(varIndex);
			}
			
			replaceString.append(_captures[varIndex]);
			
			nextVarPosition = (i+1 < varPositions.size()) ? varPositions.get(i+1) : replaceWith.length();
			nextPattern = removeEscapes(replaceWith.substring(currVarPosition+2, nextVarPosition));
			replaceString.append(nextPattern);
		}
		
		return replaceString.toString();
	}

	@Override
	public boolean matches(String text, String pattern, boolean matchCase) throws InvalidPatternException {
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		validatePattern(pattern, varPositions);
		
		return containsPattern(text, pattern, 0, matchCase) ? true : false;
	}
}
