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
import ffapl.java.math.Algorithm;
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
 * tate pairing
 * @author Johannes Winkler
 * @version 1.0
 *
 */
public class TatePairing implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a, b;
		a = (IJavaType) interpreter.popStack();
		b = (IJavaType) interpreter.popStack();
		
		if (a.typeID() == IJavaType.ELLIPTICCURVE && b.typeID() == IJavaType.ELLIPTICCURVE)
		{
			GaloisField pairing = Algorithm.tatePairing((EllipticCurve)b,(EllipticCurve)a);
			interpreter.pushStack(pairing);
		}
		else
		{
			throw new FFaplAlgebraicException(null, IAlgebraicError.EC_PAIRING_PARAMETER_NOT_VALID);
		}

	  
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
		s = new FFaplPreProcFuncSymbol("TLPairing", 
                null,
                new FFaplGaloisField(),
                ISymbol.FUNCTION);
		s.setProcFunc(new TatePairing());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplEllipticCurve(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplEllipticCurve(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	   
	}

}
