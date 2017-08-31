/**
 * 
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Vector;

import ffapl.exception.FFaplException;
import ffapl.java.classes.Array;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.JBoolean;
import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.classes.PolynomialRCPrime;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplPolynomialResidue;
import ffapl.types.FFaplPrime;

/**
 * returns true if polynomial in polynomialring is primitive
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class IsPrimitive implements IPredefinedProcFunc {

	
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType t;
		Array arr;
		Polynomial ply;
		PolynomialRC plyrc;
		BInteger p, pm;
		BigInteger key;
		TreeMap<BigInteger, BigInteger> primeFactors;
		Vector<BigInteger> primeFactorsV;
		 
		
		t = (IJavaType) interpreter.popStack();
		primeFactorsV = new Vector<BigInteger>(0,1);
		if(t.typeID() == IJavaType.ARRAY){
			arr = (Array) t;
			p = (BInteger) interpreter.popStack();
		    ply = (Polynomial) interpreter.popStack();
		   
		   
		    
		    for( int i = 0 ; i < arr.length(); i++){
		    	primeFactorsV.add((BigInteger) arr.getValue(i));
		    }
		       
		}else if(t.typeID() == IJavaType.POLYNOMIALRC){
			ply = (PolynomialRC) t;
			p = (BInteger) ((PolynomialRC) t).characteristic();
			pm = (BInteger) p.pow(ply.degree());
		    pm = (BInteger) pm.subtract(BigInteger.ONE);
		    primeFactors = Algorithm.FactorInteger(pm);
		    for(Iterator<BigInteger> itr = primeFactors.keySet().iterator(); itr.hasNext(); ){
		    	key = itr.next();
		    	primeFactorsV.add(key.pow(primeFactors.get(key).intValue()));
		    }
		}else{
			p = (BInteger) t;
		    ply = (Polynomial) interpreter.popStack();
		    //calculate p^m - 1
		    pm = (BInteger) p.pow(ply.degree());
		    pm = (BInteger) pm.subtract(BigInteger.ONE);
		    primeFactors = Algorithm.FactorInteger(pm);
		    for(Iterator<BigInteger> itr = primeFactors.keySet().iterator(); itr.hasNext(); ){
		    	//key = ;
		    	primeFactorsV.add(itr.next());
		    }
		}
	
		plyrc = new PolynomialRCPrime(ply.polynomial(), p, ply.getThread());
	    interpreter.pushStack(new JBoolean(Algorithm.isPrimitive(plyrc, primeFactorsV)));
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
		
	    
	  //isPrimitive(f, Integer, Integer[])
		s = new FFaplPreProcFuncSymbol("isPrimitive", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsPrimitive());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t3", 
                null,
                new FFaplArray(new FFaplPrime(), 1),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    /*
	    //isPrimitive(f, Prime, Integer[])
	    s = new FFaplPreProcFuncSymbol("isPrimitive", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsPrimitive());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t3", 
                null,
                new FFaplArray(new FFaplPrime(), 1),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    
	  //*/
	  //isPrimitive(f, Integer)
		s = new FFaplPreProcFuncSymbol("isPrimitive", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsPrimitive());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	  //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    /*
	  //isPrimitive(f, Prime)
		s = new FFaplPreProcFuncSymbol("isPrimitive", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsPrimitive());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	  //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t2", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();
	    */
	  //isPrimitive(Z(p)[x])
		s = new FFaplPreProcFuncSymbol("isPrimitive", 
                null,
                new FFaplBoolean(),
                ISymbol.FUNCTION);
		s.setProcFunc(new IsPrimitive());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	  //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));
	    symbolTable.closeScope();	    
	}

}
