/**
 * 
 */
package ffapl.java.interfaces;

import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface IJavaType<T extends IJavaType> extends Cloneable{

	public static final byte INTEGER			= 0;
	public static final byte GALOISFIELD 		= 1;
	public static final byte POLYNOMIAL 		= 2;
	public static final byte POLYNOMIALRC 		= 3;
	public static final byte POLYNOMIALRCPRIME 	= 4;
	public static final byte PRIME 				= 5;
	public static final byte RCPRIME 			= 6;
	public static final byte RECORD				= 7;
	public static final byte PSRANDOMGENERATOR	= 8;
	public static final byte RANDOMGENERATOR	= 9;
	public static final byte ARRAY				= 10;
	public static final byte BOOLEAN			= 11;
	public static final byte RESIDUECLASS		= 12;
	public static final byte STRING				= 13;
	public static final byte ELLIPTICCURVE		= 14;
    public static final byte MATRIX             = 15;
	
	public static final byte[] FFapl_Type_Compatibility = {
		FFaplTypeCrossTable.FFAPLINTEGER,
		FFaplTypeCrossTable.FFAPLGF,
		FFaplTypeCrossTable.FFAPLPOLYNOMIAL,
		FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE,
		-1,
		FFaplTypeCrossTable.FFAPLPRIME,
		-1,
		FFaplTypeCrossTable.FFAPLRECORD,
		FFaplTypeCrossTable.FFAPLPSRANDOMG,
		FFaplTypeCrossTable.FFAPLRANDOMG,
		FFaplTypeCrossTable.FFAPLARRAY,
		FFaplTypeCrossTable.FFAPLBOOLEAN,
		FFaplTypeCrossTable.FFAPLRESIDUECLASS,
		FFaplTypeCrossTable.FFAPLSTRING,
		FFaplTypeCrossTable.FFAPLEC		
	};
	
	
	/**
	 * returns the Type ID of the Type
	 * @return
	 */
	public int typeID();
	
	/**
	 * Returns General Info about Type
	 * @return
	 */
	public String classInfo();
	
	/**
	 * returns Clone of the Type
	 * @return
	 */
	public T clone();
	
	/**
	 * returns if two types are equal
	 * e.g. Z(3) is equal to Z(3) but not to Z(5)
	 * @param type
	 * @return
	 */
	public boolean equalType(Object type);
}
