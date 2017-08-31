package ffapl.ast.nodes;


import ffapl.ast.nodes.interfaces.INode;
import ffapl.lib.interfaces.ISymbol;

/**
 * Defines a General FFapl Node in the Syntax tree
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public abstract class FFaplNode implements INode {

	// id of the Node
	protected int _id;
	// Parent node of the node
	protected INode _parent;
	// the symbol scope of the node
	protected ISymbol _symbolscope;
	
	public FFaplNode (int id){
		_id = id; 
	}
	
	
	@Override
	public INode getParent() {
		return _parent;
	}
	
	/**
	 * Returns the scope of the symbols
	 * @return
	 */
	public ISymbol getSymbolScope(){
		return _symbolscope;
	}

	@Override
	public void setParent(INode parent) {
		_parent = parent;
	}
		
	/**
	 * Sets the Symbol scope
	 * @param scope
	 */
	public void setSymbolScope(ISymbol scope){
		_symbolscope = scope;
	}

	/**
	 * returns the id of the Node
	 * @return
	 */
	public int id(){
		return _id;
	}
		
	@Override
	public String toString(){
		return ffapl.ast.FFaplASTreeConstants.FFT_NodeName[_id];
	}
	
}
