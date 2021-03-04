package ffapl.java.interfaces;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.logging.FFaplLogger;
import ffapl.lib.interfaces.IVm;

public interface IPredefinedProcFuncLog extends IPredefinedProcFunc {
	/**
	 * Executes the procedure or function
	 * @param interpreter
	 * @throws FFaplAlgebraicException
	 */
	public void execute(IVm interpreter, FFaplLogger logger) throws FFaplAlgebraicException;
}
