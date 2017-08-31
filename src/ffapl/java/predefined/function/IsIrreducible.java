/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.JBoolean;
import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.classes.PolynomialRCPrime;
import ffapl.java.classes.Prime;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplPolynomialResidue;
//import ffapl.types.FFaplPrime;

/**
 * returns true if polynomial in polynomialring is irreducible
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class IsIrreducible implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType t;
		Polynomial ply;
		PolynomialRC plyrc;
		t = (IJavaType) interpreter.popStack();
	    if(t instanceof PolynomialRC){
	    	plyrc = (PolynomialRC) t;
	    	
	    }else{
	    	ply = (Polynomial) interpreter.popStack();
	    	if (t instanceof Prime){
	    		plyrc = new PolynomialRC(ply.polynomial(), (BigInteger) t, ply.getThread());
	    	}else{
	    		//check if t prime automatically
	    		plyrc = new PolynomialRCPrime(ply.polynomial(), (BigInteger) t, ply.getThread());
	    	}
	    }
	    interpreter.pushStack(new JBoolean(Algorithm.isIrreducible(plyrc)));
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
		//isIrreducible(a)
		s = new FFaplPreProcFuncSymbol("isIrreducible", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsIrreducible());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	  //isIrreducible(f, p)
		s = new FFaplPreProcFuncSymbol("isIrreducible", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsIrreducible());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    /*
	  //isIrreducible(f, p)
		s = new FFaplPreProcFuncSymbol("isIrreducible", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsIrreducible());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	  //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();*/
	}

}
