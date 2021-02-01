package sunset.gui.search.logic.interfaces;

public interface IBaseLogic {
	
	/**
	 * Returns the message of the search operation or the exception message if a invalid regular expression was used
	 * @return the message of the search operation or the exception message if a invalid regular expression was used
	 */
	public String getMessage();
	
	public boolean getError();
}
