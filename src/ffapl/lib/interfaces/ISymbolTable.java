package ffapl.lib.interfaces;

import java.util.Vector;

import ffapl.exception.FFaplException;

/**
 * Interface for a SymbolTable for the FFCompiler
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface ISymbolTable {
	
	
	/**
	 * open Scope and specifies if Scope is Global, the father is the actual Symbol
	 * @param isGlobal
	 * @throws FFaplException
	 *         (internal) if internal error occurs 
	 */
	public void openScope(boolean isGlobal) throws FFaplException;
	
	/**
	 * open Scope and specifies if Scope is Global, the father is the actual Symbol,specifies
	 * if scope is shielded to the outer scope
	 * @param isGlobal
	 * @param isShielded
	 * @throws FFaplException
	 *         (internal) if internal error occurs 
	 */
	public void openScope(boolean isGlobal, boolean isShielded) throws FFaplException;
	
	/**
	 * adds the Symbol to the current scope and open Scope and specifies if Scope is Global
	 * @param symbol
	 * @param isGlobal
	 * @throws FFaplException
	 *         (internal) if internal error occurs
	 */
	public void openScope(ISymbol symbol, boolean isGlobal) throws FFaplException;
	
	/**
	 * adds the Symbol to the current scope and open Scope and specifies if Scope is Global, specifies
	 * if scope is shielded to the outer scope
	 * @param symbol
	 * @param isGlobal
	 * @param isShielded
	 * @throws FFaplException
	 *         (internal) if internal error occurs
	 */
	public void openScope(ISymbol symbol, boolean isGlobal, boolean isShielded) throws FFaplException;
	
	/**
	 * close the current Scope
	 * @throws FFaplException
	 *         (internal) if internal error occurs
	 */
	public void closeScope() throws FFaplException;
	
	/**
	 * Search if similar Symbol exists in Scope to the specified <code> symbolID </code>
	 * Symbol from inner scope will hide symbols from global scope. Considers
	 * shield option
	 * @param symbolname
	 * @return <code> null </code> if the Symbol does not exist
	 * @throws FFaplException 
	 * 			(internal) if internal error occurs
	 */
	public Vector<ISymbol> lookupsimilar(String symbolname) throws FFaplException;
	
	/**
	 * Search if similar Symbol exists in Scope to the specified <code> symbol </code>
	 * Symbol from inner scope will hide symbols from global scope.
	 * considers shield option
	 * @param symbol
	 * @return <code> null </code> if the Symbol does not exist
	 * @throws FFaplException 
	 * 			(internal) if internal error occurs
	 */
	public Vector<ISymbol> lookupsimilar(ISymbol symbol) throws FFaplException;
	
	/**
	 * Search if Symbol exists in Scope to the specified <code> symbol </code>
	 * Symbol from inner scope will hide symbols from global scope.
	 * Does not consider the shield option
	 * @param symbol
	 * @return <code> null </code> if the Symbol does not exist
	 * @throws FFaplException 
	 * 			(internal) if internal error occurs
	 */
	public ISymbol lookup(ISymbol symbol) throws FFaplException;
	
	/**
	 * Search if Symbol exists with specified attributes in Scope 
	 * to the specified <code> symbol </code>. Only for procedure
	 * and function
	 * Symbol from inner scope will hide symbols from global scope.
	 * Does not consider the shield option
	 * @param symbol
	 * @param attribv
	 * @return
	 * @throws FFaplException
	 * 			(internal) if internal error occurs
	 */
	public ISymbol lookup(ISymbol symbol, Vector<IAttribute> attribv) throws FFaplException;
	
	/**
	 * Search if Symbol <code> symbol </code> exists in local Scope to the specified Symbol <code> parent </code>
	 * @param parent
	 * @param symbol
	 * @return <code> null </code> if the Symbol does not exist
	 * @throws FFaplException 
	 * 			(internal) if internal error occurs
	 */
	public ISymbol lookuplocal(ISymbol parent, ISymbol symbol) throws FFaplException;
   
	/**
    * Returns the Parent Symbol of the scope
    * @return
    * @throws FFaplException
    * 			(internal) if internal error occurs
    */
	public ISymbol getParentScopeSymbol() throws FFaplException;
	
	/**
	 * Adds Symbol to the current Scope
	 * @param nSymbol
	 * @throws FFaplException
	 * 			(SymbolExists) if Symbol exists in current scope. If Symbol already
	 * 			exits in upper scope the error will be also thrown, except found symbol
	 *          is Global
	 * @throws FFaplException
	 * 			(internal) if internal error occurs
	 */
	public void addSymbol(ISymbol symbol) throws FFaplException;
	
	/**
	 * Returns the currentScope
	 * @return
	 */
	public ISymbol currentScope();
	
	/**
	 * Return the root of the Symbol Table
	 * @return
	 */
	public ISymbol root();
	
	/**
	 * Clone the Subtree including next Symbols beginning with <Code> symbol </Code>
	 * the new scope of the cloned symbol is the specified <Code> scope </Code> 
	 * @param symbol
	 * @param scope
	 * @return
	 */
	public ISymbol cloneSubTree(ISymbol symbol, ISymbol scope);
	
	/**
	 * Sets the scope do the defined Symbol
	 * @param symbol
	 */
	public void setScope(ISymbol scope) throws FFaplException;
	
	/**
	 * Returns the i-th parameter of a function or procedure, starting at 1
	 * @param procfunc
	 * @param i
	 * @return
	 * @throws FFaplException
	 */
	public ISymbol getParam(ISymbol procfunc, int i) throws FFaplException;
	
	
	public ISymbol getActualSymbol();
}
