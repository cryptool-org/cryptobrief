package ffapl.lib;

import ffapl.exception.FFaplException;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.predefined.function.*;
import ffapl.lib.interfaces.ISymbolTable;

public class FFaplPredefinedProcFuncDeclaration {

	/**
	 * Fills symbol Table with predefined procedures and functions
	 * @param symbolTable
	 * @param logger
	 * @param thread
	 * @throws FFaplException
	 */
	public static void fill(ISymbolTable symbolTable, FFaplLogger logger, Thread thread) throws FFaplException{
		
		Print.registerProcFunc(symbolTable, logger);
		MaxBIntegerBInteger.registerProcFunc(symbolTable);
		MinBIntegerBInteger.registerProcFunc(symbolTable);
		IsIrreducible.registerProcFunc(symbolTable);
		IsPrimitive.registerProcFunc(symbolTable);
		RandomPolynomial.registerProcFunc(symbolTable);
		IrreduciblePolynomial.registerProcFunc(symbolTable);
		IsPrime.registerProcFunc(symbolTable);
		GetNextPrime.registerProcFunc(symbolTable);
		GetCharacteristic.registerProcFunc(symbolTable);
		GetIrreduciblePolynomial.registerProcFunc(symbolTable);
		CRT.registerProcFunc(symbolTable);
		GCD.registerProcFunc(symbolTable);
		LCM.registerProcFunc(symbolTable);
		EEA.registerProcFunc(symbolTable);
		FactorInteger.registerProcFunc(symbolTable);
		LeadingCoefficient.registerProcFunc(symbolTable);
		Degree.registerProcFunc(symbolTable);
		CoefficientAt.registerProcFunc(symbolTable);
		Factor.registerProcFunc(symbolTable);
		FactorSquareFree.registerProcFunc(symbolTable);
		EvalulatePolynomial.registerProcFunc(symbolTable);
		ConvertToInteger.registerProcFunc(symbolTable);
		GetPolynomial.registerProcFunc(symbolTable);
		ConvertToString.registerProcFunc(symbolTable);
		Hash.registerProcFunc(symbolTable);
		ReadFunctions.registerProcFunc(symbolTable, logger);
		Sqrt.registerProcFunc(symbolTable);
		LegendreSymbol.registerProcFunc(symbolTable);
		JacobiSymbol.registerProcFunc(symbolTable);
		getXasInteger.registerProcFunc(symbolTable);
		getYasInteger.registerProcFunc(symbolTable);
		getXasPolynomial.registerProcFunc(symbolTable);
		getYasPolynomial.registerProcFunc(symbolTable);
		
		TatePairing.registerProcFunc(symbolTable);
		TatePairingWithOrder.registerProcFunc(symbolTable);
		GetECPointOrder.registerProcFunc(symbolTable);
		getECParameter.registerProcFunc(symbolTable);
		
		AESEncrypt.registerProcFunc(symbolTable);
		AESDecrypt.registerProcFunc(symbolTable);
		AESRawEncrypt.registerProcFunc(symbolTable);
		AESRawDecrypt.registerProcFunc(symbolTable);
	}
	
	
	
	
}
