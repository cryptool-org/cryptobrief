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
import ffapl.lib.interfaces.ICompilerError;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.*;

/**
 * @author Johannes Winkler
 * @version 1.0
 *
 */
public class getECParameter implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		
		EllipticCurve ec;
		String param;
		GaloisField result;
		
		param = ((JString) interpreter.popStack()).toString();
		ec = (EllipticCurve) interpreter.popStack();
		
		if (!ec.isGf())
		{
			throw new FFaplAlgebraicException(null, IAlgebraicError.EC_GET_PARAMETER_ERROR);
		}
		
		result = ec.getGF().clone();
		
		if (param.equals("a1"))
		{
			result.setValue(ec.get_a1());
		}
		else if (param.equals("a2"))
		{
			result.setValue(ec.get_a2());
		}
		else if (param.equals("a3"))
		{
			result.setValue(ec.get_a3());
		}
		else if (param.equals("a4"))
		{
			result.setValue(ec.get_a4());
		}
		else if (param.equals("a6"))
		{
			result.setValue(ec.get_a6());
		}
		else
		{
			Object[] arguments ={param };
			throw new FFaplAlgebraicException(arguments, ICompilerError.WRONG_EC_PARAMETER);
		}
		
		interpreter.pushStack(result);

	    interpreter.funcReturn();
	}
	
	/**
	 * Registers predefined Function in Symbol table
	 * @param symbolTable
	 * @param thread
	 * @throws FFaplException
	 */
	public static void registerProcFunc(ISymbolTable symbolTable)
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		//getXasPolynomial(EC)
		s = new FFaplPreProcFuncSymbol("getECParameter", 
                null,
                new FFaplGaloisField(),
                ISymbol.FUNCTION);
		s.setProcFunc(new getECParameter());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplEllipticCurve(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplString(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    

	}

}
