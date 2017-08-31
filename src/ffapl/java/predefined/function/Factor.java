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
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.Polynomial;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplTypeCrossTable;

/**
 * Calculates Minimum of two BigInteger Values
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Factor implements IPredefinedProcFunc {

	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		GaloisField a;
		GaloisField key;
		SortedSet<GaloisField> set;
		TreeMap<GaloisField, BigInteger> primeFactors;
		int i;
		Object[] res;
		
		a = (GaloisField) interpreter.popStack();
		
		primeFactors = Algorithm.Factor(a);
		res = new Object[primeFactors.size()];
		
		i = 0;
		set = new TreeSet<GaloisField>(primeFactors.keySet());
		//Generates output Array
		for(Iterator<GaloisField> itr = set.iterator(); itr.hasNext(); ){
			key = itr.next();
			//System.out.println(key);
			Object[] arr = {key.value().getPolynomial(), 
					        new Polynomial(primeFactors.get(key), BigInteger.ZERO, a.getThread())};
			res[i] = arr;
			i++;
		}
		
	    interpreter.pushStack(new Array(FFaplTypeCrossTable.FFAPLPOLYNOMIAL, 2, res, null, a.getThread()));
	    
		//interpreter.pushStack();
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
	    s = new FFaplPreProcFuncSymbol("factor", 
	            null,
	            new FFaplArray(new FFaplInteger(), 2),
	            ISymbol.FUNCTION);
		s.setProcFunc(new Factor());
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
