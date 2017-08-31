/**
 * 
 */
package sunset.gui.api;

import java.util.Collections;
import java.util.Vector;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Parameter {

	
	private String _name;
	private Vector<String> _types;
	
	
	public Parameter(String name) {
		_name = name;
		_types = new Vector<String>(0,1);
	}
	
	/**
	 * adds the Type to the parameter
	 * @param type
	 */
	public void addType(String type){
		_types.add(type);
		Collections.sort(_types);
	}
	
	/**
	 * @return the parameter name
	 */
	public String getName(){
		return _name;
	}
	
	/**
	 * @return copy of types
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> getTypes(){
		return (Vector<String>) _types.clone();
	}

}
