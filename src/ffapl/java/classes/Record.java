/**
 * 
 */
package ffapl.java.classes;

import java.util.HashMap;

import ffapl.java.interfaces.IJavaType;
import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Record implements IJavaType<Record> {

	
	private HashMap<String, IJavaType> _record;
	/**
	 * 
	 */
	public Record() {
		_record = new HashMap<String, IJavaType>();
	}
	
	/**
	 * Maps the specified id to the specified value in this record. 
	 * @param id
	 * @param val
	 */
	public void addElement(String id, IJavaType val){
		_record.put(id, val);
	}
	
	/**
	 * Returns the value to which the specified id is mapped, 
	 * or null if this map contains no mapping for the key. 
	 * @param id
	 * @return
	 */
	public IJavaType getElement(String id){
		return _record.get(id);
	}

	@Override
	public int typeID() {
		return IJavaType.RECORD;
	}

	@Override
	public String classInfo() {
		return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLRECORD];
	}
	
	@Override
	public Record clone(){
		return null;
	}
	
	@Override
	public String toString(){
		return _record.toString();
	}
	
	@Override
	public boolean equalType(Object type) {
		if(type instanceof Record){
			return true;
		}
		return false;
	}

}
