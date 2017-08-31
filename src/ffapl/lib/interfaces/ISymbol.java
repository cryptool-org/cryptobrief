package ffapl.lib.interfaces;

import ffapl.types.*;

/**
 * Specifies an interface for an Symbol in FFapl Symboltable
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface ISymbol {
	
	/*Symbol Kinds*/
	public static final int PROGRAM       = 0;
	public static final int PROCEDURE     = 1;
	public static final int FUNCTION      = 2;
	public static final int VARIABLE      = 3;
	public static final int CONSTANT      = 4;
	public static final int PARAMETER     = 5; // formal parameter
	public static final int BLOCK         = 6;

	
	/**
	 * String representation of symbol kind
	 */
	public static final String[] kindArray ={
		"program",
		"procedure",
		"function",
		"variable",
		"constant",
		"paramenter",
		"block",
	};
	
	/**
	 * Returns kind of a Symbol
	 * @return
	 */
	public int getKind();
	
	/**
	 * Returns String representation of the Symbol kind
	 * @return
	 */
	public String getKindStr();
	
	/**
	 * Returns next Symbol linked to this Symbol,
	 * @return
	 */
    public ISymbol next();
    
    /**
     * points to the father
     * @return
     */
    public ISymbol scope();
    
    
    /**
     * Returns Type of the Symbol
     * @return
     */
    public Type getType();
    
    
    /**
     * Returns the Token of the Symbol
     * @return
     */
    public IToken getToken();
    
    /**
     * Returns the pointer to the Child
     * @return
     */
    public ISymbol local();
    
    /**
     * Returns the level of the Scope
     * @return
     */
    public int level();
    
    /**
     * Returns the Symbol name
     * @return
     */
    public String getName();
    
    /**
     * Returns the id of the Symbol
     * @return
     */
    public String getID();
    
    
    /**
     * Returns true if Symbol is Global, false otherwise;
     * @return
     */
    public boolean isGlobal();
    
    /**
     * Returns true if Symbol is Shielded, means do not look above the Symbol
     * in the scope case
     * @return
     */
    public boolean isShielded();
    
    /**
     * Returns true if Symbol is an Reference in case of
     * Array, Record, etc.
     * @return
     */
    public boolean isReference();
    
   /**
    * Returns true if the symbol is initialized
    * @return
    */
    public boolean isInitialized();
    
    /**
     * Returns if the Symbol is readonly
     * @return
     */
    public boolean readonly();
    
    /**
     * Sets the isGlobal value for the Symbol, true if the Symbol is Global
     * false otherwise
     * @param isGlobal
     */
    public void setGlobal(boolean isGlobal);
    
    /**
     * Sets the isShielded value for the Symbol, true if the Symbol is shielded,
     * which means, do not look above the Symbol in case of scoping.
     * @param isShielded
     */
    public void setShielded(boolean isShielded);
    
    /**
     * specifies if the symbol is readonly
     * @param readonly
     */
    public void setReadonly(boolean readonly);
    
    /**
     * Sets the scope of the Symbol
     * @param symbol
     */
    public void setScope(ISymbol symbol);
    
    /**
     * Sets the kind of the Symbol
     * @param kind
     */
    public void setKind(int kind);
    
    /**
     * Sets the pointer to the child
     * @param symbol
     */
    public void setLocal(ISymbol symbol);
    
    /**
     * links a Symbol to this Symbol, used especially for parameter
     * list for functions and procedures.
     * @param nSymbol
     */
    public void setNext(ISymbol symbol);
    
    /** 
     * Set true if the Symbol is a Reference
     * @param isReference
     */
    public void setReference(boolean isReference);
    
    /**
     * Set true if the Symbol is initialized;
     * @param isInitialized
     */
    public void setInitialized(boolean isInitialized);
    
    /**
     * Set the Symbol Type
     * @param sType
     */
    public void setType(Type type);
    
    /**
     * Set scope level
     * @param i
     */
    public void setLevel(int i);
    
    /**
     * Checks if the Symbol is equal to the input. Name and structure are controlled.
     * @param symbol
     * @return
     */
    public boolean equals(ISymbol symbol);
    
    /**
     * Checks if the Symbol is similar to the input. Only name is controlled
     * @param symbol
     * @return
     */
    public boolean similarTo(ISymbol symbol);
    
    /**
     * Returns String representation of the Symbol
     * only for debug issues
     * @return
     */
    public String toString();
    
    /**
     * Clones the Symbol
     * @return
     */
    public ISymbol clone();
    
    /**
     * Sets the offset of the symbol
     * @param offset
     */
    public void setOffset(int offset);
    
    /**
     * Returns the offset of the symbol
     * @return
     */
    public int getOffset();
    
    /**
     * Returns true if the offset is set, false otherwise
     * @return
     */
    public boolean setOffset();

    /**
     * resets the offset to default value
     */
	public void resetOffset();
    
}
