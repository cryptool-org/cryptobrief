/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.PolynomialRC;
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
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomialResidue;

/**
 * Represents the GCD of a, b of either Integer or Z()[x]
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class GCD implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a;
		IJavaType b;
		
		b = (IJavaType) interpreter.popStack();
		a = (IJavaType) interpreter.popStack();
		
		if(a.typeID() == IJavaType.POLYNOMIALRC){
			 interpreter.pushStack(Algorithm.gcd((PolynomialRC)a, (PolynomialRC)b));
		}else if(a instanceof BInteger){
			
			if( ((BInteger)a).compareTo(BigInteger.ZERO) < 0){//must not be < 0
				Object[] messages ={"gcd(" + a + ", " + b + ")", a};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
			}else if( ((BInteger)b).compareTo(BigInteger.ZERO) < 0){
				Object[] messages ={"gcd(" + a + ", " + b + ")", b};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
			}
			
			 interpreter.pushStack(Algorithm.gcd((BInteger)a, (BInteger)b));
		}else{
			System.err.println("error in GCD.execute");
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
		//gcd(a,b) Integer-Integer
		s = new FFaplPreProcFuncSymbol("gcd", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GCD());
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
	  //gcd(a,b) Prime - Prime
		s = new FFaplPreProcFuncSymbol("gcd", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GCD());
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
	    symbolTable.closeScope();
	    
	    //gcd(a,b) Integer- Prime
		s = new FFaplPreProcFuncSymbol("gcd", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GCD());
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
	    
	  //gcd(a,b) Prime - Integer
		s = new FFaplPreProcFuncSymbol("gcd", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GCD());
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
	    */
	    //gcd(a,b) Z()[x] -  Z()[x]
	    s = new FFaplPreProcFuncSymbol("gcd", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GCD());
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
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	}

}
