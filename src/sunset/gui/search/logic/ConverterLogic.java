package sunset.gui.search.logic;

import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.InvalidPatternException;
import sunset.gui.search.advanced.interfaces.IAdvancedSearchReplace;
import sunset.gui.search.logic.interfaces.IConverterLogic;

public class ConverterLogic extends BaseLogic implements IConverterLogic {

	@Override
	public boolean isValidAdvancedSearchPattern(String pattern) {		
		try {
			IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace();
			advSearchReplace.validatePattern(pattern);
			_error = false;
			return true;
		} catch (InvalidPatternException e) {
			_message = e.getMessage();
			_error = true;
			return false;
		}
	}

	@Override
	public String convertPatterntoRegex(String pattern) {
		IAdvancedSearchReplace advSearchReplace = new AdvancedSearchReplace();
		
		return advSearchReplace.convertPatternToRegex(pattern);
	}
}
