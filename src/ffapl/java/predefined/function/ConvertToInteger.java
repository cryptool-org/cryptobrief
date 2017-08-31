/**
 * 
 */
package ffapl.java.predefined.function;

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
 * Converts value of different Type to Integer
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ConvertToInteger implements IPredefinedProcFunc {

	private static final String CONVERT_TO_INT_KEY = "convertToInteger";
	private static final String CONVERT_TO_INT_KEY_2 = "int";
	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType ply;
		ply = (IJavaType) interpreter.popStack();
		BInteger result;
		switch(ply.typeID()){
		case IJavaType.RESIDUECLASS :
			result = (BInteger) ((ResidueClass)ply).value();
			break;
		default:
			Object[] arguments = {CONVERT_TO_INT_KEY};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		}
		
		interpreter.pushStack(result);
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
		//convertToInteger(Z)
		s = new FFaplPreProcFuncSymbol(CONVERT_TO_INT_KEY, 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new ConvertToInteger());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplResidueClass(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	    //int(Z)
		s = new FFaplPreProcFuncSymbol(CONVERT_TO_INT_KEY_2, 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new ConvertToInteger());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplResidueClass(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}

}
