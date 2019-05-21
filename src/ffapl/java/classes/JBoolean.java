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
public class JBoolean implements IJavaType<JBoolean>  {

	private boolean _value;
	
	/**
	 * Default constructor
	 */
	public JBoolean(){
		this(false);
	}
	
	/**
	 * Constructor
	 * @param value
	 */
	public JBoolean(boolean value) {
		_value = value;
	}
	
	/**
	 * returns the boolean value
	 * @return
	 */
	public boolean getValue(){
		return _value;
	}
	
	/**
	 * Set the Boolean Value
	 * @param val
	 */
	public void setValue(boolean val){
		_value = val;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJavaType#typeID()
	 */
	@Override
	public int typeID() {
		return IJavaType.BOOLEAN;
	}
	
	@Override
	public String toString(){
		return Boolean.toString(_value);
	}

	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLBOOLEAN];
	}

	/**
	 * return not(value)
	 * @return
	 */
	public JBoolean not() {
		return new JBoolean(!_value);
	}

	/**
	 * return this && b
	 * @param b
	 * @return
	 */
	public JBoolean and(JBoolean b) {
		return new JBoolean(_value && b.getValue());
	}

	/**
	 * Return this || b
	 * @param b
	 * @return
	 */
	public JBoolean or(JBoolean b) {
		return new JBoolean(_value || b.getValue());
	}
	
	@Override
	public JBoolean clone(){
		return new JBoolean(_value);
	}
	
	@Override
	public boolean equalType(Object type) {
		if(type instanceof JBoolean){
			return true;
		}
		return false;
	}

}
