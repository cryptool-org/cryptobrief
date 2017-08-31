/**
 * 
 */
package ffapl.lib;

import java.util.Iterator;
import java.util.Vector;

import ffapl.exception.FFaplException;
import ffapl.lib.interfaces.IAttribute;
import ffapl.lib.interfaces.ICompilerError;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplSymbolTable implements ISymbolTable {

	private ISymbol _currentScope;
	private ISymbol _root;
	private boolean _isGlobal;
	private ISymbol _parentSymbol;//Head of the Scope
	private ISymbol _actualSymbol;
	private Vector<ISymbol[]> _overloading; //holds Symbols to control overloading for function and procedure

	
	/**
	 * Constructor
	 */
	public FFaplSymbolTable() {
		_currentScope = null;
		_parentSymbol = null;
		_actualSymbol = null;
		_root = null;
		_overloading = new Vector<ISymbol[]>(0,1);
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#openScope(boolean)
	 */
	@Override
	public void openScope(boolean isGlobal) throws FFaplException{
		openScope(isGlobal, false);		
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#openScope(boolean, boolean)
	 */
	@Override
	public void openScope(boolean isGlobal, boolean isShielded)
			throws FFaplException {
		if (_currentScope == null){// Control if scope is set
			Object[] arguments = {"SymbolTable openScope"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		} 
		_isGlobal = isGlobal;
		_currentScope = _actualSymbol;
		_currentScope.setShielded(isShielded);
		_parentSymbol = _currentScope.local();
		_actualSymbol = getLastSymbol(_parentSymbol);	
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#openScope(ISymbol, boolean)
	 */
	@Override
	public void openScope(ISymbol symbol, boolean isGlobal) throws FFaplException{
		
		openScope(symbol, isGlobal, false);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#openScope(ISymbol, boolean, boolean)
	 */
	@Override
	public void openScope(ISymbol symbol, boolean isGlobal, boolean isShielded)
			throws FFaplException {
		_isGlobal = isGlobal;
		if (_currentScope != null){
			this.addSymbol(symbol);
			_currentScope = symbol;
		}else{//first scope
			_root = symbol;
			_currentScope = symbol;
			_currentScope.setLevel(0);
		}
		_currentScope.setShielded(isShielded);
		_parentSymbol = symbol.local();
		_actualSymbol = getLastSymbol(_parentSymbol);
		
	}	
	
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#closeScope()
	 */
	@Override 
	public void closeScope() throws FFaplException {
		if (_currentScope == null){// Control if scope is set
			Object[] arguments = {"SymbolTable closeScope"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		controlOverloading(_overloading);
		_isGlobal = _currentScope.isGlobal();
		_currentScope = _currentScope.scope();
		if(_currentScope != null){
			_parentSymbol = _currentScope.local();	
		}else{
			_parentSymbol = null;
		}
		_actualSymbol = getLastSymbol(_parentSymbol);
		
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#lookup(java.lang.String)
	 */
	@Override
	public Vector<ISymbol> lookupsimilar(String symbolname) throws FFaplException {
		ISymbol scope = null;
		Vector<ISymbol> result = new Vector<ISymbol>(0,1);
		ISymbol next = null;
		
		if (_currentScope == null || symbolname == null){// Control if scope is set
			Object[] arguments = {"SymbolTable lookupsimilar"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		scope = _currentScope;
		next = _parentSymbol;
		while (scope != null){
			if(next == null){//end of list, go one scope up 
				if(!scope.isShielded()){ //if not shielded
					scope = scope.scope();
					if(scope != null){
						next = scope.local();
					}
				}else{
					scope = null;
				}
			}else if (symbolname.equals(next.getName())){
				result.add(next);
				next = next.next();
			}else{
				next = next.next();
			}				
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#lookup(ffapl.lib.interfaces.ISymbol)
	 */
	@Override
	public Vector<ISymbol> lookupsimilar(ISymbol symbol) throws FFaplException {
		ISymbol scope = null;
		Vector<ISymbol> result = new Vector<ISymbol>(0,1);
		ISymbol next = null;	
		if (_currentScope == null || symbol == null){// Control if scope is set
			Object[] arguments = {"SymbolTable lookupsimilar"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		scope = _currentScope;
		next = _parentSymbol;
		while (scope != null){
			if(next == null){//end of list, go one scope up 
				if(!scope.isShielded()){ //if not shielded
					scope = scope.scope();
					if(scope != null){
						next = scope.local();
					}
				}else{
					scope = null;
				}
			}else if (symbol.similarTo(next)){
				result.add(next);
				next = next.next();
			}else{
				next = next.next();
			}				
		}
		
		return result;
	}
	
	@Override
	public ISymbol lookup(ISymbol symbol) throws FFaplException {
		// delete print
		ISymbol scope = null;
		ISymbol result = null;
		ISymbol next = null;	
		if (_currentScope == null || symbol == null){// Control if scope is set
			Object[] arguments = {"SymbolTable lookup(symbol)"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		scope = _currentScope;
		next = _parentSymbol;
		//System.out.println("Scope: " + _currentScope + " -search: " + symbol);
		while (result == null && scope != null){
			if(next == null){//end of list, go one scope up 
				//if(!scope.isShielded()){ //if not shielded
					scope = scope.scope();
					if(scope != null){
						next = scope.local();
					}
				//}else{
				//	scope = null;
				//}
		     
			}else if (symbol.equals(next)){
				result = next;
			}else{
				next = next.next();
			}				
		}
		return result;
	}
	
	@Override
	public ISymbol lookup(ISymbol symbol, Vector<IAttribute> attribv)
			throws FFaplException {
		ISymbol tmp1 = null, tmp2;
		if (symbol == null || symbol.local() != null){// Control if scope is set
			Object[] arguments = {"SymbolTable lookup(Symbol, Vector Attribute)"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
       if(attribv != null){//has attributes -> Arguments
			for(Iterator<IAttribute> attr = attribv.iterator(); attr.hasNext();){
				tmp2 = new FFaplSymbol("argument", null, attr.next().getType(), ISymbol.PARAMETER);
				if(tmp1 != null){
					tmp1.setNext(tmp2);
				}else{
					symbol.setLocal(tmp2);
				}	
				tmp1 = tmp2;
			}
       }
		return lookup(symbol);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#lookuplocal(ISymobl, ISymbol)
	 */
	public ISymbol lookuplocal(ISymbol parent, ISymbol symbol) 
		throws FFaplException{
		ISymbol result = null;
		ISymbol next = null;	
		if (parent == null || symbol == null){// Control if scope is set
			Object[] arguments = {"SymbolTable lookuplocal"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		next = parent.local();
		
		while (result == null && next != null){
			if (symbol.equals(next)){
				result = next;
			}else{
				next = next.next();
			}				
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#getParentScopeSymbol()
	 */
	@Override
	public ISymbol getParentScopeSymbol() throws FFaplException {
		if (_currentScope == null){// Control if scope is set
			Object[] arguments = {"SymbolTable getParentScopeSymbol"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}else{
			return _parentSymbol;
		}
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#addSymbol(ffapl.lib.interfaces.ISymbol)
	 */
	@Override
	public void addSymbol(ISymbol symbol) throws FFaplException {
		ISymbol tmpsymbol;
		if (_currentScope == null){// Control if scope is set
			Object[] arguments = {"SymbolTable addSymbol"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		if (symbol != null){
			symbol.setGlobal(_isGlobal);//setGlobal State
			symbol.setLevel(_currentScope.level() + 1);//set level of Symbol
			tmpsymbol = controlHiding(lookupsimilar(symbol), symbol);
			if( tmpsymbol != null ){
				Object[] arguments = { "'" + tmpsymbol.getName() + "'",  
									   "'" + tmpsymbol.getKindStr() + "'"};//for error
				throw new FFaplException(arguments, ICompilerError.SYMBOL_EXISTS, symbol.getToken());
			}
			
				
				if(_parentSymbol != null){
				   _actualSymbol.setNext(symbol);
				   _actualSymbol = symbol;
				}else{//first Symbol
				   _parentSymbol = symbol;
				   _currentScope.setLocal(_parentSymbol);
				   _actualSymbol = _parentSymbol;
				}
				 symbol.setScope(_currentScope);
							
		}else{
			Object[] arguments = {"SymbolTable addSymbol"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.ISymbolTable#currentScope()
	 */
	@Override
	public ISymbol currentScope() {
		return _currentScope;
	}
	
	@Override
	public ISymbol root(){
		return _root;
	}
	
	/**
	 * Returns the last Symbol of the list beginning with <Code> symbol </Code>
	 * @param symbol
	 * @return
	 */
	private ISymbol getLastSymbol(ISymbol symbol){
		ISymbol iterator = symbol;
		if(iterator != null){
			while(iterator.next() != null){
				iterator = iterator.next();
			}
		}
		return iterator;
	}
	
	/**
	 * Controls the overloading of Function and procedures after enough information is
	 * available
	 * @param symbols
	 */
	private void controlOverloading(Vector<ISymbol[]> symbols) throws FFaplException{
		ISymbol[] tmp;
		if (symbols != null){
			for (final Iterator<ISymbol[]>  itr = symbols.iterator(); itr.hasNext();) {
			      tmp = itr.next();
				if(tmp[0].level() ==  _currentScope.level() + 1){
					
					if(tmp[0].equals(tmp[1])){
						Object[] arguments = { "'" + tmp[1].getName() + "'",  
								   "'" + tmp[1].getKindStr() + "'"};//for error
						throw new FFaplException(arguments, ICompilerError.SYMBOL_EXISTS, tmp[0].getToken());
					}
				}
			}
		}
	}
	
	/**
	 * Controls the hiding of Variables and adds Procedures and functions to the
	 * Overloading Vector for later control
	 * @param lookupsymbols
	 * @param symbol
	 * @return
	 */
	private ISymbol controlHiding(Vector<ISymbol> lookupsymbols, ISymbol symbol){
		ISymbol tmp;
		ISymbol[] overload;
		ISymbol result = null;
		for (final Iterator<ISymbol>  itr = lookupsymbols.iterator(); itr.hasNext();) {
		      tmp = itr.next();
			  if( (!tmp.isGlobal() || symbol.isGlobal() ) && 
			      tmp.getKind() != ISymbol.FUNCTION && 
			      tmp.getKind() != ISymbol.PROCEDURE &&
			      symbol.getKind() != ISymbol.FUNCTION && 
		          symbol.getKind() != ISymbol.PROCEDURE ){
				   return tmp; //no hiding possible;
			  }else if(  
			  (
			    ( 	tmp.getKind() == ISymbol.FUNCTION || 
			    		tmp.getKind() == ISymbol.PROCEDURE 
			     ) 
			     &&
			    (	symbol.getKind() == ISymbol.FUNCTION || 
			    		symbol.getKind() == ISymbol.PROCEDURE 
			     )
			   ) 
			 ){//Both are function or Procedures
           //add for later hiding control
				overload = new ISymbol[2];
				overload [0] = symbol;
				overload [1] = tmp;
				_overloading.add(overload);
			  }
		}
		return result;
	}
	
	@Override
	public ISymbol cloneSubTree(ISymbol symbol, ISymbol scope){
		ISymbol  clone = null;
		ISymbol  temp = null;
		
		if(symbol != null){
			clone = symbol.clone();
			clone.setScope(scope);//sets the scope
			//clone local symbols
			temp = cloneSubTree(symbol.local(), clone);
			clone.setLocal(temp);
			//clone next symbols
			temp = cloneSubTree(symbol.next(), scope);
			clone.setNext(temp);	
		}
		
		return clone;
		
	}

	@Override
	public void setScope(ISymbol scope) throws FFaplException {
		if (scope == null){// Control if scope is set
			Object[] arguments = {"SymbolTable setScope"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		_currentScope = scope;
		
		
		_parentSymbol = _currentScope.local();	
		if(_parentSymbol != null){
			_isGlobal = _currentScope.local().isGlobal();
		}
		_actualSymbol = getLastSymbol(_parentSymbol);
		
	}
	
	@Override
	public ISymbol getParam(ISymbol procfunc, int i) throws FFaplException{
		ISymbol symbol;
		symbol = procfunc.local();
		
		for(int j = 1; j < i; j++){
			if(symbol != null){
				symbol = symbol.next();
			}else{
				Object[] arguments = {"getParam"};
				throw new FFaplException(arguments, ICompilerError.INTERNAL);
			}
		}
		if(symbol == null){
			Object[] arguments = {"getParam"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}		
		return symbol;
	}

	

	public ISymbol getActualSymbol(){
		return this._actualSymbol;
	}

	
	
	

}
