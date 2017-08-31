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
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplPolynomialResidue;

/**
 * Calculates the Polynomial
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class EvalulatePolynomial implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger val;
		IJavaType ply;
		BigInteger result;
	
		val = (BInteger) interpreter.popStack();
		ply = (IJavaType) interpreter.popStack();
		
		switch(ply.typeID()){
		case IJavaType.POLYNOMIAL :
			result = ((Polynomial) ply).calculate(val);
			break;
		case IJavaType.POLYNOMIALRC :
			result = ((PolynomialRC) ply).calculate(val);
			break;
		case IJavaType.GALOISFIELD :
			result = ((GaloisField) ply).calculate(val);
			break;
		default:
			Object[] arguments = {"evaluatePolynomial"};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		}
		
		interpreter.pushStack(result);
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
			    
	  //evaluatePolynomial(Polynomial, Integer)
		s = new FFaplPreProcFuncSymbol("evaluatePolynomial", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EvalulatePolynomial());
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
	    //evaluatePolynomial(Polynomial, Prime)
		s = new FFaplPreProcFuncSymbol("evaluatePolynomial", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EvalulatePolynomial());
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
	    symbolTable.closeScope();
	    */
	  //evaluatePolynomial(PolynomialRC, Integer)
		s = new FFaplPreProcFuncSymbol("evaluatePolynomial", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EvalulatePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    /*
	  //calculatePolynomial(PolynomialRC, Prime)
		s = new FFaplPreProcFuncSymbol("evaluatePolynomial", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EvalulatePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	  */
	  //evaluatePolynomial(GF, Integer)
		s = new FFaplPreProcFuncSymbol("evaluatePolynomial", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EvalulatePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    /*
	  //evaluatePolynomial(GF, Prime)
		s = new FFaplPreProcFuncSymbol("evaluatePolynomial", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EvalulatePolynomial());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	  */
	}
}
