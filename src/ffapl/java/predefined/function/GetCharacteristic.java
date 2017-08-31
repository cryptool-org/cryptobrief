/**
 * 
 */
package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomialResidue;
import ffapl.types.FFaplResidueClass;

/**
 * returns true if Integer is Prime with certainty 1 - 1/(2^100)
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class GetCharacteristic implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a;
		a = (IJavaType) interpreter.popStack();
		
		switch(a.typeID()){
		case IJavaType.GALOISFIELD:
			interpreter.pushStack(((GaloisField)a).characteristic());
			break;
		case IJavaType.RESIDUECLASS:
			interpreter.pushStack(((ResidueClass)a).modulus());
			break;
		case IJavaType.POLYNOMIALRC:
			interpreter.pushStack(((PolynomialRC)a).characteristic());
			break;
		}
	  
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
		//getCharacteristic(a)
		s = new FFaplPreProcFuncSymbol("getCharacteristic", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetCharacteristic());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	   
		//getCharacteristic(a)
		s = new FFaplPreProcFuncSymbol("getCharacteristic", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetCharacteristic());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	  //getCharacteristic(a)
		s = new FFaplPreProcFuncSymbol("getCharacteristic", 
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
		s.setProcFunc(new GetCharacteristic());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplResidueClass(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	   
	}

}
