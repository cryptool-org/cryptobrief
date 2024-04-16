package ffapl.java.predefined.function;

import java.math.BigInteger;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ffapl.exception.FFaplException;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.EllipticCurve;
import ffapl.java.classes.JBoolean;
import ffapl.java.classes.JString;
import ffapl.java.classes.Polynomial;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.ILevel;
import ffapl.java.interfaces.IPredefinedProcFunc;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.math.Algorithm;
import ffapl.java.util.FFaplReader;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplEllipticCurve;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPolynomial;
import ffapl.types.FFaplString;
import ffapl.utils.FFaplProperties;

public class ReadFunctions implements IPredefinedProcFunc {

    private int rType;
    private FFaplLogger logger;
    private FFaplReader inputReader;
    private ISymbolTable symbolTable;
    private boolean correctInput;

    private static final String INTREGEX = "(0|[+-]?[1-9][0-9]*(\\^[1-9][0-9]*)?)";
    private static final String POLYREGEX = "\\[[+-]?([1-9][0-9]*)?(x|[1-9][0-9]*)(\\^[1-9][0-9]*)?([+-]((([1-9][0-9]*)?x)|(([1-9][0-9]*)x?))(\\^[1-9][0-9]*)?)*\\]";
    private static final String ECREGEX = "<<((" + INTREGEX + "," + INTREGEX + ")|(" + POLYREGEX + "," + POLYREGEX + ")|(PAI))>>";

    private static Pattern intPattern = Pattern.compile(INTREGEX);
    private static Pattern polyPattern = Pattern.compile(POLYREGEX);
    private static Pattern ecPattern = Pattern.compile(ECREGEX);

    @Override
    public void execute(IVm interpreter) throws FFaplAlgebraicException {
        try {
            interpreter.peekStack();
            IJavaType msg = (IJavaType) interpreter.popStack();
            logger.log(ILevel.RESULT, msg.toString() + ": ");
        } catch (EmptyStackException e) {
            //Do nothing, just function without parameters ;)
        }

        correctInput = false;
        String s = "";

        while (!correctInput) {
            s = inputReader.getInput(symbolTable);

            //if (validIntRegex(s))
            //    rType = IJavaType.INTEGER;
            //else if (validPolyRegex(s))
            //    rType = IJavaType.POLYNOMIAL;
            //else if (s.equals("true") || s.equals("false"))
            //    rType = IJavaType.BOOLEAN;
            //else
            //    rType = IJavaType.STRING;

            switch (rType) {
                case IJavaType.STRING:
                    if (s == null) {
                        //s = logger.getVal();
                        s = "";
                    }
                    interpreter.pushStack(new JString(s));
                    correctInput = true;
                    break;

                case IJavaType.INTEGER:
                case IJavaType.RESIDUECLASS:
                    Thread _thread = new Thread();
                    BigInteger temp;
                    BInteger ret;
                    if (validIntRegex(s)) {
                        if (s.contains("^")) {
                            String[] splitted = s.split("\\^");
                            temp = Algorithm.squareAndMultiply(BigInteger.valueOf((Long.valueOf(splitted[0]))),
                                                               BigInteger.valueOf((Long.valueOf(splitted[1]))),
                                                               _thread);
                        } else {
                            temp = BigInteger.valueOf(Long.parseLong(s));
                        }

                        ret = new BInteger(temp, _thread);
                        s = ret.toString();
                        interpreter.pushStack(ret);
                        correctInput = true;
                    } else {
                        wrongInput(s);
                    }
                    break;

                case IJavaType.BOOLEAN:
                    interpreter.pushStack(new JBoolean(Boolean.valueOf(s)));
                    break;

                case IJavaType.POLYNOMIALRC:
                case IJavaType.GALOISFIELD:
                case IJavaType.POLYNOMIAL:
                    Polynomial poly = null;
                    if (validPolyRegex(s) || validIntRegex(s)) {
                        poly = new Polynomial(s, new Thread());
                        s = poly.toString();
                        interpreter.pushStack(poly);
                        correctInput = true;
                    } else {
                        wrongInput(s);
                    }
                    break;

                case IJavaType.ELLIPTICCURVE:
                    EllipticCurve ec = null;
                    s = s.replaceAll("\\s", "");
                    if (validECRegex(s)) {
                        ec = new EllipticCurve(s, new Thread());
                        s = ec.toString();
                        interpreter.pushStack(ec);
                        correctInput = true;
                    } else {
                        wrongInput(s);
                    }
                    break;

                default: //should never happen
                    s = "Kein Wert zugewiesen";
                    break;
            }
        }

        logger.log(ILevel.RESULT, s.toString() + "\n");
        interpreter.funcReturn();
    }

    public ReadFunctions(int rType, FFaplLogger logger, FFaplReader inputReader) {
        this.rType = rType;
        this.logger = logger;
        this.inputReader = inputReader;
    }

    public ReadFunctions(int rType, FFaplLogger logger, FFaplReader inputReader, ISymbolTable table) {
        this.rType = rType;
        this.logger = logger;
        this.inputReader = inputReader;
        this.symbolTable = table;
    }

    public static void registerProcFunc(ISymbolTable symbolTable, FFaplReader inputReader, FFaplLogger logger) throws FFaplException {
        if (inputReader != null) {
            readInteger(symbolTable, inputReader, logger);
            readString(symbolTable, inputReader, logger);
            readBool(symbolTable, inputReader, logger);
            readPoly(symbolTable, inputReader, logger);
            readEC(symbolTable, inputReader, logger);
        }
    }

    private static void readInteger(ISymbolTable symbolTable, FFaplReader inputReader, FFaplLogger logger) throws FFaplException {
        FFaplPreProcFuncSymbol s;
        s = new FFaplPreProcFuncSymbol("readInt", null, new FFaplInteger(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.INTEGER, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);

        s = new FFaplPreProcFuncSymbol("readInt", null, new FFaplInteger(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.INTEGER, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);
        symbolTable.openScope(false); // for Parameter
        symbolTable.addSymbol(new FFaplSymbol("_msg", null, new FFaplString(), ISymbol.PARAMETER));
        symbolTable.closeScope();
    }

    private static void readString(ISymbolTable symbolTable, FFaplReader inputReader, FFaplLogger logger) throws FFaplException {
        FFaplPreProcFuncSymbol s;
        s = new FFaplPreProcFuncSymbol("readStr", null, new FFaplString(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.STRING, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);

        s = new FFaplPreProcFuncSymbol("readStr", null, new FFaplString(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.STRING, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);
        symbolTable.openScope(false); // for Parameter
        symbolTable.addSymbol(new FFaplSymbol("_msg", null, new FFaplString(), ISymbol.PARAMETER));
        symbolTable.closeScope();
    }

    private static void readBool(ISymbolTable symbolTable, FFaplReader inputReader, FFaplLogger logger) throws FFaplException {
        FFaplPreProcFuncSymbol s;
        s = new FFaplPreProcFuncSymbol("readBool", null, new FFaplBoolean(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.BOOLEAN, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);

        s = new FFaplPreProcFuncSymbol("readBool", null, new FFaplBoolean(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.BOOLEAN, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);
        symbolTable.openScope(false); // for Parameter
        symbolTable.addSymbol(new FFaplSymbol("_msg", null, new FFaplString(), ISymbol.PARAMETER));
        symbolTable.closeScope();
    }

    private static void readPoly(ISymbolTable symbolTable, FFaplReader inputReader, FFaplLogger logger) throws FFaplException {
        FFaplPreProcFuncSymbol s;
        s = new FFaplPreProcFuncSymbol("readPoly", null, new FFaplPolynomial(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.POLYNOMIAL, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);

        s = new FFaplPreProcFuncSymbol("readPoly", null, new FFaplPolynomial(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.POLYNOMIAL, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);
        symbolTable.openScope(false); // for Parameter
        symbolTable.addSymbol(new FFaplSymbol("_msg", null, new FFaplString(), ISymbol.PARAMETER));
        symbolTable.closeScope();
    }

    private static void readEC(ISymbolTable symbolTable, FFaplReader inputReader, FFaplLogger logger) throws FFaplException {
        FFaplPreProcFuncSymbol s;
        s = new FFaplPreProcFuncSymbol("readEC", null, new FFaplEllipticCurve(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.ELLIPTICCURVE, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);

        s = new FFaplPreProcFuncSymbol("readEC", null, new FFaplEllipticCurve(), ISymbol.FUNCTION);
        s.setProcFunc(new ReadFunctions(IJavaType.ELLIPTICCURVE, logger, inputReader, symbolTable));
        symbolTable.addSymbol(s);
        symbolTable.openScope(false); // for Parameter
        symbolTable.addSymbol(new FFaplSymbol("_msg", null, new FFaplString(), ISymbol.PARAMETER));
        symbolTable.closeScope();
    }

    private boolean validECRegex(String input) {
        Matcher matcher = ecPattern.matcher(input);
        return matcher.find();
    }

    private boolean validPolyRegex(String input) {
        Matcher matcher = polyPattern.matcher(input);
        return matcher.find();
    }

    private boolean validIntRegex(String input) {
        Matcher matcher = intPattern.matcher(input);
        return matcher.matches();
    }

    private void wrongInput(String s) {
        logger.log(ILevel.ERROR, "\n'" + s.toString() + FFaplProperties.getInstance().getProperty(2001));
    }
}
