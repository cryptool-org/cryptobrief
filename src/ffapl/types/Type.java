package ffapl.types;

/**
 * Represents the super class of all FFapl types
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public abstract class Type {

	/** The type ID */
	//protected String _typeID = "Type";
	protected int _typeID = FFaplTypeCrossTable.FFAPLTYPE;
	
	
	/**
	 * Returns true if Type is compatible with <Code>type</Code>
	 * @param type
	 * @return
	 */
	public abstract boolean isCompatibleTo(Type type);
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return FFaplTypeCrossTable.TYPE_Name[_typeID];
	}
	
	/**
	 * Return the TypeID
	 * @return
	 */
	public int typeID(){
		return this._typeID;
	}
	
	/**
	 * Returns true if two types are equal
	 * @param type
	 * @return
	 */
	public boolean equals(Type type){
		return _typeID == type.typeID();
	}
	
	/**
	 * Clones the current Type
	 * @return
	 */
	public abstract Type clone();
}
