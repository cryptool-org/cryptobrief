package ffapl.java.interfaces;


/**
 * Interface for Java Attribute
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface IJAttribute {
	/**
	 * Invalid Attribute
	 */
	public final static int NONE = 0;
	
	/**
	 * Constant Operand
	 */
	public final static int CONSTANT = 1;
	
	/**
	 * register or stack operand
	 */
	public final static int REGISTER = 2;
	
	/**
	 * memory operand
	 */
	public final static int ADDRESS = 3;
	
	/**
	 * Attribute Object holds the Array Base
	 */
	public final static int ARRAYELEMENT = 4;
		
	/**
	 * Attribute Object holds the Record Base
	 */
	public final static int RECORDELEMENT = 5;
	
	/**
	 * Only specifies a Type
	 */
	public final static int TYPE = 6;
	
	/**
	 * Sets the Type of the Attribute
	 * @param type
	 */
	public void setType(Object type);
	
	/**
	 * Sets the kind of the Attribute
	 * @param kind
	 */
	public void setKind(int kind);
	
	/**
	 * Returns the Type of the Attribute
	 * @return
	 */
	public Object getType();
	
	/**
	 * Returns the Kind of the Attribute
	 * @return
	 */
	public int getKind();
	
	/**
	 * Sets the offset of the Attribute
	 * @param register
	 */
	public void setOffset(int offset);
	
	/**
	 * returns the offset of the Attribute
	 * @return
	 */
	public int getOffset();
	
	/**
	 * returns true if the Attribute is Global
	 * @return
	 */
	public boolean isGlobal();
	
	/**
	 * sets the state of the Attribute
	 * @param global
	 */
	public void setGlobal(boolean global);
	
	
	/**
	 * Returns true if the operand can be evaluated at compile time
	 * @return
	 */
	public boolean isConstant();
}
