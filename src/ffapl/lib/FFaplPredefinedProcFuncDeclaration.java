package ffapl.lib;

import ffapl.exception.FFaplException;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.predefined.function.AESDecrypt;
import ffapl.java.predefined.function.AESEncrypt;
import ffapl.java.predefined.function.AESRawDecrypt;
import ffapl.java.predefined.function.AESRawEncrypt;
import ffapl.java.predefined.function.CoefficientAt;
import ffapl.java.predefined.function.ConvertToInteger;
import ffapl.java.predefined.function.ConvertToString;
import ffapl.java.predefined.function.Degree;
import ffapl.java.predefined.function.EEA;
import ffapl.java.predefined.function.EvalulatePolynomial;
import ffapl.java.predefined.function.Factor;
import ffapl.java.predefined.function.FactorInteger;
import ffapl.java.predefined.function.FactorSquareFree;
import ffapl.java.predefined.function.GCD;
import ffapl.java.predefined.function.GetCharacteristic;
import ffapl.java.predefined.function.GetECPointOrder;
import ffapl.java.predefined.function.GetIrreduciblePolynomial;
import ffapl.java.predefined.function.GetNextPrime;
import ffapl.java.predefined.function.Hash;
import ffapl.java.predefined.function.IrreduciblePolynomial;
import ffapl.java.predefined.function.IsIrreducible;
import ffapl.java.predefined.function.IsPrime;
import ffapl.java.predefined.function.IsPrimitive;
import ffapl.java.predefined.function.LCM;
import ffapl.java.predefined.function.LeadingCoefficient;
import ffapl.java.predefined.function.LegendreSymbol;
import ffapl.java.predefined.function.MaxBIntegerBInteger;
import ffapl.java.predefined.function.MinBIntegerBInteger;
import ffapl.java.predefined.function.Print;
import ffapl.java.predefined.function.RandomPolynomial;
import ffapl.java.predefined.function.GetPolynomial;
import ffapl.java.predefined.function.ReadFunctions;
import ffapl.java.predefined.function.Sqrt;
import ffapl.java.predefined.function.TatePairing;
import ffapl.java.predefined.function.TatePairingWithOrder;
import ffapl.java.predefined.function.getECParameter;
import ffapl.java.predefined.function.getXasInteger;
import ffapl.java.predefined.function.getXasPolynomial;
import ffapl.java.predefined.function.getYasInteger;
import ffapl.java.predefined.function.getYasPolynomial;
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
