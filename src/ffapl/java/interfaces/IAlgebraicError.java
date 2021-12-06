package ffapl.java.interfaces;

/** 
 * Generic FFAPL Java Error Interface
 * It defines error numbers and common methods of exception and error types
 * thrown by the FFAPL execution.
 * 
 * @author Alexander Ortner
 */
public interface IAlgebraicError {

/* ---- Algebraic Error Numbers ---- */
	
	/** execution interrupted */
	public static final int INTERRUPT 				 = 1099;
	
	/** internal Error */
	public static final int INTERNAL                 = 1100;
	
	/** Divide by zero. */
	public static final int DIVZERO                  = 1101;
	
	/** Number not Prime. */
	public static final int NOTPRIME                 = 1102;
	
	/** Polynom not irreducible. */
	public static final int NOTIRREDUCIBLE           = 1103;
	
	/** Unequal characteristics. */
	public static final int CHARACTERISTIC_UNEQUAL   = 1104;
	
	/** Types are not compatible **/
	public static final int TYPES_INCOMPATIBLE       = 1105;
	
	/** no multiplicative inverse available **/
	public static final int NO_MULTINVERSE           = 1106;
	
	/** Negative not allowed **/
	public static final int NEGATIVE_EXPONENT        = 1107;
	
	/** Division not reasonable **/
	public static final int DIVISION_NOT_REASONABLE   = 1108;
	
	/** Division not reasonable **/
	public static final int MOD_NOT_REASONABLE        = 1109;
	
	/** Illegal Cast **/
	public static final int CAST_NOT_POSSIBLE        = 1110;

	/** to high exponent for calculation **/
	public static final int TO_HIGH_EXPONENT         = 1111;
	
	/** minimum is greater than maximum */
	public static final int MIN_GREATER_EQUAL_MAX    = 1112;
	
	/** value must be > 0 */
	public static final int VAL_LESS_EQUAL_ZERO      = 1113;
	
	/** value must be >= 0 */
	public static final int VAL_LESS_ZERO      		= 1114;
	
	/** length too high for array */
	public static final int ARR_LENGTH_TOOHIGH      = 1115;
	
	/** Array index out of bound	 */
	public static final int ARR_INDEX_OUT_OF_BOUNDS = 1116;
	
	/** Value is null	 */
	public static final int VALUE_IS_NULL			= 1117;

	/** Some coeffiecients has no inverse **/
	public static final int NO_MULTINVERSE_COEFF    = 1118;

	/** PrimeFactors are unequal to **/
	public static final int PRIMITIVE 				= 1119;

	/** Exponent < 0 **/
	public static final int EXPONENT_LESS_ZERO 		= 1120;
	
	/** underfull AES block  **/
	public static final int ILLEGAL_AES_INPUT		= 1132;

	/** WARNING **/
	public static final int WARNING_IMPLICIT_CAST	= 2000;
	
	/** WARNING **/
	public static final int WARNING_OVERSIZE_AES_KEY	= 2002;
	

	public static final int WRONG_EC_FIELD		 	= 1121;

	public static final int EC_POINT_ERROR 			= 1122;
	
	public static final int INVALID_EC_RESIDUE_CLASS_CHARACTERISTIC 			= 1123;

	public static final int WEIERSTRASS_SINGULAR		= 1124;
        
    public static final int RESIDUE_CLASS_CHARACTERISTIC_POSITIVE = 1125;

    public static final int NEGATIVE_MODUL = 1126;

    public static final int SQUARE_ROOT_DOES_NOT_EXIST = 1127;

    public static final int EC_FIELD_ERROR = 1128;
    
    public static final int EC_PAIRING_PARAMETER_NOT_VALID = 1129;
    
    public static final int EC_BASEFIELD_ERROR = 1130;

    public static final int EC_GET_PARAMETER_ERROR = 1131;

	public static final int SQRT_COMPOSITE_MODULUS = 1133;

	/** Field element is not primitive **/
	public static final int NOT_PRIMITIVE = 1201;

	/** Q-Matrix cannot be computed for degree or characteristic less than two **/
	public static final int Q_MATRIX_DEGREE = 1202;

	/** Curve has no solutions **/
	public static final int EC_NO_POINTS = 1203;

	/** Could not find any solutions for curve **/
	public static final int EC_NO_POINTS_FOUND = 1204;

	/** Operation can take a long time with the given arguments **/
	public static final int WARNING_OPERATION_SLOW = 1205;

	/** Integer Factorization can take a lot of time for big arguments **/
	public static final int WARNING_FACTORIZATION_SLOW = 1206;

	/** Task cannot be performed on non square matrix **/
	public static final int  MATRIX_NOT_SQUARE = 1207;

	/** Singular matrix cannot be LUP decomposed **/
	public static final int SINGULAR_MATRIX = 1208;

	/** Random points not implemented yet **/
	public static final int NOT_IMPLEMENTED = 9999;


	/** Different array lengths for congruences and moduli **/
	public static final int CRT_DIFFERENT_ARRAY_LENGTHS = 1300;
	/** Array is empty, non-empty expected **/
	public static final int CRT_ARRAY_EMPTY = 1301;
	/** Zeroes or negative values in moduli array detected **/
	public static final int CRT_ZERO_OR_NEGATIVE_MODULES = 1302;
	/** This Chinese Remainder Theorem feature is not implemented yet **/
	public static final int CRT_NOT_IMPLEMENTED = 1303;



    /**
	 * Return the compiler error number.
	 * @return error number
	 */
	public int errorNumber();
	
	/** 
	 * Return the detailed error message. 
	 * @return error message
	 * */
	public String getErrorMessage();
	
}
