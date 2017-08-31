package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.JString;
import ffapl.java.classes.Record;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.interfaces.IRandomGenerator;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplEllipticCurve;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplPolynomialResidue;
import ffapl.types.FFaplPrime;
import ffapl.types.FFaplPsRandomGenerator;
import ffapl.types.FFaplRandom;
import ffapl.types.FFaplRandomGenerator;
import ffapl.types.FFaplRecord;
import ffapl.types.FFaplResidueClass;
import ffapl.types.FFaplString;

public class ConvertToString implements IPredefinedProcFunc {
	
	private static int maxArrayDim = 4;
	private static final String TOSTRING = "str";
	@Override
	public void execute(IVm interpreter) 
			throws FFaplAlgebraicException {
		IJavaType arg1 = (IJavaType) interpreter.popStack();
		JString ret;
		switch(arg1.typeID()){
			
		case IJavaType.PSRANDOMGENERATOR:
	    case IJavaType.RANDOMGENERATOR:
	    	ret = new JString(((IRandomGenerator)arg1).next().toString());
	    break;
	    
//	    case IJavaType.ARRAY:
//	    	FFaplArray myArr = (FFaplArray) arg1;
//	    	ret = new JString((myArr).toString());
//	    break;
	    
	    default:
	    	ret = new JString(arg1.toString());
		}
		interpreter.pushStack(ret);
		interpreter.funcReturn();

	}
	
	public static void registerProcFunc(ISymbolTable symbolTable)
			throws FFaplException {

		toStrInteger(symbolTable);
		toStrPolynomial(symbolTable);
		
		toStrPrime(symbolTable);
		toStrRC(symbolTable);
		toStrPolynomialRC(symbolTable);
		toStrGF(symbolTable);
		toStrRandom(symbolTable);
		toStrString(symbolTable);
		toStrBoolean(symbolTable);
		toStrRandomGenerator(symbolTable);
		toStrEC(symbolTable);
	}
	
	private static void toStrInteger(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		//TOSTRING(Integer)
		s = new FFaplPreProcFuncSymbol(TOSTRING, 
                null,
                new FFaplString(),
                ISymbol.FUNCTION);
		s.setProcFunc(new ConvertToString());
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));
	    
	   symbolTable.closeScope();
	   
	   //Array of Integers
	   
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
		                null,
		                new FFaplString(),
		                ISymbol.FUNCTION);
				s.setProcFunc(new ConvertToString());
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplInteger(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    }
	}
	
	
	private static void toStrPrime(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;   
	   //Array of Primes --> Simple Primes are within the Scope of Integer
	   
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
		                null,
		                new FFaplString(),
		                ISymbol.FUNCTION);
				s.setProcFunc(new ConvertToString());
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplPrime(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    }
	}
	private static void toStrPolynomial(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		
		s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplPolynomial(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		   //Array of Polynoms
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplPolynomial(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	
	}
	
	private static void toStrRC(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		
		//TOSTRING(ResidueClass)
				s = new FFaplPreProcFuncSymbol(TOSTRING, 
		                null,
		                new FFaplString(),
		                ISymbol.FUNCTION);
				s.setProcFunc(new ConvertToString());
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplResidueClass(),
		                ISymbol.PARAMETER));
			  
			   symbolTable.closeScope();
			   
			   //Array of ResidueClasses
			   
			    for (int i = 1; i <= maxArrayDim; i++){
			    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
				                null,
				                new FFaplString(),
				                ISymbol.FUNCTION);
						s.setProcFunc(new ConvertToString());
						symbolTable.addSymbol(s);
					    symbolTable.openScope(false);
					    //for Parameter
					    symbolTable.addSymbol(
					    		new FFaplSymbol("_t1", 
				                null,
				                new FFaplArray(new FFaplResidueClass(), i),
				                ISymbol.PARAMETER));	
					    symbolTable.closeScope();
			    }
	}
	
	private static void toStrString(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		
		s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplString(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		
		   //Array of Strings
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplString(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	}
	
	private static void toStrPolynomialRC(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		
		s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplPolynomialResidue(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		   //Array of PolynomialResidues
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplPolynomialResidue(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	}
	
	private static void toStrGF(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		  //TOSTRING(GaloisField)
		   s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplGaloisField(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		   //Array of GF
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplGaloisField(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	}
	
	private static void toStrRandom(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		
		s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplRandom(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		   //Array of RandomNumbers
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplRandom(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	}
	
	private static void toStrBoolean(ISymbolTable symbolTable)
			throws FFaplException{
		
		FFaplPreProcFuncSymbol s;
		
		s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplBoolean(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		   //Array of Boolean
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplBoolean(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	}
	
	/*
	private static void toStrRecord(ISymbolTable symbolTable) 
			throws FFaplException{
		FFaplPreProcFuncSymbol s;
		 //TOSTRING(Record)
		   s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplRecord(),
	               ISymbol.PARAMETER));
		  
			   symbolTable.closeScope();	
	} */
		
	private static void toStrRandomGenerator(ISymbolTable symbolTable)
			throws FFaplException{
		FFaplPreProcFuncSymbol s;
		 //PseudoRandom Generator
		   s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplPsRandomGenerator(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		 //Random Generator
		   s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplRandomGenerator(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		   //Array of Polynoms
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	//Pseudo RandomGen
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplPsRandomGenerator(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
				    
				    s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplRandomGenerator(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
		    
		
	} 
	
	private static void toStrEC(ISymbolTable symbolTable) 
		throws FFaplException{
		FFaplPreProcFuncSymbol s;
		 //PseudoRandom Generator
		   s = new FFaplPreProcFuncSymbol(TOSTRING, 
	               null,
	               new FFaplString(),
	               ISymbol.FUNCTION);
			s.setProcFunc(new ConvertToString());
			symbolTable.addSymbol(s);
		    symbolTable.openScope(false);
		    //for Parameter
		    symbolTable.addSymbol(
		    		new FFaplSymbol("_t1", 
	               null,
	               new FFaplEllipticCurve(),
	               ISymbol.PARAMETER));
		  
		   symbolTable.closeScope();
		   
		 //Array of EC
		   
		    for (int i = 1; i <= maxArrayDim; i++){
		    	 s = new FFaplPreProcFuncSymbol(TOSTRING, 
			                null,
			                new FFaplString(),
			                ISymbol.FUNCTION);
					s.setProcFunc(new ConvertToString());
					symbolTable.addSymbol(s);
				    symbolTable.openScope(false);
				    //for Parameter
				    symbolTable.addSymbol(
				    		new FFaplSymbol("_t1", 
			                null,
			                new FFaplArray(new FFaplEllipticCurve(), i),
			                ISymbol.PARAMETER));	
				    symbolTable.closeScope();
		    }
	}
}

