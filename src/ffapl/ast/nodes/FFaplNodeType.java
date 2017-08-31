/**
 * 
 */
package ffapl.ast.nodes;

import ffapl.ast.nodes.interfaces.INode;
import ffapl.exception.FFaplException;
import ffapl.visitor.interfaces.IRetArgVisitor;
import ffapl.visitor.interfaces.IRetVisitor;
import ffapl.visitor.interfaces.IVoidArgVisitor;
import ffapl.visitor.interfaces.IVoidVisitor;
import ffapl.types.*;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplNodeType implements INode {

	private INode _parent;
	private INode _node;
	private Type _type;
	
	
	public FFaplNodeType() {
		_type = null;
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#getParent()
	 */
	@Override
	public INode getParent() {
		return _parent;
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#setParent(ffapl.ast.nodes.interfaces.INode)
	 */
	@Override
	public void setParent(INode parent) {
		_parent = parent;
	}
	
	/**
     * adds the node to the Optional
     * @param node
     */
    public void addNode(INode node) {
   	    if (_node != null)
   	      throw new Error("Attempt to set optional node twice");
   	    _node = node;
    }
    
    /**
     * returns the Optional Node
     * @return
     */
    public INode getNode(){
   	 return _node;
    }
    
    public void setType(Type type){
    	_type = type;
    }
    
    public Type getType(){
    	return _type;
    }

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IVoidVisitor)
	 */
	@Override
	public void accept(IVoidVisitor visitor) throws FFaplException{
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IVoidArgVisitor, java.lang.Object)
	 */
	@Override
	public <A> void accept(IVoidArgVisitor<A> visitor, A argument) throws FFaplException{
		visitor.visit(this, argument);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IRetVisitor)
	 */
	@Override
	public <R> R accept(IRetVisitor<R> visitor) throws FFaplException{
		return visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IRetArgVisitor, java.lang.Object)
	 */
	@Override
	public <A, R> R accept(IRetArgVisitor<R, A> visitor, A argument) throws FFaplException{
		return visitor.visit(this, argument);
	}
	
	@Override
	public String toString(){
		return "NodeType";
	}

}
