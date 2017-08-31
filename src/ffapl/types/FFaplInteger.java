package ffapl.types;

import java.math.BigInteger;

/**
 * Represents a Integer type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplInteger extends PrimitiveType {
	
	
	private BigInteger _value = null;
	/**
	 * Constructor
	 */
	public FFaplInteger(){
		this._typeID = FFaplTypeCrossTable.FFAPLINTEGER;
	}
	
	/**
	 * Constructor
	 * @param value
	 */
	public FFaplInteger(BigInteger value){
		this();
		_value = value;
		
	}
	
	/**
	 * Constructor
	 * @param str
	 */
	public FFaplInteger(String str){
		this();
		if (str.startsWith("0x"))
		{
			_value = new BigInteger(str.substring(2), 16);
		}
		else 
			_value = new BigInteger(str);
	}
	
	/**
	 * Set Value of the Type
	 * @param value
	 */
	public void setValue(BigInteger value){
		_value = value;
	}
	
	/**
	 * Returns the value of the Type
	 * @return
	 */
	public BigInteger getValue(){
		return _value;
	}
	
	@Override 
	public boolean isCompatibleTo(Type type){
		//compatible with Integer and Residual
		return (   type.typeID() == FFaplTypeCrossTable.FFAPLINTEGER
				|| type.typeID() == FFaplTypeCrossTable.FFAPLRESIDUECLASS
				);
		
	}
	
	@Override
	public Type clone() {
		return new FFaplInteger(_value);
	}
	
	/**
	 * Returns true if two types are equal
	 * @param type
	 * @return
	 */
	public boolean equals(Type type){
		return _typeID == type.typeID();// || type.typeID() == FFaplTypeCrossTable.FFAPLPRIME;
	}
		
}
