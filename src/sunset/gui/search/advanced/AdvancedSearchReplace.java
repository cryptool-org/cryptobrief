package sunset.gui.search.advanced;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;

public class AdvancedSearchReplace implements IAdvancedSearchReplace{
	
	/*
	 * Constants
	 */
	private final static int MAX_VARS = 10;
	private final static char VAR_START_SYMBOL = '%';
	private final static char VAR_ESC_SYMBOL = '%';
	
	// variable regex: 				%[0-9]
	private final static String VARIABLE = VAR_START_SYMBOL + "[0-9]";
	
	// escaped variable regex: 		%(%[0-9])
	private final static String ESC_VAR = VAR_ESC_SYMBOL + "(" + VARIABLE + ")";
	
	// non escaped variable regex: 	([^%])%[0-9]
	private final static String NON_ESC_VAR = 
			"([^" + VAR_ESC_SYMBOL + "])" + VARIABLE;
	
	// minimum one variable regex: 			.*(^%[0-9]|[^%]%[0-9])((.+%[0-9])|.)*
	private final static String MIN_ONE_VAR = 
			".*(^" + VARIABLE + "|" + NON_ESC_VAR + ")((.+" + VARIABLE + ")|.)*";
	
	// two consecutive variables regex:		(^%[0-9]%[0-9])|([^%]%[0-9]%[0-9])
	private final static String TWO_CONSECUTIVE_VARS = 
			"(^" + VARIABLE + VARIABLE + ")|(" + NON_ESC_VAR + VARIABLE + ")";
	
	private final static String PAIR_DELIM_SYMBOL = "...";
	
	// pair delimiter regex:	\Q...\E
	private final static String PAIR_DELIM = "\\Q" + PAIR_DELIM_SYMBOL + "\\E";
	
	private final static String ENTRY_DELIM_SYMBOL = ",";
	
	// entry delimiter regex:	\Q,\E
	private final static String ENTRY_DELIM = "\\Q" + ENTRY_DELIM_SYMBOL + "\\E";
	
	// valid part string regex:	[^...,]+
	private final static String VALID_STRING = "[^" + PAIR_DELIM_SYMBOL + ENTRY_DELIM_SYMBOL + "]+";
	
	// valid matching pair config regex:	"([^...,]+\\Q...\\E[^...,]+\\Q,\\E)*[^...,]+\\Q...\\E[^...,]+"
	private final static String VALID_MATCHING_PAIR_CONFIG = 
			"(" + VALID_STRING + PAIR_DELIM + VALID_STRING + ENTRY_DELIM + ")*" + VALID_STRING + PAIR_DELIM + VALID_STRING;
	
	/*
	 * member variables
	 */
	private String[] _captures = new String[MAX_VARS];
	private int _matchStart = -1;
	private int _matchEnd = -1;
	private Map<String, String> _matchingPairTemplates = new HashMap<String, String>();
	
	public AdvancedSearchReplace() {
		_matchingPairTemplates.clear();
	}
	
	public AdvancedSearchReplace(String matchingPairs) throws MatchingPairConfigurationException {
		_matchingPairTemplates.clear();
		
		if (matchingPairs != null && !matchingPairs.isEmpty()) {
			Matcher m = getMatcher(matchingPairs, VALID_MATCHING_PAIR_CONFIG, true);
			
			if (!m.matches()) {
				throw new MatchingPairConfigurationException(matchingPairs);
			}
			
			for (String pair : matchingPairs.split(ENTRY_DELIM)) {
				String[] pairValues = pair.trim().split(PAIR_DELIM);
				String key = pairValues[1].trim();
				String val = pairValues[0].trim();
						
				ArrayList<Integer> varPositionsKey = getVariablePositions(key);
				ArrayList<Integer> varPositionsVal = getVariablePositions(val);
				ArrayList<Integer> varIndexesVal = new ArrayList<Integer>();
				
				if (varPositionsKey.size() != varPositionsVal.size()) {
					throw new MatchingPairConfigurationException(val + PAIR_DELIM_SYMBOL + key);
				}
				
				for (int varPosVal : varPositionsVal) {
					int varIndex = getVariableIndex(val, varPosVal);
					varIndexesVal.add(varIndex);
				}
				
				for (int varPosKey : varPositionsKey) {
					int varIndex = getVariableIndex(key, varPosKey);

					if (!varIndexesVal.contains(varIndex)) {
						throw new MatchingPairConfigurationException(val + PAIR_DELIM_SYMBOL + key);
					}
				}
				
				_matchingPairTemplates.put(key, val);
			}
		}
	}
	
	@Override
	public boolean find(String text, String pattern, int fromIndex, boolean matchCase, boolean showBalancingError) 
			throws InvalidPatternException, UnbalancedStringException {
		reset();
		
		if (fromIndex < 0 || fromIndex > text.length()) {
			throw new IndexOutOfBoundsException(fromIndex);
		}
		
		System.out.println("\nFind executed:\t" + (text.length() < 20 ? "Text: " + text : "") 
				+ " Pattern: " + pattern + " fromIndex: " + fromIndex + " matchCase: " + matchCase);
		
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		Map<String, String> matchingPairs = resolveWildcards(text);
		validatePattern(pattern, varPositions);
		
		boolean balanced = true;
		
		do {
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
				varIndex = getVariableIndex(pattern, currVarPosition);
				nextPattern = removeEscapes(pattern.substring(currVarPosition+2, nextVarPosition));
				_matchEnd = getIndexOf(text, nextPattern, prevMatch, false, matchCase);
				try {
					_matchEnd = performMatch(text, nextPattern, prevMatch, _matchEnd, varIndex, matchCase, matchingPairs);
					balanced = true;
					_matchEnd += nextPattern.length();
					prevMatch = _matchEnd;
				} catch (UnbalancedStringException e) {
					if (showBalancingError) {
						throw e;
					} else {
						balanced = false;
						fromIndex = _matchEnd;
						break;
					}
				}
			}
		} while (!balanced);
		
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
		Matcher m = getMatcher(pattern, "^" + VARIABLE, false);
		
		if (m.find()) {
			varIndexes.add(m.start());
		}
		
		// find variables (with index from 0-9) which are not escaped
		m = getMatcher(pattern, NON_ESC_VAR, false);
		
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
		
		for (int varPos : varPositions) {
			int varIndex = getVariableIndex(pattern, varPos);
			
			if (varIndexes.contains(varIndex)) {
				String msg = "Variable has been used more than once: %";
				throw new InvalidPatternException(msg + varIndex);
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
		return m.find(fromIndex);
	}
	
	/**
	 * Converts the given pattern into a regular expression pattern
	 * @param pattern the advanced search pattern to be converted to a regular expression pattern
	 * @return a regex version of the pattern where all literals are escaped and variables are replaced by .*
	 */
	private String convertPatternToRegex(String pattern) {
		// non escaped variables are replaced by .*? (if pattern starts with variable)
		pattern = pattern.replaceAll("^" + VARIABLE, "\\\\E(.*?)\\\\Q");
		
		// non escaped variables are replaced by .*? (variables which are not at start of pattern)
		// $1 is needed to include the symbol before the (non escaped) variable ([^%])
		pattern = pattern.replaceAll(NON_ESC_VAR, "$1\\\\E(.*?)\\\\Q");
		
		// escaped variables (i.e. %%[0-9]) are replaced by the string %[0-9]
		pattern = pattern.replaceAll(ESC_VAR, "$1");
		
		return "\\Q" + pattern + "\\E";
	}
	
	/**
	 * Returns a Matcher object corresponding to the specified text and regular expression pattern
	 * @param text the text the matcher is based on
	 * @param pattern the pattern the matcher is based on
	 * @param matchCase the flag indicating if case sensitive regular expression matching is required
	 * @return a Matcher object corresponding to the given parameters
	 */
	private Matcher getMatcher(String text, String pattern, boolean matchCase) {
		final int flags = (matchCase ? 0 : Pattern.CASE_INSENSITIVE) | Pattern.DOTALL;
		Pattern p = Pattern.compile(pattern, flags);
		// no exception needs to be handled as pattern syntax is ensured to be correct
		return p.matcher(text);
	}
	
	/**
	 * Removes the escaping character from the given pattern, e.g. %%1 is changed to %1
	 * @param pattern the pattern from which the escaping character should be removed
	 * @return the pattern with all escaped variables replaced by variables
	 */
	private String removeEscapes(String pattern) {
		return pattern.replaceAll(ESC_VAR, "$1");
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
	private int getVariableIndex(String pattern, int varPosition) {
		char cVarIndex = pattern.charAt(varPosition+1);
		
		return Character.getNumericValue(cVarIndex);
	}
	
	private int performMatch(String text, String next, int prevMatch, int nextMatch, int varIndex, boolean matchCase, Map<String, String> matchingPairs) 
			throws UnbalancedStringException {
		Deque<String> stack = new ArrayDeque<String>();
		int startIndex = prevMatch;
		String str = "";
		_captures[varIndex] = "";
		
		for (int i = prevMatch; i < nextMatch; i++) {
			char c = text.charAt(i);
			str = getLongestMatch(matchingPairs, str, c);
			
			if (matchingPairs.containsValue(str)) {			// opening
				stack.push(str);
			} else if (matchingPairs.containsKey(str)) {	// closing
				try {
					String top = stack.pop();
					
					if (!top.equals(matchingPairs.get(str))) {
						throw new UnbalancedStringException(text.substring(startIndex, nextMatch));
					}
				} catch (NoSuchElementException e) {
					throw new UnbalancedStringException(text.substring(startIndex, nextMatch));
				}
			}
			
			if ((i+1) == nextMatch && !stack.isEmpty()) {	// stay in loop if stack is not empty (unbalanced)
				nextMatch = getIndexOf(text, next, nextMatch + next.length(), false, matchCase);

				if (nextMatch == -1) {
					throw new UnbalancedStringException(text.substring(startIndex, text.length()));
				}
			}
			
			_captures[varIndex] += c;
		}
		
		return nextMatch;
	}
	
	private Map<String, String> resolveWildcards(String text) {
		Map<String, String> matchingPairs = new HashMap<String, String>();
		String key, val;

		for (Entry<String, String> entry : _matchingPairTemplates.entrySet()) {
			key = entry.getKey();
			val = entry.getValue();

			Matcher m = getMatcher(key, MIN_ONE_VAR, false);
			
			if (m.matches()) {	// at least one variable used in key
				addContents(matchingPairs, text, key, val, true);
				addContents(matchingPairs, text, key, val, false);
			} else {	// no variable used in key
				matchingPairs.put(entry.getKey(), entry.getValue());
			}
		}
		
		return matchingPairs;
	}
	
	private void addContents(Map<String, String> matchingPairs, String text, String key, String val, boolean useKey) {
		String matchingKey, matchingVal;
		ArrayList<String[]> matchedContents = getVariableContents(text, useKey ? key : val);
		
		for (String[] contents : matchedContents) {
			try {
				matchingKey = replaceVariables(key, contents);
				matchingVal = replaceVariables(val, contents);
				
				if (!matchingPairs.containsKey(matchingKey)) {
					matchingPairs.put(matchingKey, matchingVal);
				}
			} catch (UndeclaredVariableException e) {
				// can be ignored here
			}
		}
	}
	
	private ArrayList<String[]> getVariableContents(String text, String pattern) {
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		ArrayList<String[]> matchedContents = new ArrayList<String[]>();
		String regexPattern = convertPatternToRegex(pattern);
		Matcher m = getMatcher(text, regexPattern, true);
		int pos = 0;
		
		while (m.find(pos)) {
			pos = m.end();
			
			String[] contents = new String[MAX_VARS];
			
			for (int i = 0; i < m.groupCount(); i++) {
				int varPos = varPositions.get(i);
				int varIdx = getVariableIndex(pattern, varPos);
				
				contents[varIdx] = m.group(i+1);
			}
			
			matchedContents.add(contents);
		}
		
		return matchedContents;
	}
	
	private String getLongestMatch(Map<String, String> pairs, String str, char c) {
		str += c;
		
		for (Map.Entry<String, String> entry : pairs.entrySet()) {
			if (entry.getKey().startsWith(str) || entry.getValue().startsWith(str)) {
				return str;
			}
		}
		
		return String.valueOf(c);
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
	public String replaceVariables(String pattern, String[] contents) throws UndeclaredVariableException {
		StringBuilder replaceString = new StringBuilder();
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		if (varPositions.isEmpty()) {
			return removeEscapes(pattern);
		}
		
		int currVarPosition, nextVarPosition, varIndex;
		currVarPosition = varPositions.get(0);
		String nextPattern = removeEscapes(pattern.substring(0, currVarPosition));
		replaceString.append(nextPattern);
		
		for (int i = 0; i < varPositions.size(); i++) {
			currVarPosition = varPositions.get(i);			
			varIndex = getVariableIndex(pattern, currVarPosition);
			String content = contents[varIndex];
			
			if (content == null) {
				throw new UndeclaredVariableException(varIndex);
			}
			
			replaceString.append(content);
			nextVarPosition = (i+1 < varPositions.size()) ? varPositions.get(i+1) : pattern.length();
			nextPattern = removeEscapes(pattern.substring(currVarPosition+2, nextVarPosition));
			replaceString.append(nextPattern);
		}
		
		return replaceString.toString();
	}

	@Override
	public boolean matches(String text, String pattern, boolean matchCase) throws InvalidPatternException {
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		validatePattern(pattern, varPositions);
		
		return containsPattern(text, pattern, 0, matchCase);
	}
}
