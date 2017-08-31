/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.JBoolean;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplInteger;

/**
 * returns true if Integer is Prime with certainty 1 - 1/(2^100)
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class IsPrime implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger a;
		a = (BInteger) interpreter.popStack();
	    interpreter.pushStack(new JBoolean(a.isProbablePrime(100)));
	    interpreter.funcReturn();
	}
	
	/**
	 * Registers predefined Function in Symbol table
	 * @param symbolTable
	 * @throws FFaplException
	 */
	public static void registerProcFunc(ISymbolTable symbolTable)
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		//isPrime(a)
		s = new FFaplPreProcFuncSymbol("isPrime", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsPrime());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}

}
