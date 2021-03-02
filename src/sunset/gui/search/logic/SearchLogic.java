package sunset.gui.search.logic;

import java.util.regex.Matcher;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.search.advanced.exception.UnbalancedStringException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.interfaces.ISearchLogic;
import sunset.gui.search.util.SearchReplaceMessageHandler;

public class SearchLogic extends BaseLogic implements ISearchLogic {
	
	private int _matchStart;
	private int _matchEnd;
	
	@Override
	public boolean search(SearchContext context, boolean wrapAround) {
		String text = context.getText();
		String pattern = context.getPattern();
		int fromIndex = context.getFromIndex();
		
		if (context.isMatchCase()) {
			_matchStart = text.indexOf(pattern, fromIndex);
		} else {
			_matchStart = text.toLowerCase().indexOf(pattern.toLowerCase(), fromIndex);
		}
		
		if (_matchStart == -1 && fromIndex != 0 && wrapAround) {
			// if not found starting fromIndex, fromIndex was not 0, and wrap around is activated, search again from 0
			context.setFromIndex(0);
			return search(context, wrapAround);
		}
		
		if (_matchStart != -1) {
			_matchEnd = _matchStart + pattern.length();
		} else {
			_matchEnd = -1;
		}
		
		_message = generateMessage();
		_error = false;
		return _matchStart != -1;
	}
	
	@Override
	public boolean searchRegex(SearchContext context, boolean wrapAround, boolean dotAll) {
		Matcher m = getMatcher(context.getText(), context.getPattern(), context.isMatchCase(), dotAll);
		
		if (m != null) {
			if (m.find(context.getFromIndex()) || wrapAround && context.getFromIndex() != 0 && m.find(0)) {
				_matchStart = m.start();
				_matchEnd = m.end();
			} else {
				_matchStart = -1;
				_matchEnd = -1;
			}
			
			_message = generateMessage();
			_error = false;
			return _matchStart != -1;
		} else {
			_error = true;
			return false;
		}
	}
	
	@Override
	public boolean searchAdvanced(SearchContext context, boolean wrapAround, String matchingPairs, boolean showBalancingError) {
		try {
			IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace(matchingPairs);
			boolean found = advSearchReplace.find(context, showBalancingError);
			
			if (!found && context.getFromIndex() != 0 && wrapAround) {
				// if not found starting fromIndex, fromIndex was not 0, and wrap around is activated, search again from 0
				context.setFromIndex(0);
				found = advSearchReplace.find(context, showBalancingError);
			}

			_matchStart = advSearchReplace.getStart();
			_matchEnd = advSearchReplace.getEnd();
			_message = generateMessage();
			_error = false;
			return found;
		} catch (MatchingPairConfigurationException | InvalidPatternException e) {
			_message = e.getMessage();
		} catch (UnbalancedStringException e) {
			_matchStart = e.getStart();
			_matchEnd = e.getEnd();
			_message = e.getMessage();
		}
		
		_error = true;
		return false;
	}
	
	/**
	 * Generates an information message depending on the search outcome
	 * @return The information message representing the outcome of the search operation
	 */
	private String generateMessage() {
		String msg_key = "";
		
		if (_matchStart != -1 && _matchStart == _matchEnd) {
			msg_key = "search_zerolengthmatch";
		} else if (_matchStart != -1) {
			msg_key = "search_success";
		} else {
			msg_key = "search_nosuccess";
		}
		
		return SearchReplaceMessageHandler.getInstance().getMessage(msg_key);
	}
	
	@Override
	public int getStart() {
		return _matchStart;
	}
	
	@Override
	public int getEnd() {
		return _matchEnd;
	}
}