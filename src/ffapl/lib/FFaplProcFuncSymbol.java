/**
 * 
 */
package ffapl.lib;

import ffapl.ast.nodes.interfaces.INode;
import ffapl.lib.interfaces.IToken;
import ffapl.types.Type;

/**
 * Represent a procedure or function symbol
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplProcFuncSymbol extends FFaplSymbol {

	
	private INode _treeNode;
	
	/**
	 * 
	 * @param name
	 * @param token
	 * @param type
	 * @param kind
	 * @param node
	 */
	public FFaplProcFuncSymbol(String name, IToken token, Type type, int kind, INode node) {
		super(name, token, type, kind);
		_treeNode = node;
	}
	
	/**
	 * returns the Node of the Parse Tree
	 * @return
	 */
	public INode getTreeNode(){
		return _treeNode;
	}

}
