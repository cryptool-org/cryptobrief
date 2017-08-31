/**
 * 
 */
package ffapl.lib;

import ffapl.lib.interfaces.IAttribute;
import ffapl.types.Type;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplAttribute implements IAttribute {

	private Type _type;
	private int _kind;
	private byte _register;
	
	/**
	 * Constructor
	 */
	public FFaplAttribute() {
		this(IAttribute.NONE);
	}
	
	/**
	 * Constructor
	 * @param kind
	 */
	public FFaplAttribute(int kind){
		this(kind, null);
	}
	
	/**
	 * Constructor
	 * @param kind
	 * @param type
	 */
	public FFaplAttribute(int kind, Type type){
		_kind = kind;
		_type = type;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#setType(ffapl.types.Type)
	 */
	@Override
	public void setType(Type type) {
		_type = type;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#setKind(int)
	 */
	@Override
	public void setKind(int kind) {
		_kind = kind;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#setRegister(byte)
	 */
	@Override
	public void setRegister(byte register) {
		_register = register;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#getType()
	 */
	@Override
	public Type getType() {
		return _type;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#getKind()
	 */
	@Override
	public int getKind() {
		return _kind;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#getRegister()
	 */
	@Override
	public byte getRegister() {
		return _register;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttribute#isConstant()
	 */
	@Override
	public boolean isConstant() {
		return _kind == IAttribute.CONSTANT;
	}
	
	@Override
	public String toString(){
		return _type.toString();
	}

}
