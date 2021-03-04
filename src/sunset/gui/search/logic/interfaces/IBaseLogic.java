package sunset.gui.search.logic.interfaces;

public interface IBaseLogic {
	
	/**
	 * Returns the message of the search operation
	 * @return the message of the search operation
	 */
	public String getMessage();
	
	/**
	 * Returns the error flag of the search operation
	 * @return the error flag of the search operation
	 */
	public boolean getError();
}
