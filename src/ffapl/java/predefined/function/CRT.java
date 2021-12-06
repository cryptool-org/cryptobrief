package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.Array;
import ffapl.java.classes.BInteger;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.crt.SimultaneousCongruencesProblem;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;

/**
 * Calculates the value 'x' which satisfies a number of simultaneous congruences "congruences" for different moduli "moduli"
 * using the Chinese remainder theorem with "x" and "congruences" of either Integer or Polynomial
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
public class CRT implements IPredefinedProcFunc {

    @Override
    public void execute(IVm interpreter) throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;

        b = (IJavaType) interpreter.popStack();
        a = (IJavaType) interpreter.popStack();

        if (a.typeID() != IJavaType.ARRAY || b.typeID() != IJavaType.ARRAY) {
            System.err.println("error in CRT.execute");
        }

        Array moduli = (Array) b;
        Array congruences = (Array) a;

        if (moduli.getBaseType() != IJavaType.INTEGER) {
            System.err.println("error in CRT.execute");
        }

        if (congruences.getBaseType() == IJavaType.INTEGER) {
            BInteger[] congr = new BInteger[congruences.length()];
            for (int i = 0; i < congr.length; i++) {
                congr[i] = (BInteger) congruences.getValue(i);
            }

            BInteger[] mod = new BInteger[moduli.length()];
            for (int i = 0; i < mod.length; i++) {
                mod[i] = (BInteger) moduli.getValue(i);
            }

            interpreter.pushStack(
                    new SimultaneousCongruencesProblem(congr, mod).solve());

        } else if (congruences.getBaseType() == IJavaType.POLYNOMIAL) {
            throw new FFaplAlgebraicException(null, IAlgebraicError.CRT_NOT_IMPLEMENTED);
        } else {
            System.err.println("error in CRT.execute");
        }

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
        //crt(congruences,moduli) Integer-Array - Integer-Array
        s = new FFaplPreProcFuncSymbol("crt",
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
        s.setProcFunc(new CRT());
        symbolTable.addSymbol(s);
        symbolTable.openScope(false);
        //for Parameter
        symbolTable.addSymbol(
                new FFaplSymbol("_t1",
                        null,
                        new FFaplArray(new FFaplInteger(), 1),
                        ISymbol.PARAMETER));
        symbolTable.addSymbol(
                new FFaplSymbol("_t2",
                        null,
                        new FFaplArray(new FFaplInteger(), 1),
                        ISymbol.PARAMETER));
        symbolTable.closeScope();

        //crt(congruences,moduli) Polynomial-Array - Integer-Array
        s = new FFaplPreProcFuncSymbol("crt",
                null,
                new FFaplPolynomial(),
                ISymbol.FUNCTION);
        s.setProcFunc(new CRT());
        symbolTable.addSymbol(s);
        symbolTable.openScope(false);
        //for Parameter
        symbolTable.addSymbol(
                new FFaplSymbol("_t1",
                        null,
                        new FFaplArray(new FFaplPolynomial(), 1),
                        ISymbol.PARAMETER));
        symbolTable.addSymbol(
                new FFaplSymbol("_t2",
                        null,
                        new FFaplArray(new FFaplInteger(), 1),
                        ISymbol.PARAMETER));
        symbolTable.closeScope();
    }
}