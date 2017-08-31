/**
 * 
 */
package ffapl.lib;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.interfaces.IToken;
import ffapl.lib.interfaces.IVm;
import ffapl.types.Type;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplPreProcFuncSymbol extends FFaplSymbol {

	IPredefinedProcFunc _procfunc;
	/**
	 * @param name
	 * @param token
	 */
	public FFaplPreProcFuncSymbol(String name, IToken token) {
		super(name, token);
	}

	/**
	 * @param name
	 * @param kind
	 */
	public FFaplPreProcFuncSymbol(String name, int kind) {
		super(name, kind);
	}

	/**
	 * @param name
	 * @param token
	 * @param type
	 */
	public FFaplPreProcFuncSymbol(String name, IToken token, Type type) {
		super(name, token, type);
	}

	/**
	 * @param name
	 * @param token
	 * @param kind
	 */
	public FFaplPreProcFuncSymbol(String name, IToken token, int kind) {
		super(name, token, kind);
	}

	/**
	 * @param name
	 * @param token
	 * @param type
	 * @param kind
	 */
	public FFaplPreProcFuncSymbol(String name, IToken token, Type type, int kind) {
		super(name, token, type, kind);
	}
	
	/**
	 * Sets the procedure or function class
	 * @param procfunc
	 */
	public void setProcFunc(IPredefinedProcFunc procfunc){
		_procfunc = procfunc;
	}
	
	/**
	 * executes the procedure or function
	 * @param interpreter
	 * @throws FFaplAlgebraicException 
	 */
	public void execute(IVm interpreter) throws FFaplAlgebraicException{
                _procfunc.execute(interpreter); 
	}

}
