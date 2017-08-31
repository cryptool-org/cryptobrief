package ffapl.types;


/**
 * Represents a Boolean type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplBoolean extends PrimitiveType {

	
	private Boolean _value;
	
	/**
	 * Default Constructor
	 */
	public FFaplBoolean(){
		this(null);
	}
	
	/**
	 * Constructor
	 * @param value
	 */
	public FFaplBoolean(Boolean value){
		_value = value;
		this._typeID = FFaplTypeCrossTable.FFAPLBOOLEAN;
		
	}
	
	/**
	 * Set Value of the Type
	 * @param value
	 */
	public void setValue(Boolean value){
		_value = value;
	}
	
	/**
	 * Returns the value of the Type
	 * @return
	 */
	public boolean getValue(){
		return _value;
	}
	
	@Override 
	public boolean isCompatibleTo(Type type){
		//compatible with Integer and Residual
		return type instanceof FFaplBoolean;
	}
		
	public Type clone() {
		return new FFaplBoolean(_value);
	}
}
