package sunset.gui.search.logic.interfaces;

public interface IConverterLogic extends IBaseLogic {
	
	/**
	 * Checks if pattern is a valid advanced search pattern
	 * @param pattern The pattern to be validated
	 * @return true if the pattern is valid
	 */
	public boolean isValidAdvancedSearchPattern(String pattern);
	
	/**
	 * Converts the given advanced search pattern to an equivalent regex pattern
	 * @param pattern The pattern to be converted
	 * @return The equivalent regex pattern
	 */
	public String convertPatterntoRegex(String pattern);
}
