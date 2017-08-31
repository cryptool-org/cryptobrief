/**
 * 
 */
package ffapl.java.interfaces;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.lib.interfaces.IVm;

/**
 * Interface for a predefined procedure or function
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface IPredefinedProcFunc {

	/**
	 * Executes the procedure or function
	 * @param interpreter
	 * @throws FFaplAlgebraicException
	 */
	public void execute(IVm interpreter) throws FFaplAlgebraicException;
	
}
