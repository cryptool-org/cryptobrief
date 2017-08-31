/**
 * 
 */
package ffapl.java;

import ffapl.java.interfaces.IJAttribute;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class JAttribute implements IJAttribute {

	
	private Object _type;
	
	private int _kind;
	
	private int _offset;
	
	private boolean _isglobal;
	
	
	/**
	 * Constructor
	 * @param kind
	 */
	public JAttribute(int kind) {
		_kind = kind;
		_offset = -1;
		_isglobal = false;
	}
	
	/**
	 * Constructor
	 * @param kind
	 * @param type
	 */
	public JAttribute(int kind, Object type){
		this(kind);
		_type = type;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#setType(ffapl.types.Type)
	 */
	@Override
	public void setType(Object type) {
		_type = type;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#setKind(int)
	 */
	@Override
	public void setKind(int kind) {
		_kind = kind;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#getType()
	 */
	@Override
	public Object getType() {
		return _type;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#getKind()
	 */
	@Override
	public int getKind() {
		return _kind;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#setOffset(int)
	 */
	@Override
	public void setOffset(int offset) {
		_offset = offset;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#getOffset()
	 */
	@Override
	public int getOffset() {
		return _offset;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#isGlobal()
	 */
	@Override
	public boolean isGlobal() {
		return _isglobal;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#isConstant()
	 */
	@Override
	public boolean isConstant() {
		return _kind == IJAttribute.CONSTANT;
	}

	/* (non-Javadoc)
	 * @see ffapl.java.interfaces.IJAttribute#setGlobal()
	 */
	@Override
	public void setGlobal(boolean global) {
		_isglobal = global;		
	}

}
