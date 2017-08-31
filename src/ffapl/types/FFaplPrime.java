package ffapl.types;

import java.math.BigInteger;

/**
 * Represents a Prime type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplPrime extends AlgebraicType {

	private BigInteger _value;
	
	/**
	 * Constructor
	 */
	public FFaplPrime(){
		this(null);
	}
	
	/**
	 * Constructor
	 * @param value
	 */
	public FFaplPrime(BigInteger value){
		_typeID = FFaplTypeCrossTable.FFAPLPRIME;
		_value = value;
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
		return type instanceof FFaplInteger || 
		type instanceof FFaplResidueClass ||
		type instanceof FFaplPolynomial;
	}
	
	@Override
	public Type clone() {
		return new FFaplPrime(_value);
	}	
	
	/**
	 * Returns true if two types are equal
	 * @param type
	 * @return
	 */
	public boolean equals(Type type){
		return _typeID == type.typeID(); //|| type.typeID() == FFaplTypeCrossTable.FFAPLINTEGER;
	}
}
