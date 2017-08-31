/**
 * 
 */
package ffapl.types;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplString extends Type {

	
	private String _value;
	
	/**
	 * Constructor
	 */
	public FFaplString() {
		_value = "";
		this._typeID = FFaplTypeCrossTable.FFAPLSTRING;
	}
	
	/**
	 * Constructor
	 * @param _value
	 */
	public FFaplString(String value){
		this();
		if(value.startsWith("\"")){
			value = value.substring(1, value.length());
		}
		if(value.endsWith("\"")){
			value = value.substring(0, value.length() - 1);
		}
		_value = value.replace("\\\"", "\"");
		//_value = value.replace("\"", "");//Remove "
		_value = _value.replace("\\n", "\n");// new line
		_value = _value.replace("\\t", "\t");//tab
		
	}

	/* (non-Javadoc)
	 * @see ffapl.types.Type#isCompatibleTo(ffapl.types.Type)
	 */
	@Override
	public boolean isCompatibleTo(Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ffapl.types.Type#clone()
	 */
	@Override
	public Type clone() {
		return new FFaplString(_value);
	}
	
	public String getValue(){
		return _value;
	}

}
