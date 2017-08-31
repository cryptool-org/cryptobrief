package ffapl.types;

import java.util.UUID;

/**
 * Represents a GaloisField type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplEllipticCurve extends ComplexAlgebraicType {

	//Unique ID for GaloisField
	protected UUID _uuid;
	
	/**
	 * Constructor
	 */
	public FFaplEllipticCurve(){
		_typeID = FFaplTypeCrossTable.FFAPLEC;
		_uuid = UUID.randomUUID();
	}
	
	public FFaplEllipticCurve(UUID uuid){
		_typeID = FFaplTypeCrossTable.FFAPLEC;
		_uuid = uuid;
	}
	
	
	@Override 
	public boolean isCompatibleTo(Type type){
		//TODO check Polynom
		return type instanceof FFaplEllipticCurve;
	}
	
	/**
	 * Return unique id of the Type
	 * @return
	 */
	public UUID getUUID(){
		return _uuid;
	}
	
	public Type clone() {
		return new FFaplEllipticCurve(_uuid);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ffapl.types.Type#toString()
	 
	public String toString(){
		return FFaplTypeCrossTable.TYPE_Name[_typeID] + " ID: " + _uuid;
	}
	*/
}
