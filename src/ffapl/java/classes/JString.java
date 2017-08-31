/**
 * 
 */
package ffapl.java.classes;

import ffapl.java.interfaces.IJavaType;
import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class JString implements IJavaType {

	private String _value;
	/**
	 * 
	 */
	public JString() {
		_value = "";
	}

	/**
	 * Constructor
	 * @param value
	 */
	public JString(String value) {
		_value = value;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJavaType#typeID()
	 */
	@Override
	public int typeID() {
		return IJavaType.STRING;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJavaType#classInfo()
	 */
	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLSTRING];
	}

	public JString clone(){
		return new JString(_value);
	}
	
	@Override
	public String toString(){
		return _value;
	}
	
	@Override
	public boolean equalType(Object type) {
		if(type instanceof JString){
			return true;
		}
		return false;
	}
}
