/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;
import ffapl.exception.FFaplException;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.*;

/**
 * @author Johannes Winkler
 * @version 1.0
 *
 */
public class getXasInteger implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {

		BInteger data;
		EllipticCurve ec;

		ec = (EllipticCurve) interpreter.popStack();
		
		if (ec.isGf())
		{
			Object[] arguments ={};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.EC_FIELD_ERROR);
		}
		
		data = (BInteger) ec.getX_rc();

		
		interpreter.pushStack(data);
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
		//getXasInteger(EC)
		s = new FFaplPreProcFuncSymbol("getXasInteger", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new getXasInteger());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplEllipticCurve(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	}

}
