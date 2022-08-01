package ffapl.java.predefined.function;

import ffapl.exception.FFaplException;
import ffapl.java.classes.Array;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.crt.IntegerSimultaneousCongruencesProblem;
import ffapl.java.math.crt.PolynomialSimultaneousCongruencesProblem;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.*;

/**
 * Calculates the value 'x' which satisfies a number of simultaneous congruences "congruences" for different moduli "moduli"
 * using the Chinese remainder theorem with "x" and "congruences" of either Integer or Polynomial
 * @author Dominic Weinberger
 * @version 1.0
 *
 */
// TODO - DOMINIC - 08.12.2021: 1. What about the case when requested congruences > moduli? 2. what to return in polynomial case since [-1] might be a valid answer?
public class CRT implements IPredefinedProcFunc {

    @Override
    public void execute(IVm interpreter) throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;

        b = (IJavaType) interpreter.popStack();
        a = (IJavaType) interpreter.popStack();

        if (a.typeID() == IJavaType.ARRAY && b.typeID() == IJavaType.ARRAY) {
            Array moduli = (Array) b;
            Array congruences = (Array) a;

            if (congruences.getBaseType() == IJavaType.INTEGER && moduli.getBaseType() == IJavaType.INTEGER) {
                BInteger[] congr = new BInteger[congruences.length()];
                for (int i = 0; i < congr.length; i++) {
                    congr[i] = (BInteger) congruences.getValue(i);
                }

                BInteger[] mod = new BInteger[moduli.length()];
                for (int i = 0; i < mod.length; i++) {
                    mod[i] = (BInteger) moduli.getValue(i);
                }

                BInteger[] result = new IntegerSimultaneousCongruencesProblem(congr, mod).solve();
                interpreter.pushStack(new Array(FFaplTypeCrossTable.FFAPLINTEGER,
                                    1,
                                    result,
                                    result[0].clone(),
                                    result[0].getThread()));
            } else if (congruences.getBaseType() == IJavaType.BOOLEAN && moduli.getBaseType() == IJavaType.BOOLEAN) { // TODO - DOMINIC - 08.12.2021: BOOLEAN is given but ... that's actually wrong! POLYNOMIALRC is expected. There must a bug somewhere where the java type enum is assigned
                PolynomialRC[] congr = new PolynomialRC[congruences.length()];
                for (int i = 0; i < congr.length; i++) {
                    congr[i] = (PolynomialRC) congruences.getValue(i);
                }

                PolynomialRC[] mod = new PolynomialRC[moduli.length()];
                for (int i = 0; i < mod.length; i++) {
                    mod[i] = (PolynomialRC) moduli.getValue(i);
                }

                PolynomialRC[] result = new PolynomialSimultaneousCongruencesProblem(congr, mod).solve();
                interpreter.pushStack(new Array(FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE,
                        1,
                        result,
                        result[0].clone(),
                        result[0].getThread()));
            } else {
                System.err.println("error in CRT.execute");
            }
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
                new FFaplArray(new FFaplInteger(), 1),
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

        //crt(congruences,moduli) Z()[x]-Array - Z()[x]-Array
        s = new FFaplPreProcFuncSymbol("crt",
                null,
                new FFaplArray(new FFaplPolynomialResidue(), 1),
                ISymbol.FUNCTION);
        s.setProcFunc(new CRT());
        symbolTable.addSymbol(s);
        symbolTable.openScope(false);
        //for Parameter
        symbolTable.addSymbol(
                new FFaplSymbol("_t1",
                        null,
                        new FFaplArray(new FFaplPolynomialResidue(), 1),
                        ISymbol.PARAMETER));
        symbolTable.addSymbol(
                new FFaplSymbol("_t2",
                        null,
                        new FFaplArray(new FFaplPolynomialResidue(), 1),
                        ISymbol.PARAMETER));
        symbolTable.closeScope();
    }
}