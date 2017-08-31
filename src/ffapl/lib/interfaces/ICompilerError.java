package ffapl.lib.interfaces;

/** Generic FFAPL compiler Error Interface
 * It defines error numbers and common methods of exception and error types
 * thrown by the FFAPL compiler.
 * 
 * @author Alexander Ortner
 */
public interface ICompilerError {

	/* ##### ERROR Numbers ##### */
	
	/* ---- Global Error Numbers ---- */
	
	/** Internal error. */
	public static final int INTERNAL             = 100;
	/** Lexical error. */
	public static final int LEXICAL              = 101;
	/** Syntax error. */
	public static final int SYNTAX               = 102;
	/** Expected one*/
	public static final int SYNTAXSINGLE         = 103;
	/** Expected one*/
	public static final int INTERRUPT            = 104;
	
	/* ---- Errors Symbol checking  ----*/
	
	/** Symbol already declared. */
	public static final int SYMBOL_EXISTS          = 110;
	/** Identifier not declared. */
	public static final int IDENT_NOT_DECL         = 111;
	/** Symbol not initialized. */
	public static final int SYMBOL_NOT_INIT        = 112;
	
	/* ---- Type error checking ----*/
	
	/** Illegal Use of Type */
	public static final int TYPE_ILLEGAL_USE       			= 200;
	/** Readonly variable in Assignment */
	public static final int READONLY_ASSIGN        			= 201;
	/** Expression before '[' is not an array type. */
	public static final int SELECTOR_NOT_ARRAY     			= 202;
	/** Expression before '.' is not an record type. */
	public static final int SELECTOR_NOT_RECORD    			= 203;
	/** Expression after '#' is not an array type. */
	public static final int ARRAY_LEN_NOT_ARRAY    			= 204;
	/** Illegal operand type for unary operator. */
	public static final int ILLEGAL_OP1_TYPE     			= 205;
	/** Illegal operand type for binary operator. */
	public static final int ILLEGAL_OP2_TYPE      			= 206;
	/** Using procedure (not a function) in expression. */
	public static final int PROC_NOT_FUNC_EXPR    			= 207;
	/** Type mismatch in assignment. */
	public static final int TYPE_MISMATCH_ASSIGN  			= 208;
	/** Condition is not a boolean expression. */
	public static final int COND_NOT_BOOL          			= 209;
	/** Returning none or invalid type from function. */
	public static final int INVALID_RETURN_TYPE    			= 210;
	/** Type mismatch in assignment. */
	public static final int TYPE_MISMATCH_FORSTATEMENT   	= 211;
	/**  Illegal use if type in an argument*/
	public static final int ILLEGAL_TYPE_ARGUMENT			= 212;
	/** Implicit cast error **/
	public static final int IMPLICIT_CAST_ERROR 			= 213;
	/** invalid subarray length **/
	public static final int INVALID_SUBARRAY_LENGTH         = 214;
	/** argumentlist not allowed in function or procedure call **/
	public static final int ARGUMENTLIST_NOT_ALLOWED 		= 215;
	/** invalid indeterminate variable**/
	public static final int INVALID_INDETERMINATE			= 216; 
	/** invalid position of break statement **/
	public static final int BREAK_WRONG_POSITION			= 217;
	/** invalid ec-parameter type **/
	public static final int WRONG_EC_PARAMETER_TYPE			= 218;
	/** invalid ec-parameter, only a1, a2, a3, a4, a6 **/
	public static final int WRONG_EC_PARAMETER 				= 219;
	/**  **/
	public static final int WRONG_EC_COORDINATE 			= 220;

	
	/* ##### End of error numbers ##### */
	
	
	
	/** 
	 * Return the compiler error number.
	 * @return error number
	 */
	public int errorNumber();
	
	/**
	 * Return the source code line number where the error occurred.
	 * @return line number 
	 * */
	public int errorLine();
	
	/** 
	 * Return the source code column number where the error occurred.
	 * @return column number 
	 * */
	public int errorColumn();
	
	/** 
	 * Return the detailed error message. 
	 * @return error message
	 * */
	public String getErrorMessage();
	
	/**
	 * Returns the error type
	 * @return
	 */
	public String errorType();
}

