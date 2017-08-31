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
 * LegendreSymbol
 * @author Johannes Winkler
 * @version 1.0
 *
 */
public class LegendreSymbol implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a;
		IJavaType b;
		
		BigInteger result = new BigInteger("-1");
		
		b = (IJavaType) interpreter.popStack();
		a = (IJavaType) interpreter.popStack();
		
		if(a.typeID() == IJavaType.POLYNOMIALRC && b.typeID() == IJavaType.POLYNOMIALRC){
			
			result = Algorithm.legendreSymbol((PolynomialRC)a, (PolynomialRC)b);

		}else if(a instanceof BInteger && b instanceof BInteger){
			
			if( ((BInteger)a).compareTo(BigInteger.ZERO) < 0){//must not be < 0
				Object[] messages ={"legendreSymbol(" + a + ", " + b + ")", a};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
			}else if( ((BInteger)b).compareTo(BigInteger.ZERO) < 0){
				Object[] messages ={"legendreSymbol(" + a + ", " + b + ")", b};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.VAL_LESS_ZERO);
			}
			
			result = Algorithm.legendreSymbol(((BInteger)a), ((BInteger)b)); 
			
		}else{
			System.out.println("error");
		}
	   
		interpreter.pushStack(new BInteger(result,null));
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
		
		//LegendreSymbol(a,b) Integer-Integer
		s = new FFaplPreProcFuncSymbol("legendreSymbol", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LegendreSymbol());
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
	   
	    
	    
	    //LegendreSymbol(a,b) Z()[x] -  Z()[x]
	    s = new FFaplPreProcFuncSymbol("legendreSymbol", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new LegendreSymbol());
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
