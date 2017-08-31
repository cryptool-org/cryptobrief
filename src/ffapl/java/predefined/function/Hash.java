package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.JString;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplEllipticCurve;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplPolynomialResidue;
import ffapl.types.FFaplResidueClass;
import ffapl.types.FFaplString;

public class Hash implements IPredefinedProcFunc {
	
	@Override
	public void execute(IVm interpreter) throws FFaplAlgebraicException {
	
		IJavaType a = (IJavaType) interpreter.popStack();
		BInteger result;
		switch(a.typeID()){
		case IJavaType.STRING:
			result = Algorithm.hashSHA256((JString)a);
			
			break;
		default:
			result = Algorithm.hashSHA256(new JString(a.classInfo() + ": " + a));
			break;
		}
		interpreter.pushStack(result);
		interpreter.funcReturn();

	}
	
	public static void registerProcFunc(ISymbolTable symbolTable)
			throws FFaplException {
			
		registerInteger(symbolTable);
		registerString(symbolTable);
		registerGF(symbolTable);
		registerRC(symbolTable);
		registerPolynomialRC(symbolTable);
		registerEC(symbolTable);
		registerPolynomial(symbolTable);
	}	
	
	private static void registerInteger(ISymbolTable symbolTable) 
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplInteger(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
	private static void registerString(ISymbolTable symbolTable) 
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplString(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
	private static void registerGF(ISymbolTable symbolTable) 
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplGaloisField(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
	private static void registerRC(ISymbolTable symbolTable) 
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplResidueClass(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
	private static void registerPolynomialRC(ISymbolTable symbolTable) 
			throws FFaplException {
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplPolynomialResidue(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
	private static void registerEC(ISymbolTable symbolTable)
			throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplEllipticCurve(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
	private static void registerPolynomial(ISymbolTable symbolTable)
			throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("hash",
	    		null,
	    		new FFaplInteger(),
	    		ISymbol.FUNCTION);
	    s.setProcFunc(new Hash());
	    symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    
	    symbolTable.addSymbol(
	    			new FFaplSymbol("_t1",
	    			null,
	    			new FFaplPolynomial(),
	    			ISymbol.PARAMETER));
	    symbolTable.closeScope();
	}
	
}

