package ffapl.types;

import java.util.UUID;

/**
 * Represents a GaloisField type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplGaloisField extends ComplexAlgebraicType {

	//Unique ID for GaloisField
	protected UUID _uuid;
	
	/**
	 * Constructor
	 */
	public FFaplGaloisField(){
		_typeID = FFaplTypeCrossTable.FFAPLGF;
		_uuid = UUID.randomUUID();
	}
	
	public FFaplGaloisField(UUID uuid){
		_typeID = FFaplTypeCrossTable.FFAPLGF;
		_uuid = uuid;
	}
	
	
	@Override 
	public boolean isCompatibleTo(Type type){
		//TODO check Polynom
		return type instanceof FFaplGaloisField;
	}
	
	/**
	 * Return unique id of the Type
	 * @return
	 */
	public UUID getUUID(){
		return _uuid;
	}
	
	public Type clone() {
		return new FFaplGaloisField(_uuid);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ffapl.types.Type#toString()
	 
	public String toString(){
		return FFaplTypeCrossTable.TYPE_Name[_typeID] + " ID: " + _uuid;
	}
	*/
}
