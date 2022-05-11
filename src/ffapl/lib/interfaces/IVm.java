package ffapl.lib.interfaces;

import ffapl.java.classes.Array;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.types.Type;

public interface IVm {
	
	/**
	 * Allocate space on the new procedure stack
	 * @param type
	 * @param symbol
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public int allocStack(ISymbol symbol) throws FFaplAlgebraicException;
	
	/**
	 * Allocate space for an array an loads the base address on the stack
	 * @param type
	 * @param initArray
	 * @throws FFaplAlgebraicException
	 */
	public void allocArray(Type type, Array initArray) throws FFaplAlgebraicException;
	
	/**
	 * Allocate space on the new procedure stack for FormalParam
	 * @param symbol
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public int allocFormalParam(ISymbol symbol)  throws FFaplAlgebraicException;
	
	
	/**
	 * Allocate space on the new procedure stack, the place will be 
	 * a copy of the specified offset
	 * @param symbol
	 * @param offset
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public int alloStack(ISymbol symbol, int offset)  throws FFaplAlgebraicException;
    
	/**
	 * Allocate space on the global memory 
	 * @param type
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public int allocGlobal(Type type) throws FFaplAlgebraicException;
	
		
	/**
	 * loads value from procedure Stack or from the global memory
	 * @param offset
	 * @param global specifies if value is from global memory or procedure stack
	 * @throws FFaplAlgebraicException
	 */
	public void loadValue(int offset, boolean global) throws FFaplAlgebraicException;
	
	/**
	 * runtime effect: b = pop(); a = pop(); push(a[b]);
	 * @throws FFaplAlgebraicException
	 */
	public void loadArrayElement() throws FFaplAlgebraicException;
	
	/**
	 * loads a copy of the value from procedure Stack or from the global memory
	 * @param offset
	 * @param global specifies if value is from global memory or procedure stack
	 * @throws FFaplAlgebraicException
	 */
	public void loadCopy(int offset, boolean global) throws FFaplAlgebraicException;
	
	/**
	 * stores value on procedure stack or in the global memory
	 * @param offset
	 * @param global
	 * @throws FFaplAlgebraicException
	 */
	public void storeValue(int offset, boolean global) throws FFaplAlgebraicException;
	
	/**
	 * stores formal parameter value on procedure stack
	 * @param offset
	 * @param global
	 * @throws FFaplAlgebraicException
	 */
	public void storeFormalParamValue(int elementAt) throws FFaplAlgebraicException;
	
	/**
	 * Emit code for add operation on expression stack
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a + b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void add() throws FFaplAlgebraicException;
	
	/**
	 * Emit code for sub operation on expression stack
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a - b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void sub() throws FFaplAlgebraicException;
	
	
	/**
	 * Emit code for mul operation on expression stack
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a * b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void mul() throws FFaplAlgebraicException;
	
	/**
	 * Emit code for div operation on expression stack
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a / b)
     * </pre>
	 * @throws FFaplAlgebraicException 
	 */
	public void div() throws FFaplAlgebraicException;
	
	/**
	 * Emit code for neg operation on expression stack
	 * <pre>
     * Runtime effect: a = pop(), push( - a)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void neg() throws FFaplAlgebraicException;
	
	/**
	 * Emit code for mod operation on expression stack
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a Mod b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void mod() throws FFaplAlgebraicException;
	
	/**
	 * Emit code for pow operation on expression stack
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a ^ b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void pow() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a && b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void and() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a || b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void or() throws FFaplAlgebraicException;

	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a ^ b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void xor() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: a = pop(), push(!a)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void not() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a > b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void isGreater() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a >= b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void isGreaterEqual() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a == b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void isEqual() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a < b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void isLess() throws FFaplAlgebraicException;
	
	/**
	 * <pre>
     * Runtime effect: b = pop(), a = pop(), push(a <= b)
     * </pre>
	 * @throws FFaplAlgebraicException
	 */
	public void isLessEqual() throws FFaplAlgebraicException;
	
	/**
	 * push on expression Stack
	 * @param exp
	 */
	public void pushStack(Object exp) ;
	
	/**
	 * pop from expression Stack
	 * @return top element of the stack
	 */
	public Object popStack();
	
	/**
	 * peeks from expression Stack
	 * @return top element of the stack
	 */
	public Object peekStack();
	
	/**
	 * Cast the second top Element of the Stack to the top element
	 * of the Stack
	 * @param typeID
	 * @throws FFaplAlgebraicException
	 */
	public void castElementOnStackTo(int typeID) throws FFaplAlgebraicException;

	/**
	 * simulates an enter of a procedure or function
	 * @throws FFaplAlgebraicException
	 */
	public void enterProcFunc() throws FFaplAlgebraicException;
	
	/**
	 * simulates an exit of a procedure or function
	 * @throws FFaplAlgebraicException
	 */
	public void exitProcFunc() throws FFaplAlgebraicException;
	
	/**
	 * Simulates return of function.
	 * Runtime effect: rt = pop()
	 */
	public void funcReturn();
	
	/**
	 * Runtime effect: push(rt);
	 */
	public void loadReturn();

	/**
	 * runtime effect: c = pop(), b = pop(), a = pop(), a[b] = c
	 * @throws FFaplAlgebraicException
	 */
	public void storeArrayElement() throws FFaplAlgebraicException;

	/**
	 * load array length
	 * @throws FFaplAlgebraicException
	 */
	public void arrayLen() throws FFaplAlgebraicException;

	/**
	 * @param offset the offset to check
	 * @return true if offset is allocated, false otherwise
	 */
	public boolean isStackOffsetAllocated(int offset);


	String getRecordScope(int index, ISymbol scope, ISymbol recordN);

	public String getIndex_procedureStackSymbols(String recordN);

}
