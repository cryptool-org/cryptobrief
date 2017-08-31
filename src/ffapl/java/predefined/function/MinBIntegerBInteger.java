/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplInteger;

/**
 * Calculates Minimum of two BigInteger Values
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class MinBIntegerBInteger implements IPredefinedProcFunc {

		
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger a;
		BInteger b;
		b = (BInteger) interpreter.popStack();
		a = (BInteger) interpreter.popStack();
	    interpreter.pushStack(new BInteger(a.add(b).subtract(a.subtract(b).abs()).shiftRight(1), a.getThread()));
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
		//min(a,b)
	    s = new FFaplPreProcFuncSymbol("min", 
	            null,
	            new FFaplInteger(),
	            ISymbol.FUNCTION);
		s.setProcFunc(new MinBIntegerBInteger());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
	            null,
	            new FFaplInteger(),
	            ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
	            null,
	            new FFaplInteger(),
	            ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}

}
