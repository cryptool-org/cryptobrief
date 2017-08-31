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
 * returns the leading coefficient of a polynomial
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class LeadingCoefficient implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType ply;
		ply = (IJavaType) interpreter.popStack();
		BInteger result;
		switch(ply.typeID()){
		case IJavaType.GALOISFIELD :
			result = (BInteger) ((GaloisField)ply).value().leadingCoefficient();
			break;
		case IJavaType.POLYNOMIAL :
			result = (BInteger) ((Polynomial)ply).leadingCoefficient();
			break;
		case IJavaType.POLYNOMIALRC :
			result = (BInteger) ((PolynomialRC)ply).leadingCoefficient();
			break;
		default:
			Object[] arguments = {"leadingCoefficient"};
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
		//leadingCoefficient(GF)
		s = new FFaplPreProcFuncSymbol("leadingCoefficient", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LeadingCoefficient());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	    //leadingCoefficient(Polynomial)
		s = new FFaplPreProcFuncSymbol("leadingCoefficient", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LeadingCoefficient());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	  //leadingCoefficient(Z(p)[x])
		s = new FFaplPreProcFuncSymbol("leadingCoefficient", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LeadingCoefficient());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	   
	}

}
