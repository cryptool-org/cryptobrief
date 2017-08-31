/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;


/**
 * returns true if polynomial in polynomialring is irreducible
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class IrreduciblePolynomial implements IPredefinedProcFunc {

		
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger p;
		BInteger n;
		p = (BInteger) interpreter.popStack();
		n = (BInteger) interpreter.popStack();
	    interpreter.pushStack((Algorithm.getIrreduciblePolynomial(n, p).getPolynomial()));
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
		s = new FFaplPreProcFuncSymbol("irreduciblePolynomial", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IrreduciblePolynomial());
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
	    /*
	    s = new FFaplPreProcFuncSymbol("irreduciblePolynomial", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IrreduciblePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("irreduciblePolynomial", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IrreduciblePolynomial());
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
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("irreduciblePolynomial", 
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IrreduciblePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();*/
	}
	
	

}
