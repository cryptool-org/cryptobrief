/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.*;

/**
 * returns the Irreducible polynomial of an GF
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class GetIrreduciblePolynomial implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		GaloisField a;
		a = (GaloisField) interpreter.popStack();
		interpreter.pushStack(((GaloisField)a).irrPolynomial().getPolynomial());
	    interpreter.funcReturn();
	}
	
	/**
	 * Registers predefined Function in Symbol table
	 * @param symbolTable
	 * @param thread
	 * @throws FFaplException
	 */
	public static void registerProcFunc(ISymbolTable symbolTable)
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		//getIrreduciblePolynomial(a)
		s = new FFaplPreProcFuncSymbol("getIrreduciblePolynomial", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetIrreduciblePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	   
	}

}
