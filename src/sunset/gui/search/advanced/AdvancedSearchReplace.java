package sunset.gui.search.advanced;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.exception.UndeclaredVariableException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.SearchContext;
import sunset.gui.search.util.SearchReplaceMessageHandler;

public class AdvancedSearchReplace implements IAdvancedSearchReplace {
	
	/* constants */
	private final static int 	MAX_VARS = 10;
	private final static char 	VAR_START_SYMBOL	= '%';	// variable symbol
	private final static char 	VAR_ESC_SYMBOL 		= '%';	// escaping symbol
	
	/* variable regex */
	private final static String VAR 		= VAR_START_SYMBOL + "[0-9]";				// %[0-9] - variable
	private final static String VAR_ESC 	= VAR_ESC_SYMBOL + "(" + VAR + ")";			// %(%[0-9]) - escaped variable
	private final static String VAR_NON_ESC = "([^" + VAR_ESC_SYMBOL + "])" + VAR;		// ([^%])%[0-9] - non escaped variable
	private final static String VAR_PATTERN	= "^" + VAR + "|" + VAR_NON_ESC;			// ^%[0-9]|([^%])%[0-9] - variable pattern
	private final static String VAR_TWO_CON = "(^" + VAR + VAR + ")|(" + VAR_NON_ESC + VAR + ")";			// (^%[0-9]%[0-9])|([^%]%[0-9]%[0-9]) - two consecutive variables
	private final static String VAR_MIN_ONE = ".*(^" + VAR + "|" + VAR_NON_ESC + ")((.+" + VAR + ")|.)*";	// .*(^%[0-9]|[^%]%[0-9])((.+%[0-9])|.)* - minimum one variable
	
	/* matching pair regex */
	private final static String PAIR_DELIM_SYMBOL	= "...";	// matching pair delimiter string
	private final static String ENTRY_DELIM_SYMBOL 	= ",";		// matching pair entry delimiter string
	private final static String PAIR_DELIM 			= Pattern.quote(PAIR_DELIM_SYMBOL);		// \Q...\E
	private final static String ENTRY_DELIM 		= Pattern.quote(ENTRY_DELIM_SYMBOL);	// \Q,\E
	private final static String VALID_STRING 		= "[^" + PAIR_DELIM_SYMBOL + ENTRY_DELIM_SYMBOL + "]+";	// [^...,]+
	private final static String VALID_MATCHING_PAIR_CONFIG =	// ([^...,]+\Q...\E[^...,]+\Q,\E)*[^...,]+\Q...\E[^...,]+
			"("  + VALID_STRING + PAIR_DELIM + VALID_STRING + ENTRY_DELIM + 
			")*" + VALID_STRING + PAIR_DELIM + VALID_STRING;	
	
	/* member variables accessible with getter and setter methods */
	private String[] 	_captures 	= new String[MAX_VARS];
	private int 		_matchStart = -1;
	private int 		_matchEnd 	= -1;
	
	/* internal member variables */
	private String		_text		= null;
	private int			_fromIndex	= -1;
	private boolean		_matchCase;
	private Map<String, String> _matchingPairTemplates = new HashMap<String, String>();
	
	/**
	 * Default constructor (no balancing supported)
	 */
	public AdvancedSearchReplace() {
		_matchingPairTemplates.clear();
	}
	
	/**
	 * Constructor which validates the given matchingPairs and stores the result in a map
	 * @param matchingPairs The string representation of the matching pairs
	 * @throws MatchingPairConfigurationException If the matching pair configuration represented by matchingPairs is invalid
	 */
	public AdvancedSearchReplace(String matchingPairs) throws MatchingPairConfigurationException {
		_matchingPairTemplates.clear();
		
		if (matchingPairs != null && !matchingPairs.isEmpty()) {
			Matcher matcher = getMatcher(matchingPairs, VALID_MATCHING_PAIR_CONFIG, true);
			
			if (!matcher.matches()) {
				String msg = SearchReplaceMessageHandler.getInstance().
						getMessage("exception_matchingpairconfig", matchingPairs);
				throw new MatchingPairConfigurationException(msg);
			}
			
			for (String pair : matchingPairs.split(ENTRY_DELIM)) {
				String[] pairValues = pair.trim().split(PAIR_DELIM);
				String key = pairValues[1].trim();	// key is the ending tag
				String val = pairValues[0].trim();	// val is the starting tag
				
				ArrayList<Integer> varPositionsKey = getVariablePositions(key);
				ArrayList<Integer> varPositionsVal = getVariablePositions(val);
				ArrayList<Integer> varIndexesVal = new ArrayList<Integer>();
				
				// check if key and value have same number of variables
				if (varPositionsKey.size() != varPositionsVal.size()) {
					String msg = SearchReplaceMessageHandler.getInstance().
							getMessage("exception_matchingpairconfig", val + PAIR_DELIM_SYMBOL + key);
					throw new MatchingPairConfigurationException(msg);
				}
				
				// check if variables are used correctly
				if (varPositionsKey.size() > 0) {
					try {
						validatePattern(key);
						validatePattern(val);
					} catch (InvalidPatternException e) {
						String msg = SearchReplaceMessageHandler.getInstance().
								getMessage("exception_matchingpairconfig", val + PAIR_DELIM_SYMBOL + key);
						throw new MatchingPairConfigurationException(msg);
					}
				}
				
				// check if same variables are used
				for (int varPosVal : varPositionsVal) {
					int varIndex = getVariableIndex(val, varPosVal);
					varIndexesVal.add(varIndex);
				}
				
				for (int varPosKey : varPositionsKey) {
					int varIndex = getVariableIndex(key, varPosKey);

					if (!varIndexesVal.contains(varIndex)) {
						String msg = SearchReplaceMessageHandler.getInstance().
								getMessage("exception_matchingpairconfig", val + PAIR_DELIM_SYMBOL + key);
						throw new MatchingPairConfigurationException(msg);
					}
				}
				
				_matchingPairTemplates.put(key, val);
			}
		}
	}
	
	@Override
	public boolean find(SearchContext context, boolean showBalancingError) throws InvalidPatternException, UnbalancedStringException {
		reset();
		_text = context.getText();
		_fromIndex = context.getFromIndex();
		_matchCase = context.isMatchCase();
		String pattern = context.getPattern();
		boolean balanced = true;

		validatePattern(pattern);
		Map<String, String> matchingPairs = getConcreteMatchingPairs();
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		do {
			if (!containsPattern(pattern)) {
				// if pattern is not found from current index
				return false;
			}
			
			int currVarPos = varPositions.get(0);
			String subPattern = removeAdvancedEscapeSymbol(pattern.substring(0, currVarPos));
			_matchStart = subPattern.isEmpty() ? _fromIndex : getIndexOf(subPattern, _fromIndex);
			int prevMatch = _matchStart + subPattern.length();
			
			for (int i = 0; i < varPositions.size(); i++) {
				currVarPos = varPositions.get(i);			
				int nextVarPos = (i+1 < varPositions.size()) ? varPositions.get(i+1) : pattern.length();
				int varIndex = getVariableIndex(pattern, currVarPos);
				subPattern = removeAdvancedEscapeSymbol(pattern.substring(currVarPos+2, nextVarPos));
				_matchEnd = subPattern.isEmpty() ? _text.length() : getIndexOf(subPattern, prevMatch);
				
				try {
					_matchEnd = performBalancedMatch(subPattern, prevMatch, _matchEnd, varIndex, matchingPairs);
					_matchEnd += subPattern.length();
					balanced = true;
					prevMatch = _matchEnd;
				} catch (UnbalancedStringException e) {
					if (showBalancingError) {
						throw e;
					} else {
						balanced = false;
						_fromIndex = _matchEnd;
						break;
					}
				}
			}
		} while (!balanced);
		
		return true;
	}
	
	@Override
	public boolean matches(String text, String pattern, boolean matchCase) throws InvalidPatternException {
		_text = text;
		_fromIndex = 0;
		_matchCase = matchCase;
		
		validatePattern(pattern);
		
		return containsPattern(pattern);
	}
	
	@Override
	public String replaceVariables(String pattern, String[] contents) throws UndeclaredVariableException {
		StringBuilder replaceString = new StringBuilder();
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		if (varPositions.isEmpty()) {
			return removeAdvancedEscapeSymbol(pattern);
		}
		
		int currVarPosition = varPositions.get(0);
		String nextPattern = removeAdvancedEscapeSymbol(pattern.substring(0, currVarPosition));
		replaceString.append(nextPattern);
		
		for (int i = 0; i < varPositions.size(); i++) {
			currVarPosition = varPositions.get(i);			
			int varIndex = getVariableIndex(pattern, currVarPosition);
			String content = contents[varIndex];
			
			if (content == null) {
				String msg = SearchReplaceMessageHandler.getInstance().
						getMessage("exception_undeclaredvariable", String.valueOf(varIndex));
				throw new UndeclaredVariableException(msg);
			}
			
			replaceString.append(content);
			int nextVarPosition = (i+1 < varPositions.size()) ? varPositions.get(i+1) : pattern.length();
			nextPattern = removeAdvancedEscapeSymbol(pattern.substring(currVarPosition+2, nextVarPosition));
			replaceString.append(nextPattern);
		}
		
		return replaceString.toString();
	}
	
	@Override
	public void validatePattern(String pattern) throws InvalidPatternException {		
		// check if the pattern includes at least one variable
		Matcher	matcher = getMatcher(pattern, VAR_MIN_ONE, false);
				
		if (!matcher.matches()) {
			String msg = SearchReplaceMessageHandler.getInstance().
					getMessage("exception_invalidpattern_novarused");
			throw new InvalidPatternException(msg);
		}
		
		// check if a variable is used more than once
		ArrayList<Integer> varIndexes = new ArrayList<Integer>();
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		
		for (int varPos : varPositions) {
			int varIndex = getVariableIndex(pattern, varPos);
			
			if (varIndexes.contains(varIndex)) {
				String msg = SearchReplaceMessageHandler.getInstance().
						getMessage("exception_invalidpattern_varmorethanonce", String.valueOf(varIndex));
				throw new InvalidPatternException(msg);
			} else {
				varIndexes.add(varIndex);
			}
		}
	
		// check if there are two consecutive variables without delimiter
		matcher = getMatcher(pattern, VAR_TWO_CON, false);
		
		if (matcher.find()) {
			String msg = SearchReplaceMessageHandler.getInstance().
					getMessage("exception_invalidpattern_missingdelim", pattern.substring(matcher.end()-4, matcher.end()));
			throw new InvalidPatternException(msg);
		}
	}
	
	@Override
	public String convertPatternToRegex(String pattern) {
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		String regex = "";
		int pos = 0;
		
		for (Integer varPos : varPositions) {
			String prefix = pattern.substring(pos, varPos);
			prefix = prefix.isEmpty() ? "" : Pattern.quote(prefix);
			regex += prefix + "(?<g" + getVariableIndex(pattern, varPos) + ">.*?)";	// named capturing group g[0-9]
			pos = varPos+2;
		}
		
		String suffix = pattern.substring(pos, pattern.length());
		suffix = suffix.isEmpty() ? "" : Pattern.quote(suffix);
		regex += suffix;
		System.out.println("Converted RegEx Pattern: " + removeAdvancedEscapeSymbol(regex));
		// finally escaped variables (i.e. %%[0-9]) are replaced by the string %[0-9]
		return removeAdvancedEscapeSymbol(regex);
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
	 * Analyzes the given pattern and returns a list of positions of all non-escaped variables in the pattern 
	 * Example: Pattern = a%1b%%2c%3d, returns a list with values 1 and 8, %2 is not a variable as it is escaped (%%2)
	 * @param pattern The advanced search pattern
	 * @return An ArrayList of Integers containing the starting position of all variables
	 */
	private ArrayList<Integer> getVariablePositions(String pattern) {
		ArrayList<Integer> varIndexes = new ArrayList<Integer>();
		Matcher matcher = getMatcher(pattern, VAR_PATTERN, false);
		int pos = 0;
		
		while (matcher.find(pos)) {
			varIndexes.add(matcher.end()-2);	// -2 to get start position of variable
			pos = matcher.end()-1;			// -1 to support replace patterns like %1%1, to find 2nd variable using NON_ESC_VAR
		}
		
		return varIndexes;
	}
	
	/**
	 * Checks if the given text contains the given pattern starting fromIndex and considering the matchCase flag
	 * @param pattern The advanced search pattern
	 * @return true if the regex representation of the pattern is found in the given text considering fromIndex and matchCase
	 */
	private boolean containsPattern(String pattern) {	
		// convert pattern to equivalent regex pattern
		String patternRegex = convertPatternToRegex(pattern);
		
		// create matcher for regex pattern
		Matcher matcher = getMatcher(_text, patternRegex, _matchCase);
		
		// use regex to check if text contains the converted pattern		
		return matcher.find(_fromIndex);
	}
	
	/**
	 * Returns a Matcher object corresponding to the specified text and regular expression pattern
	 * @param text The text the matcher should use
	 * @param pattern The pattern the matcher should use
	 * @param matchCase The flag indicating if case sensitive regular expression matching is required
	 * @return a Matcher object corresponding to the given parameters
	 */
	private Matcher getMatcher(String text, String pattern, boolean matchCase) {
		// specify regex flags considering matchCase flag, DOTALL is needed as variables also match new lines
		final int flags = (matchCase ? 0 : Pattern.CASE_INSENSITIVE) | Pattern.DOTALL;
		
		// no exception needs to be handled as pattern syntax is ensured to be correct
		return Pattern.compile(pattern, flags).matcher(text);
	}
	
	/**
	 * Removes the escaping character from all variables of the given pattern, e.g. %%1 is converted to %1
	 * @param pattern The pattern from which the escape symbol should be removed from the escaped variables
	 * @return the pattern with all escaped variables replaced by the literals %[0-9]
	 */
	private String removeAdvancedEscapeSymbol(String pattern) {
		return pattern.replaceAll(VAR_ESC, "$1");	// replace escaped variable with %[0-9] 
	}
	
	/**
	 * Returns the index of the pattern in the text considering startFrom and matchCase
	 * @param pattern The pattern which is searched
	 * @param startFrom The starting index of the search
	 * @return the index of the pattern inside the text considering startFrom and matchCase
	 */
	private int getIndexOf(String pattern, int startFrom) {
		return _matchCase ? _text.indexOf(pattern, startFrom) : _text.toLowerCase().indexOf(pattern.toLowerCase(), startFrom);
	}
	
	/**
	 * Returns the index of a variable
	 * @param pattern The pattern comprising the variable
	 * @param varPosition The position of the variable in the pattern
	 * @return the index of the variable, i.e. for variable %2 it returns 2
	 */
	private int getVariableIndex(String pattern, int varPosition) {
		return Character.getNumericValue(pattern.charAt(varPosition+1));
	}
	
	/**
	 * Performs a balanced match of the substring of the text which is supposed to be captured by a variable
	 * If the substring is balanced, it's content is saved into the according capture using the variable index
	 * If the substring is unbalanced, an exception is thrown
	 * Example: text = a(()b)b, next = b, start = 1, end = 4
	 * The substring of the text between start and end position is "(()", which is unbalanced
	 * Therefore, end position is increased to 6, so that the substring "(()b)" is balanced
	 * @param next The next part of the pattern after the current match, needed if initial substring is unbalanced
	 * @param start The start position of the balanced match
	 * @param end The end position of the balanced match, which could be increased if the substring is unbalanced
	 * @param varIndex The index of the variable which should capture the substring between start and end
	 * @param matchingPairs The map of matching opening and closing strings 
	 * @return The end position of the balanced substring
	 * @throws UnbalancedStringException If the substring between start and end is unbalanced
	 */
	private int performBalancedMatch(String next, int start, int end, int varIndex, Map<String, String> matchingPairs) 
			throws UnbalancedStringException {
		Deque<String> stack = new ArrayDeque<String>();
		String str = "";
		_captures[varIndex] = "";
		
		for (int i = start; i < end; i++) {
			str += _text.charAt(i);
			boolean found = false;
			
			for (Map.Entry<String, String> entry : matchingPairs.entrySet()) {
				if (entry.getKey().startsWith(str) || entry.getValue().startsWith(str)) {
					found = true;
					break;
				}
			}
			
			str = found ? str : String.valueOf(_text.charAt(i));
			
			if (matchingPairs.containsValue(str)) {			// opening string, e.g. (
				stack.push(str);
			} else if (matchingPairs.containsKey(str)) {	// closing string, e.g. )
				boolean balancingError = false;
				
				if (stack.isEmpty()) {
					// stack is empty, but a closing string was found
					balancingError = true;
				} else {
					String top = stack.pop();
					
					if (!top.equals(matchingPairs.get(str))) {
						// opening and closing string do not match
						balancingError = true;
					}
				}
				
				if (balancingError) {
					_matchEnd = i;
					String msg = SearchReplaceMessageHandler.getInstance().
							getMessage("exception_unbalancedstring", _text.substring(start, end));
					throw new UnbalancedStringException(msg, start, end);
				}
			}
			
			if ((i+1) == end && !stack.isEmpty()) {	// if loop is about to end but stack is not empty
				// get next index of the next pattern
				// Example: text = a(()b)b, next = b, the index of the 2nd b is needed to get a balanced string
				end = getIndexOf(next, end + next.length());

				if (end == -1) {
					// no more match found, but stack is not empty
					String msg = SearchReplaceMessageHandler.getInstance().
							getMessage("exception_unbalancedstring", _text.substring(start, _text.length()));
					throw new UnbalancedStringException(msg, start, _text.length());
				}
			}
			
			_captures[varIndex] += _text.charAt(i);
		}
		
		return end;
	}
	
	/**
	 * Resolves all variables in the matching pair templates to concrete string values
	 * @param text The text which is used to get the concrete values
	 * @return the map of strings with concrete matching pairs
	 */
	private Map<String, String> getConcreteMatchingPairs() {
		Map<String, String> matchingPairs = new HashMap<String, String>();
		String key, val;

		for (Entry<String, String> entry : _matchingPairTemplates.entrySet()) {
			key = entry.getKey();
			val = entry.getValue();

			Matcher matcher = getMatcher(key, VAR_MIN_ONE, false);
			
			if (matcher.matches()) {	// check if at least one variable is used in key
				// get matched contents for key, e.g. \end{%1}
				ArrayList<String[]> matchedContents = getAllVariableContents(key);
				
				// add matched contents for value, e.g. \begin{%1}
				matchedContents.addAll(getAllVariableContents(val));
				
				for (String[] contents : matchedContents) {
					try {
						// replace all variables in key and value with according contents
						String matchingKey = replaceVariables(key, contents);
						
						if (!matchingPairs.containsKey(matchingKey)) {
							String matchingVal = replaceVariables(val, contents);
							matchingPairs.put(matchingKey, matchingVal);
						}
					} catch (UndeclaredVariableException e) {
						// can be ignored here
					}
				}
			} else {	
				// no variable used in key, entry can be added to map directly
				matchingPairs.put(entry.getKey(), entry.getValue());
			}
		}
		
		return matchingPairs;
	}
	
	/**
	 * Finds all occurrences of the pattern in the text and replaces all variables with the according contents
	 * Example: text = a{d1,d2}b{d3,d4}c, pattern = {%1,%2}
	 * This pattern occurs two times in the text. The first set of the variables %1 and %2 stores the values d1 and d2, and the second set stores d3 and d4.
	 * Example: text = \begin{a}\begin{b}\end{b}\end{a}, pattern = \begin{%1} or \end{%1}
	 * @param text The subject text
	 * @param pattern The advanced search pattern which is searched in the text
	 * @return A list of String[], where each entry of this list corresponds to a set of variables from one match of the entire pattern in the text
	 */
	private ArrayList<String[]> getAllVariableContents(String pattern) {
		ArrayList<Integer> varPositions = getVariablePositions(pattern);
		ArrayList<String[]> matchedContents = new ArrayList<String[]>();
		String regexPattern = convertPatternToRegex(pattern);
		Matcher matcher = getMatcher(_text, regexPattern, true);
		int pos = 0;
		
		while (matcher.find(pos)) {
			String[] contents = new String[MAX_VARS];
			
			for (int i = 0; i < matcher.groupCount(); i++) {
				int varPos = varPositions.get(i);
				int varIdx = getVariableIndex(pattern, varPos);
				contents[varIdx] = matcher.group(i+1);	// +1 because group 0 stores the entire match
			}
			
			matchedContents.add(contents);
			pos = matcher.end();
		}
		
		return matchedContents;
	}
}
