/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.Prime;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPrime;

/**
 * returns true if Integer is Prime with certainty 1 - 1/(2^100)
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class GetNextPrime implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger a;
		a = (BInteger) interpreter.popStack();
	    interpreter.pushStack(new Prime(a.nextProbablePrime(), a.getThread()));
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
		//getNextPrime(a)
		s = new FFaplPreProcFuncSymbol("getNextPrime", 
                null,
                new FFaplPrime(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetNextPrime());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    /*
	  //getNextPrime(a)
		s = new FFaplPreProcFuncSymbol("getNextPrime", 
                null,
                new FFaplPrime(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetNextPrime());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();*/
	}

}
