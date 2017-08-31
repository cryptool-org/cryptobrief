package ffapl.types;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Represents a Integer Residue type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplResidueClass extends ComplexAlgebraicType {

	private BigInteger _value;
	
	protected UUID _uuid; //Unique id for Residue class
	
	/**
	 * Constructor
	 */
	public FFaplResidueClass(){
		this(null);
		_uuid = UUID.randomUUID();
	}
	
	/**
	 * Constructor
	 * @param value
	 */
	public FFaplResidueClass(BigInteger value){
		_value = value;
		_typeID = FFaplTypeCrossTable.FFAPLRESIDUECLASS;
		_uuid = UUID.randomUUID();
	}
	
	/**
	 * Constructor
	 * @param value
	 * @param uuid
	 */
	public FFaplResidueClass(BigInteger value, UUID uuid){
		_value = value;
		_typeID = FFaplTypeCrossTable.FFAPLRESIDUECLASS;
		_uuid = uuid;
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
		return type instanceof FFaplResidueClass || 
		                  type instanceof FFaplInteger;
	}
	
	/**
	 * Return unique id of the Type
	 * @return
	 */
	public UUID getUUID(){
		return _uuid;
	}
	
	public Type clone() {
		return new FFaplResidueClass(_value, _uuid);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see ffapl.types.Type#toString()
	 
	public String toString(){
		return FFaplTypeCrossTable.TYPE_Name[_typeID];// + " ID: " + _uuid;
	}
	*/
}
