/**
 *
 */
package ffapl.java.predefined.function;

import java.math.BigInteger;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.math.Algorithm;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomialResidue;


/**
 * JacobiSymbol
 *
 * @author Julian Weichinger
 * @version 1.0
 */
public class JacobiSymbol implements IPredefinedProcFunc {

    @Override
    public void execute(IVm interpreter) throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        BigInteger result = new BigInteger("-1");

        b = (IJavaType) interpreter.popStack();
        a = (IJavaType) interpreter.popStack();
        if (a.typeID() == IJavaType.POLYNOMIALRC && b.typeID() == IJavaType.POLYNOMIALRC) {
            result = Algorithm.jacobiSymbol((PolynomialRC) a, (PolynomialRC) b);
        } else if (a instanceof BInteger && b instanceof BInteger) {
            Object[] messages = {"jacobiSymbol(" + a + ", " + b + ")", b};
            if (((BInteger) b).mod(BigInteger.TWO).equals(BigInteger.ZERO) || ((BInteger) b).compareTo(BigInteger.valueOf(4)) < 0){
                throw new FFaplAlgebraicException(messages,IAlgebraicError.NOT_DEFINED);
            }
            if (((BInteger) a).compareTo(BigInteger.ZERO) < 0) {
                a = (BInteger)((BInteger) a).negate();
                BigInteger modResult = ((BInteger) b).mod(BigInteger.valueOf(4));
                result = Algorithm.jacobiSymbol(((BInteger) a), ((BInteger) b));
                if(modResult.equals(BigInteger.valueOf(3))) result = result.multiply(BigInteger.valueOf(-1));
            }else{
                result = Algorithm.jacobiSymbol(((BInteger) a), ((BInteger) b));
            }
        } else {
            System.err.println("error in JacobiSymbol.execute");
        }
        interpreter.pushStack(new BInteger(result, null));
        interpreter.funcReturn();
    }

    /**
     * Registers predefined Function in Symbol table
     *
     * @param symbolTable
     * @throws FFaplException
     */
    public static void registerProcFunc(ISymbolTable symbolTable) throws FFaplException {
      //JacobiSymbol(a,b) Integer-Integer
        FFaplPreProcFuncSymbol  s = new FFaplPreProcFuncSymbol("jacobiSymbol",
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
        s.setProcFunc(new JacobiSymbol());
        symbolTable.addSymbol(s);
        symbolTable.openScope(false);
        //insert Parameter in symbol table scope
        symbolTable.addSymbol(
                new FFaplSymbol("_t1",
                        null,
                        new FFaplInteger(),
                        ISymbol.PARAMETER));
        symbolTable.addSymbol(
                new FFaplSymbol("_t2",
                        null,
                        new FFaplInteger(),
                        ISymbol.PARAMETER));
        symbolTable.closeScope();


        //JacobiSymbol(a,b) Z()[x] -  Z()[x]
        s = new FFaplPreProcFuncSymbol("jacobiSymbol",
                null,
                new FFaplInteger(),
                ISymbol.FUNCTION);
        s.setProcFunc(new JacobiSymbol());
        symbolTable.addSymbol(s);
        symbolTable.openScope(false);
        //for Parameter
        symbolTable.addSymbol(
                new FFaplSymbol("_t1",
                        null,
                        new FFaplPolynomialResidue(),
                        ISymbol.PARAMETER));
        symbolTable.addSymbol(
                new FFaplSymbol("_t2",
                        null,
                        new FFaplPolynomialResidue(),
                        ISymbol.PARAMETER));
        symbolTable.closeScope();
    }
}