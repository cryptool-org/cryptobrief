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

/**
 * Defines a optional Node
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplNodeOpt implements INode{

	 private INode _node;
	 private INode _parent;
	
	 /**
	  * constructor
	  */
     public FFaplNodeOpt(){
    	 this(null);
     }
     
     /**
      * constructor
      * @param node
      */
     public FFaplNodeOpt(INode node){
    	 if(node != null){
    		 node.setParent(this);
    	 }
    	 _node = node;
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
      * returns if the Optional Node is present
      * @return
      */
     public boolean ispresent(){
    	 return _node != null;
     }
     
     /**
      * returns the Optional Node
      * @return
      */
     public INode getNode(){
    	 return _node;
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
	public INode getParent() {
		return _parent;
	}

	@Override
	public void setParent(INode parent) {
		_parent = parent;		
	}
	

}
