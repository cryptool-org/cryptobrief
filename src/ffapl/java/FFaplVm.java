package ffapl.java;

import java.math.BigInteger;
import java.util.*;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ffapl.FFaplInterpreter;
import ffapl.java.classes.*;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IRandomGenerator;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.predefined.function.Print;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplTypeCrossTable;
import ffapl.types.Type;

/**
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplVm implements IVm {

    Thread _thread;
    ISymbol originalNode = null;
    List<ISymbol> list = new ArrayList<>();
    Record recordMap = new Record();
    JSONObject recordJSON = new JSONObject();
    boolean restartNode = false;
    List<ISymbol> nodeListPath;
    List<ISymbol> scopeListPathTemp;
    HashSet<ISymbol> nodelist = new HashSet();
    // simulates Stack for Expression
    private Stack<Object> _expressionStack;
    // Simulates the call stack
    // local variables, arguments etc.
    private List<Object> _procedureStack;
    // Logger not used yet
    //private FFaplLogger _logger;
    // Holds the type id for
    // local variables, arguments etc. in the _procedureStack
    private List<Integer> _procedureStackTypes;
    //Holds the Symbol of the declaration, otherwise null
    private List<ISymbol> _procedureStackSymbols;
    // simulates globalMemory
    private List<Object> _globalMemory;
    // simulates globalMemory Type
    private List<Integer> _globalMemoryTypes;
    //Frame pointer for procedureStack
    private int _fp;
    // points to the next available free space
    private int _sp;
    //return value of a function
    private IJavaType _rt;

    public FFaplVm(FFaplLogger logger, Thread thread) {
        _expressionStack = new Stack<Object>();
        _procedureStack = new Vector<Object>(0, 1);
        _procedureStackTypes = new Vector<Integer>(0, 1);
        _procedureStackSymbols = new Vector<ISymbol>(0, 1);
        _globalMemory = new Vector<Object>(0, 1);
        _globalMemoryTypes = new Vector<Integer>(0, 1);
        //_logger = logger;
        _fp = 0;
        _sp = 0;
        _rt = null;
        _thread = thread;

    }



    @Override
    public int allocStack(ISymbol symbol) throws FFaplAlgebraicException {
        int result;
        this._procedureStackTypes.add(symbol.getType().typeID());
        this._procedureStackSymbols.add(symbol);
         if (initvalue(symbol.getType().typeID())) {
            this._procedureStack.add(popStack());
        } else {
            this._procedureStack.add(null);
        }
        result = _sp - _fp;
        _sp = _sp + 1;
        return result;
    }

    @Override
    public void allocArray(Type type, Array initArray) throws FFaplAlgebraicException {
        Object[] elements;
        Array array;
        if (type instanceof FFaplArray) {
            elements = (Object[]) createArray(type, initArray);
            array = new Array(((FFaplArray) type).baseType().typeID(),
                    ((FFaplArray) type).getDim(),
                    elements, initArray.getjType(), _thread);
            _expressionStack.push(array);
        } else {
            Object[] arguments = {"allocArray"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
        }


    }

    @Override
    public int allocFormalParam(ISymbol symbol) {
        int result;
        this._procedureStackTypes.add(symbol.getType().typeID());
        this._procedureStackSymbols.add(symbol);
         this._procedureStack.add(null);

        result = _sp - _fp;
        _sp = _sp + 1;
        return result;
    }

    @Override
    public int alloStack(ISymbol symbol, int offset) throws FFaplAlgebraicException {
        int result;
        this._procedureStackTypes.add(_procedureStackTypes.get(offset + _fp));
        this._procedureStackSymbols.add(symbol);
        this.loadCopy(offset, false);
        this._procedureStack.add(popStack());

        result = _sp - _fp;
        _sp = _sp + 1;
         return result;
    }

    @Override
    public int allocGlobal(Type type) throws FFaplAlgebraicException {
        this._globalMemoryTypes.add(type.typeID());

        if (initvalue(type.typeID())) {
            this._globalMemory.add(popStack());
        } else {
            this._globalMemory.add(null);
        }
        return this._globalMemory.size() - 1;
    }

    @Override
    public void loadValue(int offset, boolean global) {
        IJavaType val;
        if (global) {
            val = (IJavaType) this._globalMemory.get(offset);
        } else {
            val = (IJavaType) this._procedureStack.get(offset + _fp);
        }

        if (val.typeID() != IJavaType.ARRAY) {
            this._expressionStack.push(val.clone());
        } else {
            this._expressionStack.push(val);
        }
    }

    public void loadArrayElement() throws FFaplAlgebraicException {
        Array a;
        BInteger b;
        b = (BInteger) popStack();
        a = (Array) popStack();
        if (!a.isNull()) {
            if (a.typeID() == IJavaType.ARRAY) {
                if (b.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0) {
                    this.pushStack(((Array) a).getValue(b.intValue()));
                } else {
                    Object[] arguments = {b};
                    throw new FFaplAlgebraicException(arguments, IAlgebraicError.ARR_LENGTH_TOOHIGH);
                }
            } else {
                Object[] arguments = {"loadArrayElement"};
                throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
            }
        } else {
            Object[] arguments = {a.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.VALUE_IS_NULL);
        }

    }

    @Override
    public void loadCopy(int offset, boolean global)
            throws FFaplAlgebraicException {

        Object val;
        if (global) {
            val = this._globalMemory.get(offset);
        } else {
            val = this._procedureStack.get(offset + _fp);
        }
        if (val != null) {
            val = ((IJavaType) val).clone();
        }
        _expressionStack.push(val);
    }

    @Override
    public void storeValue(int offset, boolean global) throws FFaplAlgebraicException {
        Object val;
        if (global) {
            val = popStack();
            val = this.castTo(this._globalMemoryTypes.get(offset),
                    val, _globalMemory.get(offset));
            this._globalMemory.set(offset, val);
        } else {
            val = popStack();
            val = this.castTo(this._procedureStackTypes.get(offset + _fp),
                    val, _procedureStack.get(offset + _fp));
            this._procedureStack.set(offset + _fp, val);
        }
    }

    @Override
    public void storeArrayElement() throws FFaplAlgebraicException {
        Array a;
        BInteger b;
        IJavaType c, d;

        c = (IJavaType) popStack();
        b = (BInteger) popStack();
        a = (Array) popStack();
        if (!a.isNull()) {
            if (a.typeID() == IJavaType.ARRAY) {
                if (b.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0) {
                    d = (IJavaType) ((Array) a).getValue(b.intValue());
                    c = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[d.typeID()], c, d);
                    ((Array) a).setValue(b.intValue(), c);

                } else {
                    Object[] arguments = {b};
                    throw new FFaplAlgebraicException(arguments, IAlgebraicError.ARR_LENGTH_TOOHIGH);
                }
            } else {
                Object[] arguments = {"storeArrayElement"};
                throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
            }
        } else {
            Object[] arguments = {a.classInfo()};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.VALUE_IS_NULL);
        }

    }

    @Override
    public void storeFormalParamValue(int offset) throws FFaplAlgebraicException {
        Object val;
        val = popStack();
        //val = this.castTo(this._procedureStackTypes.elementAt(offset + _fp),
        //val, _procedureStack.elementAt(offset + _fp));
        this._procedureStack.set(offset + _fp, val);
    }

    @Override
    public void arrayLen() throws FFaplAlgebraicException {
        Array a;
        a = (Array) popStack();
        _expressionStack.push(BInteger.valueOf(a.length(), _thread));
    }

    @Override
    public void add() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        IJavaType[] conv;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();

        if (b.typeID() != a.typeID()) {
            conv = convertToMaxType(a, b);
            a = conv[0];
            b = conv[1];
        }


        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(add((BigInteger) a, (BigInteger) b));
            }/*else if(b instanceof Polynomial){
				this.pushStack(add((Polynomial) b, (BigInteger) a ));
			}else if(b instanceof ResidueClass){
				this.pushStack(add((ResidueClass) b, (BigInteger) a));
			}else if(b instanceof GaloisField){
				this.pushStack(add((GaloisField) b, (BigInteger) a));
			}*/
        } else if (a instanceof Polynomial) {
            if (b instanceof Polynomial) {
                this.pushStack(add((Polynomial) a, (Polynomial) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(add((Polynomial) a, (BigInteger)b ));
			}else if(b instanceof GaloisField){
				this.pushStack(add((GaloisField)b, (Polynomial) a));
			}*/
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(add((ResidueClass) a, (ResidueClass) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(add((ResidueClass) a, (BigInteger) b));
			}else if(b instanceof GaloisField){
				this.pushStack(add((GaloisField) b, (ResidueClass) a));
			}else if(b instanceof PolynomialRC){
				this.pushStack(add((PolynomialRC) b, (ResidueClass) a));
			}*/
        } else if (a instanceof GaloisField) {
            if (b instanceof GaloisField) {
                this.pushStack(add((GaloisField) a, (GaloisField) b));
            }/*else if(b instanceof ResidueClass){
				this.pushStack(add((GaloisField) a, (ResidueClass) b));
			}else if(b instanceof BigInteger){
				this.pushStack(add((GaloisField) a, (BigInteger) b));
			}else if(b instanceof Polynomial){
				this.pushStack(add((GaloisField) a, (Polynomial) b));
			}
		}else if(a instanceof PolynomialRC){
			if(b instanceof ResidueClass){
				this.pushStack(add((PolynomialRC) a, (ResidueClass) b));
			}*/
        } else if (a instanceof JString) {
            if (b instanceof JString) {
                this.pushStack(add((JString) a, (JString) b));
            }
        } else if (a instanceof EllipticCurve) {
            if (b instanceof EllipticCurve) {
                this.pushStack(add((EllipticCurve) a, (EllipticCurve) b));
            }
        } else {
            Object[] arguments = {"add"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
        }
    }

    @Override
    public void sub() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        IJavaType[] conv;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();

        if (b.typeID() != a.typeID()) {
            conv = convertToMaxType(a, b);
            a = conv[0];
            b = conv[1];
        }

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(sub((BigInteger) a, (BigInteger) b));
            }/*else if(b instanceof Polynomial){
				this.pushStack(add(((Polynomial) b).negate(), (BigInteger)a));
			}else if(b instanceof ResidueClass){
				this.pushStack(add(((ResidueClass) b).negate(), (BigInteger) a));
			}else if(b instanceof GaloisField){
				this.pushStack(add(((GaloisField) b).negate(), (BigInteger) a));
			}*/
        } else if (a instanceof Polynomial) {
            if (b instanceof Polynomial) {
                this.pushStack(sub((Polynomial) a, (Polynomial) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(sub((Polynomial) a, (BigInteger) b ));
			}else if(b instanceof GaloisField){
				this.pushStack(add(((GaloisField) b).negate(), (Polynomial) a));
			}*/
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(sub((ResidueClass) a, (ResidueClass) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(sub((ResidueClass) a, (BigInteger) b));
			}else if(b instanceof GaloisField){
				this.pushStack(add(((GaloisField) b).negate(), (ResidueClass) a));
			}else if(b instanceof PolynomialRC){
				this.pushStack(add((PolynomialRC)((PolynomialRC) b).negate(), (ResidueClass) a));
			}*/
        } else if (a instanceof GaloisField) {
            if (b instanceof GaloisField) {
                this.pushStack(sub((GaloisField) a, (GaloisField) b));
            }/*else if(b instanceof ResidueClass){
				this.pushStack(sub((GaloisField) a, (ResidueClass) b));
			}else if(b instanceof BigInteger){
				this.pushStack(sub((GaloisField) a, (BigInteger) b));
			}else if(b instanceof Polynomial){
				this.pushStack(sub((GaloisField) a, (Polynomial) b));
			}
		}else if(a instanceof PolynomialRC){
			if(b instanceof ResidueClass){
				this.pushStack(sub((PolynomialRC) a, (ResidueClass) b));
			}*/
        } else if (a instanceof EllipticCurve) {
            if (b instanceof EllipticCurve) {
                this.pushStack(sub((EllipticCurve) a, (EllipticCurve) b));
            }
        }

    }

    @Override
    public void mul() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        IJavaType[] conv;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();

        if (b.typeID() != a.typeID()) {
            conv = convertToMaxType(a, b);
            a = conv[0];
            b = conv[1];
        }

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(mul((BigInteger) a, (BigInteger) b));
            } else if (b instanceof EllipticCurve) {
                this.pushStack(mul((EllipticCurve) b, (BigInteger) a));
            }/*else if(b instanceof Polynomial){
				this.pushStack(mul((BigInteger)a, (Polynomial) b));
			}else if(b instanceof ResidueClass){
				this.pushStack(mul((ResidueClass) b, (BigInteger) a));
			}else if(b instanceof GaloisField){
				this.pushStack(mul((GaloisField) b, (BigInteger) a));
			}*/
        } else if (a instanceof Polynomial) {
            if (b instanceof Polynomial) {
                this.pushStack(mul((Polynomial) a, (Polynomial) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(mul((BigInteger)b, (Polynomial) a));
			}else if(b instanceof GaloisField){
				this.pushStack(mul((GaloisField)b, (Polynomial) a));
			}*/
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(mul((ResidueClass) a, (ResidueClass) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(mul((ResidueClass) a, (BigInteger) b));
			}else if(b instanceof GaloisField){
				this.pushStack(mul((GaloisField) b, (ResidueClass) a));
			}else if(b instanceof PolynomialRC){
				this.pushStack(mul((PolynomialRC) b, (ResidueClass) a));
			}*/
        } else if (a instanceof GaloisField) {
            if (b instanceof GaloisField) {
                this.pushStack(mul((GaloisField) a, (GaloisField) b));
            }/*else if(b instanceof ResidueClass){
				this.pushStack(mul((GaloisField) a, (ResidueClass) b));
			}else if(b instanceof BigInteger){
				this.pushStack(mul((GaloisField) a, (BigInteger) b));
			}else if(b instanceof Polynomial){
				this.pushStack(mul((GaloisField) a, (Polynomial) b));
			}
		}else if(a instanceof PolynomialRC){
			if(b instanceof ResidueClass){
				this.pushStack(mul((PolynomialRC) a, (ResidueClass) b));
			}*/
        } else if (a instanceof EllipticCurve) {
            if (b instanceof BigInteger) {
                this.pushStack(mul((EllipticCurve) a, (BigInteger) b));
            }
        }
    }

    @Override
    public void div() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        IJavaType[] conv;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();

        if (b.typeID() != a.typeID()) {
            conv = convertToMaxType(a, b);
            a = conv[0];
            b = conv[1];
        }
        //TODO error bei division durch Polynomial

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(div((BigInteger) a, (BigInteger) b));
            }/*else if(b instanceof Polynomial){
				this.pushStack(div((BigInteger)a, (Polynomial) b));
			}else if(b instanceof ResidueClass){
				this.pushStack(div((BigInteger) a, (ResidueClass) b ));
			}else if(b instanceof GaloisField){
				this.pushStack(div((BigInteger) a, (GaloisField) b));
			}*/
        } else if (a instanceof Polynomial) {
            if (b instanceof Polynomial) {
                this.pushStack(div((Polynomial) a, (Polynomial) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(div((Polynomial) a, (BigInteger)b ));
			}else if(b instanceof GaloisField){
				this.pushStack(div((Polynomial) a, (GaloisField)b ));
			}*/
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(div((ResidueClass) a, (ResidueClass) b));
            }/*else if(b instanceof BigInteger){
				this.pushStack(div((ResidueClass) a, (BigInteger) b ));
			}else if(b instanceof GaloisField){
				this.pushStack(div((ResidueClass) a, (GaloisField) b ));
			}else if(b instanceof PolynomialRC){
				this.pushStack(div((ResidueClass) a, (PolynomialRC) b ));
			}*/
        } else if (a instanceof GaloisField) {
            if (b instanceof GaloisField) {
                this.pushStack(div((GaloisField) a, (GaloisField) b));
            }/*else if(b instanceof ResidueClass){
				this.pushStack(div((GaloisField) a, (ResidueClass) b));
			}else if(b instanceof BigInteger){
				this.pushStack(div((GaloisField) a, (BigInteger) b));
			}else if(b instanceof Polynomial){
				this.pushStack(div((GaloisField) a, (Polynomial) b));
			}
		}else if(a instanceof PolynomialRC){
			if(b instanceof ResidueClass){
				this.pushStack(div((PolynomialRC) a, (ResidueClass) b));
			}*/
        }
    }

    @Override
    public void mod() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        IJavaType[] conv;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();

        if (b.typeID() != a.typeID()) {
            conv = convertToMaxType(a, b);
            a = conv[0];
            b = conv[1];
        }
        //TODO error bei division durch Polynomial

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(mod((BigInteger) a, (BigInteger) b));
            }
        } else if (a instanceof Polynomial) {
            if (b instanceof Polynomial) {
                this.pushStack(mod((Polynomial) a, (Polynomial) b));
            }
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(mod((ResidueClass) a, (ResidueClass) b));
            }
        } else if (a instanceof GaloisField) {
            if (b instanceof GaloisField) {
                this.pushStack(mod((GaloisField) a, (GaloisField) b));
            }
        }
    }

    @Override
    public void pow() throws FFaplAlgebraicException {
        Object a;
        Object b;
        b = popStack();
        a = popStack();
        //TODO error bei division durch Polynomial

        if (b instanceof RNG_Placebo) {
            b = ((RNG_Placebo) b).next();
        } else if (b instanceof TRNG_Placebo) {
            b = ((TRNG_Placebo) b).next();
        } else if (b instanceof ResidueClass) {
            b = ((ResidueClass) b).value();
        }

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(pow((BigInteger) a, (BigInteger) b));
            }
        } else if (a instanceof Polynomial) {
            if (b instanceof BigInteger) {
                this.pushStack(pow((Polynomial) a, (BigInteger) b));
            }
        } else if (a instanceof ResidueClass) {
            if (b instanceof BigInteger) {
                this.pushStack(pow((ResidueClass) a, (BigInteger) b));
            }
        } else if (a instanceof GaloisField) {
            if (b instanceof BigInteger) {
                this.pushStack(pow((GaloisField) a, (BigInteger) b));
            }
        }
    }

    @Override
    public void neg() throws FFaplAlgebraicException {
        Object a;
        a = popStack();
        if (a instanceof BigInteger) {
            this.pushStack(((BigInteger) a).negate());
        } else if (a instanceof Polynomial) {
            this.pushStack(((Polynomial) a).negate());
        } else if (a instanceof ResidueClass) {
            this.pushStack(((ResidueClass) a).negate());
        } else if (a instanceof GaloisField) {
            this.pushStack(((GaloisField) a).negate());
        } else if (a instanceof EllipticCurve) {
            this.pushStack(((EllipticCurve) a).negate());
        }
    }

    @Override
    public void and() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();

        if ((a instanceof JBoolean) && (b instanceof JBoolean)) {
            _expressionStack.push(((JBoolean) a).and(((JBoolean) b)));
        } else if ((a instanceof BigInteger) && (b instanceof BigInteger)) {
            //for integers
            _expressionStack.push(new BInteger(((BInteger) a).and(((BInteger) b)), null));
        }

    }

    @Override
    public void or() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();


        if ((a instanceof JBoolean) && (b instanceof JBoolean)) {
            _expressionStack.push(((JBoolean) a).or(((JBoolean) b)));
        } else if ((a instanceof BigInteger) && (b instanceof BigInteger)) {
            //for integers
            _expressionStack.push(new BInteger(((BInteger) a).or(((BInteger) b)), null));
        }
    }

    @Override
    public void xor() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();


        if ((a instanceof JBoolean) && (b instanceof JBoolean)) {
            _expressionStack.push(((JBoolean) a).and(((JBoolean) b).not()).or(((JBoolean) a).not().and(((JBoolean) b))));
        } else if ((a instanceof BigInteger) && (b instanceof BigInteger)) {
            //for integers
            _expressionStack.push(new BInteger(((BInteger) a).xor(((BInteger) b)), null));
        }
    }

    @Override
    public void isGreater() throws FFaplAlgebraicException {
        Object a;
        Object b;
        b = popStack();
        a = popStack();

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((BigInteger) a).compareTo((BigInteger) b) > 0)
                );
            } else if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) b).compareTo((BigInteger) a) <= 0)
                );
            }
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((ResidueClass) b) > 0)
                );
            } else if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((BigInteger) b) > 0)
                );
            }
        }
    }

    @Override
    public void isGreaterEqual() throws FFaplAlgebraicException {
        Object a;
        Object b;
        b = popStack();
        a = popStack();

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((BigInteger) a).compareTo((BigInteger) b) >= 0)
                );
            } else if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) b).compareTo((BigInteger) a) < 0)
                );
            }
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((ResidueClass) b) >= 0)
                );
            } else if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((BigInteger) b) >= 0)
                );
            }
        }

    }

    @Override
    public void isEqual() throws FFaplAlgebraicException {
        IJavaType a;
        IJavaType b;
        IJavaType[] max;
        b = (IJavaType) popStack();
        a = (IJavaType) popStack();
        max = convertToMaxType(a, b);

        if (max[0] instanceof BigInteger) {
            if (max[1] instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((BigInteger) max[0]).compareTo((BigInteger) max[1]) == 0)
                );
                return;
            }
        } else if (max[0] instanceof Polynomial) {
            if (max[1] instanceof Polynomial) {
                this.pushStack(
                        new JBoolean(((Polynomial) max[0]).equals((Polynomial) max[1]))
                );
                return;
            }
        } else if (max[0] instanceof ResidueClass) {
            if (max[1] instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) max[0]).compareTo((ResidueClass) max[1]) == 0)
                );
                return;
            }
        } else if (max[0] instanceof GaloisField) {
            if (max[1] instanceof GaloisField) {
                this.pushStack(
                        new JBoolean(((GaloisField) max[0]).equals((GaloisField) max[1]))
                );
                return;
            }
        } else if (max[0] instanceof EllipticCurve) {
            if (max[1] instanceof EllipticCurve) {
                this.pushStack(
                        new JBoolean(((EllipticCurve) max[0]).equals((EllipticCurve) max[1]))
                );
                return;
            }
        } else if (max[0] instanceof JBoolean) {
            this.pushStack(
                    new JBoolean(((JBoolean) max[0]).getValue() == ((JBoolean) max[1]).getValue())
            );
            return;
        }

        Object[] arguments = {"equals"};
        throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);



		/*if(a instanceof BigInteger){
			if(b instanceof BigInteger){
				this.pushStack(
						new JBoolean(((BigInteger)a).compareTo((BigInteger) b) == 0)
				);
			}else if(b instanceof Polynomial){
				this.pushStack(
						new JBoolean(((Polynomial) b).equals((BigInteger) a))
				);
			}else if(b instanceof ResidueClass){
				this.pushStack(
						new JBoolean(((ResidueClass)b ).compareTo((BigInteger) a) == 0)
				);
			}else if(b instanceof GaloisField){
				this.pushStack(
						new JBoolean(((GaloisField)b ).equals((BigInteger) a))
				);
			}
		}else if(a instanceof Polynomial){
			if( b instanceof Polynomial){
				this.pushStack(
						new JBoolean(((Polynomial)a).equals((Polynomial) b))
				);
			}else if(b instanceof BigInteger){
				this.pushStack(
						new JBoolean(((Polynomial)a).equals((BigInteger) b))
				);
			}else if(b instanceof GaloisField){
				this.pushStack(
						new JBoolean(((GaloisField)b).equals((Polynomial) a))
				);
			}else if(b instanceof ResidueClass){
				this.pushStack(
						new JBoolean(((Polynomial)a).equals((ResidueClass) b))
				);
			}
		}else if(a instanceof ResidueClass){
			if(b instanceof ResidueClass){
				this.pushStack(
						new JBoolean(((ResidueClass)a).compareTo((ResidueClass) b) == 0)
				);
			}else if(b instanceof BigInteger){
				this.pushStack(
						new JBoolean(((ResidueClass)a).compareTo((BigInteger) b) == 0)
				);
			}else if(b instanceof GaloisField){
				this.pushStack(
						new JBoolean(((GaloisField)b).equals((ResidueClass) a))
				);
			}else if(b instanceof Polynomial){
				this.pushStack(
						new JBoolean(((Polynomial)b).equals((ResidueClass) a))
				);
			}
		}else if(a instanceof GaloisField){
			if(b instanceof GaloisField){
				this.pushStack(
						new JBoolean(((GaloisField)a).equals((GaloisField) b))
				);
			}else if(b instanceof ResidueClass){
				this.pushStack(
						new JBoolean(((GaloisField)a).equals((ResidueClass) b))
				);
			}else if(b instanceof BigInteger){
				this.pushStack(
						new JBoolean(((GaloisField)a).equals((BigInteger) b))
				);
			}else if(b instanceof Polynomial){
				this.pushStack(
						new JBoolean(((GaloisField)a).equals((Polynomial) b))
				);
			}
		}else{
			Object[] arguments = {"equals"};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		}*/
    }

    @Override
    public void isLess() throws FFaplAlgebraicException {
        Object a;
        Object b;
        b = popStack();
        a = popStack();

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((BigInteger) a).compareTo((BigInteger) b) < 0)
                );
            } else if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) b).compareTo((BigInteger) a) >= 0)
                );
            }
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((ResidueClass) b) < 0)
                );
            } else if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((BigInteger) b) < 0)
                );
            }
        }

    }

    @Override
    public void isLessEqual() throws FFaplAlgebraicException {
        Object a;
        Object b;
        b = popStack();
        a = popStack();

        if (a instanceof BigInteger) {
            if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((BigInteger) a).compareTo((BigInteger) b) <= 0)
                );
            } else if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) b).compareTo((BigInteger) a) > 0)
                );
            }
        } else if (a instanceof ResidueClass) {
            if (b instanceof ResidueClass) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((ResidueClass) b) <= 0)
                );
            } else if (b instanceof BigInteger) {
                this.pushStack(
                        new JBoolean(((ResidueClass) a).compareTo((BigInteger) b) <= 0)
                );
            }
        }
    }

    @Override
    public void not() throws FFaplAlgebraicException {
        JBoolean a = (JBoolean) popStack();
        _expressionStack.push(a.not());
    }

    @Override
    public void funcReturn() {

        _rt = (IJavaType) popStack();
    }

    @Override
    public void loadReturn() {
        _expressionStack.push(_rt);
    }


    /**
     * Multiply Integer and Polynomial
     * @param a
     * @param b
     * @return private Polynomial mul(BigInteger a, Polynomial b){
    Polynomial c = b.clone();
    c.multiply(a, BigInteger.ZERO);
    return c;
    }
     */
    /**
     * Multiply
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private PolynomialRC mul(PolynomialRC a, ResidueClass b) throws FFaplAlgebraicException {
    PolynomialRC c = (PolynomialRC) a.clone();
    c.multiply(b);
    return c;
    }
     */

    @Override
    public void enterProcFunc() throws FFaplAlgebraicException {
        this._procedureStackTypes.add(-1);
        this._procedureStack.add(_fp);
        this._procedureStackSymbols.add(null);
        _sp = _sp + 1;
        _fp = _sp;
    }

    /**
     * Multiply GaloisField and Polynomial
     * @param a
     * @param b
     * @return private GaloisField mul(GaloisField a, Polynomial b) throws FFaplAlgebraicException{
    GaloisField c = a.clone();
    c.multiply(b);
    return c;
    }
     */

    @Override
    public void exitProcFunc() {
        int oldfp;
        oldfp = (Integer) this._procedureStack.get(_fp - 1);
        _sp = _fp - 1;
        _fp = oldfp;
        freeProcedureStack();
    }

    @Override
    public boolean isStackOffsetAllocated(int offset) {
        return ((_fp + offset) < _sp);
    }

    /**
     * Multiply
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private GaloisField mul(GaloisField a, ResidueClass b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.multiply(b);
    return c;
    }
     */
    /**
     * Multiply
     * @param b
     * @param a
     * @return
     * @throws FFaplAlgebraicException

    private GaloisField mul(GaloisField b, BigInteger a) throws FFaplAlgebraicException {
    GaloisField c = b.clone();
    c.multiply(a);
    return c;
    }
     */

    /**
     * Multiply
     * @param a
     * @param b
     * @return private ResidueClass mul(ResidueClass a, BigInteger b) {
    ResidueClass c = a.clone();
    c.multiply(b);
    return c;
    }
     */

    /**
     * Power Operation for GaloisField
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object pow(GaloisField a, BigInteger b) throws FFaplAlgebraicException {
        GaloisField c = a.clone();
        c.pow(b);
        return c;
    }

    /**
     * Power Operation for ResidueClass
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object pow(ResidueClass a, BigInteger b) throws FFaplAlgebraicException {
        ResidueClass c = a.clone();
        c.pow(b);
        return c;
    }


    /**
     * Add Integer and Polynomial
     * @param a
     * @param b
     * @return private Polynomial add(Polynomial a, BigInteger b ){
    Polynomial c = a.clone();
    c.add(b, BigInteger.ZERO);
    return c;
    }
     */

    /**
     * Power Operation for Polynomial
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object pow(Polynomial a, BigInteger b) throws FFaplAlgebraicException {
        Polynomial c = a.clone();
        c.pow(b);
        return c;
    }

    /**
     * Power Operation for Integer
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object pow(BigInteger a, BigInteger b) throws FFaplAlgebraicException {
        BInteger c = new BInteger(a, _thread);
        return c.pow(b);
    }

    /**
     * multiply two Integer
     *
     * @param a
     * @param b
     * @return
     */
    private BigInteger mul(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    /**
     * Add
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object add(GaloisField a, BigInteger b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.add(b);
    return c;
    }
     */
    /**
     * Add
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object add(PolynomialRC a, ResidueClass b) throws FFaplAlgebraicException {
    PolynomialRC c = (PolynomialRC) a.clone();
    c.add(b);
    return c;
    }
     */
    /**
     * Add
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object add(GaloisField a, ResidueClass b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.add(b);
    return c;
    }
     */

    /**
     * Multiply Polynomial and Polynomial
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Polynomial mul(Polynomial a, Polynomial b) throws FFaplAlgebraicException {
        Polynomial c;
        if (a instanceof PolynomialRC) {
            c = a.clone();
            c.multiply(b);
            return c;
        } else {
            c = b.clone();
            c.multiply(a);
            return c;
        }
    }

    /**
     * Multiply GaloisField and GaloisField
     *
     * @param a
     * @param b
     * @return
     */
    private GaloisField mul(GaloisField a, GaloisField b) throws FFaplAlgebraicException {
        GaloisField c = a.clone();
        c.multiply(b);
        return c;
    }

    /**
     * Add
     * @param a
     * @param b
     * @return private Object add(ResidueClass a, BigInteger b) {
    ResidueClass c = a.clone();
    c.add(b);
    return c;
    }
     */
    /**
     * Add
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object add(GaloisField a, Polynomial b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.add(b);
    return c;
    }
     */

    /**
     * Multiply GaloisField and Integer
     *
     * @param a
     * @param b
     * @return
     */

    private EllipticCurve mul(EllipticCurve a, BigInteger b) throws FFaplAlgebraicException {
        EllipticCurve c = a.clone();
        c.multiply(b);
        return c;
    }

    /**
     * Multiply
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private ResidueClass mul(ResidueClass a, ResidueClass b) throws FFaplAlgebraicException {
        ResidueClass c = a.clone();
        c.multiply(b);
        return c;
    }

    /**
     * Subtract
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object sub(PolynomialRC a, ResidueClass b) throws FFaplAlgebraicException {
    PolynomialRC c = (PolynomialRC) a.clone();
    c.subtract(b);
    return c;
    }
     */
    /**
     * Subtract
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object sub(GaloisField a, Polynomial b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.subtract(b);
    return c;
    }
     */

    /**
     * Add two Integer
     *
     * @param a
     * @param b
     * @return
     */
    private BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    /**
     * Subtract
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object sub(GaloisField a, ResidueClass b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.subtract(b);
    return c;
    }
     */
    /**
     * Subtract
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object sub(GaloisField a, BigInteger b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.subtract(b);
    return c;
    }
     */

    /**
     * Add Polynomial and Polynomial
     *
     * @param a
     * @param b
     * @return
     */
    private Polynomial add(Polynomial a, Polynomial b) {
        Polynomial c = a.clone();
        c.add(b);
        return c;
    }

    /**
     * Subtract
     * @param a
     * @param b
     * @return private Object sub(ResidueClass a, BigInteger b) {
    ResidueClass c = a.clone();
    c.subtract(b);
    return c;
    }
     */

    /**
     * Add
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object add(GaloisField a, GaloisField b) throws FFaplAlgebraicException {
        GaloisField c = a.clone();
        c.add(b);
        return c;
    }

    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(PolynomialRC a, ResidueClass b) throws FFaplAlgebraicException {
    PolynomialRC c = (PolynomialRC) a.clone();
    c.divide(b);
    return c;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(GaloisField a, Polynomial b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.divide(b);
    return c;
    }
     */

    /**
     * Add
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object add(EllipticCurve a, EllipticCurve b) throws FFaplAlgebraicException {
        EllipticCurve c = a.clone();
        c.add(b);
        return c;
    }

    /**
     * divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(GaloisField a, ResidueClass b) throws FFaplAlgebraicException {
    GaloisField c = a.clone();
    c.divide(b);
    return c;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(GaloisField a, BigInteger b) throws FFaplAlgebraicException {
    if(b.equals(BigInteger.ZERO)){
    throw new FFaplAlgebraicException("Division by Zero not possible! --> " + a +" / "+
    b , IAlgebraicError.DIVZERO);
    }
    GaloisField c = a.clone();
    c.divide(b);
    return c;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(ResidueClass a, PolynomialRC b) throws FFaplAlgebraicException {
    PolynomialRC ply = new PolynomialRC(a.modulus());
    ply.setPolynomial(a.value(), BigInteger.ZERO);
    ply.divide(b);
    return ply;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(ResidueClass a, GaloisField b) throws FFaplAlgebraicException {
    GaloisField gf = GaloisField.inverse(b);
    gf.multiply(a);
    return gf;
    }
     */

    /**
     * Add
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object add(ResidueClass a, ResidueClass b) throws FFaplAlgebraicException {
        ResidueClass c = a.clone();
        c.add(b);
        return c;
    }

    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(ResidueClass a, BigInteger b) throws FFaplAlgebraicException {
    if(b.equals(BigInteger.ZERO)){
    throw new FFaplAlgebraicException("Division by Zero not possible! --> " + a +" / "+
    b , IAlgebraicError.DIVZERO);
    }
    ResidueClass c = a.clone();
    c.divide(b);
    return c;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(Polynomial a, GaloisField b) throws FFaplAlgebraicException {
    GaloisField gf = GaloisField.inverse(b);
    gf.multiply(a);
    return gf;
    }
     */

    /**
     * Add
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object add(JString a, JString b) throws FFaplAlgebraicException {
        return new JString(a.toString() + b.toString());
    }

    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(Polynomial a, BigInteger b) throws FFaplAlgebraicException {
    if(b.equals(BigInteger.ZERO)){
    throw new FFaplAlgebraicException("Division by Zero not possible! --> " + a +" / "+
    b , IAlgebraicError.DIVZERO);
    }
    Polynomial c = a.clone();
    c.divide(b);
    return c;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(BigInteger a, GaloisField b) throws FFaplAlgebraicException {
    GaloisField gf = GaloisField.inverse(b);
    gf.multiply(a);
    return gf;
    }
     */
    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(BigInteger a, ResidueClass b) throws FFaplAlgebraicException {
    ResidueClass rc = b.inverse();
    rc.multiply(a);
    return rc;
    }
     */

    /**
     * Subtract two Integer
     *
     * @param a
     * @param b
     * @return
     */
    private BigInteger sub(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    /**
     * Divide
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException

    private Object div(BigInteger a, Polynomial b) throws FFaplAlgebraicException {
    PolynomialRC ply;
    if(b instanceof PolynomialRC){
    ply = (PolynomialRC) b.clone();
    ply.setPolynomial(a, BigInteger.ZERO);
    ply.divide(b);
    return ply;
    }else{
    throw new FFaplAlgebraicException("Integer '" + a + "' divided by Polynomial '" +
    b + "' is not reasonable", IAlgebraicError.OPERATION_NOT_REASONABLE);
    }
    }
     */

    /**
     * Subtract Polynomial and Polynomial
     *
     * @param a
     * @param b
     * @return
     */
    private Polynomial sub(Polynomial a, Polynomial b) {
        Polynomial c = a.clone();
        c.subtract(b);
        return c;
    }

    /**
     * Subtract
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object sub(GaloisField a, GaloisField b) throws FFaplAlgebraicException {
        GaloisField c = a.clone();
        c.subtract(b);
        return c;
    }

    /**
     * Subtract
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object sub(ResidueClass a, ResidueClass b) throws FFaplAlgebraicException {
        ResidueClass c = a.clone();
        c.subtract(b);
        return c;
    }

    /**
     * Subtract
     *
     * @param a
     * @param b
     * @return private Object sub(Polynomial a, BigInteger b) {
     * Polynomial c = a.clone();
     * c.subtract(b, BigInteger.ZERO);
     * return c;
     * }
     */

    private Object sub(EllipticCurve a, EllipticCurve b) throws FFaplAlgebraicException {
        EllipticCurve c = a.clone();
        c.sub(b);
        return c;
    }

    /**
     * Divide
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object div(GaloisField a, GaloisField b) throws FFaplAlgebraicException {
        GaloisField c = a.clone();
        c.divide(b);
        return c;
    }

    /**
     * Divide
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object div(ResidueClass a, ResidueClass b) throws FFaplAlgebraicException {
        ResidueClass c = a.clone();
        c.divide(b);
        return c;
    }

    /**
     * Divide
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object div(Polynomial a, Polynomial b) throws FFaplAlgebraicException {
        Polynomial c = a.clone();
        c.divide(b);
        return c;
    }

    /**
     * Divide
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object div(BigInteger a, BigInteger b) throws FFaplAlgebraicException {
        if (b.equals(BigInteger.ZERO)) {
            Object[] arguments = {a +
                    FFaplInterpreter.tokenImage[FFaplInterpreter.DIVIDE].replace("\"", "") + b};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.DIVZERO);
        }
        return a.divide(b);
    }

    /**
     * Modulo operatino for Galois Field
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object mod(GaloisField a, GaloisField b) throws FFaplAlgebraicException {
        GaloisField c = a.clone();
        c.mod(b);
        return c;
    }

    /**
     * Modulo operation for residue class
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object mod(ResidueClass a, ResidueClass b) throws FFaplAlgebraicException {
        ResidueClass c = a.clone();
        c.mod(b);
        return c;
    }

    /**
     * Modulo operation for Polynomial and Polynomial ring
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object mod(Polynomial a, Polynomial b) throws FFaplAlgebraicException {
        Polynomial c = a.clone();
        c.mod(b);
        return c;
    }

    /**
     * Modulo operation for BigInteger
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object mod(BigInteger a, BigInteger b) throws FFaplAlgebraicException {
        if ((b).compareTo(BigInteger.ZERO) <= 0) {
            Object[] arguments = {a + " " +
                    FFaplInterpreter.tokenImage[FFaplInterpreter.MODULO].replace("\"", "") + " " + b, b};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
        }
        return a.mod(b);
    }

    /**
     * Returns information about interpreter
     */
    public String toString() {
        return "Expression Stack size: " + this._expressionStack.size() + "\n" +
                "Procedure Stack size: " + this._procedureStack.size() + "\n" +
                "Global Memory size: " + this._globalMemory.size() + "\n" +
                "FP: " + _fp + "\n" +
                "Expression Stack: " + this._expressionStack + "\n" +
                "Procedure Stack: " + this._procedureStack + "\n" +
                "Procedure Stack Type: " + this._procedureStackTypes + "\n" +
                "Procedure Stack Symbol: " + this._procedureStackSymbols + "\n" +
                "Global Memory: " + this._globalMemory + "\n";
    }

    @Override
    public void pushStack(Object exp) {
        _expressionStack.push(exp);
    }

    @Override
    public Object popStack() {
         return _expressionStack.pop();
    }

    @Override
    public Object peekStack() {
        return _expressionStack.peek();
    }

    @Override
    public void castElementOnStackTo(int typeID) throws FFaplAlgebraicException {
        Object a;
        Object b;

        b = popStack();




        a = popStack();
        _expressionStack.push(castTo(typeID, a, b));
//		}

        //}

    }

    /**
     * Cast object to the specified type if possible
     *
     * @param typeID
     * @param obj
     * @return
     * @throws FFaplAlgebraicException
     */
    private Object castTo(int typeID, Object from, Object to) throws FFaplAlgebraicException {
        IJavaType fromobj;
        IJavaType toobj;
        IJavaType result;
        String txt;
         fromobj = (IJavaType) from;
         toobj = (IJavaType) to;

        switch (typeID) {
            case FFaplTypeCrossTable.FFAPLARRAY:
                if (fromobj.typeID() == IJavaType.ARRAY) { //Integer
                    if (((Array) toobj).equalType(((Array) fromobj))) {
                        return fromobj;//call by reference
                    }
                }
                break;
            case FFaplTypeCrossTable.FFAPLBOOLEAN:
                if (fromobj.typeID() == IJavaType.BOOLEAN) { //Integer
                    return fromobj.clone();
                }
                break;
            case FFaplTypeCrossTable.FFAPLEC:

                if (!(fromobj instanceof EllipticCurve)) {
                    return fromobj.clone();
                }

                if (((EllipticCurve) fromobj).getRandom()) {
                    result = ((EllipticCurve) toobj).clone();
                    ((EllipticCurve) result).setRandomPoint(((EllipticCurve) fromobj).getRandomSubfield());
                    return result;
                }


                if (((EllipticCurve) toobj).parametersSet() && ((EllipticCurve) fromobj).parametersSet() && !((EllipticCurve) toobj).equalEC(((EllipticCurve) fromobj))) {
                    throw new FFaplAlgebraicException(null, IAlgebraicError.WRONG_EC_FIELD);
                }
                if (((EllipticCurve) toobj).parametersSet()) {
                    result = ((EllipticCurve) toobj).clone();
                    ((EllipticCurve) result).setPAI(((EllipticCurve) fromobj).getPAI());
                } else {
                    result = ((EllipticCurve) fromobj).clone();
                    ((EllipticCurve) result).setPAI(((EllipticCurve) toobj).getPAI());
                }


                if (!((EllipticCurve) fromobj).isGf() && !((EllipticCurve) toobj).isGf()) {
                    ((EllipticCurve) result).setValue_rc(((EllipticCurve) fromobj).getX_rc(), ((EllipticCurve) fromobj).getY_rc());
                } else if (((EllipticCurve) fromobj).isGf() && ((EllipticCurve) toobj).isGf()) {
                    ((EllipticCurve) result).setValue_gf(((EllipticCurve) fromobj).getX_gf(), ((EllipticCurve) fromobj).getY_gf());
                } else if (((EllipticCurve) result).getPAI()) {
                    //
                } else {
                    throw new FFaplAlgebraicException(null, IAlgebraicError.WRONG_EC_FIELD);
                }


                return result;

            case FFaplTypeCrossTable.FFAPLGF:

                if (fromobj.typeID() == IJavaType.PRIME || fromobj.typeID() == IJavaType.INTEGER) {
                    result = ((GaloisField) toobj).clone();
                    ((GaloisField) result).setValue(
                            new Polynomial((BigInteger) fromobj, BigInteger.ZERO, _thread));
                    return result;
                } else if (fromobj.typeID() == IJavaType.POLYNOMIALRC) {
                    if (((GaloisField) toobj).characteristic().equals(//polynomialring
                            ((PolynomialRC) fromobj).characteristic())) {
                        result = ((GaloisField) toobj).clone();
                        ((GaloisField) result).setValue(((PolynomialRC) fromobj));
                        return result;
                    }
                } else if (fromobj.typeID() == IJavaType.POLYNOMIAL) {//Polynomial
                    result = ((GaloisField) toobj).clone();
                    ((GaloisField) result).setValue(((Polynomial) fromobj));
                    return result;
                } else if (fromobj.typeID() == IJavaType.RESIDUECLASS) { //residueclass
                    if (((GaloisField) toobj).characteristic().equals(
                            ((ResidueClass) fromobj).modulus())) {
                        result = ((GaloisField) toobj).clone();
                        ((GaloisField) result).setValue(
                                new Polynomial(((ResidueClass) fromobj).value(), BigInteger.ZERO, _thread));
                        return result;
                    }
                } else if (fromobj.typeID() == IJavaType.GALOISFIELD) {//GaloisField
                    if (((GaloisField) toobj).equalGF((GaloisField) (fromobj))) {
                        return fromobj.clone();
                    }
                } else if (fromobj instanceof IRandomGenerator) {
                    result = ((GaloisField) toobj).clone();
                    ((GaloisField) result).setValue(
                            new Polynomial(((IRandomGenerator) fromobj).next(), BigInteger.ZERO, _thread));
                    return result;
                }
                break;
            case FFaplTypeCrossTable.FFAPLINTEGER:
                if (fromobj.typeID() == IJavaType.PRIME || fromobj.typeID() == IJavaType.INTEGER) { //Integer
                    return new BInteger((BigInteger) fromobj, _thread);
                } else if (fromobj instanceof IRandomGenerator) {
                    return ((IRandomGenerator) fromobj).next();
                }
                break;
            case FFaplTypeCrossTable.FFAPLRESIDUECLASS:
                 if (fromobj.typeID() == IJavaType.PRIME || fromobj.typeID() == IJavaType.INTEGER) { //Integer
                    result = ((ResidueClass) toobj).clone();
                    ((ResidueClass) result).setValue((BigInteger) fromobj);
                    return result;
                } else if (fromobj instanceof ResidueClass) { //residueclass
                    if (((ResidueClass) fromobj).isCompatibleTo((ResidueClass) toobj)) {
                        result = ((ResidueClass) toobj).clone();
                        ((ResidueClass) result).setValue(((ResidueClass) fromobj).value());
                        return result; //same characteristic
                    }
                } else if (fromobj instanceof IRandomGenerator) {
                    result = ((ResidueClass) toobj).clone();
                    ((ResidueClass) result).setValue(((IRandomGenerator) fromobj).next());
                    return result; //same characteristic
                }
                break;
            case FFaplTypeCrossTable.FFAPLPOLYNOMIAL:
                if (fromobj.typeID() == IJavaType.PRIME || fromobj.typeID() == IJavaType.INTEGER) {
                    result = new Polynomial(_thread);
                    ((Polynomial) result).setPolynomial((BigInteger) (fromobj), BigInteger.ZERO);
                    return result;
                } else if (fromobj.typeID() == IJavaType.POLYNOMIAL) {
                    //result = ((Polynomial)toobj).clone();
                    //((Polynomial) result).setPolynomial(((Polynomial) fromobj).polynomial());
                    return fromobj.clone();
                } else if (fromobj instanceof IRandomGenerator) {
                    result = new Polynomial(_thread);
                    ((Polynomial) result).setPolynomial(((IRandomGenerator) fromobj).next(), BigInteger.ZERO);
                    return result;
                }
                break;
            case FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE:
                if (fromobj.typeID() == IJavaType.PRIME || fromobj.typeID() == IJavaType.INTEGER) { //Integer
                    result = ((PolynomialRC) toobj).clone();
                    ((PolynomialRC) result).setPolynomial((BigInteger) fromobj, BigInteger.ZERO);
                    return result;
                } else if (fromobj.typeID() == IJavaType.RESIDUECLASS) { //residueclass
                    if (((ResidueClass) fromobj).modulus().equals(
                            ((PolynomialRC) toobj).characteristic())) {
                        result = ((PolynomialRC) toobj).clone();
                        ((PolynomialRC) result).setPolynomial(((ResidueClass) fromobj).value(), BigInteger.ZERO);
                        return result; //same characteristic
                    }
                } else if (fromobj.typeID() == IJavaType.POLYNOMIALRC) { //Polynomring
                    if (((PolynomialRC) fromobj).characteristic().equals((
                            (PolynomialRC) toobj).characteristic())) {
                        return fromobj.clone(); //same characteristic
                    }
                } else if (fromobj.typeID() == IJavaType.POLYNOMIAL) { //Polynomial
                    result = ((PolynomialRC) toobj).clone();
                    ((PolynomialRC) result).setPolynomial(((Polynomial) fromobj).polynomial());
                    return result;
                } else if (fromobj instanceof IRandomGenerator) {
                    result = ((PolynomialRC) toobj).clone();
                    ((PolynomialRC) result).setPolynomial(((IRandomGenerator) fromobj).next(), BigInteger.ZERO);
                    return result;
                }
                break;
            case FFaplTypeCrossTable.FFAPLPRIME:
                if (fromobj.typeID() == IJavaType.PRIME) {
                    return fromobj.clone();
                } else if (fromobj.typeID() == IJavaType.INTEGER) {
                    return new Prime((BigInteger) fromobj, _thread);
                } else if (fromobj instanceof IRandomGenerator) {
                    return new Prime(((IRandomGenerator) fromobj).next(), _thread);
                }
                break;
            case FFaplTypeCrossTable.FFAPLPSRANDOMG:
                if (fromobj.typeID() == IJavaType.PSRANDOMGENERATOR) {
                    return fromobj.clone();
                }
            case FFaplTypeCrossTable.FFAPLRANDOM:
                //should not be possible
                break;
            case FFaplTypeCrossTable.FFAPLRANDOMG:
                if (fromobj.typeID() == IJavaType.RANDOMGENERATOR) {
                    return fromobj.clone();
                }
            case FFaplTypeCrossTable.FFAPLRECORD:
                if (fromobj.typeID() == IJavaType.RECORD) {
                    return fromobj.clone();
                }
            case FFaplTypeCrossTable.FFAPLSTRING:
                if (fromobj.typeID() == IJavaType.GALOISFIELD) {
                    //return new JString(fromobj + " in " + fromobj.classInfo());
                    return new JString(fromobj.classInfo() + ": " + fromobj);
                } else if (fromobj.typeID() == IJavaType.RESIDUECLASS) {
                    return new JString(fromobj.classInfo() + ": " + fromobj);
                } else if (fromobj.typeID() == IJavaType.POLYNOMIALRC) {
                    return new JString(fromobj.classInfo() + ": " + fromobj);
                } else if (fromobj.typeID() == IJavaType.ELLIPTICCURVE) {
                    return new JString(fromobj.classInfo() + ": " + fromobj);
                } else if (fromobj instanceof IRandomGenerator) {
                    return new JString(((IRandomGenerator) fromobj).next().toString());
                } else {
                    return new JString(fromobj.toString());
                }
            default:
                Object[] arguments = {"implicit cast"};
                throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
        }
        if (toobj != null) {
            txt = ((IJavaType) toobj).classInfo();
        } else {
            txt = FFaplTypeCrossTable.TYPE_Name[typeID];
        }
        Object[] arguments = {((IJavaType) fromobj).classInfo(), txt};
        throw new FFaplAlgebraicException(arguments, IAlgebraicError.CAST_NOT_POSSIBLE);
    }

    /**
     * returns true if initvalue is on stack
     *
     * @param typeID
     * @return
     * @throws FFaplAlgebraicException
     */
    private boolean initvalue(int typeID) throws FFaplAlgebraicException {
        boolean result = false;

        switch (typeID) {
            case FFaplTypeCrossTable.FFAPLARRAY:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLBOOLEAN:
                this.pushStack(new JBoolean());
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLGF:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLEC:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLINTEGER:
                this.pushStack(new BInteger("0", _thread));
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLRESIDUECLASS:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLPOLYNOMIAL:
                this.pushStack(new Polynomial(_thread));
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLPRIME:
                this.pushStack(new Prime("2", _thread));
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLPSRANDOMG:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLRANDOM:
                Object[] arguments = {"value initialization"};
                throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
            case FFaplTypeCrossTable.FFAPLRANDOMG:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLRECORD:
                result = true;
                break;
            case FFaplTypeCrossTable.FFAPLSTRING:
                this.pushStack(new JString(""));
                result = true;
                break;
            default:
                Object[] arguments2 = {"value initialization"};
                throw new FFaplAlgebraicException(arguments2, IAlgebraicError.INTERNAL);
        }

        return result;
    }

    /**
     * free unused Space on stack
     */
    private void freeProcedureStack() {
        int size;
        //ISymbol symbol;
        int last;
        size = _procedureStack.size();
        for (int i = 0; i < (size - _sp); i++) {
            last = _procedureStack.size() - 1;
            _procedureStack.remove(last);
            _procedureStackTypes.remove(last);
            _procedureStackSymbols.remove(last);
            //if(symbol != null){
            //symbol.resetOffset();
            //}
        }
    }

    /**
     * creates an array
     *
     * @param arrayType
     * @param initArray
     * @return the created array of type arrayType
     * @throws FFaplAlgebraicException
     */
    private Object createArray(Type arrayType, Array initArray) throws FFaplAlgebraicException {
        Object val;
        BInteger length;
        Object[] elements;

        if (!(arrayType instanceof FFaplArray)) {
            switch (arrayType.typeID()) {
                case FFaplTypeCrossTable.FFAPLINTEGER:
                    val = new BInteger("0", _thread);
                    break;
                case FFaplTypeCrossTable.FFAPLBOOLEAN:
                    val = new JBoolean(false);
                    break;
                case FFaplTypeCrossTable.FFAPLPRIME:
                    val = new Prime("2", _thread);
                    break;
                case FFaplTypeCrossTable.FFAPLPOLYNOMIAL:
                    val = new Polynomial(_thread);
                    break;
                case FFaplTypeCrossTable.FFAPLSTRING:
                    val = new JString("");
                    break;
                case FFaplTypeCrossTable.FFAPLGF:
                    val = initArray.getjType().clone();
                    break;
                case FFaplTypeCrossTable.FFAPLRESIDUECLASS:
                    val = initArray.getjType().clone();
                    break;
                case FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE:
                    val = initArray.getjType().clone();
                    break;
                default:
                    Object[] arguments = {"createArray"};
                    throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
            }
            return val;
        } else {
            val = createArray(((FFaplArray) arrayType).subarray(1), initArray);
            length = (BInteger) popStack();

            if (length.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0) {
                elements = new Object[length.intValue()];
                for (int i = 0; i < elements.length; i++) {
                    if (val instanceof IJavaType) {
                        elements[i] = ((IJavaType) val).clone();
                    } else {
                        elements[i] = ((Object[]) val).clone();
                    }
                }
            } else {
                Object[] arguments = {length};
                throw new FFaplAlgebraicException(arguments, IAlgebraicError.ARR_LENGTH_TOOHIGH);
            }
        }
        return elements;
    }

    /**
     * convert a and b to max type of both
     *
     * @param a
     * @param b
     * @return
     * @throws FFaplAlgebraicException
     */
    private IJavaType[] convertToMaxType(IJavaType a, IJavaType b) throws FFaplAlgebraicException {
        IJavaType[] result = new IJavaType[2];
         if (a.typeID() == IJavaType.STRING) {
            result[0] = a;
            result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
        } else if (b.typeID() == IJavaType.STRING) {
            result[0] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[b.typeID()], a, b);
            result[1] = b;
        } else if (a.typeID() == IJavaType.INTEGER || a.typeID() == IJavaType.PRIME) { //integer and prime
            if (b.typeID() == IJavaType.ELLIPTICCURVE) {
                result[0] = a;
                result[1] = b;
            } else if (b.typeID() != IJavaType.PRIME) {
                result[0] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[b.typeID()], a, b);
                result[1] = b;
            } else {
                result[0] = a;
                result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
            }

        } else if (a.typeID() == IJavaType.GALOISFIELD) {//GF
            result[0] = a;
            result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
        } else if (a.typeID() == IJavaType.ELLIPTICCURVE) {//EC
            if (b.typeID() == IJavaType.INTEGER || b.typeID() == IJavaType.PRIME) {
                result[0] = a;
                result[1] = b;
            }
            if (b.typeID() == IJavaType.ELLIPTICCURVE) {
                result[0] = a;
                result[1] = b;
            }
        } else if (a.typeID() == IJavaType.POLYNOMIALRC) {//Polynom ring
            if (b.typeID() != IJavaType.GALOISFIELD) {
                result[0] = a;
                result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
            } else {
                result[0] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[b.typeID()], a, b);
                result[1] = b;
            }
        } else if (a.typeID() == IJavaType.POLYNOMIAL) {//polynomial
            if (b.typeID() != IJavaType.GALOISFIELD && b.typeID() != IJavaType.POLYNOMIALRC) {
                result[0] = a;
                result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
            } else {
                result[0] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[b.typeID()], a, b);
                result[1] = b;
            }
        } else if (a.typeID() == IJavaType.RESIDUECLASS) {//Residueclass
            if (b.typeID() != IJavaType.GALOISFIELD &&
                    b.typeID() != IJavaType.POLYNOMIALRC) {
                result[0] = a;
                result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
            } else {
                result[0] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[b.typeID()], a, b);
                result[1] = b;
            }
        } else if (a.typeID() == IJavaType.RANDOMGENERATOR ||
                a.typeID() == IJavaType.PSRANDOMGENERATOR) {
            result[0] = a;
            result[1] = (IJavaType) castTo(IJavaType.FFapl_Type_Compatibility[a.typeID()], b, a);
        } else if (a.typeID() == IJavaType.BOOLEAN && b.typeID() == IJavaType.BOOLEAN) {
            result[0] = a;
            result[1] = b;
        } else {
            Object[] arguments = {"convertToMax"};
            throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
        }
        return result;
    }


    public ISymbol getMasterNode(ISymbol node) {
        if (node.scope().toString().equals("_MainBlock")) {
            return node;
        } else {
            node = node.scope();
            return getMasterNode(node);
        }
    }


    @Override
    public String getRecordScope(int index, ISymbol masterNode, ISymbol recordN) {
        restartNode = false;
        masterNode = getMasterNode(recordN);
        for (int i = 0; i < this._procedureStackSymbols.size(); i++) {
            originalNode = this._procedureStackSymbols.get(i);
            if (getMasterNode(originalNode) == masterNode) {
                buildRecordScope(i, originalNode, recordN);
            }
        }

        String finalResult = null;
        originalNode = null;
        for (ISymbol s : nodelist) {
            if (s.getType().typeID() == 9 && s.scope().getName() != "_MainBlock") {
                finalResult= buildTreeJson();
                    } else {
                finalResult = recordJSON.getJSONObject(Print.recordName.getName()).toString();
            }
        }

        String recordJSONOutput = recordJSON.get(Print.recordName.getName()).toString();
        recordJSON.clear();
        return recordJSONOutput;
    }

    public JSONObject getJSON() {
        GsonBuilder gsonMapBuilder = new GsonBuilder();
        JSONObject crunchifyObject = new JSONObject(recordMap._record.get(Print.recordName.getName()));
        return crunchifyObject;
    }

    public void buildRecordScope(int index, ISymbol node, ISymbol recordN) {
        if (node.scope().toString().equals("_MainBlock")) {
            return;
        } else if (node.scope().getName() == Print.recordName.getName()) {
            list.add(node.scope());
            node = node.scope();
        } else {
            node = node.scope();
            list.add(node);
            buildRecordScope(index, node, recordN);
        }
        nodelist.add(node);
        String nodeName = node.getName();
        HashMap buildListInput = buildList(node.getName());
        recordMap._record.put(nodeName, buildListInput);
        JSONObject crunchifyObject = new JSONObject(buildListInput);
        recordJSON.put(nodeName, crunchifyObject);
    }

    public String buildTreeJson() {
        int index = 0;
        for (Object entryKey : recordJSON.keySet()) {
            String key = (String) entryKey;
            Object value = recordJSON.get(key);

            if (index >= 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (entryKey == list.get(i).getName()) {
                        for (ISymbol s : list) {
                            if (entryKey == s.getName()) {
                                if (!s.scope().getName().equals("_MainBlock") && s.scope().getType().typeID() == 9) {
                                    recordJSON.getJSONObject(s.scope().getName()).put(key, value);
                                }
                            }
                        }
                    }
                }
            }

            index++;
        }
//        recordMap._record.get(Print.recordName);

// 		return recordMap._record.get(Print.recordName.getName()).toString();
        String finalJSON= getJSON().toString();
        finalJSON= finalJSON.replace("\"{","{").replace("=",":").replace("}\"","}");

        return finalJSON;
        }

        public HashMap<String, String> buildList (String recordN){
            HashMap<String, String> internalHasMap = new HashMap<>();
            List<String> list = new ArrayList<>();

            for (int i = 0; i < this._procedureStackSymbols.size(); i++) {
                try {

                    String name = this._procedureStackSymbols.get(i).getName();
                    String type = this._procedureStackSymbols.get(i).getType().toString();
                    String bScopeName = this._procedureStackSymbols.get(i).scope().getName();
                    String bScopetype = this._procedureStackSymbols.get(i).scope().getType().toString();
                                        IJavaType a;
                    a = (IJavaType) this._procedureStack.get(i);
                    String msg;
                    if (bScopetype == "Record" && recordN == bScopeName) {

                        switch (a.typeID()) {
                            case IJavaType.GALOISFIELD:
                                //msg =  a + " in " + a.classInfo();
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
//							recordObj.addElement(name.toString(),a.toString());
                                internalHasMap.put(name.toString(), a.toString());
                                break;
                            case IJavaType.RESIDUECLASS:
                                //msg =  a + " in " + a.classInfo();
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
//							recordObj.addElement(name.toString(),msg);
                                internalHasMap.put(name.toString(), msg);
                                break;
                            case IJavaType.POLYNOMIAL:
                                 msg = a.classInfo() + ": " + a;
                                list.add(msg);
                                String pol = "[" + a.toString() + "]";
                                 internalHasMap.put(name.toString(), pol);
                                break;
                            case IJavaType.PSRANDOMGENERATOR:
                            case IJavaType.RANDOMGENERATOR:
                                msg = ((IRandomGenerator) a).next().toString();
                                list.add(msg);
                                internalHasMap.put(name.toString(), msg);
                                break;
                            case IJavaType.ELLIPTICCURVE:
                                msg = a.classInfo() + ": " + a;
                                list.add(a.classInfo());
                                internalHasMap.put(name.toString(), a.classInfo());
                                break;
                            default:
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
                                internalHasMap.put(name.toString(), a.toString());
                        }
                    }
                } catch (NullPointerException e) {

                } catch (FFaplAlgebraicException e) {
                    e.printStackTrace();
                }
            }
             return internalHasMap;
        }

        @Override
        public String getIndex_procedureStackSymbols (String recordN){
            List<String> list = new ArrayList<>();
            Record recordObj = new Record();
            for (int i = 0; i < this._procedureStackSymbols.size(); i++) {

                try {
                    String name = this._procedureStackSymbols.get(i).getName();
                    String type = this._procedureStackSymbols.get(i).getType().toString();
                    String bScopeName = this._procedureStackSymbols.get(i).scope().getName();
                    String bScopetype = this._procedureStackSymbols.get(i).scope().getType().toString();
                    IJavaType a;
                    a = (IJavaType) this._procedureStack.get(i);
                    String msg;
                    if (bScopetype == "Record" && recordN == bScopeName) {
                        switch (a.typeID()) {
                            case IJavaType.GALOISFIELD:
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
                                break;
                            case IJavaType.RESIDUECLASS:
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
                                break;
                            case IJavaType.POLYNOMIAL:
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
                                String pol = "[" + a.toString() + "]";
                                break;
                            case IJavaType.PSRANDOMGENERATOR:
                            case IJavaType.RANDOMGENERATOR:
                                msg = ((IRandomGenerator) a).next().toString();
                                list.add(msg);
                                break;
                            case IJavaType.ELLIPTICCURVE:
                                msg = a.classInfo() + ": " + a;
                                list.add(a.classInfo());
                                break;
                            default:
                                msg = a.classInfo() + ": " + a;
                                list.add(msg);
                        }
                    }
                } catch (NullPointerException e) {

                } catch (FFaplAlgebraicException e) {
                    e.printStackTrace();
                }
            }
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String jsonString = gson.toJson(recordObj);
            jsonString = jsonString.replace("_record", Print.recordName.getName());
            return jsonString;
        }
    }
