/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.Array;
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
import ffapl.types.FFaplArray;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomialResidue;
import ffapl.types.FFaplTypeCrossTable;

/**
 * Represents the EEA of a, b of either Integer or Z()[x]
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class EEA implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a;
		IJavaType b;
		Object[] array;
		
		b = (IJavaType) interpreter.popStack();
		a = (IJavaType) interpreter.popStack();
		
		if(a.typeID() == IJavaType.POLYNOMIALRC){
			array = Algorithm.eea((PolynomialRC)a, (PolynomialRC)b);
			interpreter.pushStack(new Array(FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE,
											1,
											array,
											a.clone(),
											((PolynomialRC)a).getThread()));
			//System.out.println(interpreter);
		}else if(a instanceof BInteger){
			
			if( ((BInteger)a).compareTo(BigInteger.ZERO) < 0){//must not be < 0
				Object[] messages ={"gcd(" + a + ", " + b + ")", a};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
			}else if( ((BInteger)b).compareTo(BigInteger.ZERO) < 0){
				Object[] messages ={"gcd(" + a + ", " + b + ")", b};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
			}
			
			array = Algorithm.eea((BInteger)a, (BInteger)b); 
			interpreter.pushStack(new Array(FFaplTypeCrossTable.FFAPLINTEGER,
												1,
												array,
												a.clone(),
												((BInteger)a).getThread()));
			
		}else{
			System.out.println("error");
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
		//eea(a,b) Integer-Integer
		s = new FFaplPreProcFuncSymbol("eea", 
                null,
                new FFaplArray(new FFaplInteger(), 1),
                ISymbol.FUNCTION);
		s.setProcFunc(new EEA());
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
	  //eea(a,b) Prime - Prime
		s = new FFaplPreProcFuncSymbol("eea", 
                null,
                new FFaplArray(new FFaplInteger(), 1),
                ISymbol.FUNCTION);
		s.setProcFunc(new EEA());
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
	    
	    //eea(a,b) Integer- Prime
		s = new FFaplPreProcFuncSymbol("eea", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new EEA());
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
	    
	  //eea(a,b) Prime - Integer
		s = new FFaplPreProcFuncSymbol("eea", 
                null,
                new FFaplArray(new FFaplInteger(), 1),
                ISymbol.FUNCTION);
		s.setProcFunc(new EEA());
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
	    //eea(a,b) Z()[x] -  Z()[x]
	    s = new FFaplPreProcFuncSymbol("eea", 
                null,
                new FFaplArray(new FFaplPolynomialResidue(), 1),
                ISymbol.FUNCTION);
		s.setProcFunc(new EEA());
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
