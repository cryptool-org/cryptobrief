/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import ffapl.exception.FFaplException;
import ffapl.java.classes.Array;
import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplTypeCrossTable;

/**
 * Calculates Minimum of two BigInteger Values
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FactorInteger implements IPredefinedProcFunc {

	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		BInteger a;
		BigInteger key;
		SortedSet<BigInteger> set;
		TreeMap<BigInteger, BigInteger> primeFactors;
		int i;
		Object[] res;
		
		a = (BInteger) interpreter.popStack();
		primeFactors = Algorithm.FactorInteger(a);
		res = new Object[primeFactors.size()];
		
		i = 0;
		set = new TreeSet<BigInteger>(primeFactors.keySet());
		//Generates output Array
		for(Iterator<BigInteger> itr = set.iterator(); itr.hasNext(); ){
			key = itr.next();
			Object[] arr = {key, primeFactors.get(key)};
			res[i] = arr;
			i++;
		}
		
	    interpreter.pushStack(new Array(FFaplTypeCrossTable.FFAPLINTEGER, 2, res, null, a.getThread()));
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
		//FactorInteger
	    s = new FFaplPreProcFuncSymbol("factorInteger", 
	            null,
	            new FFaplArray(new FFaplInteger(), 2),
	            ISymbol.FUNCTION);
		s.setProcFunc(new FactorInteger());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
	            null,
	            new FFaplInteger(),
	            ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}

}
