package ffapl.ast;

import java.util.Stack;
import ffapl.ast.nodes.interfaces.INode;
import ffapl.exception.FFaplException;
import ffapl.lib.interfaces.ICompilerError;

/**
 * FFapl Syntax Tree generation
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplASTree {
	
	private Stack<INode> _nodelist;
	private INode _currentscope;
	private INode _root;
	
	/**
	 * Constructor
	 */
	public FFaplASTree(){
		_nodelist = new Stack<INode>();
		_currentscope = null;
	}
	
	/**
	 * Opens the scope of a Node, each Node added in this scope will
	 * be a child of the specified <Code>node</Code>
	 * @param node
	 * @throws FFaplException
	 * 		(internal) if internal error occurs
	 */
	public void openScope(INode node) throws FFaplException{
		if(node == null){
			Object[] arguments = {"ASTTree openScope"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		if(_currentscope != null){
			node.setParent(_currentscope);
		}else if (_nodelist.empty()) {
			_root = node;
		}else{
			Object[] arguments = {"ASTTree openScope"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		_nodelist.push(node);
		_currentscope = node;
	}
	
	/**
	 * Close the scope of the Node
	 */
	public void closeScope(){
		_nodelist.pop();
		if(!_nodelist.empty()){
			_currentscope = _nodelist.peek();
		}else{
			_currentscope = null;
		}
	}
	
	/**
	 * returns the root node of the Tree
	 * @return
	 */
	public INode getRootNode(){
		return _root;
	}
	
	
}
