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
 * Defines a Node with Choice Options
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplNodeChoice implements INode {

	private INode _node;
	private INode _parent;
	//which choice
	private int _pos;
	//how many choice possibilities
	private int _amount;
	
	 /**
	  * Constructor
	  * @param node
	  */
    public FFaplNodeChoice(INode node){
   	 this(node, -1, -1);
    }
    
   /**
    * constructor
    * @param node
    * @param pos
    * @param amount
    */
    public FFaplNodeChoice(INode node, int pos, int amount){
   	 	_node = node;
   	 	_pos = pos;
   	 	_amount = amount;
   	 	_node.setParent(this);
    }
    
    /**
     * returns the position of the choice
     * @return
     */
    public int getPos(){
    	return _pos;
    }
    
    /**
     * Returns the amount of choice options
     * @return
     */
    public int getAmount(){
    	return _amount;
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
