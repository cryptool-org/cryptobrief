package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.ResidueClass;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplResidueClass;

public class Sqrt implements IPredefinedProcFunc {
	
	@Override
	public void execute(IVm interpreter) throws FFaplAlgebraicException {
		
		IJavaType a = (IJavaType) interpreter.popStack();
		BigInteger valueRC = BigInteger.ZERO;
		ResidueClass resultRC;
		GaloisField resultGF;
		
		
		switch(a.typeID()){
		case IJavaType.RESIDUECLASS:
			valueRC = Algorithm.sqrtMod(((ResidueClass)a).value(), ((ResidueClass)a).modulus(), true);
			resultRC = new ResidueClass(valueRC,((ResidueClass)a).modulus());
			interpreter.pushStack(resultRC);
			break;
			
		case IJavaType.GALOISFIELD:
			resultGF = ((GaloisField)a).clone();
			resultGF.setValue( Algorithm.sqrt(((GaloisField)a)).value().getPolynomial() );
			interpreter.pushStack(resultGF);
			break;
		}

		interpreter.funcReturn();

	}

	public static void registerProcFunc(ISymbolTable symbolTable)
			throws FFaplException {
		FFaplPreProcFuncSymbol s;

		s = new FFaplPreProcFuncSymbol("sqrt", 
                null,
                new FFaplResidueClass(),
                ISymbol.FUNCTION);
		s.setProcFunc(new Sqrt());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplResidueClass(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	    
	    
		s = new FFaplPreProcFuncSymbol("sqrt", 
                null,
                new FFaplGaloisField(),
                ISymbol.FUNCTION);
		s.setProcFunc(new Sqrt());
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

