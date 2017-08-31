/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplEllipticCurve;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomialResidue;
import ffapl.types.FFaplResidueClass;

/**
 * returns true if Integer is Prime with certainty 1 - 1/(2^100)
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class GetECPointOrder implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a;
		a = (IJavaType) interpreter.popStack();
		
		BigInteger order = ((EllipticCurve)a).getOrder();
		interpreter.pushStack(order);
	  
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
		//getCharacteristic(a)
		s = new FFaplPreProcFuncSymbol("getECPointOrder", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetECPointOrder());
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
