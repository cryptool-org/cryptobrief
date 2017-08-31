/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplPolynomialResidue;


/**
 * polynomial in polynomialring or GF
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class GetPolynomial implements IPredefinedProcFunc {

		
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {

		IJavaType type =  (IJavaType) interpreter.popStack();
		
		if(type instanceof PolynomialRC){
			 interpreter.pushStack(((PolynomialRC) type).getPolynomial());
		}else if(type instanceof GaloisField){
			 interpreter.pushStack(((GaloisField) type).value().getPolynomial());
		}
	   
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
		s = new FFaplPreProcFuncSymbol("ply", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetPolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("ply", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetPolynomial());
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
