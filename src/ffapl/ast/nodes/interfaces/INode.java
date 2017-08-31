package ffapl.ast.nodes.interfaces;

import ffapl.exception.FFaplException;
import ffapl.visitor.interfaces.*;

public interface INode {
	
	/**
	 * returns the Parent of the Node
	 * @return
	 */
	public INode getParent();
	
	/**
	 * sets the parent of the Node
	 * @param parent
	 */
	public void setParent(INode parent);
	
	/**
	 * Accepts a visitor
	 * @param visitor
	 */
	public void accept(IVoidVisitor visitor) throws FFaplException;
	
	
	/**
	 * Accepts a visitor with arguments
	 * @param <A>
	 * @param visitor
	 * @param argument
	 */
	public <A> void accept(IVoidArgVisitor<A> visitor, A argument) throws FFaplException;
	
	/**
	 * Accepts a visitor with return value
	 * @param <R>
	 * @param visitor
	 * @return
	 */
	public <R> R accept(IRetVisitor<R> visitor) throws FFaplException;
	
	/**
	 * Accepts a visitor with return value and arguments
	 * @param <A>
	 * @param <R>
	 * @param visitor
	 * @param argument
	 * @return
	 */
	public <A, R> R accept(IRetArgVisitor<R,A> visitor, A argument) throws FFaplException;
	
	
}
