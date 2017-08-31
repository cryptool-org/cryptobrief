package ffapl.java.predefined.function;

//import com.sun.xml.internal.fastinfoset.util.CharArray;
import java.math.BigInteger;
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
import ffapl.utils.FFaplSymbolTablePrinter;
import ffapl.visitor.FFaplJavaInterpreterVisitor;
import java.util.EmptyStackException;
import java.util.LinkedList;
import sunset.gui.FFaplJFrame;
import sunset.gui.listener.ActionListenerCloseTab;
import sunset.gui.listener.ActionListenerInputField;

public class ReadFunctions implements IPredefinedProcFunc {
	
	private int rType;
	private FFaplLogger logger;
	private static ISymbolTable symbolTable;
        private boolean correctInput;
        
        private final String POLYREGEX = "\\[[+-]?([1-9][0-9]*)?(x|[1-9][0-9]*)(\\^[1-9][0-9]*)?([+-]((([1-9][0-9]*)?x)|(([1-9][0-9]*)x?))(\\^[1-9][0-9]*)?)*\\]";
        private final String INTREGEX = "(0|[+-]?[1-9][0-9]*(\\^[1-9][0-9]*)?)";
        
	@Override
	public void execute(IVm interpreter) throws FFaplAlgebraicException{
		// TODO Auto-generated method stub
		// System.out.println(interpreter);
                try{
                    interpreter.peekStack();
                    IJavaType msg = (IJavaType) interpreter.popStack();
                    logger.log(ILevel.RESULT, msg.toString() + ": ");
                }catch(EmptyStackException e){
                    //Do nothing, just function without parameters ;)
                }
		Vector<ISymbol> a;
                LinkedList<String> input = ActionListenerInputField.getInput();
                correctInput = false;
                String s = "";
                
                while(!correctInput){                        
                        int i = ActionListenerInputField.getTabbedPane().getSelectedIndex();
                        FFaplJFrame.getInputField().setEnabled(true);
                        FFaplJFrame.getInputField().grabFocus();
                        try {
                                a = symbolTable.lookupsimilar(new FFaplSymbol("", IJavaType.INTEGER));
                                ActionListenerInputField.addMap(i, Thread.currentThread()); //Maps the selected tab index at the current Thread
                                synchronized (input) {
                                    // wait for input from field
                                    input.wait();
                                    while(!ActionListenerInputField.getMap(ActionListenerInputField.getTabbedPane().getSelectedIndex()).equals(Thread.currentThread()))
                                        input.wait();       //wait until selected Tab Maps to current running thread
                                    s = input.remove(0).trim();    //take input value
                                    ActionListenerInputField.deleteMap(i);      //delete the map of this index
                                }
                                Thread.sleep(10);  //because if you do not wait, another lock afterwards would cause the Textfield to be disabled
                        } catch (FFaplException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } catch (InterruptedException ex) {
                                ActionListenerInputField.deleteMap(i);
                                FFaplJFrame.getInputField().setText("");
                                FFaplJFrame.getInputField().setEnabled(false);

                                Thread.currentThread().stop();  //deprecated, but the only way to stop the thread instantly
                        }
                        /*
                        if(validIntRegex(s))
                            rType = IJavaType.INTEGER;
                        else if(validPolyRegex(s))
                            rType = IJavaType.POLYNOMIAL;
                        else if(s.equals("true") || s.equals("false"))
                            rType = IJavaType.BOOLEAN;
                        else
                            rType = IJavaType.STRING;
                        */

                        switch(rType){
                            case IJavaType.STRING:
                                if(s == null){
                                    //s = logger.getVal();
                                    s="";
                                }
                                interpreter.pushStack(new JString(s));
                                correctInput = true;

                            break;
                            case IJavaType.INTEGER:
                            case IJavaType.RESIDUECLASS:
                                    Thread _thread = new Thread();
                                            BigInteger temp;
                                            BInteger ret;
                                            if(validIntRegex(s)){
                                                    System.out.println("Regex valid");
                                                    if(s.contains("^")){
                                                            String[] splitted = s.split("\\^");
                                                            temp = Algorithm.squareAndMultiply(BigInteger.valueOf((Long.valueOf(splitted[0]))),
                                                                            BigInteger.valueOf((Long.valueOf(splitted[1]))),
                                                                            _thread);
                                                    }else{
                                                            temp = BigInteger.valueOf(Long.parseLong(s));
                                                    }
                                                    ret = new BInteger(temp, _thread);
                                                    s = ret.toString();
                                                    interpreter.pushStack(ret);
                                                    correctInput = true;
                                            } else {
                                                    System.out.println("Regex invalid");
        //                                            ret = new BInteger(BigInteger.ZERO, _thread);
        //                                            s = "0"; //for logging purpose to show the added value

                                                    wrongInput(s);
                                                    //ActionListenerCloseTab.setProgramRunning(false);
                                                    //Thread.currentThread().stop();
                                            }

                            break;

                            /*case IJavaType.POLYNOMIALRC:
                            case IJavaType.GALOISFIELD:
                                    Polynomial r;
                                    Thread t = new Thread();
                                    if(validPolyRegex(s) || validIntRegex(s)){
                                            r = new Polynomial(s, t);
                                    } else{
                                            r = new Polynomial(BigInteger.ZERO, BigInteger.ZERO, t);
                                    }
                                    interpreter.pushStack(r);
                            break;
                            */
                            case IJavaType.BOOLEAN:
                                    interpreter.pushStack(new JBoolean(Boolean.valueOf(s)));
                                    break;
                            case IJavaType.POLYNOMIALRC:
                            case IJavaType.GALOISFIELD:
                            case IJavaType.POLYNOMIAL:
                                    Polynomial poly = null;
                                    if(validPolyRegex(s)||validIntRegex(s)){
                                            poly = new Polynomial(s, new Thread());
                                            s = poly.toString();
                                            interpreter.pushStack(poly);
                                            correctInput = true;
                                    } else{
                                            System.out.println("regex is false");
                                            //ActionListenerCloseTab.setProgramRunning(false);
                                            //Thread.currentThread().stop();
                                            wrongInput(s);
                                    }
                                    
                                    break;
                                    //new FFaplInterpreter(logger, new ByteArrayInputStream(s.getBytes()));
                            case IJavaType.ELLIPTICCURVE:
                                    EllipticCurve ec = null;
                                    s = s.replaceAll("\\s", "");
                                    if(validECRegex(s)){
                                            ec = new EllipticCurve(s, new Thread());
                                            s = ec.toString();
                                            interpreter.pushStack(ec);
                                            correctInput = true;
                                    }else
                                            wrongInput(s);

                                    
                                    break;
                            default: //should never happen
                                    s = "Kein Wert zugewiesen";
                            break;
                        }
                }
                logger.log(ILevel.RESULT, s.toString() + "\n");
                interpreter.funcReturn();
	}
        
	public ReadFunctions(int rType, FFaplLogger logger){
		this.rType = rType;
		this.logger = logger;
	}
	
	public ReadFunctions(int rType, FFaplLogger logger, ISymbolTable table){
		this.rType = rType;
		this.logger = logger;
		this.symbolTable = table;
	}
	
	public static void registerProcFunc(ISymbolTable symbolTable, FFaplLogger logger)
			throws FFaplException {
		readInteger(symbolTable, logger);
		readString(symbolTable, logger);
		readBool(symbolTable, logger);
                readPoly(symbolTable, logger);
                readEC(symbolTable, logger);
	}
        
	private static void readInteger(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
                s = new FFaplPreProcFuncSymbol("readInt", 
                    null,
                    new FFaplInteger(),
                    ISymbol.FUNCTION);
                    s.setProcFunc(new ReadFunctions(IJavaType.INTEGER, logger, symbolTable));
		symbolTable.addSymbol(s);
                
		s = new FFaplPreProcFuncSymbol("readInt", 
                    null,
                    new FFaplInteger(),
                    ISymbol.FUNCTION);
                    s.setProcFunc(new ReadFunctions(IJavaType.INTEGER, logger, symbolTable));
		symbolTable.addSymbol(s);
                symbolTable.openScope(false);
                //for Parameter
                symbolTable.addSymbol(
                            new FFaplSymbol("_msg", 
                    null,
                    new FFaplString(),
                    ISymbol.PARAMETER));
                symbolTable.closeScope();
	}
	
	private static void readString(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
                s = new FFaplPreProcFuncSymbol("readStr", 
                    null,
                    new FFaplString(),
                    ISymbol.FUNCTION);
                    s.setProcFunc(new ReadFunctions(IJavaType.STRING, logger, symbolTable));
		symbolTable.addSymbol(s);
                
		s = new FFaplPreProcFuncSymbol("readStr", 
                    null,
                    new FFaplString(),
                    ISymbol.FUNCTION);
                    s.setProcFunc(new ReadFunctions(IJavaType.STRING, logger, symbolTable));
		symbolTable.addSymbol(s);
                symbolTable.openScope(false);
                //for Parameter
                symbolTable.addSymbol(
                            new FFaplSymbol("_msg", 
                    null,
                    new FFaplString(),
                    ISymbol.PARAMETER));
                symbolTable.closeScope();
	}

	private static void readBool(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
                s = new FFaplPreProcFuncSymbol("readBool",
				null,
				new FFaplBoolean(),
				ISymbol.FUNCTION);
		s.setProcFunc(new ReadFunctions(IJavaType.BOOLEAN, logger, symbolTable));
		symbolTable.addSymbol(s);
                
		s = new FFaplPreProcFuncSymbol("readBool",
				null,
				new FFaplBoolean(),
				ISymbol.FUNCTION);
		s.setProcFunc(new ReadFunctions(IJavaType.BOOLEAN, logger, symbolTable));
		symbolTable.addSymbol(s);
                symbolTable.openScope(false);
                //for Parameter
                symbolTable.addSymbol(
                            new FFaplSymbol("_msg", 
                    null,
                    new FFaplString(),
                    ISymbol.PARAMETER));
                symbolTable.closeScope();
	}
        
        private static void readPoly(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
                s = new FFaplPreProcFuncSymbol("readPoly",
				null,
				new FFaplPolynomial(),
				ISymbol.FUNCTION);
		s.setProcFunc(new ReadFunctions(IJavaType.POLYNOMIAL, logger, symbolTable));
		symbolTable.addSymbol(s);
                
		s = new FFaplPreProcFuncSymbol("readPoly",
				null,
				new FFaplPolynomial(),
				ISymbol.FUNCTION);
		s.setProcFunc(new ReadFunctions(IJavaType.POLYNOMIAL, logger, symbolTable));
		symbolTable.addSymbol(s);
                symbolTable.openScope(false);
                //for Parameter
                symbolTable.addSymbol(
                            new FFaplSymbol("_msg", 
                    null,
                    new FFaplString(),
                    ISymbol.PARAMETER));
                symbolTable.closeScope();
	}
        
        private static void readEC(ISymbolTable symbolTable, FFaplLogger logger) throws FFaplException{
		FFaplPreProcFuncSymbol s;
                s = new FFaplPreProcFuncSymbol("readEC",
				null,
				new FFaplEllipticCurve(),
				ISymbol.FUNCTION);
		s.setProcFunc(new ReadFunctions(IJavaType.ELLIPTICCURVE, logger, symbolTable));
		symbolTable.addSymbol(s);
                
		s = new FFaplPreProcFuncSymbol("readEC",
				null,
				new FFaplEllipticCurve(),
				ISymbol.FUNCTION);
		s.setProcFunc(new ReadFunctions(IJavaType.ELLIPTICCURVE, logger, symbolTable));
		symbolTable.addSymbol(s);
                symbolTable.openScope(false);
                //for Parameter
                symbolTable.addSymbol(
                            new FFaplSymbol("_msg", 
                    null,
                    new FFaplString(),
                    ISymbol.PARAMETER));
                symbolTable.closeScope();
	}
        
        private boolean validECRegex(String input){
		Pattern p;
                String myRegex = "<<((" + INTREGEX + "," + INTREGEX + ")|(" + POLYREGEX + "," + POLYREGEX + ")|(PAI))>>";
		p = Pattern.compile(myRegex);
		Matcher matcher = p.matcher(input);
		return matcher.find();
	}

	private boolean validPolyRegex(String input){
		Pattern p;
                String myRegex = POLYREGEX;
		p = Pattern.compile(myRegex);
		Matcher matcher = p.matcher(input);
		return matcher.find();
	}
	
	private boolean validIntRegex(String input){
		Pattern p;
		String myRegex = INTREGEX;
		p = Pattern.compile(myRegex);
		Matcher matcher = p.matcher(input);
		return matcher.matches();
				
	}
        
        private void wrongInput(String s){
                logger.log(ILevel.ERROR, "\n'" + s.toString() + FFaplProperties.getInstance().getProperty(2001));
        }
}
