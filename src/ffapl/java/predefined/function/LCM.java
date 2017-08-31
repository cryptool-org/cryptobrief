/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplInteger;

/**
 * Represents the LCM of a, b of either Integer or Z()[x]
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class LCM implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger  a;
		BInteger  b;
		
		b = (BInteger) interpreter.popStack();
		a = (BInteger) interpreter.popStack();
		
		if( a.compareTo(BigInteger.ZERO) < 0){//must not be < 0
				Object[] messages ={"lcm(" + a + ", " + b + ")", a};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
		}else if( b.compareTo(BigInteger.ZERO) < 0){
				Object[] messages ={"lcm(" + a + ", " + b + ")", b};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
		}
			
		interpreter.pushStack(a.multiply(b).abs().divide(Algorithm.gcd((BInteger)a, (BInteger)b)));
		
	
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
		//lcm(a,b) Integer-Integer
		s = new FFaplPreProcFuncSymbol("lcm", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LCM());
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
	  //lcm(a,b) Prime - Prime
		s = new FFaplPreProcFuncSymbol("lcm", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LCM());
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
	    
	    //lcm(a,b) Integer- Prime
		s = new FFaplPreProcFuncSymbol("lcm", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LCM());
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
	    
	  //lcm(a,b) Prime - Integer
		s = new FFaplPreProcFuncSymbol("lcm", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LCM());
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
	    
	}

}
