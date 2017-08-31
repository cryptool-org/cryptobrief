/**
 * 
 */
package ffapl.java.predefined.function;



import ffapl.exception.FFaplException;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.ILevel;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.interfaces.IRandomGenerator;
import ffapl.java.logging.FFaplLogger;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
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
import ffapl.types.FFaplResidueClass;
import ffapl.types.FFaplString;

/**
 * Prints the value
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Print implements IPredefinedProcFunc {

	
	private FFaplLogger _logger;
	private boolean _newLine;
	private static int maxArrayDim = 4;
	
	/**
	 * Constructor
	 * @param logger
	 * @param newLine
	 */
	public Print (FFaplLogger logger, boolean newLine){
		_logger = logger;
		_newLine = newLine;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IPredefinedProcFunc#execute(ffapl.lib.interfaces.ICodeInterpreter)
	 */
	@Override
	public void execute(IVm interpreter)
			throws FFaplAlgebraicException {
		IJavaType a;
		String msg;
		a = (IJavaType) interpreter.popStack();
		
	    switch(a.typeID()){
	    case IJavaType.GALOISFIELD:
	    	//msg =  a + " in " + a.classInfo();
	    	msg = a.classInfo() + ": " + a;
	    	break;
	    case IJavaType.RESIDUECLASS:
	    	//msg =  a + " in " + a.classInfo();
	    	msg = a.classInfo() + ": " + a;
	    	break;
	    case IJavaType.POLYNOMIALRC:
	    	//msg = a + " in " + a.classInfo();
	    	msg = a.classInfo() + ": " + a;
	    	break;
	    case IJavaType.PSRANDOMGENERATOR:
	    case IJavaType.RANDOMGENERATOR:
	    	msg = ((IRandomGenerator)a).next().toString();
	    	break;
	    case IJavaType.ELLIPTICCURVE:
    		msg = a.classInfo() + ": " + a;
	    	break;
	    default:
	    	msg = a.toString();
	    }
	    if (_newLine){
	    	msg = msg + "\n";
	    }
	    _logger.log(ILevel.RESULT, msg);
	}

	
	/**
	 * Registers predefined Function in Symbol table
	 * @param symbolTable
	 * @param logger
	 * @throws FFaplException
	 */
	public static void registerProcFunc(ISymbolTable symbolTable, FFaplLogger logger)
			throws FFaplException {
		printInteger(symbolTable, logger);
		printRandom(symbolTable, logger);
		printEC(symbolTable, logger);
		printGF(symbolTable, logger);
		printRC(symbolTable, logger);
		printPrime(symbolTable, logger);
		printPolynomial(symbolTable, logger);
		printPolynomialRC(symbolTable, logger);
		printBoolean(symbolTable, logger);
		printString(symbolTable, logger);
		printRandomGenerator(symbolTable, logger);
		
	}
	
	/**
	 * procedure print(Integer) and println(Integer)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printInteger(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	   
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplInteger(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    
	    //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplInteger(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(Random) and println(Random)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printRandom(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplRandom(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	   
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplRandom(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();	    
	}

	/**
	 * procedure print(GaloisField) and println(GaloisField)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printEC(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplEllipticCurve(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplEllipticCurve(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplEllipticCurve(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	
	

	/**
	 * procedure print(GaloisField) and println(GaloisField)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printGF(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplGaloisField(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplGaloisField(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(ResidueClass) and println(ResidueClass)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printRC(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplResidueClass(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    //println
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplResidueClass(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplResidueClass(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(Prime) and println(Prime)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printPrime(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		/*
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPrime(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    */
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplPrime(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(Polynomial) and println(Polynomial)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printPolynomial(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomial(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplPolynomial(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(PolynomialRC) and println(PolynomialRC)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printPolynomialRC(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPolynomialResidue(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplPolynomialResidue(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(Boolean) println(Boolean)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printBoolean(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplBoolean(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplBoolean(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplBoolean(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(String) and println(String)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printString(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplString(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplString(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	  //Array support
	    for (int i = 1; i <= maxArrayDim; i++){
	    	 s = new FFaplPreProcFuncSymbol("print", 
		                null,
		                null,
		                ISymbol.PROCEDURE);
				s.setProcFunc(new Print(logger, false));
				symbolTable.addSymbol(s);
			    symbolTable.openScope(false);
			    //for Parameter
			    symbolTable.addSymbol(
			    		new FFaplSymbol("_t1", 
		                null,
		                new FFaplArray(new FFaplString(), i),
		                ISymbol.PARAMETER));	
			    symbolTable.closeScope();
	    	
	    	s = new FFaplPreProcFuncSymbol("println", 
	                null,
	                null,
	                ISymbol.PROCEDURE);
			s.setProcFunc(new Print(logger, true));
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
	
	/**
	 * procedure print(PseudoRandomGenerator) and println(PseudoRandomGenerator)
	 * procedure print(RandomGenerator) println(RandomGenerator)
	 * @param symbolTable
	 * @throws FFaplException
	 */
	private static void printRandomGenerator(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		// PseudoRandomGenerator
		FFaplPreProcFuncSymbol s;
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPsRandomGenerator(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplPsRandomGenerator(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	 // TrueRandomGenerator
		s = new FFaplPreProcFuncSymbol("print", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, false));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplRandomGenerator(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
	    
	    s = new FFaplPreProcFuncSymbol("println", 
                null,
                null,
                ISymbol.PROCEDURE);
		s.setProcFunc(new Print(logger, true));
		symbolTable.addSymbol(s);
	    symbolTable.openScope(false);
	    //for Parameter
	    symbolTable.addSymbol(
	    		new FFaplSymbol("_t1", 
                null,
                new FFaplRandomGenerator(),
                ISymbol.PARAMETER));	
	    symbolTable.closeScope();
		
	}

}
